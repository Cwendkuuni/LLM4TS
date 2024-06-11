import org.junit.Assert;
import org.junit.Test;

public class RecordTypeTest {
  
  @Test
  public void testIsSynthetic() {
    RecordType recordType = new RecordType(null);
    boolean isSynthetic = recordType.isSynthetic();
    Assert.assertEquals(false, isSynthetic);
  }
  
  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType recordType1 = new RecordType(null);
    RecordType recordType2 = new RecordType(null);
    boolean equivalent = recordType1.checkRecordEquivalenceHelper(recordType2, true);
    Assert.assertEquals(true, equivalent);
  }
  
  @Test
  public void testDefineProperty() {
    RecordType recordType = new RecordType(null);
    boolean defined = recordType.defineProperty("propertyName", null, true, null);
    Assert.assertEquals(false, defined);
  }
  
  @Test
  public void testGetGreatestSubtypeHelper() {
    RecordType recordType = new RecordType(null);
    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(null);
    Assert.assertEquals(null, greatestSubtype);
  }
  
  @Test
  public void testIsSubtype() {
    RecordType recordType = new RecordType(null);
    boolean isSubtype = recordType.isSubtype(null);
    Assert.assertEquals(false, isSubtype);
  }
  
  @Test
  public void testResolveInternal() {
    RecordType recordType = new RecordType(null);
    JSType resolvedType = recordType.resolveInternal(null, null);
    Assert.assertEquals(null, resolvedType);
  }
}