import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.isFunctionDeclaration;
import static com.google.javascript.jscomp.NodeUtil.isVarDeclaration;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.test.BaseTestCase;

public class MakeDeclaredNamesUniqueTest extends BaseTestCase {

  public void testMakeDeclaredNamesUnique() {
    String jsSource =
        "function foo() {" +
        "  var a = 1, b = 2, c = 3;" +
        "  function bar() {}" +
        "  var d = 4;" +
        "  var e = 5;" +
        "  try {" +
        "    var f = 6;" +
        "  } catch (g) {}" +
        "}";

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = newAbstractCompiler(options);

    Node ast = parseJs(compiler, jsSource);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = new NodeTraversal(ast, null, null, makeDeclaredNamesUnique);

    nodeTraversal.traverse();
    compiler.process();

    Node result = nodeTraversal.getCompiledResult();

    // Check that the function and variable names are unique.
    ImmutableSet<String> expectedUniqueFunctionNames =
        ImmutableSet.of("foo$0", "bar$0");
    ImmutableSet<String> expectedUniqueVariableNames =
        ImmutableSet.of(
            "a$0", "b$0", "c$0", "d$1", "e$1", "g$1", "f$2");

    Map<String, Node> functionMap = getFunctions(result);
    Map<String, Node> variableMap = getVariables(result);

    assertEquals(expectedUniqueFunctionNames, getFunctionNames(functionMap));
    assertEquals(expectedUniqueVariableNames, getVariableNames(variableMap));
  }

  private Map<String, Node> getFunctions(Node node) {
    Map<String, Node> functions = Maps.newHashMap();
    for (Node n = node; n != null; n = n.getParent()) {
      if (n.getType() == Token.FUNCTION) {
        functions.put(n.getFirstChild().getString(), n.getLastChild());
      }
    }
    return functions;
  }

  private Map<String, Node> getVariables(Node node) {
    Map<String, Node> variables = Maps.newHashMap();
    for (Node n = node; n != null; n = n.getParent()) {
      for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
        if (isVarDeclaration(child)) {
          variables.put(child.getString(), child);
        }
      }
    }
    return variables;
  }

  private ImmutableSet<String> getFunctionNames(Map<String, Node> functions) {
    return ImmutableSet.copyOf(functions.keySet());
  }

  private ImmutableSet<String> getVariableNames(Map<String, Node> variables) {
    return ImmutableSet.copyOf(variables.keySet());
  }

}