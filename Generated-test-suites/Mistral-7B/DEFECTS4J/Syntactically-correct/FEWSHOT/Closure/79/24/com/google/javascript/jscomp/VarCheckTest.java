package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.test.NodeMatcher;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class VarCheckTest {

  private static AbstractCompiler createCompiler() {
    // Create a compiler that doesn't actually compile, just checks types.
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Node createAST(CompilerInput input, int options) {
        return NodeFactory.createRootNode();
      }

      @Override
      public CompilerInput newExternInput(String name) {
        CompilerInput input = new CompilerInput();
        input.setName(name);
        return input;
      }
    };
    compiler.setErrorHandler(new ErrorHandler() {
      @Override
      public void report(Node node, DiagnosticType type, Object... args) {
        fail("Unexpected diagnostic: " + type + " " + Arrays.toString(args));
      }
    });
    return compiler;
  }

  @Test
  public void testEmpty() {
    AbstractCompiler compiler = createCompiler();
    CompilerPass pass = new VarCheck(compiler);
    CompilerInput input = new CompilerInput();
    Node root = NodeFactory.createRootNode();
    pass.process(null, root);
    NodeTraversal.traverse(compiler, null, root, new NodeMatcher(""));
  }

  @Test
  public void testUndefinedVar() {
    AbstractCompiler compiler = createCompiler();
    CompilerPass pass = new VarCheck(compiler);
    CompilerInput input = new CompilerInput();
    Node root = NodeFactory.createRootNode();
    Node var = NodeFactory.createVar("x");
    root.addChild(var);
    pass.process(input, root);
    NodeTraversal.traverse(compiler, null, root, new NodeMatcher("x"));
  }

  @Test
  public void testUndefinedVarInFunction() {
    AbstractCompiler compiler = createCompiler();
    CompilerPass pass = new VarCheck(compiler);
    CompilerInput input = new CompilerInput();
    Node root = NodeFactory.createRootNode();
    Node func = NodeFactory.createFunction(null, "foo", null);
    Node var = NodeFactory.createVar("x");
    func.addChild(var);
    root.addChild(func);
    pass.process(input, root);
    NodeTraversal.traverse(compiler, null, root, new NodeMatcher("foo"));
    NodeTraversal.traverse(compiler, null, root, new NodeMatcher("x"));
  }

  @Test
  public void testDuplicateVar() {
    AbstractCompiler compiler = createCompiler();
    CompilerPass pass = new VarCheck(compiler);
    CompilerInput input = new CompilerInput();
    Node root = NodeFactory.createRootNode();
    Node var1 = NodeFactory.createVar("x");
    Node var2 = NodeFactory.createVar("x");
    root.addChild(var1);
    root.addChild(var2);
    pass.process(input, root);
    NodeTraversal.traverse(compiler, null, root, new NodeMatcher("x"));
  }

  @Test
  public void testUndefinedVarInExterns() {
    AbstractCompiler compiler = createCompiler();
    CompilerPass pass = new VarCheck(compiler);
    CompilerInput input = new CompilerInput();
    Node root = NodeFactory.createRootNode();
    Node externs = NodeFactory.createRootNode();
    Node var = NodeFactory.createVar("x");
    externs.addChild(var);
    pass.process(input, root);
    NodeTraversal.traverse(compiler, null, root, new NodeMatcher("x"));
  }

  @Test
  public void testDuplicateVarInExterns() {
    AbstractCompiler compiler = createCompiler();
    CompilerPass pass = new VarCheck(compiler);
    CompilerInput input = new CompilerInput();
    Node root = NodeFactory.createRootNode();
    Node externs = NodeFactory.createRootNode();
    Node var1 = NodeFactory.createVar("x");
    Node var2 = NodeFactory.createVar("x");
    externs.addChild(var1);
    externs.addChild(var2);
    pass.process(input, root);
    NodeTraversal.traverse(compiler, null, root, new NodeMatcher("x"));
  }

  @Test
  public void testModuleDependency() {
    AbstractCompiler compiler = createCompiler();
    AbstractCompiler compiler2 = createCompiler();
    CompilerPass pass = new VarCheck(compiler);
    CompilerPass pass2 = new VarCheck(compiler2);

    CompilerInput input1 = new CompilerInput();
    CompilerInput input2 = new CompilerInput();
    Node root1 = NodeFactory.createRootNode();
    Node root2 = NodeFactory.createRootNode();

    Node module1 = NodeFactory.createModule("module1", null, null);
    Node var1 = NodeFactory.createVar("x");
    module1.addChild(NodeFactory.createExport("x", var1));
    root1.addChild(module1);

    Node module2 = NodeFactory.createModule("module2", null, null);
    Node var2 = NodeFactory.createVar("x");
    root2.addChild(module2);

    // Create module graph.
    JSModule moduleGraph = NodeFactory.createJSModuleGraph();
    moduleGraph.addModule("module1", null);
    moduleGraph.addModule("module2", module1);

    pass.compiler = compiler2;
    pass.setModuleGraph(moduleGraph);
    pass.process(input1, root1);
    pass2.process(input2, root2);

    // Check for errors.
    NodeTraversal.traverse(compiler, null, root1, new NodeMatcher("x"));
    NodeTraversal.traverse(compiler2, null, root2, new NodeMatcher("x"));
  }

  @Test
  public void testModuleDependencyViolation() {
    AbstractCompiler compiler = createCompiler();
    AbstractCompiler compiler2 = createCompiler();
    CompilerPass pass = new VarCheck(compiler);
    CompilerPass pass2 = new VarCheck(compiler2);

    CompilerInput input1 = new CompilerInput();
    CompilerInput input2 = new CompilerInput();
    Node root1 = NodeFactory.createRootNode();
    Node root2 = NodeFactory.createRootNode();

    Node module1 = NodeFactory.createModule("module1", null, null);
    Node var1 = NodeFactory.createVar("x");
    module1.addChild(NodeFactory.createExport("x", var1));
    root1.addChild(module1);

    Node module2 = NodeFactory.createModule("module2", null, null);
    Node func = NodeFactory.createFunction(null, "foo", null);
    Node getProp = NodeFactory.createGetProp(func, "x");
    root2.addChild(module2);
    root2.addChild(func);

    // Create module graph.
    JSModule moduleGraph = NodeFactory.createJSModuleGraph();
    moduleGraph.addModule("module1", null);
    moduleGraph.addModule("module2", module1);

    pass.compiler = compiler2;
    pass.setModuleGraph(moduleGraph);
    pass.process(input1, root1);
    pass2.process(input2, root2);

    // Check for errors.
    NodeTraversal.traverse(compiler, null, root1, new NodeMatcher("x"));
    NodeTraversal.traverse(compiler2, null, root2, new NodeMatcher("foo"));
  }

  @Test
  public void testModuleDependencyMissing() {
    AbstractCompiler compiler = createCompiler();
    AbstractCompiler compiler2 = createCompiler();
    CompilerPass pass = new VarCheck(compiler);
    CompilerPass pass2 = new VarCheck(compiler2);

    CompilerInput input1 = new CompilerInput();
    CompilerInput input2 = new CompilerInput();
    Node root1 = NodeFactory.createRootNode();
    Node root2 = NodeFactory.createRootNode();

    Node module1 = NodeFactory.createModule("module1", null, null);
    Node var1 = NodeFactory.createVar("x");
    module1.addChild(NodeFactory.createExport("x", var1));
    root1.addChild(module1);

    Node module2 = NodeFactory.createModule("module2", null, null);
    Node func = NodeFactory.createFunction(null, "foo", null);
    Node getProp = NodeFactory.createGetProp(func, "x");
    root2.addChild(module2);
    root2.addChild(func);

    // Create module graph.
    JSModule moduleGraph = NodeFactory.createJSModuleGraph();
    moduleGraph.addModule("module1", null);

    pass.compiler = compiler2;
    pass.setModuleGraph(moduleGraph);
    pass.process(input1, root1);
    pass2.process(input2, root2);

    // Check for warnings.
    NodeTraversal.traverse(compiler, null, root1, new NodeMatcher("x"));
    NodeTraversal.traverse(compiler2, null, root2, new NodeMatcher("foo"));
  }

  private static class ErrorHandler implements Compiler.ErrorHandler {
    @Override
    public void report(CompilerInput input, int line, int character, DiagnosticType type, Object... args) {
      fail("Unexpected diagnostic: " + type + " " + Arrays.toString(args) +
          " on line " + line + " character " + character);
    }
  }

  private static class NodeMatcher implements NodeTraversal.Callback {
    private final String name;

    public NodeMatcher(String name) {
      this.name = name;
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.getType() == Token.NAME && n.getString().equals(name)) {
        assertNotNull("Variable " + name + " should be defined", parent.getParent());
      }
    }
  }

  private void assertNotNull(String message, Node node) {
    assertThat(node).isNotNull().named(message);
  }

  private void assertNull(String message, Node node) {
    assertThat(node).isNull().named(message);
  }
}