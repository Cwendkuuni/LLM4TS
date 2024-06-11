import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class RecordTypeTest {

  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, recordProperties);

    assertEquals("Expected record type name to match.", "RecordType", recordType.getClass().getName());
    assertEquals("Expected record type to have correct properties.", 2, recordType.getPropertyCount());

    JSType nameType = recordType.getPropertyType("name");
    JSType ageType = recordType.getPropertyType("age");

    assertEquals("Expected name property type to match.", registry.getStringType(), nameType);
    assertEquals("Expected age property type to match.", registry.getNumberType(), ageType);
  }

  @Test
  public void testRecordTypeIsSubtypeOfSelf() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();

    recordProperties.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, recordProperties);

    assertTrue("Expected record type to be a subtype of itself.", recordType.isSubtype(recordType));
  }

  @Test
  public void testRecordTypeIsSubtypeOfRecordWithSameProperties() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();

    recordProperties.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, recordProperties);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties2.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertTrue("Expected recordType1 to be a subtype of recordType2.", recordType1.isSubtype(recordType2));
  }

  @Test
  public void testRecordTypeIsNotSubtypeOfRecordWithFewerProperties() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();

    recordProperties.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, recordProperties);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", registry.getStringType()));

    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertFalse("Expected recordType1 not to be a subtype of recordType2.", recordType1.isSubtype(recordType2));
  }

  @Test
  public void testRecordTypeIsSubtypeOfRecordWithMoreProperties() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();

    recordProperties.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, recordProperties);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties2.put("age", new RecordProperty("age", registry.getNumberType()));
    recordProperties2.put("city", new RecordProperty("city", registry.getStringType()));

    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertTrue("Expected recordType1 to be a subtype of recordType2.", recordType1.isSubtype(recordType2));
  }

  @Test
  public void testRecordTypeIsSubtypeOfObjectType() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();

    recordProperties.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, recordProperties);

    assertTrue("Expected recordType to be a subtype of ObjectType.", recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRecordTypeFrozen() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();

    recordProperties.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, recordProperties);

    recordType.defineProperty("name", registry.getNumberType(), false, null);
  }

  @Test
  public void testRecordTypeGreatestSubtype() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties1.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties2.put("age", new RecordProperty("age", registry.getNumberType()));
    recordProperties2.put("city", new RecordProperty("city", registry.getStringType()));

    RecordType recordType2 = new RecordType(registry, recordProperties2);

    RecordType greatestSubtype = recordType1.getGreatestSubtype(recordType2);

    assertEquals("Expected recordType1 to be the greatest subtype of recordType2.", recordType1, greatestSubtype);
  }

  @Test
  public void testRecordTypeGreatestSubtypeWithDifferentPropertyTypes() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties1.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", registry.getBooleanType()));
    recordProperties2.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType greatestSubtype = recordType1.getGreatestSubtype(recordType2);

    assertNull("Expected null as the greatest subtype.", greatestSubtype);
  }

  @Test
  public void testRecordTypeGetGreatestSubtypeWithEmptyObjectType() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();

    recordProperties.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, recordProperties);

    RecordType greatestSubtype = recordType.getGreatestSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));

    assertEquals("Expected ObjectType to be the greatest subtype of the record type.", registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype);
  }
}