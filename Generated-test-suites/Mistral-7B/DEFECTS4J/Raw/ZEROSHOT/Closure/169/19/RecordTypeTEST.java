import static org.junit.Assert.*;
import static com.google.javascript.rhino.testing.TypeChecker.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;

import java.util.Map;
import java.util.SortedMap;

/**
 * Test for RecordType class in JsType package.
 */
public class RecordTypeTest {

  private static RecordType createRecordType(JSTypeRegistry registry,
      String[] properties, boolean declared) {
    Map<String, RecordProperty> propertyMap = Maps.newHashMap();
    for (String property : properties) {
      RecordProperty recordProperty = new RecordProperty();
      recordProperty.setType(registry.getUnknownType());
      recordProperty.setPropertyNode(Node.newObjectLiteral());
      propertyMap.put(property, recordProperty);
    }
    return new RecordType(registry, propertyMap, declared);
  }

  @org.junit.Test
  public void testRecordTypeWithSameProperties() {
    JSTypeRegistry registry = createRegistry();

    String[] propertiesA = {"a", "b"};
    RecordType recordTypeA = createRecordType(registry, propertiesA, true);
    RecordType recordTypeB = createRecordType(registry, propertiesA, true);

    assertEquals(recordTypeA, recordTypeB);
  }

  @org.junit.Test
  public void testRecordTypeWithDifferentProperties() {
    JSTypeRegistry registry = createRegistry();

    String[] propertiesA = {"a", "b"};
    RecordType recordTypeA = createRecordType(registry, propertiesA, true);
    String[] propertiesB = {"b", "c"};
    RecordType recordTypeB = createRecordType(registry, propertiesB, true);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
    assertFalse(recordTypeB.isSubtype(recordTypeA));
  }

  @org.junit.Test
  public void testRecordTypeWithSamePropertiesButDifferentTypes() {
    JSTypeRegistry registry = createRegistry();

    String[] properties = {"a", "b"};
    RecordType recordTypeA = createRecordType(registry, properties, true);
    RecordType recordTypeB = createRecordType(registry, properties, true);

    JSType typeA = registry.getNumberType();
    JSType typeB = registry.getStringType();

    RecordProperty recordPropertyA = new RecordProperty();
    recordPropertyA.setType(typeA);
    recordPropertyA.setPropertyNode(Node.newNumberLiteral(1));

    RecordProperty recordPropertyB = new RecordProperty();
    recordPropertyB.setType(typeB);
    recordPropertyB.setPropertyNode(Node.newStringLiteral("1"));

    Map<String, RecordProperty> propertyMap = Maps.newHashMap();
    propertyMap.put("a", recordPropertyA);
    propertyMap.put("b", recordPropertyB);

    RecordType recordTypeC = new RecordType(registry, propertyMap, true);

    assertTrue(recordTypeA.isSubtype(recordTypeC));
    assertTrue(recordTypeC.isSubtype(recordTypeA));
    assertFalse(recordTypeA.isSubtype(recordTypeB));
    assertFalse(recordTypeB.isSubtype(recordTypeA));
  }

  @org.junit.Test
  public void testRecordTypeWithGeneratedProperties() {
    JSTypeRegistry registry = createRegistry();

    String[] properties = {"a", "b"};
    RecordType recordTypeA = createRecordType(registry, properties, false);
    String[] propertiesB = {"b", "a"};
    RecordType recordTypeB = createRecordType(registry, propertiesB, false);

    RecordProperty recordPropertyA = new RecordProperty();
    recordPropertyA.setType(registry.getNumberType());
    recordPropertyA.setPropertyNode(Node.newNumberLiteral(1));

    RecordProperty recordPropertyB = new RecordProperty();
    recordPropertyB.setType(registry.getStringType());
    recordPropertyB.setPropertyNode(Node.newStringLiteral("1"));

    Map<String, RecordProperty> propertyMapA = Maps.newHashMap();
    propertyMapA.put("b", recordPropertyA);
    propertyMapA.put("a", recordPropertyB);

    Map<String, RecordProperty> propertyMapB = Maps.newHashMap();
    propertyMapB.put("a", recordPropertyB);
    propertyMapB.put("b", recordPropertyA);

    RecordType recordTypeC = new RecordType(registry, propertyMapA, true);
    RecordType recordTypeD = new RecordType(registry, propertyMapB, true);

    assertEquals(recordTypeC, recordTypeD);
  }

