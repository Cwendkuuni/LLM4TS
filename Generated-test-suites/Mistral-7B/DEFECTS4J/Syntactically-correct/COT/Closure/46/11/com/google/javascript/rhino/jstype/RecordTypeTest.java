import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

 private JSTypeRegistry registry;

 @BeforeClass
 public void setUp() {
 registry = new JSTypeRegistry();
 }

 @Test
 public void testRecordTypeConstruction() {
 // Test basic construction
 RecordProperty property1 = new RecordProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE));
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("name", property1);
 RecordType recordType = new RecordType(registry, properties);
 assertEquals("RecordType", recordType.getClass().getSimpleName());
 assertEquals("{name : String}", recordType.toString());

 // Test construction with frozen property
 properties.put("age", registry.getNativeType(JSTypeNative.NUMBER_TYPE));
 RecordType frozenRecordType = new RecordType(registry, properties);
 assertFalse(frozenRecordType.isFrozen());

 // Test construction with frozen property set
 properties.put("age", registry.getNativeType(JSTypeNative.NUMBER_TYPE));
 RecordType frozenRecordTypeWithFrozenPropertySet = new RecordType(registry, properties);
 frozenRecordTypeWithFrozenPropertySet.freeze();
 assertTrue(frozenRecordTypeWithFrozenPropertySet.isFrozen());
 }

 @Test
 public void testIsEquivalentTo() {
 RecordProperty property1 = new RecordProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE));
 RecordProperty property2 = new RecordProperty("age", registry.getNativeType(JSTypeNative.NUMBER_TYPE));
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("name", property1);
 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("name", property1);
 properties2.put("age", property2);

 RecordType recordType1 = new RecordType(registry, properties1);
 RecordType recordType2 = new RecordType(registry, properties2);

 assertTrue(recordType1.isEquivalentTo(recordType1));
 assertTrue(recordType1.isEquivalentTo(recordType2));

 RecordType recordType3 = new RecordType(registry, properties2);
 assertFalse(recordType1.isEquivalentTo(recordType3));
 }

 @Test
 public void testGetImplicitPrototype() {
 RecordProperty property = new RecordProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE));
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("name", property);
 RecordType recordType = new RecordType(registry, properties);

 ObjectType implicitPrototype = recordType.getImplicitPrototype();
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
 }

 @Test
 public void testDefineProperty() {
 RecordProperty property = new RecordProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE));
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("name", property);
 RecordType recordType = new RecordType(registry, properties);

 // Test defineProperty with existing property
 assertFalse(recordType.defineProperty("name", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null));

 // Test defineProperty with non-existing property
 assertTrue(recordType.defineProperty("age", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null));
 }

 @Test
 public void testGetLeastSupertype() {
 RecordProperty property1 = new RecordProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE));
 RecordProperty property2 = new RecordProperty("age", registry.getNativeType(JSTypeNative.NUMBER_TYPE));
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("name", property1);
 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("name", property1);
 properties2.put("age", property2);

 RecordType recordType1 = new RecordType(registry, properties1);
 RecordType recordType2 = new RecordType(registry, properties2);

 JSType leastSupertype = recordType1.getLeastSupertype(recordType2);
 assertEquals(recordType1.getClass(), leastSupertype.getClass());
 assertEquals(recordType1, leastSupertype);

 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType4 = new RecordType(registry, properties2);
 JSType greatestSubtype = recordType3.getLeastSupertype(recordType4);
 assertEquals(recordType4.getClass(), greatestSubtype.getClass());
 assertEquals(recordType4, greatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 RecordProperty property1 = new RecordProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE));
 RecordProperty property2 = new RecordProperty("age", registry.getNativeType(JSTypeNative.NUMBER_TYPE));
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("name", property1);
 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("name", property1);
 properties2.put("age", property2);

 RecordType recordType1 = new RecordType(registry, properties1);
 RecordType recordType2 = new RecordType(registry, properties2);
 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType4 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType5 = new RecordType(registry, properties1);
 RecordType recordType6 = new RecordType(registry, properties2);
 RecordType recordType7 = new RecordType(registry, properties2);
 RecordType recordType8 = new RecordType(registry, properties2);
 RecordType recordType9 = new RecordType(registry, properties2);

 // Test with empty record type as base
 assertFalse(recordType1.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
 assertFalse(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isSubtype(recordType1));

 // Test with empty record type as subtype
 assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isSubtype(recordType1));

 // Test subtypes of record type 1
 assertTrue(recordType1.isSubtype(recordType1));
 assertFalse(recordType1.isSubtype(recordType2));
 assertFalse(recordType2.isSubtype(recordType1));

 // Test subtypes of record type 2
 // Test subtypes with same properties and types
 assertTrue(recordType2.isSubtype(recordType5));
 assertTrue(recordType5.isSubtype(recordType2));
 // Test subtypes with same properties but different types
 assertFalse(recordType2.isSubtype(recordType3));
 assertFalse(recordType3.isSubtype(recordType2));
 // Test subtypes with different properties but compatible types
 assertFalse(recordType2.isSubtype(recordType4));
 assertFalse(recordType4.isSubtype(recordType2));
 // Test subtypes with different properties and incompatible types
 assertFalse(recordType2.isSubtype(recordType6)); // Different property
 assertFalse(recordType6.isSubtype(recordType2)); // Different property type
 // Test subtypes with different properties and compatible types
 assertFalse(recordType2.isSubtype(recordType7)); // Different property
 assertTrue(recordType7.isSubtype(recordType2)); // Same type but different property
 // Test subtypes with the same property but different types
 assertTrue(recordType2.isSubtype(recordType8)); // Property exists in both
 assertFalse(recordType8.isSubtype(recordType2)); // Different property type
 // Test subtypes with the same property but same type but different property nodes
 assertTrue(recordType2.isSubtype(recordType9)); // Property exists in both
 assertTrue(recordType9.isSubtype(recordType2)); // Property exists in both
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWithFrozenRecord() {
 RecordProperty property = new RecordProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE));
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("name", property);
 RecordType recordType = new RecordType(registry, properties);
 recordType.freeze();
 assertFalse(recordType.defineProperty("name", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null));
 }
}