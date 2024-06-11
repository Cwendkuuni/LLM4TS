import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.*;
import static com.google.javascript.rhino.Token.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.VariableMap;

public class RenamePrototypesTest {

  private AbstractCompiler compiler;
  private CompilerOptions options;
  private CompilerPass rp;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = new AbstractCompiler(options);
    compiler.initialize();
    rp = new RenamePrototypes(compiler, false, null, null);
  }

  @Test
  public void testRenamePrototypeProperties() {
    CompilerInput input = createInput("var Foo = function() { this.bar = 1; };");

    Node root = compiler.parseTree(input);

    rp.process(input.getExterns(), root);

    NodeTraversal.traverse(compiler, root, new AssertPropertiesRenamed());
  }

  @Test
  public void testRenamePrototypeProperties_aggressive() {
    CompilerOptions options = new CompilerOptions();
    options.setAggressiveRenaming(true);

    AbstractCompiler compiler = new AbstractCompiler(options);
    compiler.initialize();

    RenamePrototypes rp = new RenamePrototypes(compiler, true, null, null);

    CompilerInput input = createInput("var Foo = function() { this.bar = 1; };");

    Node root = compiler.parseTree(input);

    rp.process(input.getExterns(), root);

    NodeTraversal.traverse(compiler, root, new AssertPropertiesRenamed());
  }

  @Test
  public void testRenamePrototypeProperties_reserved() {
    CompilerOptions options = new CompilerOptions();
    options.addReservedName("indexOf");

    AbstractCompiler compiler = new AbstractCompiler(options);
    compiler.initialize();

    RenamePrototypes rp = new RenamePrototypes(compiler, false, null, null);

    CompilerInput input = createInput("var Foo = function() { this.indexOf = 1; };");

    Node root = compiler.parseTree(input);

    rp.process(input.getExterns(), root);

    NodeTraversal.traverse(compiler, root, new AssertPropertiesNotRenamed("indexOf"));
  }

  @Test
  public void testRenameObjLitProperties() {
    CompilerInput input = createInput("var Foo = { bar: 1 };");

    Node root = compiler.parseTree(input);

    rp.process(input.getExterns(), root);

    NodeTraversal.traverse(compiler, root, new AssertPropertiesRenamed());
  }

  @Test
  public void testPrototypeObjLitAssignment() {
    CompilerInput input = createInput("var Foo = function() {}; Foo.prototype = { bar: 1 };");

    Node root = compiler.parseTree(input);

    rp.process(input.getExterns(), root);

    NodeTraversal.traverse(compiler, root, new AssertPropertiesRenamed());
  }

  @Test
  public void testPrototypeObjLitAssignmentWithExtern() {
    CompilerOptions options = new CompilerOptions();
    options.addExtern("Array", "");

    AbstractCompiler compiler = new AbstractCompiler(options);
    compiler.initialize();

    RenamePrototypes rp = new RenamePrototypes(compiler, false, null, null);

    CompilerInput input = createInput("Array.prototype = { bar: 1 };");

    Node root = compiler.parseTree(input);

    rp.process(input.getExterns(), root);

    NodeTraversal.traverse(compiler, root, new AssertPropertiesNotRenamed("bar"));
  }

  @Test
  public void testPrototypeObjLitAssignmentWithReserved() {
    CompilerOptions options = new CompilerOptions();
    options.addReservedName("indexOf");

    AbstractCompiler compiler = new AbstractCompiler(options);
    compiler.initialize();

    RenamePrototypes rp = new RenamePrototypes(compiler, false, null, null);

    CompilerInput input = createInput("Array.prototype = { indexOf: 1 };");

    Node root = compiler.parseTree(input);

    rp.process(input.getExterns(), root);

    NodeTraversal.traverse(compiler, root, new AssertPropertiesNotRenamed("indexOf"));
  }

  @Test
  public void testPrototypeObjLitAssignmentWithAggressive() {
    CompilerOptions options = new CompilerOptions();
    options.setAggressiveRenaming(true);

    AbstractCompiler compiler = new AbstractCompiler(options);
    compiler.initialize();

    RenamePrototypes rp = new RenamePrototypes(compiler, true, null, null);

    CompilerInput input = createInput("Array.prototype = { indexOf: 1 };");

    Node root = compiler.parseTree(input);

    rp.process(input.getExterns(), root);

    NodeTraversal.traverse(compiler, root, new AssertPropertiesRenamed());
  }

  @Test
  public void testPrototypeObjLitAssignmentWithExternAndReserved() {
    CompilerOptions options = new CompilerOptions();
    options.addExtern("Array", "");
    options.addReservedName("indexOf");

    AbstractCompiler compiler = new AbstractCompiler(options);
    compiler.initialize();

    RenamePrototypes rp = new RenamePrototypes(compiler, false, null, null);

    CompilerInput input = createInput("Array.prototype = { indexOf: 1 };");

    Node root = compiler.parseTree(input);

    rp.process(input.getExterns(), root);

    NodeTraversal.traverse(compiler, root, new AssertPropertiesNotRenamed("indexOf"));
  }

  @Test
  public void testExternedPropertiesNotRenamed() {
    CompilerInput input = createInput("var Foo = function() { this.bar = 1; }; Foo.prototype.bar = 1;");

    Node root = compiler.parseTree(input);

    rp.process(input.getExterns(), root);

    NodeTraversal.traverse(compiler, root, new AssertPropertiesNotRenamed("bar"));
  }

  @Test
  public void testExternedPropertiesNotRenamed_reserved() {
    CompilerOptions options = new CompilerOptions();
    options.addReservedName("indexOf");

    AbstractCompiler compiler = new AbstractCompiler(options);
    compiler.initialize();

    RenamePrototypes rp = new RenamePrototypes(compiler, false, null, null);

    CompilerInput input = createInput("var Foo = function() { this.bar = 1; }; Foo.prototype.indexOf = 1;");

    Node root = compiler.parseTree(input);

    rp.process(input.getExterns(), root);

    NodeTraversal.traverse(compiler, root, new AssertPropertiesNotRenamed("indexOf"));
  }

  @Test
  public void testExternedPropertiesNotRenamed_aggressive() {
    CompilerOptions options = new CompilerOptions();
    options.setAggressiveRenaming(true);

    AbstractCompiler compiler = new AbstractCompiler(options);
    compiler.initialize();

    RenamePrototypes rp = new RenamePrototypes(compiler, true, null, null);

    CompilerInput input = createInput("var Foo = function() { this.bar = 1; }; Foo.prototype.bar = 1;");

    Node root = compiler.parseTree(input);

    rp.process(input.getExterns(), root);

    NodeTraversal.traverse(compiler, root, new AssertPropertiesNotRenamed("bar"));
  }

  @Test
  public void testReservedNameUsedAsPropertyName() {
    CompilerOptions options = new CompilerOptions();
    options.addReservedName("indexOf");

    AbstractCompiler compiler = new AbstractCompiler(options);
    compiler.initialize();

    RenamePrototypes rp = new RenamePrototypes(compiler, false, null, null);

    CompilerInput input = createInput("var Foo = function() { this.indexOf = 1; };");

    Node root = compiler.parseTree(input);

    rp.process(input.getExterns(), root);

    NodeTraversal.traverse(compiler, root, new AssertPropertiesNotRenamed("indexOf"));
  }

  @Test
  public void testReservedNameUsedAsPropertyName_aggressive() {
    CompilerOptions options = new CompilerOptions();
    options.setAggressiveRenaming(true);

    AbstractCompiler compiler = new AbstractCompiler(options);
    compiler.initialize();

    RenamePrototypes rp = new RenamePrototypes(compiler, true, null, null);

    CompilerInput input = createInput("var Foo = function() { this.indexOf = 1; };");

    Node root = compiler.parseTree(input);

    rp.process(input.getExterns(), root);

    NodeTraversal.traverse(compiler, root, new AssertPropertiesRenamed());
  }

  @Test(expected = RuntimeException.class)
  public void testNullInput() {
    AbstractCompiler compiler = new AbstractCompiler(new CompilerOptions());
    compiler.initialize();

    RenamePrototypes rp = new RenamePrototypes(compiler, false, null, null);

    rp.process(null, null);
  }

  @Test(expected = RuntimeException.class)
  public void testNullRoot() {
    AbstractCompiler compiler = new AbstractCompiler(new CompilerOptions());
    compiler.initialize();

    RenamePrototypes rp = new RenamePrototypes(compiler, false, null, null);

    CompilerInput input = new CompilerInput();

    rp.process(input.getExterns(), null);
  }

  private CompilerInput createInput(String code) {
    SourceFile sourceFile = new SourceFile("test.js", code, SourceFile.Kind.UNKNOWN, new JsSourceRoot());
    CompilerInput input = new CompilerInput( null, Arrays.asList(sourceFile), new CompilerOptions());
    return input;
  }

  private static class AssertPropertiesRenamed implements Visitor {

    @Override
    public void visit(NodeTraversal nt, Node n, Node parent) {
      if (n.getType() == STRING) {
        Property p = RenamePrototypesTest.this.rp.properties.get(n.getString());
        if (p != null && p.newName != null) {
          assertEquals(p.newName, n.getString());
        }
      }
    }
  }

  private static class AssertPropertiesNotRenamed implements Visitor {

    private final String name;

    AssertPropertiesNotRenamed(String name) {
      this.name = name;
    }

    @Override
    public void visit(NodeTraversal nt, Node n, Node parent) {
      if (n.getType() == STRING && n.getString().equals(name)) {
        fail("Expected property " + name + " not to be renamed");
      }
    }
  }

  private interface Visitor extends NodeVisitor2<NodeTraversal, Node> {}
}