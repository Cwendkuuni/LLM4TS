import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

  private RecordType recordType;

  @Before
  public void setUp() {
    recordType = new RecordType(null, null, true);
  }

  @Test
  public void testIsSynthetic() {
    assertFalse(recordType.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType otherRecord = new RecordType(null, null, true);
    boolean result = recordType.checkRecordEquivalenceHelper(otherRecord, false);
    assertTrue(result);
  }

  @Test
  public void testGetImplicitPrototype() {
    ObjectType implicitPrototype = recordType.getImplicitPrototype();
    assertNotNull(implicitPrototype);
  }

  @Test
  public void testDefineProperty() {
    boolean result = recordType.defineProperty("propertyName", null, false, null);
    assertTrue(result);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType that = null;
    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(that);
    assertNotNull(greatestSubtype);
  }

  @Test
  public void testIsSubtype() {
    JSType that = null;
    boolean result = recordType.isSubtype(that);
    assertFalse(result);
  }

  @Test
  public void testResolveInternal() {
    ErrorReporter t = null;
    StaticScope<JSType> scope = null;
    JSType resolvedType = recordType.resolveInternal(t, scope);
    assertNotNull(resolvedType);
  }
}