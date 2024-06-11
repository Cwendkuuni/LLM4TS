import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordProperty prop1 = new RecordProperty("prop1", JSType.NUMBER, null);
    RecordProperty prop2 = new RecordProperty("prop2", JSType.STRING, null);
    RecordType record1 = new RecordType(null, Map.of("prop1", prop1, "prop2", prop2), true);
    RecordType record2 = new RecordType(null, Map.of("prop1", prop1, "prop2", prop2), true);
    
    assertTrue(record1.checkRecordEquivalenceHelper(record2, false));
  }

  @Test
  public void testDefineProperty() {
    RecordProperty prop1 = new RecordProperty("prop1", JSType.NUMBER, null);
    RecordType record = new RecordType(null, Map.of("prop1", prop1), true);
    
    assertFalse(record.defineProperty("prop1", JSType.STRING, false, null));
  }

  @Test
  public void testIsSubtype() {
    RecordProperty prop1 = new RecordProperty("prop1", JSType.NUMBER, null);
    RecordType record1 = new RecordType(null, Map.of("prop1", prop1), true);
    RecordProperty prop2 = new RecordProperty("prop2", JSType.STRING, null);
    RecordType record2 = new RecordType(null, Map.of("prop1", prop1, "prop2", prop2), true);
    
    assertTrue(RecordType.isSubtype(record2, record1));
  }

  @Test
  public void testResolveInternal() {
    RecordProperty prop1 = new RecordProperty("prop1", JSType.NUMBER, null);
    RecordType record = new RecordType(null, Map.of("prop1", prop1), true);
    
    assertEquals(record.resolveInternal(null, null), record);
  }
}