import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.PropertyNode;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnknownType;

public class RecordTypeTest {

  private static JSTypeRegistry registry;

  @org.junit.BeforeClass
  public static void setup() {
    registry = new JSTypeRegistry();
  }

  // Test constructor
  @Test
  public void testRecordTypeConstructor() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNativeStringType()));

    RecordType recordType = new RecordType(registry, properties);
    assertEquals(registry.getRecordType("{a: String}"), recordType);
  }

  // Test isEquivalentTo
  @Test
  public void testIsEquivalentTo() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getNativeStringType()));
    properties1.put("b", new RecordProperty("b", registry.getNativeNumberType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getNativeStringType()));
    properties2.put("b", new RecordProperty("b", registry.getNativeStringType()));
    properties2.put("c", new RecordProperty("c", registry.getNativeStringType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.isEquivalentTo(recordType2));

    properties2.remove("c");

    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  // Test getImplicitPrototype
  @Test
  public void testGetImplicitPrototype() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNativeStringType()));

    RecordType recordType = new RecordType(registry, properties);

    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        recordType.getImplicitPrototype());
  }

  // Test defineProperty
  @Test
  public void testDefineProperty() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNativeStringType()));

    RecordType recordType = new RecordType(registry, properties);

    assertFalse(recordType.defineProperty("b", registry.getNativeNumberType(),
        false, null));

    recordType.defineProperty("a", registry.getNativeNumberType(), false, null);

    assertEquals(registry.getNativeNumberType(), recordType.getPropertyType("a"));
  }

  // Test getGreatestSubtypeHelper
  @Test
  public void testGetGreatestSubtypeHelper() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getNativeStringType()));
    properties1.put("b", new RecordProperty("b", registry.getNativeNumberType()));

    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getNativeStringType()));
    properties2.put("c", new RecordProperty("c", registry.getNativeStringType()));

    RecordType recordType2 = new RecordType(registry, properties2);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

    assertEquals(registry.getRecordType("{a: String}"), greatestSubtype);
  }

  // Test isSubtype
  @Test
  public void testIsSubtype() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getNativeStringType()));

    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getNativeStringType()));
    properties2.put("b", new RecordProperty("b", registry.getNativeNumberType()));

    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isSubtype(recordType2));

    properties2.put("c", new RecordProperty("c", registry.getNativeStringType()));

    assertFalse(recordType1.isSubtype(recordType2));
  }

  // Test resolveInternal
  @Test
  public void testResolveInternal() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNativeStringType()));

    RecordType recordType = new RecordType(registry, properties);

    JSTypeString typeA = registry.getNativeStringType();
    JSTypeNumber typeB = registry.getNativeNumberType();
    JSTypeUnknown typeC = registry.getNativeUnknownType();

    PropertyNode propNode = new PropertyNode("a");

    // Test with no properties to resolve
    assertEquals(recordType, recordType.resolveInternal(null, null));

    // Test with a property of a known type
    recordType.defineProperty("a", typeA, false, propNode);

    JSType resolvedType = recordType.resolveInternal(null, null);

    assertEquals(recordType, resolvedType);

    // Test with a property of an unknown type
    recordType.defineProperty("b", typeC, false, propNode);

    resolvedType = recordType.resolveInternal(null, null);

    assertEquals(recordType, resolvedType);

    // Test with a property of a known type that has been resolved
    recordType.getPropertyType("a").resolve(null, null);

    resolvedType = recordType.resolveInternal(null, null);

    assertEquals(recordType, resolvedType);

    // Test with a property of a known type that has not been resolved
    JSType typeD = registry.getNativeBooleanType();

    recordType.defineProperty("b", typeD, false, propNode);

    resolvedType = recordType.resolveInternal(null, null);

    JSType resolvedPropertyType = resolvedType.getPropertyType("b");

    assertEquals(typeD, resolvedPropertyType);
  }

  // Test defineProperty with a frozen record
  @Test(expected = UnsupportedOperationException.class)
  public void testDefinePropertyWithFrozenRecord() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNativeStringType()));

    RecordType recordType = new RecordType(registry, properties);
    recordType.isFrozen = true;

    recordType.defineProperty("b", registry.getNativeNumberType(), false, null);
  }

  // Test getGreatestSubtype with a UnionType
  @Test
  public void testGetGreatestSubtypeWithUnionType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNativeStringType()));

    RecordType recordType = new RecordType(registry, properties);

    UnionType unionType = new UnionType(registry);
    unionType.addAlternate(registry.getNativeStringType());
    unionType.addAlternate(registry.getNativeNumberType());

    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(unionType);

    assertEquals(registry.getRecordType("{a: String, a: Number}"), greatestSubtype);
  }
}