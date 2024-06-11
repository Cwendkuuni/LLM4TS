import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.testing.JSTestCase;

public class RecordTypeTest extends JSTestCase {

  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    registry = new JSTypeRegistry(null);
    errorReporter = new ErrorReporter() {
      @Override
      public void error(int line, int pos, String message) {}

      @Override
      public void warning(int line, int pos, String message) {}
    };
  }

  /** Basic test for the constructor. */
  public void testRecordTypeConstruction() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    RecordType recordType = new RecordType(registry, properties);
    assertEquals("RecordType", recordType.getClassName());
  }

  /** Test that a record type can be assigned to itself. */
  public void testSelfAssignment() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNativeType(
        JSTypeNative.STRING_TYPE)));
    RecordType recordType = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    assertEquals(recordType, recordType2);
  }

  /** Test that a record type can be assigned to a subtype. */
  public void testSubtypeAssignment() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNativeType(
        JSTypeNative.NUMBER_TYPE)));
    RecordType recordType = new RecordType(registry, properties);
    RecordType subtypeRecordType = new RecordType(registry, properties);
    subtypeRecordType.defineProperty("b", registry.getNativeType(
        JSTypeNative.BOOLEAN_TYPE), false, null);
    assertEquals(recordType, subtypeRecordType);
  }

  /** Test that a record type cannot be assigned to a supertype with a missing property. */
  public void testSupertypeMissingProperty() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getNativeType(
        JSTypeNative.STRING_TYPE)));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("b", new RecordProperty("b", registry.getNativeType(
        JSTypeNative.NUMBER_TYPE)));
    RecordType recordType2 = new RecordType(registry, properties2);
    assertFalse(recordType1.isSubtype(recordType2));
  }

  /** Test that a record type can be assigned to a supertype with compatible properties. */
  public void testSupertypeCompatibleProperties() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getNativeType(
        JSTypeNative.STRING_TYPE)));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getNativeType(
        JSTypeNative.NUMBER_TYPE)));
    RecordType recordType2 = new RecordType(registry, properties2);
    assertTrue(recordType1.isSubtype(recordType2));
  }

  /** Test that a record type cannot be assigned to a supertype with incompatible properties. */
  public void testSupertypeIncompatibleProperties() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getNativeType(
        JSTypeNative.STRING_TYPE)));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getNativeType(
        JSTypeNative.BOOLEAN_TYPE)));
    RecordType recordType2 = new RecordType(registry, properties2);
    assertFalse(recordType1.isSubtype(recordType2));
  }

  /** Test that a record type can be frozen. */
  public void testFreezeRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNativeType(
        JSTypeNative.STRING_TYPE)));
    RecordType recordType = new RecordType(registry, properties);
    recordType.freeze();
    assertFalse(recordType.defineProperty("b", registry.getNativeType(
        JSTypeNative.NUMBER_TYPE), false, null));
  }

  /** Test that a record type can determine its greatest subtype. */
  public void testGetGreatestSubtype() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getNativeType(
        JSTypeNative.STRING_TYPE)));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getNativeType(
        JSTypeNative.STRING_TYPE)));
    properties2.put("b", new RecordProperty("b", registry.getNativeType(
        JSTypeNative.NUMBER_TYPE)));
    RecordType recordType2 = new RecordType(registry, properties2);

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("a", new RecordProperty("a", registry.getNativeType(
        JSTypeNative.STRING_TYPE)));
    properties3.put("b", new RecordProperty("b", registry.getNativeType(
        JSTypeNative.BOOLEAN_TYPE)));
    RecordType recordType3 = new RecordType(registry, properties3);

    assertEquals(recordType1, recordType1.getGreatestSubtype(recordType1));
    assertEquals(recordType1, recordType1.getGreatestSubtype(recordType2));
    assertEquals(recordType2, recordType1.getGreatestSubtype(recordType3));

    UnionType unionType = new UnionType(registry);
    unionType.addAlternate(recordType1);
    unionType.addAlternate(recordType2);
    assertEquals(unionType, recordType1.getGreatestSubtype(recordType3));
  }

  /** Test that a record type can be assigned to a defined type with compatible properties. */
  public void testAssignToDefinedType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNativeType(
        JSTypeNative.STRING_TYPE)));
    RecordType recordType = new RecordType(registry, properties);

    ObjectType definedType = new ObjectType(registry);
    definedType.setPrototype(recordType);
    definedType.defineProperty("b", registry.getNativeType(
        JSTypeNative.NUMBER_TYPE), false, null);
    assertEquals(recordType, definedType);
  }

  /** Test that a record type cannot be assigned to a defined type with incompatible properties. */
  public void testCannotAssignToDefinedType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNativeType(
        JSTypeNative.STRING_TYPE)));
    RecordType recordType = new RecordType(registry, properties);

    ObjectType definedType = new ObjectType(registry);
    definedType.setPrototype(registry.getNativeObjectType(
        JSTypeNative.OBJECT_TYPE));
    definedType.defineProperty("a", registry.getNativeType(
        JSTypeNative.BOOLEAN_TYPE), false, null);
    assertFalse(recordType.isSubtype(definedType));
  }

  /** Test that a record type can be resolved. */
  public void testResolveRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNativeType(
        JSTypeNative.STRING_TYPE)));
    RecordType recordType = new RecordType(registry, properties);

    String propertyName = "a";
    JSType propertyType = recordType.getPropertyType(propertyName);
    assertNotNull(propertyType);
    assertEquals(registry.getNativeType(JSTypeNative.STRING_TYPE), propertyType);

    Node propertyNode = recordType.getPropertyNode(propertyName);
    assertNotNull(propertyNode);
  }
}