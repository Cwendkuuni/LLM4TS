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
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScopedAliasesTest {
  private static final String SOURCE_FILE_NAME = "MyFile.js";
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private ScopedAliases scopedAliases;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testProcessEmptyScript() {
    Node root = Node.newTextNode("", SOURCE_FILE_NAME, 1, 1);
    scopedAliases.process(null, root);

    // No errors expected.
    verify(compiler).reportCodeChange();
  }

  @Test
  public void testProcessEmptyScriptWithAlias() {
    Node root = Node.newCall(Token.NAME, "goog.scope", null, null, 1, 1, SOURCE_FILE_NAME, 1, 1);
    root.addChild(Node.newFunction(null, null, null, null, null, 2, 2, 2, SOURCE_FILE_NAME, 1, 2));
    scopedAliases.process(null, root);

    // No errors expected.
    verify(compiler).reportCodeChange();
  }

  @Test
  public void testProcessInvalidScopeCall() {
    Node root = Node.newCall(Token.NAME, "goog.scope", null, null, 1, 1, SOURCE_FILE_NAME, 1, 1);
    root.addChild(Node.newFunction(null, null, null, null, null, 2, 2, 2, SOURCE_FILE_NAME, 1, 2));
    root.addChild(Node.newCall(Token.NAME, "goog.scope", null, null, 1, 3, SOURCE_FILE_NAME, 1, 3));
    scopedAliases.process(null, root);

    ArgumentCaptor<DiagnosticType> errorCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    verify(compiler).report(eq(0), errorCaptor.capture(), containsString("goog.scope must be alone in a single statement."));
  }

  @Test
  public void testProcessInvalidScopeCallWithArguments() {
    Node root = Node.newCall(Token.NAME, "goog.scope", null, null, 1, 1, SOURCE_FILE_NAME, 1, 1);
    root.addChild(Node.newFunction(null, null, null, null, null, 2, 2, 2, SOURCE_FILE_NAME, 1, 2));
    root.addChild(Node.newCall(Token.NAME, "goog.scope", new Node[] {Node.newArrayLiteral(new Node[] {
        Node.newTextNode("arg1", SOURCE_FILE_NAME, 1, 3),
        Node.newTextNode("arg2", SOURCE_FILE_NAME, 1, 4)
    })}, null, null, null, null, 1, 4, SOURCE_FILE_NAME, 1, 4));
    scopedAliases.process(null, root);

    ArgumentCaptor<DiagnosticType> errorCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    verify(compiler).report(eq(0), errorCaptor.capture(), containsString("The call to goog.scope must take only a single parameter."));
  }

  @Test
  public void testProcessInvalidScopeCallWithReturnStatement() {
    Node root = Node.newCall(Token.NAME, "goog.scope", null, null, 1, 1, SOURCE_FILE_NAME, 1, 1);
    root.addChild(Node.newFunction(null, null, null, null, null, 2, 2, 2, SOURCE_FILE_NAME, 1, 2));
    root.addChild(Node.newReturnStatement(Node.newName("x"), null, null, 1, 3, SOURCE_FILE_NAME, 1, 3));
    scopedAliases.process(null, root);

    ArgumentCaptor<DiagnosticType> errorCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    verify(compiler).report(eq(0), errorCaptor.capture(), containsString("The body of a goog.scope function cannot use 'return'."));
  }

  @Test
  public void testProcessInvalidScopeCallWithThrowStatement() {
    Node root = Node.newCall(Token.NAME, "goog.scope", null, null, 1, 1, SOURCE_FILE_NAME, 1, 1);
    root.addChild(Node.newFunction(null, null, null, null, null, 2, 2, 2, SOURCE_FILE_NAME, 1, 2));
    root.addChild(Node.newThrowStatement(new Node[] {Node.newTextNode("error", SOURCE_FILE_NAME, 1, 3)}, null, null, 1, 3, SOURCE_FILE_NAME, 1, 3));
    scopedAliases.process(null, root);

    ArgumentCaptor<DiagnosticType> errorCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    verify(compiler).report(eq(0), errorCaptor.capture(), containsString("The body of a goog.scope function cannot use 'throw'."));
  }

  @Test
  public void testProcessAliasRedefinition() {
    Node root = Node.newCall(Token.NAME, "goog.scope", null, null, 1, 1, SOURCE_FILE_NAME, 1, 1);
    root.addChild(Node.newFunction(null, null, null, null, null, 2, 2, 2, SOURCE_FILE_NAME, 1, 2));
    root.addChild(Node.newVariableDeclaration(Token.VAR, "x", Node.newName("goog.x"), null, null, 1, 3, SOURCE_FILE_NAME, 1, 3));
    root.addChild(Node.newVariableDeclaration(Token.VAR, "x", Node.newName("1"), null, null, 1, 4, SOURCE_FILE_NAME, 1, 4));
    scopedAliases.process(null, root);

    ArgumentCaptor<DiagnosticType> errorCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    verify(compiler).report(eq(0), errorCaptor.capture(), containsString("The alias x is assigned a value more than once."));
  }

  @Test
  public void testProcessNonAliasLocalVariable() {
    Node root = Node.newCall(Token.NAME, "goog.scope", null, null, 1, 1, SOURCE_FILE_NAME, 1, 1);
    root.addChild(Node.newFunction(null, null, null, null, null, 2, 2, 2, SOURCE_FILE_NAME, 1, 2));
    root.addChild(Node.newVariableDeclaration(Token.VAR, "x", Node.newName("x"), null, null, 1, 3, SOURCE_FILE_NAME, 1, 3));
    scopedAliases.process(null, root);

    ArgumentCaptor<DiagnosticType> errorCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    verify(compiler).report(eq(0), errorCaptor.capture(), containsString("The local variable x is in a goog.scope and is not an alias."));
  }

  @Test
  public void testApplyAliases() {
    Node root = Node.newCall(Token.NAME, "goog.scope", null, null, 1, 1, SOURCE_FILE_NAME, 1, 1);
    root.addChild(Node.newFunction(null, null, null, null, null, 2, 2, 2, SOURCE_FILE_NAME, 1, 2));

    // Define some aliases.
    Node aliasDefinition1 = Node.newVariableDeclaration(Token.VAR, "dom", Node.newName("goog.dom"), null, null, 1, 3, SOURCE_FILE_NAME, 1, 3);
    root.addChild(aliasDefinition1);
    Node aliasDefinition2 = Node.newVariableDeclaration(Token.VAR, "DIV", Node.newName("goog.dom.TagName.DIV"), null, null, 1, 4, SOURCE_FILE_NAME, 1, 4);
    root.addChild(aliasDefinition2);

    // Use the aliases.
    Node usage1 = Node.newCall(Token.NAME, "dom", null, null, 1, 5, SOURCE_FILE_NAME, 1, 5);
    root.addChild(usage1);
    Node usage2 = Node.newCall(Token.NAME, "DIV", null, null, 1, 6, SOURCE_FILE_NAME, 1, 6);
    root.addChild(usage2);

    List<Node> aliasDefinitionsInOrder = Lists.newArrayList();
    List<AliasUsage> aliasUsages = Lists.newArrayList();
    List<Node> scopeCalls = Lists.newArrayList();

    scopedAliases.process(null, root);

    // Verify that the aliases were applied.
    assertEquals(2, aliasDefinitionsInOrder.size());
    assertEquals("goog.dom", aliasDefinitionsInOrder.get(0).getString());
    assertEquals("goog.dom.TagName.DIV", aliasDefinitionsInOrder.get(1).getString());

    assertEquals(2, aliasUsages.size());
    AliasUsage aliasUsage1 = aliasUsages.get(0);
    assertSame(aliasDefinition1, ((AliasedNode) aliasUsage1).aliasDefinition);
    AliasUsage aliasUsage2 = aliasUsages.get(1);
    assertSame(aliasDefinition2, ((AliasedTypeNode) aliasUsage2).aliasDefinition);

    // Verify that the alias definitions were removed.
    assertEquals(2, root.getChildCount());
    assertEquals(0, root.getLastChild().getChildCount());

    // Verify that the scopes were collapsed.
    assertEquals(2, scopeCalls.size());
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        NodeUtil.toSourceString(root.getChildAtIndex(0).getChildAtIndex(1).getLastChild()));
  }
}

class NodeUtil {
  public static String toSourceString(Node node) {
    StringBuilder sb = new StringBuilder();
    Traversal.printNode(node, sb, 0);
    return sb.toString();
  }
}