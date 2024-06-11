import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    AbstractCompiler compiler = new TestCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    CompilerInput input = NodeFactory.createCompilationUnit("(function() {})()");
    checkSideEffects.process(input, null);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    input = NodeFactory.createCompilationUnit(
        "function x() {}\n" +
        "var s = \"this string is \";\n" +
        "var s2 = s + 'continued on the next line but you forgot the +\';\n" +
        "x == foo();\n" +
        "foo();;\n");
    checkSideEffects.process(input, null);
    assertFalse(checkSideEffects.problemNodes.isEmpty());

    input = NodeFactory.createCompilationUnit(
        "function foo() { return 42; }\n" +
        "function x() { foo(); }\n");
    checkSideEffects.process(input, null);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    input = NodeFactory.createCompilationUnit(
        "function foo() { return 42; }\n" +
        "function x() { }\n" +
        "x.foo = foo;");
    checkSideEffects.process(input, null);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    input = NodeFactory.createCompilationUnit(
        "function foo() { return 42; }\n" +
        "function x() { x.foo = foo(); }\n");
    checkSideEffects.process(input, null);
    assertFalse(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testProtectSideEffects() {
    AbstractCompiler compiler = new TestCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    CompilerInput input = NodeFactory.createCompilationUnit("(function() {})()");
    checkSideEffects.process(input, null);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    input = NodeFactory.createCompilationUnit("var x = 42;");
    checkSideEffects.process(input, null);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    input = NodeFactory.createCompilationUnit("function foo() { return 42; }\n" +
        "function x() { foo(); }\n");
    checkSideEffects.process(input, null);
    assertFalse(checkSideEffects.problemNodes.isEmpty());

    checkSideEffects.protectSideEffects();
    input = NodeFactory.createCompilationUnit("function foo() { return 42; }\n" +
        "function x() { JSCOMPILER_PRESERVE(foo()); }\n");
    checkSideEffects.process(input, null);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  static class TestCompiler extends AbstractCompiler {
    @Override
    public CompilerPass getPass(String name) {
      if (StripProtection.class.getName().equals(name)) {
        return new StripProtection(this);
      }
      return super.getPass(name);
    }
  }
}