import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.testing.BaseGeneratorTest;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest extends BaseGeneratorTest {

  private static final String CHECK_GLOBAL_THIS_CLASS =
      "com.google.javascript.jscomp.CheckGlobalThis";

  @Override
  protected CompilerOptions getOptions() {
    CompilerOptions options = super.getOptions();
    options.setWarningLevel(WarningLevel.QUIRKS, "JSC_USED_GLOBAL_THIS");
    return options;
  }

  public void testCheckGlobalThis() throws Exception {
    String sourceCode =
        "function evil() { this.useful = undefined; } "
            + "function safe() { return this.useful; } "
            + "var a = function() { }; "
            + "function b() { function c() {} } "
            + "var d = { x: function() {} }; ";

    AbstractCompiler compiler = getCompiler();
    Checker checker = compiler.getChecker();
    Parser parser = new Parser(new SourceFile("test.js", sourceCode));
    Node program = parser.parse();
    NodeTraversal traversal = new NodeTraversal(checker, program, null);
    Callback callback = new CheckGlobalThis(compiler, WARNINGS);
    traversal.traverse(callback);
    assertEquals(1, callback.getNumDiagnostics());

    Node nodeToCheck =
        findNode("function evil() { this.useful = undefined; }", parser);
    DiagnosticType diagnosticType = DIAGNOSTICS.get(WARNINGS)
        .get("JSC_USED_GLOBAL_THIS");
    Node diagnosticNode = nodeToCheck.getFirstChild();
    assertEquals("dangerous use of the global 'this' object",
        diagnosticNode.getString());
    assertEquals("evil", diagnosticNode.getFirstChild().getString());
    assertEquals(nodeToCheck, diagnosticNode.getLocation().getSourceNode());
  }

  public void testCheckGlobalThisInFunctionCall() throws Exception {
    String sourceCode =
        "function evil(thisArg) { this.useful = undefined; } "
            + "evil();";

    AbstractCompiler compiler = getCompiler();
    Checker checker = compiler.getChecker();
    Parser parser = new Parser(new SourceFile("test.js", sourceCode));
    Node program = parser.parse();
    NodeTraversal traversal = new NodeTraversal(checker, program, null);
    Callback callback = new CheckGlobalThis(compiler, WARNINGS);
    traversal.traverse(callback);
    assertEquals(0, callback.getNumDiagnostics());
  }

  public void testCheckGlobalThisInFunctionAnnotation() throws Exception {
    String sourceCode =
        "@constructor "
            + "function EvilConstructor() { this.useful = undefined; } ";

    AbstractCompiler compiler = getCompiler();
    Checker checker = compiler.getChecker();
    Parser parser = new Parser(new SourceFile("test.js", sourceCode));
    Node program = parser.parse();
    NodeTraversal traversal = new NodeTraversal(checker, program, null);
    Callback callback = new CheckGlobalThis(compiler, WARNINGS);
    traversal.traverse(callback);
    assertEquals(0, callback.getNumDiagnostics());
  }

  public void testCheckGlobalThisInPrototypeMethod() throws Exception {
    String sourceCode =
        "function EvilParent() {} "
            + "EvilParent.prototype.evil = function() { this.useful = undefined; } ";

    AbstractCompiler compiler = getCompiler();
    Checker checker = compiler.getChecker();
    Parser parser = new Parser(new SourceFile("test.js", sourceCode));
    Node program = parser.parse();
    NodeTraversal traversal = new NodeTraversal(checker, program, null);
    Callback callback = new CheckGlobalThis(compiler, WARNINGS);
    traversal.traverse(callback);
    assertEquals(0, callback.getNumDiagnostics());
  }

  public void testCheckGlobalThisInObjectLiteral() throws Exception {
    String sourceCode =
        "var evil = { evil: function() { this.useful = undefined; } }; ";

    AbstractCompiler compiler = getCompiler();
    Checker checker = compiler.getChecker();
    Parser parser = new Parser(new SourceFile("test.js", sourceCode));
    Node program = parser.parse();
    NodeTraversal traversal = new NodeTraversal(checker, program, null);
    Callback callback = new CheckGlobalThis(compiler, WARNINGS);
    traversal.traverse(callback);
    assertEquals(0, callback.getNumDiagnostics());
  }

  public void testCheckGlobalThisInPrototypeProperty() throws Exception {
    String sourceCode =
        "function EvilParent() {} "
            + "EvilParent.prototype.evil = function() { return this.useful; } "
            + "EvilParent.prototype.useful = undefined;";

    AbstractCompiler compiler = getCompiler();
    Checker checker = compiler.getChecker();
    Parser parser = new Parser(new SourceFile("test.js", sourceCode));
    Node program = parser.parse();
    NodeTraversal traversal = new NodeTraversal(checker, program, null);
    Callback callback = new CheckGlobalThis(compiler, WARNINGS);
    traversal.traverse(callback);
    assertEquals(1, callback.getNumDiagnostics());

    Node nodeToCheck =
        findNode("EvilParent.prototype.evil = function() { return this.useful; }", parser);
    DiagnosticType diagnosticType = DIAGNOSTICS.get(WARNINGS)
        .get("JSC_USED_GLOBAL_THIS");
    Node diagnosticNode = nodeToCheck.getFirstChild();
    assertEquals("dangerous use of the global 'this' object",
        diagnosticNode.getString());
    assertEquals("EvilParent.prototype.evil", diagnosticNode.getFirstChild().getString());
    assertEquals(nodeToCheck, diagnosticNode.getLocation().getSourceNode());
  }

  public void testCheckGlobalThisInPrototypeSubproperty() throws Exception {
    String sourceCode =
        "function EvilParent() {} "
            + "EvilParent.prototype.evil = function() { return this.sub.useful; } "
            + "EvilParent.prototype.sub = { useful: undefined };";

    AbstractCompiler compiler = getCompiler();
    Checker checker = compiler.getChecker();
    Parser parser = new Parser(new SourceFile("test.js", sourceCode));
    Node program = parser.parse();
    NodeTraversal traversal = new NodeTraversal(checker, program, null);
    Callback callback = new CheckGlobalThis(compiler, WARNINGS);
    traversal.traverse(callback);
    assertEquals(0, callback.getNumDiagnostics());
  }

  public void testCheckGlobalThisInFunctionWithNoAnnotation() throws Exception {
    String sourceCode =
        "function evil() { this.useful = undefined; } ";

    AbstractCompiler compiler = getCompiler();
    Checker checker = compiler.getChecker();
    Parser parser = new Parser(new SourceFile("test.js", sourceCode));
    Node program = parser.parse();
    NodeTraversal traversal = new NodeTraversal(checker, program, null);
    Callback callback = new CheckGlobalThis(compiler, WARNINGS);
    traversal.traverse(callback);
    assertEquals(1, callback.getNumDiagnostics());

    Node nodeToCheck =
        findNode("function evil() { this.useful = undefined; }", parser);
    DiagnosticType diagnosticType = DIAGNOSTICS.get(WARNINGS)
        .get("JSC_USED_GLOBAL_THIS");
    Node diagnosticNode = nodeToCheck.getFirstChild();
    assertEquals("dangerous use of the global 'this' object",
        diagnosticNode.getString());
    assertEquals("evil", diagnosticNode.getFirstChild().getString());
    assertEquals(nodeToCheck, diagnosticNode.getLocation().getSourceNode());
  }

  public void testCheckGlobalThisInFunctionWithAssignment() throws Exception {
    String sourceCode =
        "function evil() { var thisRef = this; this.useful = undefined; } ";

    AbstractCompiler compiler = getCompiler();
    Checker checker = compiler.getChecker();
    Parser parser = new Parser(new SourceFile("test.js", sourceCode));
    Node program = parser.parse();
    NodeTraversal traversal = new NodeTraversal(checker, program, null);
    Callback callback = new CheckGlobalThis(compiler, WARNINGS);
    traversal.traverse(callback);
    assertEquals(0, callback.getNumDiagnostics());
  }

  public void testCheckGlobalThisInFunctionWithFunctionDeclaration() throws Exception {
    String sourceCode =
        "function evil() { function inner() { this.useful = undefined; } } ";

    AbstractCompiler compiler = getCompiler();
    Checker checker = compiler.getChecker();
    Parser parser = new Parser(new SourceFile("test.js", sourceCode));
    Node program = parser.parse();
    NodeTraversal traversal = new NodeTraversal(checker, program, null);
    Callback callback = new CheckGlobalThis(compiler, WARNINGS);
    traversal.traverse(callback);
    assertEquals(0, callback.getNumDiagnostics());
  }

  public void testCheckGlobalThisInFunctionWithFunctionExpression() throws Exception {
    String sourceCode =
        "function evil() { var inner = function() { this.useful = undefined; } }; } ";

    AbstractCompiler compiler = getCompiler();
    Checker checker = compiler.getChecker();
    Parser parser = new Parser(new SourceFile("test.js", sourceCode));
    Node program = parser.parse();
    NodeTraversal traversal = new NodeTraversal(checker, program, null);
    Callback callback = new CheckGlobalThis(compiler, WARNINGS);
    traversal.traverse(callback);
    assertEquals(0, callback.getNumDiagnostics());
  }

  public void testCheckGlobalThisInFunctionWithVariableDeclaration() throws Exception {
    String sourceCode =
        "function evil() { var inner = this; this.useful = undefined; } ";

    AbstractCompiler compiler = getCompiler();
    Checker checker = compiler.getChecker();
    Parser parser = new Parser(new SourceFile("test.js", sourceCode));
    Node program = parser.parse();
    NodeTraversal traversal = new NodeTraversal(checker, program, null);
    Callback callback = new CheckGlobalThis(compiler, WARNINGS);
    traversal.traverse(callback);
    assertEquals(0, callback.getNumDiagnostics());
  }

  public void testCheckGlobalThisInFunctionWithVariableFunctionDeclaration() throws Exception {
    String sourceCode =
        "function evil() { var inner = function() { this.useful = undefined; }; } ";

    AbstractCompiler compiler = getCompiler();
    Checker checker = compiler.getChecker();
    Parser parser = new Parser(new SourceFile("test.js", sourceCode));
    Node program = parser.parse();
    NodeTraversal traversal = new NodeTraversal(checker, program, null);
    Callback callback = new CheckGlobalThis(compiler, WARNINGS);
    traversal.traverse(callback);
    assertEquals(0, callback.getNumDiagnostics());
  }

  public void testCheckGlobalThisInFunctionWithVariableFunctionExpression() throws Exception {
    String sourceCode =
        "function evil() { var inner = function inner() { this.useful = undefined; }; } ";

    AbstractCompiler compiler = getCompiler();
    Checker checker = compiler.getChecker();
    Parser parser = new Parser(new SourceFile("test.js", sourceCode));
    Node program = parser.parse();
    NodeTraversal traversal = new NodeTraversal(checker, program, null);
    Callback callback = new CheckGlobalThis(compiler, WARNINGS);
    traversal.traverse(callback);
    assertEquals(0, callback.getNumDiagnostics());
  }

  public void testCheckGlobalThisInFunctionWithVariableFunctionExpressionWithName() throws Exception {
    String sourceCode =
        "function evil() { var inner = function inner() { this.useful = undefined; }.bind(this); } ";

    AbstractCompiler compiler = getCompiler();
    Checker checker = compiler.getChecker();
    Parser parser = new Parser(new SourceFile("test.js", sourceCode));
    Node program = parser.parse();
    NodeTraversal traversal = new NodeTraversal(checker, program, null);
    Callback callback = new CheckGlobalThis(compiler, WARNINGS);
    traversal.traverse(callback);
    assertEquals(0, callback.getNumDiagnostics());
  }

  private Node findNode(String sourceCode, Parser parser) {
    Node program = parser.parse();
    return findNode(sourceCode, program);
  }

  private Node findNode(String sourceCode, Node node) {
    Node current = node;
    int startPos = 0;
    while (current != null) {
      if (current.getText().equals(sourceCode)) {
        return current;
      }
      startPos += current.getEndPosition() - current.getStartPosition();
      current = current.getNext();
    }
    throw new RuntimeException("Could not find node for source code: " + sourceCode);
  }
}