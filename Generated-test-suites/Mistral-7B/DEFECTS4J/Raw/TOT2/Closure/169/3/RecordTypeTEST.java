import com.google.javascript.rhino.jstype.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

 // Test constructor with valid properties
 @Test
 public void testRecordTypeConstruction() {
 RecordType registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = new HashMap<>();
 properties.put("a", new RecordProperty("a", new IntType()));
 properties.put("b", new RecordProperty("b", new StringType()));
 RecordType recordType = new RecordType(registry, properties, true);
 assertEquals(2, recordType.getDeclaredProperties().size());
 }

 // Test constructor with null RecordProperty
 @Test(expected = IllegalStateException.class)
 public void testRecordTypeConstructionWithNullProperty() {
 RecordType registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = new HashMap<>();
 properties.put("a", new RecordProperty("a", new IntType()));
 properties.put(null, new RecordProperty(null, new IntType())); // null property
 RecordType recordType = new RecordType(registry, properties, true);
 fail("Expected IllegalStateException but got RecordType instead.");
 }

 // Test checkRecordEquivalenceHelper with equal record types
 @Test
 public void testCheckRecordEquivalenceHelperWithEqualRecordTypes() {
 RecordTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> prop1 = Maps.newHashMap();
 prop1.put("x", new RecordProperty("x", new IntType()));
 RecordType recordType1 = new RecordType(registry, prop1, true);
 Map<String, RecordProperty> prop2 = Maps.newHashMap();
 prop2.put("x", new RecordProperty("x", new IntType()));
 RecordType recordType2 = new RecordType(registry, prop2, true);
 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));
 }

 // Test checkRecordEquivalenceHelper with different record types
 @Test
 public void testCheckRecordEquivalenceHelperWithDifferentRecordTypes() {
 RecordTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> prop1 = Maps.newHashMap();
 prop1.put("x", new RecordProperty("x", new IntType()));
 RecordType recordType1 = new RecordType(registry, prop1, true);
 Map<String, RecordProperty> prop2 = Maps.newHashMap();
 prop2.put("y", new RecordProperty("y", new IntType()));
 RecordType recordType2 = new RecordType(registry, prop2, true);
 assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, false));
 }

 // Test checkRecordEquivalenceHelper with tolerateUnknowns = true
 @Test
 public void testCheckRecordEquivalenceHelperWithTolerateUnknowns() {
 RecordTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> prop1 = Maps.newHashMap();
 prop1.put("x", new RecordProperty("x", new IntType()));
 RecordType recordType1 = new RecordType(registry, prop1, true);
 Map<String, RecordProperty> prop2 = Maps.newHashMap();
 prop2.put("x", new RecordProperty("x", new AnyType())); // AnyType is a supertype of IntType
 RecordType recordType2 = new RecordType(registry, prop2, true);
 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));
 }

 // Test getImplicitPrototype
 @Test
 public void testGetImplicitPrototype() {
 RecordType recordType = new RecordType(new JSTypeRegistry(), Maps.newHashMap(), true);
 assertEquals(ObjectType.instance, recordType.getImplicitPrototype());
 }

 // Test defineProperty
 @Test
 public void testDefineProperty() {
 RecordType recordType = new RecordType(new JSTypeRegistry(), Maps.newHashMap(), true);
 recordType.defineProperty("property", new IntType(), false, null);
 assertTrue(recordType.hasProperty("property"));
 }

 // Test defineProperty with property already defined
 @Test
 public void testDefinePropertyWithPropertyAlreadyDefined() {
 RecordType recordType = new RecordType(new JSTypeRegistry(), Maps.newHashMap(), true);
 recordType.defineProperty("property", new IntType(), true, null);
 recordType.defineProperty("property", new StringType(), false, null);
 assertEquals(
 "Cannot redefine property 'property'. The property was previously defined as: 'IntType'.",
 recordType.getMessage());
 }

 // Test getGreatestSubtypeHelper
 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordTypeRegistry registry = new JSTypeRegistry();
 RecordType recordA = new RecordType(registry, Maps.newHashMap(), true) {{
 defineProperty("propA", new IntType(), true, null);
 }};
 RecordType recordB = new RecordType(registry, Maps.newHashMap(), true) {{
 defineProperty("propA", new StringType(), true, null);
 defineProperty("propB", new StringType(), true, null);
 }};
 RecordType recordC = new RecordType(registry, Maps.newHashMap(), true) {{
 defineProperty("propA", new IntType(), true, null);
 defineProperty("propB", new IntType(), true, null);
 }};
 RecordType recordD = new RecordType(registry, Maps.newHashMap(), true) {{
 defineProperty("propA", new IntType(), true, null);
 defineProperty("propB", new ObjectType(), true, null);
 }};
 RecordType recordE = new RecordType(registry, Maps.newHashMap(), true) {{
 defineProperty("propA", new StringType(), true, null);
 defineProperty("propB", new ObjectType(), true, null);
 }};
 RecordType recordF = new RecordType(registry, Maps.newHashMap(), true) {{
 defineProperty("propA", new IntType(), true, null);
 defineProperty("propC", new ObjectType(), true, null);
 }};

 RecordType greatestSubtypeAB = recordA.getGreatestSubtypeHelper(recordB);
 assertEquals(recordB, greatestSubtypeAB);

 RecordType greatestSubtypeAC = recordA.getGreatestSubtypeHelper(recordC);
 assertEquals(recordC, greatestSubtypeAC);

 RecordType greatestSubtypeAD = recordA.getGreatestSubtypeHelper(recordD);
 assertEquals(recordA, greatestSubtypeAD);

 RecordType greatestSubtypeAE = recordA.getGreatestSubtypeHelper(recordE);
 assertEquals(recordA, greatestSubtypeAE);

 RecordType greatestSubtypeBF = recordB.getGreatestSubtypeHelper(recordF);
 assertEquals(recordB, greatestSubtypeBF);
 }

 // Test isSubtype
 @Test
 public void testIsSubtype() {
 RecordTypeRegistry registry = new JSTypeRegistry();
 RecordType recordA = new RecordType(registry, Maps.newHashMap(), true) {{
 defineProperty("propA", new IntType(), true, null);
 }};
 RecordType recordB = new RecordType(registry, Maps.newHashMap(), true) {{
 defineProperty("propA", new IntType(), true, null);
 defineProperty("propB", new StringType(), true, null);
 }};
 RecordType recordC = new RecordType(registry, Maps.newHashMap(), true) {{
 defineProperty("propA", new IntType(), true, null);
 defineProperty("propC", new ObjectType(), true, null);
 }};
 RecordType recordD = new RecordType(registry, Maps.newHashMap(), true) {{
 defineProperty("propB", new StringType(), true, null);
 }};

 assertTrue(recordA.isSubtype(recordA));
 assertTrue(recordA.isSubtype(recordB));
 assertFalse(recordA.isSubtype(recordC));
 assertFalse(recordA.isSubtype(recordD));
 }

 // Test toMaybeRecordType
 @Test
 public void testToMaybeRecordType() {
 RecordType recordType = new RecordType(new JSTypeRegistry(), Maps.newHashMap(), true);
 assertEquals(recordType, recordType.toMaybeRecordType());
 }
}