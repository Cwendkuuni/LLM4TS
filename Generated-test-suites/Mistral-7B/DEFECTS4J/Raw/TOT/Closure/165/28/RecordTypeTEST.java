import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.testing.JSTestCase;

/**
 * Test for {@link RecordType} class.
 */
public class RecordTypeTest extends JSTestCase {

  private JSTypeRegistry registry;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    registry = new JSTypeRegistry();
  }

  @Test
  public void testRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Number.class));
    RecordType recordType = new RecordType(registry, properties);

    // Test constructor
    assertInstanceOf(RecordType.class, recordType);

    // Test getImplicitPrototype
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        recordType.getImplicitPrototype());

    // Test isEquivalentTo
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    assertTrue(recordType1.isEquivalentTo(recordType2));

    // Test defineProperty
    RecordProperty newProperty = new RecordProperty("gender", String.class);
    recordType.defineProperty("gender", newProperty.getType(), false, null);
    assertNotNull(recordType.getPropertyType("gender"));

    // Test isSubtype
    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    RecordType recordType3 = new RecordType(registry, Maps.newTreeMap());
    RecordType recordType4 = new RecordType(registry, Maps.newTreeMap());
    RecordType recordType5 = new RecordType(registry, Maps.newTreeMap());
    RecordType recordType6 = new RecordType(registry, new RecordProperty[] {
        new RecordProperty("name", String.class),
        new RecordProperty("age", Number.class),
        new RecordProperty("gender", String.class)
    });

    assertTrue(objectType.isSubtype(recordType1));
    assertFalse(recordType1.isSubtype(objectType));
    assertFalse(recordType1.isSubtype(recordType2));
    assertFalse(recordType1.isSubtype(recordType3));
    assertFalse(recordType1.isSubtype(recordType4));
    assertTrue(recordType1.isSubtype(recordType6));
    assertTrue(recordType6.isSubtype(recordType1));

    // Test getGreatestSubtypeHelper
    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType6);
    assertEquals(recordType6.getClass(), greatestSubtype.getClass());

    // Test toMaybeRecordType
    assertEquals(recordType1, recordType1.toMaybeRecordType());

    // Test isFrozen
    recordType1.defineProperty("newProperty", Integer.class, true, null);
    assertFalse(recordType1.isFrozen());
    recordType5.isFrozen = true;
    assertFalse(recordType5.defineProperty("newProperty", Integer.class, false, null));
  }
}