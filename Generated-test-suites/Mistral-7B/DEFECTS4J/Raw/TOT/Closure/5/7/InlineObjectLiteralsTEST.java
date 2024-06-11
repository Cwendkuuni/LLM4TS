import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    String sourceCode = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: function() {" +
        "      return 2;" +
        "    }," +
        "    c: [1, 2]," +
        "    d: null," +
        "    e: {}," +
        "  };" +
        "  var x = obj.a;" +
        "  var y = obj.b();" +
        "  var z = obj.c[0];" +
        "  var w = obj.e.f;" +
        "}" +
        "function bar() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 1," +
        "    c: null," +
        "  };" +
        "  var x = obj.a;" +
        "  var y = obj.b;" +
        "}" +
        "function baz() {" +
        "  var obj = {};" +
        "  var x = obj;" +
        "}" ;

    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiteralsPass = new InlineObjectLiterals(compiler, () -> "tmp_");
    Node root = NodePrinter.printNodeToAst(sourceCode);
    inlineObjectLiteralsPass.process(null, root);
    String result = NodePrinter.printNodeToCompilableJs(root);

    String expectedCode = "function foo() {" +
        "  var a = 1;" +
        "  var b = function b() {" +
        "      return 2;" +
        "  };" +
        "  var c = [1, 2];" +
        "  var d = null;" +
        "  var e = null;" +
        "  var f = null;" +
        "  var x = a;" +
        "  var y = b();" +
        "  var z = c[0];" +
        "  var w = f;" +
        "}" +
        "function foo_a() {" +
        "  return 1;" +
        "}" +
        "function foo_b() {" +
        "  return 2;" +
        "}" +
        "function bar() {" +
        "  var a = 1;" +
        "  var b = 1;" +
        "  var c = null;" +
        "  var x = a;" +
        "  var y = b;" +
        "}" +
        "function baz() {" +
        "  var x = null;" +
        "}" +
        "function baz_x() {" +
        "  return null;" +
        "}" ;

    assertEquals(expectedCode, result);
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReferentialAssignments() {
    String sourceCode = "function foo() {" +
        "  var obj = {" +
        "    a: {" +
        "      b: obj.a" +
        "    }" +
        "  };" +
        "  var x = obj.a.b;" +
        "}" ;

    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiteralsPass = new InlineObjectLiterals(compiler, () -> "tmp_");
    Node root = NodePrinter.printNodeToAst(sourceCode);
    inlineObjectLiteralsPass.process(null, root);
    String result = NodePrinter.printNodeToCompilableJs(root);
    String expectedCode = "function foo() {" +
        "  var a = {" +
        "    b: null" +
        "  };" +
        "  var x = a.b;" +
        "  function foo_a() {" +
        "    return null;" +
        "  }" +
        "  function foo_a_b() {" +
        "    return a.b;" +
        "  }" +
        "}" ;

    assertEquals(expectedCode, result);
  }

  @Test
  public void testInlineObjectLiteralsWithUnassignedProperties() {
    String sourceCode = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: {}" +
        "  };" +
        "  var x = obj.b.c;" +
        "}" ;

    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiteralsPass = new InlineObjectLiterals(compiler, () -> "tmp_");
    Node root = NodePrinter.printNodeToAst(sourceCode);
    inlineObjectLiteralsPass.process(null, root);
    String result = NodePrinter.printNodeToCompilableJs(root);

    String expectedCode = "function foo() {" +
        "  var a = 1;" +
        "  var b = null;" +
        "  var c = null;" +
        "  var x = c;" +
        "}" +
        "function foo_a() {" +
        "  return 1;" +
        "}" ;

    assertEquals(expectedCode, result);
  }

  @Test
  public void testInlineObjectLiteralsWithGettersAndSetters() {
    String sourceCode = "function foo() {" +
        "  var obj = {" +
        "    get a() {" +
        "      return 1;" +
        "    }," +
        "    set a(val) {" +
        "      this.a = val;" +
        "    }" +
        "  };" +
        "  Object.defineProperty(obj, 'a', {" +
        "    get: function() {" +
        "      return 2;" +
        "    }" +
        "  });" +
        "  obj.a = 3;" +
        "  var x = obj.a;" +
        "}" ;

    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiteralsPass = new InlineObjectLiterals(compiler, () -> "tmp_");
    Node root = NodePrinter.printNodeToAst(sourceCode);
    inlineObjectLiteralsPass.process(null, root);
    String result = NodePrinter.printNodeToCompilableJs(root);
    String expectedCode = "function foo() {" +
        "  var a = null;" +
        "  var x = a;" +
        "  function foo_a_get() {" +
        "    return 1;" +
        "  }" +
        "  function foo_a_set(val) {" +
        "    a = val;" +
        "  }" +
        "  function foo_a() {" +
        "    return 2;" +
        "  }" +
        "}" ;

    assertEquals(expectedCode, result);
  }

  @Test
  public void testInlineObjectLiteralsWithExportedVariables() {
    String sourceCode = "var globalVar = {" +
        "  a: 1," +
        "  b: 2" +
        "};" +
        "export function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    c: globalVar" +
        "  };" +
        "  var x = obj.a;" +
        "  var y = obj.c.a;" +
        "}" ;

    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiteralsPass = new InlineObjectLiterals(compiler, () -> "tmp_");
    Node root = NodePrinter.printNodeToAst(sourceCode);
    inlineObjectLiteralsPass.process(null, root);
    String result = NodePrinter.printNodeToCompilableJs(root);
    String expectedCode = "var globalVar = 1;" +
        "export function foo() {" +
        "  var a = 1;" +
        "  var c = globalVar;" +
        "  var x = a;" +
        "  var y = c;" +
        "}" ;

    assertEquals(expectedCode, result);
  }

  @Test
  public void testInlineObjectLiteralsInsideFunctionCalls() {
    String sourceCode = "function foo(obj) {" +
        "  var x = obj.a;" +
        "}" +
        "function bar() {" +
        "  foo({a: 1});" +
        "}" ;

    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiteralsPass = new InlineObjectLiterals(compiler, () -> "tmp_");
    Node root = NodePrinter.printNodeToAst(sourceCode);
    inlineObjectLiteralsPass.process(null, root);
    String result = NodePrinter.printNodeToCompilableJs(root);
    String expectedCode = "function foo(a) {" +
        "  var x = a;" +
        "}" +
        "function bar() {" +
        "  foo(1);" +
        "}" ;

    assertEquals(expectedCode, result);
  }

  @Test
  public void testInlineObjectLiteralsWithEmptyObjectLiteral() {
    String sourceCode = "function foo() {" +
        "  var obj = {};" +
        "}" ;

    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiteralsPass = new InlineObjectLiterals(compiler, () -> "tmp_");
    Node root = NodePrinter.printNodeToAst(sourceCode);
    inlineObjectLiteralsPass.process(null, root);
    String result = NodePrinter.printNodeToCompilableJs(root);
    String expectedCode = "function foo() {}" ;

    assertEquals(expectedCode, result);
  }

  @Test
  public void testInlineObjectLiteralsWithEmptyObjectLiteralAsReturnType() {
    String sourceCode = "function foo() {" +
        "  return {};" +
        "}" ;

    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiteralsPass = new InlineObjectLiterals(compiler, () -> "tmp_");
    Node root = NodePrinter.printNodeToAst(sourceCode);
    inlineObjectLiteralsPass.process(null, root);
    String result = NodePrinter.printNodeToCompilableJs(root);
    String expectedCode = "function foo() {" +
        "  return {};" +
        "}" ;

    assertEquals(expectedCode, result);
  }

  @Test
  public void testInlineObjectLiteralsWithNestedObjectLiterals() {
    String sourceCode = "function foo() {" +
        "  var obj1 = {" +
        "    a: 1," +
        "    obj2: {" +
        "      b: 2," +
        "      c: 3" +
        "    }" +
        "  };" +
        "  var x = obj1.a;" +
        "  var y = obj1.obj2.b;" +
        "  var z = obj1.obj2.c;" +
        "}" ;

    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiteralsPass = new InlineObjectLiterals(compiler, () -> "tmp_");
    Node root = NodePrinter.printNodeToAst(sourceCode);
    inlineObjectLiteralsPass.process(null, root);
    String result = NodePrinter.printNodeToCompilableJs(root);
    String expectedCode = "function foo() {" +
        "  var a = 1;" +
        "  var obj2 = {" +
        "    b: 2," +
        "    c: 3" +
        "  };" +
        "  var x = a;" +
        "  var y = obj2.b;" +
        "  var z = obj2.c;" +
        "}" ;

    assertEquals(expectedCode, result);
  }
}