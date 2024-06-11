import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.node.Node;
import com.google.javascript.rhino.type.UnionType;
import com.google.javascript.rhino.type.ObjectType;
import com.google.javascript.rhino.type.RecordType;
import com.google.javascript.rhino.type.RecordTypeBuilder;
import com.google.javascript.rhino.type.PrototypeObjectType;
import org.junit.Test;

public class RecordTypeTest {

 @Test
 public void testRecordTypeCreation() {
 RecordType recordType = new RecordType(null, Maps.newHashMap());
 assertTrue(recordType instanceof RecordType);
 assertEquals(1, recordType.getPropertyCount());
 }

 @Test
 public void testIsEquivalentToSameRecordType() {
 RecordType recordType1 = new RecordType(null, Maps.newHashMap());
 RecordType recordType2 = new RecordType(null, Maps.newHashMap());
 assertEquals(recordType1, recordType2);
 assertTrue(recordType1.isEquivalentTo(recordType2));
 }

 @Test
 public void testIsEquivalentToDifferentRecordTypeWithSameProperties() {
 RecordType recordType1 = new RecordType(null, Maps.newHashMap());
 recordType1.defineProperty("property1", JSType.INT, false, null);
 RecordType recordType2 = new RecordType(null, Maps.newHashMap());
 recordType2.defineProperty("property1", JSType.INT, false, null);
 assertTrue(recordType1.isEquivalentTo(recordType2));
 }

 @Test(expected = IllegalStateException.class)
 public void testNullRecordProperty() {
 RecordType recordType = new RecordType(null, Maps.newHashMap());
 recordType.defineProperty("property1", null, false, null);
 }

 @Test
 public void testLeastSupertype() {
 RecordType recordType1 = new RecordType(null, Maps.newHashMap());
 recordType1.defineProperty("property1", JSType.INT, false, null);
 recordType1.defineProperty("property2", JSType.STRING, false, null);

 RecordType recordType2 = new RecordType(null, Maps.newHashMap());
 recordType2.defineProperty("property1", JSType.INT, false, null);
 recordType2.defineProperty("property3", JSType.STRING, false, null);

 JSType leastSupertype = recordType1.getLeastSupertype(recordType2);
 assertEquals(JSType.OBJECT, leastSupertype.getType());
 }

 @Test
 public void testLeastSupertypeSameRecordType() {
 RecordType recordType = new RecordType(null, Maps.newHashMap());
 recordType.defineProperty("property1", JSType.INT, false, null);
 recordType.defineProperty("property2", JSType.STRING, false, null);

 JSType leastSupertype = recordType.getLeastSupertype(recordType);
 assertEquals(recordType.getClass(), leastSupertype.getClass());
 }

 @Test
 public void testLeastSupertypeSubtype() {
 RecordType recordType1 = new RecordType(null, Maps.newHashMap());
 recordType1.defineProperty("property1", JSType.INT, false, null);
 recordType1.defineProperty("property2", JSType.STRING, false, null);

 RecordType recordType2 = new RecordType(null, Maps.newHashMap());
 recordType2.defineProperty("property1", JSType.INT, false, null);
 recordType2.defineProperty("property2", JSType.NUMBER, false, null);
 recordType2.defineProperty("property3", JSType.STRING, false, null);

 JSType leastSupertype = recordType1.getLeastSupertype(recordType2);
 assertEquals(recordType1.getClass(), leastSupertype.getClass());
 }

 @Test
 public void testLeastSupertypeNoCommonProperty() {
 RecordType recordType1 = new RecordType(null, Maps.newHashMap());
 recordType1.defineProperty("property1", JSType.INT, false, null);
 recordType1.defineProperty("property2", JSType.STRING, false, null);

 RecordType recordType2 = new RecordType(null, Maps.newHashMap());
 recordType2.defineProperty("property3", JSType.INT, false, null);
 recordType2.defineProperty("property4", JSType.STRING, false, null);

 JSType leastSupertype = recordType1.getLeastSupertype(recordType2);
 assertEquals(JSType.OBJECT, leastSupertype.getType());
 }