  @org.junit.Test
  public void testRecordTypeWithSubtypeProperties() {
    JSTypeRegistry registry = createRegistry();

    String[] propertiesA = {"a"};
    RecordType recordTypeA = new RecordType(registry, Maps.newHashMap(), true);
    JSType typeA = registry.getNumberType();

    RecordProperty recordPropertyA = new RecordProperty();
    recordPropertyA.setType(typeA);
    recordPropertyA.setPropertyNode(Node.newNumberLiteral(1));

    Map<String, RecordProperty> propertyMapB = Maps.newHashMap();
    propertyMapB.put("a", recordPropertyA);

    RecordType recordTypeB = new RecordType(registry, propertyMapB, true);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
  }

  @org.junit.Test
  public void testRecordTypeWithSupertypeProperties() {
    JSTypeRegistry registry = createRegistry();

    String[] propertiesA = {"a"};
    RecordType recordTypeA = new RecordType(registry, Maps.newHashMap(), true);
    JSType typeA = registry.getStringType();

    RecordProperty recordPropertyA = new RecordProperty();
    recordPropertyA.setType(typeA);
    recordPropertyA.setPropertyNode(Node.newStringLiteral("1"));

    Map<String, RecordProperty> propertyMapB = Maps.newHashMap();
    propertyMapB.put("a", recordPropertyA);

    RecordType recordTypeB = new RecordType(registry, propertyMapB, true);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }

  @org.junit.Test
  public void testRecordTypeWithUnknownPropertyType() {
    JSTypeRegistry registry = createRegistry();

    String[] properties = {"a"};
    RecordType recordType = new RecordType(registry, Maps.newHashMap(), true);

    assertFalse(recordType.isSubtype(registry.getNativeType(JSTypeNative.NO_TYPE)));
  }

