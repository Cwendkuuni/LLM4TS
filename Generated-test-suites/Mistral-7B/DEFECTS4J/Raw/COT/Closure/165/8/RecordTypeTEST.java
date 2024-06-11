import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Test;

public class RecordTypeTest {

 private static JSTypeRegistry registry;
 private static RecordType recordType;

 @BeforeClass
 public static void setUp() {
 registry = new JSTypeRegistry();
 RecordProperty[] properties = new RecordProperty[] {
 new RecordProperty("property1", JSType.getStringType()),
 new RecordProperty("property2", JSType.getNumberType()),
 new RecordProperty("property3", JSType.getBooleanType())
 };
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 recordType = recordTypeBuilder.buildRecordType("MyRecord", properties);
 }

 @Test
 public void testRecordTypeCreation() {
 // Test that a record type can be created
 RecordType expectedRecordType = recordType;
 RecordType actualRecordType = new RecordType(registry, Maps.newHashMap());
 assertEquals("Record types should be equal", expectedRecordType, actualRecordType);
 }

 @Test
 public void testIsEquivalentTo() {
 // Test that a record type is equivalent to itself
 assertTrue("Record type should be equivalent to itself", recordType.isEquivalentTo(recordType));

 // Test that a record type is not equivalent to an object type
 assertFalse("Record type should not be equivalent to an object type",
 recordType.isEquivalentTo(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));

 // Test that a record type is not equivalent to a different record type
 RecordType differentRecordType = new RecordType(registry, Maps.newHashMap());
 assertFalse("Record types should not be equivalent if they have different properties",
 recordType, differentRecordType);

 // Test that a record type is equivalent to a record type with the same properties
 RecordType samePropertiesRecordType = new RecordType(registry, Maps.newHashMap());
 for (RecordProperty property : recordType.getPropertyNames()) {
 samePropertiesRecordType.defineProperty(property.getName(), property.getType(),
 false, property.getNode());
 }
 assertTrue("Record types should be equivalent if they have the same properties",
 recordType, samePropertiesRecordType);
 }

