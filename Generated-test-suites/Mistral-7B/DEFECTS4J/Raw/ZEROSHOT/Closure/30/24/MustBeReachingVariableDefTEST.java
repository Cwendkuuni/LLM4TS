import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class MustBeReachingVariableDefTest {

  @Test
  public void testSimpleAssign() {
    String code = "function foo() {\n" +
        "  var x = 1;\n" +
        "  var y = x;\n" +
        "  return y;\n" +
        "}";

    Node root = NodeFactory.compile(code);
    Scope jsScope = new Scope(root.getFirstChild());
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(root, jsScope, compiler);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node defNode = analyzer.getDef("y", root);
    assertEquals(analyzer.getNode(jsScope.getVar("x")), defNode);
  }

  @Test
  public void testSimpleAssignWithConditional() {
    String code = "function foo() {\n" +
        "  var x = 1;\n" +
        "  if (true) {\n" +
        "    var y = x;\n" +
        "  } else {\n" +
        "    var y = 2;\n" +
        "  }\n" +
        "  return y;\n" +
        "}";

    Node root = NodeFactory.compile(code);
    Scope jsScope = new Scope(root.getFirstChild());
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(root, jsScope, compiler);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node defNode = analyzer.getDef("y", root);

    // In this case, we cannot determine the reaching definition, so it should
    // be null.
    assertNull(defNode);
  }

  @Test
  public void testSimpleIf() {
    String code = "function foo() {\n" +
        "  var x = 1;\n" +
        "  if (true) {\n" +
        "    var y = x;\n" +
        "  } else {\n" +
        "    var y = 2;\n" +
        "  }\n" +
        "  return y;\n" +
        "}";

    Node root = NodeFactory.compile(code);
    Scope jsScope = new Scope(root.getFirstChild());
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(root, jsScope, compiler);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node defNode = analyzer.getDef("y", root);

    // In this case, we cannot determine the reaching definition because the
    // definition is conditional.
    assertNull(defNode);
  }

  @Test
  public void testSimpleWhile() {
    String code = "function foo() {\n" +
        "  var x = 1;\n" +
        "  while (true) {\n" +
        "    var y = x;\n" +
        "  }\n" +
        "  return y;\n" +
        "}";

    Node root = NodeFactory.compile(code);
    Scope jsScope = new Scope(root.getFirstChild());
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(root, jsScope, compiler);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node defNode = analyzer.getDef("y", root);

    // In this case, we cannot determine the reaching definition because the
    // loop never terminates.
    assertNull(defNode);
  }

  @Test
  public void testSimpleFor() {
    String code = "function foo() {\n" +
        "  var x = 1;\n" +
        "  for (var i = 0; i < 10; i++) {\n" +
        "    var y = x;\n" +
        "  }\n" +
        "  return y;\n" +
        "}";

    Node root = NodeFactory.compile(code);
    Scope jsScope = new Scope(root.getFirstChild());
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(root, jsScope, compiler);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node defNode = analyzer.getDef("y", root);

    // In this case, we cannot determine the reaching definition because the
    // loop condition is not constant.
    assertNull(defNode);
  }

  @Test
  public void testSimpleForIn() {
    String code = "function foo() {\n" +
        "  for (var x in [1, 2, 3]) {\n" +
        "    var y = x;\n" +
        "  }\n" +
        "  return y;\n" +
        "}";

    Node root = NodeFactory.compile(code);
    Scope jsScope = new Scope(root.getFirstChild());
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(root, jsScope, compiler);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node defNode = analyzer.getDef("y", root);

    // In this case, we cannot determine the reaching definition because the
    // loop condition is not constant.
    assertNull(defNode);
  }

  @Test
  public void testSimpleIfElse() {
    String code = "function foo() {\n" +
        "  var x = 1;\n" +
        "  if (true) {\n" +
        "    var y = x;\n" +
        "  } else {\n" +
        "    var y = 2;\n" +
        "  }\n" +
        "  return y;\n" +
        "}";

    Node root = NodeFactory.compile(code);
    Scope jsScope = new Scope(root.getFirstChild());
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(root, jsScope, compiler);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node defNode = analyzer.getDef("y", root);

    // In this case, we cannot determine the reaching definition because the
    // definition is conditional.
    assertNull(defNode);
  }

  @Test
  public void testSimpleAssignWithFunctionCall() {
    String code = "function foo() {\n" +
        "  var x = 1;\n" +
        "  var y = foo(x);\n" +
        "  return y;\n" +
        "}";

    String functionCode = "function foo(x) {\n" +
        "  return x * 2;\n" +
        "}";

    Node root = NodeFactory.compile(code);
    Scope jsScope = new Scope(root.getFirstChild());
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(root, jsScope, compiler);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node functionDefNode = cfg.getNode(NodeUtil.getFunctionDefinition(root, "foo"));
    Node defNode = analyzer.getDef("y", root);

    assertEquals(functionDefNode.getFirstChild(), defNode);
  }

  @Test
  public void testSimpleAssignWithFunctionCallAndConditional() {
    String code = "function foo() {\n" +
        "  var x = 1;\n" +
        "  if (true) {\n" +
        "    var y = foo(x);\n" +
        "  } else {\n" +
        "    var y = 2;\n" +
        "  }\n" +
        "  return y;\n" +
        "}";

    String functionCode = "function foo(x) {\n" +
        "  return x * 2;\n" +
        "}";

    Node root = NodeFactory.compile(code);
    Scope jsScope = new Scope(root.getFirstChild());
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(root, jsScope, compiler);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node functionDefNode = cfg.getNode(NodeUtil.getFunctionDefinition(root, "foo"));
    Node defNode = analyzer.getDef("y", root);

    assertNull(defNode);
  }

  @Test
  public void testSimpleAssignWithFunctionCallAndLoop() {
    String code = "function foo() {\n" +
        "  var x = 1;\n" +
        "  while (true) {\n" +
        "    var y = foo(x);\n" +
        "  }\n" +
        "  return y;\n" +
        "}";

    String functionCode = "function foo(x) {\n" +
        "  return x * 2;\n" +
        "}";

    Node root = NodeFactory.compile(code);
    Scope jsScope = new Scope(root.getFirstChild());
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(root, jsScope, compiler);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node functionDefNode = cfg.getNode(NodeUtil.getFunctionDefinition(root, "foo"));
    Node defNode = analyzer.getDef("y", root);

    assertNull(defNode);
  }

  @Test
  public void testSimpleAssignWithFunctionCallAndConditionalLoop() {
    String code = "function foo() {\n" +
        "  var x = 1;\n" +
        "  if (true) {\n" +
        "    while (true) {\n" +
        "      var y = foo(x);\n" +
        "    }\n" +
        "  } else {\n" +
        "    var y = 2;\n" +
        "  }\n" +
        "  return y;\n" +
        "}";

    String functionCode = "function foo(x) {\n" +
        "  return x * 2;\n" +
        "}";

    Node root = NodeFactory.compile(code);
    Scope jsScope = new Scope(root.getFirstChild());
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(root, jsScope, compiler);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node functionDefNode = cfg.getNode(NodeUtil.getFunctionDefinition(root, "foo"));
    Node defNode = analyzer.getDef("y", root);

    assertNull(defNode);
  }
}