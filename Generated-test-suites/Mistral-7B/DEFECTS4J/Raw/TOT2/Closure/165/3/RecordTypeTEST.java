import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;

public class RecordTypeTest {
  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, recordProperties);

    // Basic functionality test
    assertEquals("Expected record type object", recordType.getClass(), RecordType.class);

    // Edge case: Check if record type can be frozen
    RecordType frozenRecordType = new RecordType(registry, recordProperties);
    frozenRecordType.isFrozen = true;
    assertFalse("Expected record type to be unfrozen", frozenRecordType.isFrozen());

    // Exception handling: Check if defineProperty fails when record is frozen
    try {
      frozenRecordType.defineProperty("newProperty", Integer.class, false, null);
      fail("Expected defineProperty to fail when record is frozen");
    } catch (IllegalStateException e) {
      assertTrue("Expected IllegalStateException", true);
    }
  }
}