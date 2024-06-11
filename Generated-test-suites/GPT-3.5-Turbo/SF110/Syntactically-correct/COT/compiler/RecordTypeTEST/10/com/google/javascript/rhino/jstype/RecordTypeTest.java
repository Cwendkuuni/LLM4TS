import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

  @Test
  public void testIsSynthetic() {
    RecordType recordType = new RecordType(null, null, false);
    assertTrue(recordType.isSynthetic());
  }

  @Test
  public void testDefineProperty() {
    RecordType recordType = new RecordType(null, null);
    assertTrue(recordType.defineProperty("property", new JSType(), false, null));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    RecordType recordType1 = new RecordType(null, null);
    RecordType recordType2 = new RecordType(null, null);
    assertNotEquals(recordType1.getGreatestSubtypeHelper(recordType2), null);
  }

  @Test
  public void testIsSubtype() {
    RecordType recordType1 = new RecordType(null, null);
    RecordType recordType2 = new RecordType(null, null);
    assertFalse(recordType1.isSubtype(null));
    assertTrue(recordType1.isSubtype(recordType2));
  }

}