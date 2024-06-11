import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenamePrototypes.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.SourceFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for the RenamePrototypes compiler pass.
 */
public class RenamePrototypesTest {

  @Nullable
  private static Node getPropertyNode(Node root, String name) {
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.GETPROP && child.getString().equals(name)) {
        return child.getFirstChild();
      }
    }
    return null;
  }

  @Nullable
  private static Node getPrototypeObjectLiteral(Node root) {
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.OBJECTLIT && isPrototypeAssignment(child)) {
        return child;
      }
    }
    return null;
  }

  private static boolean isPrototypeAssignment(Node objLit) {
    for (Node key = objLit.getFirstChild(); key != null; key = key.getNext()) {
      if (key.getType() != Token.STRING || !key.getString().equals("prototype")) {
        return false;
      }

      Node value = key.getNext();
      if (value.getType() != Token.OBJECTLIT) {
        return false;
      }
      return true;
    }
    return false;
  }

  @Test
  public void testRenamesPrototypeProperties() throws IOException {
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);

    AbstractCompiler abstractCompiler = compiler.getAbstractCompiler();
    abstractCompiler.addPass(new RenamePrototypes(abstractCompiler, false, null, null));

    SourceFile input =
        new SourceFile("test.js", "function Foo() {}\n" +
                      "Foo.prototype.bar = function() {}\n" +
                      "function Foo2() {}\n" +
                      "Foo2.prototype.bar = function() {}\n");

    CompilerResult result = compiler.compile(input, null);

    Node code = result.toSource();

    assertNotNull(getPropertyNode(code, "bar"));
    assertNotNull(getPropertyNode(code, "prototype"));
    assertNotNull(getPropertyNode(code, "prototype.bar"));

    // Test that property names weren't renamed if they have the same name as
    // built-in JS functions
    assertNull(getPropertyNode(code, "indexOf"));
    assertNull(getPropertyNode(code, "lastIndexOf"));
    assertNull(getPropertyNode(code, "toString"));
    assertNull(getPropertyNode(code, "valueOf"));

    // Test that new properties were renamed
    assertNotNull(getPropertyNode(code, "prototype.renamedBar"));
    assertNotNull(getPropertyNode(code, "prototype.renamedBarFunction"));

    // Test that the prototype object literal was renamed
    Node prototypeObjLit = getPrototypeObjectLiteral(code);
    assertNotNull(prototypeObjLit);
    assertNotNull(getPropertyNode(prototypeObjLit, "renamedBar"));
    assertNotNull(getPropertyNode(prototypeObjLit, "renamedBarFunction"));
  }

  @Test
  public void testDoesNotRenamePrototypePropertiesWithLeadingUnderscore()
      throws IOException {
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);

    AbstractCompiler abstractCompiler = compiler.getAbstractCompiler();
    abstractCompiler.addPass(new RenamePrototypes(abstractCompiler, false, null, null));

    SourceFile input =
        new SourceFile("test.js", "function Foo() {}\n" +
                      "Foo.prototype._bar = function() {}\n" +
                      "function Foo2() {}\n" +
                      "Foo2.prototype._bar = function() {}\n");

    CompilerResult result = compiler.compile(input, null);

    Node code = result.toSource();

    assertNotNull(getPropertyNode(code, "_bar"));
    assertNotNull(getPropertyNode(code, "_prototype"));
    assertNotNull(getPropertyNode(code, "_prototype._bar"));

    // Test that new properties weren't renamed even if their names have the
    // same characters as built-in JS functions
    assertNull(getPropertyNode(code, "indexOf"));
    assertNull(getPropertyNode(code, "lastIndexOf"));
    assertNull(getPropertyNode(code, "toString"));
    assertNull(getPropertyNode(code, "valueOf"));

    // Test that the prototype object literal was not renamed
    Node prototypeObjLit = getPrototypeObjectLiteral(code);
    assertNotNull(prototypeObjLit);
    assertNotNull(getPropertyNode(prototypeObjLit, "_bar"));
    assertNotNull(getPropertyNode(prototypeObjLit, "_prototype"));
    assertNotNull(getPropertyNode(prototypeObjLit, "_prototype._bar"));
  }

  @Test
  public void testRenamesObjLitProperties() throws IOException {
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);

    AbstractCompiler abstractCompiler = compiler.getAbstractCompiler();
    abstractCompiler.addPass(new RenamePrototypes(abstractCompiler, false, null, null));

    SourceFile input =
        new SourceFile("test.js", "{\n" +
                      "  bar: 1,\n" +
                      "  prototype: { bar: 2 },\n" +
                      "  prototype2: { bar: 3 },\n" +
                      "}");

    CompilerResult result = compiler.compile(input, null);

    Node code = result.toSource();

    assertNotNull(code.getFirstChild().getNext()); // Test that the object literal was processed

    assertNotNull(getPropertyNode(code, "prototype.renamedBar"));
    assertNotNull(getPropertyNode(code, "prototype2.renamedBar"));
  }

  @Test
  public void testDoesNotRenameObjLitPropertiesWithSameNameAsBuiltInFunctions()
      throws IOException {
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);

    AbstractCompiler abstractCompiler = compiler.getAbstractCompiler();
    abstractCompiler.addPass(new RenamePrototypes(abstractCompiler, false, null, null));

    SourceFile input =
        new SourceFile("test.js", "{\n" +
                      "  indexOf: 1,\n" +
                      "  lastIndexOf: 2,\n" +
                      "  toString: 3,\n" +
                      "  valueOf: 4,\n" +
                      "  bar: 5,\n" +
                      "}");

    CompilerResult result = compiler.compile(input, null);

    Node code = result.toSource();

    assertNotNull(code.getFirstChild().getNext()); // Test that the object literal was processed

    assertNull(getPropertyNode(code, "indexOf"));
    assertNull(getPropertyNode(code, "lastIndexOf"));
    assertNull(getPropertyNode(code, "toString"));
    assertNull(getPropertyNode(code, "valueOf"));

    assertNotNull(getPropertyNode(code, "bar"));
  }

  @Test
  public void testRenamesPrototypePropertiesWithAggressiveRenaming()
      throws IOException {
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);

    AbstractCompiler abstractCompiler = compiler.getAbstractCompiler();
    abstractCompiler.addPass(new RenamePrototypes(abstractCompiler, true, null, null));

    SourceFile input =
        new SourceFile("test.js", "function Foo() {}\n" +
                      "Foo.prototype.bar = function() {}\n" +
                      "function Foo2() {}\n" +
                      "Foo2.prototype.bar = function() {}\n");

    CompilerResult result = compiler.compile(input, null);

    Node code = result.toSource();

    assertNotNull(getPropertyNode(code, "prototype.bar"));
    assertNotNull(getPropertyNode(code, "prototype.barFunction"));

    assertNotNull(getPropertyNode(code, "prototype.renamedBar"));
    assertNotNull(getPropertyNode(code, "prototype.renamedBarFunction"));

    assertNotNull(getPropertyNode(code, "prototype2.renamedBar"));
    assertNotNull(getPropertyNode(code, "prototype2.renamedBarFunction"));
  }

  @Test
  public void testRenamesPrototypePropertiesWithMixedRenamingPolicies()
      throws IOException {
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);

    AbstractCompiler abstractCompiler = compiler.getAbstractCompiler();
    abstractCompiler.addPass(new RenamePrototypes(abstractCompiler, false, null, null));

    SourceFile input =
        new SourceFile("test.js", "function Foo() {}\n" +
                      "Foo.prototype.bar = function() {}\n" +
                      "Foo.prototype.prototype = function() {}\n" +
                      "function Foo2() {}\n" +
                      "Foo2.prototype.bar = function() {}\n");

    CompilerResult result = compiler.compile(input, null);

    Node code = result.toSource();

    assertNotNull(getPropertyNode(code, "prototype.bar"));
    assertNotNull(getPropertyNode(code, "prototype.barFunction"));

    assertNull(getPropertyNode(code, "prototype.prototype"));
    assertNull(getPropertyNode(code, "prototype.prototypeFunction"));

    assertNotNull(getPropertyNode(code, "prototype2.renamedBar"));
    assertNotNull(getPropertyNode(code, "prototype2.renamedBarFunction"));
  }

  @Test
  public void testDoesNotRenamePrototypePropertiesWithSameNameAsPrototypeProperties()
      throws IOException {
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);

    AbstractCompiler abstractCompiler = compiler.getAbstractCompiler();
    abstractCompiler.addPass(new RenamePrototypes(abstractCompiler, false, null, null));

    SourceFile input =
        new SourceFile("test.js", "function Foo() {}\n" +
                      "Foo.prototype.bar = function() {}\n" +
                      "function Foo2() {}\n" +
                      "Foo.prototype.prototype = function() {}\n" +
                      "Foo2.prototype.bar = function() {}\n" +
                      "Foo2.prototype.prototype = function() {}\n");

    CompilerResult result = compiler.compile(input, null);

    Node code = result.toSource();

    assertNotNull(getPropertyNode(code, "prototype.bar"));
    assertNotNull(getPropertyNode(code, "prototype.barFunction"));

    assertNotNull(getPropertyNode(code, "prototype.prototype"));
    assertNotNull(getPropertyNode(code, "prototype.prototypeFunction"));

    assertNotNull(getPropertyNode(code, "prototype2.bar"));
    assertNotNull(getPropertyNode(code, "prototype2.barFunction"));

    assertNotNull(getPropertyNode(code, "prototype2.prototype"));
    assertNotNull(getPropertyNode(code, "prototype2.prototypeFunction"));
  }

  @Test
  public void testDoesNotRenamePrototypePropertiesWithSameNameAsBuiltInFunctions()
      throws IOException {
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);

    AbstractCompiler abstractCompiler = compiler.getAbstractCompiler();
    abstractCompiler.addPass(new RenamePrototypes(abstractCompiler, false, null, null));

    SourceFile input =
        new SourceFile("test.js", "function Foo() {}\n" +
                      "Foo.prototype.indexOf = function() {}\n" +
                      "function Foo2() {}\n" +
                      "Foo.prototype.lastIndexOf = function() {}\n" +
                      "Foo2.prototype.toString = function() {}\n" +
                      "Foo2.prototype.valueOf = function() {}\n");

    CompilerResult result = compiler.compile(input, null);

    Node code = result.toSource();

    assertNotNull(code.getFirstChild().getNext()); // Test that the code was processed

    assertNull(getPropertyNode(code, "indexOf"));
    assertNull(getPropertyNode(code, "lastIndexOf"));
    assertNull(getPropertyNode(code, "toString"));
    assertNull(getPropertyNode(code, "valueOf"));

    assertNotNull(getPropertyNode(code, "prototype.bar"));
    assertNotNull(getPropertyNode(code, "prototype.barFunction"));
  }
}