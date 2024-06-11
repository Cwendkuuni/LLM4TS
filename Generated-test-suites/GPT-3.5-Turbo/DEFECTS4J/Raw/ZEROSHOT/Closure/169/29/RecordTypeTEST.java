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
    boolean result = recordType.isSynthetic();
    assertFalse(result);
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType otherRecord = new RecordType(null, null, false);
    boolean tolerateUnknowns = false;
    boolean result = recordType.checkRecordEquivalenceHelper(otherRecord, tolerateUnknowns);
    assertFalse(result);
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeConstructor() {
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    recordType = new RecordType(null, properties);
  }

  @Test
  public void testRecordTypeConstructorDeclared() {
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    recordType = new RecordType(null, properties, true);
  }

  @Test
  public void testRecordTypeConstructorSynthesized() {
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    recordType = new RecordType(null, properties, false);
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType typeA = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    RecordType typeB = new RecordType(null, null);
    boolean result = RecordType.isSubtype(typeA, typeB);
    assertTrue(result);
  }

  @Test
  public void testResolveInternal() {
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();
    JSType result = recordType.resolveInternal(errorReporter, scope);
    assertNull(result);
  }
}