import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

  private RecordType recordType;

  @Before
  public void setUp() {
    recordType = new RecordType(null, null, true);
  }

  @Test
  public void testIsSynthetic() {
    assertEquals(false, recordType.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType otherRecord = new RecordType(null, null, true);
    boolean result = recordType.checkRecordEquivalenceHelper(otherRecord, true);
    assertEquals(true, result);
  }

  @Test
  public void testGetImplicitPrototype() {
    ObjectType prototype = recordType.getImplicitPrototype();
    assertNotNull(prototype);
  }

  @Test
  public void testDefineProperty() {
    boolean result = recordType.defineProperty("propertyName", null, false, null);
    assertEquals(true, result);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType that = new JSType();
    JSType result = recordType.getGreatestSubtypeHelper(that);
    assertNotNull(result);
  }

  @Test
  public void testIsSubtype() {
    JSType that = new JSType();
    boolean result = recordType.isSubtype(that);
    assertEquals(false, result);
  }

  @Test
  public void testResolveInternal() {
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();
    JSType result = recordType.resolveInternal(t, scope);
    assertNotNull(result);
  }
}