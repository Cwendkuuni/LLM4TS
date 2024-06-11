import static org.junit.Assert.*;

import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testIsSynthetic() {
    RecordType recordType = new RecordType(null, null);
    assertEquals(false, recordType.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType recordType1 = new RecordType(null, null);
    RecordType recordType2 = new RecordType(null, null);
    assertEquals(true, recordType1.checkRecordEquivalenceHelper(recordType2, null));
  }

  @Test
  public void testGetImplicitPrototype() {
    RecordType recordType = new RecordType(null, null);
    assertNotNull(recordType.getImplicitPrototype());
  }

  @Test
  public void testDefineProperty() {
    RecordType recordType = new RecordType(null, null);
    assertEquals(true, recordType.defineProperty("", null, false, null));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    RecordType recordType = new RecordType(null, null);
    JSType jSType = new JSType(null, null);
    assertNotNull(recordType.getGreatestSubtypeHelper(jSType));
  }

  @Test
  public void testToMaybeRecordType() {
    RecordType recordType = new RecordType(null, null);
    assertNotNull(recordType.toMaybeRecordType());
  }

  @Test
  public void testIsSubtype() {
    RecordType recordType = new RecordType(null, null);
    JSType jSType = new JSType(null, null);
    assertEquals(true, recordType.isSubtype(jSType));
  }

  @Test
  public void testIsSubtype_ObjectType_RecordType() {
    ObjectType objectType = new ObjectType(null, null, null);
    RecordType recordType = new RecordType(null, null);
    assertEquals(true, RecordType.isSubtype(objectType, recordType));
  }

}