import static org.junit.Assert.*;
import org.junit.Test;

import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.errors.TypeError;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    properties.put("prop2", new RecordProperty("prop2", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties);

    assertEquals(recordType.getClass(), RecordType.class);
    assertEquals(recordType.getPrototype(), registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    assertEquals(recordType.getName(), "RecordType{prop1: string, prop2: number}");
    assertEquals(recordType.isRecordType(), true);
  }

  @Test
  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    properties1.put("prop2", new RecordProperty("prop2", registry.getNumberType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    properties2.put("prop2", new RecordProperty("prop2", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeFailedCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", null);

    new RecordType(registry, properties);
  }

  @Test
  public void testRecordTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", registry.getStringType()));

    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    properties2.put("prop2", new RecordProperty("prop2", registry.getNumberType()));

    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testRecordTypeNotSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", registry.getStringType()));

    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", new RecordProperty("prop1", registry.getBooleanType()));

    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testRecordTypeLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    properties1.put("prop2", new RecordProperty("prop2", registry.getNumberType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    properties2.put("prop3", new RecordProperty("prop3", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    JSType leastSupertype = recordType1.getLeastSupertype(recordType2);

    assertEquals(leastSupertype.getClass(), RecordType.class);
    assertEquals(leastSupertype.getName(), "RecordType{prop1: string, prop2: number, prop3: number}");
  }

  @Test
  public void testRecordTypeGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    properties1.put("prop2", new RecordProperty("prop2", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop2", new RecordProperty("prop2", registry.getNumberType()));
    properties2.put("prop3", new RecordProperty("prop3", registry.getBooleanType()));

    RecordType recordType2 = new RecordType(registry, properties2);

    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);

    assertEquals(greatestSubtype.getClass(), UnionType.class);
    assertEquals(greatestSubtype.getName(), "UnionType{RecordType{prop1: string, prop2: number}, Object}");
  }

  @Test(expected = TypeError.class)
  public void testRecordTypeGreatestSubtypeError() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", registry.getStringType()));
    properties1.put("prop2", new RecordProperty("prop2", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", new RecordProperty("prop1", registry.getBooleanType()));
    properties2.put("prop2", new RecordProperty("prop2", registry.getNumberType()));

    RecordType recordType2 = new RecordType(registry, properties2);

    recordType1.getGreatestSubtype(recordType2);
  }
}