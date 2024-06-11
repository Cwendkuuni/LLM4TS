import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  private static JSTypeRegistry registry;

  @SuppressWarnings("unchecked")
  @BeforeClass
  public static void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testRecordTypeCreationWithDeclaredProperties() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, properties, true);

    assertEquals("{name: String, age: Integer}", recordType.toString());
    assertTrue(recordType.hasProperty("name"));
    assertTrue(recordType.hasProperty("age"));
    assertEquals(String.class, recordType.getPropertyType("name"));
    assertEquals(Integer.class, recordType.getPropertyType("age"));
  }

  @Test
  public void testRecordTypeCreationWithInferredProperties() {
    RecordType recordType = new RecordType(registry, Maps.newHashMap(), false);

    recordType.defineProperty("name", String.class, false, null);
    recordType.defineProperty("age", Integer.class, false, null);

    assertEquals("{name: String, age: Integer}", recordType.toString());
    assertTrue(recordType.hasProperty("name"));
    assertTrue(recordType.hasProperty("age"));
    assertEquals(String.class, recordType.getPropertyType("name"));
    assertEquals(Integer.class, recordType.getPropertyType("age"));
  }

  @Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> property1 = Maps.newHashMap();
    property1.put("name", new RecordProperty("name", String.class));
    property1.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> property2 = Maps.newHashMap();
    property2.put("name", new RecordProperty("name", String.class));
    property2.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType1 = new RecordType(registry, property1, true);
    RecordType recordType2 = new RecordType(registry, property2, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));
  }

  @Test
  public void testRecordTypeEquivalenceWithUnknownProperties() {
    Map<String, RecordProperty> property1 = Maps.newHashMap();
    property1.put("name", new RecordProperty("name", String.class));
    property1.put("age", new RecordProperty("age", Integer.class));
    property1.put("city", new RecordProperty("city", Object.class));

    Map<String, RecordProperty> property2 = Maps.newHashMap();
    property2.put("name", new RecordProperty("name", String.class));
    property2.put("age", new RecordProperty("age", Integer.class));
    property2.put("city", new RecordProperty("city", Object.class));

    RecordType recordType1 = new RecordType(registry, property1, true);
    RecordType recordType2 = new RecordType(registry, property2, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));
  }

  @Test
  public void testRecordTypeWithIncompatiblePropertyTypes() {
    Map<String, RecordProperty> property1 = Maps.newHashMap();
    property1.put("name", new RecordProperty("name", String.class));
    property1.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> property2 = Maps.newHashMap();
    property2.put("name", new RecordProperty("name", Object.class));
    property2.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType1 = new RecordType(registry, property1, true);
    RecordType recordType2 = new RecordType(registry, property2, true);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, false));
  }

  @Test
  public void testRecordTypeSubtyping() {
    Map<String, RecordProperty> property1 = Maps.newHashMap();
    property1.put("name", new RecordProperty("name", String.class));
    property1.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> property2 = Maps.newHashMap();
    property2.put("name", new RecordProperty("name", String.class));
    property2.put("age", new RecordProperty("age", Number.class));

    RecordType recordType1 = new RecordType(registry, property1, true);
    RecordType recordType2 = new RecordType(registry, property2, true);

    JSType subtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertNotNull(subtype);
    assertEquals(recordType2, subtype);
  }

  @Test
  public void testRecordTypeSubtypingWithIncompatiblePropertyTypes() {
    Map<String, RecordProperty> property1 = Maps.newHashMap();
    property1.put("name", new RecordProperty("name", String.class));
    property1.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> property2 = Maps.newHashMap();
    property2.put("name", new RecordProperty("name", Object.class));
    property2.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType1 = new RecordType(registry, property1, true);
    RecordType recordType2 = new RecordType(registry, property2, true);

    JSType subtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertNotNull(subtype);
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), subtype);
  }

  @Test
  public void testRecordTypeSubtypingWithNullPropertyType() {
    Map<String, RecordProperty> property1 = Maps.newHashMap();
    property1.put("name", new RecordProperty("name", String.class));
    property1.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> property2 = Maps.newHashMap();
    property2.put("name", new RecordProperty("name", null));
    property2.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType1 = new RecordType(registry, property1, true);
    RecordType recordType2 = new RecordType(registry, property2, true);

    JSType subtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertNotNull(subtype);
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), subtype);
  }
}