import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private Checker checker;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {
      @Override
      public Node parseTree(Node root) {
        return Parser.parseCompilationUnit(root, getClass().getClassLoader());
      }
    };

    nodeFactory = compiler.getFactory();
    checker = new Checker(new Diagnostics(), compiler);
  }

  @Test
  public void testCheckGlobalThis_NormalCase() {
    String source = "function f() { this.prop = 42; }";
    Node ast = compiler.parse(source);
    CheckGlobalThis checker = new CheckGlobalThis(checker, WARNING);
    NodeTraversal traversal = new NodeTraversal(ast, null, checker, null);
    traversal.traverseAllChildren();
    assertTrue(checker.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_Constructor() {
    String source = "function Foo() { this.prop = 42; }";
    Node ast = compiler.parse(source);
    CheckGlobalThis checker = new CheckGlobalThis(checker, WARNING);
    NodeTraversal traversal = new NodeTraversal(ast, null, checker, null);
    traversal.traverseAllChildren();
    assertTrue(checker.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_ThisAnnotatedFunction() {
    String source = "function Foo@this() { this.prop = 42; }";
    Node ast = compiler.parse(source);
    CheckGlobalThis checker = new CheckGlobalThis(checker, WARNING);
    NodeTraversal traversal = new NodeTraversal(ast, null, checker, null);
    traversal.traverseAllChildren();
    assertTrue(checker.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_PrototypeMethod() {
    String source = "var p = { prop: 0 }; function p.m() { this.prop = 43; }";
    Node ast = compiler.parse(source);
    CheckGlobalThis checker = new CheckGlobalThis(checker, WARNING);
    NodeTraversal traversal = new NodeTraversal(ast, null, checker, null);
    traversal.traverseAllChildren();
    assertTrue(checker.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_AssignLeftSide() {
    String source = "var a = this;";
    Node ast = compiler.parse(source);
    CheckGlobalThis checker = new CheckGlobalThis(checker, WARNING);
    NodeTraversal traversal = new NodeTraversal(ast, null, checker, null);
    traversal.traverseAllChildren();
    assertEquals(1, checker.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThis_AssignRightSide() {
    String source = "var a = {}; a.x = this;";
    Node ast = compiler.parse(source);
    CheckGlobalThis checker = new CheckGlobalThis(checker, WARNING);
    NodeTraversal traversal = new NodeTraversal(ast, null, checker, null);
    traversal.traverseAllChildren();
    assertTrue(checker.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_ThrowOnWarning() {
    String source = "function f() { this.prop = 42; }";
    CheckGlobalThis checker = new CheckGlobalThis(checker, ERROR);
    Node ast = compiler.parse(source);
    NodeTraversal traversal = new NodeTraversal(ast, null, checker, null);
    traversal.traverseAllChildren();
    assertEquals(1, checker.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThis_NestedAssign() {
    String source = "(a = this).prop = 42;";
    Node ast = compiler.parse(source);
    CheckGlobalThis checker = new CheckGlobalThis(checker, WARNING);
    NodeTraversal traversal = new NodeTraversal(ast, null, checker, null);
    traversal.traverseAllChildren();
    assertTrue(checker.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_GetProperty() {
    String source = "function f() { this.prop.subprop = 42; }";
    Node ast = compiler.parse(source);
    CheckGlobalThis checker = new CheckGlobalThis(checker, WARNING);
    NodeTraversal traversal = new NodeTraversal(ast, null, checker, null);
    traversal.traverseAllChildren();
    assertTrue(checker.getDiagnostics().isEmpty());
  }

  @Test(expected = AssertionError.class)
  public void testCheckGlobalThis_GetPrototypeProperty() {
    String source = "function f() { this.prototype.prop = 42; }";
    Node ast = compiler.parse(source);
    CheckGlobalThis checker = new CheckGlobalThis(checker, WARNING);
    NodeTraversal traversal = new NodeTraversal(ast, null, checker, null);
    traversal.traverseAllChildren();
  }
}