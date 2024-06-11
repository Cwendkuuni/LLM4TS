import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Compiler.warnings;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilationTestBase;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.rhino.Node;

public class CrossModuleMethodMotionTest extends CompilationTestBase {

  public void testMoveMethodAcrossModules() {
    String sourceCode =
        "var Foo = function() {}\n" +
        "Foo.prototype.bar = function() { return 42; };\n" +
        "var A = function() {}\n" +
        "A.prototype.foo = function() { return this.bar(); };\n" +
        "var B = function() {}\n" +
        "B.prototype.baz = function() { return A.prototype.foo(); };";

    Compiler compiler = newCompiler();
    compiler.addSource(UnitTestSourceFile.Name.SOURCE_FILE, sourceCode);
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(
        compiler, new IdGenerator(), true);
    pass.process(null, null);
    Node root = compiler.getRootNode();

    // Verify that bar is now defined in module B
    Node baz = root.lookupVar("B.prototype.baz");
    assertNotNull(baz);
    Node bar = baz.getChild(0);
    assertEquals(Node.CALL, bar.getType());
    Node barExpression = bar.getChild(0);
    assertEquals("Foo.prototype.bar", barExpression.getString());

    // Verify that A.prototype.foo is replaced with a stub function
    Node foo = root.lookupVar("A.prototype.foo");
    assertNotNull(foo);
    Node fooCall = foo.getChild(0);
    assertEquals(Node.CALL, fooCall.getType());
    assertEquals("JSCompiler_stubMethod", fooCall.getChild(0).getString());
    assertEquals(1, fooCall.getNumChildren());

    // Verify that there are no warnings
    assertEquals(0, warnings.size());
  }

  public void testMoveMethodToSameModule() {
    String sourceCode =
        "var Foo = function() {}\n" +
        "Foo.prototype.bar = function() { return 42; };\n" +
        "var A = function() {}\n" +
        "A.prototype.foo = function() { return this.bar(); };\n" +
        "A.prototype.bar = function() { return 0; };";

    Compiler compiler = newCompiler();
    compiler.addSource(UnitTestSourceFile.Name.SOURCE_FILE, sourceCode);
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(
        compiler, new IdGenerator(), true);
    pass.process(null, null);
    Node root = compiler.getRootNode();

    // Verify that bar is still defined in module A and hasn't been replaced
    Node bar = root.lookupVar("A.prototype.bar");
    assertNotNull(bar);
    Node barExpression = bar.getChild(0);
    assertEquals("function bar() { return 0; }", barExpression.toString());

    // Verify that A.prototype.foo is not replaced with a stub function
    Node foo = root.lookupVar("A.prototype.foo");
    assertNotNull(foo);
    Node fooCall = foo.getChild(0);
    assertEquals(Node.CALL, fooCall.getType());
    Node fooExpression = fooCall.getChild(0);
    assertEquals("function foo() { return this.bar(); }",
        fooExpression.toString());

    // Verify that there are no warnings
    assertEquals(0, warnings.size());
  }

  public void testMoveMethodWhichReadsClosureVariables() {
    String sourceCode =
        "var Foo = function(x) {}\n" +
        "Foo.prototype.bar = function() { return x; };\n" +
        "var A = function() {}\n" +
        "A.prototype.foo = function() { return this.bar(); };\n";

    Compiler compiler = newCompiler();
    compiler.addSource(UnitTestSourceFile.Name.SOURCE_FILE, sourceCode);
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(
        compiler, new IdGenerator(), true);
    pass.process(null, null);
    Node root = compiler.getRootNode();

    // Verify that bar is not moved
    Node bar = root.lookupVar("Foo.prototype.bar");
    assertNotNull(bar);

    // Verify that A.prototype.foo is not moved
    Node foo = root.lookupVar("A.prototype.foo");
    assertNotNull(foo);
    Node fooCall = foo.getChild(0);
    assertEquals(Node.CALL, fooCall.getType());
    Node fooExpression = fooCall.getChild(0);
    assertEquals("Foo.prototype.bar", fooExpression.getString());

    // Verify that there is a warning
    assertEquals(1, warnings.size());
    assertTrue(warnings.get(0).getMessage().contains("readsClosureVariables"));
  }

