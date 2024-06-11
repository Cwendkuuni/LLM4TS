import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @Before
 public void setup() {
 registry = new JSTypeRegistry();
 RecordProperty recordProperty1 = new RecordProperty("prop1", intType());
 RecordProperty recordProperty2 = new RecordProperty("prop2", booleanType());
 Map<String, RecordProperty> recordProperties = Maps.newHashMap();
 recordProperties.put("prop1", recordProperty1);
 recordProperties.put("prop2", recordProperty2);
 recordType = new RecordType(registry, recordProperties, true);
 }

 @Test
 public void testRecordTypeConstructor() {
 // Test the constructor with valid arguments
 RecordType expectedRecordType = new RecordType(registry, recordProperties, true);
 assertEquals(expectedRecordType.getClass(), recordType.getClass());
 assertEquals(registry, recordType.getRegistry());
 assertTrue(recordType.isFrozen());
 assertEquals(recordProperties, recordType.getProperties());
 }

 @Test(expected = IllegalStateException.class)
 public void testRecordTypeConstructorWithNullRecordProperties() {
 Map<String, RecordProperty> nullRecordProperties = null;
 // Test the constructor with null record properties
 new RecordType(registry, nullRecordProperties, true);
 }

 @Test
 public void testIsSynthetic() {
 RecordProperty recordProperty = new RecordProperty("prop1", intType());
 Map<String, RecordProperty> recordProperties = Maps.newHashMap();
 recordProperties.put("prop1", recordProperty);
 RecordType recordType = new RecordType(registry, recordProperties, false);
 assertFalse(recordType.isSynthetic());

 RecordType synthesizedRecordType = new RecordType(registry, recordProperties, true);
 assertTrue(recordType.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 RecordProperty recordProperty1 = new RecordProperty("prop1", intType());
 RecordProperty recordProperty2 = new RecordProperty("prop1", intType());
 RecordProperty recordProperty3 = new RecordProperty("prop2", booleanType());
 Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
 recordProperties1.put("prop1", recordProperty1);
 recordProperties1.put("prop2", recordProperty3);
 Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
 recordProperties2.put("prop1", recordProperty1);
 recordProperties2.put("prop2", recordProperty2);
 RecordType recordType1 = new RecordType(registry, recordProperties1, true);
 RecordType recordType2 = new RecordType(registry, recordProperties2, true);

 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));
 assertTrue(recordType2.checkRecordEquivalenceHelper(recordType1, true));

 RecordType recordType3 = new RecordType(registry, recordProperties1, true);
 recordProperties1.put("prop2", recordProperty2);
 RecordType recordType4 = new RecordType(registry, recordProperties1, true);
 assertFalse(recordType3.checkRecordEquivalenceHelper(recordType4, true));
 assertFalse(recordType4.checkRecordEquivalenceHelper(recordType3, true));
 }

 @Test
 public void testGetImplicitPrototype() {
 ObjectType expectedImplicitPrototype = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 ObjectType actualImplicitPrototype = recordType.getImplicitPrototype();
 assertEquals(expectedImplicitPrototype, actualImplicitPrototype);
 }

 @Test
 public void testDefineProperty() {
 String propertyName = "property";
 JSType propertyType = stringType();
 Node propertyNode = new TextNode("propertyName");

 // Test defining a property when the record is not frozen
 recordType.defineProperty(propertyName, propertyType, false, propertyNode);
 assertTrue(recordType.hasProperty(propertyName));
 assertEquals(propertyType, recordType.getPropertyType(propertyName));
 assertEquals(propertyNode, recordType.getPropertyNode(propertyName));

 // Test defining a property when the record is frozen
 recordType.isFrozen = true;
 assertFalse(recordType.defineProperty(propertyName, propertyType, false, propertyNode));
 assertFalse(recordType.hasProperty(propertyName));
 assertNull(recordType.getPropertyType(propertyName));
 assertNull(recordType.getPropertyNode(propertyName));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 // Test the greatest subtype helper with two empty record types
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap(), true);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap(), true);
 JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 assertNull(greatestSubtype);

 // Test the greatest subtype helper with two non-empty record types
 RecordProperty recordProperty1 = new RecordProperty("prop1", intType());
 RecordProperty recordProperty2 = new RecordProperty("prop2", booleanType());
 RecordProperty recordProperty3 = new RecordProperty("prop3", stringType());
 Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
 recordProperties1.put("prop1", recordProperty1);
 recordProperties1.put("prop3", recordProperty3);
 Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
 recordProperties2.put("prop2", recordProperty2);
 recordProperties2.put("prop3", recordProperty3);
 RecordType recordType1 = new RecordType(registry, recordProperties1, true);
 RecordType recordType2 = new RecordType(registry, recordProperties2, true);
 JSType greatestSubtypeExpected = new RecordType(registry, recordProperties1, true);
 greatestSubtypeExpected.getProperties().put("prop2", nullType());
 JSType greatestSubtypeActual = recordType1.getGreatestSubtypeHelper(recordType2);
 assertEquals(greatestSubtypeExpected, greatestSubtypeActual);

 // Test the greatest subtype helper with a record type and a primitive type
 JSType primitiveType = intType();
 JSType greatestSubtypeActual = recordType1.getGreatestSubtypeHelper(primitiveType);
 assertNull(greatestSubtypeActual);

 // Test the greatest subtype helper with a record type and an object type
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 JSType greatestSubtypeActual = recordType1.getGreatestSubtypeHelper(objectType);
 assertEquals(objectType, greatestSubtypeActual);

 // Test the greatest subtype helper with a record type and a record type with a
 // common property
 RecordProperty recordProperty4 = new RecordProperty("prop4", intType());
 Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
 recordProperties3.putAll(recordProperties1);
 recordProperties3.put("prop4", recordProperty4);
 RecordType recordType3 = new RecordType(registry, recordProperties3, true);
 JSType greatestSubtypeExpected = recordType1.getGreatestSubtypeHelper(recordType3);
 assertEquals(greatestSubtypeExpected, recordType1);
 }

 @Test
 public void testIsSubtype() {
 // Test isSubtype with two empty record types
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap(), true);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap(), true);
 assertTrue(recordType1.isSubtype(recordType2));
 assertTrue(recordType2.isSubtype(recordType1));

 // Test isSubtype with a record type and a primitive type
 JSType primitiveType = intType();
 assertFalse(recordType1.isSubtype(primitiveType));
 assertFalse(primitiveType.isSubtype(recordType1));

 // Test isSubtype with a record type and an object type
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 assertTrue(recordType1.isSubtype(objectType));
 assertFalse(objectType.isSubtype(recordType1));

 // Test isSubtype with a record type and another record type with a common property
 RecordProperty recordProperty4 = new RecordProperty("prop4", intType());
 Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
 recordProperties3.putAll(recordProperties1);
 recordProperties3.put("prop4", recordProperty4);
 RecordType recordType3 = new RecordType(registry, recordProperties3, true);
 assertTrue(recordType1.isSubtype(recordType3));
 assertTrue(recordType3.isSubtype(recordType1));

 // Test isSubtype with a record type and another record type with incompatible properties
 RecordProperty recordProperty5 = new RecordProperty("prop5", booleanType());
 Map<String, RecordProperty> recordProperties4 = Maps.newHashMap();
 recordProperties4.putAll(recordProperties1);
 recordProperties4.put("prop5", recordProperty5);
 RecordType recordType4 = new RecordType(registry, recordProperties4, true);
 assertFalse(recordType1.isSubtype(recordType4));
 assertFalse(recordType4.isSubtype(recordType1));
 }

 @Test
 public void testToMaybeRecordType() {
 RecordType recordType = new RecordType(registry, recordProperties, true);
 assertEquals(recordType, recordType.toMaybeRecordType());
 }
}