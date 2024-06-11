import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 RecordProperty recordProperty1 = new RecordProperty("property1", int.class);
 RecordProperty recordProperty2 = new RecordProperty("property2", double.class);
 Map<String, RecordProperty> propertiesMap = Maps.newHashMap();
 propertiesMap.put("property1", recordProperty1);
 propertiesMap.put("property2", recordProperty2);
 recordType = new RecordType(registry, propertiesMap);
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType recordType1 = new RecordType(registry, propertiesMap);
 RecordType recordType2 = new RecordType(registry, propertiesMap);
 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());

 assertTrue(recordType1.isEquivalentTo(recordType2));
 assertFalse(recordType1.isEquivalentTo(recordType3));
 }

 @Test
 public void testGetImplicitPrototype() {
 ObjectType expectedPrototype = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 ObjectType actualPrototype = recordType.getImplicitPrototype();
 assertEquals(expectedPrototype, actualPrototype);
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWhenFrozen() {
 recordType.defineProperty("property", int.class, false, null);
 }

 @Test
 public void testGreatestSubtypeHelper() {
 JSType other = registry.createRecordType(registry, Maps.newHashMap());
 other.defineProperty("property", int.class, false, null);
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(other);

 assertEquals(JSTypeNative.OBJECT_TYPE, greatestSubtype.getClassName());
 }

 @Test
 public void testIsSubtype() {
 RecordType recordTypeA = new RecordType(registry, propertiesMap);
 RecordType recordTypeB = new RecordType(registry, Maps.newHashMap());
 recordTypeB.defineProperty("property1", int.class, false, null);
 recordTypeB.defineProperty("property2", double.class, false, null);

 RecordType recordTypeC = new RecordType(registry, Maps.newHashMap());
 recordTypeC.defineProperty("property1", int.class, false, null);
 recordTypeC.defineProperty("property2", double.class, false, null);
 recordTypeC.defineProperty("property3", String.class, false, null);

 assertTrue(recordTypeA.isSubtype(recordTypeA));
 assertTrue(recordTypeA.isSubtype(recordTypeB));
 assertTrue(recordTypeB.isSubtype(recordTypeC));
 assertTrue(recordTypeC.isSubtype(recordTypeB));
 assertTrue(recordTypeC.isSubtype(recordTypeA));

 RecordType recordTypeD = new RecordType(registry, Maps.newHashMap());
 recordTypeD.defineProperty("property1", int[].class, false, null);
 assertFalse(recordTypeA.isSubtype(recordTypeD));
 }

 @Test
 public void testIsSubtypeWithEmptyRecordType() {
 RecordType recordType = new RecordType(registry, Maps.newHashMap());
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 assertTrue(objectType.isSubtype(recordType));
 }

 @Test
 public void testIsSubtypeWithRecordTypeAndObjectType() {
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 RecordType recordType = new RecordType(registry, propertiesMap);

 assertFalse(recordType.isSubtype(objectType));
 }

 @Test
 public void testIsSubtypeWithUnionType() {
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 UnionType unionType =
     new UnionTypeBuilder(registry).addAlternate(objectType).build();
 RecordType recordType = new RecordType(registry, propertiesMap);

 assertFalse(recordType.isSubtype(unionType));
 }

 @Test
 public void testIsSubtypeWithRecordTypeAndRecordType() {
 RecordType recordTypeA = new RecordType(registry, propertiesMap);
 RecordType recordTypeB = new RecordType(registry, Maps.newHashMap());
 recordTypeB.defineProperty("property1", int.class, false, null);
 recordTypeB.defineProperty("property2", double.class, false, null);

 assertTrue(recordTypeA.isSubtype(recordTypeA));
 assertTrue(recordTypeA.isSubtype(recordTypeB));
 assertTrue(recordTypeB.isSubtype(recordTypeA));
 }

 @Test
 public void testResolveInternal() {
 RecordType recordType = new RecordType(registry, propertiesMap);
 JSType intType = int.class.getType(registry);
 JSType intLiteralType = registry.createNamedType("int");
 RecordProperty recordProperty = new RecordProperty("property", intType);
 recordType.defineProperty("property", recordProperty.getType(), false, null);
 JSType resolvedRecordType = recordType.resolveInternal(null, null);

 assertEquals(recordType, resolvedRecordType);
 }
}