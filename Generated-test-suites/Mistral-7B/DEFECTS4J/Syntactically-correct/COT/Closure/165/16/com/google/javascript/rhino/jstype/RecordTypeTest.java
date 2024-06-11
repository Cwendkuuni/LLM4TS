import static org.junit.Assert.*;
import com.google.javascript.rhino.*;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class RecordTypeTest {

 private static RecordType createRecordType(JSTypeRegistry registry, Map<String, RecordProperty> properties) {
 return new RecordType(registry, properties);
 }

 @Test
 public void testRecordTypeCreation() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("prop1", new RecordProperty("prop1", StringType.INSTANCE));
 RecordType recordType = createRecordType(registry, properties);
 assertEquals("{prop1: String}", recordType.toString());
 }

 @Test
 public void testIsEquivalentTo() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("prop1", new RecordProperty("prop1", StringType.INSTANCE));
 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("prop1", new RecordProperty("prop1", StringType.INSTANCE));
 properties2.put("prop2", new RecordProperty("prop2", NumberType.INSTANCE));

 RecordType recordType1 = createRecordType(registry, properties1);
 RecordType recordType2 = createRecordType(registry, properties2);
 assertTrue(recordType1.isEquivalentTo(recordType1));
 assertTrue(recordType1.isEquivalentTo(recordType2)); // Edge Case: Different instances of the same record type

 RecordType recordType3 = createRecordType(registry, properties1);
 RecordType recordType4 = new RecordType(registry, properties2); // Different constructor
 assertFalse(recordType3.isEquivalentTo(recordType4));
 }

 @Test
 public void testIsSubtype() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("prop1", new RecordProperty("prop1", StringType.INSTANCE));
 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("prop1", new RecordProperty("prop1", NumberType.INSTANCE));

 RecordType recordType1 = createRecordType(registry, properties1);
 RecordType recordType2 = createRecordType(registry, properties2);
 assertFalse(recordType1.isSubtype(recordType2)); // Different property types

 RecordType recordType3 = createRecordType(registry, properties1);
 RecordType recordType4 = new RecordType(registry, properties1);
 assertTrue(recordType3.isSubtype(recordType4)); // Equivalent record types

 RecordType recordType5 = new RecordType(registry, properties1);
 RecordType recordType6 = new RecordType(registry, properties2);
 assertFalse(recordType5.isSubtype(recordType6)); // Different record types

 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 RecordType recordType7 = createRecordType(registry, properties1);
 assertTrue(objectType.isSubtype(recordType7)); // Record type is a subtype of ObjectType
 }

 @Test
 public void testDefineProperty() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("prop1", new RecordProperty("prop1", StringType.INSTANCE));
 RecordType recordType = createRecordType(registry, properties);

 assertFalse(recordType.hasProperty("prop2"));
 recordType.defineProperty("prop2", StringType.INSTANCE, false, null);
 assertTrue(recordType.hasProperty("prop2"));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyFrozen() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("prop1", new RecordProperty("prop1", StringType.INSTANCE));
 RecordType recordType = createRecordType(registry, properties);
 recordType.isFrozen = true;
 recordType.defineProperty("prop2", StringType.INSTANCE, false, null);
 }

 @Test
 public void testGetImplicitPrototype() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("prop1", new RecordProperty("prop1", StringType.INSTANCE));
 RecordType recordType = createRecordType(registry, properties);
 ObjectType expectedPrototype = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 ObjectType actualPrototype = recordType.getImplicitPrototype();
 assertEquals(expectedPrototype, actualPrototype);
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> propertiesA = Maps.newHashMap();
 propertiesA.put("prop1", new RecordProperty("prop1", StringType.INSTANCE));
 RecordType recordTypeA = createRecordType(registry, propertiesA);

 Map<String, RecordProperty> propertiesB = Maps.newHashMap();
 propertiesB.put("prop1", new RecordProperty("prop1", StringType.INSTANCE));
 propertiesB.put("prop2", new RecordProperty("prop2", NumberType.INSTANCE));
 RecordType recordTypeB = createRecordType(registry, propertiesB);

 RecordType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
 ObjectType expected = registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE);
 ObjectType actual = greatestSubtype.getImplicitPrototype();
 assertEquals(expected, actual);
 }

 @Test
 public void testToString() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("prop1", new RecordProperty("prop1", StringType.INSTANCE));
 RecordType recordType = createRecordType(registry, properties);
 assertEquals("{prop1: String}", recordType.toString());
 }

}