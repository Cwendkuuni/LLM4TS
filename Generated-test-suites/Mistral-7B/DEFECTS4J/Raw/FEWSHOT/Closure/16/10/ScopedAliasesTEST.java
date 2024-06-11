package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.AliasUsage;
import com.google.javascript.jscomp.Traversal.ScopedCallback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {
  private Compiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    compiler = new Compiler();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    transformationHandler = new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(
          String fileName, SourcePosition position) {
        return new AliasTransformation(fileName, position, null);
      }
    };
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testValidGoogScopeCall() {
    Node root = Node.parse("goog.scope(function() {" +
        "var x = goog.x;" +
        "})");
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();

    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals("goog.x", traversal.getAliasDefinitionsInOrder().get(0).getNode().getString());
  }

  @Test
  public void testGoogScopeEmptyParameters() {
    Node root = Node.parse("goog.scope()");
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();

    assertFalse(traversal.hasErrors());
  }

  @Test
  public void testGoogScopeBadParameters() {
    Node root = Node.parse("goog.scope(123)");
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();

    assertEquals(1, traversal.getErrors().size());
    assertEquals("goog.scope must take only a single parameter. It must be an anonymous function that itself takes no parameters.",
        traversal.getErrors().get(0).getMessage());
  }

  @Test
  public void testGoogScopeRefersToThis() {
    Node root = Node.parse("goog.scope(function() { this.x = 123; })");
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();

    assertEquals(1, traversal.getErrors().size());
    assertEquals("The body of a goog.scope function cannot reference 'this'.", traversal.getErrors().get(0).getMessage());
  }

  @Test
  public void testGoogScopeUsesReturn() {
    Node root = Node.parse("goog.scope(function() { return 123; })");
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();

    assertEquals(1, traversal.getErrors().size());
    assertEquals("The body of a goog.scope function cannot use 'return'.", traversal.getErrors().get(0).getMessage());
  }

  @Test
  public void testGoogScopeUsesThrow() {
    Node root = Node.parse("goog.scope(function() { throw new Error(); })");
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();

    assertEquals(1, traversal.getErrors().size());
    assertEquals("The body of a goog.scope function cannot use 'throw'.", traversal.getErrors().get(0).getMessage());
  }

  @Test
  public void testGoogScopeAliasRedefined() {
    Node root = Node.parse("goog.scope(function() {" +
        "var x = goog.x;" +
        "var x = 123;" +
        "})");
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();

    assertEquals(1, traversal.getErrors().size());
    assertEquals("The alias x is assigned a value more than once.", traversal.getErrors().get(0).getMessage());
  }

  @Test
  public void testGoogScopeNonAliasLocal() {
    Node root = Node.parse("goog.scope(function() {" +
        "var x = 123;" +
        "})");
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();

    assertEquals(1, traversal.getErrors().size());
    assertEquals("The local variable x is in a goog.scope and is not an alias.", traversal.getErrors().get(0).getMessage());
  }

  @Test
  public void testGoogScopeNoError() {
    Node root = Node.parse("goog.scope(function() {" +
        "var x = goog.myAlias;" +
        "var y = goog.otherAlias;" +
        "})");
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();

    assertFalse(traversal.hasErrors());
  }

  @Test
  public void testGoogScopeWithJSDoc() {
    Node root = Node.parse("/**" +
        " * goog.scope function." +
        " */" +
        "goog.scope(function() {" +
        "  var x = goog.myAlias;" +
        "})");
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();

    assertFalse(traversal.hasErrors());
  }

  @Test
  public void testGoogScopeNoAliasDefinition() {
    Node root = Node.parse("goog.scope(function() {" +
        "var x = 123;" +
        "})");
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();

    assertTrue(traversal.getAliasDefinitionsInOrder().isEmpty());
  }

  @Test
  public void testGoogScopeWithAliasDefinition() {
    Node root = Node.parse("goog.scope(function() {" +
        "var myAlias = goog.myAlias;" +
        "})");
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();

    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals("goog.myAlias", traversal.getAliasDefinitionsInOrder().get(0).getNode().getString());
  }

  @Test
  public void testApplyAlias() {
    Node root = Node.parse("goog.scope(function() {" +
        "var myAlias = goog.myAlias;" +
        "goog.myAlias.doSomething();" +
        "})");
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();

    AliasUsage aliasUsage = traversal.getAliasUsages().get(0);

    Node aliasDefinition = aliasUsage.getNode();
    Node originalNode = aliasUsage.getOriginalNode();

    assertEquals("goog.myAlias", aliasDefinition.getString());
    assertEquals("goog", originalNode.getString());
  }

  @Test
  public void testApplyAliasType() {
    Node root = Node.parse("goog.scope(function() {" +
        "TypeAlias typeAlias = goog.myTypeAlias;" +
        "})");
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();

    AliasUsage aliasUsage = traversal.getAliasUsages().get(0);

    Node aliasDefinition = aliasUsage.getNode();
    Node originalNode = aliasUsage.getOriginalNode();

    assertEquals("goog.myTypeAlias", aliasDefinition.getString());
    assertEquals("TypeAlias", originalNode.getString());
  }

  @Test
  public void testApplyAliasesInOrder() {
    Node root = Node.parse("goog.scope(function() {" +
        "TypeAlias a = goog.a;" +
        "TypeAlias b = goog.b;" +
        "TypeAlias c = goog.c;" +
        "})");
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();

    List<String> expectedAliasOrder = Lists.newArrayList("goog.a", "goog.b", "goog.c");

    for (AliasUsage aliasUsage : traversal.getAliasUsages()) {
      Node aliasDefinition = aliasUsage.getNode();
      assertEquals(expectedAliasOrder.remove(0), aliasDefinition.getString());
    }

    assertTrue(expectedAliasOrder.isEmpty());
  }

  @Test
  public void testCollapseScope() {
    Node root = Node.parse("goog.scope(function() {" +
        "var myAlias = goog.myAlias;" +
        "goog.myAlias.doSomething();" +
        "goog.scope(function() {" +
        "  var myAlias = 123;" +
        "  goog.myAlias.doSomething();" +
        "})" +
        "})");
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();

    List<Node> scopeCalls = traversal.getScopeCalls();

    assertEquals(2, scopeCalls.size());

    Node firstScopeCall = scopeCalls.get(0);
    Node secondScopeCall = scopeCalls.get(1);

    assertEquals(1, firstScopeCall.getChildCount());
    assertEquals(1, secondScopeCall.getChildCount());

    Node firstAliasDefinition = traversal.getAliasDefinitionsInOrder().get(0);
    Node secondAliasDefinition = traversal.getAliasDefinitionsInOrder().get(1);

    assertEquals(2, traversal.getAliasDefinitionsInOrder().size());

    Node firstScopeBlock = firstScopeCall.getLastChild();
    Node secondScopeBlock = secondScopeCall.getLastChild();

    assertEquals(firstScopeBlock.getChildCount(), 1);
    assertEquals(secondScopeBlock.getChildCount(), 1);

    Node firstScopeClosure = firstScopeBlock.getLastChild();
    Node secondScopeClosure = secondScopeBlock.getLastChild();

    assertSame(firstScopeClosure, secondScopeClosure);
  }

  @Test
  public void testRenameNamespaceShadows() {
    Node root = Node.parse("goog.scope(function() {" +
        "var goog = { foo: 123, bar: 456 };" +
        "var aliased = goog.foo;" +
        "goog.bar = 789;" +
        "})");
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();

    Map<String, Var> aliases = traversal.getAliases();

    Var aliasVar = aliases.get("goog");
    assertNull(aliasVar);

    Var aliasVar2 = aliases.get("foo");
    assertSame("goog__foo", aliasVar2.getName());

    Node aliasedNode = aliasVar2.getInitialValue();
    assertEquals("goog__foo", aliasedNode.getString());
  }
}