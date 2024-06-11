import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
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

  private static JSTypeRegistry registry;

  @org.junit.BeforeClass
  public static void setUp() {
    registry = new JSTypeRegistry();
  }

  /**
   * Test case for constructing a record type with known property types.
   */
  @Test
  public void testRecordTypeWithKnownProperties() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("a", new RecordProperty("a", registry.getStringType()));
    recordProperties.put("b", new RecordProperty("b", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    assertEquals("RecordType{declared=true, properties=[a=String, b=Number]}",
        recordType.toString());

    // Test property access
    assertEquals(registry.getStringType(), recordType.getPropertyType("a"));
    assertEquals(registry.getNumberType(), recordType.getPropertyType("b"));

    // Test property values
    Object obj = recordType.createInstance(registry, new Object());
    Node aNode = registry.newTextNode("test");
    Node bNode = registry.newNumberNode(123);

    recordType.setProperty(obj, "a", aNode);
    recordType.setProperty(obj, "b", bNode);

    assertEquals("test", recordType.getProperty(obj, "a").toString());
    assertEquals(123, recordType.getProperty(obj, "b").toNumber().doubleValue(), 0.0);
  }

  /**
   * Test case for constructing a record type with unknown property types.
   */
  @Test
  public void testRecordTypeWithUnknownProperties() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("a", new RecordProperty("a", null));
    recordProperties.put("b", new RecordProperty("b", null));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    assertEquals("RecordType{declared=true, properties=[a=UnknownType, b=UnknownType]}",
        recordType.toString());

    // Test getPropertyType with unknown properties
    assertNull(recordType.getPropertyType("a"));
    assertNull(recordType.getPropertyType("b"));

    // Test setProperty with unknown properties
    Object obj = recordType.createInstance(registry, new Object());
    Node aNode = registry.newTextNode("test");
    Node bNode = registry.newNumberNode(123);

    recordType.setProperty(obj, "a", aNode);
    recordType.setProperty(obj, "b", bNode);

    assertNotNull(recordType.getProperty(obj, "a"));
    assertNotNull(recordType.getProperty(obj, "b"));

    // Test getPropertyType after setProperty
    assertEquals(registry.getStringType(), recordType.getPropertyType("a"));
    assertEquals(registry.getNumberType(), recordType.getPropertyType("b"));
  }

  /**
   * Test case for checking record type equivalence.
   */
  @Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("a", new RecordProperty("a", registry.getStringType()));
    recordProperties1.put("b", new RecordProperty("b", registry.getNumberType()));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("a", new RecordProperty("a", registry.getStringType()));
    recordProperties2.put("b", new RecordProperty("b", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));

    // Test incompatible record types
    Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
    recordProperties3.put("a", new RecordProperty("a", registry.getStringType()));
    recordProperties3.put("c", new RecordProperty("c", registry.getNumberType()));

    RecordType recordType3 = new RecordType(registry, recordProperties3, true);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, true));
  }

  /**
   * Test case for checking record subtyping.
   */
  @Test
  public void testRecordSubtyping() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("a", new RecordProperty("a", registry.getStringType()));
    recordProperties1.put("b", new RecordProperty("b", registry.getNumberType()));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("a", new RecordProperty("a", registry.getStringType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    assertTrue(recordType1.isSubtype(recordType2));

    // Test a subtype with an extra property
    Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
    recordProperties3.putAll(recordProperties1);
    recordProperties3.put("c", new RecordProperty("c", registry.getNumberType()));

    RecordType recordType3 = new RecordType(registry, recordProperties3, true);

    assertTrue(recordType1.isSubtype(recordType3));

    // Test a supertype with fewer properties
    Map<String, RecordProperty> recordProperties4 = Maps.newHashMap();
    recordProperties4.put("a", new RecordProperty("a", registry.getStringType()));

    RecordType recordType4 = new RecordType(registry, recordProperties4, true);

    assertTrue(recordType4.isSubtype(recordType1));

    // Test a subtype with different property types
    Map<String, RecordProperty> recordProperties5 = Maps.newHashMap();
    recordProperties5.put("a", new RecordProperty("a", registry.getNumberType()));

    RecordType recordType5 = new RecordType(registry, recordProperties5, true);

    assertFalse(recordType1.isSubtype(recordType5));

    // Test a subtype with null properties
    Map<String, RecordProperty> recordProperties6 = Maps.newHashMap();
    recordProperties6.put("a", new RecordProperty("a", null));

    RecordType recordType6 = new RecordType(registry, recordProperties6, true);

    assertFalse(recordType1.isSubtype(recordType6));
  }
}