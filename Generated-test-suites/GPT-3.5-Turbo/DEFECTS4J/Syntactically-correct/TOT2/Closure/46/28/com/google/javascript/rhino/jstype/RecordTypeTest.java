import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testDefineProperty() {
    RecordType recordType = new RecordType(null, null);
    
    boolean isDefined = recordType.defineProperty("property1", JSType.STRING, false, null);
    
    assertTrue(isDefined);
    assertEquals(JSType.STRING, recordType.getPropertyType("property1"));
  }

  @Test
  public void testIsEquivalentTo() {
    RecordType recordType1 = new RecordType(null, null);
    RecordType recordType2 = new RecordType(null, null);
    
    boolean isEquivalent = recordType1.isEquivalentTo(recordType2);
    
    assertTrue(isEquivalent);
  }

  @Test
  public void testGetLeastSupertype() {
    RecordType recordType1 = new RecordType(null, null);
    RecordType recordType2 = new RecordType(null, null);
    
    JSType leastSupertype = recordType1.getLeastSupertype(recordType2);
    
    assertEquals(recordType1, leastSupertype);
  }

  @Test
  public void testIsSubtype() {
    RecordType recordType1 = new RecordType(null, null);
    RecordType recordType2 = new RecordType(null, null);
    
    boolean isSubtype = recordType1.isSubtype(recordType2);
    
    assertTrue(isSubtype);
  }

  @Test
  public void testResolveInternal() {
    RecordType recordType = new RecordType(null, null);
    
    JSType resolvedType = recordType.resolveInternal(null, null);
    
    assertNotNull(resolvedType);
  }
}