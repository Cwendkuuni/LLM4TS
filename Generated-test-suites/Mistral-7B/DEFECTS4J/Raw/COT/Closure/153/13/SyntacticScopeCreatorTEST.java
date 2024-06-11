import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopeCreator.VAR_MULTIPLE_DECLARED_ERROR;
import static com.google.javascript.jscomp.ScopeCreator.VAR_ARGUMENTS_SHADOWED_ERROR;
import static com.google.javascript.rhino.JSDocInfo.Suppressions.duplicate;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.ScopeCreator.RedeclarationHandler;
import com.google.javascript.jscomp.ScopeCreator.DefaultRedeclarationHandler;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSTypeFactory;

import org.junit.Test;

public class SyntacticScopeCreatorTest {

  @Test
  public void testCreateScope() {
    AbstractCompiler compiler = new NoOpCompiler();
    RedeclarationHandler redeclarationHandler = new DefaultRedeclarationHandler();
    ScopeCreator syntacticScopeCreator = new SyntacticScopeCreator(compiler, redeclarationHandler);

    Node rootNode = createMockNode("function foo() {}");
    Scope scope = syntacticScopeCreator.createScope(rootNode, null);
    assertEquals(0, scope.size());

    rootNode = createMockNode("function foo() { var x; }");
    scope = syntacticScopeCreator.createScope(rootNode, null);
    assertEquals(1, scope.size());
    assertEquals("x", scope.getVarNames().get(0));

    rootNode = createMockNode("function foo() { var x = 1; }");
    scope = syntacticScopeCreator.createScope(rootNode, null);
    assertEquals(1, scope.size());
    assertEquals("x", scope.getVarNames().get(0));
    assertEquals(JSTypeFactory.intType(), scope.getVarType("x"));

    rootNode = createMockNode("function foo() { var x; var x; }");
    scope = syntacticScopeCreator.createScope(rootNode, null);
    assertEquals(1, scope.size());
    assertEquals("x", scope.getVarNames().get(0));

    rootNode = createMockNode("function foo(x) { var x; }");
    scope = syntacticScopeCreator.createScope(rootNode, null);
    assertEquals(1, scope.size());
    assertEquals("x", scope.getVarNames().get(0));

    rootNode = createMockNode("function foo(x) { var y = 1; }");
    scope = syntacticScopeCreator.createScope(rootNode, null);
    assertEquals(2, scope.size());
    assertEquals("y", scope.getVarNames().get(1));
    assertEquals(JSTypeFactory.intType(), scope.getVarType("y"));

    rootNode = createMockNode("function foo(x, y) { var x; }");
    scope = syntacticScopeCreator.createScope(rootNode, null);
    assertEquals(2, scope.size());
    assertEquals("x", scope.getVarNames().get(0));
    assertEquals("y", scope.getVarNames().get(1));

    rootNode = createMockNode("function foo(x, y) { var x = 1; }");
    scope = syntacticScopeCreator.createScope(rootNode, null);
    assertEquals(1, scope.size());
    assertEquals("x", scope.getVarNames().get(0));
    assertEquals(JSTypeFactory.intType(), scope.getVarType("x"));
  }

  @Test(expected = AssertionError.class)
  public void testCreateScopeWithMultipleDeclarationAtTopLevel() {
    AbstractCompiler compiler = new NoOpCompiler();
    RedeclarationHandler redeclarationHandler = new DefaultRedeclarationHandler();
    ScopeCreator syntacticScopeCreator = new SyntacticScopeCreator(compiler, redeclarationHandler);

    Node rootNode = createMockNode("(function() { var x; var x; })()");
    syntacticScopeCreator.createScope(rootNode, null);
  }

  @Test(expected = AssertionError.class)
  public void testCreateScopeWithArgumentsVariableDeclaration() {
    AbstractCompiler compiler = new NoOpCompiler();
    RedeclarationHandler redeclarationHandler = new DefaultRedeclarationHandler();
    ScopeCreator syntacticScopeCreator = new SyntacticScopeCreator(compiler, redeclarationHandler);

    Node rootNode = createMockNode("function foo(x) { var arguments; }");
    syntacticScopeCreator.createScope(rootNode, null);
  }

  @Test(expected = AssertionError.class)
  public void testCreateScopeWithArgumentsVariableDeclarationAndRedeclaration() {
    AbstractCompiler compiler = new NoOpCompiler();
    RedeclarationHandler redeclarationHandler = new DefaultRedeclarationHandler() {
      @Override
      public void onRedeclaration(Scope s, String name, Node n, Node parent, Node gramps, Node nodeWithLineNumber) {
        fail("Should not reach here");
      }
    };
    ScopeCreator syntacticScopeCreator = new SyntacticScopeCreator(compiler, redeclarationHandler);

    Node rootNode = createMockNode("function foo(x) { var arguments; var arguments; }");
    syntacticScopeCreator.createScope(rootNode, null);
  }

