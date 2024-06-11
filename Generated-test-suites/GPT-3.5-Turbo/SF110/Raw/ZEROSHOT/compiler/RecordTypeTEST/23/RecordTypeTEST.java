import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RecordTypeTest {

  private JSTypeRegistry registry;
  private RecordType recordType;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordProperty("property1", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    properties.put("property2", new RecordProperty("property2", registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    properties.put("property3", new RecordProperty("property3", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), null));
    recordType = new RecordType(registry, properties);
  }

  @Test
  public void testIsSubtype() {
    assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    assertFalse(recordType.isSubtype(registry.getNativeType(JSTypeNative.ARRAY_TYPE)));
    assertFalse(recordType.isSubtype(registry.getNativeType(JSTypeNative.NULL_TYPE)));
    assertFalse(recordType.isSubtype(registry.getNativeType(JSTypeNative.UNDEFINED_TYPE)));
  }

  @Test
  public void testDefineProperty() {
    assertFalse(recordType.defineProperty("property4", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null));
  }

  @Test
  public void testGetImplicitPrototype() {
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType otherRecord = new RecordType(registry, new HashMap<>());
    assertFalse(recordType.checkRecordEquivalenceHelper(otherRecord, null));

    Map<String, RecordProperty> otherProperties = new HashMap<>();
    otherProperties.put("property1", new RecordProperty("property1", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    otherProperties.put("property2", new RecordProperty("property2", registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    otherProperties.put("property3", new RecordProperty("property3", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), null));
    otherRecord = new RecordType(registry, otherProperties);
    assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, null));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType otherType = registry.getNativeType(JSTypeNative.STRING_TYPE);
    assertSame(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), recordType.getGreatestSubtypeHelper(otherType));

    otherType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertSame(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), recordType.getGreatestSubtypeHelper(otherType));
  }

  @Test
  public void testToMaybeRecordType() {
    assertSame(recordType, recordType.toMaybeRecordType());
  }

  @Test
  public void testIsSynthetic() {
    assertFalse(recordType.isSynthetic());
  }

}