import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;

import static org.junit.Assert.*;

public class RecordTypeTest {

  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testCreateRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties);
    assertEquals("Expected record type", recordType.getClass(), RecordType.class);

    // Test getting properties
    assertEquals("Expected property count", 2, recordType.getPropertyCount());
    assertEquals("Expected property name", "name", recordType.getPropertyNames()[0]);
    assertEquals("Expected property name", "age", recordType.getPropertyNames()[1]);

    // Test getting property types
    assertEquals("Expected string type for name property", registry.getStringType(), recordType.getPropertyType("name"));
    assertEquals("Expected number type for age property", registry.getNumberType(), recordType.getPropertyType("age"));

    // Test property access
    JSType propertyValue = recordType.getPropertyType("name");
    assertNotNull("Expected non-null property value", propertyValue);
    assertEquals("Expected string type for name property value", registry.getStringType(), propertyValue);

    propertyValue = recordType.getPropertyType("age");
    assertNotNull("Expected non-null property value", propertyValue);
    assertEquals("Expected number type for age property value", registry.getNumberType(), propertyValue);
  }

  @Test
  public void testIsSubtype() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("name", new RecordProperty("name", registry.getStringType()));
    propertiesA.put("age", new RecordProperty("age", registry.getNumberType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("name", new RecordProperty("name", registry.getStringType()));
    propertiesB.put("age", new RecordProperty("age", registry.getNumberType()));
    propertiesB.put("city", new RecordProperty("city", registry.getStringType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    // Test isSubtype with identical record types
    assertTrue("Expected true for identical record types", recordTypeA.isSubtype(recordTypeA));
    assertTrue("Expected true for identical record types", recordTypeA.isSubtype(recordTypeB));
    assertTrue("Expected true for identical record types", recordTypeB.isSubtype(recordTypeA));
    assertTrue("Expected true for identical record types", recordTypeB.isSubtype(recordTypeB));

    // Test isSubtype with record type A as supertype
    RecordType recordTypeC = new RecordType(registry, propertiesB);
    assertTrue("Expected true for record type B as subtype of record type A", recordTypeB.isSubtype(recordTypeC));

    // Test isSubtype with record type B as supertype
    RecordType recordTypeD = new RecordType(registry, propertiesA);
    assertTrue("Expected true for record type A as subtype of record type B", recordTypeA.isSubtype(recordTypeD));

    // Test isSubtype with different record types
    RecordType recordTypeE = new RecordType(registry, Map.of("x", registry.getStringType()));
    assertFalse("Expected false for different record types", recordTypeA.isSubtype(recordTypeE));
  }

  @Test
  public void testIsEquivalentTo() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("name", new RecordProperty("name", registry.getStringType()));
    propertiesA.put("age", new RecordProperty("age", registry.getNumberType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("name", new RecordProperty("name", registry.getStringType()));
    propertiesB.put("age", new RecordProperty("age", registry.getNumberType()));
    propertiesB.put("city", new RecordProperty("city", registry.getStringType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    // Test isEquivalentTo with identical record types
    assertTrue("Expected true for identical record types", recordTypeA.isEquivalentTo(recordTypeA));
    assertTrue("Expected true for identical record types", recordTypeA.isEquivalentTo(recordTypeB));
    assertTrue("Expected true for identical record types", recordTypeB.isEquivalentTo(recordTypeA));
    assertTrue("Expected true for identical record types", recordTypeB.isEquivalentTo(recordTypeB));

    // Test isEquivalentTo with different record types
    RecordType recordTypeC = new RecordType(registry, Map.of("x", registry.getStringType()));
    assertFalse("Expected false for different record types", recordTypeA.isEquivalentTo(recordTypeC));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("name", new RecordProperty("name", registry.getStringType()));
    propertiesA.put("age", new RecordProperty("age", registry.getNumberType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("name", new RecordProperty("name", registry.getStringType()));
    propertiesB.put("age", new RecordProperty("age", registry.getNumberType()));
    propertiesB.put("city", new RecordProperty("city", registry.getStringType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    // Test getGreatestSubtypeHelper with identical record types
    JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeA);
    assertEquals("Expected record type as greatest subtype", recordTypeA.getClass(), greatestSubtype.getClass());
    assertEquals("Expected record type as greatest subtype", recordTypeA, greatestSubtype);

    greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
    assertEquals("Expected record type as greatest subtype", recordTypeA.getClass(), greatestSubtype.getClass());
    assertEquals("Expected record type as greatest subtype", recordTypeA, greatestSubtype);

    greatestSubtype = recordTypeB.getGreatestSubtypeHelper(recordTypeA);
    assertEquals("Expected record type as greatest subtype", recordTypeA.getClass(), greatestSubtype.getClass());
    assertEquals("Expected record type as greatest subtype", recordTypeA, greatestSubtype);

    greatestSubtype = recordTypeB.getGreatestSubtypeHelper(recordTypeB);
    assertEquals("Expected record type as greatest subtype", recordTypeB.getClass(), greatestSubtype.getClass());
    assertEquals("Expected record type as greatest subtype", recordTypeB, greatestSubtype);

    // Test getGreatestSubtypeHelper with record type A as supertype
    JSType recordTypeC = new RecordType(registry, Map.of("x", registry.getStringType()));
    greatestSubtype = recordTypeB.getGreatestSubtypeHelper(recordTypeC);
    assertEquals("Expected NO_TYPE as greatest subtype", registry.getNativeObjectType(JSTypeNative.NO_TYPE).getClass(), greatestSubtype.getClass());

    // Test getGreatestSubtypeHelper with record type A as subtype
    greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
    assertEquals("Expected record type as greatest subtype", recordTypeA.getClass(), greatestSubtype.getClass());
    assertEquals("Expected record type as greatest subtype", recordTypeA, greatestSubtype);

    // Test getGreatestSubtypeHelper with object type as supertype
    greatestSubtype = recordTypeA.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    assertEquals("Expected record type as greatest subtype", recordTypeA.getClass(), greatestSubtype.getClass());
    assertEquals("Expected record type as greatest subtype", recordTypeA, greatestSubtype);
  }

  @Test
  public void testToString() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties);

    String recordTypeString = recordType.toString();
    String expectedRecordTypeString =
        "RecordType{name=String, age=Number}";
    assertEquals("Expected toString to return expected string", expectedRecordTypeString, recordTypeString);
  }
}