  public void testMoveMethodWhichIsAGetter() {
    String sourceCode =
        "var Foo = function() {}\n" +
        "Object.defineProperty(Foo.prototype, 'bar', {\n" +
        "    get: function() { return 42; },\n" +
        "});\n" +
        "var A = function() {}\n" +
        "A.prototype.foo = function() { return this.bar; };\n";

    Compiler compiler = newCompiler();
    compiler.addSource(UnitTestSourceFile.Name.SOURCE_FILE, sourceCode);
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(
        compiler, new IdGenerator(), true);
    pass.process(null, null);
    Node root = compiler.getRootNode();

    // Verify that bar is not moved
    Node getter = root.lookupVar("Foo.prototype.bar$");
    assertNotNull(getter);

    // Verify that A.prototype.foo is not moved
    Node foo = root.lookupVar("A.prototype.foo");
    assertNotNull(foo);
    Node fooCall = foo.getChild(0);
    assertEquals(Node.GETPROP, fooCall.getType());
    assertEquals("Foo.prototype.bar$", fooCall.getString());

    // Verify that there are no warnings
    assertEquals(0, warnings.size());
  }

  public void testMoveMethodWhichIsASetter() {
    String sourceCode =
        "var Foo = function() {}\n" +
        "Object.defineProperty(Foo.prototype, 'bar', {\n" +
        "    set: function(val) {}\n" +
        "});\n" +
        "var A = function() {}\n" +
        "A.prototype.foo = function() { this.bar = 42; };\n";

    Compiler compiler = newCompiler();
    compiler.addSource(UnitTestSourceFile.Name.SOURCE_FILE, sourceCode);
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(
        compiler, new IdGenerator(), true);
    pass.process(null, null);
    Node root = compiler.getRootNode();

    // Verify that bar is not moved
    Node setter = root.lookupVar("Foo.prototype.bar$");
    assertNotNull(setter);

    // Verify that A.prototype.foo is not moved
    Node foo = root.lookupVar("A.prototype.foo");
    assertNotNull(foo);
    Node setterAssignment = foo.getChild(0);
    assertEquals(Node.ASSIGN, setterAssignment.getType());
    assertEquals("Foo.prototype.bar$", setterAssignment.getChild(1).getString());

    // Verify that there are no warnings
    assertEquals(0, warnings.size());
  }

  public void testMoveMethodWithNullDeepestCommonModuleReference() {
    String sourceCode =
        "var Foo = function() {}\n" +
        "Foo.prototype.bar = function() { return 42; };\n" +
        "var A = function() {}\n" +
        "A.prototype.foo = function() { return this.bar(); };\n" +
        "var B = null;";

    Compiler compiler = newCompiler();
    compiler.addSource(UnitTestSourceFile.Name.SOURCE_FILE, sourceCode);
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(
        compiler, new IdGenerator(), true);
    pass.process(null, null);
    Node root = compiler.getRootNode();

    // Verify that there is a warning
    assertEquals(1, warnings.size());
    assertTrue(warnings.get(0).getMessage().contains("null deepestCommonModuleRef"));

    // Verify that bar is not moved
    Node bar = root.lookupVar("Foo.prototype.bar");
    assertNotNull(bar);

    // Verify that A.prototype.foo is not moved
    Node foo = root.lookupVar("A.prototype.foo");
    assertNotNull(foo);
    Node fooCall = foo.getChild(0);
    assertEquals(Node.CALL, fooCall.getType());
    Node fooExpression = fooCall.getChild(0);
    assertEquals("Foo.prototype.bar", fooExpression.getString());
  }

  @Override
  protected Compiler newCompiler() {
    return new Compiler() {
      @Override
      public JSModuleGraph getModuleGraph() {
        return new JSModuleGraph();
      }
    };
  }

  @Override
  protected AbstractCompiler getPass() {
    return new CrossModuleMethodMotion(null, null, true);
  }
}