import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

  @Test
  public void testDefineDeclaredProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, null);
    assertTrue(recordType.defineProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null));
    assertTrue(recordType.hasProperty("a"));
    assertEquals(registry.getNativeType(JSTypeNative.NUMBER_TYPE), recordType.getPropertyType("a"));
  }

  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType1 = new RecordType(registry, null);
    RecordType recordType2 = new RecordType(registry, null);
    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  // Edge case: record type with hasProperty as null
  @Test(expected = IllegalStateException.class)
  public void testRecordTypeWithNullProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", null);
    RecordType recordType = new RecordType(registry, properties);
  }

  // Exception handling: try to define property after the record type is frozen
  @Test
  public void testDefinePropertyAfterFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, null);
    assertTrue(recordType.defineProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null));
    recordType.freeze();
    assertFalse(recordType.defineProperty("b", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null));
  }

  // Exception handling: try to resolve internal type with unknown properties
  @Test
  public void testResolveInternalWithUnknownProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporterImpl();
    StaticScope<JSType> scope = new StaticScope<>();
    RecordType recordType = new RecordType(registry, null);
    recordType.defineProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);
    recordType.resolveInternal(errorReporter, scope);
    assertEquals(registry.getNativeType(JSTypeNative.NUMBER_TYPE), recordType.getPropertyType("a"));
  }

  // Exception handling: try to resolve internal type with unresolved properties
  @Test
  public void testResolveInternalWithUnresolvedProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporterImpl();
    StaticScope<JSType> scope = new StaticScope<>();
    RecordType recordType = new RecordType(registry, null);
    recordType.defineProperty("a", registry.createArrayType(registry.getNativeType(JSTypeNative.NUMBER_TYPE)), false, null);
    recordType.resolveInternal(errorReporter, scope);
    assertEquals(registry.createArrayType(registry.getNativeType(JSTypeNative.NUMBER_TYPE)), recordType.getPropertyType("a"));
  }

}