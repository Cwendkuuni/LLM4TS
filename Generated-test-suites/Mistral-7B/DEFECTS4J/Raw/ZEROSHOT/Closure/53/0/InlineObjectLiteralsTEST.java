import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerPasses.InlineObjectLiterals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Suppliers;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSExtendedCompiler;
import com.google.javascript.rhino.Node;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    String input = "function f() {"
        + "  var obj = {"
        + "    a: 1,"
        + "    b: function() {},"
        + "    c: {'d': 2}"
        + "  };"
        + "  var x = obj.a;"
        + "  x = {'a': 1};"
        + "  obj.c.d = x;"
        + "  var y = obj.b();"
        + "  y.test = 1;"
        + "  var z = obj.c.d;"
        + "  return x;"
        + "}";

    CompilerOptions options = new CompilerOptions(false, null);
    JSExtendedCompiler compiler = new JSExtendedCompiler(options);
    Compiler pass = new InlineObjectLiterals(compiler, Suppliers.ofInstance(new StringGenerator()));

    Node ast = compiler.parseTree(input);
    pass.process(ast, null);

    Node expected = Node.newBlock(
        Node.newVar("a", Node.newUndefinedNode(null)),
        Node.newVar("b", Node.newFunctionNode(Node.newName("$0"), null, null,
            Node.newBlock(
                Node.newVar("test", Node.newNumberLiteral(1)),
                Node.newReturn(Node.newVar("a"))))),
        Node.newVar("c", Node.newObjectLiteral(
            Node.newProperty("d", Node.newNumberLiteral(2))
        )),
        Node.newVar("x", Node.newNumberLiteral(1)),
        Node.newAssign(Node.newVar("c.d"), Node.newVar("x")),
        Node.newAssign(Node.newVar("y"), Node.newFunctionNode(null, null, null,
            Node.newBlock(
                Node.newVar("test", Node.newNumberLiteral(1)),
                Node.newReturn(Node.newVar("a"))))),
        Node.newVar("z", Node.newGetprop(Node.newVar("c"), Node.newString("d"))),
        Node.newReturn(Node.newVar("x"))
    );

    assertEquals(expected.toSource(), ast.toSource());
  }
}

class StringGenerator implements Supplier<String> {
  private int count = 0;

  @Override
  public String get() {
    return "string_" + (++count);
  }
}