import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Compiler.parseTree;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerTestCase;
import com.google.javascript.jscomp.ParserException;
import com.google.javascript.rhino.Node;

public class CrossModuleMethodMotionTest extends CompilerTestCase {

  private CompilerPass pass;
  private static final String CLASS_NAME = "CrossModuleMethodMotion";

  @Override
  protected CompilerPass getProcessor(AbstractCompiler compiler) {
    CompilerOptions options = compiler.getOptions();
    return new CrossModuleMethodMotion(compiler, new IdGenerator(), options.isCanonicalize());
  }

  @org.junit.Test
  public void testMoveMethodAcrossModules() throws ParserException {
    String sourceCode = "class A {" +
        "  function foo() {" +
        "    return 42;" +
        "  }" +
        "}";
    Node root = parseTree(sourceCode);
    pass.process(null, root);

    Node aPrototype = root.lookupVar("A.prototype");
    assertNotNull(aPrototype);

    Node foo = aPrototype.lookupProperty("foo");
    assertNotNull(foo);
    assertEquals("JSCompiler_stubMethod(42)", foo.toString());

    Node aInstance = root.lookupVariable("new A()");
    assertNotNull(aInstance);
    assertEquals(42, aInstance.callMethod("foo").getDoubleValue());
  }

  @org.junit.Test
  public void testMoveMethodToSameModule() throws ParserException {
    String sourceCode = "class A {" +
        "  function foo() {" +
        "    return 42;" +
        "  }" +
        "}" +
        "class B {" +
        "  function bar() {" +
        "    return A.prototype.foo();" +
        "  }" +
        "}";
    Node root = parseTree(sourceCode);
    pass.process(null, root);

    Node bPrototype = root.lookupVar("B.prototype");
    assertNotNull(bPrototype);

    Node bar = bPrototype.lookupProperty("bar");
    assertNotNull(bar);
    assertEquals("function foo() {return 42;}", bar.toString());

    Node bInstance = root.lookupVariable("new B()");
    assertNotNull(bInstance);
    assertEquals(42, bInstance.callMethod("bar").getDoubleValue());
  }

  @org.junit.Test
  public void testNullDeepestCommonModuleError() throws ParserException {
    String sourceCode = "class A {}";
    Node root = parseTree(sourceCode);

    try {
      pass.process(null, root);
      fail("Expected JSC_INTERNAL_ERROR_MODULE_DEPEND");
    } catch (AssertionError e) {
      // expected
    }
  }
}