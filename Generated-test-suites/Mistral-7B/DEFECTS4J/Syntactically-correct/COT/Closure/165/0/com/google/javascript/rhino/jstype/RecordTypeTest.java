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

 private static RecordProperty createProperty(String name, JSType type) {
 return new RecordProperty(name, type, null);
 }

 private static RecordType createRecordType(JSTypeRegistry registry,
 Map<String, RecordProperty> properties) {
 return new RecordType(registry, properties);
 }

 @Test
 public void testCreateRecordType() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("name", createProperty("name", JSType.STRING));
 RecordType recordType = createRecordType(registry, properties);
 assertEquals("RecordType", recordType.getName());
 }

 @Test(expected = IllegalStateException.class)
 public void testRecordTypeWithNullProperty() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("name", createProperty("name", null));
 try {
 RecordType recordType = new RecordType(registry, properties);
 fail();
 } catch (IllegalStateException e) {
 assertEquals("RecordProperty associated with a property should not be null!", e.getMessage());
 }
 }

 @Test
 public void testIsEquivalentTo() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("name1", createProperty("name1", JSType.INT));
 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("name2", createProperty("name1", JSType.INT));
 properties2.put("name2", createProperty("name2", JSType.INT));
 RecordType recordType1 = createRecordType(registry, properties1);
 RecordType recordType2 = createRecordType(registry, properties2);
 assertTrue(recordType1.isEquivalentTo(recordType2));
 }

 @Test
 public void testIsEquivalentToDifferentProperties() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("name1", createProperty("name1", JSType.INT));
 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("name2", createProperty("name2", JSType.INT));
 RecordType recordType1 = createRecordType(registry, properties1);
 RecordType recordType2 = createRecordType(registry, properties2);
 assertFalse(recordType1.isEquivalentTo(recordType2));
 }

 @Test
 public void testGetImplicitPrototype() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType = new RecordType(registry, Maps.newHashMap());
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("name", createProperty("name", JSType.INT));
 RecordType recordType = new RecordType(registry, properties);
 recordType.defineProperty("name", JSType.STRING, false, null);
 JSType type = recordType.getPropertyType("name");
 assertEquals(JSType.STRING, type);
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyFrozenRecord() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("name", createProperty("name", JSType.INT));
 RecordType recordType = new RecordType(registry, properties);
 recordType.isFrozen = true;
 try {
 recordType.defineProperty("name", JSType.STRING, false, null);
 fail();
 } catch (IllegalStateException e) {
 assertEquals("Cannot modify frozen record type", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDefinePropertyWithInferredProperty() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("name", createProperty("name", null));
 RecordType recordType = new RecordType(registry, properties);
 try {
 recordType.defineProperty("name", JSType.STRING, true, null);
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("Cannot define inferred property on a non-synthesized record type", e.getMessage());
 }
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 recordType1.defineProperty("name1", JSType.INT, false, null);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("name1", JSType.INT, false, null);
 recordType2.defineProperty("name2", JSType.INT, false, null);
 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
 recordType3.defineProperty("name1", JSType.INT, false, null);
 recordType3.defineProperty("name2", JSType.STRING, false, null);
 JSType type1 = recordType1.getGreatestSubtypeHelper(recordType2);
 assertEquals(recordType2, type1);
 JSType type2 = recordType1.getGreatestSubtypeHelper(recordType3);
 assertEquals(recordType3, type2);
 }

 @Test
 public void testIsSubtype() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 recordType1.defineProperty("name1", JSType.INT, false, null);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("name1", JSType.INT, false, null);
 recordType2.defineProperty("name2", JSType.INT, false, null);
 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
 recordType3.defineProperty("name1", JSType.INT, false, null);
 recordType3.defineProperty("name2", JSType.STRING, false, null);
 RecordType recordType4 = new RecordType(registry, Maps.newHashMap());
 recordType4.defineProperty("name1", JSType.INT, false, null);
 recordType4.defineProperty("name2", JSType.STRING, false, null);
 recordType4.defineProperty("name3", JSType.INT, false, null);
 RecordType recordType5 = new RecordType(registry, Maps.newHashMap());
 recordType5.defineProperty("name1", JSType.INT, false, null);
 recordType5.defineProperty("name2", JSType.STRING, false, null);
 RecordType recordType6 = new RecordType(registry, Maps.newHashMap());
 recordType6.defineProperty("name1", JSType.STRING, false, null);
 RecordType recordType7 = new RecordType(registry, Maps.newHashMap());
 recordType7.defineProperty("name1", JSType.INT, false, null);

 assertTrue(recordType1.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(recordType2));
 assertFalse(recordType1.isSubtype(recordType3));
 assertFalse(recordType1.isSubtype(recordType4));
 assertFalse(recordType1.isSubtype(recordType5));
 assertTrue(recordType2.isSubtype(recordType1));
 assertTrue(recordType2.isSubtype(recordType2));
 assertTrue(recordType2.isSubtype(recordType3));
 assertTrue(recordType2.isSubtype(recordType4));
 assertTrue(recordType2.isSubtype(recordType5));
 assertFalse(recordType2.isSubtype(recordType6));
 assertFalse(recordType3.isSubtype(recordType1));
 assertFalse(recordType3.isSubtype(recordType2));
 assertTrue(recordType3.isSubtype(recordType3));
 assertFalse(recordType3.isSubtype(recordType4));
 assertTrue(recordType3.isSubtype(recordType5));
 assertFalse(recordType3.isSubtype(recordType6));
 assertTrue(recordType4.isSubtype(recordType1));
 assertTrue(recordType4.isSubtype(recordType2));
 assertTrue(recordType4.isSubtype(recordType3));
 assertTrue(recordType4.isSubtype(recordType4));
 assertTrue(recordType4.isSubtype(recordType5));
 assertFalse(recordType4.isSubtype(recordType6));
 assertTrue(recordType5.isSubtype(recordType1));
 assertTrue(recordType5.isSubtype(recordType2));
 assertTrue(recordType5.isSubtype(recordType3));
 assertTrue(recordType5.isSubtype(recordType4));
 assertTrue(recordType5.isSubtype(recordType5));
 assertFalse(recordType5.isSubtype(recordType6));
 }

 @Test
 public void testToMaybeRecordType() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType = new RecordType(registry, Maps.newHashMap());
 assertEquals(recordType, recordType.toMaybeRecordType());
 }

 @Test(expected = ClassCastException.class)
 public void testToMaybeRecordTypeNonRecordType() {
 JSTypeRegistry registry = new JSTypeRegistry();
 UnionTypeBuilder builder = new UnionTypeBuilder(registry);
 UnionType unionType = builder.build();
 assertEquals(unionType, unionType.toMaybeRecordType());
 fail();
 }

 @Test
 public void testIsSubtypeRecordToRecord() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 recordType1.defineProperty("name1", JSType.INT, false, null);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("name1", JSType.INT, false, null);
 recordType2.defineProperty("name2", JSType.INT, false, null);
 assertTrue(RecordType.isSubtype(recordType1, recordType2));
 }

 @Test
 public void testIsSubtypeRecordToUnionType() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 recordType1.defineProperty("name1", JSType.INT, false, null);
 UnionTypeBuilder builder = new UnionTypeBuilder(registry);
 builder.addAlternate(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
 UnionType unionType = builder.build();
 assertFalse(RecordType.isSubtype(recordType1, unionType));
 }

 @Test
 public void testIsSubtypeRecordToRecordWithDifferentProperties() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 recordType1.defineProperty("name1", JSType.INT, false, null);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("name2", JSType.INT, false, null);
 recordType2.defineProperty("name1", JSType.INT, false, null);
 assertFalse(RecordType.isSubtype(recordType1, recordType2));
 }

 @Test
 public void testIsSubtypeRecordToRecordWithSameProperties() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 recordType1.defineProperty("name1", JSType.INT, false, null);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("name1", JSType.INT, false, null);
 recordType2.defineProperty("name2", JSType.INT, false, null);
 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
 recordType3.defineProperty("name1", JSType.INT, false, null);
 recordType3.defineProperty("name2", JSType.INT, false, null);
 recordType3.defineProperty("name3", JSType.INT, false, null);
 assertTrue(RecordType.isSubtype(recordType1, recordType2));
 assertTrue(RecordType.isSubtype(recordType1, recordType3));
 }

 @Test
 public void testResolveInternal() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType = new RecordType(registry, Maps.newHashMap());
 recordType.defineProperty("name", JSType.INT, false, null);
 RecordProperty recordProperty = recordType.getProperty(0);
 Node propertyNode = recordProperty.getPropertyNode();
 JSType resolvedType = recordType.resolveInternal(null, null);
 recordType.resolveInternal(null, null);
 JSType resolvedType1 = recordType.getPropertyType("name");
 assertEquals(JSType.INT, resolvedType);
 assertEquals(JSType.INT, resolvedType1);
 }
}