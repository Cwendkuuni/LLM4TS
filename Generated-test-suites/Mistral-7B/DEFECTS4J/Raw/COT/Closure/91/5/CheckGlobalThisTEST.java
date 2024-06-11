import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.JSDocInfo;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    String sourceCode = ""
        + "function evil1() { this.useful = undefined; }"
        + "function evil2() { var a = this; a.useful = undefined; }"
        + "function good() { this.useful = undefined; }" // constructor
        + "function goodConstructor() {}"
        + "function goodPrototype() { this.useful = undefined; }" // prototype method
        + "function goodPrototype2() {}" // prototype method without this
        + "function goodFunction() { this.useful = undefined; }" // function with this annotated
        + "function goodFunction2() {}" // function with no logical place to put this
        + "function global() { return this; }";

    Compiler compiler = new Compiler();
    Checker checker = new Checker(compiler, new Diagnostics());
    Parser parser = new Parser(new TestSourceFile("test.js", sourceCode));
    Node root = parser.parse();

    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, CHECK_SEMANTIC);
    CheckGlobalThis checkerUnderTest = new CheckGlobalThis(abstractCompiler, CHECK_SEMANTIC);
    NodeTraversal traversal = new NodeTraversal(root, checkerUnderTest);

    traversal.traverse();
    Diagnostics diagnostics = checker.getDiagnostics();

    assertEquals(1, diagnostics.getNumErrors());
    assertEquals("dangerous use of the global 'this' object", diagnostics.getError(0).getDescription());
    assertEquals("test.js", diagnostics.getError(0).getSourceName());
    assertEquals(10, diagnostics.getError(0).getStartPosition().getLineNumber());
    assertEquals(11, diagnostics.getError(0).getStartPosition().getCharacterOffsetInLine());

    // Test safe cases
    sourceCode = "function constructor() {}"
        + "function prototype() { this.useful = undefined; }"
        + "function annotated() { @this function() {} }"
        + "var x = function() {};"
        + "function noPlaceToPutThis() { return {'useful': function() {}}; }";

    compiler = new Compiler();
    checker = new Checker(compiler, new Diagnostics());
    parser = new Parser(new TestSourceFile("test.js", sourceCode));
    root = parser.parse();

    abstractCompiler = new AbstractCompiler(compiler, CHECK_SEMANTIC);
    checkerUnderTest = new CheckGlobalThis(abstractCompiler, CHECK_SEMANTIC);
    traversal = new NodeTraversal(root, checkerUnderTest);

    traversal.traverse();
    diagnostics = checker.getDiagnostics();

    assertEquals(0, diagnostics.getNumErrors());

    // Test exceptions
    sourceCode = "function throwException() { throw new Error('Test'); }";

    compiler = new Compiler();
    checker = new Checker(compiler, new Diagnostics());
    parser = new Parser(new TestSourceFile("test.js", sourceCode));
    root = parser.parse();

    abstractCompiler = new AbstractCompiler(compiler, CHECK_SEMANTIC);
    checkerUnderTest = new CheckGlobalThis(abstractCompiler, CHECK_SEMANTIC);
    traversal = new NodeTraversal(root, checkerUnderTest);

    try {
      traversal.traverse();
      fail("Expected exception");
    } catch (RuntimeException e) {
      // expected
    }
  }
}