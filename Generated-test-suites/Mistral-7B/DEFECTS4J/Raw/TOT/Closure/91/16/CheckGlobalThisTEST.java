import static org.junit.Assert.assertThat;
import static com.google.common.truth.Truth.assertThat;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testSafeUsageOfThis() {
    SourceFile src = new SourceFile("Test.js", "class Test {\n" +
        "  constructor() {\n" +
        "    this.name = 'John Doe';\n" +
        "  }\n" +
        "  @this\n" +
        "  method() {\n" +
        "    return this.name;\n" +
        "  }\n" +
        "  static getProperty() {\n" +
        "    return this.prototype.name;\n" +
        "  }\n" +
        "}");

    Compiler compiler = new Compiler();
    Checker checker = new Checker(new AbstractCompiler(compiler), CheckLevel.WARNING);
    Parser parser = new Parser(new NodeFactory());
    Node root = parser.parse(src.getCode(), src, 0);

    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    assertThat(checker.getDiagnostics().length).isZero();
  }

  @Test
  public void testUnsafeUsageOfThis() {
    SourceFile src = new SourceFile("Test.js", "var globalThis = this;\n" +
        "function unsafeFunction() {\n" +
        "  globalThis.property = 'John Doe';\n" +
        "}");

    Compiler compiler = new Compiler();
    Checker checker = new Checker(new AbstractCompiler(compiler), CheckLevel.WARNING);
    Parser parser = new Parser(new NodeFactory());
    Node root = parser.parse(src.getCode(), src, 0);

    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    DiagnosticType diagnostic = DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS",
        "dangerous use of the global 'this' object");

    Node diagnosticNode = null;
    for (Node diagnosticNodeTemp : checker.getDiagnostics()) {
      if (diagnosticNodeTemp.getType() == Token.ERROR &&
          diagnosticNodeTemp.getDiagnosticType() == diagnostic) {
        diagnosticNode = diagnosticNodeTemp;
        break;
      }
    }

    assertThat(diagnosticNode).isNotNull();
  }

  @Test
  public void testCheckGlobalThisWithLoop() {
    SourceFile src = new SourceFile("Test.js", "function CheckGlobalThis() {\n" +
        "  for (var i = 0; i < 5; i++) {\n" +
        "    if (this === window) {\n" +
        "      throw new Error('Global this detected');\n" +
        "    }\n" +
        "  }\n" +
        "}");

    Compiler compiler = new Compiler();
    Checker checker = new Checker(new AbstractCompiler(compiler), CheckLevel.WARNING);
    Parser parser = new Parser(new NodeFactory());
    Node root = parser.parse(src.getCode(), src, 0);

    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    assertThat(checker.getDiagnostics().length).isZero();
  }

  @Test(expected = RuntimeException.class)
  public void testCheckGlobalThisWithLoopError() {
    SourceFile src = new SourceFile("Test.js", "function CheckGlobalThis() {\n" +
        "  for (var i = 0; i < 5; i++) {\n" +
        "    if (this === window) {\n" +
        "      throw new Error('Global this detected');\n" +
        "    }\n" +
        "  }\n" +
        "}");

    Compiler compiler = new Compiler();
    Checker checker = new Checker(new AbstractCompiler(compiler), CheckLevel.ERROR);
    Parser parser = new Parser(new NodeFactory());
    Node root = parser.parse(src.getCode(), src, 0);

    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    // Test should fail with an error because of the 'this' keyword usage
    // inside the for loop
  }
}