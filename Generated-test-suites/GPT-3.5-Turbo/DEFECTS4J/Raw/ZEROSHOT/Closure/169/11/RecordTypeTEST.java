import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
  
  private RecordType recordType;
  
  @Before
  public void setUp() {
    recordType = new RecordType(new JSTypeRegistry(), null);
  }
  
  @Test
  public void testIsSynthetic() {
    boolean result = recordType.isSynthetic();
    assertFalse(result);
  }
  
  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType otherRecord = new RecordType(new JSTypeRegistry(), null);
    boolean result = recordType.checkRecordEquivalenceHelper(otherRecord, true);
    assertFalse(result);
  }
  
  @Test
  public void testGetPropertyType() {
    JSType result = recordType.getPropertyType("propertyName");
    assertNull(result);
  }
  
  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType that = new JSType(new JSTypeRegistry(), null, null);
    JSType result = recordType.getGreatestSubtypeHelper(that);
    assertNotNull(result);
  }
  
  @Test
  public void testIsSubtype() {
    JSType that = new JSType(new JSTypeRegistry(), null, null);
    boolean result = recordType.isSubtype(that);
    assertFalse(result);
  }
  
  @Test
  public void testDefineProperty() {
    String propertyName = "propertyName";
    JSType type = new JSType(new JSTypeRegistry(), null, null);
    boolean inferred = true;
    Node propertyNode = new Node();
    boolean result = recordType.defineProperty(propertyName, type, inferred, propertyNode);
    assertFalse(result);
  }
  
  @Test
  public void testToMaybeRecordType() {
    RecordType result = recordType.toMaybeRecordType();
    assertNotNull(result);
  }
  
  @Test
  public void testResolveInternal() {
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>();
    JSType result = recordType.resolveInternal(t, scope);
    assertNotNull(result);
  }

}