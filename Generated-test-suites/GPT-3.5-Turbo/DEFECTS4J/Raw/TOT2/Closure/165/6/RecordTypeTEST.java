import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testDefineProperty() {
    RecordType recordType = new RecordType(new JSTypeRegistry(), new HashMap<>());
    assertTrue(recordType.defineProperty("property1", JSType.BOOLEAN_TYPE, false, null));
    assertEquals(JSType.BOOLEAN_TYPE, recordType.getPropertyType("property1"));
  }

  @Test
  public void testDefinePropertyInferred() {
    RecordType recordType = new RecordType(new JSTypeRegistry(), new HashMap<>());
    assertTrue(recordType.defineProperty("property1", JSType.BOOLEAN_TYPE, true, null));
    assertEquals(null, recordType.getPropertyType("property1"));
  }

  @Test
  public void testIsSubtype() {
    RecordType recordType1 = new RecordType(new JSTypeRegistry(), new HashMap<>());
    RecordType recordType2 = new RecordType(new JSTypeRegistry(), new HashMap<>());
    assertTrue(RecordType.isSubtype(recordType1, recordType2));
  }

  @Test
  public void testIsEquivalentTo() {
    RecordType recordType1 = new RecordType(new JSTypeRegistry(), new HashMap<>());
    RecordType recordType2 = new RecordType(new JSTypeRegistry(), new HashMap<>());
    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testResolveInternal() {
    RecordType recordType = new RecordType(new JSTypeRegistry(), new HashMap<>());
    ErrorReporter reporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();
    JSType resolvedType = recordType.resolveInternal(reporter, scope);
    assertNotNull(resolvedType);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testIllegalStateExceptionInConstructor() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("property1", null);
    new RecordType(new JSTypeRegistry(), properties);
  }

  @Test
  public void testGetGreatestSubtypeHelperEmptyType() {
    RecordType recordType = new RecordType(new JSTypeRegistry(), new HashMap<>());
    JSType emptyType = JSType.NO_TYPE;
    assertEquals(emptyType, recordType.getGreatestSubtypeHelper(emptyType));
  }
  
  @Test(expected = IllegalStateException.class)
  public void testIllegalStateExceptionInDefineProperty() {
    RecordType recordType = new RecordType(new JSTypeRegistry(), new HashMap<>());
    recordType.defineProperty("property1", JSType.BOOLEAN_TYPE, false, null);
    recordType.defineProperty("property1", JSType.NUMBER_TYPE, false, null);
  }

}