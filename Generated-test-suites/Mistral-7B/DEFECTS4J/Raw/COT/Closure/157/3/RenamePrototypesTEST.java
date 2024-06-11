import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenamePrototypes.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SimpleNode;
import com.google.javascript.rhino.TokenStream;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RenamePrototypesTest {

  @SuppressWarnings("unchecked")
  private static <T> T newNode(int type, Object... children) {
    Node node = new SimpleNode(type);
    for (Object child : children) {
      node.addChild((Node) child);
    }
    return (T) node;
  }

  @SuppressWarnings("unchecked")
  private static Node newStringNode(String value) {
    return (Node) new SimpleNode(Token.STRING, value);
  }

  @Test
  public void testSimpleRename() throws Exception {
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    TokenStream input = new TokenStream(new StringBuilder("var obj = { foo : 1 };\n"));
    Node root = input.parseProgram();

    renamePrototypes.process(null, root);

    Map<String, String> expectedRenameMap = Maps.newHashMap();
    expectedRenameMap.put("foo", "_foo");

    assertEquals(expectedRenameMap, renamePrototypes.getPropertyMap());

    Node output = compiler.getIntermediateRepresentation();
    String expectedOutput = "var obj = { _foo : 1 };\n";
    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testPrototypePropertyRename() throws Exception {
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    Node prototypeNode = newNode(Token.GETPROP, Token.IDENT, newNode(Token.STRING, "prototype"));
    Node prototypeObjLit = newNode(Token.OBJECTLIT);
    prototypeObjLit.addChild(newNode(Token.ASSIGN, prototypeNode, prototypeObjLit));
    Node prototypeObjLitProperty = newNode(Token.ASSIGN, newStringNode("getBar"), null);
    prototypeObjLit.addChild(prototypeObjLitProperty);
    Node root = newNode(Token.ASSIGN, prototypeObjLit, newNode(Token.GETPROP, Token.IDENT, Token.IDENT, newNode(Token.STRING, "Foo")));

    renamePrototypes.process(null, root);

    Map<String, String> expectedRenameMap = Maps.newHashMap();
    expectedRenameMap.put("getBar", "_getBar");

    assertEquals(expectedRenameMap, renamePrototypes.getPropertyMap());

    Node output = compiler.getIntermediateRepresentation();
    String expectedOutput = "var Foo$1$ = Foo;\n" +
        "Foo$1$.prototype.prototype = {\n" +
        "  'getBar' : {}\n" +
        "};\n";
    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testPrototypePropertyRenameAggressive() throws Exception {
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    Node prototypeNode = newNode(Token.GETPROP, Token.IDENT, newNode(Token.STRING, "prototype"));
    Node prototypeObjLit = newNode(Token.OBJECTLIT);
    prototypeObjLit.addChild(newNode(Token.ASSIGN, prototypeNode, prototypeObjLit));
    Node prototypeObjLitProperty = newNode(Token.ASSIGN, newNode(Token.STRING, "getFoo"), null);
    prototypeObjLit.addChild(prototypeObjLitProperty);
    Node root = newNode(Token.ASSIGN, prototypeObjLit, newNode(Token.GETPROP, Token.IDENT, Token.IDENT, newNode(Token.STRING, "Foo")));

    renamePrototypes.process(null, root);

    Map<String, String> expectedRenameMap = Maps.newHashMap();
    expectedRenameMap.put("getFoo", "_getFoo");

    assertEquals(expectedRenameMap, renamePrototypes.getPropertyMap());

    Node output = compiler.getIntermediateRepresentation();
    String expectedOutput = "var Foo$1$ = Foo;\n" +
        "Foo$1$.prototype.prototype = {\n" +
        "  'getFoo' : {}\n" +
        "};\n";
    assertEquals(expectedOutput, output.toString());
  }

  @Test(expected = AssertionError.class)
  public void testPrototypePropertyReservedName() throws Exception {
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    Node prototypeNode = newNode(Token.GETPROP, Token.IDENT, newNode(Token.STRING, "prototype"));
    Node prototypeObjLit = newNode(Token.OBJECTLIT);
    prototypeObjLit.addChild(newNode(Token.ASSIGN, prototypeNode, prototypeObjLit));
    Node prototypeObjLitProperty = newNode(Token.ASSIGN, newNode(Token.STRING, "toString"), null);
    prototypeObjLit.addChild(prototypeObjLitProperty);
    Node root = newNode(Token.ASSIGN, prototypeObjLit, newNode(Token.GETPROP, Token.IDENT, Token.IDENT, newNode(Token.STRING, "Foo")));

    renamePrototypes.process(null, root);

    Map<String, String> expectedRenameMap = Maps.newHashMap();
    expectedRenameMap.put("toString", "_toString");

    assertEquals(expectedRenameMap, renamePrototypes.getPropertyMap());

    Node output = compiler.getIntermediateRepresentation();
    String expectedOutput = "var Foo$1$ = Foo;\n" +
        "Foo$1$.prototype.prototype = {\n" +
        "  'toString' : {}\n" +
        "};\n";
    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testObjLitPropertyRename() throws Exception {
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    Node objLitNode = newNode(Token.OBJECTLIT);
    Node objLitProperty = newNode(Token.ASSIGN, newStringNode("getBar"), null);
    objLitNode.addChild(objLitProperty);
    Node root = newNode(Token.ASSIGN, objLitNode, newNode(Token.GETPROP, Token.IDENT, Token.IDENT, newNode(Token.STRING, "Foo")));

    renamePrototypes.process(null, root);

    Map<String, String> expectedRenameMap = Maps.newHashMap();
    expectedRenameMap.put("getBar", "_getBar");

    assertEquals(expectedRenameMap, renamePrototypes.getPropertyMap());

    Node output = compiler.getIntermediateRepresentation();
    String expectedOutput = "var Foo$1$ = Foo;\n" +
        "Foo$1$ = {\n" +
        "  '_getBar' : {}\n" +
        "};\n";
    assertEquals(expectedOutput, output.toString());
  }

  @Test(expected = AssertionError.class)
  public void testObjLitPropertyReservedName() throws Exception {
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    Node objLitNode = newNode(Token.OBJECTLIT);
    Node objLitProperty = newNode(Token.ASSIGN, newNode(Token.STRING, "toString"), null);
    objLitNode.addChild(objLitProperty);
    Node root = newNode(Token.ASSIGN, objLitNode, newNode(Token.GETPROP, Token.IDENT, Token.IDENT, newNode(Token.STRING, "Foo")));

    renamePrototypes.process(null, root);

    Map<String, String> expectedRenameMap = Maps.newHashMap();
    expectedRenameMap.put("toString", "_toString");

    assertEquals(expectedRenameMap, renamePrototypes.getPropertyMap());

    Node output = compiler.getIntermediateRepresentation();
    String expectedOutput = "var Foo$1$ = Foo;\n" +
        "Foo$1$ = {\n" +
        "  '_toString' : {}\n" +
        "};\n";
    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testPrototypePropertyRenameWithSameName() throws Exception {
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    Node prototypeNode = newNode(Token.GETPROP, Token.IDENT, newNode(Token.STRING, "prototype"));
    Node prototypeObjLit = newNode(Token.OBJECTLIT);
    prototypeObjLit.addChild(newNode(Token.ASSIGN, prototypeNode, prototypeObjLit));
    Node prototypeObjLitProperty1 = newNode(Token.ASSIGN, newStringNode("getFoo"), null);
    prototypeObjLit.addChild(prototypeObjLitProperty1);
    Node prototypeObjLitProperty2 = newNode(Token.ASSIGN, newStringNode("getFoo"), null);
    prototypeObjLit.addChild(prototypeObjLitProperty2);
    Node root = newNode(Token.ASSIGN, prototypeObjLit, newNode(Token.GETPROP, Token.IDENT, Token.IDENT, newNode(Token.STRING, "Foo")));

    renamePrototypes.process(null, root);

    Map<String, String> expectedRenameMap = Maps.newHashMap();
    expectedRenameMap.put("getFoo", "_getFoo_1_");

    assertEquals(expectedRenameMap, renamePrototypes.getPropertyMap());

    Node output = compiler.getIntermediateRepresentation();
    String expectedOutput = "var Foo$1$ = Foo;\n" +
        "Foo$1$.prototype.prototype = {\n" +
        "  '_getFoo_1_' : {}\n" +
        ",\n" +
        "  '_getFoo_2_' : {}\n" +
        "};\n";
    assertEquals(expectedOutput, output.toString());
  }
}