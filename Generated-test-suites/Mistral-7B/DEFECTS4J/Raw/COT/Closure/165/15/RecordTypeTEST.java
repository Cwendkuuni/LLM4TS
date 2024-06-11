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

 private JSTypeRegistry registry;
 private RecordType recordType;

 @BeforeClass
 public static void setUpBeforeClass() throws Exception {
 registry = new JSTypeRegistry();
 }

 @Before
 public void setUp() throws Exception {
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", new RecordProperty("a", registry.getStringType()));
 properties.put("b", new RecordProperty("b", registry.getNumberType()));
 recordType = new RecordType(registry, properties);
 }

 @Test
 public void testRecordTypeCreation() {
 assertNotNull(recordType);
 assertEquals("RecordType", recordType.getName());
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);
 assertTrue(recordType1.isEquivalentTo(recordType2));
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 assertFalse(recordType1.isSubtype(recordType2));

 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
 recordType3.defineProperty("a", registry.getStringType(), false, null);
 assertTrue(recordType1.isSubtype(recordType3));

 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 assertTrue(objectType.isSubtype(recordType1));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("a", registry.getStringType(), false, null);
 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
 recordType3.defineProperty("b", registry.getNumberType(), false, null);

 JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 assertEquals(objectType, greatestSubtype);

 greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType3);
 RecordType greatestRecordType = new RecordType(registry, Maps.newHashMap());
 greatestRecordType.defineProperty("a", registry.getStringType(), false, null);
 greatestRecordType.defineProperty("b", registry.getNumberType(), false, null);
 assertEquals(greatestRecordType, greatestSubtype);
 }

 @Test
 public void testDefineProperty() {
 RecordType recordType = new RecordType(registry, properties);
 assertFalse(recordType.defineProperty("c", registry.getStringType(), false, null));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWithNullRecordProperty() {
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put(null, null);
 RecordType recordType = new RecordType(registry, properties);
 }

 @Test
 public void testPropertyTypesAreEqual() {
 RecordType recordType = new RecordType(registry, properties);
 JSType aType = recordType.getPropertyType("a");
 JSType aType2 = recordType.getPropertyType("a");
 assertEquals(aType, aType2);
 }

 @Test
 public void testPropertyTypesAreSubtypes() {
 RecordType recordType = new RecordType(registry, properties);
 JSType aType = recordType.getPropertyType("a");
 JSType stringType = registry.getStringType();
 assertTrue(aType.isSubtype(stringType));
 }

 @Test
 public void testPropertyTypesAreUnknown() {
 RecordType recordType = new RecordType(registry, properties);
 JSType aType = recordType.getPropertyType("a");
 assertTrue(aType.isUnknownType());
 }

 @Test
 public void testPropertyNodesAreEqual() {
 RecordType recordType = new RecordType(registry, properties);
 Node aNode = recordType.getPropertyNode("a");
 Node aNode2 = recordType.getPropertyNode("a");
 assertEquals(aNode, aNode2);
 }

 @Test
 public void testIsFrozen() {
 RecordType recordType = new RecordType(registry, properties);
 assertFalse(recordType.isFrozen());
 }

 @Test
 public void testIsFrozenAfterConstruction() {
 RecordType recordType = new RecordType(registry, properties);
 recordType.freeze();
 assertTrue(recordType.isFrozen());
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyAfterFreeze() {
 RecordType recordType = new RecordType(registry, properties);
 recordType.freeze();
 assertFalse(recordType.defineProperty("c", registry.getStringType(), false, null));
 }

 @Test
 public void testGetImplicitPrototype() {
 RecordType recordType = new RecordType(registry, properties);
 ObjectType prototype = recordType.getImplicitPrototype();
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), prototype);
 }

 @Test
 public void testToString() {
 RecordType recordType = new RecordType(registry, properties);
 String toString = recordType.toString();
 assertTrue(toString.contains("RecordType"));
 }

 @Test
 public void testHasProperty() {
 RecordType recordType = new RecordType(registry, properties);
 assertTrue(recordType.hasProperty("a"));
 }

 @Test
 public void testHasPropertyWithNullPropertyName() {
 RecordType recordType = new RecordType(registry, properties);
 assertFalse(recordType.hasProperty(null));
 }

 @Test
 public void testHasPropertyWithNonExistentProperty() {
 RecordType recordType = new RecordType(registry, properties);
 assertFalse(recordType.hasProperty("c"));
 }

 @Test
 public void testGetPropertyTypeWithNullPropertyName() {
 RecordType recordType = new RecordType(registry, properties);
 assertNull(recordType.getPropertyType(null));
 }

 @Test
 public void testGetPropertyTypeWithNonExistentProperty() {
 RecordType recordType = new RecordType(registry, properties);
 assertNull(recordType.getPropertyType("c"));
 }

 @Test
 public void testGetPropertyNodeWithNullPropertyName() {
 RecordType recordType = new RecordType(registry, properties);
 assertNull(recordType.getPropertyNode(null));
 }

 @Test
 public void testGetPropertyNodeWithNonExistentProperty() {
 RecordType recordType = new RecordType(registry, properties);
 assertNull(recordType.getPropertyNode("c"));
 }

 @Test
 public void testIsEquivalentToWithNullType() {
 RecordType recordType = new RecordType(registry, properties);
 assertFalse(recordType.isEquivalentTo(null));
 }

 @Test
 public void testIsEquivalentToWithDifferentRecordTypes() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 assertFalse(recordType1.isEquivalentTo(recordType2));
 }

 @Test
 public void testIsSubtypeWithNullType() {
 RecordType recordType = new RecordType(registry, properties);
 assertFalse(recordType.isSubtype(null));
 }

 @Test
 public void testIsSubtypeWithDifferentRecordTypes() {
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 recordType1.defineProperty("a", registry.getStringType(), false, null);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("b", registry.getStringType(), false, null);
 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithSameRecordType() {
 RecordType recordType = new RecordType(registry, properties);
 assertTrue(recordType.isSubtype(recordType));
 }

 @Test
 public void testIsSubtypeWithObjectType() {
 RecordType recordType = new RecordType(registry, properties);
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 assertTrue(objectType.isSubtype(recordType));
 }

 @Test
 public void testIsSubtypeWithUnionType() throws Exception {
 RecordType recordType = new RecordType(registry, properties);
 UnionTypeBuilder unionBuilder = new UnionTypeBuilder(registry);
 ObjectType objectType1 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 ObjectType objectType2 = registry.getNativeObjectType(JSTypeNative.ARRAY_TYPE);
 ObjectType objectType3 = registry.getNativeObjectType(JSTypeNative.STRING_TYPE);
 unionBuilder.addAlternate(objectType1);
 unionBuilder.addAlternate(objectType2);
 unionBuilder.addAlternate(objectType3);
 JSType unionType = unionBuilder.build();
 assertTrue(recordType.isSubtype(unionType));
 }

 @Test(expected = IllegalStateException.class)
 public void testIsSubtypeWithNonRecordType() {
 RecordType recordType = new RecordType(registry, properties);
 JSType numberType = registry.getNumberType();
 assertFalse(recordType.isSubtype(numberType));
 }

 @Test
 public void testIsSubtypeWithRecordTypeWithLessProperties() throws Exception {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("a", registry.getStringType(), false, null);
 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithRecordTypeWithMoreProperties() throws Exception {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("a", registry.getStringType(), false, null);
 recordType2.defineProperty("b", registry.getStringType(), false, null);
 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithRecordTypeWithIncompatiblePropertyTypes() throws Exception {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType1.defineProperty("a", registry.getStringType(), false, null);
 recordType2.defineProperty("a", registry.getNumberType(), false, null);
 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithRecordTypeWithCompatiblePropertyTypes() throws Exception {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType1.defineProperty("a", registry.getStringType(), false, null);
 recordType2.defineProperty("a", registry.getStringType(), false, null);
 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithRecordTypeWithUnknownPropertyTypes() throws Exception {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType1.defineProperty("a", registry.getUnknownType(), false, null);
 recordType2.defineProperty("a", registry.getUnknownType(), false, null);
 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithRecordTypeWithFrozenProperties() throws Exception {
 RecordType recordType1 = new RecordType(registry, properties);
 recordType1.freeze();
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("a", registry.getStringType(), false, null);
 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithRecordTypeWithUnfrozenProperties() throws Exception {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("a", registry.getStringType(), false, null);
 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithRecordTypeWithSameFrozenProperties() throws Exception {
 RecordType recordType1 = new RecordType(registry, properties);
 recordType1.freeze();
 RecordType recordType2 = new RecordType(registry, properties);
 recordType2.freeze();
 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithRecordTypeWithDifferentFrozenProperties() throws Exception {
 RecordType recordType1 = new RecordType(registry, properties);
 recordType1.freeze();
 RecordType recordType2 = new RecordType(registry, properties);
 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testFreeze() {
 RecordType recordType = new RecordType(registry, properties);
 recordType.freeze();
 assertTrue(recordType.isFrozen());
 }

 @Test(expected = IllegalStateException.class)
 public void testFreezeTwice() {
 RecordType recordType = new RecordType(registry, properties);
 recordType.freeze();
 recordType.freeze();
 }

 @Test
 public void testResolveInternal() throws Exception {
 RecordType recordType = new RecordType(registry, properties);
 ErrorReporter errorReporter = new ErrorReporter() {
 @Override
 public void reportError(int line, String message) {
 fail("Error reported: " + message);
 }

 @Override
 public void reportError(int line, String file, String message) {
 fail("Error reported: " + message);
 }

 @Override
 public void reportWarning(int line, String message) {
 fail("Warning reported: " + message);
 }

 @Override
 public void reportWarning(int line, String file, String message) {
 fail("Warning reported: " + message);
 }
 };
 StaticScope<JSType> scope = new StaticScope<>(registry, errorReporter);
 JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
 assertNotNull(resolvedType);
 }

 @Test
 public void testToStringWithFrozenProperties() throws Exception {
 RecordType recordType = new RecordType(registry, properties);
 recordType.freeze();
 String toString = recordType.toString();
 assertTrue(toString.contains("RecordType"));
 assertTrue(toString.contains("frozen"));
 }

 @Test
 public void testToStringWithUnfrozenProperties() throws Exception {
 RecordType recordType = new RecordType(registry, properties);
 String toString = recordType.toString();
 assertTrue(toString.contains("RecordType"));
 assertFalse(toString.contains("frozen"));
 }
}