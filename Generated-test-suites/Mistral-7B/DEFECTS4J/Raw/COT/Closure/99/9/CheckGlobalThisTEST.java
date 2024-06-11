import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

/**
 * Test class for CheckGlobalThis.
 */
@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  private static final DiagnosticType GLOBAL_THIS = DiagnosticType.warning(
      "JSC_USED_GLOBAL_THIS",
      "dangerous use of the global 'this' object");

  @Test
  public void testCheckGlobalThis() {
    AbstractCompiler compiler = new Compiler();
    Checker checker = new Checker(new SourceFile("Test.js", "test"), new CompilerOptions());
    Checker.Phase phase = checker.getPhase(Checker.Phase.CHECK_GLOBAL);
    CheckGlobalThis check = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(check, phase, checker.getRootNode());

    // Basic functionality test
    testFunctionDeclaration(traversal, "function evil() { this.useful = undefined; }");
    testFunctionExpression(traversal, "var evil = function() { this.useful = undefined; }");
    testVariableDeclaration(traversal, "var a = function() { this.useful = undefined; }");
    testVariableDeclarationWithFunctionExpression(traversal, "var a = function() { }; a.useful = undefined;");
    testFunctionWithPrototypeProperty(traversal, "function evil() { Object.prototype.property = this; }");
    testFunctionWithPrototypeSubproperty(traversal, "function evil() { Object.prototype.prototype.subproperty = this; }");

    // Exception handling test
    testFunctionWithTryCatch(traversal, "function evil() { try { this.useful = undefined; } catch (e) {} }");
    testFunctionWithThrow(traversal, "function evil() { throw new Error(); this.useful = undefined; }");

    // Constructor test
    testFunctionWithConstructorAnnotation(traversal, "function EvilConstructor() {}");

    // @this test
    testFunctionWithThisAnnotation(traversal, "function evil@this() { this.useful = undefined; }");

    // Functions inside a block, script or name node test
    testFunctionInsideBlock(traversal, "var a = function() { this.useful = undefined; }");
    testFunctionInsideScript(traversal, "function evil() { this.useful = undefined; }");
    testFunctionInsideNameNode(traversal, "var a = function evil() { this.useful = undefined; }");

    // Functions with no logical place to put a @this annotation test
    testFunctionWithNoThisAnnotation(traversal, "function a() {}");
    testFunctionWithNoThisAnnotationInsideObjectLiteral(traversal, "var a = { foo: function() { this.useful = undefined; } }");

    // Assignments test
    testAssignmentToVariable(traversal, "var a = this;");
    testAssignmentToFunctionExpression(traversal, "var evil = function() { this.useful = undefined; }; a = this;");
    testAssignmentToFunctionDeclaration(traversal, "function evil() { this.useful = undefined; }; var a = this;");
    testAssignmentToPrototypeProperty(traversal, "var a = { prototype: { property: function() { this.useful = undefined; } } }; Object.prototype.property = this; a.prototype.property = this;");

    // Nested assignment test
    testNestedAssignment(traversal, "(a = this).property = c;");

    // Edge cases test
    testFunctionWithThisInsideObjectLiteral(traversal, "var a = { foo: function() { this.useful = undefined; } }; this.foo = function() { this.useful = undefined; }");
    testFunctionWithThisInsideArrayLiteral(traversal, "var a = [function() { this.useful = undefined; }]; this[0]();");
    testFunctionWithThisInsideExpressionStatement(traversal, "this.useful = function() {};");
    testFunctionWithThisInsideIfStatement(traversal, "if (true) { this.useful = function() {}; }");

    check.checkTree(compiler.getDiagnostics(), level -> level.warningOrErrorEquals(GLOBAL_THIS));
    assertEquals("Expected 1 warning(s).", 1, compiler.getDiagnostics().size());
  }

  // Helper methods for generating JUnit test cases

  private void testFunctionDeclaration(NodeTraversal traversal, String code) {
    SourceFile sourceFile = new SourceFile("Test.js", code);
    Node rootNode = traversal.traverse(sourceFile.getTree(), null);
    traversal.traverse(rootNode);
  }

  // ... other helper methods for generating JUnit test cases ...

}