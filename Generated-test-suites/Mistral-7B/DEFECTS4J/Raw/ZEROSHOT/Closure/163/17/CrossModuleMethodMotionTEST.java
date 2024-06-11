import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Compiler.factory;
import static com.google.javascript.rhino.NodeTree.*;
import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.jscomp.NodeTraversingCompilerPass;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.compiler.ErrorReporter;

public class CrossModuleMethodMotionTest {

  private AbstractCompiler compiler;
  private JSModuleGraph moduleGraph;
  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = factory(options);
    moduleGraph = compiler.getModuleGraph();
    errorReporter = compiler.getErrorReporter();
  }

  @Test
  public void testMoveMethod() {
    String input = "function Foo() {} Foo.prototype.foo = function() {}; "
        + "function Bar() {} Bar.prototype.bar = function() {}; "
        + "Foo.prototype.bar = Bar.prototype.foo;";

    Node root = compiler.parseTree(input);

    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler,
        new CrossModuleMethodMotion.IdGenerator(), false);

    NodeTraversingCompilerPass.traverse(pass, root);

    // Check that foo is stubbed in Foo and unstubbed in Bar
    Node foo = root.lookupDeclaredIdentifier("Foo");
    Node fooProto = foo.getFirstChild();
    Node fooStub = fooProto.lookupChildByName("foo");
    assertEquals("stubbed method", fooStub.getFirstChild().getClass(),
        IRCall.class);
    assertEquals("stubbed method id",
        ((IRCall) fooStub.getFirstChild()).getArguments().get(0).getFirstChild()
            .getFirstChild().getClass(),
        IRNumber.class);

    Node bar = root.lookupDeclaredIdentifier("Bar");
    Node barProto = bar.getFirstChild();
    Node barFoo = barProto.lookupChildByName("foo");
    assertNotNull("unstubbed method", barFoo);
    assertEquals("function", barFoo.getClass().getSimpleName(), "Node");

    // Check that there are no errors
    assertTrue("no errors", errorReporter.getAllErrors().isEmpty());
  }

  @Test
  public void testMoveMethodWithNullCommonModule() {
    String input = "function Foo() {} Foo.prototype.foo = function() {}; "
        + "function Bar() {} Bar.prototype.foo = function() {}; "
        + "Foo.prototype.bar = Bar.prototype.foo; "
        + "delete Bar.prototype;";

    Node root = compiler.parseTree(input);

    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler,
        new CrossModuleMethodMotion.IdGenerator(), false);

    try {
      NodeTraversingCompilerPass.traverse(pass, root);
      fail("should have reported an error");
    } catch (JSError e) {
      assertEquals("JSC_INTERNAL_ERROR_MODULE_DEPEND", e.getType().getName());
      List<String> expectedMessages = newArrayList();
      expectedMessages.add("null deepest common module");
      assertEquals("expected error messages", expectedMessages,
          ImmutableList.copyOf(errorReporter.getAllErrors()));
    }
  }

  @Test
  public void testMoveMethodWithCycles() {
    String input = "function A() {} A.prototype.foo = function() {}; "
        + "function B() {} B.prototype.bar = function() {}; "
        + "A.prototype.bar = B.prototype.foo; "
        + "function C() {} C.prototype.foo = function() {}; "
        + "C.prototype.bar = A.prototype.foo;";

    Node root = compiler.parseTree(input);

    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler,
        new CrossModuleMethodMotion.IdGenerator(), false);

    try {
      NodeTraversingCompilerPass.traverse(pass, root);
      fail("should have reported an error");
    } catch (JSError e) {
      assertEquals("JSC_INTERNAL_ERROR_CYCLIC_DEPENDENCY", e.getType().getName());
      List<String> expectedMessages = newArrayList();
      expectedMessages.add("cyclic dependency detected: A -> B -> A");
      assertEquals("expected error messages", expectedMessages,
          ImmutableList.copyOf(errorReporter.getAllErrors()));
    }
  }

  @Test
  public void testMoveMethodWithMutuallyDependentModules() {
    String input = "function A() {} A.prototype.foo = function() {}; "
        + "function B() {} B.prototype.bar = function() {}; "
        + "A.prototype.bar = B.prototype.foo; "
        + "function C() {} C.prototype.foo = function() {}; "
        + "C.prototype.bar = A.prototype.foo;";

    Node root = compiler.parseTree(input);

    CrossModuleMethodMotion passA = new CrossModuleMethodMotion(compiler,
        new CrossModuleMethodMotion.IdGenerator(), false);

    Node rootA = NodeTraversingCompilerPass.traverse(passA, root);

    CrossModuleMethodMotion passB = new CrossModuleMethodMotion(compiler,
        new CrossModuleMethodMotion.IdGenerator(), false);

    Node rootB = NodeTraversingCompilerPass.traverse(passB, rootA);

    // Check that foo is stubbed in A and unstubbed in B
    Node fooA = rootB.lookupDeclaredIdentifier("A").getFirstChild();
    Node fooStubA = fooA.lookupChildByName("foo");
    assertEquals("stubbed method", fooStubA.getFirstChild().getClass(),
        IRCall.class);
    assertEquals("stubbed method id",
        ((IRCall) fooStubA.getFirstChild()).getArguments().get(0).getFirstChild()
            .getFirstChild().getClass(),
        IRNumber.class);

    Node fooB = rootB.lookupDeclaredIdentifier("B").getFirstChild();
    Node fooStubB = fooB.lookupChildByName("foo");
    assertNotNull("unstubbed method", fooStubB);
    assertEquals("function", fooStubB.getClass().getSimpleName(), "Node");

    // Check that there are no errors
    assertTrue("no errors", errorReporter.getAllErrors().isEmpty());
  }
}

class CrossModuleMethodMotionTest$IdGenerator {
  private int currentId = 0;

  int newId() {
    return currentId++;
  }
}