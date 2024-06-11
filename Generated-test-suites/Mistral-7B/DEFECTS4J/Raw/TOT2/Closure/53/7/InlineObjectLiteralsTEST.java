import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    String sourceCode = "function test() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: function() {" +
        "      return 2;" +
        "    }," +
        "    c: {'d': 3}" +
        "  };" +
        "  var x = obj.a;" +
        "  var y = obj.b();" +
        "  var z = obj.c.d;" +
        "}" +
        "test();";

    Compiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "");
    pass.process(null, NodePrinter.parse(sourceCode));
    assertEquals("function test() {" +
        "  var a = 1;" +
        "  var b = function() {" +
        "    return 2;" +
        "  };" +
        "  var d = 3;" +
        "  var x = a;" +
        "  var y = b();" +
        "  var z = d;" +
        "}", compiler.getSource());
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReference() {
    String sourceCode = "function test() {" +
        "  var obj = {" +
        "    a: {" +
        "      b: obj.a" +
        "    }" +
        "  };" +
        "}" +
        "test();";

    Compiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "");
    pass.process(null, NodePrinter.parse(sourceCode));
    assertEquals("function test() {" +
        "  var a = {" +
        "    b: a" +
        "  };" +
        "}", compiler.getSource());
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectLiteralsWithCircularReference() {
    String sourceCode = "function test() {" +
        "  var obj1 = {" +
        "    a: obj2" +
        "  };" +
        "  var obj2 = {" +
        "    a: obj1" +
        "  };" +
        "}" +
        "test();";

    Compiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "");
    pass.process(null, NodePrinter.parse(sourceCode));
    fail("Expected AssertionError but test passed.");
  }

  @Test
  public void testInlineObjectLiteralsWithExportedVariable() {
    String sourceCode = "var EXPORTED_VAR = {" +
        "  a: 1" +
        "};" +
        "function test() {" +
        "  var obj = EXPORTED_VAR;" +
        "}" +
        "test();";

    Compiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "");
    pass.process(null, NodePrinter.parse(sourceCode));
    assertEquals("var EXPORTED_VAR = {" +
        "  a: 1" +
        "};" +
        "function test() {" +
        "  var a = 1;" +
        "}", compiler.getSource());
  }

  @Test
  public void testInlineObjectLiteralsWithGlobalVariable() {
    String sourceCode = "function test() {" +
        "  var obj = {" +
        "    a: 1" +
        "  };" +
        "  window.GLOBAL_VAR = obj;" +
        "}" +
        "test();";

    Compiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "");
    pass.process(null, NodePrinter.parse(sourceCode));
    fail("InlineObjectLiterals should not inline global variables.");
  }

  @Test
  public void testInlineObjectLiteralsWithUnusedVariable() {
    String sourceCode = "function test() {" +
        "  var obj = {" +
        "    a: 1" +
        "  };" +
        "  var unused = obj.a;" +
        "  // unused variable is not referenced again" +
        "}" +
        "test();";

    Compiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "");
    pass.process(null, NodePrinter.parse(sourceCode));
    assertEquals("function test() {" +
        "  var a = 1;" +
        "  var unused;" +
        "}", compiler.getSource());
  }

  @Test
  public void testInlineObjectLiteralsWithFunctionAsValue() {
    String sourceCode = "function test() {" +
        "  var obj = {" +
        "    a: function() {" +
        "      return 1;" +
        "    }" +
        "  };" +
        "  var sum = obj.a();" +
        "}" +
        "test();";

    Compiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "");
    pass.process(null, NodePrinter.parse(sourceCode));
    assertEquals("function test() {" +
        "  var a = function() {" +
        "    return 1;" +
        "  };" +
        "  var sum = a();" +
        "}", compiler.getSource());
  }

  @Test
  public void testInlineObjectLiteralsWithFunctionAsKey() {
    String sourceCode = "function test() {" +
        "  var obj = {" +
        "    func: function() {" +
        "      return 1;" +
        "    }," +
        "    a: obj.func()" +
        "  };" +
        "}" +
        "test();";

    Compiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "");
    pass.process(null, NodePrinter.parse(sourceCode));
    assertEquals("function test() {" +
        "  var func = function() {" +
        "    return 1;" +
        "  };" +
        "  var a = func();" +
        "}", compiler.getSource());
  }

  @Test
  public void testInlineObjectLiteralsWithComplexObject() {
    String sourceCode = "function test() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: {" +
        "      c: 2," +
        "      d: {" +
        "        e: 3," +
        "        f: function() {" +
        "          return 4;" +
        "        }" +
        "      }" +
        "    }" +
        "  };" +
        "}" +
        "test();";

    Compiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "");
    pass.process(null, NodePrinter.parse(sourceCode));
    assertEquals("function test() {" +
        "  var a = 1;" +
        "  var b = {" +
        "    c: 2," +
        "    d: {" +
        "      e: 3," +
        "      f: function() {" +
        "        return 4;" +
        "      }" +
        "    }" +
        "  };" +
        "}", compiler.getSource());
  }
}