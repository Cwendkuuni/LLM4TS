import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @Before
 public void setup() {
 registry = new JSTypeRegistry();
 RecordProperty property1 = new RecordProperty("prop1", registry.getBooleanType());
 RecordProperty property2 = new RecordProperty("prop2", registry.getNumberType());
 RecordProperty property3 = new RecordProperty("prop3", registry.getStringType());
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("prop1", property1);
 properties.put("prop2", property2);
 properties.put("prop3", property3);
 recordType = new RecordType(registry, properties);
 }

 @Test
 public void testConstructor() {
 RecordType recordType = new RecordType(registry, properties);
 assertNotNull(recordType.properties);
 assertEquals(3, recordType.properties.size());
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);
 assertTrue(recordType1.isEquivalentTo(recordType2));
 }

 @Test
 public void testIsEquivalentToDifferentProperties() {
 RecordType recordType1 = new RecordType(registry, properties);
 Map<String, RecordProperty> newProperties = Maps.newHashMap(properties);
 newProperties.put("prop4", new RecordProperty("prop4", registry.getNumberType()));
 RecordType recordType2 = new RecordType(registry, newProperties);
 assertFalse(recordType1.isEquivalentTo(recordType2));
 }

 @Test
 public void testGetImplicitPrototype() {
 JSType implicitPrototype = recordType.getImplicitPrototype();
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
 }

 @Test
 public void testDefineProperty() {
 String newPropertyName = "newProp";
 JSType newPropertyType = registry.getBooleanType();
 boolean definePropertyResult = recordType.defineProperty(newPropertyName, newPropertyType, false, null);
 assertTrue(definePropertyResult);
 assertEquals(newPropertyType, recordType.getPropertyType(newPropertyName));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyFrozenRecordType() {
 recordType.isFrozen = true;
 recordType.defineProperty("prop1", null, false, null);
 fail("Should not be able to define property on a frozen record type.");
 }

 @Test
 public void testGetLeastSupertype() {
 RecordType subTypeRecord = new RecordType(registry, properties);
 RecordType superTypeRecord = new RecordType(registry, new PropertiesBuilder().addAll(properties).addProperty("prop4", registry.getBooleanType()).build());
 JSType leastSupertype = recordType.getLeastSupertype(subTypeRecord);
 assertEquals(recordType, leastSupertype);
 }

 @Test
 public void testGetLeastSupertypeDifferentPropertyTypes() {
 RecordType subTypeRecord = new RecordType(registry, properties);
 RecordType superTypeRecord1 = new RecordType(registry, new PropertiesBuilder().addAll(properties).addProperty("prop3", registry.getBooleanType()).build());
 RecordType superTypeRecord2 = new RecordType(registry, new PropertiesBuilder().addAll(properties).addProperty("prop3", registry.getNumberType()).build());
 JSType leastSupertype = recordType.getLeastSupertype(subTypeRecord);
 assertEquals(recordType, leastSupertype);
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType subTypeRecord1 = new RecordType(registry, properties);
 RecordType subTypeRecord2 = new RecordType(registry, new PropertiesBuilder().addAll(properties).addProperty("prop4", registry.getBooleanType()).build());
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(subTypeRecord1);
 assertEquals(subTypeRecord1, greatestSubtype);
 }

 @Test
 public void testGetGreatestSubtypeHelperDifferentPropertyTypes() {
 RecordType subTypeRecord1 = new RecordType(registry, properties);
 RecordType subTypeRecord2 = new RecordType(registry, new PropertiesBuilder().addAll(properties).addProperty("prop3", registry.getNumberType()).build());
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(subTypeRecord2);
 assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), greatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 // Test with same record type
 assertTrue(recordType.isSubtype(recordType));
 // Test with null
 assertFalse(recordType.isSubtype(null));
 // Test with different record type with same properties
 RecordType otherRecordType = new RecordType(registry, properties);
 assertTrue(recordType.isSubtype(otherRecordType));
 // Test with different record type with same properties but different order
 RecordType otherRecordType2 = new RecordType(registry, new PropertiesBuilder().reverse(properties).build());
 assertTrue(recordType.isSubtype(otherRecordType2));
 // Test with different record type with different properties
 Map<String, RecordProperty> newProperties = Maps.newHashMap(properties);
 newProperties.put("prop4", new RecordProperty("prop4", registry.getBooleanType()));
 RecordType otherRecordType3 = new RecordType(registry, newProperties);
 assertFalse(recordType.isSubtype(otherRecordType3));
 // Test with different record type with different properties but same types
 newProperties.put("prop3", property3);
 RecordType otherRecordType4 = new RecordType(registry, newProperties);
 assertTrue(recordType.isSubtype(otherRecordType4));
 // Test with native object type
 RecordType otherRecordType5 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).toMaybeRecordType();
 assertFalse(recordType.isSubtype(otherRecordType5));
 // Test with empty record type
 RecordType emptyRecordType = registry.getNativeObjectType(JSTypeNative.EMPTY_OBJECT_TYPE).toMaybeRecordType();
 assertTrue(recordType.isSubtype(emptyRecordType));
 // Test with record type with one property
 Map<String, RecordProperty> onePropertyProperties = new PropertiesBuilder().addProperty("prop1", property1).build();
 RecordType onePropertyRecordType = new RecordType(registry, onePropertyProperties);
 assertTrue(recordType.isSubtype(onePropertyRecordType));
 }

 @Test
 public void testResolveInternal() {
 RecordType.Property property = recordType.getProperty("prop1");
 JSType resolvedType = recordType.resolveInternal(null, null);
 assertNotNull(resolvedType);
 assertEquals(property.getType(), resolvedType.resolve(null, null));
 }
}