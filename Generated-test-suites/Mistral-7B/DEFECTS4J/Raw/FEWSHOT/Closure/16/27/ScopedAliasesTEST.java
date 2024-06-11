package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal.ScopeCall;
import com.google.javascript.jscomp.Traversal.Traversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

/**
 * Test cases for {@link ScopedAliases} class.
 */
public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);

    MockitoAnnotations.initMocks(this);

    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testProcessNoAliases() {
    Node root = Node.makeScript("");
    scopedAliases.process(Node.DOCUMENT, root);

    // No alias definitions, scope calls or errors should be reported.
    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testProcessAliases() {
    // Create some nodes representing the following code:
    //
    // goog.scope(function() {
    //   var dom = goog.dom;
    //   var DIV = dom.TagName.DIV;
    //
    //   dom.createElement(DIV);
    // });

    Node root = Node.makeScript("goog.scope(function() { var dom = goog.dom; var DIV = dom.TagName.DIV; dom.createElement(DIV); })");

    Node scopeCall = root.getFirstChild();
    Node scopeBlock = scopeCall.getLastChild();
    Node functionNode = scopeBlock.getFirstChild();
    Node varDomNode = functionNode.getChildAtIndex(1);
    Node varDIVNode = functionNode.getChildAtIndex(4);

    Node aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitionsInOrder.size());

    Node aliasDomDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("dom", aliasDomDefinition.getString());
    Node aliasDOMNode = aliasDomDefinition.getLastChild();
    assertEquals("goog.dom", aliasDOMNode.getQualifiedName());

    Node aliasDIVDefinition = aliasDefinitionsInOrder.get(1);
    assertEquals("DIV", aliasDIVDefinition.getString());
    Node aliasDIVNode = aliasDIVDefinition.getLastChild();
    assertEquals("goog.dom.TagName.DIV", aliasDIVNode.getQualifiedName());

    Scope scope = NodeUtil.getScope(scopeCall);
    Var domVar = scope.getVar("dom");
    Var DIVVar = scope.getVar("DIV");
    assertEquals("dom", domVar.getName());
    assertEquals("goog.dom", domVar.getInitialValue().getQualifiedName());
    assertEquals("DIV", DIVVar.getName());
    assertEquals("goog.dom.TagName.DIV", DIVVar.getInitialValue().getQualifiedName());

    // No aliasUsages or errors should be reported.
    assertEquals(0, scopedAliases.getAliasUsages().size());

    // No scopeCalls should be reported.
    assertEquals(0, scopedAliases.getScopeCalls().size());

    // The scopes should have been collapsed.
    assertEquals(6, root.getChildCount());
    NodeElement element = root.getChildAtIndex(0);
    assertEquals("goog.scope", element.getNodeType());
    assertEquals(5, element.getChildCount());
    Node functionNodeCollapsed = element.getChildAtIndex(0);
    assertEquals("function () {", functionNodeCollapsed.getString());
    Node varDomCollapsed = functionNodeCollapsed.getChildAtIndex(1);
    assertEquals("var dom", varDomCollapsed.getString());
    Node varDIVCollapsed = functionNodeCollapsed.getChildAtIndex(4);
    assertEquals("var DIV", varDIVCollapsed.getString());
    Node elementCreate = functionNodeCollapsed.getChildAtIndex(5);
    assertEquals("dom.createElement(DIV)", elementCreate.getString());
  }

  @Test
  public void testProcessAliasesWithErrors() {
    // Create some nodes representing the following code:
    //
    // goog.scope(function() {
    //   var this = goog.this;
    //   var returnVal = goog.return;
    //   throw new Error();
    // });

    Node root = Node.makeScript("goog.scope(function() { var this = goog.this; var returnVal = goog.return; throw new Error(); })");

    scopedAliases.process(Node.DOCUMENT, root);

    // One error should be reported.
    assertEquals(1, root.getErrorCount());

    // No alias definitions, aliasUsages or scopeCalls should be reported.
    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testProcessDuplicateAliases() {
    // Create some nodes representing the following code:
    //
    // goog.scope(function() {
    //   var dom = goog.dom;
    //   var DIV = dom.TagName.DIV;
    //   var DIV = goog.dom.TagName.DIV;
    //
    //   dom.createElement(DIV);
    // });

    Node root = Node.makeScript("goog.scope(function() { var dom = goog.dom; var DIV = dom.TagName.DIV; var DIV = goog.dom.TagName.DIV; dom.createElement(DIV); })");

    scopedAliases.process(Node.DOCUMENT, root);

    // One error should be reported.
    assertEquals(1, root.getErrorCount());

    // No alias definitions, aliasUsages or scopeCalls should be reported.
    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testProcessAliasesWithLocalVariables() {
    // Create some nodes representing the following code:
    //
    // goog.scope(function() {
    //   var dom = goog.dom;
    //   var x = 1;
    //   var DIV = dom.TagName.DIV;
    //
    //   dom.createElement(DIV);
    // });

    Node root = Node.makeScript("goog.scope(function() { var dom = goog.dom; var x = 1; var DIV = dom.TagName.DIV; dom.createElement(DIV); })");

    Node scopeCall = root.getFirstChild();
    Node scopeBlock = scopeCall.getLastChild();
    Node functionNode = scopeBlock.getFirstChild();

    scopedAliases.process(Node.DOCUMENT, root);

    // No alias definitions, aliasUsages or errors should be reported.
    assertEquals(0, root.getChildCount());

    // The local variable 'x' should not have been reported as an alias.
    Var xVar = NodeUtil.getScope(scopeCall).getVar("x");
    assertNull(xVar);
  }

  @Test
  public void testProcessAliasesWithBleedingFunctions() {
    // Create some nodes representing the following code:
    //
    // (function() {
    //   goog.scope(function() {
    //     goog.scope(function() {});
    //   });
    // })();

    Node root = Node.makeScript("(function() { goog.scope(function() { goog.scope(function() {}); }); })()");

    scopedAliases.process(Node.DOCUMENT, root);

    // No alias definitions, aliasUsages or errors should be reported.
    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testProcessAliasesWithParameters() {
    // Create some nodes representing the following code:
    //
    // goog.scope(function(name) {
    //   var DIV = goog.dom.TagName.DIV;
    //
    //   function foo(name, value) {
    //     name = value;
    //   }
    //
    //   foo("bar", "baz");
    // });

    Node root = Node.makeScript("goog.scope(function(name) { var DIV = goog.dom.TagName.DIV; function foo(name, value) { name = value; } foo(\"bar\", \"baz\"); })");

    Node scopeCall = root.getFirstChild();
    Node scopeBlock = scopeCall.getLastChild();
    Node functionNode = scopeBlock.getFirstChild();

    scopedAliases.process(Node.DOCUMENT, root);

    // No alias definitions, aliasUsages or errors should be reported.
    assertEquals(0, root.getChildCount());

    // The formal parameter 'name' should not have been reported as an alias.
    Var nameVar = NodeUtil.getScope(scopeCall).getVar("name");
    assertNull(nameVar);
  }

  @Test
  public void testProcessAliasesWithReturnStatements() {
    // Create some nodes representing the following code:
    //
    // goog.scope(function() {
    //   var DIV = goog.dom.TagName.DIV;
    //
    //   function foo() {
    //     return goog.dom.createElement(DIV);
    //   }
    //
    //   return foo();
    // });

    Node root = Node.makeScript("goog.scope(function() { var DIV = goog.dom.TagName.DIV; function foo() { return goog.dom.createElement(DIV); } return foo(); })");

    scopedAliases.process(Node.DOCUMENT, root);

    // No alias definitions, aliasUsages or errors should be reported.
    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testProcessAliasesWithThrowStatements() {
    // Create some nodes representing the following code:
    //
    // goog.scope(function() {
    //   var DIV = goog.dom.TagName.DIV;
    //
    //   function foo() {
    //     throw new Error();
    //   }
    //
    //   foo();
    // });

    Node root = Node.makeScript("goog.scope(function() { var DIV = goog.dom.TagName.DIV; function foo() { throw new Error(); } foo(); })");

    scopedAliases.process(Node.DOCUMENT, root);

    // No alias definitions, aliasUsages or errors should be reported.
    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testProcessAliasesWithNestedScopes() {
    // Create some nodes representing the following code:
    //
    // goog.scope(function() {
    //   goog.scope(function() {
    //     var DIV = goog.dom.TagName.DIV;
    //
    //     function foo() {
    //       return goog.dom.createElement(DIV);
    //     }
    //
    //     return foo();
    //   });
    // });

    Node root = Node.makeScript("goog.scope(function() { goog.scope(function() { var DIV = goog.dom.TagName.DIV; function foo() { return goog.dom.createElement(DIV); } return foo(); })})");

    Node scopeCall = root.getFirstChild();
    Node scopeBlock1 = scopeCall.getLastChild();
    Node scopeCall2 = scopeBlock1.getLastChild();
    Node functionNode = scopeCall2.getLastChild();

    scopedAliases.process(Node.DOCUMENT, root);

    // No alias definitions, aliasUsages or errors should be reported.
    assertEquals(0, root.getChildCount());

    // The scope blocks should have been collapsed.
    assertEquals(5, root.getChildCount());
    NodeElement elementScope1 = root.getChildAtIndex(0);
    assertEquals("goog.scope", elementScope1.getNodeType());
    assertEquals(1, elementScope1.getChildCount());
    Node functionNodeCollapsed = elementScope1.getChildAtIndex(0);
    assertEquals("function () {", functionNodeCollapsed.getString());
    Node scopeBlock2 = functionNodeCollapsed.getLastChild();
    assertEquals(5, scopeBlock2.getChildCount());
    NodeElement elementScope2 = scopeBlock2.getChildAtIndex(0);
    assertEquals("function () {", elementScope2.getNodeType());
    assertEquals(1, elementScope2.getChildCount());
    Node functionNodeCollapsed2 = elementScope2.getChildAtIndex(0);
    assertEquals("function foo() { return goog.dom.createElement(DIV); }", functionNodeCollapsed2.getString());
    Node returnStatement = functionNodeCollapsed2.getLastChild();
    assertEquals("return", returnStatement.getString());
  }
}