 @Test
 public void testGetImplicitPrototype() {
 // Test that a record type has the native object type as its implicit prototype
 assertEquals("Record type's implicit prototype should be the native object type",
 registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 // Test that a property can be defined on a record type
 RecordProperty newProperty = new RecordProperty("newProperty", JSType.getNumberType());
 recordType.defineProperty("newProperty", JSType.getNumberType(), false, null);
 assertTrue("Record type should have a new property", recordType.hasProperty("newProperty"));
 JSType newPropertyType = recordType.getPropertyType("newProperty");
 assertEquals("New property's type should be the same as the type passed to defineProperty",
 JSType.getNumberType(), newPropertyType);
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyFrozen() {
 recordType.isFrozen = true;
 recordType.defineProperty("property1", JSType.getBooleanType(), false, null);
 fail("A frozen record type should not be able to define new properties");
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 // Test that a record type with no properties is a subtype of any record type
 RecordType emptyRecordType = new RecordType(registry, Maps.newHashMap());
 JSType anyRecordType = recordType.getGreatestSubtypeHelper(emptyRecordType);
 assertTrue("Empty record type should be a subtype of any record type",
 anyRecordType.isSubtype(emptyRecordType));

 // Test that two record types with the same properties are equivalent
 RecordType samePropertiesRecordType = new RecordType(registry, Maps.newHashMap());
 for (RecordProperty property : recordType.getPropertyNames()) {
 samePropertiesRecordType.defineProperty(property.getName(), property.getType(),
 false, property.getNode());
 }
 JSType sameProperties = recordType.getGreatestSubtypeHelper(samePropertiesRecordType);
 assertTrue("Record types with the same properties should be equivalent",
 recordType, samePropertiesRecordType);
 assertEquals("Record types with the same properties should have the same types",
 recordType, sameProperties);

 // Test that a record type is a subtype of itself
 JSType selfSubtype = recordType.getGreatestSubtypeHelper(recordType);
 assertTrue("A record type should be a subtype of itself", recordType, selfSubtype);
 assertEquals("Record types should be identical if they are equivalent", recordType, selfSubtype);

 // Test that a record type with properties is a subtype of a record type with the same properties
 RecordType samePropertiesSubtypeRecordType = new RecordType(registry, Maps.newHashMap());
 for (RecordProperty property : recordType.getPropertyNames()) {
 samePropertiesSubtypeRecordType.defineProperty(property.getName(), property.getType(),
 false, property.getNode());
 }
 JSType samePropertiesSubtype = recordType.getGreatestSubtypeHelper(samePropertiesSubtypeRecordType);
 assertTrue("Record types with the same properties should be subtypes of each other",
 recordType, samePropertiesSubtypeRecordType);
 assertEquals("Record types with the same properties should have the same types",
 recordType, samePropertiesSubtype);

 // Test that a record type is a subtype of a record type with additional properties
 RecordType additionalPropertiesRecordType = new RecordType(registry, Maps.newHashMap());
 additionalPropertiesRecordType.defineProperty("newProperty", JSType.getNumberType(), false, null);
 JSType subType = recordType.getGreatestSubtypeHelper(additionalPropertiesRecordType);
 assertTrue("A record type should be a subtype of a record type with additional properties",
 recordType, subType);

 // Test that a record type is not a subtype of a record type with fewer properties
 RecordType fewerPropertiesRecordType = new RecordType(registry, Maps.newHashMap());
 assertFalse("A record type should not be a subtype of a record type with fewer properties",
 recordType, fewerPropertiesRecordType);

 // Test that a record type is not a subtype of a record type with properties of incompatible types
 RecordType incompatibleTypesRecordType = new RecordType(registry, Maps.newHashMap());
 incompatibleTypesRecordType.defineProperty("newProperty", JSType.getStringType(), false, null);
 assertFalse("A record type should not be a subtype of a record type with properties of incompatible types",
 recordType, incompatibleTypesRecordType);

 // Test that a record type is not a subtype of a record type with properties of incompatible names
 RecordType incompatibleNamesRecordType = new RecordType(registry, Maps.newHashMap());
 incompatibleNamesRecordType.defineProperty("newProperty2", JSType.getNumberType(), false, null);
 assertFalse("A record type should not be a subtype of a record type with properties of incompatible names",
 recordType, incompatibleNamesRecordType);
 }

 @Test
 public void testToMaybeRecordType() {
 // Test that a record type is its own toMaybeRecordType
 assertEquals("Record type should be its own toMaybeRecordType", recordType, recordType.toMaybeRecordType());
 }

 @Test
 public void testIsSubtype() {
 // Test that a record type is a subtype of itself
 assertTrue("Record type should be a subtype of itself", recordType.isSubtype(recordType));

 // Test that a record type is a subtype of a record type with the same properties
 RecordType samePropertiesSubtypeRecordType = new RecordType(registry, Maps.newHashMap());
 for (RecordProperty property : recordType.getPropertyNames()) {
 samePropertiesSubtypeRecordType.defineProperty(property.getName(), property.getType(),
 false, property.getNode());
 }
 assertTrue("Record types with the same properties should be subtypes of each other",
 recordType, samePropertiesSubtypeRecordType);

 // Test that a record type is a subtype of a record type with additional properties
 RecordType additionalPropertiesRecordType = new RecordType(registry, Maps.newHashMap());
 additionalPropertiesRecordType.defineProperty("newProperty", JSType.getNumberType(), false, null);
 assertTrue("A record type should be a subtype of a record type with additional properties",
 recordType, additionalPropertiesRecordType);

 // Test that a record type is not a subtype of a record type with fewer properties
 RecordType fewerPropertiesRecordType = new RecordType(registry, Maps.newHashMap());
 assertFalse("A record type should not be a subtype of a record type with fewer properties",
 recordType, fewerPropertiesRecordType);

 // Test that a record type is not a subtype of a record type with properties of incompatible types
 RecordType incompatibleTypesRecordType = new RecordType(registry, Maps.newHashMap());
 incompatibleTypesRecordType.defineProperty("newProperty", JSType.getStringType(), false, null);
 assertFalse("A record type should not be a subtype of a record type with properties of incompatible types",
 recordType, incompatibleTypesRecordType);

 // Test that a record type is not a subtype of a record type with properties of incompatible names
 RecordType incompatibleNamesRecordType = new RecordType(registry, Maps.newHashMap());
 incompatibleNamesRecordType.defineProperty("newProperty2", JSType.getNumberType(), false, null);
 assertFalse("A record type should not be a subtype of a record type with properties of incompatible names",
 recordType, incompatibleNamesRecordType);
 }

 @Test
 public void testResolveInternal() {
 // Test that a record type resolves its properties
 RecordType resolvedRecordType = new RecordType(registry, Maps.newHashMap());
 resolvedRecordType.defineProperty("property1", registry.getStringType(), false, null);
 resolvedRecordType.defineProperty("property2", registry.getNumberType(), false, null);
 resolvedRecordType.defineProperty("property3", registry.getBooleanType(), false, null);
 JSType resolvedProperty1 = resolvedRecordType.getPropertyType("property1");
 JSType resolvedProperty2 = resolvedRecordType.getPropertyType("property2");
 JSType resolvedProperty3 = resolvedRecordType.getPropertyType("property3");
 assertEquals("Record types should resolve to the same types as their properties",
 JSType.getStringType(), resolvedProperty1);
 assertEquals("Record types should resolve to the same types as their properties",
 JSType.getNumberType(), resolvedProperty2);
 assertEquals("Record types should resolve to the same types as their properties",
 JSType.getBooleanType(), resolvedProperty3);
 }

 @Test
 public void testUnionType() {
 // Test that a record type with no properties is a subtype of any record type
 UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
 unionTypeBuilder.addAlternate(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
 unionTypeBuilder.addAlternate(recordType);
 JSType emptyRecordTypeSubtype = unionTypeBuilder.build();
 assertTrue("Empty record type should be a subtype of any record type",
 recordType, emptyRecordTypeSubtype);

 // Test that a record type with properties is a subtype of a record type with the same properties
 UnionTypeBuilder samePropertiesUnionTypeBuilder = new UnionTypeBuilder(registry);
 for (RecordProperty property : recordType.getPropertyNames()) {
 samePropertiesUnionTypeBuilder.addAlternate(registry.newRecordType(property.getName(), property.getType()));
 }
 JSType samePropertiesSubtype = unionTypeBuilder.build();
 assertTrue("Record types with the same properties should be subtypes of each other",
 recordType, samePropertiesSubtype);

 // Test that a record type is a subtype of a record type with additional properties
 UnionTypeBuilder additionalPropertiesUnionTypeBuilder = new UnionTypeBuilder(registry);
 for (RecordProperty property : recordType.getPropertyNames()) {
 additionalPropertiesUnionTypeBuilder.addAlternate(registry.newRecordType(property.getName(), property.getType()));
 }
 additionalPropertiesUnionTypeBuilder.addAlternate(new RecordType(registry, Maps.newHashMap()){
 {
 defineProperty("newProperty", JSType.getNumberType(), false, null);
 }
 }.getClosure());
 JSType additionalPropertiesSubtype = unionTypeBuilder.build();
 assertTrue("A record type should be a subtype of a record type with additional properties",
 recordType, additionalPropertiesSubtype);

 // Test that a record type is not a subtype of a record type with fewer properties
 UnionTypeBuilder fewerPropertiesUnionTypeBuilder = new UnionTypeBuilder(registry);
 for (RecordProperty property : recordType.getPropertyNames()) {
 fewerPropertiesUnionTypeBuilder.addAlternate(registry.newRecordType(property.getName(), property.getType()));
 }
 fewerPropertiesUnionTypeBuilder.addAlternate(new RecordType(registry, Maps.newHashMap()).getClosure());
 JSType fewerPropertiesSubtype = unionTypeBuilder.build();
 assertFalse("A record type should not be a subtype of a record type with fewer properties",
 recordType, fewerPropertiesSubtype);

 // Test that a record type is not a subtype of a record type with properties of incompatible types
 UnionTypeBuilder incompatibleTypesUnionTypeBuilder = new UnionTypeBuilder(registry);
 for (RecordProperty property : recordType.getPropertyNames()) {
 incompatibleTypesUnionTypeBuilder.addAlternate(registry.newRecordType(property.getName(), property.getType()));
 }
 incompatibleTypesUnionTypeBuilder.addAlternate(new RecordType(registry, Maps.newHashMap()){
 {
 defineProperty("newProperty", registry.getStringType(), false, null);
 }
 }.getClosure());
 JSType incompatibleTypesSubtype = unionTypeBuilder.build();
 assertFalse("A record type should not be a subtype of a record type with properties of incompatible types",
 recordType, incompatibleTypesSubtype);

 // Test that a record type is not a subtype of a record type with properties of incompatible names
 UnionTypeBuilder incompatibleNamesUnionTypeBuilder = new UnionTypeBuilder(registry);
 for (RecordProperty property : recordType.getPropertyNames()) {
 incompatibleNamesUnionTypeBuilder.addAlternate(registry.newRecordType(property.getName(), property.getType()));
 }
 incompatibleNamesUnionTypeBuilder.addAlternate(new RecordType(registry, Maps.newHashMap()){
 {
 defineProperty("newProperty2", JSType.getNumberType(), false, null);
 }
 }.getClosure());
 JSType incompatibleNamesSubtype = unionTypeBuilder.build();
 assertFalse("A record type should not be a subtype of a record type with properties of incompatible names",
 recordType, incompatibleNamesSubtype);
 }
}