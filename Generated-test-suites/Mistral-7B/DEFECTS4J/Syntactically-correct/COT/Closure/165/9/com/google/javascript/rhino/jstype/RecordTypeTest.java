import com.google.javascript.rhino.*;
import static com.google.javascript.rhino.Node.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("property1", new RecordProperty("property1", INT_TYPE));
    RecordType recordType = new RecordType(registry, recordProperties);

    // Basic functionality test
    assertFalse(recordType.isEquivalentTo(StringType.instance));
    assertEquals(INT_TYPE, recordType.getPropertyType("property1"));
    assertTrue(recordType.isSubtype(recordType));
    RecordType recordType2 = new RecordType(registry, recordProperties);
    assertTrue(recordType.isEquivalentTo(recordType2));

    // Edge cases and exception handling tests follow
  }

  @Test(expected = IllegalStateException.class)
  public void testNullRecordProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("property1", null);
    new RecordType(registry, recordProperties);
  }

  @Test
  public void testIsEquivalentToWithDifferentPropertyOrder() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("property2", new RecordProperty("property2", INT_TYPE));
    recordProperties1.put("property1", new RecordProperty("property1", STRING_TYPE));
    RecordType recordType1 = new RecordType(registry, recordProperties1);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("property1", new RecordProperty("property1", STRING_TYPE));
    recordProperties2.put("property2", new RecordProperty("property2", INT_TYPE));
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsEquivalentToWithEmptyMaps() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    RecordType recordType1 = new RecordType(registry, recordProperties1);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsSubtypeWithRecordTypeAndDifferentProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    recordPropertiesA.put("property1", new RecordProperty("property1", INT_TYPE));
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA);

    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
    recordPropertiesB.put("property1", new RecordProperty("property1", STRING_TYPE));
    recordPropertiesB.put("property2", new RecordProperty("property2", INT_TYPE));
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testIsSubtypeWithRecordTypeAndSameProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    recordPropertiesA.put("property1", new RecordProperty("property1", INT_TYPE));
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA);

    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
    recordPropertiesB.put("property1", new RecordProperty("property1", INT_TYPE));
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testGetGreatestSubtypeWithRecordTypeAndSameProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    recordPropertiesA.put("property1", new RecordProperty("property1", INT_TYPE));
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA);

    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
    recordPropertiesB.put("property1", new RecordProperty("property1", INT_TYPE));
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

    RecordType recordTypeC = recordTypeA.getGreatestSubtype(recordTypeB);
    assertEquals(recordTypeA, recordTypeC);
  }

  @Test
  public void testGetGreatestSubtypeWithRecordTypeAndDifferentProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    recordPropertiesA.put("property1", new RecordProperty("property1", INT_TYPE));
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA);

    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
    recordPropertiesB.put("property1", new RecordProperty("property1", STRING_TYPE));
    recordPropertiesB.put("property2", new RecordProperty("property2", INT_TYPE));
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

    RecordType recordTypeC = recordTypeA.getGreatestSubtype(recordTypeB);
    assertTrue(recordTypeC.isSubtype(recordTypeA));
    assertTrue(recordTypeC.isSubtype(recordTypeB));
  }

  @Test
  public void testGetGreatestSubtypeWithObjectType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    RecordType recordType = new RecordType(registry, recordProperties);

    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    RecordType greatestSubtype = recordType.getGreatestSubtypeHelper(objectType);

    assertEquals(objectType, greatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeWithNoType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    RecordType recordType = new RecordType(registry, recordProperties);

    JSType noType = registry.getNativeType(JSTypeNative.NO_TYPE);
    RecordType greatestSubtype = recordType.getGreatestSubtypeHelper(noType);

    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), greatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeWithNull() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    RecordType recordType = new RecordType(registry, recordProperties);

    RecordType greatestSubtype = recordType.getGreatestSubtypeHelper(null);

    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), greatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeWithRecordTypeAndNullProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    recordPropertiesA.put("property1", new RecordProperty("property1", INT_TYPE));
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA);

    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
    recordPropertiesB.put("property1", new RecordProperty("property1", INT_TYPE));
    recordPropertiesB.put(null, new RecordProperty("property2", INT_TYPE));
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

    RecordType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);

    assertEquals(recordTypeA, greatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeWithRecordTypeAndEmptyPropertiesMap() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA);

    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

    RecordType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);

    assertEquals(recordTypeA, greatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeWithRecordTypeAndUnknownPropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    recordPropertiesA.put("property1", new RecordProperty("property1", UNKNOWN_TYPE));
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA);

    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
    recordPropertiesB.put("property1", new RecordProperty("property1", INT_TYPE));
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

    RecordType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);

    assertEquals(recordTypeB, greatestSubtype);
  }
}