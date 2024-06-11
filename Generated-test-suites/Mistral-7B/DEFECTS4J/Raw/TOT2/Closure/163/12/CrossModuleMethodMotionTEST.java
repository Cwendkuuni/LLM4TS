import static org.junit.Assert.*;
import com.google.javascript.jscomp.CrossModuleMethodMotion;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;

public class CrossModuleMethodMotionTest {

  private Compiler compiler;
  private CrossModuleMethodMotion pass;

  @Before
  public void setUp() {
    compiler = new Compiler();
    pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true);
    compiler.addPass(pass);
  }

  @Test
  public void testMoveMethods() {
    // Basic functionality test
    Node root = IR.parse("var a = {b: function() {}};");
    Node result = pass.process(null, root).getRootNode();
    assertEquals("expected function declaration in deeper module",
        IR.isFunctionDeclaration(result.getChild(0)),
        IR.isFunctionDeclaration(result.getChild(1).getChild(0)));

    // Edge case: moving a method that is a prototype property
    Node prototypeRoot = IR.parse("var a = {}; a.prototype.b = function() {};");
    Node prototypeResult = pass.process(null, prototypeRoot).getRootNode();
    assertEquals("expected function declaration in deeper module",
        IR.isFunctionDeclaration(prototypeResult.getChild(0).getChild(0)),
        IR.isFunctionDeclaration(prototypeResult.getChild(1).getChild(0)));

    // Edge case: moving a method with a getter and a setter
    Node getterSetterRoot = IR.parse("var a = {get b() {return 1;}, set b(x) {}};");
    Node getterSetterResult = pass.process(null, getterSetterRoot).getRootNode();
    assertFalse("expected no method movement for getter and setter",
        IR.isFunctionDeclaration(getterSetterResult.getChild(0)));

    // Edge case: moving a method that depends on a closure variable
    Node closedRoot = IR.parse("(function() {var a = {b: function() {}};})();");
    Node closedResult = pass.process(null, closedRoot).getRootNode();
    assertNull("expected no method movement for closure variables",
        IR.isFunctionDeclaration(closedResult.getChild(0).getChild(0)));

    // Exception handling: null deepest common module
    Node nullRoot = IR.parse("{a: function() {}}");
    try {
      pass.process(null, nullRoot).getRootNode();
      fail("expected InternalError for null deepest common module");
    } catch (NullPointerException e) {
      // expected
    }

    // Exception handling: unreferenced property
    Node unreferencedRoot = IR.parse("{a: function b() {}}");
    try {
      pass.process(null, unreferencedRoot).getRootNode();
      fail("expected error for unreferenced property");
    } catch (IllegalArgumentException e) {
      // expected
    }

    // Exception handling: non-function value
    Node nonFunctionRoot = IR.parse("var a = {b: 1};");
    try {
      pass.process(null, nonFunctionRoot).getRootNode();
      fail("expected error for non-function value");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @After
  public void tearDown() {
    // Reset global Compiler for next test
    Compiler.reset();
  }
}