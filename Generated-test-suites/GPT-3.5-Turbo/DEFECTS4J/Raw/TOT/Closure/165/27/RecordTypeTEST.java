import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

  @Test
  public void testRecordTypeConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.UNKNOWN_TYPE), null));
    properties.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.OBJECT_TYPE), null));
    RecordType recordType = new RecordType(registry, properties);

    assertEquals(registry.getNativeType(JSTypeNative.UNKNOWN_TYPE), recordType.getPropertyType("a"));
    assertEquals(registry.getNativeType(JSTypeNative.OBJECT_TYPE), recordType.getPropertyType("b"));
  }

  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.UNKNOWN_TYPE), null));
    properties.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.OBJECT_TYPE), null));
    RecordType recordType = new RecordType(registry, properties);

    assertTrue(recordType.defineProperty("c", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null));
    assertEquals(registry.getNativeType(JSTypeNative.STRING_TYPE), recordType.getPropertyType("c"));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    properties1.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    properties2.put("c", new RecordProperty(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), null));
    RecordType recordType2 = new RecordType(registry, properties2);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

    assertTrue(greatestSubtype.isRecordType());
    assertEquals(registry.getNativeType(JSTypeNative.STRING_TYPE), greatestSubtype.getPropertyType("b"));
    assertEquals(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), greatestSubtype.getPropertyType("c"));
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    properties1.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    properties2.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordProperty(registry.getNamedType("MyType"), null));
    RecordType recordType = new RecordType(registry, properties);

    ErrorReporter reporter = new MyErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();
    JSType resolvedType = recordType.resolveInternal(reporter, scope);

    assertTrue(resolvedType.isRecordType());
    assertEquals(registry.getNamedType("MyType"), resolvedType.getPropertyType("a"));
  }

}