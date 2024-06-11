import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Arrays;
import java.util.Set;

public class MustBeReachingVariableDefTest {

  @org.junit.Test
  public void testMustBeReachingVariableDef_SimpleAssignment() {
    String sourceCode = "function foo() {" +
        "var x = 5;" +
        "var y = x;" +
        "return y;" +
        "}";

    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(sourceCode, jsScope, compiler);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef result = analysis.compute();

    Node useNode = cfg.getEntryNode().getFirstSuccessor().getNode();
    Node defNode = analysis.getDef("y", useNode);
    assertEquals(cfg.getNode(5).getNode(), defNode);
  }

  @org.junit.Test
  public void testMustBeReachingVariableDef_MultipleAssignments() {
    String sourceCode = "function foo() {" +
        "var x = 5;" +
        "var y = x;" +
        "x = 10;" +
        "return y;" +
        "}";

    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(sourceCode, jsScope, compiler);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef result = analysis.compute();

    Node useNode = cfg.getEntryNode().getFirstSuccessor().getNode();
    Node defNode = analysis.getDef("y", useNode);
    assertEquals(cfg.getNode(5).getNode(), defNode);
  }

  @org.junit.Test
  public void testMustBeReachingVariableDef_MultipleDefinitions() {
    String sourceCode = "function foo() {" +
        "var x = 5;" +
        "var x = 10;" +
        "return x;" +
        "}";

    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(sourceCode, jsScope, compiler);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef result = analysis.compute();

    Node useNode = cfg.getEntryNode().getFirstSuccessor().getNode();
    Node defNode = analysis.getDef("x", useNode);
    assertNull(defNode);
  }

  @org.junit.Test
  public void testMustBeReachingVariableDef_FunctionCalls() {
    String sourceCode = "function foo() {" +
        "var x = 5;" +
        "function bar() {" +
        "return x;" +
        "}" +
        "return bar();" +
        "}";

    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(sourceCode, jsScope, compiler);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef result = analysis.compute();

    Node useNode = cfg.getEntryNode().getFirstSuccessor().getNode();
    Node defNode = analysis.getDef("x", useNode);
    assertEquals(cfg.getNode(5).getNode(), defNode);
  }

  @org.junit.Test
  public void testMustBeReachingVariableDef_Conditionals() {
    String sourceCode = "function foo() {" +
        "var x = 5;" +
        "if (true) {" +
        "  var y = x;" +
        "} else {" +
        "  var y = 10;" +
        "}" +
        "return y;" +
        "}";

    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(sourceCode, jsScope, compiler);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef result = analysis.compute();

    Node useNode = cfg.getEntryNode().getFirstSuccessor().getNode();
    Node defNode = analysis.getDef("y", useNode);
    assertEquals(cfg.getNode(5).getNode(), defNode);
  }

  @org.junit.Test
  public void testMustBeReachingVariableDef_Loops() {
    String sourceCode = "function foo() {" +
        "var x = 5;" +
        "for (var i = 0; i < 10; i++) {" +
        "  x += i;" +
        "}" +
        "return x;" +
        "}";

    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(sourceCode, jsScope, compiler);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef result = analysis.compute();

    Node useNode = cfg.getEntryNode().getFirstSuccessor().getNode();
    Node defNode = analysis.getDef("x", useNode);
    assertEquals(cfg.getNode(5).getNode(), defNode);
  }

  @org.junit.Test
  public void testMustBeReachingVariableDef_EscapedVariables() {
    String sourceCode = "function foo(x) {" +
        "return x;" +
        "}";

    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(sourceCode, jsScope, compiler);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef result = analysis.compute();

    Node useNode = cfg.getEntryNode().getFirstSuccessor().getNode();
    Var arg = jsScope.getVar("x");
    assertNull(analysis.getDef(arg.getName(), useNode));
  }

  @org.junit.Test
  public void testMustBeReachingVariableDef_MultipleEscapedVariables() {
    String sourceCode = "function foo(x, y) {" +
        "return x + y;" +
        "}";

    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(sourceCode, jsScope, compiler);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef result = analysis.compute();

    Node useNode = cfg.getEntryNode().getFirstSuccessor().getNode();
    Var arg1 = jsScope.getVar("x");
    Var arg2 = jsScope.getVar("y");
    assertNull(analysis.getDef(arg1.getName(), useNode));
    assertNull(analysis.getDef(arg2.getName(), useNode));
  }

