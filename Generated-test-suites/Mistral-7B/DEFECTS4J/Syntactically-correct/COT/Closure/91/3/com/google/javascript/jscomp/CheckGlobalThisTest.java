import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.JSDocInfo;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() throws Exception {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    String sourceCode = ""
        + "function evil() { this.useful = undefined; } "
        + "function good() { var a = this; a.useful = undefined; } "
        + "function Ctor() {} "
        + "function Ctor2() {} "
        + "Ctor.prototype.method = function() { this.useful = undefined; } "
        + "function Ctor3() {} "
        + "Ctor3.prototype.method = function() { this.useful = undefined; } "
        + "function Ctor4() {} "
        + "Ctor4.prototype.method = function() { var a = this; a.useful = undefined; } "
        + "function Ctor5() {} "
        + "Ctor5.prototype.method = { value: function() { this.useful = undefined; } } "
        + "function Ctor6() {} "
        + "var obj = { method: function() { this.useful = undefined; } }; "
        + "function Ctor7() {} "
        + "Ctor7.prototype = obj; "
        + "function Ctor8() {} "
        + "Ctor8.prototype = obj; "
        + "function evil2() { this.useful = undefined; } ";

    CompilerOptions options = new CompilerOptions();

    AbstractCompiler compiler = new AbstractCompiler(new TestSourceFile("test.js", sourceCode), options);
    Node program = compiler.parseTree();

    NodeTraversal traverser = new NodeTraversal(program, checker);
    traverser.traverse();

    int numDiagnostics = compiler.getDiagnostics().length;

    // Test for methods that are not safe to use 'this' keyword
    assertTrue("Expected at least one warning for unsafe use of 'this'", numDiagnostics > 0);

    // Test edge cases and exception handling
    testFunctionAnnotatedWithConstructor(checker, sourceCode);
    testFunctionAnnotatedWithThis(checker, sourceCode);
    testFunctionWithNoLogicalPlaceToPutThisAnnotation(checker, sourceCode);
    testFunctionThatIsLentToPrototype(checker, sourceCode);
    testFunctionAnnotatedWithOverride(checker, sourceCode);
    testFunctionWithGetProperty(checker, sourceCode);
    testFunctionWithAssignment(checker, sourceCode);
    testFunctionWithLhsChild(checker, sourceCode);
    testFunctionWithLhsChildInFunction(checker, sourceCode);
    testFunctionWithLhsChildInObjectLiteral(checker, sourceCode);
    testFunctionWithLhsChildInAssignment(checker, sourceCode);
  }

  private void testFunctionAnnotatedWithConstructor(CheckGlobalThis checker, String sourceCode) {
    // Test function annotated with @constructor
    String constructorSourceCode = sourceCode.replaceAll("function Ctor() {", "/** @constructor */ function Ctor() {")
        .replaceAll("Ctor.prototype.method = function() {", "Ctor.prototype.method = function() {/** @this {Object} */}");

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(new TestSourceFile("test.js", constructorSourceCode), options);
    Node program = compiler.parseTree();

    NodeTraversal traverser = new NodeTraversal(program, checker);
    traverser.traverse();

    int numDiagnostics = compiler.getDiagnostics().length;

    assertTrue("Expected no warning for method annotated with @constructor", numDiagnostics == 0);
  }

  private void testFunctionAnnotatedWithThis(CheckGlobalThis checker, String sourceCode) {
    // Test function annotated with @this
    String thisSourceCode = sourceCode.replaceAll("function Ctor() {", "/** @this {Object} */ function Ctor() {")
        .replaceAll("Ctor.prototype.method = function() {", "Ctor.prototype.method = function() {/** @this {Object} */}");

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(new TestSourceFile("test.js", thisSourceCode), options);
    Node program = compiler.parseTree();

    NodeTraversal traverser = new NodeTraversal(program, checker);
    traverser.traverse();

    int numDiagnostics = compiler.getDiagnostics().length;

    assertTrue("Expected no warning for method annotated with @this", numDiagnostics == 0);
  }

  private void testFunctionWithNoLogicalPlaceToPutThisAnnotation(CheckGlobalThis checker, String sourceCode) {
    // Test function where there's no logical place to put a @this annotation
    String noLogicalPlaceToPutThisAnnotationSourceCode = sourceCode
        .replaceAll("function Ctor() {", "function Ctor() { /** @this {Object} */ }")
        .replaceAll("Ctor.prototype.method = function() {", "var method = function() { /** @this {Object} */ }; Ctor.prototype.method = method;");

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(new TestSourceFile("test.js", noLogicalPlaceToPutThisAnnotationSourceCode), options);
    Node program = compiler.parseTree();

    NodeTraversal traverser = new NodeTraversal(program, checker);
    traverser.traverse();

    int numDiagnostics = compiler.getDiagnostics().length;

    assertTrue("Expected no warning for method with no logical place to put @this annotation", numDiagnostics == 0);
  }

  private void testFunctionThatIsLentToPrototype(CheckGlobalThis checker, String sourceCode) {
    // Test function being lent to a prototype
    String prototypeSourceCode = sourceCode
        .replaceAll("Ctor7.prototype = obj;", "Ctor7.prototype = Object.create(obj);")
        .replaceAll("Ctor8.prototype = obj;", "Ctor8.prototype = Object.create(obj);");

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(new TestSourceFile("test.js", prototypeSourceCode), options);
    Node program = compiler.parseTree();

    NodeTraversal traverser = new NodeTraversal(program, checker);
    traverser.traverse();

    int numDiagnostics = compiler.getDiagnostics().length;

    assertTrue("Expected no warning for functions being lent to a prototype", numDiagnostics == 0);
  }

  private void testFunctionAnnotatedWithOverride(CheckGlobalThis checker, String sourceCode) {
    // Test function annotated with @override
    String overrideSourceCode = sourceCode
        .replaceAll("function Ctor3() {", "/** @override */ function Ctor3() {")
        .replaceAll("Ctor3.prototype.method = function() {", "Ctor3.prototype.method = function() {/** @this {Object} */}");

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(new TestSourceFile("test.js", overrideSourceCode), options);
    Node program = compiler.parseTree();

    NodeTraversal traverser = new NodeTraversal(program, checker);
    traverser.traverse();

    int numDiagnostics = compiler.getDiagnostics().length;

    assertTrue("Expected no warning for method annotated with @override", numDiagnostics == 0);
  }

  private void testFunctionWithGetProperty(CheckGlobalThis checker, String sourceCode) {
    // Test function with getProperty
    String getPropertySourceCode = sourceCode
        .replaceAll("function Ctor() {", "function Ctor() {")
        .replaceAll("Ctor.prototype.method = function() { getProp(); }", "Ctor.prototype.method = function() { this.getProp(); }")
        .replaceAll("function getProp() {", "function getProp() { /** @this {Object} */ }");

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(new TestSourceFile("test.js", getPropertySourceCode), options);
    Node program = compiler.parseTree();

    NodeTraversal traverser = new NodeTraversal(program, checker);
    traverser.traverse();

    int numDiagnostics = compiler.getDiagnostics().length;

    assertTrue("Expected no warning for function with getProperty", numDiagnostics == 0);
  }

  private void testFunctionWithAssignment(CheckGlobalThis checker, String sourceCode) {
    // Test function with assignment
    String assignmentSourceCode = sourceCode
        .replaceAll("function Ctor() {", "function Ctor() {")
        .replaceAll("var a = this; a.useful = undefined;", "this.useful = undefined;");

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(new TestSourceFile("test.js", assignmentSourceCode), options);
    Node program = compiler.parseTree();

    NodeTraversal traverser = new NodeTraversal(program, checker);
    traverser.traverse();

    int numDiagnostics = compiler.getDiagnostics().length;

    assertTrue("Expected no warning for function with assignment", numDiagnostics == 0);
  }

  private void testFunctionWithLhsChild(CheckGlobalThis checker, String sourceCode) {
    // Test function with lhsChild
    String lhsChildSourceCode = sourceCode
        .replaceAll("function evil() { this.useful = undefined; }", "function evil() { this.useful = this.useful + 1; }")
        .replaceAll("function good() { var a = this; a.useful = undefined; }", "function good() { var a = this; a.useful = a.useful + 1; }");

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(new TestSourceFile("test.js", lhsChildSourceCode), options);
    Node program = compiler.parseTree();

    NodeTraversal traverser = new NodeTraversal(program, checker);
    traverser.traverse();

    int numDiagnostics = compiler.getDiagnostics().length;

    assertTrue("Expected no warning for function with lhsChild", numDiagnostics == 0);
  }

  private void testFunctionWithLhsChildInFunction(CheckGlobalThis checker, String sourceCode) {
    // Test function with lhsChild in function
    String lhsChildInFunctionSourceCode = sourceCode
        .replaceAll("function evil() { this.useful = undefined; }", "function evil() { return this.useful + 1; }")
        .replaceAll("function good() { var a = this; a.useful = undefined; }", "function good() { return a.useful + 1; }");

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(new TestSourceFile("test.js", lhsChildInFunctionSourceCode), options);
    Node program = compiler.parseTree();

    NodeTraversal traverser = new NodeTraversal(program, checker);
    traverser.traverse();

    int numDiagnostics = compiler.getDiagnostics().length;

    assertTrue("Expected no warning for function with lhsChild in function", numDiagnostics == 0);
  }

  private void testFunctionWithLhsChildInObjectLiteral(CheckGlobalThis checker, String sourceCode) {
    // Test function with lhsChild in object literal
    String lhsChildInObjectLiteralSourceCode = sourceCode
        .replaceAll("function evil() { this.useful = undefined; }", "var obj = { evil: function() { this.useful = undefined; } }")
        .replaceAll("function good() { var a = this; a.useful = undefined; }", "var obj = { good: function() { var a = this; a.useful = undefined; } }");

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(new TestSourceFile("test.js", lhsChildInObjectLiteralSourceCode), options);
    Node program = compiler.parseTree();

    NodeTraversal traverser = new NodeTraversal(program, checker);
    traverser.traverse();

    int numDiagnostics = compiler.getDiagnostics().length;

    assertTrue("Expected no warning for function with lhsChild in object literal", numDiagnostics == 0);
  }

  private void testFunctionWithLhsChildInAssignment(CheckGlobalThis checker, String sourceCode) {
    // Test function with lhsChild in assignment
    String lhsChildInAssignmentSourceCode = sourceCode
        .replaceAll("function evil() { this.useful = undefined; }", "function evil() { this.useful = this.useful + 1; }")
        .replaceAll("function good() { var a = this; a.useful = undefined; }", "function good() { this.useful = this.useful + 1; }")
        .replaceAll("evil();", "evil(); this.useful = this.useful + 1;")
        .replaceAll("good();", "good(); this.useful = this.useful + 1;");

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(new TestSourceFile("test.js", lhsChildInAssignmentSourceCode), options);
    Node program = compiler.parseTree();

    NodeTraversal traverser = new NodeTraversal(program, checker);
    traverser.traverse();

    int numDiagnostics = compiler.getDiagnostics().length;

    assertTrue("Expected no warning for function with lhsChild in assignment", numDiagnostics == 0);
  }
}