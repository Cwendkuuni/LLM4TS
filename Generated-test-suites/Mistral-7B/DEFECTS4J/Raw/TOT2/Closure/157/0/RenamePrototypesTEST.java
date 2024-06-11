import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.TokenStream;

import static org.junit.Assert.assertEquals;

public class RenamePrototypesTest {

  @org.junit.Test
  public void testRenamePrototypeProperty() {
    String input = "function Foo() {} Foo.prototype.getBar = function() { return 42; }";
    Compiler compiler = new Compiler();
    CompilerPass renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    TokenStream ts = new TokenStream(new TokenStream.InputSource("test.js", input));
    Node externs = Node.rootTraversal(ts);
    Node root = Node.rootTraversal(ts);

    renamePrototypes.process(externs, root);

    Node getBar = root.mayContainNode("/Foo/prototype/getBar");
    assertEquals("getBar_", getBar.getFirstChild().getString());
  }

  @org.junit.Test
  public void testRenameObjLitProperty() {
    String input = "var obj = { getBar : function() { return 42; } }";
    Compiler compiler = new Compiler();
    CompilerPass renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    TokenStream ts = new TokenStream(new TokenStream.InputSource("test.js", input));
    Node externs = Node.rootTraversal(ts);
    Node root = Node.rootTraversal(ts);

    renamePrototypes.process(externs, root);

    Node getBar = root.getMayContainNode("obj").getFirstChild("getBar");
    assertEquals("getBar_", getBar.getFirstChild().getString());
  }

  @org.junit.Test
  public void testRenameConflictingPrototypeAndObjLitProperty() {
    String input = "function Foo() {} Foo.prototype.getBar = function() { return 42; } var obj = { getBar : 42 }";
    Compiler compiler = new Compiler();
    CompilerPass renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    TokenStream ts = new TokenStream(new TokenStream.InputSource("test.js", input));
    Node externs = Node.rootTraversal(ts);
    Node root = Node.rootTraversal(ts);

    renamePrototypes.process(externs, root);

    Node getBarPrototype = root.mayContainNode("/Foo/prototype/getBar");
    Node getBarObjLit = root.getMayContainNode("obj").getFirstChild("getBar");

    assertEquals("getBar_prototype_", getBarPrototype.getFirstChild().getString());
    assertEquals("getBar_obj_lit_", getBarObjLit.getFirstChild().getString());
  }

  @org.junit.Test
  public void testDontRenameExternedProperty() {
    String input = "function Foo() {} Foo.prototype.toString = String.prototype.toString;";
    Compiler compiler = new Compiler();
    CompilerPass renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    TokenStream ts = new TokenStream(new TokenStream.InputSource("test.js", input));
    Node externs = Node.rootTraversal(ts);
    Node root = Node.rootTraversal(ts);

    renamePrototypes.process(externs, root);

    Node toStringPrototype = root.mayContainNode("/Foo/prototype/toString");
    assertEquals("toString", toStringPrototype.getFirstChild().getString());
  }

  @org.junit.Test
  public void testDontRenamePrivateProperty() {
    String input = "function Foo() { this._bar = 42; }";
    Compiler compiler = new Compiler();
    CompilerPass renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    TokenStream ts = new TokenStream(new TokenStream.InputSource("test.js", input));
    Node externs = Node.rootTraversal(ts);
    Node root = Node.rootTraversal(ts);

    renamePrototypes.process(externs, root);

    Node _bar = root.mayContainNode("/Foo/_bar");
    assertEquals("_bar", _bar.getFirstChild().getString());
  }

  @org.junit.Test
  public void testDontRenameAggressivelyNamedProperty() {
    String input = "function Foo() { this.someUpperCase_Property = 42; }";
    Compiler compiler = new Compiler();
    CompilerPass renamePrototypes = new RenamePrototypes(compiler, false, null, null);

    TokenStream ts = new TokenStream(new TokenStream.InputSource("test.js", input));
    Node externs = Node.rootTraversal(ts);
    Node root = Node.rootTraversal(ts);

    renamePrototypes.process(externs, root);

    Node someUpperCase_Property = root.mayContainNode("/Foo/someUpperCase_Property");
    assertEquals("someUpperCase_Property", someUpperCase_Property.getFirstChild().getString());
  }

  @org.junit.Test
  public void testRenamePropertyWithReservedCharacter() {
    String input = "function Foo() { this.my$property = 42; }";
    Compiler compiler = new Compiler();
    CompilerPass renamePrototypes = new RenamePrototypes(compiler, true, new String[]{"$"}, null);

    TokenStream ts = new TokenStream(new TokenStream.InputSource("test.js", input));
    Node externs = Node.rootTraversal(ts);
    Node root = Node.rootTraversal(ts);

    renamePrototypes.process(externs, root);

    Node my__property = root.mayContainNode("/Foo/my__property");
    assertEquals("my__property", my__property.getFirstChild().getString());
  }
}