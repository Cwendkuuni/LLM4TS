import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  private JSTypeRegistry registry;

  @org.junit.Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testRecordTypeWithSimpleProperties() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getBooleanType()));
    properties.put("b", new RecordProperty("b", registry.getIntType()));
    RecordType recordType = new RecordType(registry, properties, true);

    assertEquals("{a: boolean, b: integer}", recordType.toString());

    assertNull(recordType.getPropertyType("c"));
    assertEquals(registry.getBooleanType(), recordType.getPropertyType("a"));
    assertEquals(registry.getIntType(), recordType.getPropertyType("b"));

    Object record = recordType.newRecordInstance();
    assertNull(record.getProperty("c"));
    assertTrue(record.getProperty("a").isBoolean());
    assertTrue(record.getProperty("b").isNumber());
    assertEquals(0, record.getProperty("b").asNumber().intValue());
  }

  @Test
  public void testRecordTypeWithInferredProperties() {
    JSType stringType = registry.getStringType();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", stringType));

    RecordType recordType = new RecordType(registry, properties, false);

    assertEquals("{a: string}", recordType.toString());

    Object record = recordType.newRecordInstance();
    assertEquals("", record.getProperty("a").asString());
  }

  @Test
  public void testRecordTypeEquivalenceHelper() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getBooleanType()));
    properties1.put("b", new RecordProperty("b", registry.getIntType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getBooleanType()));
    properties2.put("b", new RecordProperty("b", registry.getIntType()));
    properties2.put("c", new RecordProperty("c", registry.getIntType()));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));
  }

  @Test
  public void testRecordTypeEquivalenceHelperWithUnknownProperties() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getBooleanType()));
    properties1.put("b", new RecordProperty("b", registry.getIntType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getBooleanType()));
    properties2.put("b", new RecordProperty("b", registry.getIntType()));
    properties2.put("c", new RecordProperty("c", registry.getBooleanType()));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, true));
  }

  @Test
  public void testRecordTypeSubtyping() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getIntType()));
    properties1.put("b", new RecordProperty("b", registry.getStringType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getIntType()));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);
    assertEquals(recordType1, greatestSubtype);

    properties2.put("b", new RecordProperty("b", registry.getIntType()));
    RecordType recordType3 = new RecordType(registry, properties2, true);
    greatestSubtype = recordType1.getGreatestSubtype(recordType3);
    assertEquals(recordType1, greatestSubtype);

    properties2.put("c", new RecordProperty("c", registry.getBooleanType()));
    RecordType recordType4 = new RecordType(registry, properties2, true);
    greatestSubtype = recordType1.getGreatestSubtype(recordType4);
    assertEquals(recordType1, greatestSubtype);

    properties2.put("b", new RecordProperty("b", registry.getStringType()));
    greatestSubtype = recordType1.getGreatestSubtype(recordType2);
    assertEquals(recordType2, greatestSubtype);
  }

  @Test
  public void testRecordTypeSubtypingWithEmptyRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    RecordType recordType1 = new RecordType(registry, properties, true);

    JSType greatestSubtype = recordType1.getGreatestSubtype(
        registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype);
  }

  @Test
  public void testRecordTypeFrozen() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));

    RecordType recordType = new RecordType(registry, properties, true);
    assertFalse(recordType.isFrozen());

    recordType.freeze();
    assertTrue(recordType.isFrozen());

    Map<String, RecordProperty> newProperties = Maps.newHashMap();
    newProperties.put("a", new RecordProperty("a", registry.getIntType()));
    newProperties.put("b", new RecordProperty("b", registry.getIntType()));

    RecordType newRecordType = new RecordType(registry, newProperties, true);
    assertEquals(recordType, newRecordType.getGreatestSubtype(newRecordType));
  }

  @Test
  public void testRecordTypeWithInferredAndDeclaredProperties() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    properties.put("b", new RecordProperty("b", registry.getBooleanType()));

    RecordType recordType = new RecordType(registry, properties, true);

    assertEquals("{a: integer, b: boolean}", recordType.toString());

    Object record = recordType.newRecordInstance();
    assertEquals(0, record.getProperty("a").asNumber().intValue());
    assertTrue(record.getProperty("b").isBoolean());

    Map<String, RecordProperty> newProperties = Maps.newHashMap();
    newProperties.put("a", new RecordProperty("a", registry.getIntType()));
    newProperties.put("b", new RecordProperty("b", registry.getStringType()));

    RecordType newRecordType = new RecordType(registry, newProperties, true);
    assertEquals(recordType, newRecordType.getGreatestSubtype(newRecordType));

    RecordType newRecordType2 = new RecordType(registry, newProperties, false);
    assertEquals(newRecordType2, recordType.getGreatestSubtype(newRecordType2));
  }

  @Test
  public void testRecordTypeWithCovariantProperties() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getBooleanType()));
    properties.put("b", new RecordProperty("b", registry.getIntType()));

    RecordType recordType = new RecordType(registry, properties, true);

    assertEquals("{a: boolean, b: integer}", recordType.toString());

    Object record = recordType.newRecordInstance();
    assertFalse(record.getProperty("a").isNumber());
    assertFalse(record.getProperty("a").isBoolean());
    assertFalse(record.getProperty("b").isBoolean());

    Map<String, RecordProperty> newProperties = Maps.newHashMap();
    newProperties.put("a", new RecordProperty("a", registry.getIntType()));
    newProperties.put("b", new RecordProperty("b", registry.getBooleanType()));

    RecordType newRecordType = new RecordType(registry, newProperties, true);
    assertFalse(recordType.isSubtype(newRecordType));

    newProperties.put("a", new RecordProperty("a", registry.getBooleanType()));

    RecordType newRecordType2 = new RecordType(registry, newProperties, true);
    assertTrue(recordType.isSubtype(newRecordType2));
  }
}