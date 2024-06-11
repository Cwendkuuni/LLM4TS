import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSTestCase.*;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.SortedMap;

/**
 * Tests for {@link RecordType}.
 */
public class RecordTypeTest extends JSTestCase {
  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  /** Test method for {@link RecordType#RecordType(JSTypeRegistry, Map)}. */
  public void testRecordType() {
    // Test creating a new record type.
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", IntType.INSTANCE));
    properties.put("b", new RecordProperty("b", StringType.INSTANCE));

    RecordType recordType = new RecordType(registry, properties);

    // Test getters.
    assertEquals("RecordType", recordType.getClassName());
    assertEquals(RecordType.class, recordType.getTypeClass());
    assertFalse(recordType.isFunctionType());
    assertTrue(recordType.isNamedType());
    assertEquals(2, recordType.getNumProperties());

    // Test isEquivalentTo.
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    assertTrue(recordType1.isEquivalentTo(recordType2));

    // Test defineProperty.
    String propertyName = "c";
    JSType propertyType = IntType.INSTANCE;
    RecordProperty recordProperty = new RecordProperty(propertyName, propertyType);
    recordType.defineProperty(propertyName, propertyType, false, null);
    assertTrue(recordType.hasProperty(propertyName));
    assertEquals(propertyType, recordType.getPropertyType(propertyName));

    // Test isFrozen.
    recordType.defineProperty(propertyName, propertyType, false, null);
    assertFalse(recordType.isFrozen());
    recordType.defineProperty(propertyName, propertyType, false, null);
    assertTrue(recordType.isFrozen());

    // Test getImplicitPrototype.
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        recordType.getImplicitPrototype());

    // Test isEquivalentTo with different record types.
    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", IntType.INSTANCE));
    properties2.put("b", new RecordProperty("b", StringType.INSTANCE));
    properties2.put("c", new RecordProperty("c", DoubleType.INSTANCE));

    RecordType recordType3 = new RecordType(registry, properties2);
    assertFalse(recordType.isEquivalentTo(recordType3));

    // Test getLeastSupertype.
    JSType leastSupertype = recordType.getLeastSupertype(registry.getNativeObjectType(
        JSTypeNative.OBJECT_TYPE));
    assertEquals(recordType, leastSupertype);

    RecordType recordType4 = new RecordType(registry, properties);
    RecordType recordType5 = new RecordType(registry, properties2);
    RecordType recordType6 = new RecordType(registry, properties);

