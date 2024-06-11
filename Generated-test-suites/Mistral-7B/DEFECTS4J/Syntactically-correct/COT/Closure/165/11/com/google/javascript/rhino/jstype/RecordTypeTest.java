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

 @Test
 public void testRecordTypeCreation() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties = new Maps.NewHashMap<>();
 properties.put("a", new RecordProperty("a", JSType.STRING));
 properties.put("b", new RecordProperty("b", JSType.NUMBER));
 RecordType recordType = new RecordType(registry, properties);
 assertNotNull(recordType);
 }

 @Test
 public void testIsEquivalentTo() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties1 = new Maps.NewHashMap<>();
 properties1.put("a", new RecordProperty("a", JSType.STRING));
 RecordType recordType1 = new RecordType(registry, properties1);

 Map<String, RecordProperty> properties2 = new Maps.NewHashMap<>();
 properties2.put("a", new RecordProperty("a", JSType.STRING));
 properties2.put("b", new RecordProperty("b", JSType.NUMBER));
 RecordType recordType2 = new RecordType(registry, properties2);

 assertTrue(recordType1.isEquivalentTo(recordType1));
 assertTrue(recordType1.isEquivalentTo(recordType2));
 }

 @Test
 public void testGetImplicitPrototype() {
 RecordType recordType = new RecordType(new RecordTypeRegistry(), null);
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties = new Maps.NewHashMap<>();
 RecordType recordType = new RecordType(registry, properties);

 Node propertyNode = Node.newIdent("p");
 JSType propertyType = JSType.BOOLEAN;
 recordType.defineProperty("p", propertyType, false, propertyNode);
 assertTrue(recordType.hasProperty("p"));
 assertEquals(propertyType, recordType.getPropertyType("p"));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWithNullRecordProperty() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties = new Maps.NewHashMap<>();
 RecordType recordType = new RecordType(registry, properties);
 RecordProperty nullRecordProperty = null;
 recordType.defineProperty("p", JSType.BOOLEAN, false, nullRecordProperty);
 fail("Should throw IllegalStateException");
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 JSType type1 = JSType.STRING;
 JSType type2 = JSType.NUMBER;
 RecordType recordType1 = new RecordType(registry, null);
 recordType1.defineProperty("p", type1, false, Node.newIdent("p"));

 RecordType recordType2 = new RecordType(registry, null);
 recordType2.defineProperty("p", type2, false, Node.newIdent("p"));
 recordType2.defineProperty("q", JSTypeNative.NO_TYPE, false, Node.newIdent("q"));

 JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 assertNotNull(greatestSubtype);
 assertEquals(JSTypeNative.NO_TYPE, greatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 RecordType recordType1 = new RecordType(registry, null);
 recordType1.defineProperty("p", JSType.STRING, false, Node.newIdent("p"));

 RecordType recordType2 = new RecordType(registry, null);
 recordType2.defineProperty("p", JSType.NUMBER, false, Node.newIdent("p"));
 recordType2.defineProperty("q", JSType.NUMBER, false, Node.newIdent("q"));

 assertTrue(recordType1.isSubtype(objectType));
 assertTrue(objectType.isSubtype(recordType1));

 assertFalse(recordType1.isSubtype(recordType2));
 assertFalse(recordType2.isSubtype(recordType1));
 }

 @Test
 public void testIsSubtypeWithRecordTypeSubClass() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 RecordType recordType1 = new RecordType(registry, null);
 recordType1.defineProperty("p", JSType.STRING, false, Node.newIdent("p"));

 RecordType recordType2 = new RecordType(registry, null);
 recordType2.defineProperty("p", JSType.STRING, false, Node.newIdent("p"));
 recordType2.defineProperty("q", JSType.NUMBER, false, Node.newIdent("q"));

 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithRecordTypeSuperClass() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 RecordType recordType1 = new RecordType(registry, null);
 recordType1.defineProperty("p", JSType.STRING, false, Node.newIdent("p"));
 recordType1.defineProperty("q", JSType.NUMBER, false, Node.newIdent("q"));

 RecordType recordType2 = new RecordType(registry, null);
 recordType2.defineProperty("p", JSType.STRING, false, Node.newIdent("p"));

 assertTrue(recordType2.isSubtype(recordType1));
 }

 @Test
 public void testIsSubtypeWithRecordTypeSameType() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 RecordType recordType1 = new RecordType(registry, null);
 recordType1.defineProperty("p", JSType.STRING, false, Node.newIdent("p"));

 RecordType recordType2 = new RecordType(registry, null);
 recordType2.defineProperty("p", JSType.STRING, false, Node.newIdent("p"));

 assertTrue(recordType1.isSubtype(recordType2));
 assertTrue(recordType2.isSubtype(recordType1));
 }

 @Test
 public void testIsSubtypeWithNonRecordType() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 RecordType recordType = new RecordType(registry, null);
 recordType.defineProperty("p", JSType.STRING, false, Node.newIdent("p"));

 assertFalse(recordType.isSubtype(objectType));
 assertFalse(objectType.isSubtype(recordType));
 }

 @Test
 public void testIsSubtypeWithRecordTypeAndNonRecordType() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 RecordType recordType = new RecordType(registry, null);
 recordType.defineProperty("p", JSType.STRING, false, Node.newIdent("p"));

 assertFalse(recordType.isSubtype(objectType));
 assertFalse(objectType.isSubtype(recordType));
 }

 @Test
 public void testIsSubtypeWithRecordTypeAndRecordTypeWithCommonProperties() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 RecordType recordType1 = new RecordType(registry, null);
 recordType1.defineProperty("p", JSType.STRING, false, Node.newIdent("p"));
 recordType1.defineProperty("q", JSType.NUMBER, false, Node.newIdent("q"));

 RecordType recordType2 = new RecordType(registry, null);
 recordType2.defineProperty("p", JSType.STRING, false, Node.newIdent("p"));
 recordType2.defineProperty("q", JSType.NUMBER, false, Node.newIdent("q"));

 assertTrue(recordType1.isSubtype(recordType2));
 assertTrue(recordType2.isSubtype(recordType1));
 }

 @Test
 public void testIsSubtypeWithRecordTypeAndRecordTypeWithDifferentProperties() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 RecordType recordType1 = new RecordType(registry, null);
 recordType1.defineProperty("p", JSType.STRING, false, Node.newIdent("p"));
 recordType1.defineProperty("q", JSType.NUMBER, false, Node.newIdent("q"));

 RecordType recordType2 = new RecordType(registry, null);
 recordType2.defineProperty("p", JSType.STRING, false, Node.newIdent("p"));
 recordType2.defineProperty("r", JSType.NUMBER, false, Node.newIdent("r"));

 assertFalse(recordType1.isSubtype(recordType2));
 assertFalse(recordType2.isSubtype(recordType1));
 }

 @Test
 public void testIsSubtypeWithRecordTypeAndRecordTypeWithCompatibleProperties() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 RecordType recordType1 = new RecordType(registry, null);
 recordType1.defineProperty("p", JSType.STRING, false, Node.newIdent("p"));
 recordType1.defineProperty("q", JSType.NUMBER, false, Node.newIdent("q"));

 RecordType recordType2 = new RecordType(registry, null);
 recordType2.defineProperty("p", JSType.STRING, false, Node.newIdent("p"));
 recordType2.defineProperty("r", JSType.NUMBER, false, Node.newIdent("r"));
 recordType2.defineProperty("q", JSType.NUMBER, false, Node.newIdent("q"));

 assertTrue(recordType1.isSubtype(recordType2));
 assertTrue(recordType2.isSubtype(recordType1));
 }

 @Test
 public void testIsSubtypeWithRecordTypeAndRecordTypeWithIncompatibleProperties() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 RecordType recordType1 = new RecordType(registry, null);
 recordType1.defineProperty("p", JSType.STRING, false, Node.newIdent("p"));
 recordType1.defineProperty("q", JSType.NUMBER, false, Node.newIdent("q"));

 RecordType recordType2 = new RecordType(registry, null);
 recordType2.defineProperty("p", JSType.STRING, false, Node.newIdent("p"));
 recordType2.defineProperty("r", JSType.NUMBER, false, Node.newIdent("r"));
 recordType2.defineProperty("q", JSType.BOOLEAN, false, Node.newIdent("q"));

 assertFalse(recordType1.isSubtype(recordType2));
 assertFalse(recordType2.isSubtype(recordType1));
 }

 @Test
 public void testIsSubtypeWithRecordTypeAndRecordTypeWithEmptyProperties() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 RecordType recordType1 = new RecordType(registry, null);
 recordType1.defineProperty("p", JSType.STRING, false, Node.newIdent("p"));
 recordType1.defineProperty("q", JSType.NUMBER, false, Node.newIdent("q"));

 RecordType recordType2 = new RecordType(registry, null);

 assertTrue(recordType1.isSubtype(recordType2));
 assertTrue(recordType2.isSubtype(recordType1));
 }

 @Test
 public void testIsSubtypeWithRecordTypeAndUnionType() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
 unionTypeBuilder.addAlternate(registry.getNativeObjectType(JSTypeNative.NUMBER_TYPE));
 unionTypeBuilder.addAlternate(registry.getNativeObjectType(JSTypeNative.STRING_TYPE));
 JSType unionType = unionTypeBuilder.build();

 RecordType recordType = new RecordType(registry, null);
 recordType.defineProperty("p", JSType.STRING, false, Node.newIdent("p"));

 assertTrue(recordType.isSubtype(unionType));
 }

 @Test
 public void testIsSubtypeWithUnionTypeAndRecordType() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
 unionTypeBuilder.addAlternate(registry.getNativeObjectType(JSTypeNative.NUMBER_TYPE));
 unionTypeBuilder.addAlternate(registry.getNativeObjectType(JSTypeNative.STRING_TYPE));
 JSType unionType = unionTypeBuilder.build();

 RecordType recordType = new RecordType(registry, null);
 recordType.defineProperty("p", JSType.STRING, false, Node.newIdent("p"));

 assertFalse(unionType.isSubtype(recordType));
 }

 @Test
 public void testIsSubtypeWithUnionTypeAndUnionType() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 UnionTypeBuilder unionTypeBuilder1 = new UnionTypeBuilder(registry);
 unionTypeBuilder1.addAlternate(registry.getNativeObjectType(JSTypeNative.NUMBER_TYPE));
 unionTypeBuilder1.addAlternate(registry.getNativeObjectType(JSTypeNative.STRING_TYPE));
 JSType unionType1 = unionTypeBuilder1.build();

 UnionTypeBuilder unionTypeBuilder2 = new UnionTypeBuilder(registry);
 unionTypeBuilder2.addAlternate(registry.getNativeObjectType(JSTypeNative.STRING_TYPE));
 unionTypeBuilder2.addAlternate(registry.getNativeObjectType(JSTypeNative.NUMBER_TYPE));
 JSType unionType2 = unionTypeBuilder2.build();

 assertTrue(unionType1.isSubtype(unionType2));
 assertTrue(unionType2.isSubtype(unionType1));
 }
}