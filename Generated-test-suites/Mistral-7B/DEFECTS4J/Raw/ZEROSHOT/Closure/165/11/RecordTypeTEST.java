import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeFactory;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.prototype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.types.JSTypeNative;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordTypeTest {

  private ErrorReporter t;
  private JSTypeRegistry registry;

  @org.junit.Before
  public void setUp() {
    t = new ErrorReporter();
    registry = new JSTypeRegistry(JSTypeFactory.getInstance(), t);
  }

  // Test record creation with no properties
  @org.junit.Test
  public void testEmptyRecordType() {
    RecordType emptyRecord = new RecordType(registry, Collections.emptyMap());
    assertEquals(JSTypeNative.OBJECT_TYPE, emptyRecord.getImplicitPrototype());
    assertTrue(emptyRecord.isEquivalentTo(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
  }

  // Test record creation with properties
  @org.junit.Test
  public void testRecordTypeWithProperties() {
    Map<String, RecordProperty> recordProperties = new LinkedHashMap<>();
    recordProperties.put("property1", new RecordProperty("property1", registry.getNumberType()));
    recordProperties.put("property2", new RecordProperty("property2", registry.getBooleanType()));

    RecordType record = new RecordType(registry, recordProperties);
    assertEquals(recordProperties.size(), record.getDeclaredProperties().length);

    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), record.getImplicitPrototype());
    assertEquals(recordProperties, record.getPropertiesMap());

    assertFalse(record.isEquivalentTo(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));

    assertEquals(registry.getNativeType(JSTypeNative.NO_TYPE), record.getPropertyType("nonExistentProperty"));

    // Test freezing the record type.
    record.freeze();
    assertFalse(record.defineProperty("property3", registry.getNumberType(), false, null));
  }

  // Test subtyping
  @org.junit.Test
  public void testSubtyping() {
    Map<String, RecordProperty> parentRecordProperties = new LinkedHashMap<>();
    parentRecordProperties.put("property1", new RecordProperty("property1", registry.getNumberType()));

    RecordType parentRecord = new RecordType(registry, parentRecordProperties);

    Map<String, RecordProperty> childRecordProperties = new LinkedHashMap<>();
    childRecordProperties.putAll(parentRecordProperties);
    childRecordProperties.put("property2", new RecordProperty("property2", registry.getBooleanType()));

    RecordType childRecord = new RecordType(registry, childRecordProperties);

    // test that parent is a subtype of child
    assertTrue(parentRecord.isSubtype(childRecord));

    // test that child is a subtype of parent
    assertTrue(childRecord.isSubtype(parentRecord));

    // test that parent is a subtype of itself
    assertTrue(parentRecord.isSubtype(parentRecord));

    // test that parent is a subtype of an object type
    assertTrue(parentRecord.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));

    // test that a record is not a subtype of a primitive type
    assertFalse(parentRecord.isSubtype(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE)));
  }

  // Test record type with inferred properties.
  @org.junit.Test
  public void testRecordTypeWithInferredProperties() {
    Map<String, RecordProperty> recordProperties = new LinkedHashMap<>();
    recordProperties.put("property1", new RecordProperty("property1", registry.getNumberType()));

    RecordType record = new RecordType(registry, recordProperties);

    Map<String, RecordProperty> newRecordProperties = new LinkedHashMap<>();
    newRecordProperties.putAll(recordProperties);
    newRecordProperties.put("property2", new RecordProperty("property2", registry.getBooleanType()));

    RecordType newRecord = new RecordType(registry, newRecordProperties);

    // test that new record is a subtype of the old record
    assertTrue(record.isSubtype(newRecord));

    // test that the new record has an additional property
    assertEquals(newRecordProperties.size(), newRecord.getDeclaredProperties().length);
    assertFalse(record.isSubtype(newRecord));
  }

  // Test record type with a property that has a union type
  @org.junit.Test
  public void testRecordTypeWithUnionProperty() {
    Map<String, RecordProperty> recordProperties = new LinkedHashMap<>();
    recordProperties.put("property1", new RecordProperty("property1", registry.getUnionType(registry.getNumberType(), registry.getBooleanType())));

    RecordType record = new RecordType(registry, recordProperties);

    assertEquals(1, record.getDeclaredProperties().length);
    assertEquals("property1", record.getDeclaredProperties()[0].getName());
    assertTrue(record.getPropertyType("property1").isInstanceOf(UnionType.class));
  }

  // Test record type with a property that is a record type
  @org.junit.Test
  public void testRecordTypeWithRecordProperty() {
    Map<String, RecordProperty> parentRecordProperties = new LinkedHashMap<>();
    parentRecordProperties.put("property1", new RecordProperty("property1", registry.getNumberType()));

    RecordType parentRecord = new RecordType(registry, parentRecordProperties);

    Map<String, RecordProperty> childRecordProperties = new LinkedHashMap<>();
    childRecordProperties.put("property1", new RecordProperty("property1", registry.getNumberType()));
    childRecordProperties.put("childProperty", new RecordProperty("childProperty", new RecordType(registry, parentRecordProperties)));

    RecordType childRecord = new RecordType(registry, childRecordProperties);

    // test that parent record is a subtype of the child record's property
    RecordType childProperty = childRecord.getPropertyType("childProperty");
    assertTrue(parentRecord.isSubtype(childProperty));

    // test that the child record's property is a subtype of the parent record
    assertTrue(childProperty.isSubtype(parentRecord));
  }

  // Test record type with a property that is a prototype object
  @org.junit.Test
  public void testRecordTypeWithPrototypeProperty() {
    Map<String, RecordProperty> recordProperties = new LinkedHashMap<>();
    recordProperties.put("property1", new RecordProperty("property1", registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));

    RecordType record = new RecordType(registry, recordProperties);

    assertEquals(1, record.getDeclaredProperties().length);
    assertEquals("property1", record.getDeclaredProperties()[0].getName());
    assertTrue(record.getPropertyType("property1").isInstanceOf(PrototypeObjectType.class));
  }

  // Test record type with a property that is a function type
  @org.junit.Test
  public void testRecordTypeWithFunctionProperty() {
    Map<String, RecordProperty> recordProperties = new LinkedHashMap<>();
    recordProperties.put("property1", new RecordProperty("property1", registry.getFunctionType(registry.getNativeContext(), registry.getNativeFunctionType(JSTypeNative.VOID_FUNCTION_TYPE))));

    RecordType record = new RecordType(registry, recordProperties);

    assertEquals(1, record.getDeclaredProperties().length);
    assertEquals("property1", record.getDeclaredProperties()[0].getName());
    assertTrue(record.getPropertyType("property1").isInstanceOf(registry.getFunctionType(registry.getNativeContext(), registry.getNativeFunctionType(JSTypeNative.VOID_FUNCTION_TYPE)).getClass()));
  }

  // Test record type with a property that is a union of function types
  @org.junit.Test
  public void testRecordTypeWithFunctionUnionProperty() {
    Map<String, RecordProperty> recordProperties = new LinkedHashMap<>();
    recordProperties.put("property1", new RecordProperty("property1", registry.getUnionType(registry.getFunctionType(registry.getNativeContext(), registry.getNativeFunctionType(JSTypeNative.VOID_FUNCTION_TYPE)), registry.getFunctionType(registry.getNativeContext(), registry.getNativeFunctionType(JSTypeNative.NUMBER_FUNCTION_TYPE)))));

    RecordType record = new RecordType(registry, recordProperties);

    assertEquals(1, record.getDeclaredProperties().length);
    assertEquals("property1", record.getDeclaredProperties()[0].getName());
    assertTrue(record.getPropertyType("property1").isInstanceOf(registry.getUnionType(registry.getFunctionType(registry.getNativeContext(), registry.getNativeFunctionType(JSTypeNative.VOID_FUNCTION_TYPE)), registry.getFunctionType(registry.getNativeContext(), registry.getNativeFunctionType(JSTypeNative.NUMBER_FUNCTION_TYPE))).getClass()));
  }

  // Test record type with a property that is a union of record types
  @org.junit.Test
  public void testRecordTypeWithRecordUnionProperty() {
    Map<String, RecordProperty> parentRecordProperties1 = new LinkedHashMap<>();
    parentRecordProperties1.put("property1", new RecordProperty("property1", registry.getNumberType()));

    RecordType parentRecord1 = new RecordType(registry, parentRecordProperties1);

    Map<String, RecordProperty> parentRecordProperties2 = new LinkedHashMap<>();
    parentRecordProperties2.put("property2", new RecordProperty("property2", registry.getBooleanType()));

    RecordType parentRecord2 = new RecordType(registry, parentRecordProperties2);

    Map<String, RecordProperty> recordProperties = new LinkedHashMap<>();
    recordProperties.put("property1", new RecordProperty("property1", registry.getNumberType()));
    recordProperties.put("property2", new RecordProperty("property2", new RecordType(registry, parentRecordProperties1, parentRecordProperties2)));

    RecordType record = new RecordType(registry, recordProperties);

    assertEquals(1, record.getDeclaredProperties().length);
    assertEquals("property2", record.getDeclaredProperties()[0].getName());
    assertTrue(record.getPropertyType("property2").isInstanceOf(registry.getRecordType(registry, parentRecordProperties1, parentRecordProperties2).getClass()));
  }

  // Test record type with a property that is a union of primitive and record types
  @org.junit.Test
  public void testRecordTypeWithPrimitiveRecordUnionProperty() {
    Map<String, RecordProperty> parentRecordProperties1 = new LinkedHashMap<>();
    parentRecordProperties1.put("property1", new RecordProperty("property1", registry.getNumberType()));

    RecordType parentRecord1 = new RecordType(registry, parentRecordProperties1);

    Map<String, RecordProperty> parentRecordProperties2 = new LinkedHashMap<>();
    parentRecordProperties2.put("property2", new RecordProperty("property2", registry.getBooleanType()));

    RecordType parentRecord2 = new RecordType(registry, parentRecordProperties2);

    Map<String, RecordProperty> recordProperties = new LinkedHashMap<>();
    recordProperties.put("property1", new RecordProperty("property1", registry.getNumberType()));
    recordProperties.put("property2", new RecordProperty("property2", new UnionType(registry.getBooleanType(), new RecordType(registry, parentRecordProperties1))));

    RecordType record = new RecordType(registry, recordProperties);

    assertEquals(1, record.getDeclaredProperties().length);
    assertEquals("property2", record.getDeclaredProperties()[0].getName());
    assertTrue(record.getPropertyType("property2").isInstanceOf(UnionType.class));

    UnionType unionType = record.getPropertyType("property2");
    assertTrue(unionType.getAlternates().size() == 2);

    JSType booleanType = unionType.getAlternates().get(0);
    assertTrue(booleanType.isInstanceOf(registry.getBooleanType().getClass()));

    JSType parentRecordType = unionType.getAlternates().get(1);
    assertTrue(parentRecordType.isInstanceOf(parentRecord1.getClass()));
  }

  // Test record type with properties that have circular references
  @org.junit.Test
  public void testRecordTypeWithCircularProperties() {
    Map<String, RecordProperty> recordProperties = new LinkedHashMap<>();
    recordProperties.put("property1", new RecordProperty("property1", new RecordType(registry, recordProperties)));
    recordProperties.put("property2", new RecordProperty("property2", new RecordType(registry, recordProperties)));

    try {
      new RecordType(registry, recordProperties);
      fail();
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  // Test record type with a property that has a circular reference in its type
  @org.junit.Test
  public void testRecordTypeWithCircularPropertyType() {
    Map<String, RecordProperty> recordProperties = new LinkedHashMap<>();

    Map<String, RecordProperty> subRecordProperties = new LinkedHashMap<>();
    subRecordProperties.put("property1", new RecordProperty("property1", new RecordType(registry, recordProperties)));

    RecordType subRecord = new RecordType(registry, subRecordProperties);

    recordProperties.put("property1", new RecordProperty("property1", subRecord));

    try {
      new RecordType(registry, recordProperties);
      fail();
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  // Test record type with a property that has a missing property type
  @org.junit.Test
  public void testRecordTypeWithMissingPropertyType() {
    Map<String, RecordProperty> recordProperties = new LinkedHashMap<>();
    recordProperties.put("property1", new RecordProperty("property1", null));

    try {
      new RecordType(registry, recordProperties);
      fail();
    } catch (IllegalArgumentException e) {
      // expected
    }
  }
}