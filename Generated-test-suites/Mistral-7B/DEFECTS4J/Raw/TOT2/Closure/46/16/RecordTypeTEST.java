import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties);
    assertEquals(recordType.getClass(), RecordType.class);
    assertEquals(recordType.getProperties().size(), 2);
    assertEquals(recordType.getProperties().keySet().toArray()[0], "name");
    assertEquals(recordType.getProperties().keySet().toArray()[1], "age");
  }

  @Test
  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = new java.util.LinkedHashMap<>();
    propertiesA.put("name", new RecordProperty("name", registry.getStringType()));
    propertiesA.put("age", new RecordProperty("age", registry.getNumberType()));

    Map<String, RecordProperty> propertiesB = new java.util.LinkedHashMap<>();
    propertiesB.put("name", new RecordProperty("name", registry.getStringType()));
    propertiesB.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertTrue(recordTypeA.isEquivalentTo(recordTypeB));
  }

  @Test
  public void testRecordTypeNonEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = new java.util.LinkedHashMap<>();
    propertiesA.put("name", new RecordProperty("name", registry.getStringType()));
    propertiesA.put("age", new RecordProperty("age", registry.getNumberType()));

    Map<String, RecordProperty> propertiesB = new java.util.LinkedHashMap<>();
    propertiesB.put("age", new RecordProperty("age", registry.getNumberType()));
    propertiesB.put("gender", new RecordProperty("gender", registry.getStringType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertFalse(recordTypeA.isEquivalentTo(recordTypeB));
  }

  @Test
  public void testRecordTypeInheritance() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> baseProperties = new java.util.LinkedHashMap<>();
    baseProperties.put("baseProperty", new RecordProperty("baseProperty", registry.getNumberType()));

    Map<String, RecordProperty> derivedProperties = new java.util.LinkedHashMap<>();
    derivedProperties.putAll(baseProperties);
    derivedProperties.put("derivedProperty", new RecordProperty("derivedProperty", registry.getStringType()));

    RecordType baseRecordType = new RecordType(registry, baseProperties);
    RecordType derivedRecordType = new RecordType(registry, derivedProperties);

    assertTrue(baseRecordType.isSubtype(derivedRecordType));
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("property", new RecordProperty("property", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties);
    recordType.defineProperty("property", registry.getStringType(), false, null);
  }

  @Test
  public void testRecordTypeLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordAProperties = new java.util.LinkedHashMap<>();
    recordAProperties.put("name", new RecordProperty("name", registry.getStringType()));
    recordAProperties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordA = new RecordType(registry, recordAProperties);

    Map<String, RecordProperty> recordBProperties = new java.util.LinkedHashMap<>();
    recordBProperties.put("name", new RecordProperty("name", registry.getStringType()));
    recordBProperties.put("age", new RecordProperty("age", registry.getNumberType()));
    recordBProperties.put("city", new RecordProperty("city", registry.getStringType()));

    RecordType recordB = new RecordType(registry, recordBProperties);

    JSType leastSupertype = recordA.getLeastSupertype(recordB);
    assertSame(leastSupertype.getClass(), RecordType.class);
    assertTrue(recordA.isSubtype(leastSupertype));
    assertTrue(recordB.isSubtype(leastSupertype));
  }

  @Test
  public void testRecordTypeGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordAProperties = new java.util.LinkedHashMap<>();
    recordAProperties.put("name", new RecordProperty("name", registry.getStringType()));
    recordAProperties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordA = new RecordType(registry, recordAProperties);

    Map<String, RecordProperty> recordBProperties = new java.util.LinkedHashMap<>();
    recordBProperties.put("age", new RecordProperty("age", registry.getNumberType()));
    recordBProperties.put("city", new RecordProperty("city", registry.getStringType()));

    Map<String, RecordProperty> recordCProperties = new java.util.LinkedHashMap<>();
    recordCProperties.put("city", new RecordProperty("city", registry.getStringType()));
    recordCProperties.put("country", new RecordProperty("country", registry.getStringType()));

    RecordType recordB = new RecordType(registry, recordBProperties);
    RecordType recordC = new RecordType(registry, recordCProperties);

    JSType greatestSubtype = recordA.getGreatestSubtype(recordB);
    assertSame(greatestSubtype.getClass(), RecordType.class);
    assertFalse(recordA.isSubtype(greatestSubtype));
    assertTrue(recordB.isSubtype(greatestSubtype));

    greatestSubtype = recordA.getGreatestSubtype(recordC);
    assertSame(greatestSubtype.getClass(), RecordType.class);
    assertFalse(recordA.isSubtype(greatestSubtype));
    assertFalse(recordB.isSubtype(greatestSubtype));
    assertTrue(recordC.isSubtype(greatestSubtype));
  }

  @Test
  public void testRecordTypeHasProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));

    RecordType recordType = new RecordType(registry, properties);

    assertTrue(recordType.hasProperty("name"));
  }

  @Test
  public void testRecordTypeNoProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));

    RecordType recordType = new RecordType(registry, properties);

    assertFalse(recordType.hasProperty("age"));
  }

  @Test
  public void testRecordTypePropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties);

    assertEquals(recordType.getPropertyType("name"), registry.getStringType());
    assertEquals(recordType.getPropertyType("age"), registry.getNumberType());
  }

  @Test
  public void testRecordTypePropertyNode() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties);

    Node nameNode = new Node(Node.IDENTIFIER, "name");
    Node ageNode = new Node(Node.IDENTIFIER, "age");

    assertEquals(recordType.getPropertyNode("name"), nameNode);
    assertEquals(recordType.getPropertyNode("age"), ageNode);
  }

  @Test
  public void testRecordTypeToString() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties);

    String expected = "{name: String, age: Number}";
    assertEquals(expected, recordType.toString());
  }

  @Test
  public void testRecordTypeNullPropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", null);

    try {
      new RecordType(registry, properties);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testRecordTypeNullPropertyNode() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));
    properties.put("city", new RecordProperty("city", registry.getStringType(), null));

    try {
      new RecordType(registry, properties);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testRecordTypeEmptyProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType recordType = new RecordType(registry, new java.util.HashMap<>());

    String expected = "{}";
    assertEquals(expected, recordType.toString());
  }

  @Test
  public void testRecordTypeEmptyRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType recordType = new RecordType(registry, new java.util.HashMap<>());

    assertNull(recordType.getProperties());
  }

  @Test
  public void testRecordTypeSamePropertiesInDifferentOrder() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = new java.util.LinkedHashMap<>();
    propertiesA.put("age", new RecordProperty("age", registry.getNumberType()));
    propertiesA.put("name", new RecordProperty("name", registry.getStringType()));

    Map<String, RecordProperty> propertiesB = new java.util.LinkedHashMap<>();
    propertiesB.put("name", new RecordProperty("name", registry.getStringType()));
    propertiesB.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertTrue(recordTypeA.isEquivalentTo(recordTypeB));
  }

  @Test
  public void testRecordTypeSubtypeOfEmptyRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));

    RecordType recordType = new RecordType(registry, properties);

    RecordType emptyRecordType = new RecordType(registry, new java.util.HashMap<>());

    assertTrue(recordType.isSubtype(emptyRecordType));
  }

  @Test
  public void testRecordTypeSubtypeOfComplexType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new java.util.LinkedHashMap<>();
    recordProperties.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, recordProperties);

    Map<String, RecordProperty> complexRecordProperties = new java.util.LinkedHashMap<>();
    complexRecordProperties.put("name", new RecordProperty("name", registry.getStringType()));
    complexRecordProperties.put("age", new RecordProperty("age", registry.getNumberType()));
    complexRecordProperties.put("occupation", new RecordProperty("occupation", registry.getStringType()));

    RecordType complexRecordType = new RecordType(registry, complexRecordProperties);

    assertTrue(recordType.isSubtype(complexRecordType));
  }

  @Test
  public void testRecordTypeAssignability() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new java.util.LinkedHashMap<>();
    recordProperties.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, recordProperties);

    Map<String, RecordProperty> complexRecordProperties = new java.util.LinkedHashMap<>();
    complexRecordProperties.put("name", new RecordProperty("name", registry.getStringType()));
    complexRecordProperties.put("age", new RecordProperty("age", registry.getNumberType()));
    complexRecordProperties.put("occupation", new RecordProperty("occupation", registry.getStringType()));

    RecordType complexRecordType = new RecordType(registry, complexRecordProperties);

    Object recordInstance = new Object() {
      String name;
      int age;
    };

    Object complexRecordInstance = new Object() {
      String name;
      int age;
      String occupation;
    };

    assertTrue(recordType.isAssignableFrom(recordType));
    assertTrue(recordType.isAssignableFrom(complexRecordType));

    assertTrue(recordType.isAssignableFrom(recordInstance));
    assertTrue(complexRecordType.isAssignableFrom(recordInstance));

    assertTrue(recordType.isAssignableFrom(complexRecordInstance));
    assertFalse(recordType.isAssignableFrom(new Object()));
  }
}