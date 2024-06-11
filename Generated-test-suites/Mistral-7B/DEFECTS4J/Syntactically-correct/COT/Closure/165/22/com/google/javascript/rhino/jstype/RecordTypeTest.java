import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 RecordProperty property1 = new RecordProperty("property1", JSType.STRING);
 RecordProperty property2 = new RecordProperty("property2", JSType.NUMBER);
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("property1", property1);
 properties.put("property2", property2);
 recordType = new RecordType(registry, properties);
 }

 @Test
 public void testCreateRecordType() {
 // Test the constructor
 RecordType recordTypeExpected = new RecordType(registry, properties);
 assertEquals(recordTypeExpected, recordType);
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType recordTypeExpected = new RecordType(registry, properties);
 RecordType recordTypeCopy = new RecordType(registry, properties);
 RecordType recordTypeDifferent = new RecordType(registry, Maps.newHashMap());
 RecordType recordTypeDifferent2 = new RecordType(registry, null);

 // Test equality with the same instance
 assertTrue(recordType.isEquivalentTo(recordType));

 // Test equality with a copy
 assertTrue(recordType.isEquivalentTo(recordTypeCopy));

 // Test inequality with a different instance
 assertFalse(recordType.isEquivalentTo(recordTypeDifferent));

 // Test inequality with a record type with a different set of properties
 assertFalse(recordType.isEquivalentTo(recordTypeDifferent2));
 }

 @Test
 public void testGetImplicitPrototype() {
 ObjectType expectedPrototype = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 assertEquals(expectedPrototype, recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 String propertyName = "newProperty";
 JSType newPropertyType = JSType.BOOLEAN;

 // Test successful definition of a property
 assertTrue(recordType.defineProperty(propertyName, newPropertyType, false, null));

 RecordProperty newProperty = recordType.getProperty(propertyName);
 assertEquals(newPropertyType, newProperty.getType());

 // Test failure to define a property after freezing
 recordType.isFrozen = true;
 assertFalse(recordType.defineProperty(propertyName, newPropertyType, false, null));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWithNullPropertyRecord() {
 recordType.isFrozen = false;
 recordType.defineProperty("property1", null, false, null);
 }

 @Test
 public void testGetGreatestSubtype() {
 RecordType recordTypeB = new RecordType(registry, properties);
 RecordType recordTypeC = new RecordType(registry, Maps.newHashMap());
 recordTypeC.defineProperty("newProperty", JSType.INTEGER, false, null);

 // Test the greatest subtype of this record type and itself
 RecordType greatestSubtypeExpected = recordType;
 assertEquals(greatestSubtypeExpected, recordType.getGreatestSubtype(recordType));

 // Test the greatest subtype of this record type and a subtype of itself
 RecordType recordTypeD = new RecordType(registry, Maps.newHashMap());
 recordTypeD.defineProperty("property2", JSType.BOOLEAN, false, null);
 RecordType greatestSubtypeExpected2 = recordTypeD;
 assertTrue(greatestSubtypeExpected2.isSubtype(recordType));
 assertEquals(greatestSubtypeExpected2, recordType.getGreatestSubtype(recordTypeD));

 // Test the greatest subtype of this record type and a different record type
 RecordType recordTypeE = new RecordType(registry, Maps.newHashMap());
 recordTypeE.defineProperty("property3", JSType.STRING, false, null);
 assertNull(recordType.getGreatestSubtype(recordTypeE));

 // Test the greatest subtype of this record type and a union type
 UnionTypeBuilder unionBuilder = new UnionTypeBuilder(registry);
 unionBuilder.addAlternate(recordTypeB);
 unionBuilder.addAlternate(JSType.STRING);
 JSType unionType = unionBuilder.build();
 assertNull(recordType.getGreatestSubtype(unionType));

 // Test the greatest subtype of this record type and a non-record type
 JSType nonRecordType = JSType.STRING;
 assertNull(recordType.getGreatestSubtype(nonRecordType));

 // Test the greatest subtype of this record type and a supertype
 JSType superType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 assertEquals(superType, recordType.getGreatestSubtype(superType));

 // Test the greatest subtype with conflicting properties
 RecordType recordTypeF = new RecordType(registry, Maps.newHashMap());
 recordTypeF.defineProperty("property1", JSType.STRING, false, null);
 RecordType recordTypeG = new RecordType(registry, Maps.newHashMap());
 recordTypeG.defineProperty("property1", JSType.NUMBER, false, null);
 assertNull(recordType.getGreatestSubtype(recordTypeG));
 }

 @Test
 public void testIsSubtype() {
 RecordType recordTypeA = new RecordType(registry, properties);
 RecordType recordTypeB = new RecordType(registry, properties);
 RecordType recordTypeC = new RecordType(registry, Maps.newHashMap());
 RecordType recordTypeD = new RecordType(registry, Maps.newHashMap());
 recordTypeD.defineProperty("property3", JSType.STRING, false, null);

 // Test that this record type is a subtype of itself
 assertTrue(recordType.isSubtype(recordType));

 // Test that this record type is a subtype of a copy of itself
 assertTrue(recordType.isSubtype(recordTypeB));

 // Test that this record type is a subtype of a record type with the same properties
 assertTrue(recordType.isSubtype(recordTypeC));

 // Test that this record type is a subtype of a record type with the same properties with different types
 RecordType recordTypeE = new RecordType(registry, properties);
 recordTypeE.getProperty("property1").setType(JSType.INTEGER);
 assertTrue(recordType.isSubtype(recordTypeE));

 // Test that this record type is a subtype of a record type with fewer properties
 assertTrue(recordType.isSubtype(new RecordType(registry, Maps.newHashMap())));

 // Test that this record type is a subtype of a record type with more properties
 assertFalse(recordType.isSubtype(recordTypeD));

 // Test that this record type is a subtype of a union type
 UnionTypeBuilder unionBuilder = new UnionTypeBuilder(registry);
 unionBuilder.addAlternate(recordTypeB);
 unionBuilder.addAlternate(JSType.STRING);
 JSType unionType = unionBuilder.build();
 assertFalse(recordType.isSubtype(unionType));

 // Test that this record type is a subtype of a non-record type
 assertFalse(recordType.isSubtype(JSType.STRING));

 // Test that this record type is a subtype of an object type
 assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));

 // Test that a subtype of this record type is a subtype of this record type
 RecordType recordTypeF = new RecordType(registry, properties);
 recordTypeF.isFrozen = true;
 assertTrue(recordType.isSubtype(recordTypeF));
 }

 @Test
 public void testIsFrozen() {
 // Test the default state of the record type
 assertFalse(recordType.isFrozen);

 // Test the state after freezing it
 recordType.isFrozen = true;
 assertTrue(recordType.isFrozen);
 }

 @Test
 public void testToMaybeRecordType() {
 assertEquals(recordType, recordType.toMaybeRecordType());
 }
}