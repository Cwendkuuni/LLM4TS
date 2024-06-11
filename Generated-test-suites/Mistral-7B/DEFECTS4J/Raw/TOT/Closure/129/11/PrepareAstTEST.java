import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.PrepareAst;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PrepareAstTest {

  @Test
  public void testPrepareAstNormalization() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    String astCode = "function foo() {\n" +
            "  var x = 1;\n" +
            "  if (true) {\n" +
            "    if (x > 0) {\n" +
            "      return 'positive';\n" +
            "    } else {\n" +
            "      return 'negative';\n" +
            "    }\n" +
            "  } else {\n" +
            "    return 'false';\n" +
            "  }\n" +
            "}\n";

    Node root = IR.parse(astCode, null);
    prepareAst.process(null, root);

    Node expectedRoot = IR.parse("function foo() {\n" +
            "  var x = 1;\n" +
            "  if (true) {\n" +
            "    if (x > 0) {\n" +
            "      return 'positive';\n" +
            "    } else {\n" +
            "      if (x < 0) {\n" +
            "        return 'negative';\n" +
            "      } else {\n" +
            "        return 'non-positive';\n" +
            "      }\n" +
            "    }\n" +
            "  } else {\n" +
            "    return 'false';\n" +
            "  }\n" +
            "}\n", null);

    compareTrees(root, expectedRoot);
  }

  private void compareTrees(Node actual, Node expected) {
    if (!NodeUtil.isEquivalent(actual, expected)) {
      throw new AssertionError("Expected:\n" + NodePrinter.printNode(expected) +
              "\nBut was:\n" + NodePrinter.printNode(actual) + "\n");
    }
  }
}