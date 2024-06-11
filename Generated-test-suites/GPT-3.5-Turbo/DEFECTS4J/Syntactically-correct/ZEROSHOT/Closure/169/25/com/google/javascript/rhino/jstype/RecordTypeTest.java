import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
  
  private RecordType recordType;
  
  @Before
  public void setup() {
    recordType = new RecordType(null, null);
  }
  
  @Test
  public void testDefineProperty() {
    boolean result = recordType.defineProperty("propertyName", new JSType(), true, new Node());
    assertTrue(result);
  }
  
  @Test
  public void testGetImplicitPrototype() {
    ObjectType implicitPrototype = recordType.getImplicitPrototype();
    assertNotNull(implicitPrototype);
  }
  
  @Test
  public void testIsSynthetic() {
    boolean isSynthetic = recordType.isSynthetic();
    assertFalse(isSynthetic);
  }
  
  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType otherRecord = new RecordType(null, null);
    boolean result = recordType.checkRecordEquivalenceHelper(otherRecord, true);
    assertTrue(result);
  }
  
  @Test
  public void testIsSubtype() {
    RecordType that = new RecordType(null, null);
    boolean isSubtype = recordType.isSubtype(that);
    assertTrue(isSubtype);
  }
  
  @Test
  public void testResolveInternal() {
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>();
    JSType resolvedType = recordType.resolveInternal(t, scope);
    assertNotNull(resolvedType);
  }
  
}