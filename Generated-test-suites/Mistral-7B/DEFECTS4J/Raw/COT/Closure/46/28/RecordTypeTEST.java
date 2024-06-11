import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @BeforeClass
 public void setup() {
 registry = new JSTypeRegistry();
 RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
 RecordProperty prop2 = new RecordProperty("prop2", registry.getIntType());
 Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
 properties.put("prop1", prop1);
 properties.put("prop2", prop2);
 recordType = new RecordType(registry, properties);
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);
 assertTrue(recordType1.isEquivalentTo(recordType2));
 }

 @Test
 public void testIsEquivalentToDifferentOrder() {
 RecordType recordType1 = new RecordType(registry, properties);
 Map<String, RecordProperty> reverseProperties = Maps.newLinkedHashMap();
 reverseProperties.put("prop2", prop2);
 reverseProperties.put("prop1", prop1);
 RecordType recordType2 = new RecordType(registry, reverseProperties);
 assertTrue(recordType1.isEquivalentTo(recordType2));
 }

 @Test
 public void testIsEquivalentToDifferentTypes() {
 RecordType recordType1 = new RecordType(registry, properties);
 JSType intType = registry.getIntType();
 RecordType recordType2 = new RecordType(registry, Maps.<String, RecordProperty>newLinkedHashMap());
 recordType2.defineProperty("prop1", intType.getLeastSupertype(recordType1.getPropertyType("prop1")), false, null);
 recordType2.defineProperty("prop2", recordType1.getPropertyType("prop2"), false, null);
 assertFalse(recordType1.isEquivalentTo(recordType2));
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.<String, RecordProperty>newLinkedHashMap());
 recordType2.defineProperty("prop1", recordType1.getPropertyType("prop1"), false, null);
 recordType2.defineProperty("prop2", registry.getIntType(), false, null);
 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeDifferentOrder() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.<String, RecordProperty>newLinkedHashMap());
 recordType2.defineProperty("prop2", recordType1.getPropertyType("prop2"), false, null);
 recordType2.defineProperty("prop1", recordType1.getPropertyType("prop1"), false, null);
 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeEmptyRecord() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType emptyRecord = new RecordType(registry, Maps.<String, RecordProperty>newLinkedHashMap());
 assertTrue(emptyRecord.isSubtype(recordType1));
 }

 @Test
 public void testIsSubtypeDifferentTypes2() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.<String, RecordProperty>newLinkedHashMap());
 recordType2.defineProperty("prop1", recordType1.getPropertyType("prop1"), false, null);
 recordType2.defineProperty("prop3", registry.getStringType(), false, null);
 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeRecordWithInferredProperties() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.<String, RecordProperty>newLinkedHashMap());
 recordType2.defineProperty("prop1", recordType1.getPropertyType("prop1"), true, null);
 recordType2.defineProperty("prop2", recordType1.getPropertyType("prop2"), false, null);
 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testGetLeastSupertype() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.<String, RecordProperty>newLinkedHashMap());
 recordType2.defineProperty("prop1", recordType1.getPropertyType("prop1"), false, null);
 recordType2.defineProperty("prop3", registry.getStringType(), false, null);
 JSType leastSupertype = recordType1.getLeastSupertype(recordType2);
 assertTrue(recordType1.isSubtype(leastSupertype));
 }

 @Test
 public void testGetLeastSupertypeEmptyRecord() {
 RecordType recordType1 = new RecordType(registry, properties);
 JSType leastSupertype = recordType1.getLeastSupertype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
 assertTrue(recordType1.isSubtype(leastSupertype));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDefinePropertyFrozen() {
 recordType.defineProperty("prop1", registry.getStringType(), false, null);
 recordType.isFrozen = true;
 recordType.defineProperty("prop1", registry.getStringType(), false, null);
 }

 @Test
 public void testToString() {
 RecordType recordType = new RecordType(registry, properties);
 assertEquals("RecordType {prop1: String, prop2: Int}", recordType.toString());
 }

 @Test
 public void testFreezing() {
 RecordType recordType = new RecordType(registry, properties);
 assertTrue(!recordType.isFrozen);
 recordType.freeze();
 assertTrue(recordType.isFrozen);
 }
}