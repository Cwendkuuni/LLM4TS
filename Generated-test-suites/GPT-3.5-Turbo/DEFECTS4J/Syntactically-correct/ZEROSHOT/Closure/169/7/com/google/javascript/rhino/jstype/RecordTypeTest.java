import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
  
  private JSTypeRegistry registry;
  private RecordType recordType;
  
  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    recordType = new RecordType(registry, new HashMap<String, RecordProperty>(), true);
  }
  
  @Test
  public void testIsSynthetic() {
    boolean result = recordType.isSynthetic();
    assertFalse(result);
  }
  
  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType otherRecord = new RecordType(registry, new HashMap<String, RecordProperty>(), true);
    boolean result = recordType.checkRecordEquivalenceHelper(otherRecord, true);
    assertTrue(result);
  }
  
  @Test
  public void testDefineProperty() {
    boolean result = recordType.defineProperty("propertyName", JSType.UNKNOWN, true, Node.EMPTY);
    assertFalse(result);
  }
  
  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType that = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    JSType result = recordType.getGreatestSubtypeHelper(that);
    assertNotNull(result);
  }
  
  @Test
  public void testIsSubtype() {
    RecordType that = new RecordType(registry, new HashMap<String, RecordProperty>(), true);
    boolean result = recordType.isSubtype(that);
    assertFalse(result);
  }
  
  @Test
  public void testResolveInternal() {
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>();
    JSType result = recordType.resolveInternal(t, scope);
    assertNotNull(result);
  }
}