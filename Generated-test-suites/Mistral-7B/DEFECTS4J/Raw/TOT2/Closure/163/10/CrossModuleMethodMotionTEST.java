import static org.junit.Assert.*;
import com.google.javascript.jscomp.CrossModuleMethodMotion;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.rhino.Node;

public class CrossModuleMethodMotionTest {
  @org.junit.Before
  public void setup() {
    Compiler compiler = new Compiler();
    CompilerPass crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true);
    compiler.addPass(crossModuleMethodMotion);
  }

  @org.junit.Test
  public void testMoveMethods() {
    // Given a Java class with prototype methods
    String rootCode = "class Foo {\n" +
        "  static function bar() {\n" +
        "    return 42;\n" +
        "  }\n" +
        "  function baz() {\n" +
        "    return 21;\n" +
        "  }\n" +
        "}";
    Node root = compiler.parseTree(rootCode);

    // When the CrossModuleMethodMotion pass is applied
    compiler.process(null, root);

    // Then the prototype methods should be moved to a deeper module
    Node fooProto = root.getFirstChild().getFirstChild();
    assertNull(fooProto.getChildByName("bar"));
    assertNotNull(fooProto.getChildByName("JSCompiler_stubMethod_0"));
    Node bazStub = fooProto.getChildByName("JSCompiler_unstubMethod_0");
    Node bazFunction = bazStub.getChild();
    assertEquals("function (){" +
        "  return JSCompiler_stubMap[0].apply(this, arguments);" +
        "}", bazFunction.getRawText());

    // And the function bodies should be unstubbed in the deeper module
    Node deeperModule = compiler.getModuleGraph().getModuleByName("deeper");
    Node bazUnstub = deeperModule.getBody().getFirstChild();
    assertEquals("function baz() {" +
        "  return 21;" +
        "}", bazUnstub.getRawText());
  }
}