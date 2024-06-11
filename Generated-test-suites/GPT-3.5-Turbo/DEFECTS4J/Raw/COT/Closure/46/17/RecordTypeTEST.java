import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType1 = new RecordType(registry, new HashMap<String, RecordProperty>());
    RecordType recordType2 = new RecordType(registry, new HashMap<String, RecordProperty>());
    
    boolean result = recordType1.isEquivalentTo(recordType2);
    assertTrue(result);
  }

  @Test
  public void testGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType1 = new RecordType(registry, new HashMap<String, RecordProperty>());
    RecordType recordType2 = new RecordType(registry, new HashMap<String, RecordProperty>());
    
    JSType result = recordType1.getLeastSupertype(recordType2);
    assertEquals(recordType1, result);
  }

  @Test
  public void testGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType1 = new RecordType(registry, new HashMap<String, RecordProperty>());
    RecordType recordType2 = new RecordType(registry, new HashMap<String, RecordProperty>());
    
    JSType result = recordType1.getGreatestSubtype(recordType2);
    assertEquals(recordType1, result);
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType1 = new RecordType(registry, new HashMap<String, RecordProperty>());
    RecordType recordType2 = new RecordType(registry, new HashMap<String, RecordProperty>());
    
    boolean result = recordType1.isSubtype(recordType2);
    assertTrue(result);
  }

  // Additional test cases for potential edge cases and exception handling scenarios

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeConstructorNullProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType1 = new RecordType(registry, null);
  }

  @Test
  public void testRecordTypeDefinePropertyFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType1 = new RecordType(registry, new HashMap<String, RecordProperty>());
    boolean result = recordType1.defineProperty("property", registry.getNativeType(JSTypeNative.UNKNOWN_TYPE), false, null);
    assertFalse(result);
  }

}