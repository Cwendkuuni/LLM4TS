import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSType.*;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @Before
 public void setup() {
 registry = new JSTypeRegistry();
 RecordTypeBuilder builder = new RecordTypeBuilder(registry);
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("prop1", new RecordProperty("prop1", intType()));
 properties.put("prop2", new RecordProperty("prop2", stringType()));
 recordType = new RecordType(registry, properties);
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);
 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());

 // Equivalent record types
 assertTrue(recordType1.isEquivalentTo(recordType2));
 // Non-equivalent record types
 assertFalse(recordType1.isEquivalentTo(recordType3));
 // Record type and non-record type
 assertFalse(recordType1.isEquivalentTo(intType()));
 }

 @Test
 public void testGetImplicitPrototype() {
 ObjectType implicitPrototype = recordType.getImplicitPrototype();
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
 }

 @Test
 public void testDefineProperty() {
 // Check that property can be defined
 recordType.defineProperty("prop3", intType(), false, null);
 assertTrue(recordType.hasProperty("prop3"));

 // Check that property cannot be defined once the record is frozen
 recordType.isFrozen = true;
 assertFalse(recordType.defineProperty("prop3", intType(), false, null));
 }

 @Test
 public void testGetLeastSupertype() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("prop3", intType(), false, null);
 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
 recordType3.defineProperty("prop3", longType(), false, null);

 // Record type is a supertype of itself
 assertEquals(recordType1, recordType1.getLeastSupertype(recordType1));
 // Empty record type is a supertype of any record type
 assertEquals(recordType1, recordType1.getLeastSupertype(recordType2));
 // Record type is a supertype of a record type with the same properties
 assertEquals(recordType1, recordType1.getLeastSupertype(recordType1));
 // Record type is a supertype of a record type with additional properties
 assertEquals(recordType1, recordType1.getLeastSupertype(recordType3));
 // Record type with incompatible property types is not a supertype
 assertNotEquals(recordType1, recordType2.getLeastSupertype(recordType1));
 // Record type with incompatible property types is not a supertype
 assertNotEquals(recordType1, recordType3.getLeastSupertype(recordType1));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("prop3", intType(), false, null);
 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
 recordType3.defineProperty("prop3", longType(), false, null);
 RecordType recordType4 = new RecordType(registry, Maps.newHashMap());
 recordType4.defineProperty("prop3", floatType(), false, null);
 recordType4.defineProperty("prop4", stringType(), false, null);

 // Record type is a subtype of itself
 assertEquals(recordType1, recordType1.getGreatestSubtype(recordType1));
 // Empty record type is a subtype of any record type
 assertEquals(recordType1, recordType1.getGreatestSubtype(recordType2));
 // Record type is a subtype of a record type with the same properties
 assertEquals(recordType1, recordType1.getGreatestSubtype(recordType1));
 // Record type is a subtype of a record type with additional properties
 assertEquals(recordType3, recordType1.getGreatestSubtype(recordType3));
 // Record type is not a subtype of a record type with incompatible property types
 assertNotEquals(recordType1, recordType3.getGreatestSubtype(recordType1));
 // Record type is not a subtype of a record type with incompatible property names
 assertNotEquals(recordType1, recordType4.getGreatestSubtype(recordType1));
 }

 @Test
 public void testIsSubtype() {
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("prop3", intType(), false, null);
 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
 recordType3.defineProperty("prop3", longType(), false, null);
 RecordType recordType4 = new RecordType(registry, Maps.newHashMap());
 recordType4.defineProperty("prop3", floatType(), false, null);
 recordType4.defineProperty("prop4", stringType(), false, null);

 // Record type is a subtype of itself
 assertTrue(recordType1.isSubtype(recordType1));
 // Empty record type is a subtype of any record type
 assertTrue(recordType1.isSubtype(recordType2));
 // Record type is a subtype of a record type with the same properties
 assertTrue(recordType1.isSubtype(recordType1));
 // Record type is a subtype of a record type with additional properties
 assertTrue(recordType1.isSubtype(recordType3));
 // Record type with incompatible property types is not a subtype
 assertFalse(recordType1.isSubtype(recordType3));
 // Record type with incompatible property names is not a subtype
 assertFalse(recordType1.isSubtype(recordType4));
 // Object type is a subtype of any record type
 assertTrue(objectType.isSubtype(recordType1));
 }

 @Test
 public void testResolveInternal() {
 // Test that properties are resolved correctly
 RecordType recordType = new RecordType(registry, properties);
 JSType type1 = intType();
 JSType type2 = stringType();
 recordType.properties.put("prop1", type1);
 recordType.properties.put("prop2", type2);
 JSType resolvedType = recordType.resolveInternal(null, null);
 assertEquals(recordType, resolvedType);

 for (Map.Entry<String, JSType> entry : recordType.properties.entrySet()) {
 JSType propertyType = entry.getValue();
 JSType resolvedPropertyType =
 ((RecordType) resolvedType).getPropertyType(entry.getKey());
 assertEquals(propertyType, resolvedPropertyType);
 }
 }
}