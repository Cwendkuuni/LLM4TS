import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import static com.google.javascript.rhino.jstype.RecordTypeBuilder.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordType;

import java.util.Map;
import java.util.SortedMap;

/**
 * Test class for {@link RecordType}
 */
public class RecordTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    errorReporter = ErrorReporter.NULL;
  }

  @Test
  public void testCreateRecordType() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    RecordProperty property1 = new RecordProperty("stringProp", String);
    RecordProperty property2 = new RecordProperty("numberProp", Number);
    recordProperties.put("stringProp", property1);
    recordProperties.put("numberProp", property2);

    RecordType recordType = new RecordType(registry, recordProperties);
    assertEquals(recordType.getClass(), RecordType.class);

    SortedMap<String, JSType> expectedProperties = Maps.newTreeMap();
    expectedProperties.put("stringProp", String);
    expectedProperties.put("numberProp", Number);
    assertEquals(expectedProperties, recordType.properties);
  }

  @Test
  public void testIsEquivalentTo() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    RecordProperty property1 = new RecordProperty("stringProp", String);
    RecordProperty property2 = new RecordProperty("numberProp", Number);
    recordProperties.put("stringProp", property1);
    recordProperties.put("numberProp", property2);

    RecordType recordType1 = new RecordType(registry, recordProperties);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    RecordProperty property3 = new RecordProperty("booleanProp", Boolean);
    recordProperties2.put("booleanProp", property3);
    recordProperties2.putAll(recordProperties);

    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertFalse(recordType1.isEquivalentTo(recordType2));

    Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
    recordProperties3.putAll(recordProperties);
    recordProperties3.put("numberProp", Int);

    RecordType recordType3 = new RecordType(registry, recordProperties3);

    assertFalse(recordType1.isEquivalentTo(recordType3));

    RecordType recordType4 = new RecordType(registry, recordProperties);

    assertTrue(recordType1.isEquivalentTo(recordType4));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    RecordProperty property1 = new RecordProperty("stringProp", String);
    RecordProperty property2 = new RecordProperty("numberProp", Number);
    recordProperties.put("stringProp", property1);
    recordProperties.put("numberProp", property2);

    RecordType recordType1 = new RecordType(registry, recordProperties);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    RecordProperty property3 = new RecordProperty("booleanProp", Boolean);
    recordProperties2.put("booleanProp", property3);
    recordProperties2.put("stringProp", property1);
    recordProperties2.put("numberProp", Int);

    RecordType recordType2 = new RecordType(registry, recordProperties2);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertEquals(recordType2.getClass(), greatestSubtype.getClass());
  }

  @Test
  public void testIsSubtype() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    RecordProperty property1 = new RecordProperty("stringProp", String);
    RecordProperty property2 = new RecordProperty("numberProp", Number);
    recordProperties.put("stringProp", property1);
    recordProperties.put("numberProp", property2);

    RecordType recordType1 = new RecordType(registry, recordProperties);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    RecordProperty property3 = new RecordProperty("booleanProp", Boolean);
    recordProperties2.put("booleanProp", property3);
    recordProperties2.put("stringProp", property1);
    recordProperties2.put("numberProp", Int);

    RecordType recordType2 = new RecordType(registry, recordProperties2);

    Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
    recordProperties3.put("stringProp", property1);

    ObjectType objectType = registry.getNativeObjectType(ObjectType.NAME);

    // recordType1 is a subtype of recordType2
    assertTrue(recordType1.isSubtype(recordType2));

    // recordType1 is a subtype of ObjectType
    assertTrue(recordType1.isSubtype(objectType));

    // ObjectType is a subtype of recordType1
    assertTrue(objectType.isSubtype(recordType1));

    // recordType2 is not a subtype of recordType1 (due to different order of properties)
    assertFalse(recordType2.isSubtype(recordType1));
  }

  @Test
  public void testDefineProperty() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    RecordType recordType = new RecordType(registry, recordProperties);

    String propertyName = "testProperty";
    JSType propertyType = String;
    Node propertyNode = Node.NULL;

    // Define a property
    recordType.defineProperty(propertyName, propertyType, false, propertyNode);

    // Check if property exists
    assertTrue(recordType.hasProperty(propertyName));
    assertEquals(propertyType, recordType.getPropertyType(propertyName));

    // Try to define property again
    recordType.defineProperty(propertyName, propertyType, false, propertyNode);
    assertTrue(recordType.hasProperty(propertyName));
    assertEquals(propertyType, recordType.getPropertyType(propertyName));

    // Set property to frozen state
    recordType.isFrozen = true;

    // Try to define property again
    recordType.defineProperty(propertyName, propertyType, false, propertyNode);
    assertFalse(recordType.hasProperty(propertyName));
  }

  @Test(expected = IllegalStateException.class)
  public void testDefinePropertyWithNullPropertyRecord() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    RecordType recordType = new RecordType(registry, recordProperties);

    String propertyName = "testProperty";
    JSType propertyType = String;
    Node propertyNode = Node.NULL;

    RecordProperty property = new RecordProperty(propertyName, propertyType);

    // Try to define property with null property record
    recordType.defineProperty(propertyName, propertyType, false, propertyNode, null);
  }
}