import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.parser.jsType.JSType;
import com.google.javascript.jscomp.parser.jsType.RecordType;
import com.google.javascript.jscomp.parser.jsType.RecordProperty;
import com.google.javascript.jscomp.parser.jsType.TypeRegistry;
import com.google.javascript.jscomp.parser.jsType.UnionType;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.node.Node;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

  private static final ErrorReporter REPORTER = new ErrorReporter() {};
  private static final TypeRegistry REGISTRY = new JSTypeRegistry(REPORTER);

  @org.junit.BeforeClass
  public static void setup() {
    REGISTRY.setCompatibilityMode(CompatibilityMode.ES5);
  }

  @org.junit.Test
  public void testBasicRecordTypeCreation() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("x", new RecordProperty("x", new IntType(REGISTRY)));
    recordProperties.put("y", new RecordProperty("y", new StringType(REGISTRY)));

    RecordType recordType = new RecordType(REGISTRY, recordProperties);
    assertEquals("{x: int, y: string}", recordType.toString());
  }

  @org.junit.Test
  public void testRecordTypeFrozen() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("x", new RecordProperty("x", new IntType(REGISTRY)));
    recordProperties.put("y", new RecordProperty("y", new StringType(REGISTRY)));

    RecordType recordType = new RecordType(REGISTRY, recordProperties);
    assertFalse(recordType.isFrozen());

    recordType.defineProperty("x", new IntType(REGISTRY), false, Node.NO_NODE);
    recordType.defineProperty("z", new IntType(REGISTRY), false, Node.NO_NODE);

    assertEquals("{x: int, y: string, z: int}", recordType.toString());
    assertFalse(recordType.isFrozen());

    recordType.freeze();
    assertTrue(recordType.isFrozen());

    try {
      recordType.defineProperty("x", new IntType(REGISTRY), false, Node.NO_NODE);
      assertFalse(true);
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @org.junit.Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("x", new RecordProperty("x", new IntType(REGISTRY)));
    recordProperties1.put("y", new RecordProperty("y", new StringType(REGISTRY)));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("x", new RecordProperty("x", new IntType(REGISTRY)));
    recordProperties2.put("y", new RecordProperty("y", new StringType(REGISTRY)));

    RecordType recordType1 = new RecordType(REGISTRY, recordProperties1);
    RecordType recordType2 = new RecordType(REGISTRY, recordProperties2);

    assertEquals(recordType1, recordType2);
  }

  @org.junit.Test
  public void testRecordTypeSubtyping() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("x", new RecordProperty("x", new IntType(REGISTRY)));
    recordProperties1.put("y", new RecordProperty("y", new StringType(REGISTRY)));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("x", new RecordProperty("x", new IntType(REGISTRY)));
    recordProperties2.put("y", new RecordProperty("y", new StringType(REGISTRY)));
    recordProperties2.put("z", new RecordProperty("z", new IntType(REGISTRY)));

    RecordType recordType1 = new RecordType(REGISTRY, recordProperties1);
    RecordType recordType2 = new RecordType(REGISTRY, recordProperties2);

    assertTrue(recordType1.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType2));
    assertTrue(recordType2.isSubtype(recordType1));
  }

  @org.junit.Test
  public void testRecordTypeSubtypingWithUnknownTypes() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("x", new RecordProperty("x", new UnionType(REGISTRY, new IntType(REGISTRY), new StringType(REGISTRY))));
    recordProperties1.put("y", new RecordProperty("y", new UnionType(REGISTRY, new IntType(REGISTRY), new StringType(REGISTRY))));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("x", new RecordProperty("x", new UnionType(REGISTRY, new IntType(REGISTRY), new IntType(REGISTRY))));
    recordProperties2.put("y", new RecordProperty("y", new UnionType(REGISTRY, new StringType(REGISTRY), new StringType(REGISTRY))));

    RecordType recordType1 = new RecordType(REGISTRY, recordProperties1);
    RecordType recordType2 = new RecordType(REGISTRY, recordProperties2);

    assertTrue(recordType1.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType2));
    assertTrue(recordType2.isSubtype(recordType1));
  }

  @org.junit.Test
  public void testRecordTypeSubtypingWithMissingProperties() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("x", new RecordProperty("x", new IntType(REGISTRY)));
    recordProperties1.put("y", new RecordProperty("y", new StringType(REGISTRY)));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("x", new RecordProperty("x", new IntType(REGISTRY)));

    RecordType recordType1 = new RecordType(REGISTRY, recordProperties1);
    RecordType recordType2 = new RecordType(REGISTRY, recordProperties2);

    assertFalse(recordType1.isSubtype(recordType2));
  }

  @org.junit.Test
  public void testRecordTypeSubtypingWithIncompatibleProperties() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("x", new RecordProperty("x", new IntType(REGISTRY)));
    recordProperties1.put("y", new RecordProperty("y", new StringType(REGISTRY)));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("x", new RecordProperty("x", new StringType(REGISTRY)));
    recordProperties2.put("y", new RecordProperty("y", new IntType(REGISTRY)));

    RecordType recordType1 = new RecordType(REGISTRY, recordProperties1);
    RecordType recordType2 = new RecordType(REGISTRY, recordProperties2);

    assertFalse(recordType1.isSubtype(recordType2));
  }

  @org.junit.Test
  public void testRecordTypeSubtypingWithDifferentOrderOfProperties() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("x", new RecordProperty("x", new IntType(REGISTRY)));
    recordProperties1.put("y", new RecordProperty("y", new StringType(REGISTRY)));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("y", new RecordProperty("y", new StringType(REGISTRY)));
    recordProperties2.put("x", new RecordProperty("x", new IntType(REGISTRY)));

    RecordType recordType1 = new RecordType(REGISTRY, recordProperties1);
    RecordType recordType2 = new RecordType(REGISTRY, recordProperties2);

    assertTrue(recordType1.isSubtype(recordType2));
  }

  @org.junit.Test
  public void testRecordTypeIsEquivalentToReturnsFalseForDifferentTypes() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("x", new RecordProperty("x", new IntType(REGISTRY)));
    recordProperties1.put("y", new RecordProperty("y", new StringType(REGISTRY)));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("x", new RecordProperty("x", new FloatType(REGISTRY)));
    recordProperties2.put("y", new RecordProperty("y", new StringType(REGISTRY)));

    RecordType recordType1 = new RecordType(REGISTRY, recordProperties1);
    RecordType recordType2 = new RecordType(REGISTRY, recordProperties2);

    assertFalse(recordType1.isEquivalentTo(recordType2));
  }

  @org.junit.Test
  public void testRecordTypeGreatestSubtypeReturnsEmptyObjectTypeForNullTypes() {
    RecordType recordType1 = new RecordType(REGISTRY, Maps.newHashMap());
    RecordType recordType2 = new RecordType(REGISTRY, Maps.newHashMap());

    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);
    assertEquals(REGISTRY.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype);
  }

  @org.junit.Test
  public void testRecordTypeGreatestSubtypeReturnsRecordTypeWithCommonProperties() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("x", new RecordProperty("x", new IntType(REGISTRY)));
    recordProperties1.put("y", new RecordProperty("y", new StringType(REGISTRY)));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("x", new RecordProperty("x", new IntType(REGISTRY)));
    recordProperties2.put("z", new RecordProperty("z", new StringType(REGISTRY)));

    RecordType recordType1 = new RecordType(REGISTRY, recordProperties1);
    RecordType recordType2 = new RecordType(REGISTRY, recordProperties2);

    RecordType greatestSubtype = (RecordType) recordType1.getGreatestSubtype(recordType2);
    Map<String, JSType> greatestSubtypeProperties = greatestSubtype.getProperties();

    assertEquals(2, greatestSubtypeProperties.size());
    assertEquals(recordType1.getPropertyType("x"), greatestSubtype.getPropertyType("x"));
    assertEquals(recordType1.getPropertyType("y"), greatestSubtype.getPropertyType("y"));
  }

  @org.junit.Test
  public void testRecordTypeGreatestSubtypeReturnsEmptyObjectTypeForIncompatibleTypes() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("x", new RecordProperty("x", new IntType(REGISTRY)));
    recordProperties1.put("y", new RecordProperty("y", new StringType(REGISTRY)));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("x", new RecordProperty("x", new FloatType(REGISTRY)));
    recordProperties2.put("y", new RecordProperty("y", new StringType(REGISTRY)));

    RecordType recordType1 = new RecordType(REGISTRY, recordProperties1);
    RecordType recordType2 = new RecordType(REGISTRY, recordProperties2);

    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);
    assertEquals(REGISTRY.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype);
  }

  @org.junit.Test
  public void testRecordTypeGreatestSubtypeReturnsRecordTypeWithAllProperties() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("x", new RecordProperty("x", new IntType(REGISTRY)));
    recordProperties1.put("y", new RecordProperty("y", new StringType(REGISTRY)));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("x", new RecordProperty("x", new IntType(REGISTRY)));
    recordProperties2.put("y", new RecordProperty("y", new StringType(REGISTRY)));
    recordProperties2.put("z", new RecordProperty("z", new StringType(REGISTRY)));

    RecordType recordType1 = new RecordType(REGISTRY, recordProperties1);
    RecordType recordType2 = new RecordType(REGISTRY, recordProperties2);

    RecordType greatestSubtype = (RecordType) recordType1.getGreatestSubtype(recordType2);
    Map<String, JSType> greatestSubtypeProperties = greatestSubtype.getProperties();

    assertEquals(3, greatestSubtypeProperties.size());
    assertEquals(recordType1.getPropertyType("x"), greatestSubtype.getPropertyType("x"));
    assertEquals(recordType1.getPropertyType("y"), greatestSubtype.getPropertyType("y"));
    assertNull(greatestSubtype.getPropertyType("z"));
  }

  @org.junit.Test
  public void testRecordTypeGreatestSubtypeReturnsUnionTypeForDifferentPropertyTypes() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("x", new RecordProperty("x", new IntType(REGISTRY)));
    recordProperties1.put("y", new RecordProperty("y", new StringType(REGISTRY)));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("x", new RecordProperty("x", new FloatType(REGISTRY)));
    recordProperties2.put("y", new RecordProperty("y", new StringType(REGISTRY)));

    RecordType recordType1 = new RecordType(REGISTRY, recordProperties1);
    RecordType recordType2 = new RecordType(REGISTRY, recordProperties2);

    UnionType greatestSubtype = (UnionType) recordType1.getGreatestSubtype(recordType2);
    Map<Node, JSType> greatestSubtypeAlternatives = greatestSubtype.getAlternatives();

    assertEquals(2, greatestSubtypeAlternatives.size());

    JSType intType = greatestSubtypeAlternatives.get(Node.INT_LITERAL);
    JSType floatType = greatestSubtypeAlternatives.get(Node.FLOAT_LITERAL);

    assertEquals(recordType1.getPropertyType("x"), intType);
    assertEquals(recordType1.getPropertyType("y"), floatType);
  }
}