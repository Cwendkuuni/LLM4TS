import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeTree.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    String input = "// CommonJS module\n" +
        "define(function() {\n" +
        "  var a = 1;\n" +
        "  module.exports = {\n" +
        "    b: 2,\n" +
        "    c: function() {\n" +
        "      return 'hello';\n" +
        "    }\n" +
        "  };\n" +
        "});\n" +
        "\n" +
        "// Requiring the CommonJS module\n" +
        "require(['path/to/module'])\n" +
        "  .doSomething = function() {\n" +
        "    console.log(a); // should print 1\n" +
        "    console.log(module.exports.b); // should print 2\n" +
        "    console.log(module.exports.c()); // should print 'hello'\n" +
        "  };\n";

    Compiler compiler = new Compiler();
    CompilerPass processCommonJSModules = new ProcessCommonJSModules(compiler, ".");
    Node root = parse(input);
    processCommonJSModules.process(externs(), root);

    NodeTraversal nodeTraversal = NodeTraversal.forTree(root);
    nodeTraversal.traverse(processCommonJSModules);

    Node output = nodeTraversal.getOutput();
    Node doSomething = output.getFirstChild().getFirstChild();

    // Test that 'a' is assigned to a global variable and accessible
    Node a = output.getFirstChild().getNextSibling();
    assertEquals("1", a.getString());

    // Test that 'b' and 'c' are assigned to module.exports and accessible
    Node exports = output.getFirstChild().getNextSibling();
    Node b = exports.getFirstChild();
    assertEquals("2", b.getString());
    Node c = exports.getNextSibling();
    Node functionNode = c.getFirstChild();
    Node functionBody = functionNode.getFirstChild();
    assertEquals("'hello'", functionBody.getString());

    // Test that 'doSomething' function can access 'a', 'b' and 'c'
    Node doSomethingFunction = doSomething.getFirstChild();
    Node logStatementA = doSomethingFunction.getNextSibling();
    Node logStatementB = logStatementA.getNextSibling();
    Node logStatementC = logStatementB.getNextSibling();

    Node logA = logStatementA.getFirstChild();
    Node logB = logStatementB.getFirstChild();
    Node logC = logStatementC.getFirstChild();

    assertEquals("1", logA.getString());
    assertEquals("2", logB.getString());
    assertEquals("'hello'", logC.getString());
  }
}