 @Test
 public void testGetImplicitPrototype() {
 RecordType recordType = new RecordType(null, Maps.newHashMap());
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getImplicitPrototype());
 }

 @Test
 public void testTryDefinePropertyAfterFreeze() {
 RecordType recordType = new RecordType(null, Maps.newHashMap());
 recordType.defineProperty("property1", JSType.INT, false, null);
 recordType.isFrozen = true;
 assertFalse(recordType.defineProperty("property2", JSType.STRING, false, null));
 }

 @Test
 public void testIsSubtypeRecordType() {
 RecordType recordType1 = new RecordType(null, Maps.newHashMap());
 recordType1.defineProperty("property1", JSType.INT, false, null);
 recordType1.defineProperty("property2", JSType.STRING, false, null);

 RecordType recordType2 = new RecordType(null, Maps.newHashMap());
 recordType2.defineProperty("property1", JSType.INT, false, null);
 recordType2.defineProperty("property3", JSType.STRING, false, null);

 RecordType recordType3 = new RecordType(null, Maps.newHashMap());
 recordType3.defineProperty("property1", JSType.INT, false, null);
 recordType3.defineProperty("property2", JSType.NUMBER, false, null);

 assertTrue(recordType1.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(recordType2));
 assertFalse(recordType1.isSubtype(recordType3));
 }

 @Test
 public void testIsSubtypeObjectType() {
 RecordType recordType = new RecordType(null, Maps.newHashMap());
 assertTrue(recordType.getImplicitPrototype().isSubtype(recordType));
 }

 @Test
 public void testGetGreatestSubtype() {
 RecordType recordType1 = new RecordType(null, Maps.newHashMap());
 recordType1.defineProperty("property1", JSType.INT, false, null);
 recordType1.defineProperty("property2", JSType.STRING, false, null);

 RecordType recordType2 = new RecordType(null, Maps.newHashMap());
 recordType2.defineProperty("property1", JSType.INT, false, null);
 recordType2.defineProperty("property3", JSType.STRING, false, null);
 recordType2.defineProperty("property4", JSType.BOOLEAN, false, null);

 JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);
 assertEquals(recordType2.getClass(), greatestSubtype.getClass());
 }

 @Test
 public void testGetGreatestSubtypeSameRecordType() {
 RecordType recordType = new RecordType(null, Maps.newHashMap());
 recordType.defineProperty("property1", JSType.INT, false, null);
 recordType.defineProperty("property2", JSType.STRING, false, null);

 JSType greatestSubtype = recordType.getGreatestSubtype(recordType);
 assertEquals(recordType.getClass(), greatestSubtype.getClass());
 }

 @Test
 public void testGetGreatestSubtypeNoCommonProperty() {
 RecordType recordType1 = new RecordType(null, Maps.newHashMap());
 recordType1.defineProperty("property1", JSType.INT, false, null);
 recordType1.defineProperty("property2", JSType.STRING, false, null);

 RecordType recordType2 = new RecordType(null, Maps.newHashMap());
 recordType2.defineProperty("property3", JSType.INT, false, null);
 recordType2.defineProperty("property4", JSType.STRING, false, null);

 JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);
 assertEquals(JSTypeNative.OBJECT_TYPE, greatestSubtype.getType());
 }

 @Test
 public void testGetGreatestSubtypeDifferentRecordTypeWithCommonProperty() {
 RecordType recordType1 = new RecordType(null, Maps.newHashMap());
 recordType1.defineProperty("property1", JSType.INT, false, null);
 recordType1.defineProperty("property2", JSType.STRING, false, null);

 RecordType recordType2 = new RecordType(null, Maps.newHashMap());
 recordType2.defineProperty("property1", JSType.INT, false, null);
 recordType2.defineProperty("property3", JSType.NUMBER, false, null);
 recordType2.defineProperty("property2", JSType.STRING, false, null);

 JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);
 assertEquals(recordType2.getClass(), greatestSubtype.getClass());
 }

 @Test
 public void testGetGreatestSubtypeWithSamePropertyDifferentTypes() {
 RecordType recordType1 = new RecordType(null, Maps.newHashMap());
 recordType1.defineProperty("property1", JSType.INT, false, null);
 recordType1.defineProperty("property2", JSType.STRING, false, null);

 RecordType recordType2 = new RecordType(null, Maps.newHashMap());
 recordType2.defineProperty("property1", JSType.INT, false, null);
 recordType2.defineProperty("property2", JSType.NUMBER, false, null);

 JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);
 assertEquals(UnionType.class, greatestSubtype.getClass());
 }

 @Test
 public void testGetGreatestSubtypeWithPropertyOfDifferentTypesInBoth() {
 RecordType recordType1 = new RecordType(null, Maps.newHashMap());
 recordType1.defineProperty("property1", JSType.INT, false, null);
 recordType1.defineProperty("property2", JSType.STRING, false, null);

 RecordType recordType2 = new RecordType(null, Maps.newHashMap());
 recordType2.defineProperty("property1", JSType.INT, false, null);
 recordType2.defineProperty("property2", JSType.NUMBER, false, null);
 recordType2.defineProperty("property3", JSType.STRING, false, null);

 JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);
 assertEquals(recordType2.getClass(), greatestSubtype.getClass());
 }

 @Test
 public void testGetGreatestSubtypeWithPropertyOfDifferentTypesInBothWithUnknownType() {
 RecordType recordType1 = new RecordType(null, Maps.newHashMap());
 recordType1.defineProperty("property1", JSType.INT, false, null);
 recordType1.defineProperty("property2", JSType.STRING, false, null);
 recordType1.defineProperty("property3", JSType.UNKNOWN, false, null);

 RecordType recordType2 = new RecordType(null, Maps.newHashMap());
 recordType2.defineProperty("property1", JSType.INT, false, null);
 recordType2.defineProperty("property2", JSType.NUMBER, false, null);
 recordType2.defineProperty("property3", JSType.STRING, false, null);
 recordType2.defineProperty("property4", JSType.UNKNOWN, false, null);

 JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);
 assertEquals(recordType2.getClass(), greatestSubtype.getClass());
 }

 @Test
 public void testGetGreatestSubtypeWithPropertyOfDifferentTypesInBothWithEmptyType() {
 RecordType recordType1 = new RecordType(null, Maps.newHashMap());
 recordType1.defineProperty("property1", JSType.INT, false, null);
 recordType1.defineProperty("property2", JSType.STRING, false, null);
 recordType1.defineProperty("property3", JSType.UNKNOWN, false, null);

 RecordType recordType2 = new RecordType(null, Maps.newHashMap());
 recordType2.defineProperty("property1", JSType.INT, false, null);
 recordType2.defineProperty("property2", JSType.NUMBER, false, null);
 recordType2.defineProperty("property3", JSType.EMPTY, false, null);

 JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);
 assertEquals(JSTypeNative.OBJECT_TYPE, greatestSubtype.getType());
 }
}