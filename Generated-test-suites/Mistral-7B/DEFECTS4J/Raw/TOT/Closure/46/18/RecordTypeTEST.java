import static org.junit.Assert.*;
import static com.google.javascript.rhino.testing.TypeMirrorTest.*;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.testing.TypeMirrorTest;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeTest extends TypeMirrorTest {

  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    registry.init(null, null);
  }

  @Override
  protected void tearDown() throws Exception {
    registry.dispose();
    super.tearDown();
  }

  /**
   * Test creating a new record type with no properties.
   */
  public void testCreateEmptyRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    RecordType recordType = new RecordType(registry, properties);
    assertNotNull(recordType);
    assertEquals("RecordType", recordType.getClassName());
    assertEquals(0, recordType.getPropertyCount());
  }

  /**
   * Test creating a record type with one property.
   */
  public void testCreateRecordTypeWithOneProperty() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", StringType.INSTANCE));
    RecordType recordType = new RecordType(registry, properties);
    assertNotNull(recordType);
    assertEquals("RecordType", recordType.getClassName());
    assertEquals(1, recordType.getPropertyCount());
    assertEquals(StringType.INSTANCE, recordType.getPropertyType("name"));
  }

  /**
   * Test creating a record type with multiple properties.
   */
  public void testCreateRecordTypeWithMultipleProperties() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", StringType.INSTANCE));
    properties.put("age", new RecordProperty("age", NumberType.INT));
    RecordType recordType = new RecordType(registry, properties);
    assertNotNull(recordType);
    assertEquals("RecordType", recordType.getClassName());
    assertEquals(2, recordType.getPropertyCount());
    assertEquals(StringType.INSTANCE, recordType.getPropertyType("name"));
    assertEquals(NumberType.INT, recordType.getPropertyType("age"));
  }

  /**
   * Test setting a property value on a record type.
   */
  public void testSetPropertyValue() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", StringType.INSTANCE));
    RecordType recordType = new RecordType(registry, properties);
    assertEquals(null, recordType.getProperty("name"));

    recordType.setProperty("name", "John");
    assertEquals("John", recordType.getProperty("name"));
  }

  /**
   * Test getting a property value from a record type.
   */
  public void testGetPropertyValue() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", StringType.INSTANCE));
    RecordType recordType = new RecordType(registry, properties);

    recordType.setProperty("name", "John");
    assertEquals("John", recordType.getProperty("name"));
  }

  /**
   * Test getting a non-existent property value from a record type.
   */
  public void testGetNonExistentPropertyValue() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", StringType.INSTANCE));
    RecordType recordType = new RecordType(registry, properties);

    assertNull(recordType.getProperty("age"));
  }

  /**
   * Test assigning a record type to a variable of its own type.
   */
  public void testAssignSelf() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", StringType.INSTANCE));
    RecordType recordTypeA = new RecordType(registry, properties);
    RecordType recordTypeB = new RecordType(registry, properties);

    recordTypeA = recordTypeB;
    assertTrue(recordTypeA.equals(recordTypeB));
  }

  /**
   * Test assigning a subtype record type to a variable of its super type.
   */
  public void testAssignSubtype() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("name", new RecordProperty("name", StringType.INSTANCE));
    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("name", new RecordProperty("name", StringType.INSTANCE));
    propertiesB.put("age", new RecordProperty("age", NumberType.INT));
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    recordTypeA = recordTypeB;
    assertTrue(recordTypeA.equals(recordTypeB));
  }

  /**
   * Test assigning a non-subtype record type to a variable of its super type.
   */
  public void testAssignNonSubtype() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("name", new RecordProperty("name", StringType.INSTANCE));
    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("age", new RecordProperty("age", NumberType.INT));
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    try {
      recordTypeA = recordTypeB;
      fail("Expected an IllegalArgumentException.");
    } catch (IllegalArgumentException e) {
      // Expected output.
    }
  }

  /**
   * Test checking if a record type is a subtype of another record type.
   */
  public void testIsSubtype() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("name", new RecordProperty("name", StringType.INSTANCE));
    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("name", new RecordProperty("name", StringType.INSTANCE));
    propertiesB.put("age", new RecordProperty("age", NumberType.INT));
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
  }

  /**
   * Test checking if a non-record type is a subtype of a record type.
   */
  public void testIsSubtypeNonRecord() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", StringType.INSTANCE));
    RecordType recordType = new RecordType(registry, properties);

    assertFalse(recordType.isSubtype(NumberType.INT));
  }

  /**
   * Test checking if a record type is a supertype of another record type.
   */
  public void testIsSupertype() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("name", new RecordProperty("name", StringType.INSTANCE));
    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("name", new RecordProperty("name", StringType.INSTANCE));
    propertiesB.put("age", new RecordProperty("age", NumberType.INT));
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertFalse(recordTypeB.isSupertype(recordTypeA));
  }

  /**
   * Test checking if a record type is a supertype of itself.
   */
  public void testIsSupertypeSelf() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", StringType.INSTANCE));
    RecordType recordType = new RecordType(registry, properties);

    assertTrue(recordType.isSupertype(recordType));
  }

  /**
   * Test getting the least common supertype of two record types.
   */
  public void testGetLeastCommonSupertype() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("name", new RecordProperty("name", StringType.INSTANCE));
    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("age", new RecordProperty("age", NumberType.INT));
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    RecordType lcs = recordTypeA.getLeastSupertype(recordTypeB);
    assertEquals("Object", lcs.getClassName());

    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("name", new RecordProperty("name", StringType.INSTANCE));
    propertiesC.put("age", new RecordProperty("age", NumberType.INT));
    RecordType recordTypeC = new RecordType(registry, propertiesC);

    RecordType lcs2 = recordTypeA.getLeastSupertype(recordTypeC);
    assertEquals("Object", lcs2.getClassName());

    RecordType lcs3 = recordTypeC.getLeastSupertype(recordTypeA);
    assertEquals("Object", lcs3.getClassName());

    Map<String, RecordProperty> propertiesD = Maps.newHashMap();
    propertiesD.put("name", new RecordProperty("name", StringType.INSTANCE));
    propertiesD.put("age", new RecordProperty("age", NumberType.INT));
    propertiesD.put("gender", new RecordProperty("gender", StringType.INSTANCE));
    RecordType recordTypeD = new RecordType(registry, propertiesD);

    RecordType lcs4 = recordTypeA.getLeastSupertype(recordTypeD);
    assertEquals("Object", lcs4.getClassName());
  }

  /**
   * Test getting the greatest subtype of a record type and a primitive type.
   */
  public void testGetGreatestSubtypePrimitive() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", StringType.INSTANCE));
    RecordType recordType = new RecordType(registry, properties);

    JSType greatestSubtype = recordType.getGreatestSubtype(NumberType.INT);
    assertEquals(NumberType.INT, greatestSubtype);
  }

  /**
   * Test getting the greatest subtype of a record type and another record type.
   */
  public void testGetGreatestSubtypeRecord() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("name", new RecordProperty("name", StringType.INSTANCE));
    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("name", new RecordProperty("name", StringType.INSTANCE));
    propertiesB.put("age", new RecordProperty("age", NumberType.INT));
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    RecordType greatestSubtype = recordTypeA.getGreatestSubtype(recordTypeB);
    assertEquals(recordTypeB, greatestSubtype);
  }

}