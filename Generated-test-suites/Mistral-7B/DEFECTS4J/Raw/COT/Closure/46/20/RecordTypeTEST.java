import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class RecordTypeTest {

 private static Map<String, RecordProperty> createRecordPropertiesMap() {
 return Maps.newHashMap();
 }

 @Test
 public void testRecordTypeConstruction() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> recordProperties = createRecordPropertiesMap();
 RecordType recordType = new RecordType(registry, recordProperties);
 assertNotNull(recordType);
 }

 @Test
 public void testIsEquivalentTo() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> recordProperties = createRecordPropertiesMap();
 recordProperties.put("a", new RecordProperty(JSType.INT_TYPE, null));
 RecordType recordTypeA = new RecordType(registry, recordProperties);
 Map<String, RecordProperty> recordPropertiesB = createRecordPropertiesMap();
 recordPropertiesB.put("a", new RecordProperty(JSType.INT_TYPE, null));
 recordPropertiesB.put("b", new RecordProperty(JSType.INT_TYPE, null));
 RecordType recordTypeB = new RecordType(registry, recordPropertiesB);
 assertTrue(recordTypeA.isEquivalentTo(recordTypeB));
 }

 @Test
 public void testIsEquivalentToDifferentProperties() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> recordPropertiesA = createRecordPropertiesMap();
 recordPropertiesA.put("a", new RecordProperty(JSType.INT_TYPE, null));
 RecordType recordTypeA = new RecordType(registry, recordPropertiesA);
 Map<String, RecordProperty> recordPropertiesB = createRecordPropertiesMap();
 recordPropertiesB.put("b", new RecordProperty(JSType.INT_TYPE, null));
 RecordType recordTypeB = new RecordType(registry, recordPropertiesB);
 assertFalse(recordTypeA.isEquivalentTo(recordTypeB));
 }

 @Test
 public void testIsEquivalentToSelf() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> recordProperties = createRecordPropertiesMap();
 RecordType recordType = new RecordType(registry, recordProperties);
 assertTrue(recordType.isEquivalentTo(recordType));
 }

 @Test(expected = IllegalStateException.class)
 public void testNullRecordProperties() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> recordProperties = null;
 RecordType recordType = new RecordType(registry, recordProperties);
 fail("IllegalStateException should be thrown");
 }

 @Test
 public void testGetImplicitPrototype() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> recordProperties = createRecordPropertiesMap();
 RecordType recordType = new RecordType(registry, recordProperties);
 ObjectType implicitPrototype = recordType.getImplicitPrototype();
 assertEquals(ObjectType.OBJECT_TYPE, implicitPrototype);
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyFrozen() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> recordProperties = createRecordPropertiesMap();
 RecordType recordType = new RecordType(registry, recordProperties);
 recordType.defineProperty("a", JSType.INT_TYPE, false, null);
 recordType.isFrozen = true;
 try {
 recordType.defineProperty("a", JSType.INT_TYPE, false, null);
 fail();
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testLeastSupertype() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> recordPropertiesA = createRecordPropertiesMap();
 recordPropertiesA.put("a", new RecordProperty(JSType.INT_TYPE, null));
 RecordType recordTypeA = new RecordType(registry, recordPropertiesA);
 Map<String, RecordProperty> recordPropertiesB = createRecordPropertiesMap();
 recordPropertiesB.put("a", new RecordProperty(JSType.INT_TYPE, null));
 recordPropertiesB.put("b", new RecordProperty(JSType.INT_TYPE, null));
 RecordType recordTypeB = new RecordType(registry, recordPropertiesB);
 RecordType leastSupertype = recordTypeA.getLeastSupertype(recordTypeB);
 assertEquals(recordTypeB, leastSupertype);
 }

 @Test
 public void testLeastSupertypeDifferentProperties() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> recordPropertiesA = createRecordPropertiesMap();
 recordPropertiesA.put("a", new RecordProperty(JSType.INT_TYPE, null));
 RecordType recordTypeA = new RecordType(registry, recordPropertiesA);
 Map<String, RecordProperty> recordPropertiesB = createRecordPropertiesMap();
 recordPropertiesB.put("b", new RecordProperty(JSType.INT_TYPE, null));
 RecordType recordTypeB = new RecordType(registry, recordPropertiesB);
 RecordType leastSupertype = recordTypeA.getLeastSupertype(recordTypeB);
 assertEquals(ObjectType.OBJECT_TYPE, leastSupertype);
 }

 @Test
 public void testGreatestSubtype() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> recordPropertiesA = createRecordPropertiesMap();
 recordPropertiesA.put("a", new RecordProperty(JSType.INT_TYPE, null));
 RecordType recordTypeA = new RecordType(registry, recordPropertiesA);
 Map<String, RecordProperty> recordPropertiesB = createRecordPropertiesMap();
 recordPropertiesB.put("a", new RecordProperty(JSType.INT_TYPE, null));
 recordPropertiesB.put("b", new RecordProperty(JSType.INT_TYPE, null));
 RecordType recordTypeB = new RecordType(registry, recordPropertiesB);
 RecordType greatestSubtype = recordTypeB.getGreatestSubtypeHelper(recordTypeA);
 assertEquals(recordTypeA, greatestSubtype);
 }

 @Test
 public void testGreatestSubtypeNoType() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> recordPropertiesA = createRecordPropertiesMap();
 recordPropertiesA.put("a", new RecordProperty(JSType.INT_TYPE, null));
 RecordType recordTypeA = new RecordType(registry, recordPropertiesA);
 Map<String, RecordProperty> recordPropertiesB = createRecordPropertiesMap();
 RecordType recordTypeB = new RecordType(registry, recordPropertiesB);
 RecordType greatestSubtype = recordTypeB.getGreatestSubtypeHelper(recordTypeA);
 assertEquals(JSTypeNative.NO_OBJECT_TYPE, greatestSubtype);
 }

 @Test
 public void testDefinePropertyWithFrozenRecord() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> recordProperties = createRecordPropertiesMap();
 RecordType recordType = new RecordType(registry, recordProperties);
 recordType.isFrozen = true;
 try {
 recordType.defineProperty("a", JSType.INT_TYPE, false, null);
 fail();
 } catch (IllegalStateException e) {
 // Expected
 }
 }
}