import org.junit.Assert;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testDefineProperty() {
    RecordType recordType = new RecordType(null, null, false);
    boolean result = recordType.defineProperty("property", null, false, null);
    Assert.assertTrue(result);
  }

  @Test
  public void testIsSubtype() {
    RecordType recordType = new RecordType(null, null, false);
    boolean result = recordType.isSubtype(null);
    Assert.assertFalse(result);
  }

  @Test
  public void testResolveInternal() {
    RecordType recordType = new RecordType(null, null, false);
    JSType resolvedType = recordType.resolveInternal(null, null);
    Assert.assertEquals(recordType, resolvedType);
  }

}