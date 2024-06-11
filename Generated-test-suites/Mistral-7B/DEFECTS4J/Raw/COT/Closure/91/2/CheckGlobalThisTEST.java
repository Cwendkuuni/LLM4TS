import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.Warning;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testNormalFunction() {
    String code = "function test() {}";
    Checker checker = new Checker();
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = checkFunction(checker, compiler, code);
    assertTrue(diagnostics.getNumberOfErrors() == 0);
  }

  @Test
  public void testConstructorFunction() {
    String code = "function Test() {}";
    Checker checker = new Checker();
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = checkFunction(checker, compiler, code);
    assertTrue(diagnostics.getNumberOfErrors() == 0);
  }

  @Test
  public void testFunctionWithThisAnnotation() {
    String code = "function test() { @this }";
    Checker checker = new Checker();
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = checkFunction(checker, compiler, code);
    assertTrue(diagnostics.getNumberOfErrors() == 0);
  }

  @Test
  public void testFunctionWithOverrideAnnotation() {
    String code = "function test() { @override }";
    Checker checker = new Checker();
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = checkFunction(checker, compiler, code);
    assertTrue(diagnostics.getNumberOfErrors() == 0);
  }

  @Test
  public void testFunctionInObjectLiteral() {
    String code = "var obj = { test: function() {} }";
    Checker checker = new Checker();
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = checkFunction(checker, compiler, code);
    assertTrue(diagnostics.getNumberOfErrors() == 0);
  }

  @Test
  public void testFunctionWithPrototypeGetProperty() {
    String code = "function test() {} var p = {get x() { this.test = 1; }}; p.x";
    Checker checker = new Checker();
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = checkFunction(checker, compiler, code);
    assertTrue(diagnostics.getNumberOfErrors() > 0);
  }

  @Test
  public void testFunctionWithPrototypeSetProperty() {
    String code = "function test() {} var p = {set x(v) { this.test = v; }}; p.x = 1";
    Checker checker = new Checker();
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = checkFunction(checker, compiler, code);
    assertTrue(diagnostics.getNumberOfErrors() > 0);
  }

  @Test
  public void testFunctionWithPrototypeChain() {
    String code = "function test() {} var p1 = {}; var p2 = { get x() { return this.test; } }; p1.x = p2;";
    Checker checker = new Checker();
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = checkFunction(checker, compiler, code);
    assertTrue(diagnostics.getNumberOfErrors() > 0);
  }

  @Test
  public void testFunctionWithPrototypeSetChain() {
    String code = "function test() {} var p1 = {}; var p2 = { set x(v) { this.test = v; } }; p1.x = p2; p1.x = 1";
    Checker checker = new Checker();
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = checkFunction(checker, compiler, code);
    assertTrue(diagnostics.getNumberOfErrors() > 0);
  }

  @Test
  public void testFunctionWithPrototypeChainSet() {
    String code = "function test() {} var p1 = {}; var p2 = { get x() { return this.test; }, set x(v) { this.test = v; } }; p1.x = p2; p1.x = 1";
    Checker checker = new Checker();
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = checkFunction(checker, compiler, code);
    assertTrue(diagnostics.getNumberOfErrors() > 0);
  }

  @Test
  public void testFunctionWithPrototypeChainSetGet() {
    String code = "function test() {} var p1 = {}; var p2 = { get x() { return this.test; }, set x(v) { this.test = v; } }; p1.x = p2; p1.x = 1; var v = p1.x";
    Checker checker = new Checker();
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = checkFunction(checker, compiler, code);
    assertTrue(diagnostics.getNumberOfErrors() > 0);
  }

  @Test
  public void testFunctionWithParentFunctionAssignment() {
    String code = "function parent() { this.test = function() {} } parent.test";
    Checker checker = new Checker();
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = checkFunction(checker, compiler, code);
    assertTrue(diagnostics.getNumberOfErrors() > 0);
  }

  @Test
  public void testFunctionWithParentFunctionAssignmentSet() {
    String code = "function parent() { this.test = function() {} } parent.test = 1";
    Checker checker = new Checker();
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = checkFunction(checker, compiler, code);
    assertTrue(diagnostics.getNumberOfErrors() > 0);
  }

  @Test
  public void testFunctionWithParentFunctionGet() {
    String code = "function parent() { this.test = function() {} }; function child() { return parent.test(); }";
    Checker checker = new Checker();
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = checkFunction(checker, compiler, code);
    assertTrue(diagnostics.getNumberOfErrors() > 0);
  }

  @Test
  public void testFunctionWithParentFunctionGetAssignment() {
    String code = "function parent() { this.test = function() {} }; function child() { parent.test = function() {}; return parent.test(); }";
    Checker checker = new Checker();
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = checkFunction(checker, compiler, code);
    assertTrue(diagnostics.getNumberOfErrors() > 0);
  }

  @Test
  public void testFunctionWithParentFunctionAssignmentGet() {
    String code = "function parent() { this.test = function() {} }; function child() { parent.test = function() {}; return parent.test(); }";
    Checker checker = new Checker();
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = checkFunction(checker, compiler, code);
    assertTrue(diagnostics.getNumberOfErrors() > 0);
  }

  private Diagnostics checkFunction(Checker checker, Compiler compiler, String code) {
    Parser parser = new Parser();
    Node root = parser.parse(code, null, null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(checker, Warning);
    compiler.process(root, null, checkGlobalThis);
    return checker.getDiagnostics();
  }
}