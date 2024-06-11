import static org.junit.Assert.*;
import static com.google.javascript.jscomp.type.TypeUtils.*;

import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeFactory;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.TypeFactory;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.TypeUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordTypeTest {

  private static JSTypeRegistry registry;

  @org.junit.BeforeClass
  public static void setup() {
    registry = new JSTypeRegistry();
  }

  /**
   * Test record type creation with valid properties.
   */
  @org.junit.Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", stringType));
    properties.put("age", new RecordProperty("age", numberType));
    RecordType recordType = new RecordType(registry, properties);
    assertEquals(recordType.getClassName(), "RecordType");
    assertEquals(recordType.getPrettyName(), "RecordType { name: string, age: number }");
    assertEquals(recordType.isRecordType(), true);

    // Test getPropertyType and getPropertyNode
    assertEquals(recordType.getPropertyType("name"), stringType);
    assertEquals(recordType.getPropertyNode("name"), null);
    assertEquals(recordType.getPropertyType("age"), numberType);
    assertEquals(recordType.getPropertyNode("age"), null);

    // Test isEquivalentTo
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    assertTrue(recordType1.isEquivalentTo(recordType2));

    // Test isSubtype
    RecordType recordType3 = new RecordType(registry, new HashMap<String, RecordProperty>() {{
      put("name", new RecordProperty("name", stringType));
      put("age", new RecordProperty("age", numberType));
      put("city", new RecordProperty("city", stringType));
    }});
    assertTrue(recordType.isSubtype(recordType1));
    assertFalse(recordType.isSubtype(recordType3));

    // Test getLeastSupertype
    JSType leastSupertype = recordType.getLeastSupertype(stringType);
    assertTrue(leastSupertype.isRecordType());
    assertEquals(leastSupertype.getPropertyType("name"), stringType);
    assertEquals(leastSupertype.getPropertyType("age"), null);

    leastSupertype = recordType.getLeastSupertype(numberType);
    assertTrue(leastSupertype.isRecordType());
    assertEquals(leastSupertype.getPropertyType("name"), null);
    assertEquals(leastSupertype.getPropertyType("age"), numberType);

    leastSupertype = recordType.getLeastSupertype(recordType1);
    assertTrue(leastSupertype.isRecordType());
    assertEquals(leastSupertype.getPropertyType("name"), stringType);
    assertEquals(leastSupertype.getPropertyType("age"), numberType);
    assertEquals(leastSupertype.getPropertyType("city"), null);

    leastSupertype = recordType.getLeastSupertype(recordType3);
    assertTrue(leastSupertype.isRecordType());
    assertEquals(leastSupertype.getPropertyType("name"), stringType);
    assertEquals(leastSupertype.getPropertyType("age"), numberType);
    assertEquals(leastSupertype.getPropertyType("city"), stringType);

    // Test getGreatestSubtype
    JSType greatestSubtype = recordType.getGreatestSubtype(recordType1);
    assertTrue(greatestSubtype.isRecordType());
    assertEquals(greatestSubtype.getPropertyType("name"), stringType);
    assertEquals(greatestSubtype.getPropertyType("age"), numberType);
    assertEquals(greatestSubtype.getPropertyType("city"), null);

    greatestSubtype = recordType.getGreatestSubtype(recordType3);
    assertTrue(greatestSubtype.isRecordType());
    assertEquals(greatestSubtype.getPropertyType("name"), stringType);
    assertEquals(greatestSubtype.getPropertyType("age"), numberType);
    assertEquals(greatestSubtype.getPropertyType("city"), stringType);

    // Test defineProperty
    RecordType recordType4 = new RecordType(registry, properties);
    assertFalse(recordType4.hasProperty("city"));
    recordType4.defineProperty("city", stringType, false, null);
    assertTrue(recordType4.hasProperty("city"));
    assertEquals(recordType4.getPropertyType("city"), stringType);

    // Test isFrozen
    assertTrue(recordType.isFrozen());
    recordType4.defineProperty("age", booleanType, false, null);
    assertTrue(recordType4.isFrozen());
  }

  /**
   * Test record type creation with invalid properties.
   */
  @org.junit.Test
  public void testRecordTypeCreationInvalidProperties() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", null);
    RecordType recordType = null;
    try {
      recordType = new RecordType(registry, properties);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
    assertEquals(recordType, null);

    properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", stringType));
    properties.put("age", null);
    try {
      recordType = new RecordType(registry, properties);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
    assertEquals(recordType, null);
  }

  /**
   * Test record type subtyping.
   */
  @org.junit.Test
  public void testRecordTypeSubtyping() {
    Map<String, RecordProperty> baseProperties = new HashMap<>();
    baseProperties.put("name", new RecordProperty("name", stringType));

    Map<String, RecordProperty> derivedProperties = new HashMap<>();
    derivedProperties.put("name", new RecordProperty("name", stringType));
    derivedProperties.put("age", new RecordProperty("age", numberType));

    RecordType baseType = new RecordType(registry, baseProperties);
    RecordType derivedType = new RecordType(registry, derivedProperties);

    // Test isSubtype
    assertTrue(baseType.isSubtype(derivedType));

    // Test getLeastSupertype
    JSType leastSupertype = baseType.getLeastSupertype(derivedType);
    assertTrue(leastSupertype.isRecordType());
    assertEquals(leastSupertype.getPropertyType("name"), stringType);
    assertEquals(leastSupertype.getPropertyType("age"), numberType);

    // Test getGreatestSubtype
    JSType greatestSubtype = derivedType.getGreatestSubtype(baseType);
    assertTrue(greatestSubtype.isRecordType());
    assertEquals(greatestSubtype.getPropertyType("name"), stringType);
    assertEquals(greatestSubtype.getPropertyType("age"), numberType);
  }

  /**
   * Test record type with empty property map.
   */
  @org.junit.Test
  public void testRecordTypeEmptyProperties() {
    Map<String, RecordProperty> properties = new HashMap<>();
    RecordType recordType = new RecordType(registry, properties);
    assertTrue(recordType.isRecordType());
    assertEquals(recordType.getClassName(), "RecordType");
    assertEquals(recordType.getPrettyName(), "RecordType {}", true);
    assertEquals(recordType.getPropertyType("name"), null);
    assertEquals(recordType.getPropertyType("age"), null);
    assertFalse(recordType.hasProperty("name"));
    assertFalse(recordType.hasProperty("age"));

    // Test isEquivalentTo
    RecordType recordType1 = new RecordType(registry, properties);
    assertTrue(recordType.isEquivalentTo(recordType1));

    // Test isSubtype
    assertTrue(recordType.isSubtype(recordType));

    // Test getLeastSupertype
    JSType leastSupertype = recordType.getLeastSupertype(stringType);
    assertTrue(leastSupertype.isRecordType());
    assertEquals(leastSupertype.getPropertyType("name"), null);
    assertEquals(leastSupertype.getPropertyType("age"), null);

    leastSupertype = recordType.getLeastSupertype(numberType);
    assertTrue(leastSupertype.isRecordType());
    assertEquals(leastSupertype.getPropertyType("name"), null);
    assertEquals(leastSupertype.getPropertyType("age"), null);

    leastSupertype = recordType.getLeastSupertype(recordType1);
    assertTrue(leastSupertype.isRecordType());
    assertEquals(leastSupertype.getPropertyType("name"), null);
    assertEquals(leastSupertype.getPropertyType("age"), null);

    // Test getGreatestSubtype
    JSType greatestSubtype = recordType.getGreatestSubtype(recordType1);
    assertTrue(greatestSubtype.isRecordType());
    assertEquals(greatestSubtype.getPropertyType("name"), null);
    assertEquals(greatestSubtype.getPropertyType("age"), null);
  }

  /**
   * Test record type with conflicting property types.
   */
  @org.junit.Test
  public void testRecordTypeConflictingPropertyTypes() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", stringType));
    properties.put("name", new RecordProperty("name", numberType));
    RecordType recordType = null;
    try {
      recordType = new RecordType(registry, properties);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
    assertEquals(recordType, null);
  }

  /**
   * Test record type with conflicting property names.
   */
  @org.junit.Test
  public void testRecordTypeConflictingPropertyNames() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", stringType));
    properties.put("age", new RecordProperty("name", numberType));
    RecordType recordType = null;
    try {
      recordType = new RecordType(registry, properties);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
    assertEquals(recordType, null);
  }

  /**
   * Test record type with duplicate property names.
   */
  @org.junit.Test
  public void testRecordTypeDuplicatePropertyNames() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", stringType));
    properties.put("name", new RecordProperty("name", numberType));
    RecordType recordType = null;
    try {
      recordType = new RecordType(registry, properties);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
    assertEquals(recordType, null);
  }

  /**
   * Test record type with missing property types.
   */
  @org.junit.Test
  public void testRecordTypeMissingPropertyTypes() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", null));
    RecordType recordType = null;
    try {
      recordType = new RecordType(registry, properties);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
    assertEquals(recordType, null);
  }

  /**
   * Test record type with missing property nodes.
   */
  @org.junit.Test
  public void testRecordTypeMissingPropertyNodes() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", stringType));
    properties.put("age", null);
    RecordType recordType = null;
    try {
      recordType = new RecordType(registry, properties);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
    assertEquals(recordType, null);
  }

  /**
   * Test record type with invalid property nodes.
   */
  @org.junit.Test
  public void testRecordTypeInvalidPropertyNodes() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", stringType));
    properties.put("age", new Node(123));
    RecordType recordType = null;
    try {
      recordType = new RecordType(registry, properties);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
    assertEquals(recordType, null);
  }

  /**
   * Test record type with nested record type.
   */
  @org.junit.Test
  public void testRecordTypeNestedRecordType() {
    Map<String, RecordProperty> outerProperties = new HashMap<>();
    outerProperties.put("name", new RecordProperty("name", stringType));
    outerProperties.put("age", new RecordProperty("age", numberType));
    outerProperties.put("address", new RecordProperty("address", new RecordTypeBuilder(registry)
        .addProperty("street", stringType)
        .addProperty("city", stringType)
        .addProperty("state", stringType)
        .addProperty("zip", numberType)
        .build()));

    RecordType outerRecordType = new RecordType(registry, outerProperties);
    assertEquals(outerRecordType.getPropertyType("name"), stringType);
    assertEquals(outerRecordType.getPropertyType("age"), numberType);
    assertEquals(outerRecordType.getPropertyType("address.street"), stringType);
    assertEquals(outerRecordType.getPropertyType("address.city"), stringType);
    assertEquals(outerRecordType.getPropertyType("address.state"), stringType);
    assertEquals(outerRecordType.getPropertyType("address.zip"), numberType);

    // Test isSubtype
    RecordType innerRecordType = new RecordType(registry, Arrays.asList("street", "city")
        .stream()
        .collect(Collectors.toMap(s -> s, s -> new RecordProperty(s, stringType))));
    assertTrue(outerRecordType.isSubtype(innerRecordType));

    // Test getGreatestSubtype
    JSType greatestSubtype = outerRecordType.getGreatestSubtype(innerRecordType);
    assertTrue(greatestSubtype.isRecordType());
    assertEquals(greatestSubtype.getPropertyType("name"), stringType);
    assertEquals(greatestSubtype.getPropertyType("age"), numberType);
    assertEquals(greatestSubtype.getPropertyType("address.street"), stringType);
    assertEquals(greatestSubtype.getPropertyType("address.city"), stringType);
    assertEquals(greatestSubtype.getPropertyType("address.state"), null);
    assertEquals(greatestSubtype.getPropertyType("address.zip"), null);
  }
}