import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class RecordTypeTest {

  @Test
  public void testRecordTypeConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordTypeBuilder.RecordProperty(TypeA));
    properties.put("b", new RecordTypeBuilder.RecordProperty(TypeB));

    RecordType recordType = new RecordType(registry, properties, true);
    assertNotNull(recordType);
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordTypeBuilder.RecordProperty(TypeA));
    properties.put("b", new RecordTypeBuilder.RecordProperty(TypeB));

    RecordType recordType = new RecordType(registry, properties);
    assertTrue(recordType.defineProperty("c", TypeC, true, null));
    assertFalse(recordType.defineProperty("c", TypeC, false, null));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
    properties1.put("a", new RecordTypeBuilder.RecordProperty(TypeA));
    properties1.put("b", new RecordTypeBuilder.RecordProperty(TypeB));

    RecordType recordType1 = new RecordType(registry, properties1, true);

    Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
    properties2.put("c", new RecordTypeBuilder.RecordProperty(TypeC));
    properties2.put("d", new RecordTypeBuilder.RecordProperty(TypeD));

    RecordType recordType2 = new RecordType(registry, properties2, true);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertNotNull(greatestSubtype);
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
    properties1.put("a", new RecordTypeBuilder.RecordProperty(TypeA));
    properties1.put("b", new RecordTypeBuilder.RecordProperty(TypeB));

    RecordType recordType1 = new RecordType(registry, properties1, true);

    Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
    properties2.put("c", new RecordTypeBuilder.RecordProperty(TypeC));
    properties2.put("d", new RecordTypeBuilder.RecordProperty(TypeD));

    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertFalse(recordType1.isSubtype(recordType2));
    assertTrue(recordType2.isSubtype(recordType1));
  }

  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NULL_TYPE)));
    properties.put("b",  new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.UNDEFINED_TYPE)));

    RecordType recordType = new RecordType(registry, properties);
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();
    
    JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
    assertNotNull(resolvedType);
  }
}