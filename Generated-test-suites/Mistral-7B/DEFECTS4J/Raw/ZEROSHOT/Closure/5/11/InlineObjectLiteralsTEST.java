import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;

public class InlineObjectLiteralsTest {

  @org.junit.Test
  public void testInlineObjectLiterals() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test");
    String source = "function foo() {" +
        "  var obj = {" +
        "    a: 'foo'," +
        "    b: 'bar'" +
        "  };" +
        "  var x = obj.a;" +
        "  var y = obj.b;" +
        "  obj = {c: 42};" +
        "}" +
        "foo();";

    Node ast = compiler.parseTree(source, null);
    pass.process(null, ast);

    Scope rootScope = ast.getFirstChild().getFirstChild().getFirstChild();

    Node foo = rootScope.getVariable("foo");
    Node functionDeclaration = foo.getFirstChild();

    // Check that the object literal has been split into separate variables
    assertNotNull(rootScope.getVariable("JSCompiler_object_inline_a"));
    assertNotNull(rootScope.getVariable("JSCompiler_object_inline_b"));

    // Check that the variables have been assigned the correct values
    Node a = rootScope.getVariable("JSCompiler_object_inline_a").getFirstChild();
    Node b = rootScope.getVariable("JSCompiler_object_inline_b").getFirstChild();
    assertEquals("foo", a.getString());
    assertEquals("bar", b.getString());

    // Check that the references to the object literal properties have been updated
    Node x = functionDeclaration.getFirstChild().getLastChild();
    Node y = functionDeclaration.getFirstChild().getLastChild().getNextSibling();
    Node xAssignment = x.getPreviousSibling();
    Node yAssignment = y.getPreviousSibling();

    assertEquals("JSCompiler_object_inline_a", xAssignment.getString());
    assertEquals("JSCompiler_object_inline_b", yAssignment.getString());

    // Check that the object literal itself has been removed
    Node originalObject = functionDeclaration.getFirstChild().getNextSibling();
    assertNull(originalObject);
  }
}