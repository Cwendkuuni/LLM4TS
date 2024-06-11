import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.VariableMap;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RenamePrototypesTest {

  @Parameterized.Parameters(name = "{0}")
  public static List<String> data() {
    return Arrays.asList("RenamePrototypesTest0", "RenamePrototypesTest1",
        "RenamePrototypesTest2");
  }

  private String name;
  private CompilerOptions options;
  private CompilerPass renamePrototypes;
  private AbstractCompiler compiler;
  private CompilerInput input;
  private Node root;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    options.setLanguageIn(CompilerOptions.Language.ECMASCRIPT6);
    compiler = new AbstractCompiler(options);
    input = new CompilerInput(name, "source", "");
    root = new Node(Token.PROGRAM);
    renamePrototypes = new RenamePrototypes(compiler, false, null, null);
  }

  @Test
  public void testRenamePrototypes() {
    // Test empty class
    processInput("");
    assertNull(renamePrototypes.getPropertyMap());

    // Test simple class with no prototype properties or object literals
    processInput("class C {}");
    assertNull(renamePrototypes.getPropertyMap());

    // Test simple class with prototype properties and object literals
    processInput("class C {\n" +
        "  prototypeProp1 = 1;\n" +
        "  prototypeProp2 = 2;\n" +
        "  objLit = {\n" +
        "    prop1: 1,\n" +
        "    prop2: 2\n" +
        "  };\n" +
        "}");
    PropertyMap propertyMap = renamePrototypes.getPropertyMap();
    assertEquals("prototypeProp1", propertyMap.lookup("prototypeProp1"));
    assertEquals("prototypeProp2", propertyMap.lookup("prototypeProp2"));
    assertNull(propertyMap.lookup("objLit"));

    // Test class with prototype property and object literal that shadows it
    processInput("class C {\n" +
        "  prototypeProp = 1;\n" +
        "  objLit = {\n" +
        "    prop: 1\n" +
        "  };\n" +
        "}");
    propertyMap = renamePrototypes.getPropertyMap();
    assertEquals("prototypeProp", propertyMap.lookup("prototypeProp"));
    assertNull(propertyMap.lookup("prop"));

    // Test class with prototype property and multiple object literals
    processInput("class C {\n" +
        "  prototypeProp = 1;\n" +
        "  objLit1 = {\n" +
        "    prop1: 1,\n" +
        "    prop2: 2\n" +
        "  };\n" +
        "  objLit2 = {\n" +
        "    prop1: 1,\n" +
        "    prop3: 3\n" +
        "  };\n" +
        "}");
    propertyMap = renamePrototypes.getPropertyMap();
    assertEquals("prototypeProp", propertyMap.lookup("prototypeProp"));
    assertNull(propertyMap.lookup("prop1"));
    assertNull(propertyMap.lookup("prop3"));

    // Test aggressive renaming
    processInput("class C {\n" +
        "  _prototypeProp1 = 1;\n" +
        "  _prototypeProp2 = 2;\n" +
        "  objLit = {\n" +
        "    _prop1: 1,\n" +
        "    _prop2: 2\n" +
        "  };\n" +
        "}");
    propertyMap = renamePrototypes.getPropertyMap();
    assertEquals("_prototypeProp1", propertyMap.lookup("_prototypeProp1"));
    assertEquals("_prototypeProp2", propertyMap.lookup("_prototypeProp2"));
    assertNull(propertyMap.lookup("prop1"));
    assertNull(propertyMap.lookup("prop2"));

    // Test reserved names
    processInput("class C {\n" +
        "  indexOf = 1;\n" +
        "  lastIndexOf = 2;\n" +
        "  toString = 3;\n" +
        "  valueOf = 4;\n" +
        "  prototypeProp = 5;\n" +
        "}");
    propertyMap = renamePrototypes.getPropertyMap();
    assertEquals("indexOf", propertyMap.lookup("indexOf"));
    assertEquals("lastIndexOf", propertyMap.lookup("lastIndexOf"));
    assertEquals("toString", propertyMap.lookup("toString"));
    assertEquals("valueOf", propertyMap.lookup("valueOf"));
    assertNull(propertyMap.lookup("prototypeProp"));

    // Test reusing names from previous compilation
    processInput("class C {\n" +
        "  _prop1 = 1;\n" +
        "}");
    propertyMap = renamePrototypes.getPropertyMap();
    assertNull(propertyMap.lookup("prop1"));
    assertNull(propertyMap.lookup("_prop1"));
    processInput("class C {\n" +
        "  _prop1 = 1;\n" +
        "  _prop2 = 2;\n" +
        "}");
    propertyMap = renamePrototypes.getPropertyMap();
    assertEquals("_prop1", propertyMap.lookup("_prop1"));
    assertEquals("_prop2", propertyMap.lookup("_prop2"));

    // Test multiple classes
    processInput("class C1 {\n" +
        "  prototypeProp1 = 1;\n" +
        "  objLit = {\n" +
        "    prop1: 1,\n" +
        "    prop2: 2\n" +
        "  };\n" +
        "}" +
        "\n" +
        "class C2 {\n" +
        "  prototypeProp2 = 1;\n" +
        "  objLit = {\n" +
        "    prop1: 1,\n" +
        "    prop3: 3\n" +
        "  };\n" +
        "}");
    propertyMap = renamePrototypes.getPropertyMap();
    assertEquals("prototypeProp1", propertyMap.lookup("prototypeProp1"));
    assertEquals("prototypeProp2", propertyMap.lookup("prototypeProp2"));
    assertNull(propertyMap.lookup("prop1"));
    assertNull(propertyMap.lookup("prop3"));
  }

  private void processInput(String source) {
    compiler.addSourceFile(input);
    Node externs = new Node(Token.PROGRAM);
    compiler.parseMainSourceUnit(input);
    Node root = compiler.getMainSourceUnit().getTree();
    new NodeTraversal(compiler, root, new ProcessInput()).process();
    renamePrototypes.process(externs, root);
  }
}