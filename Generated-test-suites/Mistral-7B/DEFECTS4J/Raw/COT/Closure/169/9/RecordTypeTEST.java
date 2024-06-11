import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Test;

public class RecordTypeTest {

 private static RecordProperty createProperty(String name, JSType type) {
 return new RecordProperty(name, type, null);
 }

 @Test
 public void testRecordTypeConstructor() {
 // Given
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("name1", createProperty("name1", JSType.STRING));
 properties.put("name2", createProperty("name2", JSType.NUMBER));

 // When
 RecordType recordType = new RecordType(registry, properties, true);

 // Then
 assertEquals(registry, recordType.getTypeRegistry());
 assertFalse(recordType.isSynthetic());
 assertTrue(recordType.isFrozen());

 // And
 assertEquals(2, recordType.getPropertyCount());
 assertEquals("name1", recordType.getPropertyNames().get(0));
 assertEquals("name2", recordType.getPropertyNames().get(1));

 assertEquals(JSType.STRING, recordType.getPropertyType("name1"));
 assertEquals(JSType.NUMBER, recordType.getPropertyType("name2"));
 }

 @Test
 public void testRecordTypeConstructorWithNullProperty() {
 // Given
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("name1", createProperty("name1", JSType.STRING));

 // When & Then
 assertThrows(IllegalStateException.class, () -> {
 properties.put(null, createProperty(null, JSType.STRING));
 new RecordType(registry, properties, true);
 });
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 // Given
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("name1", createProperty("name1", JSType.STRING));
 RecordType recordType1 = new RecordType(registry, properties1, true);

 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("name1", createProperty("name1", JSType.STRING));
 properties2.put("name2", createProperty("name2", JSType.STRING));
 RecordType recordType2 = new RecordType(registry, properties2, true);

 // When
 boolean result = recordType1.checkRecordEquivalenceHelper(recordType2, false);

 // Then
 assertTrue(result);
 }

 @Test
 public void testCheckRecordEquivalenceHelperWithNotEqualPropertySet() {
 // Given
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("name1", createProperty("name1", JSType.STRING));
 RecordType recordType1 = new RecordType(registry, properties1, true);

 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("name2", createProperty("name2", JSType.STRING));
 RecordType recordType2 = new RecordType(registry, properties2, true);

 // When
 boolean result = recordType1.checkRecordEquivalenceHelper(recordType2, false);

 // Then
 assertFalse(result);
 }

 @Test
 public void testCheckRecordEquivalenceHelperWithConflictingProperties() {
 // Given
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("name1", createProperty("name1", JSType.STRING));
 properties1.put("name2", createProperty("name2", JSType.NUMBER));
 RecordType recordType1 = new RecordType(registry, properties1, true);

 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("name1", createProperty("name1", JSType.NUMBER));
 properties2.put("name2", createProperty("name2", JSType.STRING));
 RecordType recordType2 = new RecordType(registry, properties2, true);

 // When
 boolean result = recordType1.checkRecordEquivalenceHelper(recordType2, false);

 // Then
 assertFalse(result);
 }

 @Test
 public void testCheckRecordEquivalenceHelperWithTolerateUnknownsTrue() {
 // Given
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("name1", createProperty("name1", JSType.STRING));
 RecordType recordType1 = new RecordType(registry, properties1, true);

 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("name1", createProperty("name1", JSType.NUMBER));
 RecordType recordType2 = new RecordType(registry, properties2, true);

 // When
 boolean result = recordType1.checkRecordEquivalenceHelper(recordType2, true);
 // Then
 // This test case should not fail, because "tolerateUnknowns" is set to true
 // and the unknown property types are ignored when comparing the record types
 assertTrue(result);
 }

 @Test
 public void testGetImplicitPrototype() {
 // Given
 RecordType recordType = new RecordType(new RecordTypeRegistry(),
 Maps.newHashMap(), true);

 // When
 ObjectType implicitPrototype = recordType.getImplicitPrototype();

 // Then
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
 }

 @Test
 public void testDefineProperty() {
 // Given
 RecordType recordType = new RecordType(new RecordTypeRegistry(),
 Maps.newHashMap(), true);

 // When
 recordType.defineProperty("name", JSType.STRING, false, null);

 // Then
 assertTrue(recordType.hasProperty("name"));
 assertEquals(JSType.STRING, recordType.getPropertyType("name"));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWhenFrozen() {
 // Given
 RecordType recordType = new RecordType(new RecordTypeRegistry(),
 Maps.newHashMap(), true);
 recordType.isFrozen = true;

 // When
 recordType.defineProperty("name", JSType.STRING, false, null);

 // Then
 fail("Method should have thrown an exception");
 }

 @Test
 public void testGetGreatestSubtypeHelperWithEmptyType() {
 // Given
 RecordTypeRegistry registry = new RecordTypeRegistry();
 JSType that = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 // When
 JSType greatestSubtype = new RecordType(registry, Maps.newHashMap(), false)
 .getGreatestSubtypeHelper(that);

 // Then
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype);
 }

 @Test
 public void testGetGreatestSubtypeHelperWithObjectType() {
 // Given
 RecordTypeRegistry registry = new RecordTypeRegistry();
 JSTypeType objectType = registry.getNativeType(JSTypeNative.OBJECT_TYPE);
 JSType that = registry.getNativeType(JSTypeNative.NUMBER_TYPE);

 // When
 JSType greatestSubtype = new RecordType(registry, Maps.newHashMap(), false)
 .getGreatestSubtypeHelper(that);

 // Then
 assertTrue(greatestSubtype.isEmptyType());
 }

