import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, properties);
    JSType expected = registry.getRecordType("{name: String, age: Number}");
    assertEquals(expected, recordType);
  }

  @Test
  public void testIsEquivalentTo() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", String.class));
    properties1.put("age", new RecordProperty("age", Number.class));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", String.class));
    properties2.put("age", new RecordProperty("age", Number.class));
    properties2.put("city", new RecordProperty("city", String.class));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isEquivalentTo(recordType1));
    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsNotEquivalentToDifferentProperties() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", String.class));
    properties1.put("age", new RecordProperty("age", Number.class));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", String.class));
    properties2.put("city", new RecordProperty("city", String.class));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsNotEquivalentToDifferentPropertyTypes() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", String.class));
    properties1.put("age", new RecordProperty("age", Number.class));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", Boolean.class));
    properties2.put("age", new RecordProperty("age", Number.class));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsSubtypeOfSameType() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", String.class));
    properties1.put("age", new RecordProperty("age", Number.class));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", String.class));
    properties2.put("age", new RecordProperty("age", Number.class));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isSubtypeOf(recordType1));
    assertTrue(recordType1.isSubtypeOf(recordType2));
    assertTrue(recordType2.isSubtypeOf(recordType1));
    assertTrue(recordType2.isSubtypeOf(recordType2));
  }

  @Test
  public void testIsSubtypeOfSubType() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", String.class));
    properties1.put("age", new RecordProperty("age", Number.class));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", String.class));
    properties2.put("age", new RecordProperty("age", Number.class));
    properties2.put("city", new RecordProperty("city", String.class));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isSubtypeOf(recordType2));
  }

  @Test
  public void testIsSubtypeOfDifferentTypes() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", String.class));
    properties1.put("age", new RecordProperty("age", Number.class));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", Boolean.class));
    properties2.put("age", new RecordProperty("age", Number.class));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.isSubtypeOf(recordType2));
  }

  @Test
  public void testGetLeastSupertype() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", String.class));
    properties1.put("age", new RecordProperty("age", Number.class));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", String.class));
    properties2.put("age", new RecordProperty("age", Number.class));
    properties2.put("city", new RecordProperty("city", String.class));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    RecordType leastSupertype = recordType1.getLeastSupertype(recordType2);
    RecordType expected = new RecordType(registry, Maps.newHashMap());
    expected.defineProperty("name", String.class, false, null);
    expected.defineProperty("age", Number.class, false, null);

    assertEquals(expected, leastSupertype);
  }

  @Test
  public void testGetGreatestSubtype() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", String.class));
    properties1.put("age", new RecordProperty("age", Number.class));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", String.class));
    properties2.put("age", new RecordProperty("age", Number.class));
    properties2.put("city", new RecordProperty("city", String.class));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    RecordType greatestSubtype = recordType2.getGreatestSubtype(recordType1);
    RecordType expected = new RecordType(registry, properties1);

    assertEquals(expected, greatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeDifferentTypes() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", String.class));
    properties1.put("age", new RecordProperty("age", Number.class));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", Boolean.class));
    properties2.put("age", new RecordProperty("age", Number.class));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), greatestSubtype);
  }
}