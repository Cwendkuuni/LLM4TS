import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class RecordTypeTest {

  @Test
  public void testCreateRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSType.INT));
    RecordType recordType = new RecordType(registry, properties);
    assertEquals(JSType.RECORD, recordType.getClass());
    assertEquals(1, recordType.properties.size());
    assertNotNull(recordType.getPropertyType("a"));
    assertNull(recordType.getPropertyNode("a"));
  }

  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", JSType.INT));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", JSType.INT));
    properties2.put("b", new RecordProperty("b", JSType.INT));
    RecordType recordType2 = new RecordType(registry, properties2);

    RecordType recordType3 = new RecordType(registry, properties1);

    assertTrue(recordType1.isEquivalentTo(recordType3));
    assertTrue(recordType1.isEquivalentTo(recordType2));
    assertFalse(recordType1.isEquivalentTo(new Object()));
  }

  @Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSType.INT));
    RecordType recordType = new RecordType(registry, properties);
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        recordType.getImplicitPrototype());
  }

  @Test(expected = IllegalStateException.class)
  public void testDefineProperty_Frozen() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSType.INT));
    RecordType recordType = new RecordType(registry, properties);
    recordType.defineProperty("a", JSType.INT, true, null);
    recordType.defineProperty("a", JSType.INT, false, null);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordType1Properties = Maps.newHashMap();
    recordType1Properties.put("a", new RecordProperty("a", JSType.INT));
    Map<String, RecordProperty> recordType2Properties = Maps.newHashMap();
    recordType2Properties.put("a", new RecordProperty("a", JSType.INT));
    recordType2Properties.put("b", new RecordProperty("b", JSType.STRING));

    RecordType recordType1 = new RecordType(registry, recordType1Properties);
    RecordType recordType2 = new RecordType(registry, recordType2Properties);

    RecordType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertEquals(JSType.RECORD, greatestSubtype.getClass());
    assertEquals(2, greatestSubtype.properties.size());
    assertTrue(greatestSubtype.hasProperty("a"));
    assertTrue(greatestSubtype.hasProperty("b"));
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordType1Properties = Maps.newHashMap();
    recordType1Properties.put("a", new RecordProperty("a", JSType.INT));
    RecordType recordType1 = new RecordType(registry, recordType1Properties);

    Map<String, RecordProperty> recordType2Properties = Maps.newHashMap();
    recordType2Properties.put("a", new RecordProperty("a", JSType.INT));
    recordType2Properties.put("b", new RecordProperty("b", JSType.INT));
    RecordType recordType2 = new RecordType(registry, recordType2Properties);

    Map<String, RecordProperty> recordType3Properties = Maps.newHashMap();
    recordType3Properties.put("a", new RecordProperty("a", JSType.INT));
    recordType3Properties.put("b", new RecordProperty("b", JSType.STRING));
    RecordType recordType3 = new RecordType(registry, recordType3Properties);

    Map<String, RecordProperty> recordType4Properties = Maps.newHashMap();
    recordType4Properties.put("a", new RecordProperty("a", JSType.STRING));
    RecordType recordType4 = new RecordType(registry, recordType4Properties);

    // RecordType is a subtype of RecordType
    assertTrue(recordType1.isSubtype(recordType1));
    // RecordType1 is a subtype of RecordType2
    assertTrue(recordType1.isSubtype(recordType2));
    // RecordType2 is not a subtype of RecordType1
    assertFalse(recordType2.isSubtype(recordType1));
    // RecordType3 is not a subtype of RecordType1
    assertFalse(recordType3.isSubtype(recordType1));
    // RecordType1 is a subtype of RecordType3
    assertTrue(recordType1.isSubtype(recordType3));
    // RecordType3 is not a subtype of RecordType4
    assertFalse(recordType3.isSubtype(recordType4));
  }
}