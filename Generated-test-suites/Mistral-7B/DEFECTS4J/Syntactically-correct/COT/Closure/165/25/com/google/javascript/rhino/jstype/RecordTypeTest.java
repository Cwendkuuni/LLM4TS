import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSTypeNative.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordTypeTest {

  private static JSTypeRegistry registry;
  private static ErrorReporter errorReporter;

  @org.junit.BeforeClass
  public static void setUpBeforeClass() throws Exception {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter() {
      @Override
      public void error(String message, int line, int lineNumber, String sourceName) {
        fail("Unexpected error: " + message);
      }

      @Override
      public void warn(String message, int line, int lineNumber, String sourceName) {
        System.out.println("Warning: " + message);
      }
    };
  }

  @org.junit.After
  public void tearDown() throws Exception {
    registry = null;
  }

  /**
   * Test constructor.
   */
  @org.junit.Test
  public void testConstructor() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.stringType()));
    properties.put("b", new RecordProperty("b", registry.numberType()));

    RecordType recordType = new RecordType(registry, properties);

    assertEquals(registry.getNativeObjectType(OBJECT_TYPE), recordType.getImplicitPrototype());
  }

  /**
   * Test isEquivalentTo.
   */
  @org.junit.Test
  public void testIsEquivalentTo() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.stringType()));
    properties1.put("b", new RecordProperty("b", registry.numberType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.stringType()));
    properties2.put("b", new RecordProperty("b", registry.numberType()));
    properties2.put("c", new RecordProperty("c", registry.numberType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isEquivalentTo(recordType1));
    assertTrue(recordType1.isEquivalentTo(recordType2));

    RecordType recordType3 = new RecordType(registry, properties2);
    assertFalse(recordType1.isEquivalentTo(recordType3));
  }

  /**
   * Test defineProperty.
   */
  @org.junit.Test
  public void testDefineProperty() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.stringType()));

    RecordType recordType = new RecordType(registry, properties);

    recordType.defineProperty("a", registry.stringType(), false, null);

    assertEquals(registry.stringType(), recordType.getPropertyType("a"));
  }

  /**
   * Test defineProperty - frozen.
   */
  @org.junit.Test
  public void testDefinePropertyFrozen() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.stringType()));

    RecordType recordType = new RecordType(registry, properties);
    recordType.isFrozen = true;

    try {
      recordType.defineProperty("a", registry.stringType(), false, null);
      fail();
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  /**
   * Test getGreatestSubtypeHelper.
   */
  @org.junit.Test
  public void testGetGreatestSubtypeHelper() {
    RecordType recordType1 = createRecordType("{a: string, b: number}");
    RecordType recordType2 = createRecordType("{a: string, b: boolean}");

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

    assertEquals(createUnionType("string, boolean"), greatestSubtype);
  }

  /**
   * Test getGreatestSubtypeHelper - different property types.
   */
  @org.junit.Test
  public void testGetGreatestSubtypeHelperDifferentPropertyTypes() {
    RecordType recordType1 = createRecordType("{a: string, b: number}");
    RecordType recordType2 = createRecordType("{a: boolean, b: number}");

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

    assertEquals(registry.getNativeObjectType(NO_TYPE), greatestSubtype);
  }

  /**
   * Test getGreatestSubtypeHelper - same property types.
   */
  @org.junit.Test
  public void testGetGreatestSubtypeHelperSamePropertyTypes() {
    RecordType recordType1 = createRecordType("{a: string, b: number}");
    RecordType recordType2 = createRecordType("{b: number, a: string}");

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

    assertEquals(createRecordType("{a: string, b: number}"), greatestSubtype);
  }

  /**
   * Test isSubtype.
   */
  @org.junit.Test
  public void testIsSubtype() {
    RecordType recordType1 = createRecordType("{a: string, b: number}");
    RecordType recordType2 = createRecordType("{a: string, b: boolean, c: number}");

    assertTrue(recordType1.isSubtype(recordType2));

    RecordType recordType3 = createRecordType("{a: string, c: number}");

    assertTrue(recordType1.isSubtype(recordType3));

    RecordType recordType4 = createRecordType("{b: number, c: number}");

    assertFalse(recordType1.isSubtype(recordType4));
  }

  /**
   * Test isSubtype - empty record type.
   */
  @org.junit.Test
  public void testIsSubtypeEmptyRecordType() {
    RecordType recordType1 = createRecordType("{}");

    RecordType recordType2 = createRecordType("{a: string}");

    assertTrue(recordType1.isSubtype(recordType2));
  }

  /**
   * Test isSubtype - object type.
   */
  @org.junit.Test
  public void testIsSubtypeObjectType() {
    RecordType recordType = createRecordType("{a: string}");

    assertTrue(ObjectType.OBJECT_TYPE.isSubtype(recordType));
  }

  /**
   * Test isSubtype - primitive types.
   */
  @org.junit.Test
  public void testIsSubtypePrimitiveTypes() {
    RecordType recordType1 = createRecordType("{a: string}");

    JSType primitiveType = registry.booleanType();

    assertFalse(recordType1.isSubtype(primitiveType));
  }

  /**
   * Test resolveInternal.
   */
  @org.junit.Test
  public void testResolveInternal() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.stringType()));

    RecordType recordType = new RecordType(registry, properties);

    JSType resolvedType = recordType.resolveInternal(errorReporter, null);

    assertEquals(createRecordType("{a: string}"), resolvedType);
  }

  /**
   * Test toMaybeRecordType.
   */
  @org.junit.Test
  public void testToMaybeRecordType() {
    RecordType recordType = new RecordType(registry, null);

    assertEquals(recordType, recordType.toMaybeRecordType());
  }

  /**
   * Test getImplicitPrototype.
   */
  @org.junit.Test
  public void testGetImplicitPrototype() {
    RecordType recordType = new RecordType(registry, null);

    assertEquals(registry.getNativeObjectType(OBJECT_TYPE), recordType.getImplicitPrototype());
  }

  /**
   * Test isEquivalentTo - undefined.
   */
  @org.junit.Test
  public void testIsEquivalentToUndefined() {
    RecordType recordType = new RecordType(registry, null);

    assertFalse(recordType.isEquivalentTo(JSTypeFactory.getUndefinedType(registry)));
  }

  /**
   * Test isSubtype - undefined.
   */
  @org.junit.Test
  public void testIsSubtypeUndefined() {
    RecordType recordType = new RecordType(registry, null);

    assertFalse(recordType.isSubtype(JSTypeFactory.getUndefinedType(registry)));
  }

  /**
   * Test getPropertyType - undefined.
   */
  @org.junit.Test
  public void testGetPropertyTypeUndefined() {
    RecordType recordType = new RecordType(registry, null);

    assertNull(recordType.getPropertyType("undefinedProperty"));
  }

  /**
   * Test hasProperty - undefined.
   */
  @org.junit.Test
  public void testHasPropertyUndefined() {
    RecordType recordType = new RecordType(registry, null);

    assertFalse(recordType.hasProperty("undefinedProperty"));
  }

  /**
   * Test defineProperty - undefined.
   */
  @org.junit.Test
  public void testDefinePropertyUndefined() {
    RecordType recordType = new RecordType(registry, null);

    try {
      recordType.defineProperty("undefinedProperty", registry.stringType(), false, null);
      fail();
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  /**
   * Test defineProperty - null.
   */
  @org.junit.Test
  public void testDefinePropertyNull() {
    RecordType recordType = new RecordType(registry, null);

    try {
      recordType.defineProperty(null, registry.stringType(), false, null);
      fail();
    } catch (NullPointerException e) {
      // Expected
    }
  }

  /**
   * Test getPropertyNode - undefined.
   */
  @org.junit.Test
  public void testGetPropertyNodeUndefined() {
    RecordType recordType = new RecordType(registry, null);

    assertNull(recordType.getPropertyNode("undefinedProperty"));
  }

  private RecordType createRecordType(String recordTypeString) {
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    SortedMap<String, RecordProperty> properties = new TreeMap<>();

    String[] propertyPairs = recordTypeString.substring(1, recordTypeString.length() - 1).split("\\s*,\\s*");

    for (String propertyPair : propertyPairs) {
      String[] propertyParts = propertyPair.split(":");
      properties.put(propertyParts[0], new RecordProperty(propertyParts[0], registry.getTypeByName(propertyParts[1])));
    }

    return builder.build(properties);
  }

  private JSType createUnionType(String... types) {
    UnionTypeBuilder builder = new UnionTypeBuilder(registry);

    for (String type : types) {
      builder.addAlternate(registry.getTypeByName(type));
    }

    return builder.build();
  }
}