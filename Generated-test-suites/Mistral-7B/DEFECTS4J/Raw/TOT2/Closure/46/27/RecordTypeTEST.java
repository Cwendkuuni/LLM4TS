import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import java.util.Arrays;
import java.util.Map;

public class RecordTypeTest {

 @Test
 public void testRecordTypeCreation() {
 // Test creating a record type with valid properties
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("name", new RecordProperty("name", "string"));
 properties.put("age", new RecordProperty("age", "number"));
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType = new RecordType(registry, properties);
 JSType recordTypeInstance = JSTypeFactory.getInstance(registry).getRecordType(recordType.getName());

 // Test property access
 assertEquals("name", recordType.getPropertyNames()[0]);
 assertEquals("string", recordTypeInstance.getPropertyTypes()[0].getName());
 assertEquals("age", recordType.getPropertyNames()[1]);
 assertEquals("number", recordTypeInstance.getPropertyTypes()[1].getName());

 // Test property values
 RecordType instance = (RecordType) recordTypeInstance.getInstance();
 instance.defineProperty("name", JSTypeFactory.getInstance(registry).getStringType(), false, Node.newStringLiteralNode("John Doe"));
 instance.defineProperty("age", JSTypeFactory.getInstance(registry).getNumberType(), false, Node.newIntLiteralNode(30));

 // Test property access and equality
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);
 RecordType instance1 = (RecordType) recordType1.getInstance();
 RecordType instance2 = (RecordType) recordType2.getInstance();

 instance1.defineProperty("name", JSTypeFactory.getInstance(registry).getStringType(), false, Node.newStringLiteralNode("John Doe"));
 instance1.defineProperty("age", JSTypeFactory.getInstance(registry).getNumberType(), false, Node.newIntLiteralNode(30));
 instance2.defineProperty("name", JSTypeFactory.getInstance(registry).getStringType(), false, Node.newStringLiteralNode("John Doe"));
 instance2.defineProperty("age", JSTypeFactory.getInstance(registry).getNumberType(), false, Node.newIntLiteralNode(30));

 assertTrue(instance1.isEquivalentTo(instance2));

 // Test property modification
 instance1.defineProperty("name", JSTypeFactory.getInstance(registry).getStringType(), false, Node.newStringLiteralNode("Jane Doe"));
 assertFalse(instance1.isEquivalentTo(instance2));

