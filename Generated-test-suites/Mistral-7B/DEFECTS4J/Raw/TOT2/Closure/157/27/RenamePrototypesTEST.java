import static org.junit.Assert.*;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.TokenStream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RenamePrototypesTest {

  @org.junit.Test
  public void testRenamePrototypeProperty() {
    String code = "function Foo() {} Foo.prototype.bar = function() {};";
    TokenStream input = new TokenStream(new StringReader(code));
    RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
    renamePrototypes.process(null, input.readTree());

    Node tree = input.readTree();
    Node bar = findNodeByName(tree, "bar");
    assertEquals("renamedBar", bar.getString());
  }

  @org.junit.Test
  public void testDontRenamePrototypePropertyWithLeadingUnderscore() {
    String code = "_prototype.bar = function() {};";
    TokenStream input = new TokenStream(new StringReader(code));
    RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
    renamePrototypes.process(null, input.readTree());

    Node tree = input.readTree();
    Node bar = findNodeByName(tree, "bar");
    assertNull(bar);
  }

  @org.junit.Test
  public void testRenamePrototypePropertyWithAggressiveRenaming() {
    String code = "function Foo() {} Foo.prototype.baz = function() {};";
    TokenStream input = new TokenStream(new StringReader(code));
    RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
    renamePrototypes.process(null, input.readTree());

    Node tree = input.readTree();
    Node bar = findNodeByName(tree, "baz");
    assertEquals("renamedBaz", bar.getString());
  }

  @org.junit.Test
  public void testDontRenamePrototypePropertyWithUppercaseLetter() {
    String code = "function Foo() {} Foo.prototype.Baz = function() {};";
    TokenStream input = new TokenStream(new StringReader(code));
    RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
    renamePrototypes.process(null, input.readTree());

    Node tree = input.readTree();
    Node bar = findNodeByName(tree, "Baz");
    assertEquals("Baz", bar.getString());
  }

  @org.junit.Test
  public void testRenameObjLitProperty() {
    String code = "var obj = { bar: 1 };";
    TokenStream input = new TokenStream(new StringReader(code));
    RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
    renamePrototypes.process(null, input.readTree());

    Node tree = input.readTree();
    Node bar = findNodeByName(tree, "bar");
    assertEquals("renamedBar", bar.getString());
  }

  @org.junit.Test
  public void testDontRenameObjLitPropertyWithQuotedName() {
    String code = "var obj = {'bar': 1 };";
    TokenStream input = new TokenStream(new StringReader(code));
    RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
    renamePrototypes.process(null, input.readTree());

    Node tree = input.readTree();
    Node bar = findNodeByName(tree, "bar");
    assertEquals("bar", bar.getString());
  }

  @org.junit.Test
  public void testRenamePrototypeWithExistingName() {
    String code = "function Foo() {} Foo.prototype.bar = function() {}; var Foo = function() {}; Foo.prototype.bar = function() {};";
    TokenStream input = new TokenStream(new StringReader(code));
    RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
    renamePrototypes.process(null, input.readTree());

    Node tree = input.readTree();
    Node firstBar = findNodeByName(tree, "bar");
    Node secondBar = findNodeByName(tree, "bar");
    assertEquals("renamedBar_1", firstBar.getString());
    assertEquals("renamedBar_2", secondBar.getString());
  }

  @org.junit.Test
  public void testReuseNameFromPreviousCompilation() {
    String code = "function Foo() {} Foo.prototype.bar = function() {}; // in previous compilation: Foo.prototype.baz = function() {};";
    TokenStream input = new TokenStream(new StringReader(code));
    RenamePrototypes renamePrototypes = new RenamePrototypes(null, false, null, new VariableMap(
        new HashMap<String, String>() {{
          put("baz", "renamedBaz");
        }}));
    renamePrototypes.process(null, input.readTree());

    Node tree = input.readTree();
    Node bar = findNodeByName(tree, "bar");
    assertEquals("renamedBaz", bar.getString());
  }

  private Node findNodeByName(Node tree, String name) {
    return findNodeByFunction(tree, node -> node.getString().equals(name));
  }

  private Node findNodeByFunction(Node node, Predicate<Node> predicate) {
    if (predicate.test(node)) {
      return node;
    }

    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      Node result = findNodeByFunction(child, predicate);
      if (result != null) {
        return result;
      }
    }

    return null;
  }

  @FunctionalInterface
  private interface Predicate<T> {
    boolean test(T t);
  }

  private static class StringReader implements java.io.Reader {
    private final String string;

    public StringReader(String string) {
      this.string = string;
    }

    public int read() throws IOException {
      return string.codePointAt(0) < 0 ? -1 : Character.codePointAt(string, 0++);
    }
  }
}