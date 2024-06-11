import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  /**
   * Test the creation of a record type with no properties.
   */
  @Test
  public void testEmptyRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    RecordType recordType = new RecordType(registry, properties);
    assertEquals(recordType.getClass(), RecordType.class);
    assertTrue(recordType.isRecordType());
    assertNull(recordType.getGreatestSubtypeHelper(null));
  }

  /**
   * Test the creation of a record type with one property.
   */
  @Test
  public void testRecordTypeWithOneProperty() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getStringType()));
    RecordType recordType = new RecordType(registry, properties);

    assertEquals(recordType.getClass(), RecordType.class);
    assertTrue(recordType.isRecordType());
    assertNotNull(recordType.getGreatestSubtypeHelper(null));

    // Test property access
    assertEquals("a", recordType.getPropertyNames()[0]);
    assertEquals(registry.getStringType(), recordType.getPropertyType("a"));
  }

  /**
   * Test the creation of a record type with multiple properties.
   */
  @Test
  public void testRecordTypeWithMultipleProperties() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getStringType()));
    properties.put("b", new RecordProperty("b", registry.getNumberType()));
    RecordType recordType = new RecordType(registry, properties);

    assertEquals(recordType.getClass(), RecordType.class);
    assertTrue(recordType.isRecordType());
    assertNotNull(recordType.getGreatestSubtypeHelper(null));

    // Test property access
    assertEquals("a", recordType.getPropertyNames()[0]);
    assertEquals(registry.getStringType(), recordType.getPropertyType("a"));
    assertEquals("b", recordType.getPropertyNames()[1]);
    assertEquals(registry.getNumberType(), recordType.getPropertyType("b"));
  }

  /**
   * Test the isSubtype method with equivalent record types.
   */
  @Test
  public void testIsSubtypeWithEquivalentRecordTypes() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", registry.getStringType()));
    propertiesA.put("b", new RecordProperty("b", registry.getNumberType()));
    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", registry.getStringType()));
    propertiesB.put("b", new RecordProperty("b", registry.getNumberType()));
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertTrue(recordTypeA.isEquivalentTo(recordTypeB));
    assertTrue(recordTypeA.isSubtype(recordTypeB));
    assertTrue(recordTypeB.isSubtype(recordTypeA));
  }

  /**
   * Test the isSubtype method with incompatible record types.
   */
  @Test
  public void testIsSubtypeWithIncompatibleRecordTypes() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", registry.getStringType()));
    propertiesA.put("b", new RecordProperty("b", registry.getNumberType()));
    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", registry.getNumberType()));
    propertiesB.put("b", new RecordProperty("b", registry.getStringType()));
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertFalse(recordTypeA.isEquivalentTo(recordTypeB));
    assertFalse(recordTypeA.isSubtype(recordTypeB));
    assertFalse(recordTypeB.isSubtype(recordTypeA));
  }

  /**
   * Test the getGreatestSubtypeHelper method with an empty type.
   */
  @Test
  public void testGetGreatestSubtypeHelperWithEmptyType() {
    RecordType recordType = new RecordType(registry, Maps.newHashMap());

    assertNull(recordType.getGreatestSubtypeHelper(null));
  }

  /**
   * Test the getGreatestSubtypeHelper method with a record type as the parameter.
   */
  @Test
  public void testGetGreatestSubtypeHelperWithRecordType() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", registry.getStringType()));
    propertiesA.put("b", new RecordProperty("b", registry.getNumberType()));
    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", registry.getStringType()));
    propertiesB.put("c", new RecordProperty("c", registry.getBooleanType()));
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    RecordType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
    assertEquals(recordTypeA.getClass(), greatestSubtype.getClass());
    assertTrue(recordTypeA.isEquivalentTo(greatestSubtype));
  }

  /**
   * Test the getGreatestSubtypeHelper method with a non-record type as the parameter.
   */
  @Test
  public void testGetGreatestSubtypeHelperWithNonRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getStringType()));
    properties.put("b", new RecordProperty("b", registry.getNumberType()));
    RecordType recordType = new RecordType(registry, properties);

    UnionTypeBuilder builder = new UnionTypeBuilder(registry);
    builder.addAlternate(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    builder.addAlternate(registry.getNativeObjectType(JSTypeNative.NULL_TYPE));
    JSType unionType = builder.build();

    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(unionType);
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).getClass(),
        greatestSubtype.getClass());
    assertFalse(recordType.isEquivalentTo(greatestSubtype));
  }

  /**
   * Test the isSubtype method with a record type and a non-record type.
   */
  @Test
  public void testIsSubtypeWithRecordTypeAndNonRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getStringType()));
    properties.put("b", new RecordProperty("b", registry.getNumberType()));
    RecordType recordType = new RecordType(registry, properties);

    UnionTypeBuilder builder = new UnionTypeBuilder(registry);
    builder.addAlternate(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    builder.addAlternate(registry.getNativeObjectType(JSTypeNative.NULL_TYPE));
    JSType unionType = builder.build();

    assertFalse(recordType.isSubtype(unionType));
  }
}