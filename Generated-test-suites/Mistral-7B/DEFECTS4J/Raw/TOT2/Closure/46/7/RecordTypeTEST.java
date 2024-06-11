import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.UnionType;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, recordProperties);
    assertEquals(recordType.getClass(), RecordType.class);
    assertEquals(recordType.getPrettyPrint(), true);

    // Basic functionality test
    assertEquals(recordType.isEquivalentTo(recordType), true);

    // Test with empty map
    Map<String, RecordProperty> emptyProperties = Maps.newHashMap();
    RecordType emptyRecordType = new RecordType(registry, emptyProperties);
    assertFalse(recordType.isEquivalentTo(emptyRecordType));

    // Test with different property types
    Map<String, RecordProperty> differentProperties = Maps.newHashMap();
    differentProperties.put("name", new RecordProperty("name", String.class));
    differentProperties.put("age", new RecordProperty("age", Double.class));
    RecordType differentRecordType = new RecordType(registry, differentProperties);
    assertFalse(recordType.isEquivalentTo(differentRecordType));

    // Test with subtyping
    Map<String, RecordProperty> subtypeProperties = Maps.newHashMap();
    subtypeProperties.put("name", new RecordProperty("name", String.class));
    subtypeProperties.put("age", new RecordProperty("age", Integer.class));
    subtypeProperties.put("gender", new RecordProperty("gender", String.class));
    RecordType subtypeRecordType = new RecordType(registry, subtypeProperties);
    assertTrue(recordType.isSubtype(subtypeRecordType));

    // Test with greatest subtype
    RecordType greatestSubtype = recordType.getGreatestSubtype(subtypeRecordType);
    assertEquals(greestSubtype.getClass(), RecordType.class);
    assertEquals(greestSubtype.getPrettyPrint(), true);
    assertTrue(recordType.isEquivalentTo(greestSubtype));

    // Test with intersection
    RecordType intersectionType = recordType.intersect(subtypeRecordType);
    assertEquals(intersectionType.getClass(), RecordType.class);
    assertEquals(intersectionType.getPrettyPrint(), true);
    assertTrue(intersectionType.isEquivalentTo(recordType));
    assertTrue(intersectionType.isEquivalentTo(subtypeRecordType));

    // Test with union
    RecordType unionType = recordType.union(subtypeRecordType);
    assertEquals(unionType.getClass(), UnionType.class);
    assertTrue(unionType.contains(recordType));
    assertTrue(unionType.contains(subtypeRecordType));
  }
}