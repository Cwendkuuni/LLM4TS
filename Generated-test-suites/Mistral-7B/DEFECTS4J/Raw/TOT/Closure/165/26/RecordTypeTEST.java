import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordTypeTest {

  private static ErrorReporter errorReporter;

  @org.junit.BeforeClass
  public static void init() {
    errorReporter = new ErrorReporter() {
      @Override
      public void error(int line, String message) {
        System.err.printf("Line %d: %s%n", line, message);
      }

      @Override
      public void warning(int line, String message) {
        System.err.printf("Line %d: %s%n", line, message);
      }
    };
  }

  @org.junit.Test
  public void testCreateRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    properties.put("b", new RecordProperty("b", registry.getIntType()));

    RecordType recordType = new RecordType(registry, properties);
    assertNotNull(recordType);
    assertEquals("Record", recordType.getClassName());
    assertEquals(2, recordType.getNumProperties());
    assertTrue(recordType.isRecordType());

    TreeMap<String, JSType> expectedProperties = new TreeMap<>();
    expectedProperties.put("a", registry.getIntType());
    expectedProperties.put("b", registry.getIntType());
    JSType expectedRecordType = new RecordTypeBuilder(registry).build(
        "Record", expectedProperties).build();
    assertEquals(expectedRecordType, recordType);
  }

  @org.junit.Test
  public void testCreateRecordTypeWithNullRecordProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    properties.put("b", null);

    try {
      new RecordType(registry, properties);
      fail("Expected an IllegalStateException");
    } catch (IllegalStateException e) {
      assertEquals("RecordProperty associated with a property should not be null!",
          e.getMessage());
    }
  }

  @org.junit.Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getIntType()));
    properties1.put("b", new RecordProperty("b", registry.getIntType()));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getIntType()));
    properties2.put("b", new RecordProperty("b", registry.getIntType()));
    properties2.put("c", new RecordProperty("c", registry.getIntType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("a", new RecordProperty("a", registry.getIntType()));
    properties3.put("b", new RecordProperty("b", registry.getIntType()));
    RecordType recordType3 = new RecordType(registry, properties3);

    Map<String, RecordProperty> properties4 = Maps.newHashMap();
    properties4.put("a", new RecordProperty("a", registry.getIntType()));
    properties4.put("c", new RecordProperty("c", registry.getIntType()));
    RecordType recordType4 = new RecordType(registry, properties4);

    Map<String, RecordProperty> properties5 = Maps.newHashMap();
    properties5.put("b", new RecordProperty("b", registry.getIntType()));
    properties5.put("c", new RecordProperty("c", registry.getIntType()));
    RecordType recordType5 = new RecordType(registry, properties5);

    Map<String, RecordProperty> properties6 = Maps.newHashMap();
    properties6.put("d", new RecordProperty("d", registry.getIntType()));
    RecordType recordType6 = new RecordType(registry, properties6);

    assertEquals(recordType1, recordType1);
    assertEquals(recordType1, recordType3);
    assertFalse(recordType1.isEquivalentTo(recordType2));
    assertFalse(recordType1.isEquivalentTo(recordType4));
    assertFalse(recordType1.isEquivalentTo(recordType5));
    assertFalse(recordType1.isEquivalentTo(recordType6));
  }

  @org.junit.Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, Maps.newHashMap());
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        recordType.getImplicitPrototype());
  }

  @org.junit.Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    properties.put("b", new RecordProperty("b", registry.getIntType()));

    RecordType recordType = new RecordType(registry, properties);
    assertFalse(recordType.defineProperty("c", registry.getIntType(), false, null));
    assertTrue(recordType.defineProperty("a", registry.getIntType(), false, null));
    assertTrue(recordType.defineProperty("b", registry.getIntType(), false, null));

    assertEquals(registry.getIntType(), recordType.getPropertyType("a"));
    assertEquals(registry.getIntType(), recordType.getPropertyType("b"));
    assertNull(recordType.getPropertyType("c"));
  }

  @org.junit.Test
  public void testFreezeRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    properties.put("b", new RecordProperty("b", registry.getIntType()));

    RecordType recordType = new RecordType(registry, properties);
    assertFalse(recordType.isFrozen());

    recordType.freeze();
    assertTrue(recordType.isFrozen());

    try {
      recordType.defineProperty("a", registry.getIntType(), false, null);
      fail("Expected an IllegalStateException");
    } catch (IllegalStateException e) {
      assertEquals("Cannot define property on a frozen record type.",
          e.getMessage());
    }
  }

  @org.junit.Test
  public void testGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // A is a subtype of B if all properties of A are properties of B
    // and all types of corresponding properties are subtypes of each other.
    Map<String, RecordProperty> aProps = Maps.newHashMap();
    aProps.put("x", new RecordProperty("x", registry.getStringType()));
    aProps.put("y", new RecordProperty("y", registry.getIntType()));
    RecordType a = new RecordType(registry, aProps);

    Map<String, RecordProperty> bProps = Maps.newHashMap();
    bProps.put("x", new RecordProperty("x", registry.getStringType()));
    bProps.put("y", new RecordProperty("y", registry.getNativeObjectType(
        JSTypeNative.ANY_TYPE)));
    RecordType b = new RecordType(registry, bProps);

    Map<String, RecordProperty> cProps = Maps.newHashMap();
    cProps.put("x", new RecordProperty("x", registry.getStringType()));
    cProps.put("y", new RecordProperty("y", registry.getIntType()));
    RecordType c = new RecordType(registry, cProps);

    Map<String, RecordProperty> dProps = Maps.newHashMap();
    dProps.put("x", new RecordProperty("x", registry.getStringType()));
    dProps.put("z", new RecordProperty("z", registry.getIntType()));
    RecordType d = new RecordType(registry, dProps);

    Map<String, RecordProperty> eProps = Maps.newHashMap();
    eProps.put("z", new RecordProperty("z", registry.getIntType()));
    RecordType e = new RecordType(registry, eProps);

    assertEquals(a, a.getGreatestSubtype(a));
    assertEquals(a, a.getGreatestSubtype(b));
    assertEquals(a, a.getGreatestSubtype(c));
    assertEquals(b, b.getGreatestSubtype(a));
    assertEquals(b, b.getGreatestSubtype(b));
    assertEquals(c, c.getGreatestSubtype(a));
    assertEquals(c, c.getGreatestSubtype(b));
    assertEquals(c, c.getGreatestSubtype(c));
    assertEquals(d, d.getGreatestSubtype(a));
    assertEquals(d, d.getGreatestSubtype(b));
    assertEquals(d, d.getGreatestSubtype(c));
    assertEquals(d, d.getGreatestSubtype(d));
    assertEquals(e, e.getGreatestSubtype(a));
    assertEquals(e, e.getGreatestSubtype(b));
    assertEquals(e, e.getGreatestSubtype(c));
    assertEquals(e, e.getGreatestSubtype(d));
    assertEquals(b, b.getGreatestSubtype(e));

    RecordType f = new RecordType(registry, Maps.newHashMap());
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        f.getGreatestSubtype(a));
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        a.getGreatestSubtype(f));

    UnionType u = new UnionTypeBuilder(registry).build();
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        a.getGreatestSubtype(u));
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        u.getGreatestSubtype(a));

    assertEquals(a, a.getGreatestSubtype(a));

    assertEquals(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE),
        a.getGreatestSubtype(registry.getNativeObjectType(JSTypeNative.NULL_TYPE)));

    // Test that record type with incompatible property types
    // is not a subtype of another record type
    Map<String, RecordProperty> incompatibleA = Maps.newHashMap();
    incompatibleA.put("x", new RecordProperty("x", registry.getIntType()));
    incompatibleA.put("y", new RecordProperty("y", registry.getStringType()));
    RecordType incompatibleRecordA = new RecordType(registry, incompatibleA);

    Map<String, RecordProperty> incompatibleB = Maps.newHashMap();
    incompatibleB.put("x", new RecordProperty("x", registry.getIntType()));
    incompatibleB.put("y", new RecordProperty("y", registry.getIntType()));
    RecordType incompatibleRecordB = new RecordType(registry, incompatibleB);

    assertEquals(registry.getNativeType(JSTypeNative.NO_TYPE),
        incompatibleRecordA.getGreatestSubtype(incompatibleRecordB));
    assertEquals(registry.getNativeType(JSTypeNative.NO_TYPE),
        incompatibleRecordB.getGreatestSubtype(incompatibleRecordA));
  }

  @org.junit.Test
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntType()));
    properties.put("b", new RecordProperty("b", registry.getStringType()));

    RecordType recordType = new RecordType(registry, properties);

    Node aNode = new Node(42);
    Node bNode = new Node("hello");

    RecordProperty aProp = new RecordProperty("a", registry.getIntType());
    RecordProperty bProp = new RecordProperty("b", registry.getStringType());

    assertEquals(registry.getIntType(), recordType.resolvePropertyType("a", errorReporter, null));
    assertEquals(registry.getStringType(), recordType.resolvePropertyType("b", errorReporter, null));
    assertEquals(registry.getIntType(), recordType.resolvePropertyType("a", errorReporter, aNode));
    assertEquals(registry.getStringType(), recordType.resolvePropertyType("b", errorReporter, bNode));

    JSType resolvedRecordType = recordType.resolve(errorReporter, null);
    assertEquals(recordType, resolvedRecordType);

    Map<String, Node> resolvedProperties = Maps.newHashMap();
    resolvedProperties.put("a", aNode);
    resolvedProperties.put("b", bNode);
    assertEquals(recordType, new RecordType(registry, resolvedProperties).resolve(errorReporter, null));
  }
}