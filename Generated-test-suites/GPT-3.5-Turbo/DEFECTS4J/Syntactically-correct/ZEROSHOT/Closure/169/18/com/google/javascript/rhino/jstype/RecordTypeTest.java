import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

  private RecordType recordType;

  @Before
  public void setUp() {
    recordType = new RecordType(null, null, null);
  }

  @Test
  public void testIsSynthetic() {
    boolean result = recordType.isSynthetic();
    assertFalse(result);
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType otherRecord = new RecordType(null, null, null);
    boolean result = recordType.checkRecordEquivalenceHelper(otherRecord, false);
    assertTrue(result);
  }

  @Test
  public void testDefineProperty() {
    String propertyName = "propertyName";
    JSType type = new JSType();
    boolean inferred = true;
    Node propertyNode = new Node();
    boolean result = recordType.defineProperty(propertyName, type, inferred, propertyNode);
    assertFalse(result);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType that = new JSType();
    JSType result = recordType.getGreatestSubtypeHelper(that);
    assertNull(result);
  }

  @Test
  public void testIsSubtype() {
    JSType that = new JSType();
    boolean result = recordType.isSubtype(that);
    assertFalse(result);
  }

  @Test
  public void testResolveInternal() {
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();
    JSType result = recordType.resolveInternal(t, scope);
    assertNotNull(result);
  }

}