  @org.junit.Test
  public void testRecordTypeWithEmptyProperties() {
    JSTypeRegistry registry = createRegistry();

    RecordType recordType = new RecordType(registry, Maps.newHashMap(), true);

    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType);
  }

  @org.junit.Test
  public void testRecordTypeWithFrozenProperties() {
    JSTypeRegistry registry = createRegistry();

    String[] properties = {"a"};
    RecordType recordType = new RecordType(registry, Maps.newHashMap(), true);

    assertFalse(recordType.isFrozen());

    recordType.freeze();

    assertTrue(recordType.isFrozen());
  }

  @org.junit.Test
  public void testRecordTypeWithPropertyEquivalence() {
    JSTypeRegistry registry = createRegistry();

    String[] propertiesA = {"a"};
    RecordType recordTypeA = new RecordType(registry, Maps.newHashMap(), true);

    String[] propertiesB = {"a"};
    RecordType recordTypeB = new RecordType(registry, Maps.newHashMap(), true);

    recordTypeA.setProperty("a", registry.getNumberType(), Node.newNumberLiteral(1));
    recordTypeB.setProperty("a", registry.getNumberType(), Node.newNumberLiteral(1));

    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, false));
  }

  @org.junit.Test
  public void testRecordTypeWithPropertyMismatch() {
    JSTypeRegistry registry = createRegistry();

    String[] propertiesA = {"a"};
    RecordType recordTypeA = new RecordType(registry, Maps.newHashMap(), true);

    String[] propertiesB = {"a"};
    RecordType recordTypeB = new RecordType(registry, Maps.newHashMap(), true);

    recordTypeA.setProperty("a", registry.getNumberType(), Node.newNumberLiteral(1));
    recordTypeB.setProperty("a", registry.getStringType(), Node.newStringLiteral("1"));

    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, false));
  }

  @org.junit.Test
  public void testRecordTypeWithUnknownProperty() {
    JSTypeRegistry registry = createRegistry();

    String[] propertiesA = {"a", "b"};
    RecordType recordTypeA = new RecordType(registry, Maps.newHashMap(), true);

    String[] propertiesB = {"a", "c"};
    RecordType recordTypeB = new RecordType(registry, Maps.newHashMap(), true);

    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, false));
  }

  @org.junit.Test
  public void testRecordTypeWithUnknownPropertyInBoth() {
    JSTypeRegistry registry = createRegistry();

    String[] propertiesA = {"a", "b"};
    RecordType recordTypeA = new RecordType(registry, Maps.newHashMap(), true);

    String[] propertiesB = {"a", "c"};
    RecordType recordTypeB = new RecordType(registry, Maps.newHashMap(), true);

    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, true));
  }

  @org.junit.Test
  public void testRecordTypeWithSubtypePropertiesInBoth() {
    JSTypeRegistry registry = createRegistry();

    String[] propertiesA = {"a"};
    RecordType recordTypeA = new RecordType(registry, Maps.newHashMap(), true);

    JSType typeA = registry.getNumberType();

    RecordProperty recordPropertyA = new RecordProperty();
    recordPropertyA.setType(typeA);
    recordPropertyA.setPropertyNode(Node.newNumberLiteral(1));

    Map<String, RecordProperty> propertyMapB = Maps.newHashMap();
    propertyMapB.put("a", recordPropertyA);

    RecordType recordTypeB = new RecordType(registry, propertyMapB, true);

    String[] propertiesB = {"a", "b"};
    RecordType recordTypeC = new RecordType(registry, Maps.newHashMap(), true);

    RecordProperty recordPropertyB = new RecordProperty();
    recordPropertyB.setType(registry.getStringType());
    recordPropertyB.setPropertyNode(Node.newStringLiteral("1"));

    propertyMapB.put("b", recordPropertyB);

    RecordType recordTypeD = new RecordType(registry, propertyMapB, true);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
    assertTrue(recordTypeB.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(recordTypeC));
    assertTrue(recordTypeC.isSubtype(recordTypeA));
    assertTrue(recordTypeB.isSubtype(recordTypeD));
    assertTrue(recordTypeD.isSubtype(recordTypeB));
  }

  @org.junit.Test
  public void testRecordTypeWithIncompatibleProperties() {
    JSTypeRegistry registry = createRegistry();

    String[] propertiesA = {"a"};
    RecordType recordTypeA = new RecordType(registry, Maps.newHashMap(), true);

    JSType typeA = registry.getNumberType();

    RecordProperty recordPropertyA = new RecordProperty();
    recordPropertyA.setType(typeA);
    recordPropertyA.setPropertyNode(Node.newNumberLiteral(1));

    Map<String, RecordProperty> propertyMapB = Maps.newHashMap();
    propertyMapB.put("a", recordPropertyA);

    RecordType recordTypeB = new RecordType(registry, propertyMapB, true);

    String[] propertiesB = {"a"};
    RecordType recordTypeC = new RecordType(registry, Maps.newHashMap(), true);

    RecordProperty recordPropertyB = new RecordProperty();
    recordPropertyB.setType(registry.getStringType());
    recordPropertyB.setPropertyNode(Node.newStringLiteral("1"));

    propertyMapB.put("a", recordPropertyB);

    RecordType recordTypeD = new RecordType(registry, propertyMapB, true);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
    assertFalse(recordTypeB.isSubtype(recordTypeA));
    assertFalse(recordTypeA.isSubtype(recordTypeC));
    assertFalse(recordTypeC.isSubtype(recordTypeA));
    assertFalse(recordTypeB.isSubtype(recordTypeD));
    assertFalse(recordTypeD.isSubtype(recordTypeB));
  }

  @org.junit.Test
  public void testRecordTypeWithSubtype() {
    JSTypeRegistry registry = createRegistry();

    String[] propertiesA = {"a"};
    RecordType recordTypeA = new RecordType(registry, Maps.newHashMap(), true);

    JSType typeA = registry.getNumberType();

    RecordProperty recordPropertyA = new RecordProperty();
    recordPropertyA.setType(typeA);
    recordPropertyA.setPropertyNode(Node.newNumberLiteral(1));

    Map<String, RecordProperty> propertyMapB = Maps.newHashMap();
    propertyMapB.put("a", recordPropertyA);

    RecordType recordTypeB = new RecordType(registry, propertyMapB, true);

    String[] propertiesB = {"a", "b"};
    RecordType recordTypeC = new RecordType(registry, Maps.newHashMap(), true);

    RecordProperty recordPropertyB = new RecordProperty();
    recordPropertyB.setType(registry.getStringType());
    recordPropertyB.setPropertyNode(Node.newStringLiteral("1"));

    propertyMapB.put("b", recordPropertyB);

    RecordType recordTypeD = new RecordType(registry, propertyMapB, true);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
    assertTrue(recordTypeB.isSubtype(recordTypeC));
    assertTrue(recordTypeA.isSubtype(recordTypeC));
    assertFalse(recordTypeB.isSubtype(recordTypeD));
    assertFalse(recordTypeD.isSubtype(recordTypeB));
    assertTrue(recordTypeC.isSubtype(recordTypeD));
    assertTrue(recordTypeD.isSubtype(recordTypeC));
  }

  private static JSTypeRegistry createRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.setErrorReporter(ErrorReporter.NULL);
    registry.setTypeChecker(new TypeChecker(registry));
    registry.setNativeObjectTypes(JSTypeNative.getTypes(registry));
    return registry;
  }
}