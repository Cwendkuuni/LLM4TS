import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.InlineObjectLiterals;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineSimpleObjectLiterals() {
    String sourceCode = "function f() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2," +
        "    c: function() { return 3; }" +
        "  };" +
        "  return obj.a + obj.b + obj.c();" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp");
    compiler.addPass(pass);
    Node root = compiler.parseTree(sourceCode, null, null);
    Node externs = NodeUtil.newEmptyNodeList();
    pass.process(externs, root);
    assertEquals("function f() { var a=1,b=2,c=function(){return 3;}; return a+b+c();}",
        NodePrinter.printTree(root));
  }

  @Test
  public void testInlineComplexObjectLiterals() {
    String sourceCode = "function f() {" +
        "  var obj = {" +
        "    a: {" +
        "      b: 1," +
        "      c: {" +
        "        d: 2," +
        "        e: function() { return 3; }" +
        "      }" +
        "    }," +
        "    b: 2," +
        "    c: function() { return 4; }" +
        "  };" +
        "  return obj.a.b + obj.a.c.d + obj.c();" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp");
    compiler.addPass(pass);
    Node root = compiler.parseTree(sourceCode, null, null);
    Node externs = NodeUtil.newEmptyNodeList();
    pass.process(externs, root);
    assertEquals("function f() {" +
        "  var a_b=1,a_c_d=2,c=function(){return 4;};" +
        "  return a_b+a_c_d+c()",
        NodePrinter.printTree(root));
  }

  @Test
  public void testInlineObjectLiteralsInArray() {
    String sourceCode = "function f() {" +
        "  var arr = [{" +
        "    a: 1," +
        "    b: 2," +
        "    c: function() { return 3; }" +
        "  }, {" +
        "    a: 4," +
        "    b: 5," +
        "    c: function() { return 6; }" +
        "  }];" +
        "  return arr[0].a + arr[0].b + arr[0].c() + arr[1].a + arr[1].b + arr[1].c();" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp");
    compiler.addPass(pass);
    Node root = compiler.parseTree(sourceCode, null, null);
    Node externs = NodeUtil.newEmptyNodeList();
    pass.process(externs, root);
    assertEquals("function f() {" +
        "  var a_0_a=1,a_0_b=2,a_0_c=function(){return 3;},a_1_a=4,a_1_b=5,a_1_c=function(){return 6;};" +
        "  return a_0_a+a_0_b+a_0_c()+a_1_a+a_1_b+a_1_c()",
        NodePrinter.printTree(root));
  }

  @Test
  public void testInlineEmptyObjectLiterals() {
    String sourceCode = "function f() {" +
        "  var obj = {};" +
        "  return obj.a;" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp");
    compiler.addPass(pass);
    Node root = compiler.parseTree(sourceCode, null, null);
    Node externs = NodeUtil.newEmptyNodeList();
    pass.process(externs, root);
    assertEquals("function f() { return undefined}",
        NodePrinter.printTree(root));
  }

  @Test
  public void testInlineObjectLiteralsWithFunctionProperties() {
    String sourceCode = "function f() {" +
        "  var obj = {" +
        "    a: function() { return 1; }," +
        "    b: function() { return 2; }" +
        "  };" +
        "  return obj.a() + obj.b();" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp");
    compiler.addPass(pass);
    Node root = compiler.parseTree(sourceCode, null, null);
    Node externs = NodeUtil.newEmptyNodeList();
    pass.process(externs, root);
    assertEquals("function f() {" +
        "  var a=function(){return 1;},b=function(){return 2;};" +
        "  return a()+b()",
        NodePrinter.printTree(root));
  }

  @Test
  public void testInlineObjectLiteralsWithArrayProperties() {
    String sourceCode = "function f() {" +
        "  var obj = {" +
        "    a: [1, 2, 3]," +
        "    b: [4, 5, 6]" +
        "  };" +
        "  return obj.a[0] + obj.b[0];" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp");
    compiler.addPass(pass);
    Node root = compiler.parseTree(sourceCode, null, null);
    Node externs = NodeUtil.newEmptyNodeList();
    pass.process(externs, root);
    assertEquals("function f() {" +
        "  var a=new Array(1,2,3),b=new Array(4,5,6);" +
        "  return a[0]+b[0]",
        NodePrinter.printTree(root));
  }

  @Test
  public void testInlineObjectLiteralsWithCircularReference() {
    String sourceCode = "function f() {" +
        "  var obj = {" +
        "    a: { b: obj }" +
        "  };" +
        "  return obj.a.b;" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp");
    compiler.addPass(pass);
    Node root = compiler.parseTree(sourceCode, null, null);
    Node externs = NodeUtil.newEmptyNodeList();
    pass.process(externs, root);
    try {
      NodePrinter.printTree(root);
      fail("Expected an error due to circular reference.");
    } catch (RuntimeException e) {
      // Expected
    }
  }

  @Test
  public void testInlineObjectLiteralsWithCyclicProperty() {
    String sourceCode = "function f() {" +
        "  var obj = {" +
        "    a: {" +
        "      b: obj" +
        "    }" +
        "  };" +
        "  return obj.a.b;" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp");
    compiler.addPass(pass);
    Node root = compiler.parseTree(sourceCode, null, null);
    Node externs = NodeUtil.newEmptyNodeList();
    pass.process(externs, root);
    try {
      NodePrinter.printTree(root);
      fail("Expected an error due to circular reference.");
    } catch (RuntimeException e) {
      // Expected
    }
  }

  @Test
  public void testInlineObjectLiteralsWithGlobalObject() {
    String sourceCode = "function f() {" +
        "  var obj = {" +
        "    a: window" +
        "  };" +
        "  return obj.a.alert('Hello, World!');" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp");
    compiler.addPass(pass);
    Node root = compiler.parseTree(sourceCode, null, null);
    Node externs = NodeUtil.newEmptyNodeList();
    try {
      pass.process(externs, root);
      fail("Expected error due to inlining of global object.");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }
}