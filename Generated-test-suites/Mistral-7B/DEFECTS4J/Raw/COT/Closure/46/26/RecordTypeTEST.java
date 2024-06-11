import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordTypeTest {

  @org.junit.Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", registry.stringType()));
    properties.put("prop2", new RecordProperty("prop2", registry.numberType()));

    RecordType recordType = new RecordType(registry, properties);

    assertNotNull(recordType);
  }

  @org.junit.Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", registry.stringType()));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", new RecordProperty("prop1", registry.numberType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("prop1", new RecordProperty("prop1", registry.stringType()));
    properties3.put("prop2", new RecordProperty("prop2", registry.numberType()));
    RecordType recordType3 = new RecordType(registry, properties3);

    assertTrue(recordType1.isEquivalentTo(recordType1));
    assertTrue(recordType1.isEquivalentTo(recordType3));

    assertFalse(recordType1.isEquivalentTo(recordType2));
    assertFalse(recordType2.isEquivalentTo(recordType3));
  }

  @org.junit.Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", registry.stringType()));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", new RecordProperty("prop1", registry.numberType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("prop1", new RecordProperty("prop1", registry.numberType()));
    properties3.put("prop2", new RecordProperty("prop2", registry.numberType()));
    RecordType recordType3 = new RecordType(registry, properties3);

    Map<String, RecordProperty> properties4 = Maps.newHashMap();
    properties4.put("prop1", new RecordProperty("prop1", registry.stringType()));
    properties4.put("prop2", new RecordProperty("prop2", registry.booleanType()));
    RecordType recordType4 = new RecordType(registry, properties4);

    Map<String, RecordProperty> properties5 = Maps.newHashMap();
    properties5.put("prop1", new RecordProperty("prop1", registry.stringType()));
    RecordType recordType5 = new RecordType(registry, properties5);

    assertTrue(recordType1.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType3));
    assertTrue(recordType3.isSubtype(recordType1));
    assertTrue(recordType3.isSubtype(recordType3));

    assertFalse(recordType1.isSubtype(recordType2));
    assertFalse(recordType2.isSubtype(recordType1));
    assertFalse(recordType2.isSubtype(recordType3));

    assertTrue(recordType3.isSubtype(recordType4));
    assertFalse(recordType4.isSubtype(recordType3));

    assertFalse(recordType1.isSubtype(recordType5));
    assertFalse(recordType5.isSubtype(recordType1));
  }

  @org.junit.Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", registry.stringType()));
    RecordType recordType = new RecordType(registry, properties);

    ObjectType implicitPrototype = recordType.getImplicitPrototype();

    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
  }

  @org.junit.Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", registry.numberType()));
    RecordType recordType = new RecordType(registry, properties);

    assertFalse(recordType.defineProperty("prop2", registry.stringType(), false, null));
    assertFalse(recordType.hasProperty("prop2"));

    properties.put("prop2", new RecordProperty("prop2", registry.stringType()));
    properties = Maps.newHashMap(properties); // clear the previous map
    recordType = new RecordType(registry, properties);

    assertTrue(recordType.defineProperty("prop2", registry.stringType(), false, null));
    assertTrue(recordType.hasProperty("prop2"));
  }

  @org.junit.Test
  public void testGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", registry.stringType()));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", new RecordProperty("prop1", registry.numberType()));
    properties2.put("prop2", new RecordProperty("prop2", registry.numberType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    RecordType leastSupertype = recordType1.getLeastSupertype(recordType2);

    Map<String, RecordProperty> expectedProperties = Maps.newHashMap();
    expectedProperties.put("prop1", new RecordProperty("prop1", registry.numberType()));
    RecordType expectedRecordType = new RecordType(registry, expectedProperties);

    assertEquals(expectedRecordType, leastSupertype);
  }

  @org.junit.Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test with an empty record type
    RecordType emptyRecordType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).toMaybeRecordType();
    JSType greatestSubtype = emptyRecordType.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));

    assertEquals(emptyRecordType, greatestSubtype);

    // Test with a record type and an object type
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", registry.stringType()));
    RecordType recordType1 = new RecordType(registry, properties1);

    JSType greatestSubtype1 = recordType1.getGreatestSubtypeHelper(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE));

    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype1);

    // Test with a record type and a record type with the same properties
    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", new RecordProperty("prop1", registry.numberType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    JSType greatestSubtype2 = recordType1.getGreatestSubtypeHelper(recordType2);

    assertEquals(recordType1, greatestSubtype2);

    // Test with a record type and a record type with conflicting properties
    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("prop1", new RecordProperty("prop1", registry.numberType()));
    properties3.put("prop2", new RecordProperty("prop2", registry.booleanType()));
    RecordType recordType3 = new RecordType(registry, properties3);

    JSType greatestSubtype3 = recordType1.getGreatestSubtypeHelper(recordType3);

    assertEquals(registry.getNativeType(JSTypeNative.NO_TYPE), greatestSubtype3);

    // Test with a record type and a record type with a subproperty
    Map<String, RecordProperty> properties4 = Maps.newHashMap();
    properties4.put("prop1", new RecordProperty("prop1", registry.numberType()));
    properties4.put("prop2", new RecordProperty("prop2", registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).toMaybeRecordType()));
    RecordType recordType4 = new RecordType(registry, properties4);

    JSType greatestSubtype4 = recordType1.getGreatestSubtypeHelper(recordType4);

    assertEquals(recordType1, greatestSubtype4);
  }

  @org.junit.Test
  public void testToString() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", registry.stringType()));
    RecordType recordType = new RecordType(registry, properties);

    String toString = recordType.toString();

    String expectedString = "RecordType {prop1: String}";

    assertEquals(expectedString, toString);
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testNewRecordTypeWithNullRecordProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", null);
    new RecordType(registry, properties);
  }
}