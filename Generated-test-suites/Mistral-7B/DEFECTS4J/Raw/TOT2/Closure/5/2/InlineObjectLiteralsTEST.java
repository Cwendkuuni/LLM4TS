import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.InlineObjectLiterals;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiteral() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        InlineObjectLiterals inlineObjectLiterals =
            new InlineObjectLiterals(compiler, () -> "testVarName");
        inlineObjectLiterals.process(externs, root);
      }
    });

    String originalCode = "function foo() {" +
      "  var obj = {" +
      "    a: 1," +
      "    b: function() { return 'bar'; }," +
      "    c: 3" +
      "  };" +
      "  return obj.a + obj.b();" +
      "}";

    String expectedCode = "function foo() {" +
      "  var a = 1, b, c = 3;" +
      "  b = function() { return 'bar'; }" +
      "  return a + b;" +
      "}";

    String compiledCode = compiler.compile(originalCode, options).getCompiledCode();
    assertEquals(expectedCode, compiledCode);
  }

  @Test
  public void testInlineObjectLiteralWithEmptyInitialization() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        InlineObjectLiterals inlineObjectLiterals =
            new InlineObjectLiterals(compiler, () -> "testVarName");
        inlineObjectLiterals.process(externs, root);
      }
    });

    String originalCode = "function foo() {" +
      "  var obj = {}";

    String expectedCode = "function foo() {" +
      "  var testVarName = undefined;" +
      "}";

    String compiledCode = compiler.compile(originalCode, options).getCompiledCode();
    assertEquals(expectedCode, compiledCode);
  }

  @Test
  public void testInlineObjectLiteralWithCyclicReference() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        InlineObjectLiterals inlineObjectLiterals =
            new InlineObjectLiterals(compiler, () -> "testVarName");
        inlineObjectLiterals.process(externs, root);
      }
    });

    String originalCode = "function foo() {" +
      "  var obj1 = {" +
      "    a: 1," +
      "    b: obj2" +
      "  };" +
      "  var obj2 = {" +
      "    b: obj1" +
      "  };" +
      "  return obj1.a + obj2.b;" +
      "}";

    String expectedCode = "function foo() {" +
      "  var a = 1, b, testVarName1, testVarName2;" +
      "  testVarName1 = {" +
      "    a: 1," +
      "    b: testVarName2" +
      "  };" +
      "  testVarName2 = {" +
      "    b: testVarName1" +
      "  };" +
      "  return a + b;" +
      "}";

    String compiledCode = compiler.compile(originalCode, options).getCompiledCode();
    assertEquals(expectedCode, compiledCode);
  }

  @Test
  public void testInlineObjectLiteralWithGetters() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        InlineObjectLiterals inlineObjectLiterals =
            new InlineObjectLiterals(compiler, () -> "testVarName");
        inlineObjectLiterals.process(externs, root);
      }
    });

    String originalCode = "function foo() {" +
      "  var obj = {" +
      "    getX: function() { return 1; }" +
      "  };" +
      "  return obj.getX();" +
      "}";

    String expectedCode = "function foo() {" +
      "  var getX = function() { return 1; }" +
      "  return getX();" +
      "}";

    String compiledCode = compiler.compile(originalCode, options).getCompiledCode();
    assertEquals(expectedCode, compiledCode);
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectLiteralWithSelfReference() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        InlineObjectLiterals inlineObjectLiterals =
            new InlineObjectLiterals(compiler, () -> "testVarName");
        inlineObjectLiterals.process(externs, root);
      }
    });

    String originalCode = "function foo() {" +
      "  var obj = {" +
      "    getX: function() { return this.x; }," +
      "    x: 1" +
      "  };" +
      "  return obj.getX();" +
      "}";

    String compiledCode = "";
    try {
      compiledCode = compiler.compile(originalCode, options).getCompiledCode();
    } catch (RuntimeException e) {
      compiledCode = e.getMessage();
    }

    String expectedErrorMessage = "TypeError: Circular reference detected:";
    assertEquals(expectedErrorMessage, compiledCode);
  }

  @Test
  public void testInlineObjectLiteralWithDeepNestedObject() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        InlineObjectLiterals inlineObjectLiterals =
            new InlineObjectLiterals(compiler, () -> "testVarName");
        inlineObjectLiterals.process(externs, root);
      }
    });

    String originalCode = "function foo() {" +
      "  var obj = {" +
      "    a: 1," +
      "    b: {" +
      "      c: 2," +
      "      d: {" +
      "        e: 3" +
      "      }" +
      "    }" +
      "  };" +
      "  return obj.b.d.e;" +
      "}";

    String expectedCode = "function foo() {" +
      "  var a = 1, b, c, d, e;" +
      "  b = {" +
      "    c: 2," +
      "    d: {" +
      "      e: 3" +
      "    }" +
      "  };" +
      "  return e;" +
      "}";

    String compiledCode = compiler.compile(originalCode, options).getCompiledCode();
    assertEquals(expectedCode, compiledCode);
  }

  @Test
  public void testInlineObjectLiteralWithComplexObject() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        InlineObjectLiterals inlineObjectLiterals =
            new InlineObjectLiterals(compiler, () -> "testVarName");
        inlineObjectLiterals.process(externs, root);
      }
    });

    String originalCode = "function foo() {" +
      "  var obj = {" +
      "    a: 1," +
      "    b: [" +
      "      1, 2," +
      "      { i: 3 }" +
      "    ], " +
      "    c: function() { return 1; }," +
      "    d: [" +
      "      { i: 1 }," +
      "      function() { return 2; }" +
      "    ]" +
      "  };" +
      "  return obj.b[1].i + obj.d[1]();" +
      "}";

    String expectedCode = "function foo() {" +
      "  var a = 1, b, c, d, e, i1, i2, i3;" +
      "  b = [1, 2];" +
      "  e = {" +
      "    i: 3" +
      "  };" +
      "  c = function() { return 1; }" +
      "  d = [{" +
      "    i: 1," +
      "    getI: function() { return 2; }" +
      "  }]" +
      "  return b[1] * i1 + i2();" +
      "}";

    String compiledCode = compiler.compile(originalCode, options).getCompiledCode();
    assertEquals(expectedCode, compiledCode);
  }
}