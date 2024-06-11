import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.*;
import static com.google.javascript.rhino.Token.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.VariableMap;

import org.junit.Before;
import org.junit.Test;

public class RenamePrototypesTest {
  private CompilerInput input;
  private Node root;
  private AbstractCompiler compiler;
  private RenamePrototypes renamePrototypes;
  private CompilerPassManager passManager;

  @Before
  public void setUp() {
    compiler = new Compiler();
    input = new CompilerInput("test.js", "test.js", null);
    root = Node.parse(input);
    renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    passManager = new CompilerPassManager(compiler);
  }

  @Test
  public void testRenamePrototypeProperty() {
    Node prototype = createPrototypeNode();
    String oldName = "oldName";
    String newName = "newName";

    // Set up the input to have the prototype with the oldName property
    replaceNodeWithString(prototype, oldName);
    input.setRoot(root);

    // Process the input
    passManager.runPasses(CompilerPass.Phase.NORMALIZED, input);

    // Verify that the property was renamed
    assertEquals(newName, getPropertyName(renamePrototypes.getPropertyMap(), oldName));
  }

  @Test
  public void testDoNotRenameExportedProperty() {
    Node prototype = createPrototypeNode();
    String oldName = "oldName";

    // Set up the input to have the prototype with the exported oldName property
    replaceNodeWithString(prototype, oldName);
    input.setRoot(root);
    compiler.getCodingConvention().setExported(oldName, true);

    // Process the input
    passManager.runPasses(CompilerPass.Phase.NORMALIZED, input);

    // Verify that the property was not renamed
    assertNull(renamePrototypes.getPropertyMap().get(oldName));
  }

  @Test
  public void testDoNotRenamePrivateProperty() {
    Node prototype = createPrototypeNode();
    String oldName = "_oldName";

    // Set up the input to have the prototype with the private oldName property
    replaceNodeWithString(prototype, oldName);
    input.setRoot(root);

    // Process the input
    passManager.runPasses(CompilerPass.Phase.NORMALIZED, input);

    // Verify that the property was not renamed
    assertNull(renamePrototypes.getPropertyMap().get(oldName));
  }

  @Test
  public void testAggressiveRenaming() {
    Node prototype = createPrototypeNode();
    String oldName = "oldName123_";

    // Set up the input to have the prototype with the aggressive oldName property
    replaceNodeWithString(prototype, oldName);
    input.setRoot(root);

    // Process the input with aggressive renaming enabled
    renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    passManager.runPasses(CompilerPass.Phase.NORMALIZED, input);

    // Verify that the property was renamed
    String newName = "P_oldName123_";
    assertEquals(newName, getPropertyName(renamePrototypes.getPropertyMap(), oldName));
  }

  @Test
  public void testDoNotRenameBuiltInMethod() {
    Node prototype = createPrototypeNode();
    String oldName = "indexOf";

    // Set up the input to have the prototype with the built-in indexOf method
    replaceNodeWithFunction(prototype, "function() { return 42; }");
    input.setRoot(root);

    // Process the input
    passManager.runPasses(CompilerPass.Phase.NORMALIZED, input);

    // Verify that the method was not renamed
    assertNull(renamePrototypes.getPropertyMap().get(oldName));
  }

  @Test
  public void testRenameMultipleProperties() {
    Node prototype = createPrototypeNode();
    String oldName1 = "oldName1";
    String oldName2 = "oldName2";
    String newName1 = "newName1";
    String newName2 = "newName2";

    // Set up the input to have the prototype with the oldName1 and oldName2 properties
    replaceNodeWithString(prototype, oldName1);
    replaceNodeWithString(prototype, oldName2);
    input.setRoot(root);

    // Process the input
    passManager.runPasses(CompilerPass.Phase.NORMALIZED, input);

    // Verify that both properties were renamed
    Map<String, String> propertyMap = renamePrototypes.getPropertyMap();
    assertEquals(newName1, getPropertyName(propertyMap, oldName1));
    assertEquals(newName2, getPropertyName(propertyMap, oldName2));
  }