 // Test property deletion
 instance2.deleteProperty("name");
 assertFalse(instance2.hasProperty("name"));
 assertTrue(instance1.isEquivalentTo(instance2));
 }

 @Test
 public void testRecordTypeSubtyping() {
 // Test subtyping between record types with identical properties
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("name", new RecordProperty("name", "string"));
 properties.put("age", new RecordProperty("age", "number"));
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);

 RecordType recordType1Instance = (RecordType) recordType1.getInstance();
 RecordType recordType2Instance = (RecordType) recordType2.getInstance();

 // Test subtyping with a record having fewer properties
 Map<String, RecordProperty> propertiesSubtype = Maps.newHashMap();
 propertiesSubtype.put("name", new RecordProperty("name", "string"));
 RecordType recordTypeSubtype = new RecordType(registry, propertiesSubtype);

 assertTrue(recordType1.isSubtype(recordTypeSubtype));
 assertTrue(recordTypeSubtype.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(recordType1Instance));
 assertTrue(recordType1Instance.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(recordType2));
 assertTrue(recordType2.isSubtype(recordType1));
 assertTrue(recordType1Instance.isSubtype(recordType2Instance));
 assertTrue(recordType2Instance.isSubtype(recordType1Instance));

 // Test subtyping with a record having more properties
 Map<String, RecordProperty> propertiesSupertype = Maps.newHashMap();
 propertiesSupertype.put("name", new RecordProperty("name", "string"));
 propertiesSupertype.put("age", new RecordProperty("age", "number"));
 propertiesSupertype.put("city", new RecordProperty("city", "string"));
 RecordType recordTypeSupertype = new RecordType(registry, propertiesSupertype);

 assertFalse(recordTypeSubtype.isSubtype(recordTypeSupertype));
 assertFalse(recordTypeSupertype.isSubtype(recordTypeSubtype));
 assertFalse(recordType1Instance.isSubtype(recordTypeSupertype));
 assertFalse(recordTypeSupertype.isSubtype(recordType1Instance));
 assertFalse(recordType2Instance.isSubtype(recordTypeSupertype));
 assertFalse(recordTypeSupertype.isSubtype(recordType2Instance));
 }

 @Test
 public void testRecordTypeEquivalence() {
 // Test equivalence between record types with identical properties
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("name", new RecordProperty("name", "string"));
 properties.put("age", new RecordProperty("age", "number"));
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);

 RecordType recordType1Instance = (RecordType) recordType1.getInstance();
 RecordType recordType2Instance = (RecordType) recordType2.getInstance();

 instance1.defineProperty("name", JSTypeFactory.getInstance(registry).getStringType(), false, Node.newStringLiteralNode("John Doe"));
 instance1.defineProperty("age", JSTypeFactory.getInstance(registry).getNumberType(), false, Node.newIntLiteralNode(30));
 instance2.defineProperty("name", JSTypeFactory.getInstance(registry).getStringType(), false, Node.newStringLiteralNode("John Doe"));
 instance2.defineProperty("age", JSTypeFactory.getInstance(registry).getNumberType(), false, Node.newIntLiteralNode(30));

 assertTrue(recordType1.isEquivalentTo(recordType2));

 // Test inequivalence between record types with different properties
 Map<String, RecordProperty> propertiesDifferent = Maps.newHashMap();
 propertiesDifferent.put("name", new RecordProperty("name", "string"));
 propertiesDifferent.put("age", new RecordProperty("age", "number"));
 propertiesDifferent.put("city", new RecordProperty("city", "string"));
 RecordType recordTypeDifferent = new RecordType(registry, propertiesDifferent);

 assertFalse(recordType1.isEquivalentTo(recordTypeDifferent));
 assertFalse(recordTypeDifferent.isEquivalentTo(recordType1));
 }

 @Test
 public void testRecordTypeFrozen() {
 // Test freezing a record type
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("name", new RecordProperty("name", "string"));
 properties.put("age", new RecordProperty("age", "number"));
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType = new RecordType(registry, properties);
 recordType.freeze();

 RecordType recordTypeInstance = (RecordType) recordType.getInstance();

 // Test adding properties to a frozen record type
 recordTypeInstance.defineProperty("city", JSTypeFactory.getInstance(registry).getStringType(), false, Node.newStringLiteralNode("New York"));
 assertFalse(recordTypeInstance.hasProperty("city"));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddPropertyToFrozenRecordTypeWithNullPropertyNode() {
 // Test adding a property to a frozen record type with a null property node
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("name", new RecordProperty("name", "string"));
 properties.put("age", new RecordProperty("age", "number"));
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType = new RecordType(registry, properties);
 recordType.freeze();
 RecordType recordTypeInstance = (RecordType) recordType.getInstance();
 recordTypeInstance.defineProperty("city", null, false, Node.newStringLiteralNode("New York"));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddPropertyToFrozenRecordTypeWithDuplicatePropertyName() {
 // Test adding a property with a duplicate property name to a frozen record type
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("name", new RecordProperty("name", "string"));
 properties.put("name", new RecordProperty("name", "string"));
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType = new RecordType(registry, properties);
 recordType.freeze();
 RecordType recordTypeInstance = (RecordType) recordType.getInstance();

 try {
 recordTypeInstance.defineProperty("name", JSTypeFactory.getInstance(registry).getStringType(), false, Node.newStringLiteralNode("John Doe"));
 fail("Expected an IllegalStateException");
 } catch (IllegalStateException e) {
 }
 }

 @Test
 public void testRecordTypeToMaybeRecordType() {
 // Test that toMaybeRecordType() always returns the same instance for a given record type
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("name", new RecordProperty("name", "string"));
 properties.put("age", new RecordProperty("age", "number"));
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType = new RecordType(registry, properties);

 assertEquals(recordType, recordType.toMaybeRecordType());
 }
}