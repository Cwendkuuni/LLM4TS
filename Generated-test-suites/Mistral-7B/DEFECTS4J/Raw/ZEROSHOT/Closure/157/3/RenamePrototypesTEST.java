import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenamePrototypes.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.TokenStream;

import org.junit.Test;

import java.util.Map;

public class RenamePrototypesTest {

  @Test
  public void testSimpleRename() {
    AbstractCompiler compiler = new AbstractCompiler();
    RenamePrototypes renamer = new RenamePrototypes(compiler, true, null, null);

    CompilerInput input = createTestInput("test1.js");
    Node root = input.getRootNode();

    renamer.process(input.getExterns(), root);

    Map<String, String> actualRenames = renamer.getPropertyMap();
    Map<String, String> expectedRenames = Maps.newHashMap();
    expectedRenames.put("foo", "foo_");
    expectedRenames.put("bar", "bar_");

    assertEquals(expectedRenames, actualRenames);
  }

  @Test
  public void testNoRename() {
    AbstractCompiler compiler = new AbstractCompiler();
    RenamePrototypes renamer = new RenamePrototypes(compiler, false, null, null);

    CompilerInput input = createTestInput("test1.js");
    Node root = input.getRootNode();

    renamer.process(input.getExterns(), root);

    Map<String, String> actualRenames = renamer.getPropertyMap();
    Map<String, String> expectedRenames = Maps.newHashMap();
    expectedRenames.put("foo", "foo");
    expectedRenames.put("bar", "bar");

    assertEquals(expectedRenames, actualRenames);
  }

  @Test
  public void testPrototypeRename() {
    AbstractCompiler compiler = new AbstractCompiler();
    RenamePrototypes renamer = new RenamePrototypes(compiler, true, null, null);

    CompilerInput input = createTestInput("test2.js");
    Node root = input.getRootNode();

    renamer.process(input.getExterns(), root);

    Map<String, String> actualRenames = renamer.getPropertyMap();
    Map<String, String> expectedRenames = Maps.newHashMap();
    expectedRenames.put("MyObject.prototype.getFoo", "MyObject_prototype_getFoo");
    expectedRenames.put("MyObject.prototype.getBar", "MyObject_prototype_getBar");
    expectedRenames.put("MyObject.prototype.foo", "MyObject_prototype_foo_");
    expectedRenames.put("MyObject.prototype.bar", "MyObject_prototype_bar_");

    assertEquals(expectedRenames, actualRenames);
  }

  @Test
  public void testPrototypeAndObjLitRename() {
    AbstractCompiler compiler = new AbstractCompiler();
    RenamePrototypes renamer = new RenamePrototypes(compiler, true, null, null);

    CompilerInput input = createTestInput("test3.js");
    Node root = input.getRootNode();

    renamer.process(input.getExterns(), root);

    Map<String, String> actualRenames = renamer.getPropertyMap();
    Map<String, String> expectedRenames = Maps.newHashMap();
    expectedRenames.put("MyObject.prototype.getFoo", "MyObject_prototype_getFoo");
    expectedRenames.put("MyObject.prototype.getBar", "MyObject_prototype_getBar");
    expectedRenames.put("MyObject.prototype.foo", "MyObject_prototype_foo_");
    expectedRenames.put("MyObject.prototype.bar", "MyObject_prototype_bar_");
    expectedRenames.put("MyObject.prototype.qux", "MyObject_prototype_qux_");
    expectedRenames.put("MyObject.prototype.quux", "MyObject_prototype_quux_");
    expectedRenames.put("obj1.foo", "obj1_foo_");
    expectedRenames.put("obj1.bar", "obj1_bar_");

    assertEquals(expectedRenames, actualRenames);
  }

  @Test
  public void testNoRenameOfExtern() {
    AbstractCompiler compiler = new AbstractCompiler();
    RenamePrototypes renamer = new RenamePrototypes(compiler, true, null, null);

    CompilerInput input = createTestInput("test4.js");
    Node root = input.getRootNode();

    renamer.process(input.getExterns(), root);

    Map<String, String> actualRenames = renamer.getPropertyMap();
    Map<String, String> expectedRenames = Maps.newHashMap();
    expectedRenames.put("Foo.prototype.getFoo", "Foo.prototype.getFoo");
    expectedRenames.put("Foo.prototype.getBar", "Foo.prototype.getBar");
    expectedRenames.put("Foo.prototype.foo", "Foo.prototype.foo");
    expectedRenames.put("Foo.prototype.bar", "Foo.prototype.bar");

    assertEquals(expectedRenames, actualRenames);
  }

  private CompilerInput createTestInput(String fileName) {
    String input =
        "(" +
        "function MyObject() {" +
        "  this.foo = 1;" +
        "  this.bar = function() {};" +
        "}";

    if (fileName.equals("test2.js")) {
      input +=
          "\n" +
          "MyObject.prototype.getFoo = function() { return this.foo; }" +
          "\n" +
          "MyObject.prototype.getBar = function() { return this.bar; }" +
          "\n" +
          "var obj1 = new MyObject();";
    }

    if (fileName.equals("test3.js")) {
      input +=
          "\n" +
          "MyObject.prototype.qux = 42;" +
          "\n" +
          "MyObject.prototype.quux = function() { return 42; }" +
          "\n" +
          "var obj1 = new MyObject();" +
          "\n" +
          "obj1.foo = 1;" +
          "\n" +
          "obj1.bar = function() { return 1; }";
    }

    if (fileName.equals("test4.js")) {
      input +=
          "\n" +
          "MyObject = function() {" +
          "  this.foo = 1;" +
          "  this.bar = function() {};" +
          "}" +
          "\n" +
          "Foo = MyObject;";
    }

    TokenStream ts = new TokenStream(new StringBuilder(input));
    return new CompilerInput(ts);
  }
}