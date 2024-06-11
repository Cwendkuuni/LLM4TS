package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.javascript.rhino.types.ObjectType;

import java.util.Map;

/**
 * Test cases for RecordType class.
 */
public class RecordTypeTest {

  /**
   * Test constructor.
   */
  @Test
  public void testConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    properties.put("b", new RecordProperty("b", registry.getStringType()));

    RecordType recordType = new RecordType(registry, properties);

    // Check properties.
    assertEquals("RecordType with properties [a: int, b: String]",
        recordType.toString());
    assertEquals(2, recordType.getProperties().size());
    assertEquals(registry.getIntType(), recordType.getPropertyType("a"));
    assertEquals(registry.getStringType(), recordType.getPropertyType("b"));
  }

  /**
   * Test isEquivalentTo.
   */
  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", registry.getIntType()));
    propertiesA.put("b", new RecordProperty("b", registry.getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", registry.getIntType()));
    propertiesB.put("b", new RecordProperty("b", registry.getIntType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    // Not equal.
    assertFalse("Record types with different properties should not be equal",
        recordTypeA.isEquivalentTo(recordTypeB));

    // Equal.
    propertiesB.put("b", new RecordProperty("b", registry.getStringType()));
    RecordType recordTypeC = new RecordType(registry, propertiesB);
    assertTrue("Record types with equal properties should be equal",
        recordTypeA.isEquivalentTo(recordTypeC));

    // Equal with properties of different order.
    propertiesB.put("a", 0);
    RecordType recordTypeD = new RecordType(registry, propertiesB);
    assertTrue("Record types with equal properties but different order should be equal",
        recordTypeA.isEquivalentTo(recordTypeD));
  }

  /**
   * Test getGreatestSubtypeHelper.
   */
  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", registry.getIntType()));
    propertiesA.put("b", new RecordProperty("b", registry.getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("b", new RecordProperty("b", registry.getStringType()));
    propertiesB.put("a", new RecordProperty("a", registry.getIntType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);

    assertTrue("Record type A should be a subtype of record type B",
        recordTypeB.isSubtype(grearestSubtype));
  }

  /**
   * Test isSubtype.
   */
  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", registry.getIntType()));
    propertiesA.put("b", new RecordProperty("b", registry.getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", registry.getIntType()));
    propertiesB.put("b", new RecordProperty("b", registry.getStringType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    // Record type A is a subtype of record type B.
    assertTrue("Record type A should be a subtype of record type B",
        recordTypeA.isSubtype(recordTypeB));

    // Record type B is not a subtype of record type A.
    assertFalse("Record type B should not be a subtype of record type A",
        recordTypeB.isSubtype(recordTypeA));

    // Record type C has fewer properties than record type A.
    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("a", new RecordProperty("a", registry.getIntType()));
    RecordType recordTypeC = new RecordType(registry, propertiesC);
    assertFalse("Record type C should not be a subtype of record type A",
        recordTypeC.isSubtype(recordTypeA));

    // Record type D has an additional property.
    Map<String, RecordProperty> propertiesD = Maps.newHashMap(propertiesA);
    propertiesD.put("c", new RecordProperty("c", registry.getBooleanType()));
    RecordType recordTypeD = new RecordType(registry, propertiesD);
    assertFalse("Record type D should not be a subtype of record type A",
        recordTypeD.isSubtype(recordTypeA));

    // Record type E has a property with a different type.
    Map<String, RecordProperty> propertiesE = Maps.newHashMap(propertiesA);
    propertiesE.put("a", new RecordProperty("a", registry.getStringType()));
    RecordType recordTypeE = new RecordType(registry, propertiesE);
    assertFalse("Record type E should not be a subtype of record type A",
        recordTypeE.isSubtype(recordTypeA));
  }

  /**
   * Test resolveInternal.
   */
  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));

    RecordType recordType = new RecordType(registry, properties);
    ErrorReporter errorReporter = new ErrorReporter() {
      @Override
      public void error(String message, int line, int lineOffset) {
        fail("Unexpected error: " + message);
      }

      @Override
      public void warning(String message, int line, int lineOffset) {
        // Ignore warnings.
      }
    };

    JSType resolvedType = recordType.resolveInternal(errorReporter, null);
    assertEquals("Record type", resolvedType.getClass().getSimpleName());

    // Test resolving property types.
    RecordProperty property = properties.get("a");
    JSType propertyType = property.getType();
    JSType resolvedPropertyType = resolvedType.getPropertyType("a");
    assertEquals("Int type", propertyType.getClass().getSimpleName(),
        resolvedPropertyType.getClass().getSimpleName());
  }

  /**
   * Test toMaybeRecordType.
   */
  @Test
  public void testToMaybeRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));

    RecordType recordType = new RecordType(registry, properties);
    assertEquals(recordType, recordType.toMaybeRecordType());
  }

  /**
   * Test defineProperty.
   */
  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));

    RecordType recordType = new RecordType(registry, properties);

    // Test defining a property.
    String propertyName = "b";
    JSType propertyType = registry.getStringType();
    Node propertyNode = null;
    boolean inferred = false;
    boolean result = recordType.defineProperty(propertyName, propertyType,
        inferred, propertyNode);
    assertTrue("Property should be defined", result);
    assertTrue("Record type should have property B",
        recordType.hasProperty(propertyName));
    assertEquals("String type", propertyType.getClass().getSimpleName(),
        recordType.getPropertyType(propertyName).getClass().getSimpleName());

    // Test defining a property with the same name.
    result = recordType.defineProperty(propertyName, propertyType, inferred,
        propertyNode);
    assertTrue("Property should be defined (again)", result);

    // Test defining a property with the same name but a different type.
    JSType differentPropertyType = registry.getBooleanType();
    result = recordType.defineProperty(propertyName, differentPropertyType,
        inferred, propertyNode);
    assertFalse("Different property type should not be defined", result);

    // Test defining a property with a name that does not exist.
    String newPropertyName = "c";
    result = recordType.defineProperty(newPropertyName, propertyType, inferred,
        propertyNode);
    assertTrue("New property should be defined", result);
    assertTrue("Record type should have property C", recordType.hasProperty(newPropertyName));
  }

  /**
   * Test getImplicitPrototype.
   */
  @Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));

    RecordType recordType = new RecordType(registry, properties);

    ObjectType implicitPrototype = recordType.getImplicitPrototype();
    assertEquals("Object type", ObjectType.class.getSimpleName(),
        implicitPrototype.getClass().getSimpleName());
  }

  /**
   * Test isFrozen.
   */
  @Test
  public void testIsFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));

    RecordType recordType = new RecordType(registry, properties);

    // Test default value.
    assertFalse("Record type should not be frozen by default", recordType.isFrozen());

    // Test freezing.
    recordType.freeze();
    assertTrue("Record type should be frozen", recordType.isFrozen());
  }

  /**
   * Test hasProperty.
   */
  @Test
  public void testHasProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));

    RecordType recordType = new RecordType(registry, properties);

    // Test property existence.
    assertTrue("Record type should have property A", recordType.hasProperty("a"));

    // Test property non-existence.
    assertFalse("Record type should not have property B", recordType.hasProperty("b"));
  }

  /**
   * Test getPropertyType.
   */
  @Test
  public void testGetPropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));

    RecordType recordType = new RecordType(registry, properties);

    // Test getting property type.
    JSType propertyType = recordType.getPropertyType("a");
    assertEquals("Int type", propertyType.getClass().getSimpleName(),
        propertyType.getClass().getSimpleName());
  }

  /**
   * Test getPropertyNode.
   */
  @Test
  public void testGetPropertyNode() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));

    RecordType recordType = new RecordType(registry, properties);

    // Test getting property node.
    Node propertyNode = recordType.getPropertyNode("a");
    assertNull("Property node should be null", propertyNode);
  }

  /**
   * Test getProperties.
   */
  @Test
  public void testGetProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));

    RecordType recordType = new RecordType(registry, properties);

    // Test getting properties.
    SortedMap<String, JSType> propertiesMap = recordType.getProperties();
    assertEquals("Record type should have one property", 1, propertiesMap.size());
    assertEquals("Property name A", "a", propertiesMap.keySet().iterator().next());
    assertEquals("Int type", registry.getIntType().getClass().getSimpleName(),
        propertiesMap.get("a").getClass().getSimpleName());
  }

  /**
   * Test getLeastSupertype.
   */
  @Test
  public void testGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", registry.getIntType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", registry.getStringType()));

    RecordType recordTypeB = new RecordType(registry, propertiesB);

    UnionTypeBuilder unionBuilder = new UnionTypeBuilder(registry);
    JSType leastSupertype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
    unionBuilder.addAlternate(recordTypeB);
    JSType leastSupertypeUnion = unionBuilder.build();
    JSType expectedLeastSupertype = recordTypeA.getLeastSupertype(leastSupertypeUnion);
    assertEquals("Expected least supertype to be record type A", recordTypeA, expectedLeastSupertype);
  }
}