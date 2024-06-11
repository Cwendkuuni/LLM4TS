import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class RecordTypeTest {
  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", String.class));
    RecordType recordType = new RecordType(registry, recordProperties);

    assertEquals("RecordType", recordType.getClassName());
    assertEquals(1, recordType.getProperties().size());
    assertTrue(recordType.getProperties().containsKey("name"));
    assertEquals(String.class, recordType.getPropertyType("name"));
  }

  @Test
  public void testRecordTypeSubtyping() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", String.class));
    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", String.class));
    recordProperties2.put("age", new RecordProperty("age", Number.class));

    RecordType type1 = new RecordType(registry, recordProperties1);
    RecordType type2 = new RecordType(registry, recordProperties2);

    assertTrue(type1.isSubtype(type2)); // RecordType1 is a subtype of RecordType2
  }

  @Test
  public void testRecordTypeIsFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", String.class));
    RecordType recordType = new RecordType(registry, recordProperties);

    // Define a new property
    recordType.defineProperty("age", Integer.TYPE, false, null);

    // This should fail since the record type is frozen
    try {
      recordType.defineProperty("name", String.class, false, null);
      fail("should have thrown an IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }
}