    JSType leastSupertype2 = recordType4.getLeastSupertype(recordType5);
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), leastSupertype2);

    JSType leastSupertype3 = recordType4.getLeastSupertype(recordType6);
    assertEquals(recordType4, leastSupertype3);

    // Test getGreatestSubtypeHelper.
    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(registry.getNativeObjectType(
        JSTypeNative.OBJECT_TYPE));
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype);

    RecordType recordType7 = new RecordType(registry, properties2);
    JSType greatestSubtype2 = recordType.getGreatestSubtypeHelper(recordType7);
    UnionTypeBuilder builder = new UnionTypeBuilder(registry);
    builder.addAlternate(registry.getRecordType(properties));
    builder.addAlternate(recordType7);
    JSType greatestSubtype3 = builder.build();
    assertEquals(greatestSubtype3, greatestSubtype2);

    // Test isSubtype.
    RecordType recordType8 = new RecordType(registry, properties);
    RecordType recordType9 = new RecordType(registry, Maps.newHashMap());
    assertTrue(recordType8.isSubtype(recordType9));

    RecordType recordType10 = new RecordType(registry, properties);
    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("a", new RecordProperty("a", IntType.INSTANCE));
    properties3.put("b", new RecordProperty("b", StringType.INSTANCE));
    properties3.put("c", new RecordProperty("c", IntType.INSTANCE));

    RecordType recordType11 = new RecordType(registry, properties3);
    RecordType recordType12 = new RecordType(registry, properties);

    assertTrue(recordType10.isSubtype(recordType12));
    assertFalse(recordType12.isSubtype(recordType10));

    // Test isEquivalentTo with record types with different order of properties.
    RecordType recordType13 = new RecordType(registry, properties2);
    RecordType recordType14 = new RecordType(registry, Maps.newHashMap());
    properties3.put("b", new RecordProperty("b", StringType.INSTANCE));
    properties3.put("a", new RecordProperty("a", IntType.INSTANCE));
    RecordType recordType15 = new RecordType(registry, properties3);
    assertTrue(recordType13.isEquivalentTo(recordType15));

    // Test defineProperty with frozen record type.
    RecordType recordType16 = new RecordType(registry, properties);
    recordType16.defineProperty("c", IntType.INSTANCE, false, null);
    assertEquals(3, recordType16.getNumProperties());
    assertTrue(recordType16.hasProperty("c"));

    try {
      recordType16.defineProperty("c", IntType.INSTANCE, false, null);
      fail("Expected IllegalStateException when trying to define a property on a frozen record type.");
    } catch (IllegalStateException e) {
      // Expected.
    }

    // Test greatestSubtype with incompatible types.
    RecordType recordType17 = new RecordType(registry, properties);
    RecordType recordType18 = new RecordType(registry, properties2);
    JSType greatestSubtype4 = recordType17.getGreatestSubtypeHelper(recordType18);
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), greatestSubtype4);

    // Test isSubtype with incompatible types.
    assertFalse(recordType17.isSubtype(recordType18));

    // Test resolveInternal.
    Node propertyNode = context.mkPropertyAccess(context.mkName("recordType17"), context.mkName("c"));
    JSType propertyType2 = registry.getNativeType(JSTypeNative.INT_TYPE);
    RecordProperty recordProperty2 = new RecordProperty("c", propertyType2);
    RecordType recordType19 = new RecordType(registry, properties);
    recordType19.defineProperty("c", propertyType2, false, propertyNode);

    JSType resolvedType = recordType17.resolveInternal(null, null);
    assertEquals(recordType17, resolvedType);

    JSType registry2 = new JSTypeRegistry();
    JSTypeRegistry registry3 = new JSTypeRegistry();
    registry3.addAll(registry);

    RecordProperty recordProperty3 = new RecordProperty("c", registry2.getNativeType(
        JSTypeNative.BOOL_TYPE));
    RecordType recordType20 = new RecordType(registry2, properties);
    recordType20.defineProperty("c", registry2.getNativeType(JSTypeNative.BOOL_TYPE),
        false, null);

    RecordType recordType21 = new RecordType(registry3, properties);
    recordType21.defineProperty("c", registry.getNativeType(JSTypeNative.INT_TYPE), false, null);

    JSType resolvedType2 = recordType21.resolveInternal(null, null);
    assertEquals(recordType17, resolvedType2);

    // Test isSubtype with empty record type.
    RecordType recordType22 = new RecordType(registry, Maps.newHashMap());
    assertTrue(recordType17.isSubtype(recordType22));

    // Test isEquivalentTo with empty record type.
    assertFalse(recordType17.isEquivalentTo(recordType22));

    // Test isEquivalentTo with different number of properties.
    Map<String, RecordProperty> properties4 = Maps.newHashMap();
    properties4.put("a", new RecordProperty("a", IntType.INSTANCE));
    properties4.put("b", new RecordProperty("b", StringType.INSTANCE));
    properties4.put("c", new RecordProperty("c", IntType.INSTANCE));

    RecordType recordType23 = new RecordType(registry, properties);
    RecordType recordType24 = new RecordType(registry, properties4);
    assertFalse(recordType23.isEquivalentTo(recordType24));

    // Test isSubtype with different number of properties.
    assertFalse(recordType23.isSubtype(recordType24));

    // Test isSubtype with incompatible property types.
    properties4.put("a", new RecordProperty("a", StringType.INSTANCE));
    RecordType recordType25 = new RecordType(registry, properties4);
    assertFalse(recordType23.isSubtype(recordType25));

    // Test isSubtype with incompatible property nodes.
    properties4.put("a", new RecordProperty("a", IntType.INSTANCE));
    properties4.put("a", new RecordProperty("a", StringType.INSTANCE));
    RecordType recordType26 = new RecordType(registry, properties4);
    assertFalse(recordType23.isSubtype(recordType26));
  }
}