  @Test(expected = IllegalStateException.class)
  public void testRenamePropertyWithSameNameTwice() {
    Node prototype = createPrototypeNode();
    String oldName = "oldName";

    // Set up the input to have the prototype with the oldName property twice
    replaceNodeWithString(prototype, oldName);
    replaceNodeWithString(prototype, oldName);
    input.setRoot(root);

    // Process the input
    passManager.runPasses(CompilerPass.Phase.NORMALIZED, input);

    // This test should fail with an IllegalStateException because the
    // RenamePrototypes class does not support properties with the same name
  }

  @Test
  public void testRenameObjLitProperty() {
    Node objLit = createObjLitNode();
    String oldName = "oldName";
    String newName = "newName";

    // Set up the input to have the objLit with the oldName property
    replaceNodeWithString(objLit, oldName);
    input.setRoot(root);

    // Process the input
    passManager.runPasses(CompilerPass.Phase.NORMALIZED, input);

    // Verify that the property was renamed
    assertEquals(newName, getPropertyName(renamePrototypes.getPropertyMap(), oldName));
  }

  @Test
  public void testDoNotRenameExternObjLitProperty() {
    Node objLit = createObjLitNode();
    String oldName = "oldName";

    // Set up the input to have the objLit with the extern oldName property
    replaceNodeWithString(objLit, oldName);
    input.setRoot(root);
    compiler.getCodingConvention().setExported(oldName, true);

    // Process the input
    passManager.runPasses(CompilerPass.Phase.NORMALIZED, input);

    // Verify that the property was not renamed
    assertNull(renamePrototypes.getPropertyMap().get(oldName));
  }

  @Test
  public void testRenameMultipleObjLitProperties() {
    Node objLit = createObjLitNode();
    String oldName1 = "oldName1";
    String oldName2 = "oldName2";
    String newName1 = "newName1";
    String newName2 = "newName2";

    // Set up the input to have the objLit with the oldName1 and oldName2 properties
    replaceNodeWithString(objLit, oldName1);
    replaceNodeWithString(objLit, oldName2);
    input.setRoot(root);

    // Process the input
    passManager.runPasses(CompilerPass.Phase.NORMALIZED, input);

    // Verify that both properties were renamed
    Map<String, String> propertyMap = renamePrototypes.getPropertyMap();
    assertEquals(newName1, getPropertyName(propertyMap, oldName1));
    assertEquals(newName2, getPropertyName(propertyMap, oldName2));
  }

  @Test
  public void testReusePrototypeName() {
    Node prototype = createPrototypeNode();
    String oldName = "oldName";
    String newName = "newName";

    // Set up the input to have the prototype with the oldName property twice
    replaceNodeWithString(prototype, oldName);
    replaceNodeWithString(prototype, oldName);
    input.setRoot(root);

    // Process the input with a previous rename map that contains the oldName
    VariableMap prevUsedRenameMap = Maps.newHashMap();
    prevUsedRenameMap.put(oldName, newName);
    renamePrototypes = new RenamePrototypes(compiler, false, null, prevUsedRenameMap);
    passManager.runPasses(CompilerPass.Phase.NORMALIZED, input);

    // Verify that both properties were renamed to the same newName
    Map<String, String> propertyMap = renamePrototypes.getPropertyMap();
    assertEquals(newName, getPropertyName(propertyMap, oldName));
  }

  private Node createPrototypeNode() {
    Node prototype = new Node_Prototype();
    prototype.setSourcePosition(new SourcePosition(1, 1, "test.js", 1));
    return prototype;
  }

  private Node createObjLitNode() {
    Node objLit = new Node_ObjectLiteral();
    objLit.setSourcePosition(new SourcePosition(1, 1, "test.js", 1));
    return objLit;
  }

  private void replaceNodeWithString(Node node, String value) {
    Node newNode = new Node_String(value);
    newNode.setSourcePosition(node.getSourcePosition());
    node.setFirstChild(newNode);
  }

  private void replaceNodeWithFunction(Node node, String code) {
    Node newNode = new Node_Function(code, false);
    newNode.setSourcePosition(node.getSourcePosition());
    node.setFirstChild(newNode);
  }

  private String getPropertyName(Map<String, String> map, String key) {
    return map.get(key);
  }
}