  @org.junit.Test
  public void testMustBeReachingVariableDef_CatchBlocks() {
    String sourceCode = "function foo() {" +
        "try {" +
        "  var x = 5;" +
        "  throw x;" +
        "} catch (e) {" +
        "  var y = e + 1;" +
        "  return y;" +
        "}" +
        "}";

    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(sourceCode, jsScope, compiler);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef result = analysis.compute();

    Node useNode = cfg.getEntryNode().getFirstSuccessor().getNode();
    Node defNode = analysis.getDef("y", useNode);
    assertNull(defNode);
  }

  @org.junit.Test
  public void testMustBeReachingVariableDef_DependenciesAcrossBranches() {
    String sourceCode = "function foo() {" +
        "function bar() {" +
        "  var x = 5;" +
        "  return x;" +
        "}" +
        "if (true) {" +
        "  return bar();" +
        "} else {" +
        "  var x = 10;" +
        "  return x;" +
        "}" +
        "}";

    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(sourceCode, jsScope, compiler);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef result = analysis.compute();

    Node useNode = cfg.getEntryNode().getFirstSuccessor().getNode();
    Node defNode = analysis.getDef("x", useNode);
    assertEquals(cfg.getNode(5).getNode(), defNode);
  }

  @org.junit.Test
  public void testMustBeReachingVariableDef_FunctionReturn() {
    String sourceCode = "function foo() {" +
        "function bar() {" +
        "  return 5;" +
        "}" +
        "return bar();" +
        "}";

    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(sourceCode, jsScope, compiler);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef result = analysis.compute();

    Node useNode = cfg.getEntryNode().getNode();
    Node defNode = analysis.getDef("returnValue", useNode);
    assertEquals(cfg.getNode(5).getNode(), defNode);
  }

  @org.junit.Test
  public void testMustBeReachingVariableDef_FunctionArguments() {
    String sourceCode = "function foo(x) {" +
        "return x;" +
        "}";

    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(sourceCode, jsScope, compiler);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef result = analysis.compute();

    Node useNode = cfg.getEntryNode().getNode();
    Var arg = jsScope.getVar("x");
    Node defNode = analysis.getDef(arg.getName(), useNode);
    assertEquals(cfg.getFunctionNode(0).getFirstChild().getNode(), defNode);
  }

  @org.junit.Test
  public void testMustBeReachingVariableDef_MultipleFunctions() {
    String sourceCode = "function foo() {" +
        "function bar() {" +
        "  return 5;" +
        "}" +
        "function baz() {" +
        "  return 10;" +
        "}" +
        "return bar() + baz();" +
        "}";

    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(sourceCode, jsScope, compiler);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef result = analysis.compute();

    Node useNode = cfg.getEntryNode().getNode();
    Node defNode1 = analysis.getDef("returnValue", useNode);
    Node defNode2 = analysis.getDef("returnValue", useNode);
    assertEquals(cfg.getNode(5).getNode(), defNode1);
    assertEquals(cfg.getNode(10).getNode(), defNode2);
  }

  @org.junit.Test
  public void testMustBeReachingVariableDef_ErrorHandling() {
    String sourceCode = "function foo() {" +
        "try {" +
        "  var x = 5;" +
        "  throw new Error('Something went wrong');" +
        "} catch (e) {" +
        "  var y = e.message;" +
        "  return y;" +
        "}" +
        "}";

    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(sourceCode, jsScope, compiler);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef result = analysis.compute();

    Node useNode = cfg.getEntryNode().getNode();
    Node defNode = analysis.getDef("y", useNode);
    assertEquals("Something went wrong", defNode.toString());
  }

  @org.junit.Test
  public void testMustBeReachingVariableDef_MultipleUses() {
    String sourceCode = "function foo() {" +
        "var x = 5;" +
        "return x + x;" +
        "}";

    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(sourceCode, jsScope, compiler);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef result = analysis.compute();

    Node useNode = cfg.getEntryNode().getNode();
    Node defNode = analysis.getDef("x", useNode);
    assertEquals(cfg.getNode(5).getNode(), defNode);
  }

  @org.junit.Test
  public void testMustBeReachingVariableDef_ComplexControlFlow() {
    String sourceCode = "function foo() {" +
        "function bar() {" +
        "  if (true) {" +
        "    return 5;" +
        "  } else {" +
        "    return 10;" +
        "  }}" +
        "function baz() {" +
        "  return 15;" +
        "}" +
        "var x = bar();" +
        "var y = baz();" +
        "return x + y;" +
        "}";

    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(sourceCode, jsScope, compiler);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef result = analysis.compute();

    Node useNode = cfg.getEntryNode().getNode();
    Node defNode1 = analysis.getDef("x", useNode);
    Node defNode2 = analysis.getDef("y", useNode);
    assertEquals(cfg.getNode(5).getNode(), defNode1);
    assertEquals(cfg.getNode(15).getNode(), defNode2);
  }
}