import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.TypeRegistry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    TypeRegistry registry = new TypeRegistry();
    RecordProperty prop1 = new RecordProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE));
    RecordProperty prop2 = new RecordProperty("age", registry.getNativeType(JSTypeNative.NUMBER_TYPE));

    Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
    properties.put("name", prop1);
    properties.put("age", prop2);

    RecordType recordType = new RecordType(registry, properties);

    assertEquals("{name: string, age: number}", recordType.toString());
  }

  @Test
  public void testRecordTypeEquivalence() {
    TypeRegistry registry = new TypeRegistry();
    RecordProperty prop1 = new RecordProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE));
    RecordProperty prop2 = new RecordProperty("age", registry.getNativeType(JSTypeNative.NUMBER_TYPE));

    Map<String, RecordProperty> properties1 = Maps.newLinkedHashMap();
    properties1.put("name", prop1);
    properties1.put("age", prop2);

    Map<String, RecordProperty> properties2 = Maps.newLinkedHashMap();
    properties2.put("name", prop1);
    properties2.put("age", prop2);

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertEquals(recordType1, recordType2);
  }

  @Test
  public void testRecordTypeSubtyping() {
    TypeRegistry registry = new TypeRegistry();

    RecordProperty prop1 = new RecordProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE));
    RecordProperty prop2 = new RecordProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE));
    RecordProperty prop3 = new RecordProperty("age", registry.getNativeType(JSTypeNative.NUMBER_TYPE));

    Map<String, RecordProperty> properties1 = Maps.newLinkedHashMap();
    properties1.put("name", prop1);
    properties1.put("age", prop3);

    Map<String, RecordProperty> properties2 = Maps.newLinkedHashMap();
    properties2.put("name", prop1);
    properties2.put("name", prop2);
    properties2.put("age", prop3);

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertEquals(recordType1, recordType2.getGreatestSubtype(recordType1));
  }

  @Test
  public void testRecordTypeInheritance() {
    TypeRegistry registry = new TypeRegistry();

    RecordProperty prop1 = new RecordProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE));
    RecordProperty prop2 = new RecordProperty("age", registry.getNativeType(JSTypeNative.NUMBER_TYPE));
    RecordProperty prop3 = new RecordProperty("location", registry.getNativeType(JSTypeNative.STRING_TYPE));

    Map<String, RecordProperty> properties1 = Maps.newLinkedHashMap();
    properties1.put("name", prop1);
    properties1.put("age", prop2);

    Map<String, RecordProperty> properties2 = Maps.newLinkedHashMap();
    properties2.putAll(properties1);
    properties2.put("location", prop3);

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertEquals(recordType1, recordType1.getGreatestSubtype(recordType2));
  }

  @Test
  public void testRecordTypeFrozen() {
    TypeRegistry registry = new TypeRegistry();

    RecordProperty prop1 = new RecordProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE));
    RecordProperty prop2 = new RecordProperty("age", registry.getNativeType(JSTypeNative.NUMBER_TYPE));

    Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
    properties.put("name", prop1);
    properties.put("age", prop2);

    RecordType recordType = new RecordType(registry, properties, false);

    // Try to add a property
    recordType.defineProperty("newProp", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);
    assertEquals(recordType.getProperties().size(), 2);

    // Try to delete a property
    recordType.deleteProperty("name");
    assertEquals(recordType.getProperties().size(), 1);

    // Try to redefine a property
    recordType.defineProperty("age", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), false, null);
    assertEquals(recordType.getPropertyType("age"), registry.getNativeType(JSTypeNative.BOOLEAN_TYPE));

    // Try to freeze the record type
    recordType.freeze();

    // Try to add a property
    recordType.defineProperty("newProp", registry.getNativeType(JSTYPE_STRING), false, null);
    assertEquals(recordType.getProperties().size(), 2);

    // Try to delete a property
    recordType.deleteProperty("name");
    assertEquals(recordType.getProperties().size(), 2);

    // Try to redefine a property
    recordType.defineProperty("age", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);
    assertEquals(recordType.getPropertyType("age"), registry.getNativeType(JSTypeNative.NUMBER_TYPE));
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeNullProperty() {
    TypeRegistry registry = new TypeRegistry();

    RecordProperty prop1 = new RecordProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE));
    RecordProperty prop2 = null;
    RecordProperty prop3 = new RecordProperty("age", registry.getNativeType(JSTypeNative.NUMBER_TYPE));

    Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
    properties.put("name", prop1);
    properties.put("age", prop3);

    try {
      new RecordType(registry, properties);
    } catch (IllegalStateException e) {
      assertEquals("RecordProperty associated with a property should not be null!", e.getMessage());
      throw e;
    }
  }
}