import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testDefineProperty() {
    RecordType recordType = new RecordType(registry, new HashMap<>());
    boolean result = recordType.defineProperty("testProperty", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);
    assertTrue(result);
    assertEquals(registry.getNativeType(JSTypeNative.STRING_TYPE), recordType.getPropertyType("testProperty"));
  }

  @Test
  public void testIsEquivalentTo() {
    RecordType recordType1 = new RecordType(registry, new HashMap<>());
    RecordType recordType2 = new RecordType(registry, new HashMap<>());
    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsSubtype() {
    RecordType recordType1 = new RecordType(registry, new HashMap<>());
    RecordType recordType2 = new RecordType(registry, new HashMap<>());
    assertTrue(RecordType.isSubtype(recordType1, recordType2));
  }

}