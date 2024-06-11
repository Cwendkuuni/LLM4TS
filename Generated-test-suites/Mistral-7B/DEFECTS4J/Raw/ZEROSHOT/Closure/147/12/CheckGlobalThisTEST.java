import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    Checker compiler = new Checker();
    CompilerOptions options = new CompilerOptions(null);
    options.setWarningLevel(DiagnosticType.JSC_USED_GLOBAL_THIS, WARNING);
    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, options);

    Parser parser = new Parser(new SourceFile("checkGlobalThis.js", ""));
    Node rootNode = parser.parse();

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(abstractCompiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(rootNode, checkGlobalThis);
    traversal.traverse();

    // Test cases for safe usages of 'this'

    // Prototype method
    testSafe("function Foo() {}\n" +
        "Foo.prototype.bar = function() {}\n", 0);

    // Constructor
    testSafe("function Foo() {}\n" +
        "function Foo() { this.bar = 1; }\n", 0);

    // Function annotated with @this
    testSafe("/** @constructor */\n" +
        "function Foo() {}\n" +
        "function Foo() { this.bar = 1; }\n", 0);

    // Function where there's no logical place to put a 'this' annotation
    testSafe("function Foo() {}\n" +
        "function Foo() {\n" +
        "  var that = this;\n" +
        "  this.bar = function() { return that.baz; }\n" +
        "}\n", 0);

    // Test cases for unsafe usages of 'this'

    // Global 'this'
    testUnsafe("var a = this;", 1);

    // Assignment to 'this' within a function
    testUnsafe("function evil() { this = 1; }", 1);

    // Assignment to 'this' within an object literal
    testUnsafe("var a = { foo: function() { this = 1; } };", 1);

    // Assignment to 'this' within a function expression
    testUnsafe("var a = function() { this = 1; };", 1);

    // Assignment to 'this' within a getter
    testUnsafe("var a = {\n" +
        "  get x() {\n" +
        "    this = 1;\n" +
        "  }\n" +
        "};", 1);

    // Assignment to 'this' within a setter
    testUnsafe("var a = {\n" +
        "  set x(val) {\n" +
        "    this = 1;\n" +
        "  }\n" +
        "};", 1);

    // Assignment to 'this' within a method call
    testUnsafe("function evil() { this.foo.bar = 1; }", 1);

    // Assignment to 'this' within a property access
    testUnsafe("function evil() { this.prototype.bar = 1; }", 1);

    // Assignment to 'this' within a property access chain
    testUnsafe("function evil() { this.prototype.foo.bar = 1; }", 1);
  }

  private void testSafe(String jsCode, int expectedDiagnosticCount) {
    SourceFile input = new SourceFile("checkGlobalThis.js", jsCode);
    Node rootNode = parser.parse();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(new Checker(), new CompilerOptions(null)), WARNING);
    NodeTraversal traversal = new NodeTraversal(rootNode, checkGlobalThis);
    traversal.traverse();
    int actualDiagnosticCount = compiler.getDiagnostics().size();
    assertTrue("Expected no diagnostics, but found " + actualDiagnosticCount + ":\n" + compiler.getDiagnostics().toSourceString(), actualDiagnosticCount == expectedDiagnosticCount);
  }

  private void testUnsafe(String jsCode, int expectedDiagnosticCount) {
    SourceFile input = new SourceFile("checkGlobalThis.js", jsCode);
    Node rootNode = parser.parse();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(new Checker(), new CompilerOptions(null)), WARNING);
    NodeTraversal traversal = new NodeTraversal(rootNode, checkGlobalThis);
    traversal.traverse();
    int actualDiagnosticCount = compiler.getDiagnostics().size();
    assertTrue("Expected " + expectedDiagnosticCount + " diagnostics, but found " + actualDiagnosticCount + ":\n" + compiler.getDiagnostics().toSourceString(), actualDiagnosticCount >= expectedDiagnosticCount);

    // Verify that the diagnostic messages match the test cases
    for (int i = 0; i < actualDiagnosticCount; i++) {
      DiagnosticType diagnosticType = compiler.getDiagnostics().get(i).getType();
      assertTrue("Unexpected diagnostic type: " + diagnosticType, diagnosticType.isWarning() && diagnosticType == DiagnosticType.JSC_USED_GLOBAL_THIS);
      Node node = compiler.getDiagnostics().get(i).getSourceNode();
      Node parent = node.getParent();
      assertTrue("Unexpected node type for the diagnostic: " + node.getType(), node.getType() != Token.THIS);
      if (parent != null && parent.getType() == Token.ASSIGN) {
        Node lhs = parent.getFirstChild();
        Node rhs = lhs.getNext();
        if (node == lhs && assignLhsChild != null) {
          assertTrue("Unexpected assignment: left hand side is not the expected node", assignLhsChild == lhs);
        }
      }
    }
  }
}