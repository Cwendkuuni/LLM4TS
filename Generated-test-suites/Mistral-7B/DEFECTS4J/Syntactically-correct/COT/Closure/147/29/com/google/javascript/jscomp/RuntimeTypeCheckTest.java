package com.google.javascript.jscomp; // adjust package if necessary

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.UnionType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RuntimeTypeCheckTest {
  private AbstractCompiler compiler;
  private RuntimeTypeCheck runtimeTypeCheck;

  @Before
  public void setUp() {
    compiler = new CompilerOptions();
    runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
  }

  @Test
  public void testProcess_ValidFunction() {
    Node root = Node.newScript(
        "function Foo(x) { return x; }");
    runtimeTypeCheck.process(null, root);
    NodeTraversal.traverse(compiler, root, new NodePrintVisitor());

    // The output should contain the checks and markers for Foo's constructor
    // and its parameter x.
  }

  @Test(expected = RuntimeException.class)
  public void testProcess_NullCompiler() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, null);
    Node root = Node.newScript("function Foo() {}");
    runtimeTypeCheck.process(null, root);
  }

  @Test
  public void testAddMarkers_FunctionType() {
    FunctionType funcType = new FunctionType("Foo", ImmutableList.of(), new ObjectType("Object"));
    Node root = Node.newScript("function Foo() {}");
    NodeTraversal traverser = new NodeTraversal(compiler, root);
    new AddMarkers(compiler).visitAll(traverser, root.getFirstChild());
    NodeTraversal.traverse(compiler, root, new NodePrintVisitor());

    // The output should contain the class marker for Foo.
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMarkers_InvalidFunctionType() {
    FunctionType funcType = new FunctionType("Foo", ImmutableList.of(), null);
    Node root = Node.newScript("function Foo() {}");
    NodeTraversal traverser = new NodeTraversal(compiler, root);
    new AddMarkers(compiler).visitAll(traverser, root.getFirstChild());
  }

  @Test
  public void testAddChecks_FunctionType() {
    FunctionType funcType = new FunctionType("Foo", ImmutableList.of(new ObjectType("Number")), new ObjectType("Object"));
    Node root = Node.newScript("function Foo(x) { return x; }");
    runtimeTypeCheck.process(null, root);
    NodeTraversal.traverse(compiler, root, new NodePrintVisitor());

    // The output should contain the checkType calls for Foo's parameter x.
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddChecks_InvalidFunctionType() {
    FunctionType funcType = new FunctionType("Foo", ImmutableList.of(), null);
    Node root = Node.newScript("function Foo() {}");
    runtimeTypeCheck.process(null, root);
  }

  @Test
  public void testAddBoilerplateCode() {
    Node root = Node.newScript("function Foo() {}");
    runtimeTypeCheck.process(null, root);
    NodeTraversal.traverse(compiler, root, new NodePrintVisitor());

    // The output should contain the checkType and related boilerplate code.
  }

  /**
   * Generates a tree of the given Node that simply prints the node to the
   * console.
   */
  private static class NodePrintVisitor extends NodeTraversal.AbstractPostOrderCallback {
    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      System.out.println(NodeUtil.toString(n));
    }
  }
}