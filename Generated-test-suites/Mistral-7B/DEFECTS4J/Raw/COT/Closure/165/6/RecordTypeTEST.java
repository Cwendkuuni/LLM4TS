import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class RecordTypeTest {

 RecordType testRecordType;
 JSTypeRegistry registry;

 @BeforeClass
 public static void setupClass() {
 // Set up the registry for the tests.
 registry = new JSTypeRegistry();
 }

 @Before
 public void setup() {
 // Set up the test record type.
 Map<String, RecordProperty> properties = new Maps.HashMap<>();
 properties.put("a", new RecordProperty("a", Integer.TYPE, null));
 properties.put("b", new RecordProperty("b", String.TYPE, null));
 testRecordType = new RecordType(registry, properties);
 }

 @Test
 public void testConstructor() {
 RecordType recordType = new RecordType(registry, properties);
 assertEquals(testRecordType, recordType);
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType recordType2 = new RecordType(registry, properties);
 assertTrue(testRecordType.isEquivalentTo(recordType2));
 }

 @Test
 public void testGetImplicitPrototype() {
 ObjectType implicitPrototype = testRecordType.getImplicitPrototype();
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
 }

 @Test
 public void testDefineProperty_Inferred() {
 JSType newPropertyType = registry.newRecordType(Maps.newHashMap());
 testRecordType.defineProperty("c", newPropertyType, true, null);
 assertTrue(testRecordType.hasProperty("c"));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefineProperty_Frozen() {
 testRecordType.isFrozen = true;
 try {
 testRecordType.defineProperty("a", Integer.TYPE, false, null);
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testGetGreatestSubtypeHelper_SameType() {
 JSType sameType = new RecordType(registry, properties);
 assertEquals(testRecordType, sameType.getGreatestSubtypeHelper(testRecordType));
 }

 @Test
 public void testGetGreatestSubtypeHelper_Subtype() {
 RecordType subtype = new RecordType(registry, Maps.<String, RecordProperty>newHashMap());
 subtype.defineProperty("a", testRecordType.getPropertyType("a"), false, null);
 JSType greatestSubtype = testRecordType.getGreatestSubtypeHelper(subtype);
 assertEquals(testRecordType, greatestSubtype);
 }

 @Test
 public void testGetGreatestSubtypeHelper_Supertype() {
 RecordType supertype = new RecordType(registry, Maps.<String, RecordProperty>newHashMap());
 supertype.defineProperty("a", Integer.TYPE, false, null);
 supertype.defineProperty("b", String.TYPE, false, null);
 supertype.defineProperty("c", testRecordType.getPropertyType("a"), false, null);
 JSType greatestSubtype = testRecordType.getGreatestSubtypeHelper(supertype);
 assertEquals(testRecordType, greatestSubtype);
 }

 @Test
 public void testGetGreatestSubtypeHelper_DifferentProperties() {
 RecordType otherRecord = new RecordType(registry, Maps.<String, RecordProperty>newHashMap());
 otherRecord.defineProperty("a", Integer.TYPE, false, null);
 otherRecord.defineProperty("c", String.TYPE, false, null);
 JSType greatestSubtype = testRecordType.getGreatestSubtypeHelper(otherRecord);
 assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), greatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 // Test that RecordType is a subtype of ObjectType.
 assertTrue(testRecordType.isSubtype(registry.getNativeObjectType(
 JSTypeNative.OBJECT_TYPE)));

 // Test that RecordType is a subtype of itself.
 assertTrue(testRecordType.isSubtype(testRecordType));

 // Test that RecordType is a subtype of a RecordType with the same properties.
 RecordType samePropertiesRecord = new RecordType(registry, properties);
 assertTrue(testRecordType.isSubtype(samePropertiesRecord));

 // Test that RecordType is a subtype of a RecordType with some additional properties.
 RecordType additionalPropertiesRecord = new RecordType(registry, Maps.<String, RecordProperty>newHashMap());
 additionalPropertiesRecord.defineProperty("c", Boolean.TYPE, false, null);
 assertTrue(testRecordType.isSubtype(additionalPropertiesRecord));

 // Test that RecordType is not a subtype of a RecordType with different properties.
 RecordType differentPropertiesRecord = new RecordType(registry, Maps.<String, RecordProperty>newHashMap());
 differentPropertiesRecord.defineProperty("c", Boolean.TYPE, false, null);
 assertFalse(testRecordType.isSubtype(differentPropertiesRecord));
}

 @Test
 public void testResolveInternal() {
 // Test that properties are resolved correctly.
 Node propertyNode = new Node(new Object());
 testRecordType.defineProperty("d", registry.newRecordType(Maps.newHashMap()), true, propertyNode);
 JSType resolvedType = testRecordType.resolveInternal(null, null);
 JSType propertyType = resolvedType.getPropertyType("d");
 assertNotNull(propertyType);
 }
}