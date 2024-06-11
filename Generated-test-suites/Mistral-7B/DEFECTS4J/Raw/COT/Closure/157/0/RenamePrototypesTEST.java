import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.rhino.TokenStream;

import org.junit.Before;
import org.junit.Test;

public class RenamePrototypesTest {

  Compiler compiler;
  CompilerOptions options;
  RenamePrototypes renamePrototypes;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = new Compiler(options);
    renamePrototypes = new RenamePrototypes(compiler, false, null, null);
  }

  @Test
  public void testBasicRenamePrototypeProperty() {
    String code = "var obj = {\n" +
        "  oldProp: function() {},\n" +
        "  oldProto: {\n" +
        "    oldProp: function() {}\n" +
        "  }\n" +
        "};\n" +
        "renamePrototypes.process(null, null);\n" +
        "assertEquals(\"newProp\", obj.oldProp.name);\n" +
        "assertEquals(\"newProtoNewProp\", obj.oldProto.oldProp);\n";

    Node root = new TokenStream(code).parse();
    renamePrototypes.process(null, root);

    assertEquals("newProp", root.getChild(0).getLastChild().getName());
    assertEquals("newProtoNewProp", root.getChild(1).getChild(0).getLastChild().getName());
  }

  @Test
  public void testBasicRenameObjLitProperty() {
    String code = "var obj = {\n" +
        "  oldObj: {\n" +
        "    oldProp: 42\n" +
        "  }\n" +
        "};\n" +
        "renamePrototypes.process(null, null);\n" +
        "assertEquals(\"newObjNewProp\", obj.oldObj);\n" +
        "assertEquals(\"newProp\", obj.oldObj.oldProp);\n" +
        "assertEquals(42, obj.oldObj.newProp);\n";

    Node root = new TokenStream(code).parse();
    renamePrototypes.process(null, root);

    assertEquals("newObjNewProp", root.getChild(0).getName());
    assertEquals("newProp", root.getChild(0).getChild(0).getName());
    assertEquals(42, root.getChild(0).getChild(0).getFirstChild().getIntValue());
  }

  @Test
  public void testRenameMultipleProperties() {
    String code = "var obj = {\n" +
        "  oldProp1: function() {},\n" +
        "  oldProp2: function() {},\n" +
        "  oldProto: {\n" +
        "    oldProp3: function() {},\n" +
        "    oldProp4: function() {}\n" +
        "  }\n" +
        "};\n" +
        "renamePrototypes.process(null, null);\n" +
        "assertEquals(\"newProp1\", obj.oldProp1.name);\n" +
        "assertEquals(\"newProp3\", obj.oldProto.oldProp3);\n" +
        "assertEquals(\"newProp4\", obj.oldProto.oldProp4);\n";

    Node root = new TokenStream(code).parse();
    renamePrototypes.process(null, root);

    assertEquals("newProp1", root.getChild(0).getLastChild().getName());
    assertEquals("newProp3", root.getChild(1).getChild(0).getLastChild().getName());
    assertEquals("newProp4", root.getChild(1).getChild(1).getLastChild().getName());
  }

  @Test
  public void testReservedNamesNotRenamed() {
    String code = "var obj = {\n" +
        "  indexOf: function() {},\n" +
        "  lastIndexOf: function() {},\n" +
        "  toString: function() {},\n" +
        "  valueOf: function() {},\n" +
        "  oldProp: function() {},\n" +
        "  oldProto: {\n" +
        "    oldProp: function() {}\n" +
        "  }\n" +
        "};\n" +
        "renamePrototypes.process(null, null);\n";

    Node root = new TokenStream(code).parse();
    renamePrototypes.process(null, root);

    assertEquals("indexOf", root.getChild(0).getLastChild().getName());
    assertEquals("lastIndexOf", root.getChild(1).getLastChild().getName());
    assertEquals("toString", root.getChild(2).getLastChild().getName());
    assertEquals("valueOf", root.getChild(3).getLastChild().getName());
    assertEquals("oldProp", root.getChild(4).getLastChild().getName());
    assertEquals("oldProp", root.getChild(5).getChild(0).getLastChild().getName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullExterns() {
    String code = "renamePrototypes.process(null, null);";
    Node root = new TokenStream(code).parse();
    renamePrototypes.process(null, root);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullCode() {
    String code = "renamePrototypes.process(null, null);";
    Node root = null;
    renamePrototypes.process(null, root);
  }
}