 @Test
 public void testGetGreatestSubtypeHelperWithRecordTypeSubtype() {
 // Given
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("name1", createProperty("name1", JSType.STRING));
 RecordType recordType1 = new RecordType(registry, properties1, true);

 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("name1", createProperty("name1", JSType.STRING));
 properties2.put("name2", createProperty("name2", JSType.NUMBER));
 RecordType recordType2 = new RecordType(registry, properties2, true);

 // When
 JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

 // Then
 assertEquals(recordType1, greatestSubtype);
 }

 @Test
 public void testGetGreatestSubtypeHelperWithRecordTypeSupertype() {
 // Given
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("name1", createProperty("name1", JSType.STRING));
 properties1.put("name2", createProperty("name2", JSType.NUMBER));
 RecordType recordType1 = new RecordType(registry, properties1, true);

 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("name1", createProperty("name1", JSType.STRING));
 RecordType recordType2 = new RecordType(registry, properties2, true);

 // When
 JSType greatestSubtype = recordType2.getGreatestSubtypeHelper(recordType1);

 // Then
 assertEquals(recordType1, greatestSubtype);
 }

 @Test
 public void testGetGreatestSubtypeHelperWithConflictingProperties() {
 // Given
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("name1", createProperty("name1", JSType.STRING));
 properties1.put("name2", createProperty("name2", JSType.NUMBER));
 RecordType recordType1 = new RecordType(registry, properties1, true);

 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("name1", createProperty("name1", JSType.NUMBER));
 properties2.put("name2", createProperty("name2", JSType.STRING));
 RecordType recordType2 = new RecordType(registry, properties2, true);

 // When
 JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

 // Then
 assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), greatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 // Given
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("name1", createProperty("name1", JSType.STRING));
 RecordType recordType1 = new RecordType(registry, properties1, true);

 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("name1", createProperty("name1", JSType.STRING));
 properties2.put("name2", createProperty("name2", JSType.NUMBER));
 RecordType recordType2 = new RecordType(registry, properties2, true);

 // When
 boolean result = recordType1.isSubtype(recordType2);

 // Then
 assertTrue(result);
 }

 @Test
 public void testIsSubtypeWithIncompatiblePropertyType() {
 // Given
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("name1", createProperty("name1", JSType.STRING));
 RecordType recordType1 = new RecordType(registry, properties1, true);

 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("name1", createProperty("name1", JSType.NUMBER));
 RecordType recordType2 = new RecordType(registry, properties2, true);

 // When
 boolean result = recordType1.isSubtype(recordType2);

 // Then
 assertFalse(result);
 }

 @Test
 public void testIsSubtypeWithMissingProperty() {
 // Given
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("name1", createProperty("name1", JSType.STRING));
 properties1.put("name2", createProperty("name2", JSType.NUMBER));
 RecordType recordType1 = new RecordType(registry, properties1, true);

 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("name1", createProperty("name1", JSType.STRING));
 RecordType recordType2 = new RecordType(registry, properties2, true);

 // When
 boolean result = recordType1.isSubtype(recordType2);

 // Then
 assertTrue(result);
 }

 @Test
 public void testIsSubtypeWithMissingPropertyIncompatibleType() {
 // Given
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("name1", createProperty("name1", JSType.STRING));
 properties1.put("name2", createProperty("name2", JSType.NUMBER));
 RecordType recordType1 = new RecordType(registry, properties1, true);

 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("name1", createProperty("name1", JSType.STRING));
 properties2.put("name3", createProperty("name3", JSType.NUMBER));
 RecordType recordType2 = new RecordType(registry, properties2, true);

 // When
 boolean result = recordType1.isSubtype(recordType2);

 // Then
 assertFalse(result);
 }

 @Test
 public void testIsSubtypeWithRecordTypeAndUnionType() {
 // Given
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("name1", createProperty("name1", JSType.STRING));
 RecordType recordType1 = new RecordType(registry, properties1, true);

 UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
 unionTypeBuilder.addAlternate(registry.getNativeType(JSTypeNative.OBJECT_TYPE));
 unionTypeBuilder.addAlternate(registry.getNativeType(JSTypeNative.NUMBER_TYPE));
 JSType unionType = unionTypeBuilder.build();

 // When
 boolean result = recordType1.isSubtype(unionType);

 // Then
 assertFalse(result);
 }

 @Test
 public void testIsSubtypeWithUnionTypeAndRecordType() {
 // Given
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("name1", createProperty("name1", JSType.STRING));
 RecordType recordType1 = new RecordType(registry, properties1, true);

 UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
 unionTypeBuilder.addAlternate(registry.getNativeType(JSTypeNative.OBJECT_TYPE));
 unionTypeBuilder.addAlternate(registry.getNativeType(JSTypeNative.NUMBER_TYPE));
 JSType unionType = unionTypeBuilder.build();

 // When
 boolean result = unionType.isSubtype(recordType1);

 // Then
 assertTrue(result);
 }

 @Test
 public void testResolveInternal() {
 // Given
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("name", createProperty("name", JSType.STRING));
 RecordType recordType = new RecordType(registry, properties, true);
 JSType type = JSType.INT;

 // When
 JSType resolvedType = recordType.resolveInternal(null, null);

 // Then
 assertEquals(type, resolvedType);
 }
}