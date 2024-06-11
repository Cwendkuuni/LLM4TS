import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

 @Test
 public void testRecordTypeCreation() {
 JSTypeRegistry registry = new JSTypeRegistry();

 // Create a record type with two properties, "a" and "b"
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", new RecordProperty("a", registry.getNumberType()));
 properties.put("b", new RecordProperty("b", registry.getStringType()));

 RecordType recordType = new RecordType(registry, properties);

 // Test the record type's properties
 assertEquals("RecordType with two properties: a and b", 2, recordType.getNumProperties());
 assertEquals("Property 'a' is of type Number", registry.getNumberType(), recordType.getPropertyType("a"));
 assertEquals("Property 'b' is of type String", registry.getStringType(), recordType.getPropertyType("b"));
 }

 @Test
 public void testRecordTypeSubtyping() {
 JSTypeRegistry registry = new JSTypeRegistry();

 // Create a base record type with properties "a" and "b"
 Map<String, RecordProperty> baseProperties = Maps.newHashMap();
 baseProperties.put("a", new RecordProperty("a", registry.getNumberType()));
 baseProperties.put("b", new RecordProperty("b", registry.getStringType()));
 RecordType baseRecordType = new RecordType(registry, baseProperties);

 // Create a derived record type that adds a new property "c"
 Map<String, RecordProperty> derivedProperties = Maps.newHashMap();
 derivedProperties.putAll(baseProperties);
 derivedProperties.put("c", new RecordProperty("c", registry.getBooleanType()));
 RecordType derivedRecordType = new RecordType(registry, derivedProperties);

 // Check that the derived record is a subtype of the base record
 assertTrue("Derived record is a subtype of the base record", derivedRecordType.isSubtype(baseRecordType));
 }

 @Test
 public void testRecordTypeEquivalence() {
 JSTypeRegistry registry = new JSTypeRegistry();

 // Create two record types with the same properties
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("a", new RecordProperty("a", registry.getNumberType()));
 properties1.put("b", new RecordProperty("b", registry.getStringType()));
 RecordType recordType1 = new RecordType(registry, properties1);

 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("a", new RecordProperty("a", registry.getNumberType()));
 properties2.put("b", new RecordProperty("b", registry.getStringType()));
 RecordType recordType2 = new RecordType(registry, properties2);

 // Check that the record types are equivalent
 assertTrue("Record types are equivalent", recordType1.isEquivalentTo(recordType2));
 }

 @Test
 public void testRecordTypeFrozen() {
 JSTypeRegistry registry = new JSTypeRegistry();

 // Create a record type with two properties, "a" and "b"
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", new RecordProperty("a", registry.getNumberType()));
 properties.put("b", new RecordProperty("b", registry.getStringType()));
 RecordType recordType = new RecordType(registry, properties);
 recordType.freeze();

 // Try to modify the record type's properties
 try {
 recordType.defineProperty("a", registry.getStringType(), false, null);
 fail("Failed to throw an exception when trying to modify a frozen record type's property");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testRecordTypePropertyAccess() {
 JSTypeRegistry registry = new JSTypeRegistry();

 // Create a record type with two properties, "a" and "b"
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", new RecordProperty("a", registry.getNumberType()));
 properties.put("b", new RecordProperty("b", registry.getStringType()));
 RecordType recordType = new RecordType(registry, properties);

 // Accessing properties using their names
 assertEquals("Property 'a' value is 0", 0, recordType.getNumberProperty("a").getValue(Node.UNDEFINED).toNumber());
 assertEquals("Property 'b' value is ''", "", recordType.getStringProperty("b").getValue(Node.UNDEFINED).toString());

 // Accessing properties using Node
 Node propertyANode = Node.newProperty("a");
 Node propertyBNode = Node.newProperty("b");
 assertEquals("Property 'a' value is 0", 0, recordType.getMember(propertyANode).toNumber());
 assertEquals("Property 'b' value is ''", "", recordType.getMember(propertyBNode).toString());
 }

 @Test
 public void testRecordTypeLeastSupertype() {
 JSTypeRegistry registry = new JSTypeRegistry();

 // Create a base record type with properties "a" and "b"
 Map<String, RecordProperty> baseProperties = Maps.newHashMap();
 baseProperties.put("a", new RecordProperty("a", registry.getNumberType()));
 baseProperties.put("b", new RecordProperty("b", registry.getStringType()));
 RecordType baseRecordType = new RecordType(registry, baseProperties);

 // Create a derived record type that adds a new property "c"
 Map<String, RecordProperty> derivedProperties = Maps.newHashMap();
 derivedProperties.putAll(baseProperties);
 derivedProperties.put("c", new RecordProperty("c", registry.getBooleanType()));
 RecordType derivedRecordType = new RecordType(registry, derivedProperties);

 // Test the least supertype between the base and derived record types
 JSType leastSupertype = baseRecordType.getLeastSupertype(derivedRecordType);
 assertTrue("Least supertype is the derived record type", derivedRecordType.isEquivalentTo(leastSupertype));
 }

 @Test
 public void testRecordTypeGreatestSubtype() {
 JSTypeRegistry registry = new JSTypeRegistry();

 // Create a base record type with properties "a" and "b"
 Map<String, RecordProperty> baseProperties = Maps.newHashMap();
 baseProperties.put("a", new RecordProperty("a", registry.getNumberType()));
 baseProperties.put("b", new RecordProperty("b", registry.getStringType()));
 RecordType baseRecordType = new RecordType(registry, baseProperties);

 // Create a derived record type that adds a new property "c"
 Map<String, RecordProperty> derivedProperties = Maps.newHashMap();
 derivedProperties.putAll(baseProperties);
 derivedProperties.put("c", new RecordProperty("c", registry.getBooleanType()));
 RecordType derivedRecordType = new RecordType(registry, derivedProperties);

 // Test the greatest subtype between the derived and base record types
 JSType greatestSubtype = derivedRecordType.getGreatestSubtype(baseRecordType);
 // The greatest subtype between a record and its supertype is the record itself
 assertTrue("Greatest subtype is the base record type", baseRecordType.isEquivalentTo(greatestSubtype));
 }

 @Test
 public void testRecordTypeImplicitPrototype() {
 JSTypeRegistry registry = new JSTypeRegistry();

 // Create a record type with two properties, "a" and "b"
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", new RecordProperty("a", registry.getNumberType()));
 properties.put("b", new RecordProperty("b", registry.getStringType()));
 RecordType recordType = new RecordType(registry, properties);

 // Check the implicit prototype type is Object
 assertEquals("Implicit prototype is Object", registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
 }

 @Test
 public void testRecordTypePropertyDefinition() {
 JSTypeRegistry registry = new JSTypeRegistry();

 // Create a record type with two properties, "a" and "b"
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", new RecordProperty("a", registry.getNumberType()));
 properties.put("b", new RecordProperty("b", registry.getStringType()));
 RecordType recordType = new RecordType(registry, properties);

 // Define a new property "c"
 recordType.defineProperty("c", registry.getBooleanType(), false, null);

 // Check the new property exists
 assertTrue("Property 'c' exists", recordType.hasProperty("c"));
 assertEquals("Property 'c' is of type Boolean", registry.getBooleanType(), recordType.getPropertyType("c"));
 }
}