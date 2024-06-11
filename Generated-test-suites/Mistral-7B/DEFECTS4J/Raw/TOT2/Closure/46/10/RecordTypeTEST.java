import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeTest {
  @Test
  public void testCreateRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, properties);

    // Basic functionality test
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        recordType.getImplicitPrototype());
    assertFalse(recordType.isFrozen());

    // Test adding property
    recordType.defineProperty("gender", String.class, false, null);
    assertTrue(recordType.hasProperty("gender"));
    assertEquals(String.class, recordType.getPropertyType("gender"));

    // Test getLeastSupertype method with another record type
    JSTypeRegistry registry2 = new JSTypeRegistry();
    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", String.class));
    properties2.put("age", new RecordProperty("age", Double.class));

    RecordType recordType2 = new RecordType(registry2, properties2);

    JSType leastSupertype = recordType.getLeastSupertype(recordType2);
    assertTrue(leastSupertype.isRecordType());
    RecordType leastSupertypeRecord = leastSupertype.toMaybeRecordType();
    assertEquals(recordType.properties.size() + recordType2.properties.size(),
        leastSupertypeRecord.properties.size());
    for (String property : recordType.properties.keySet()) {
      assertTrue(leastSupertypeRecord.hasProperty(property));
      assertEquals(recordType.getPropertyType(property),
          leastSupertypeRecord.getPropertyType(property));
    }
    for (String property : recordType2.properties.keySet()) {
      assertTrue(leastSupertypeRecord.hasProperty(property));
      assertEquals(recordType2.getPropertyType(property),
          leastSupertypeRecord.getPropertyType(property));
    }

    // Test getGreatestSubtype method with another record type
    JSType greatestSubtype = recordType.getGreatestSubtype(recordType2);
    assertFalse(greatestSubtype.isRecordType());
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), greatestSubtype);

    // Test isSubtype method
    RecordType recordType3 = new RecordType(registry, properties);
    assertFalse(recordType.isSubtype(recordType3));
    RecordType recordType4 = new RecordType(registry, properties2);
    assertFalse(recordType.isSubtype(recordType4));
    RecordType recordType5 = new RecordType(registry, properties);
    assertTrue(recordType.isSubtype(recordType5));

    // Test isEquivalentTo method
    RecordType recordType6 = new RecordType(registry, properties);
    assertTrue(recordType.isEquivalentTo(recordType6));
  }
}