import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.Parser.ParseTree;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    CheckerFactory factory = new CheckerFactory();
    Checker checker = factory.getChecker("none", null);
    Compiler compiler = new Compiler();
    compiler.setChecker(checker);
    AbstractCompiler abstractCompiler = compiler.getOptions().getCompiler();
    CheckGlobalThis checkerInstance = new CheckGlobalThis(abstractCompiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(compiler.getRootNode(), checkerInstance);

    String sourceCode = "function evil() { this.useful = undefined; }";
    ParseTree tree = Parser.parse(sourceCode, null, null);
    Diagnostics diagnostics = compiler.compile(tree);

    assertFalse(diagnostics.getErrors().isEmpty());
    assertEquals(1, diagnostics.getErrors().size());
    assertEquals("dangerous use of the global 'this' object", diagnostics.getErrors().get(0).getMessage());
  }

  @Test
  public void testCheckGlobalThisWithPrototypeAccess() {
    CheckerFactory factory = new CheckerFactory();
    Checker checker = factory.getChecker("none", null);
    Compiler compiler = new Compiler();
    compiler.setChecker(checker);
    AbstractCompiler abstractCompiler = compiler.getOptions().getCompiler();
    CheckGlobalThis checkerInstance = new CheckGlobalThis(abstractCompiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(compiler.getRootNode(), checkerInstance);

    String sourceCode = "var obj = { evil: function() { this.useful = undefined; } }";
    ParseTree tree = Parser.parse(sourceCode, null, null);
    Diagnostics diagnostics = compiler.compile(tree);

    assertTrue(diagnostics.getWarnings().isEmpty());
  }

  @Test
  public void testCheckGlobalThisWithConstructorFunction() {
    CheckerFactory factory = new CheckerFactory();
    Checker checker = factory.getChecker("none", null);
    Compiler compiler = new Compiler();
    compiler.setChecker(checker);
    AbstractCompiler abstractCompiler = compiler.getOptions().getCompiler();
    CheckGlobalThis checkerInstance = new CheckGlobalThis(abstractCompiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(compiler.getRootNode(), checkerInstance);

    String sourceCode = "function EvilConstructor() { this.useful = undefined; }";
    ParseTree tree = Parser.parse(sourceCode, null, null);
    Diagnostics diagnostics = compiler.compile(tree);

    assertTrue(diagnostics.getWarnings().isEmpty());
  }

  @Test
  public void testCheckGlobalThisWithThisAnnotation() {
    CheckerFactory factory = new CheckerFactory();
    Checker checker = factory.getChecker("none", null);
    Compiler compiler = new Compiler();
    compiler.setChecker(checker);
    AbstractCompiler abstractCompiler = compiler.getOptions().getCompiler();
    CheckGlobalThis checkerInstance = new CheckGlobalThis(abstractCompiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(compiler.getRootNode(), checkerInstance);

    String sourceCode = "function EvilFunction(@this) { this.useful = undefined; }";
    ParseTree tree = Parser.parse(sourceCode, null, null);
    Diagnostics diagnostics = compiler.compile(tree);

    assertTrue(diagnostics.getWarnings().isEmpty());
  }

  @Test
  public void testCheckGlobalThisWithLogicalPlaceForThisAnnotation() {
    CheckerFactory factory = new CheckerFactory();
    Checker checker = factory.getChecker("none", null);
    Compiler compiler = new Compiler();
    compiler.setChecker(checker);
    AbstractCompiler abstractCompiler = compiler.getOptions().getCompiler();
    CheckGlobalThis checkerInstance = new CheckGlobalThis(abstractCompiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(compiler.getRootNode(), checkerInstance);

    String sourceCode = "function EvilFunction() { this.useful = undefined; } var a = { useful: function() {}}";
    ParseTree tree = Parser.parse(sourceCode, null, null);
    Diagnostics diagnostics = compiler.compile(tree);

    assertTrue(diagnostics.getWarnings().isEmpty());
  }

  @Test(expected = AssertionError.class)
  public void testCheckGlobalThisWithAssignLhs() {
    CheckerFactory factory = new CheckerFactory();
    Checker checker = factory.getChecker("none", null);
    Compiler compiler = new Compiler();
    compiler.setChecker(checker);
    AbstractCompiler abstractCompiler = compiler.getOptions().getCompiler();
    CheckGlobalThis checkerInstance = new CheckGlobalThis(abstractCompiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(compiler.getRootNode(), checkerInstance);

    String sourceCode = "var a = this;";
    ParseTree tree = Parser.parse(sourceCode, null, null);
    Diagnostics diagnostics = compiler.compile(tree);

    assertFalse(diagnostics.getErrors().isEmpty());
    assertEquals(1, diagnostics.getErrors().size());
    assertEquals("dangerous use of the global 'this' object", diagnostics.getErrors().get(0).getMessage());
  }

  @Test(expected = AssertionError.class)
  public void testCheckGlobalThisWithAssignRhs() {
    CheckerFactory factory = new CheckerFactory();
    Checker checker = factory.getChecker("none", null);
    Compiler compiler = new Compiler();
    compiler.setChecker(checker);
    AbstractCompiler abstractCompiler = compiler.getOptions().getCompiler();
    CheckGlobalThis checkerInstance = new CheckGlobalThis(abstractCompiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(compiler.getRootNode(), checkerInstance);

    String sourceCode = "this = 5;";
    ParseTree tree = Parser.parse(sourceCode, null, null);
    Diagnostics diagnostics = compiler.compile(tree);

    assertFalse(diagnostics.getErrors().isEmpty());
    assertEquals(1, diagnostics.getErrors().size());
    assertEquals("dangerous use of the global 'this' object", diagnostics.getErrors().get(0).getMessage());
  }

  @Test
  public void testCheckGlobalThisWithNestedAssignments() {
    CheckerFactory factory = new CheckerFactory();
    Checker checker = factory.getChecker("none", null);
    Compiler compiler = new Compiler();
    compiler.setChecker(checker);
    AbstractCompiler abstractCompiler = compiler.getOptions().getCompiler();
    CheckGlobalThis checkerInstance = new CheckGlobalThis(abstractCompiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(compiler.getRootNode(), checkerInstance);

    String sourceCode = "(a = this).property = 5;";
    ParseTree tree = Parser.parse(sourceCode, null, null);
    Diagnostics diagnostics = compiler.compile(tree);

    assertTrue(diagnostics.getWarnings().isEmpty());
  }

  @Test
  public void testCheckGlobalThisWithPrototypeChain() {
    CheckerFactory factory = new CheckerFactory();
    Checker checker = factory.getChecker("none", null);
    Compiler compiler = new Compiler();
    compiler.setChecker(checker);
    AbstractCompiler abstractCompiler = compiler.getOptions().getCompiler();
    CheckGlobalThis checkerInstance = new CheckGlobalThis(abstractCompiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(compiler.getRootNode(), checkerInstance);

    String sourceCode = "function F() { this.x = 5; } F.prototype.y = function() { this.x = 10; }";
    ParseTree tree = Parser.parse(sourceCode, null, null);
    Diagnostics diagnostics = compiler.compile(tree);

    assertTrue(diagnostics.getWarnings().isEmpty());
  }

  @Test
  public void testCheckGlobalThisWithGetters() {
    CheckerFactory factory = new CheckerFactory();
    Checker checker = factory.getChecker("none", null);
    Compiler compiler = new Compiler();
    compiler.setChecker(checker);
    AbstractCompiler abstractCompiler = compiler.getOptions().getCompiler();
    CheckGlobalThis checkerInstance = new CheckGlobalThis(abstractCompiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(compiler.getRootNode(), checkerInstance);

    String sourceCode = "var obj = { get x() { return this.y; } } obj.y = 5;";
    ParseTree tree = Parser.parse(sourceCode, null, null);
    Diagnostics diagnostics = compiler.compile(tree);

    assertTrue(diagnostics.getWarnings().isEmpty());
  }
}