import static com.google.javascript.jscomp.RenamePrototypes.*;
import static com.google.javascript.rhino.TokenType.*;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.TokenStream;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the RenamePrototypes CompilerPass.
 */
public class RenamePrototypesTest {

  private Compiler compiler;
  private NodeFactory nodeFactory;
  private CompilerInput input;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions(null);
    compiler = new Compiler(options);
    nodeFactory = compiler.getFactory();
    input = new CompilerInput("test.js", "test.js");
  }

  /**
   * Tests that properties without leading/trailing underscores are not renamed when
   * aggressive renaming is off.
   */
  @Test
  public void testSimplePropertiesNotRenamed() {
    String code =
      "function Foo() {}" +
      "Foo.prototype.foo = function() {};";

    Node root = parseTestCode(code);

    RenamePrototypes pass = new RenamePrototypes(compiler, false, null, null);
    pass.process(null, root);

    Node foo = root.findChildByName("Foo");
    Node prototype = foo.findChildByName("prototype");
    Node fooProto = prototype.findChildByName("foo");
    assertEquals("foo", fooProto.getString());
  }

  /**
   * Tests that properties with leading/trailing underscores are renamed.
   */
  @Test
  public void testLeadingTrailingUnderscoresRenamed() {
    String code =
      "function Foo() {}" +
      "Foo.prototype._foo_ = function() {};";

    Node root = parseTestCode(code);

    RenamePrototypes pass = new RenamePrototypes(compiler, false, null, null);
    pass.process(null, root);

    Node foo = root.findChildByName("Foo");
    Node prototype = foo.findChildByName("prototype");
    Node fooProto = prototype.findChildByName("_foo_");
    assertEquals("_RENAMED_", fooProto.getString());
  }

  /**
   * Tests that properties with only one character that is not a lowercase letter
   * are renamed when aggressive renaming is on.
   */
  @Test
  public void testPropertiesRenamedWhenAggressive() {
    String code =
      "function Foo() {}" +
      "Foo.prototype.UpperCase = function() {};";

    Node root = parseTestCode(code);

    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    pass.process(null, root);

    Node foo = root.findChildByName("Foo");
    Node prototype = foo.findChildByName("prototype");
    Node upperCase = prototype.findChildByName("UpperCase");
    assertEquals("_UPPERCASE_", upperCase.getString());
  }

  /**
   * Tests that properties are renamed only if they appear both in prototype and
   * object literal definitions.
   */
  @Test
  public void testPropertiesRenamedOnlyIfInBothPrototypeAndObjectLiteral() {
    String code =
      "function Foo() {}" +
      "Foo.prototype.bar = function() {};" +
      "Foo.prototype = {" +
      "  bar: function() {}" +
      "};";

    Node root = parseTestCode(code);

    RenamePrototypes pass = new RenamePrototypes(compiler, false, null, null);
    pass.process(null, root);

    Node foo = root.findChildByName("Foo");
    Node prototype = foo.findChildByName("prototype");
    Node protoBar = prototype.findChildByName("bar");
    Node objLitBar = prototype.findChildByName("bar");
    assertEquals("bar", protoBar.getString());
    assertEquals("bar", objLitBar.getString());

    // Now change the order of the prototype and object literal definitions
    code =
      "function Foo() {}" +
      "Foo.prototype = {" +
      "  bar: function() {}" +
      "};" +
      "Foo.prototype.bar = function() {};";

    root = parseTestCode(code);
    pass = new RenamePrototypes(compiler, false, null, null);
    pass.process(null, root);

    assertEquals("_RENAMED_", protoBar.getString());
    assertEquals("_RENAMED_", objLitBar.getString());
  }

  /**
   * Tests that externed properties are not renamed.
   */
  @Test
  public void testExternedPropertiesNotRenamed() {
    String code =
      "function Foo() {}" +
      "Foo.prototype.indexOf = Array.prototype.indexOf;";

    Node root = parseTestCode(code);

    RenamePrototypes pass = new RenamePrototypes(compiler, false, null, null);
    pass.process(null, root);

    Node foo = root.findChildByName("Foo");
    Node prototype = foo.findChildByName("prototype");
    Node indexOf = prototype.findChildByName("indexOf");
    assertEquals("indexOf", indexOf.getString());
  }

  /**
   * Tests that all properties are renamed even if some have the same name.
   */
  @Test
  public void testAllPropertiesRenamed() {
    String code =
      "function Foo() {}" +
      "Foo.prototype.foo = function() {};" +
      "Foo.prototype.foo = function() {};";

    Node root = parseTestCode(code);

    RenamePrototypes pass = new RenamePrototypes(compiler, false, null, null);
    pass.process(null, root);

    Node foo = root.findChildByName("Foo");
    Node prototype = foo.findChildByName("prototype");
    Node foo1 = prototype.findChildByName("foo");
    Node foo2 = prototype.findChildByName("foo");

    // Both foo1 and foo2 should be renamed
    assertEquals("_FOO_1_", foo1.getString());
    assertEquals("_FOO_2_", foo2.getString());
  }

  /**
   * Tests that properties are not renamed if they have the same name as a
   * reserved character.
   */
  @Test
  public void testPropertiesNotRenamedIfNamedWithReservedCharacter() {
    String code =
      "function Foo() {}" +
      "Foo.prototype.# = function() {};";

    Node root = parseTestCode(code);

    RenamePrototypes pass = new RenamePrototypes(compiler, false, new char[]{'#'}, null);
    pass.process(null, root);

    Node foo = root.findChildByName("Foo");
    Node prototype = foo.findChildByName("prototype");
    Node sharp = prototype.findChildByName("#");
    assertEquals("#", sharp.getString());
  }

  /**
   * Tests that properties are renamed if they have the same name as a
   * reserved character but the character is not specified in the list of
   * reserved characters.
   */
  @Test
  public void testPropertiesRenamedIfNamedWithReservedCharacterButNotInList() {
    String code =
      "function Foo() {}" +
      "Foo.prototype.@ = function() {};";

    Node root = parseTestCode(code);

    RenamePrototypes pass = new RenamePrototypes(compiler, false, new char[]{'#'}, null);
    pass.process(null, root);

    Node foo = root.findChildByName("Foo");
    Node prototype = foo.findChildByName("prototype");
    Node atSymbol = prototype.findChildByName("@");
    assertEquals("@", atSymbol.getString());

    pass = new RenamePrototypes(compiler, false, new char[]{'@'}, null);
    pass.process(null, root);

    assertEquals("_AT_", atSymbol.getString());
  }

  /**
   * Tests that properties are renamed even if they have the same name as a
   * reserved character but the character is not specified in the list of
   * reserved characters.
   */
  @Test
  public void testPropertiesRenamedEvenIfNamedWithReservedCharacterButNotInList() {
    String code =
      "function Foo() {}" +
      "Foo.prototype.% = function() {};";

    Node root = parseTestCode(code);

    RenamePrototypes pass = new RenamePrototypes(compiler, false, null, null);
    pass.process(null, root);

    Node foo = root.findChildByName("Foo");
    Node prototype = foo.findChildByName("prototype");
    Node percent = prototype.findChildByName("%");
    assertEquals("%", percent.getString());

    pass = new RenamePrototypes(compiler, false, null, null);
    pass.process(null, root);

    assertEquals("_PERCENT_", percent.getString());
  }

  /**
   * Tests that properties are renamed even if they have the same name as a
   * reserved character and the character is specified in the list of
   * reserved characters.
   */
  @Test
  public void testPropertiesNotRenamedIfNamedWithReservedCharacterAndInList() {
    String code =
      "function Foo() {}" +
      "Foo.prototype.# = function() {};";

    Node root = parseTestCode(code);

    RenamePrototypes pass = new RenamePrototypes(compiler, false, new char[]{'#'}, null);
    pass.process(null, root);

    Node foo = root.findChildByName("Foo");
    Node prototype = foo.findChildByName("prototype");
    Node sharp = prototype.findChildByName("#");
    assertEquals("#", sharp.getString());

    pass = new RenamePrototypes(compiler, false, new char[]{'#'}, null);
    pass.process(null, root);

    assertEquals("#", sharp.getString());
  }

  private Node parseTestCode(String code) {
    TokenStream tokens = new TokenStream(nodeFactory, new StringReader(code));
    return compiler.parseTree(tokens, input, null);
  }
}