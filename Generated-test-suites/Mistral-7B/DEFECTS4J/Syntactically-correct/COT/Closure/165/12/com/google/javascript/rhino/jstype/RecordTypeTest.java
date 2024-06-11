import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.common.collect.Maps;

public class RecordTypeTest {

 private static Map<String, RecordProperty> createPropertiesMap() {
 return Maps.newHashMap();
 }

 @Test
 public void testRecordTypeCreation() {
 // Test case for the constructor of RecordType
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties = createPropertiesMap();
 RecordType recordType = new RecordType(registry, properties);
 assertEquals(JSType. Record_type, recordType.getTypeClass());
 assertTrue(recordType.isRecordType());
 }

 @Test
 public void testIsEquivalentTo() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties1 = createPropertiesMap();
 properties1.put("property1", new RecordProperty(JSType.STRING_TYPE, null));
 RecordType recordType1 = new RecordType(registry, properties1);

 Map<String, RecordProperty> properties2 = createPropertiesMap();
 properties2.put("property1", new RecordProperty(JSType.NUMBER_TYPE, null));
 RecordType recordType2 = new RecordType(registry, properties2);

 assertFalse(recordType1.isEquivalentTo(recordType2));

 properties2.put("property1", new RecordProperty(JSType.STRING_TYPE, null));
 RecordType recordType3 = new RecordType(registry, properties2);
 assertTrue(recordType1.isEquivalentTo(recordType3));
 }

 @Test
 public void testGetImplicitPrototype() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 RecordType recordType = new RecordType(registry, createPropertiesMap());
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties = createPropertiesMap();
 RecordType recordType = new RecordType(registry, properties);

 RecordProperty recordProperty = new RecordProperty(JSType.STRING_TYPE, null);
 String propertyName = "property";
 recordType.defineProperty(propertyName, JSType.STRING_TYPE, false, null);
 assertTrue(recordType.hasProperty(propertyName));
 assertEquals(JSType.STRING_TYPE, recordType.getPropertyType(propertyName));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWithNullProperty() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties = createPropertiesMap();
 RecordType recordType = new RecordType(registry, properties);
 recordType.defineProperty("property", null, false, null);
 fail("Expected an IllegalStateException when defining a property with null RecordProperty.");
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 RecordType typeA = new RecordType(registry,
 createPropertiesMap().put("property1", new RecordProperty(JSType.INT_TYPE, null)));
 RecordType typeB = new RecordType(registry,
 createPropertiesMap().put("property1", new RecordProperty(JSType.NUMBER_TYPE, null)));
 RecordType typeC = new RecordType(registry,
 createPropertiesMap().put("property1", new RecordProperty(JSType.STRING_TYPE, null)));

 JSType greatestSubtype = typeA.getGreatestSubtypeHelper(typeB);
 assertNotNull(greatestSubtype);
 assertEquals(typeC.getTypeClass(), greatestSubtype.getTypeClass());
 }

 @Test
 public void testIsSubtype() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 RecordType recordTypeA = new RecordType(registry,
 createPropertiesMap().put("property1", new RecordProperty(JSType.INT_TYPE, null)));
 RecordType recordTypeB = new RecordType(registry,
 createPropertiesMap().put("property1", new RecordProperty(JSType.NUMBER_TYPE, null)));
 RecordType recordTypeC = new RecordType(registry,
 createPropertiesMap().put("property1", new RecordProperty(JSType.STRING_TYPE, null)));
 RecordType recordTypeD = new RecordType(registry, createPropertiesMap());

 assertTrue(recordTypeA.isSubtype(recordTypeA));
 assertTrue(recordTypeA.isSubtype(recordTypeB));
 assertTrue(recordTypeA.isSubtype(recordTypeC));
 assertTrue(recordTypeA.isSubtype(recordTypeD));

 assertFalse(recordTypeB.isSubtype(recordTypeA));
 assertFalse(recordTypeC.isSubtype(recordTypeA));
 assertFalse(recordTypeD.isSubtype(recordTypeA));
 }

 @Test
 public void testToMaybeRecordType() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 RecordType recordType = new RecordType(registry, createPropertiesMap());
 assertEquals(recordType, recordType.toMaybeRecordType());
 }

 @Test
 public void testIsFrozen() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 RecordType recordType = new RecordType(registry, createPropertiesMap());
 assertFalse(recordType.isFrozen());

 recordType.setFrozen(true);
 assertTrue(recordType.isFrozen());
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWhenFrozen() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 RecordType recordType = new RecordType(registry, createPropertiesMap());
 recordType.setFrozen(true);

 RecordProperty recordProperty = new RecordProperty(JSType.STRING_TYPE, null);
 String propertyName = "property";
 recordType.defineProperty(propertyName, JSType.STRING_TYPE, false, null);
 fail("Expected an IllegalStateException when defining a property on a frozen RecordType.");
 }

 @Test
 public void testGreatestSubtypeWithObjectType() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 RecordType recordType = new RecordType(registry, createPropertiesMap());
 RecordType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(objectType);
 assertEquals(objectType.getTypeClass(), greatestSubtype.getTypeClass());
 }

 @Test
 public void testGreatestSubtypeWithNull() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 RecordType recordType = new RecordType(registry, createPropertiesMap());
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(null);
 assertNotNull(greatestSubtype);
 assertEquals(JSTypeNative.NO_OBJECT_TYPE.getTypeClass(), greatestSubtype.getTypeClass());
 }

 @Test
 public void testGreatestSubtypeWithEmptyRecordType() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 RecordType recordType = new RecordType(registry, createPropertiesMap());
 RecordType emptyRecordType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
 .createRecordType(new RecordProperty[0]);
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(emptyRecordType);
 assertEquals(recordType.getTypeClass(), greatestSubtype.getTypeClass());
 }

 @Test
 public void testGreatestSubtypeWithRecordTypeWithSameProperties() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 RecordType recordTypeA = new RecordType(registry,
 createPropertiesMap().put("property1", new RecordProperty(JSType.INT_TYPE, null)));
 RecordType recordTypeB = new RecordType(registry,
 createPropertiesMap().put("property1", new RecordProperty(JSType.INT_TYPE, null)));
 JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
 assertEquals(recordTypeA.getTypeClass(), greatestSubtype.getTypeClass());
 }

 @Test
 public void testGreatestSubtypeWithRecordTypeWithDifferentProperties() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 RecordType recordTypeA = new RecordType(registry,
 createPropertiesMap().put("property1", new RecordProperty(JSType.INT_TYPE, null)));
 RecordType recordTypeB = new RecordType(registry,
 createPropertiesMap().put("property2", new RecordProperty(JSType.STRING_TYPE, null)));
 JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
 assertEquals(JSTypeNative.NO_TYPE.getTypeClass(), greatestSubtype.getTypeClass());
 }

 @Test
 public void testGreatestSubtypeWithRecordTypeWithConflictingProperties() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 RecordType recordTypeA = new RecordType(registry,
 createPropertiesMap().put("property1", new RecordProperty(JSType.INT_TYPE, null)));
 RecordType recordTypeB = new RecordType(registry,
 createPropertiesMap().put("property1", new RecordProperty(JSType.STRING_TYPE, null)));
 JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
 assertEquals(JSTypeNative.NO_TYPE.getTypeClass(), greatestSubtype.getTypeClass());
 }

 @Test
 public void testGreatestSubtypeWithRecordTypeWithIntersectingProperties() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 RecordType recordTypeA = new RecordType(registry,
 createPropertiesMap().put("property1", new RecordProperty(JSType.INT_TYPE, null))
 .put("property2", new RecordProperty(JSType.STRING_TYPE, null)));
 RecordType recordTypeB = new RecordType(registry,
 createPropertiesMap().put("property1", new RecordProperty(JSType.NUMBER_TYPE, null))
 .put("property2", new RecordProperty(JSType.STRING_TYPE, null)));

 UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 UnionType unionType = unionTypeBuilder.build();
 JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
 assertEquals(unionType.getTypeClass(), greatestSubtype.getTypeClass());
 }

 @Test
 public void testGreatestSubtypeWithRecordTypeWithIntersectingPropertiesAndSuperType() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 RecordType recordTypeA = new RecordType(registry,
 createPropertiesMap().put("property1", new RecordProperty(JSType.INT_TYPE, null))
 .put("property2", new RecordProperty(JSType.STRING_TYPE, null)));
 RecordType recordTypeB = new RecordType(registry,
 createPropertiesMap().put("property1", new RecordProperty(JSType.NUMBER_TYPE, null))
 .put("property2", new RecordProperty(JSType.STRING_TYPE, null)));
 ObjectType superType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
 unionTypeBuilder.addAlternate(superType);
 UnionType unionType = unionTypeBuilder.build();
 JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
 assertEquals(unionType.getTypeClass(), greatestSubtype.getTypeClass());
 }
}