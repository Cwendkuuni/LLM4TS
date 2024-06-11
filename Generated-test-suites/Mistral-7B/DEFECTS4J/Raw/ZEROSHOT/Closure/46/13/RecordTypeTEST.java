import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSTypeFactory.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordProperty;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", intType()));
    properties.put("prop2", new RecordProperty("prop2", stringType()));

    RecordType recordType = new RecordType(registry, properties);
    assertEquals("RecordType", recordType.getClass().getSimpleName());
    assertEquals(2, recordType.getProperties().size());

    assertEquals(intType(), recordType.getPropertyType("prop1"));
    assertEquals(stringType(), recordType.getPropertyType("prop2"));
  }

  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", intType()));
    properties1.put("prop2", new RecordProperty("prop2", stringType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", new RecordProperty("prop1", intType()));
    properties2.put("prop2", new RecordProperty("prop2", stringType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsEquivalentToDifferentOrder() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop2", new RecordProperty("prop2", intType()));
    properties1.put("prop1", new RecordProperty("prop1", stringType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", new RecordProperty("prop1", stringType()));
    properties2.put("prop2", new RecordProperty("prop2", intType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsEquivalentToDifferentProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", intType()));
    properties1.put("prop2", new RecordProperty("prop2", stringType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", new RecordProperty("prop1", stringType()));
    properties2.put("prop3", new RecordProperty("prop3", intType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", intType()));
    properties1.put("prop2", new RecordProperty("prop2", stringType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", new RecordProperty("prop1", intType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testIsNotSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", intType()));
    properties1.put("prop2", new RecordProperty("prop2", stringType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", new RecordProperty("prop1", stringType()));
    properties2.put("prop3", new RecordProperty("prop3", intType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType2.isSubtype(recordType1));
  }

  @Test
  public void testGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", intType()));
    properties1.put("prop2", new RecordProperty("prop2", stringType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", new RecordProperty("prop1", intType()));
    properties2.put("prop3", new RecordProperty("prop3", stringType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    RecordType leastSupertype = recordType1.getLeastSupertype(recordType2);

    assertEquals(recordType1.getClass(), leastSupertype.getClass());
    assertEquals(3, leastSupertype.getProperties().size());
    assertEquals(intType(), leastSupertype.getPropertyType("prop1"));
    assertEquals(stringType(), leastSupertype.getPropertyType("prop2"));
    assertEquals(stringType(), leastSupertype.getPropertyType("prop3"));
  }

  @Test
  public void testGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", intType()));
    properties1.put("prop2", new RecordProperty("prop2", stringType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", new RecordProperty("prop1", intType()));
    properties2.put("prop3", new RecordProperty("prop3", intType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    RecordType greatestSubtype = recordType2.getGreatestSubtype(recordType1);

    assertEquals(recordType2.getClass(), greatestSubtype.getClass());
    assertEquals(2, greatestSubtype.getProperties().size());
    assertEquals(intType(), greatestSubtype.getPropertyType("prop1"));
    assertEquals(intType(), greatestSubtype.getPropertyType("prop3"));
  }

  @Test
  public void testGetGreatestSubtypeEmptyType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", intType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    JSType emptyType = registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE);

    RecordType greatestSubtype = recordType1.getGreatestSubtype(emptyType);

    assertEquals(recordType1.getClass(), greatestSubtype.getClass());
    assertEquals(1, greatestSubtype.getProperties().size());
    assertEquals(intType(), greatestSubtype.getPropertyType("prop1"));
  }

  @Test
  public void testGetGreatestSubtypeNoType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", intType()));

    RecordType recordType = new RecordType(registry, properties);

    try {
      recordType.getGreatestSubtype(null);
      fail("Should have thrown an NPE");
    } catch (NullPointerException e) {
      // Expected
    }
  }
}