import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RecordTypeTest {

  private JSTypeRegistry registry;

  @org.junit.Before
  public void setUp() {
    this.registry = new JSTypeRegistry();
  }

  @Test
  public void testCreateRecordType() {
    // Test creating a record type with defined properties
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    properties.put("b", new RecordProperty("b", registry.getStringType()));
    RecordType recordType = new RecordType(registry, properties);

    assertEquals("{a: int, b: string}", recordType.toString());

    // Test creating a record type with inferred properties
    properties = Maps.newHashMap();
    RecordType inferredRecordType = new RecordType(registry, properties);
    assertNotNull(inferredRecordType.getProperty("a"));
    assertNotNull(inferredRecordType.getProperty("b"));
  }

  @Test(expected = IllegalStateException.class)
  public void testNullRecordProperty() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", null);
    new RecordType(registry, properties);
  }

  @Test
  public void testIsEquivalentTo() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getIntType()));
    properties1.put("b", new RecordProperty("b", registry.getStringType()));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getIntType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    assertEquals(recordType1, recordType2);

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("b", new RecordProperty("b", registry.getStringType()));
    properties3.put("a", new RecordProperty("a", registry.getIntType()));
    RecordType recordType3 = new RecordType(registry, properties3);

    assertFalse(recordType1.isEquivalentTo(recordType3));
  }

  @Test
  public void testGetImplicitPrototype() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    RecordType recordType = new RecordType(registry, properties);

    assertEquals(registry.getNativeObjectType(ObjectTypeNative.OBJECT_TYPE),
        recordType.getImplicitPrototype());
  }

  @Test
  public void testDefineProperty() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    RecordType recordType = new RecordType(registry, properties);

    recordType.defineProperty("b", registry.getStringType(), false, null);

    assertEquals("{a: int, b: string}", recordType.toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testFreezeRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    RecordType recordType = new RecordType(registry, properties);

    recordType.defineProperty("b", registry.getStringType(), false, null);

    recordType.defineProperty("b", registry.getStringType(), false, null);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getIntType()));
    properties1.put("b", new RecordProperty("b", registry.getStringType()));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getIntType()));
    properties2.put("c", new RecordProperty("c", registry.getStringType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    assertEquals(recordType1, recordType1.getGreatestSubtype(recordType2));

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("a", new RecordProperty("a", registry.getIntType()));
    properties3.put("b", new RecordProperty("b", registry.getStringType()));
    properties3.put("d", new RecordProperty("d", registry.getBooleanType()));
    RecordType recordType3 = new RecordType(registry, properties3);

    assertEquals(recordType1, recordType1.getGreatestSubtype(recordType3));
  }

  @Test
  public void testIsSubtype() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getIntType()));
    properties1.put("b", new RecordProperty("b", registry.getStringType()));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getIntType()));
    properties2.put("c", new RecordProperty("c", registry.getStringType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isSubtype(recordType2));

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("a", new RecordProperty("a", registry.getIntType()));
    properties3.put("d", new RecordProperty("d", registry.getBooleanType()));
    RecordType recordType3 = new RecordType(registry, properties3);

    assertFalse(recordType1.isSubtype(recordType3));
  }

  @Test
  public void testToString() {
    Map<String, RecordType.RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordType.RecordProperty("a", registry.getIntType()));
    properties.put("b", new RecordType.RecordProperty("b", registry.getStringType()));
    RecordType recordType = new RecordType(registry, properties);

    assertEquals("{a: int, b: string}", recordType.toString());
  }
}