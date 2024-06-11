import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Scope.Var.LOCAL_VAR;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerContext;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.JSErrorReporter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RenameVarsTest {
  private CompilerOptions options;
  private AbstractCompiler compiler;
  private CompilerPassManager passManager;
  private NodeFactory nodeFactory;
  private JSErrorReporter errorReporter;
  private CompilerContext context;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    options.setShowWarnings(false);
    compiler = new TestCompiler();
    passManager = new CompilerPassManager(compiler);
    nodeFactory = new NodeFactory();
    errorReporter = new TestErrorReporter();
    context = new CompilerContext(options, nodeFactory, errorReporter);
  }

  @Test
  public void testRenameVars() {
    String testCode = "function main() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  var c = function() { return a; }" +
        "  var d = function() { return b; }" +
        "  var e = function(x) { return x; }" +
        "}";

    Node root = NodePrinter.parseJsCode(testCode, context);
    passManager.addPass(new RenameVars(compiler, "", false, false, false, null, null, Sets.<String> newHashSet()));
    passManager.runPasses(root, context);

    List<Node> renamedNodes = NodeTraversal.findNodesByType(root, Node.FUNCTION);
    for (Node renamedNode : renamedNodes) {
      assertEquals("function" + getFunctionNameSuffix(renamedNode) + "(", renamedNode.getSource());
    }

    Node mainFunction = NodeTraversal.findUniqueNode(root, Node.FUNCTION, "main");
    List<Node> renamedVariables = NodeTraversal.findNodesByTypeAndNameStartingWith(
        mainFunction, Node.VAR, "a");
    assertEquals("a", renamedVariables.get(0).getFirstChild().getString());

    renamedVariables = NodeTraversal.findNodesByTypeAndNameStartingWith(
        mainFunction, Node.VAR, "b");
    assertEquals("b", renamedVariables.get(0).getFirstChild().getString());

    renamedVariables = NodeTraversal.findNodesByTypeAndNameStartingWith(
        mainFunction, Node.VAR, "c");
    assertEquals("c", renamedVariables.get(0).getFirstChild().getString());

    renamedVariables = NodeTraversal.findNodesByTypeAndNameStartingWith(
        mainFunction, Node.VAR, "d");
    assertEquals("d", renamedVariables.get(0).getFirstChild().getString());

    renamedVariables = NodeTraversal.findNodesByTypeAndNameStartingWith(
        mainFunction, Node.VAR, "e");
    assertEquals("e", renamedVariables.get(0).getFirstChild().getString());

    List<Node> anonymousFunctions = NodeTraversal.findNodesByTypeAndParentType(
        root, Node.FUNCTION, Node.EXPRESSION);
    for (Node anonymousFunction : anonymousFunctions) {
      assertEquals("anonymous_" + getAnonymousFunctionSuffix(anonymousFunction),
          anonymousFunction.getSource());
    }
  }

  private String getFunctionNameSuffix(Node functionNode) {
    Scope scope = context.getTopLevelScope();
    Scope.Var variable = scope.getVar("main");
    int index = variable.getLocalVarIndex();
    return "_" + index;
  }

  private String getAnonymousFunctionSuffix(Node anonymousFunctionNode) {
    int index = NodeTraversal.getAnonymousFunctionIndex(anonymousFunctionNode);
    return "_" + index;
  }
}

class TestCompiler extends AbstractCompiler {
  @Override
  public void process(Node externs, Node root) {}
}

class TestErrorReporter implements JSErrorReporter {
  @Override
  public void report(int line, int lineNumber, int column, int columnNumber,
      String message, JSError.Level level) {}
}