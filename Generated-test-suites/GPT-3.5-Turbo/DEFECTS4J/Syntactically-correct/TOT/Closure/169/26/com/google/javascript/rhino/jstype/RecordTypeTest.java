import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

  @Test
  public void testRecordType_constructor() {
    RecordProperty property1 = new RecordProperty("a", JSTypeNative.STRING_TYPE, node);
    RecordProperty property2 = new RecordProperty("b", JSTypeNative.NUMBER_TYPE, node);
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", property1);
    properties.put("b", property2);
    RecordType recordType = new RecordType(registry, properties, true);

    assertEquals(recordType.getProperties().size(), 2);
    assertEquals(recordType.isSynthetic(), false);
    assertEquals(recordType.isFrozen(), true);
  }

  @Test
  public void testRecordType_isSynthetic() {
    RecordType recordType = new RecordType(registry, properties, false);
    boolean isSynthetic = recordType.isSynthetic();

    assertEquals(isSynthetic, true);
  }

  @Test
  public void testRecordType_checkRecordEquivalenceHelper() {
    RecordType recordType1 = new RecordType(registry, properties, true);
    RecordType recordType2 = new RecordType(registry, properties, true);
    boolean equivalence = recordType1.checkRecordEquivalenceHelper(recordType2, false);

    assertEquals(equivalence, true);
  }

  @Test
  public void testRecordType_getImplicitPrototype() {
    RecordType recordType = new RecordType(registry, properties, true);
    ObjectType implicitPrototype = recordType.getImplicitPrototype();

    assertEquals(implicitPrototype, registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
  }

  @Test
  public void testRecordType_defineProperty() {
    RecordType recordType = new RecordType(registry, properties, true);
    boolean defined = recordType.defineProperty("c", JSTypeNative.BOOLEAN_TYPE, false, node);

    assertEquals(defined, true);
    assertEquals(recordType.getProperties().size(), 3);

    JSType propertyCType = recordType.getPropertyType("c");
    assertEquals(propertyCType, JSTypeNative.BOOLEAN_TYPE);
  }

  @Test
  public void testRecordType_getGreatestSubtypeHelper() {
    RecordProperty property = new RecordProperty("a", JSTypeNative.STRING_TYPE, node);
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", property);
    RecordType recordType1 = new RecordType(registry, properties, true);

    RecordType recordType2 = new RecordType(registry, properties, true);
    ObjectType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

    assertEquals(greatestSubtype instanceof RecordType, true);
    assertEquals(((RecordType) greatestSubtype).getProperties().size(), 1);
  }

  @Test
  public void testRecordType_toMaybeRecordType() {
    RecordType recordType = new RecordType(registry, properties, true);
    RecordType maybeRecordType = recordType.toMaybeRecordType();

    assertEquals(maybeRecordType instanceof RecordType, true);
    assertEquals(maybeRecordType, recordType);
  }

  @Test
  public void testRecordType_isSubtype() {
    RecordType recordType1 = new RecordType(registry, properties, true);
    RecordType recordType2 = new RecordType(registry, properties, true);
    boolean isSubtype = recordType1.isSubtype(recordType2);

    assertEquals(isSubtype, true);
  }

  @Test
  public void testRecordType_resolveInternal() {
    RecordType recordType = new RecordType(registry, properties, true);
    ErrorReporter errorReporter = new ErrorReporter();
    RecordType resolvedRecordType = recordType.resolveInternal(errorReporter, scope);

    assertEquals(resolvedRecordType instanceof RecordType, true);
    assertEquals(resolvedRecordType, recordType);
  }

}