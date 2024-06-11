package com.google.javascript.jscomp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.CompilerTestBase;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

@RunWith(Parameterized.class)
public class RenamePrototypesTest extends CompilerTestBase {

  private static final String TEST_CASE_1 = "test1.js";
  private static final String TEST_CASE_2 = "test2.js";
  private static final String TEST_CASE_3 = "test3.js";
  private static final String TEST_CASE_4 = "test4.js";
  private static final String TEST_CASE_5 = "test5.js";
  private static final String TEST_CASE_6 = "test6.js";

  @Parameters(name = "{0}")
  public static Iterable<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {TEST_CASE_1},
        {TEST_CASE_2},
        {TEST_CASE_3},
        {TEST_CASE_4},
        {TEST_CASE_5},
        {TEST_CASE_6}
    });
  }

  private final String testCaseName;

  public RenamePrototypesTest(String testCaseName) {
    this.testCaseName = testCaseName;
  }

  @Test
  public void testRenamePrototypes() {
    CompilerInput input = getInputFromFile(testCaseName);
    CompilerOptions options = getDefaultOptions();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    CompilerResult result = compileWithPasses(input, Arrays.asList(pass), options);

    Node root = result.getRootNode();

    // Basic functionality test
    testRenamePrototypeProperty(root, "prop1", "renamedProp1");
    testRenamePrototypeProperty(root, "prop2_", "renamedProp2_");
    testRenamePrototypeProperty(root, "_prop3", "renamed_prop3");
    testRenamePrototypeProperty(root, "prop4", null); // should be unchanged
    testRenamePrototypeProperty(root, "prop5__", null); // should be unchanged

    // Test edge cases and exceptions
    testRenameObjLitProperty(root, "objProp1", "renamedObjProp1");
    testRenameObjLitProperty(root, "objProp2", null); // should be unchanged
    testRenameObjLitProperty(root, "objProp3_", null); // should be unchanged
    testRenameObjLitProperty(root, "objProp4__", null); // should be unchanged

    testRenamePrototypePropertyWithSameNameInPrototypeAndObjLit(root, "sameName");
    testRenamePrototypePropertyWithSameNameInPrototypeAndObjLitAndInExported(root, "sameNameExported");
    testRenamePrototypePropertyWithSameNameInPrototypeAndObjLitAndInPrivate(root, "sameNamePrivate");

    testRenamePrototypeWithMultipleAssignments(root, "propToRename");
    testRenamePrototypeWithMultipleAssignmentsAndAggressiveRenaming(root, "propToRenameAggressive");

    // Test reusing prototype names from the previous compilation
    testReusingPrototypeNames(root);
  }

  // Basic functionality test for prototype properties
  private void testRenamePrototypeProperty(Node root, String oldName, String newName) {
    Property expectedProperty = assertPropertyExists(root, oldName);

    if (newName != null) {
      assertEquals(newName, expectedProperty.newName);
    } else {
      assertNull(expectedProperty.newName);
    }
  }

  // Test prototype properties with edge cases and exceptions
  private void testRenamePrototypePropertyWithSameNameInPrototypeAndObjLit(Node root) {
    String oldName = "sameName";
    Property prop = assertPropertyExists(root, oldName);

    // Check that the property was detected as a prototype property
    assertTrue(prop.prototypeCount > 0);
    assertFalse(prop.objLitCount > 0);

    // Check that the property is still present in the root after renaming
    Node renamedProp = findChildByName(root, prop.newName);
    assertNotNull(renamedProp);
  }

  private void testRenamePrototypePropertyWithSameNameInPrototypeAndObjLitAndInExported(Node root) {
    String oldName = "sameNameExported";
    Property prop = assertPropertyExists(root, oldName);

    // Check that the property was detected as a prototype property
    assertTrue(prop.prototypeCount > 0);
    assertFalse(prop.objLitCount > 0);

    // Check that the property is still present in the root after renaming
    Node renamedProp = findChildByName(root, prop.newName);
    assertNotNull(renamedProp);

    // Check that the property is not renamed when it's exported
    assertSame(oldName, prop.oldName);
  }

  private void testRenamePrototypePropertyWithSameNameInPrototypeAndObjLitAndInPrivate(Node root) {
    String oldName = "sameNamePrivate";
    Property prop = assertPropertyExists(root, oldName);

    // Check that the property was detected as a prototype property
    assertTrue(prop.prototypeCount > 0);
    assertFalse(prop.objLitCount > 0);

    // Check that the property is still present in the root after renaming
    Node renamedProp = findChildByName(root, prop.newName);
    assertNotNull(renamedProp);

    // Check that the property is not renamed when it's private
    assertSame(oldName, prop.oldName);
  }

  // Test obj liter properties with edge cases and exceptions
  private void testRenameObjLitProperty(Node root, String oldName, String newName) {
    Property expectedProperty = assertObjLitPropertyExists(root, oldName);

    if (newName != null) {
      assertEquals(newName, expectedProperty.newName);
    } else {
      assertNull(expectedProperty.newName);
    }
  }

  // Test other tests
  private void testReusingPrototypeNames(Node root) {
    // Check that previous prototype names are reused
    Property oldName1 = assertPropertyExists(root, "property1");
    Property oldName2 = assertPropertyExists(root, "property2");

    String newName1 = "renamedProperty1";
    String newName2 = "renamedProperty2";

    // Reuse prototype names from the previous compilation
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(null, root);

    Property newName1Property = assertPropertyExists(root, newName1);
    Property newName2Property = assertPropertyExists(root, newName2);

    assertSame(oldName1, newName1Property.oldName);
    assertSame(oldName2, newName2Property.oldName);
  }

  private void testRenamePrototypeWithMultipleAssignments(Node root, String oldName) {
    Property expectedProperty = assertPropertyExists(root, oldName);

    // Check that the property was detected as a prototype property
    assertTrue(expectedProperty.prototypeCount > 0);
    assertFalse(expectedProperty.objLitCount > 0);

    // Check that the property is still present in the root after renaming
    Node renamedProp = findChildByName(root, expectedProperty.newName);
    assertNotNull(renamedProp);

    // Check that the property is renamed even when it has multiple assignments
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(null, root);

    Node renamedProp2 = findChildByName(root, expectedProperty.newName);
    assertNotNull(renamedProp2);
  }

  private void testRenamePrototypeWithMultipleAssignmentsAndAggressiveRenaming(Node root, String oldName) {
    Property expectedProperty = assertPropertyExists(root, oldName);

    // Check that the property was detected as a prototype property
    assertTrue(expectedProperty.prototypeCount > 0);
    assertFalse(expectedProperty.objLitCount > 0);

    // Check that the property is still present in the root after renaming
    Node renamedProp = findChildByName(root, expectedProperty.newName);
    assertNotNull(renamedProp);

    // Check that the property is aggressively renamed
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(null, root);

    Node renamedProp2 = findChildByName(root, expectedProperty.newName);
    assertNotNull(renamedProp2);

    String newName = "renamed_property";
    assertEquals(newName, renamedProp2.getFirstChild().getString());
  }

  // Helper methods
  private Property assertPropertyExists(Node root, String oldName) {
    Property property = properties.get(oldName);
    if (property == null) {
      fail("Could not find property with name: " + oldName);
    }
    return property;
  }

  private Property assertObjLitPropertyExists(Node root, String oldName) {
    Property property = objLitProperties.get(oldName);
    if (property == null) {
      fail("Could not find obj lit property with name: " + oldName);
    }
    return property;
  }

  private Node findChildByName(Node root, String name) {
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.GETPROP || child.getType() == Token.GETELEM) {
        Node nameNode = child.getFirstChild().getNext();
        if (nameNode.getString().equals(name)) {
          return child;
        }
      }
    }
    return null;
  }

  private Map<String, Property> properties = new HashMap<>();
  private Map<String, Property> objLitProperties = new HashMap<>();

  @Override
  protected CompilerPass getPass() {
    return new RenamePrototypes(compiler, true, null, null);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    compile(TEST_CASE_1);
    compile(TEST_CASE_2);
    compile(TEST_CASE_3);
    compile(TEST_CASE_4);
    compile(TEST_CASE_5);
    compile(TEST_CASE_6);

    Node testCase1 = astMap.get(TEST_CASE_1);
    Node testCase2 = astMap.get(TEST_CASE_2);
    Node testCase3 = astMap.get(TEST_CASE_3);
    Node testCase4 = astMap.get(TEST_CASE_4);
    Node testCase5 = astMap.get(TEST_CASE_5);
    Node testCase6 = astMap.get(TEST_CASE_6);

    properties.putAll(RenamePrototypes.processProperties(testCase1));
    properties.putAll(RenamePrototypes.processProperties(testCase2));
    properties.putAll(RenamePrototypes.processProperties(testCase3));
    properties.putAll(RenamePrototypes.processProperties(testCase4));
    properties.putAll(RenamePrototypes.processProperties(testCase5));
    properties.putAll(RenamePrototypes.processProperties(testCase6));

    objLitProperties.putAll(RenamePrototypes.processObjLitProperties(testCase1));
    objLitProperties.putAll(RenamePrototypes.processObjLitProperties(testCase2));
    objLitProperties.putAll(RenamePrototypes.processObjLitProperties(testCase3));
    objLitProperties.putAll(RenamePrototypes.processObjLitProperties(testCase4));
    objLitProperties.putAll(RenamePrototypes.processObjLitProperties(testCase5));
    objLitProperties.putAll(RenamePrototypes.processObjLitProperties(testCase6));
  }

  // 
}