  @Test
  public void testCreateScopeWithRedeclaration() {
    AbstractCompiler compiler = new NoOpCompiler();
    RedeclarationHandler redeclarationHandler = new DefaultRedeclarationHandler();
    ScopeCreator syntacticScopeCreator = new SyntacticScopeCreator(compiler, redeclarationHandler);

    Node rootNode = createMockNode("function foo() { var x; }");
    Scope parentScope = new Scope(null, null);
    Scope childScope = syntacticScopeCreator.createScope(rootNode, parentScope);

    rootNode = createMockNode("function bar() { var x; }");
    Scope grandParentScope = new Scope(null, null);
    Scope parentScope2 = new Scope(grandParentScope, rootNode);
    Scope childScope2 = syntacticScopeCreator.createScope(rootNode, parentScope2);

    // x in bar scope should not be visible in foo scope
    assertFalse(childScope.getVar("x").isVisibleTo(childScope2));

    // x in foo scope should be visible in bar scope
    assertTrue(childScope.getVar("x").isVisibleTo(childScope2));
  }

  @Test
  public void testCreateScopeWithCatchVariableDeclaration() {
    AbstractCompiler compiler = new NoOpCompiler();
    RedeclarationHandler redeclarationHandler = new DefaultRedeclarationHandler();
    ScopeCreator syntacticScopeCreator = new SyntacticScopeCreator(compiler, redeclarationHandler);

    Node rootNode = createMockNode("function foo() { try { throw new Error(); } catch (x) {} }");
    Scope scope = syntacticScopeCreator.createScope(rootNode, null);

    assertEquals(1, scope.size());
    assertEquals("x", scope.getVarNames().get(0));
  }

  @Test
  public void testCreateScopeWithGlobalVariableDeclaration() {
    AbstractCompiler compiler = new NoOpCompiler();
    RedeclarationHandler redeclarationHandler = new DefaultRedeclarationHandler();
    ScopeCreator syntacticScopeCreator = new SyntacticScopeCreator(compiler, redeclarationHandler);

    Node rootNode = createMockNode("var x = 1; function foo() {}");
    Scope scope = syntacticScopeCreator.createScope(rootNode, null);

    assertEquals(1, scope.size());
    assertEquals("x", scope.getVarNames().get(0));
    assertEquals(JSTypeFactory.intType(), scope.getVarType("x"));
  }

  @Test
  public void testCreateScopeWithGlobalVariableDeclarationAndReddeclaration() {
    AbstractCompiler compiler = new NoOpCompiler();
    RedeclarationHandler redeclarationHandler = new DefaultRedeclarationHandler() {
      @Override
      public void onRedeclaration(Scope s, String name, Node n, Node parent, Node gramps, Node nodeWithLineNumber) {
        fail("Should not reach here");
      }
    };
    ScopeCreator syntacticScopeCreator = new SyntacticScopeCreator(compiler, redeclarationHandler);

    Node rootNode = createMockNode("var x = 1; var x = 2; function foo() {}");
    Scope scope = syntacticScopeCreator.createScope(rootNode, null);

    assertEquals(1, scope.size());
    assertEquals("x", scope.getVarNames().get(0));
    assertEquals(JSTypeFactory.numberType(), scope.getVarType("x"));
    assertEquals(2, ((JSType) scope.getVarType("x")).getValue().intValue());
  }

  @Test(expected = AssertionError.class)
  public void testCreateScopeWithGlobalVariableDeclarationAndDuplicateSuppression() {
    AbstractCompiler compiler = new NoOpCompiler();
    RedeclarationHandler redeclarationHandler = new DefaultRedeclarationHandler() {
      @Override
      public void onRedeclaration(Scope s, String name, Node n, Node parent, Node gramps, Node nodeWithLineNumber) {
        if (name.equals("x")) {
          JSDocInfo jsdocInfo = nodeWithLineNumber.getJSDocInfo();
          assertEquals(duplicate, jsdocInfo.getSuppressions());
        } else {
          fail("Unexpected name: " + name);
        }
      }
    };
    ScopeCreator syntacticScopeCreator = new SyntacticScopeCreator(compiler, redeclarationHandler);

    Node rootNode = createMockNode("var x = 1; var x = 2; function foo() {}");
    Scope scope = syntacticScopeCreator.createScope(rootNode, null);

    assertEquals(1, scope.size());
    assertEquals("x", scope.getVarNames().get(0));
    assertEquals(JSTypeFactory.numberType(), scope.getVarType("x"));
    assertEquals(2, ((JSType) scope.getVarType("x")).getValue().intValue());
  }

  private Node createMockNode(String code) {
    Node node = new Node();
    node.setSource(code.toCharArray(), 0, code.length());
    node.setType(Token.SCRIPT);
    return node;
  }

  private static class NoOpCompiler implements AbstractCompiler {}
}