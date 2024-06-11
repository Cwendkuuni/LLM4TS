import static org.junit.Assert.*;
import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry = new JSTypeRegistry();
 private Map<String, RecordProperty> recordProperties;

 @Test
 public void testRecordTypeConstructor() {
 RecordType recordType = new RecordType(registry, recordProperties, true);
 assertNotNull(recordType);
 }

 @Test(expected = IllegalStateException.class)
 public void testRecordTypeConstructorWithNullPropertyMap() {
 RecordType recordType = new RecordType(registry, null, true);
 fail("Expected IllegalStateException with null RecordProperty map");
 }

 @Test
 public void testIsSynthetic() {
 RecordType recordType = new RecordType(registry, recordProperties, true);
 assertFalse(recordType.isSynthetic());

 RecordType synthesizedRecordType = new RecordType(registry, recordProperties, false);
 assertTrue(synthesizedRecordType.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 RecordType recordType1 = new RecordType(registry, recordProperties, true);
 RecordType recordType2 = new RecordType(registry, recordProperties, true);

 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));

 Map<String, RecordProperty> recordProperties2 = Maps.newHashMap(recordProperties);
 recordProperties2.remove("property1");
 RecordType recordType3 = new RecordType(registry, recordProperties2, true);

 assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, false));
 }

 @Test
 public void testGetImplicitPrototype() {
 RecordType recordType = new RecordType(registry, recordProperties, true);
 ObjectType implicitPrototype = recordType.getImplicitPrototype();
 assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype.getTypeClass());
 }

 @Test
 public void testDefineProperty() {
 RecordType recordType = new RecordType(registry, recordProperties, true);

 String propertyName = "property1";
 JSType propertyType = registry.getNativeType(JSTypeNative.STRING_TYPE);
 Node propertyNode = Node.NULL();

 assertFalse(recordType.defineProperty(propertyName, propertyType, false, propertyNode));
 recordType.defineProperty(propertyName, propertyType, true, propertyNode);
 assertTrue(recordType.hasProperty(propertyName));
 }

 @Test
 public void testDefinePropertyWithExistingProperty() {
 RecordType recordType = new RecordType(registry, recordProperties, true);
 String propertyName = "property1";
 JSType propertyType = registry.getNativeType(JSTypeNative.STRING_TYPE);
 Node propertyNode = Node.NULL();

 recordType.defineProperty(propertyName, propertyType, true, propertyNode);
 assertTrue(recordType.hasProperty(propertyName));

 assertFalse(recordType.defineProperty(propertyName, propertyType, false, propertyNode));
 }

 @Test
 public void testDefinePropertyWithFrozenRecordType() {
 RecordType recordType = new RecordType(registry, recordProperties, true);
 recordType.isFrozen = true;

 String propertyName = "property1";
 JSType propertyType = registry.getNativeType(JSTypeNative.STRING_TYPE);
 Node propertyNode = Node.NULL();

 assertFalse(recordType.defineProperty(propertyName, propertyType, false, propertyNode));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType1 = new RecordType(registry, recordProperties, true);
 RecordType recordType2 = new RecordType(registry, recordProperties, true);
 RecordType recordType3 = new RecordType(registry, recordProperties, true);
 RecordType recordType4 = new RecordType(registry, recordProperties, true);

 // Both recordType1 and recordType2 are equivalent, so their greatest
 // subtype should be themselves.
 JSType greatestSubtype12 = recordType1.getGreatestSubtypeHelper(recordType2);
 assertEquals(recordType1, greatestSubtype12);
 assertEquals(recordType2, greatestSubtype12);

 // recordType3 has an additional property, so it is a subtype of recordType1,
 // but not vice versa. recordType1 is not a subtype of recordType4, which has
 // no properties in common with recordType1.
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 recordTypeBuilder.addProperty("property2", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null);
 RecordType recordType3 = recordTypeBuilder.build();
 RecordType recordType4 = new RecordType(registry, recordProperties, true);

 JSType greatestSubtype13 = recordType1.getGreatestSubtypeHelper(recordType3);
 assertTrue(recordType1.isSubtype(greatestSubtype13));
 assertFalse(recordType3.isSubtype(recordType1));

 JSType greatestSubtype14 = recordType1.getGreatestSubtypeHelper(recordType4);
 assertFalse(recordType1.isSubtype(recordType4));
 assertFalse(recordType4.isSubtype(recordType1));

 // The greatest subtype of recordType3 and recordType4 is the empty record type.
 JSType greatestSubtype34 = recordType3.getGreatestSubtypeHelper(recordType4);
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype34);
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType1 = new RecordType(registry, recordProperties, true);
 RecordType recordType2 = new RecordType(registry, recordProperties, true);
 RecordType recordType3 = new RecordType(registry, recordProperties, true);
 RecordType recordType4 = new RecordType(registry, recordProperties, true);
 RecordType recordType5 = new RecordType(registry, recordProperties, true);
 RecordType recordType6 = new RecordType(registry, recordProperties, true);

 // Test transitive isSubtype relationship
 assertTrue(recordType1.isSubtype(recordType2));
 assertTrue(recordType2.isSubtype(recordType3));
 assertTrue(recordType1.isSubtype(recordType3));

 // A record type is a subtype of itself
 assertTrue(recordType1.isSubtype(recordType1));

 // The empty record type is a subtype of any record type.
 assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));

 // A record type is not a subtype of a non-record type
 assertFalse(recordType1.isSubtype(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE)));

 // A record type is not a subtype of itself with a different name
 RecordType recordType6WithDifferentName = new RecordType(registry, recordProperties, true);
 recordType6WithDifferentName.setName("recordType6WithDifferentName");
 assertFalse(recordType1.isSubtype(recordType6WithDifferentName));

 // A record type is not a subtype of itself with different properties
 RecordType recordType5WithDifferentProperties = new RecordType(registry, recordProperties, true);
 recordType5WithDifferentProperties.properties.put("property5", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), null);
 assertFalse(recordType1.isSubtype(recordType5WithDifferentProperties));

 // A record type is not a subtype of another record type with a different property order
 RecordType recordType4WithDifferentOrder = new RecordType(registry, recordProperties, true);
 recordType4WithDifferentOrder.setPropertyOrder(Maps.newArrayList("property2", "property1"));
 assertFalse(recordType1.isSubtype(recordType4WithDifferentOrder));

 // A record type is not a subtype of a record type with a property of incompatible type
 RecordType recordType3WithIncompatibleType = new RecordType(registry, recordProperties, true);
 recordType3WithIncompatibleType.setPropertyType("property1", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null);
 assertFalse(recordType1.isSubtype(recordType3WithIncompatibleType));
 }

 @Test
 public void testIsSubtypeWithNullTypes() {
 RecordType recordType1 = new RecordType(registry, recordProperties, true);
 RecordType recordType2 = null;

 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithNonRecordType() {
 RecordType recordType1 = new RecordType(registry, recordProperties, true);
 JSType nonRecordType = registry.getNativeType(JSTypeNative.BOOLEAN_TYPE);

 assertFalse(recordType1.isSubtype(nonRecordType));
 }

 @Test
 public void testGetLeastSupertypeWithUnionType() {
 RecordType recordType1 = new RecordType(registry, recordProperties, true);
 RecordType recordType2 = new RecordType(registry, recordProperties, true);
 RecordType recordType3 = new RecordType(registry, recordProperties, true);
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);

 // The least supertype of recordType1 and recordType2 is the intersection of
 // their properties with compatible types.
 JSType leastSupertype12 = recordType1.getGreatestSubtypeHelper(recordType2);
 unionTypeBuilder.addAlternate(recordType1);
 unionTypeBuilder.addAlternate(recordType2);
 JSType intersection = unionTypeBuilder.build().getLeastSupertype(objectType);
 assertEquals(leastSupertype12, intersection);

 // The least supertype of recordType1 and recordType3, which has no common properties,
 // is the empty record type.
 JSType leastSupertype13 = recordType1.getGreatestSubtypeHelper(recordType3);
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), leastSupertype13);
 }

 @Test
 public void testToString() {
 RecordType recordType = new RecordType(registry, recordProperties, true);
 assertEquals("RecordType{properties=[property1: any]}", recordType.toString());
 }
}