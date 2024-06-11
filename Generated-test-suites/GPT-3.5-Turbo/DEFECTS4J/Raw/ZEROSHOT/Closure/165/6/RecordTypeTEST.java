import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.RecordType;
import java.util.Map;
import java.util.TreeMap;

public class RecordTypeTest {

  private JSTypeRegistry registry;
  private Map<String, RecordTypeBuilder.RecordProperty> properties;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    properties = new TreeMap<>();
  }

  @Test(expected=IllegalStateException.class)
  public void testConstructor_NullRecordProperty_ExceptionThrown() {
    properties.put("a", null);
    RecordType recordType = new RecordType(registry, properties);
  }

  @Test
  public void testDefineProperty_NormalCase_PropertyDefined() {
    RecordType recordType = new RecordType(registry, properties);
    boolean result = recordType.defineProperty("a", registry.getNativeType(JSTypeNative.UNDEFINED_TYPE), false, null);
    assertTrue(result);
    assertTrue(recordType.hasProperty("a"));
    assertEquals(registry.getNativeType(JSTypeNative.UNDEFINED_TYPE), recordType.getPropertyType("a"));
  }

  @Test
  public void testGetImplicitPrototype_NormalCase_ImplicitPrototypeReturned() {
    RecordType recordType = new RecordType(registry, properties);
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
  }

  @Test
  public void testIsEquivalentTo_SameRecordType_TrueReturned() {
    RecordType recordType1 = new RecordType(registry, properties);
    Map<String, RecordTypeBuilder.RecordProperty> properties2 = new TreeMap<>();
    properties2.put("a", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.UNDEFINED_TYPE), null));
    RecordType recordType2 = new RecordType(registry, properties2);
    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsSubtype_SubtypeRecordType_ReturnTrue() {
    Map<String, RecordTypeBuilder.RecordProperty> properties2 = new TreeMap<>();
    properties2.put("a", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.UNDEFINED_TYPE), null));
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties2);
    assertTrue(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testToMaybeRecordType_NormalCase_RecordTypeReturned() {
    RecordType recordType = new RecordType(registry, properties);
    assertEquals(recordType, recordType.toMaybeRecordType());
  }

}