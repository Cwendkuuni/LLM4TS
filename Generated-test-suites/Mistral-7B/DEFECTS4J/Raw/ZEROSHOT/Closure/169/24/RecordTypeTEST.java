import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType1;
 private RecordType recordType2;
 private RecordType recordType3;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();

 RecordProperty recordProperty1 = new RecordProperty(
 "prop1", registry.getStringType());
 RecordProperty recordProperty2 = new RecordProperty("prop2", registry.getBooleanType());

 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("prop1", recordProperty1);
 properties1.put("prop2", recordProperty2);

 recordType1 = new RecordType(registry, properties1, true);

 RecordProperty recordProperty3 = new RecordProperty("prop2", registry.getIntType());
 RecordProperty recordProperty4 = new RecordProperty("prop3", registry.getIntType());

 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("prop1", recordProperty1);
 properties2.put("prop2", recordProperty3);
 properties2.put("prop3", recordProperty4);

 recordType2 = new RecordType(registry, properties2, true);

 RecordType recordType4 = new RecordType(registry, properties1, false);
 recordType3 = new RecordType(registry, properties2, false);
 }

 @Test
 public void testIsSynthetic() {
 assertFalse(recordType1.isSynthetic());
 assertTrue(recordType4.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType1, false));
 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType4, false));
 assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, false));
 }

 @Test
 public void testGetImplicitPrototype() {
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
 recordType1.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 assertFalse(recordType1.defineProperty("prop3", registry.getIntType(), false, null));
 assertTrue(recordType1.defineProperty("prop1", registry.getIntType(), false, null));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), greatestSubtype);

 greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType1);
 assertEquals(recordType1, greatestSubtype);

 greatestSubtype = recordType1.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 assertTrue(recordType1.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(recordType4));
 assertFalse(recordType1.isSubtype(recordType2));
}

 @Test
 public void testIsSubtypeRecordTypeToRecordType() {
 assertTrue(recordType1.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(recordType2));
 assertFalse(recordType1.isSubtype(recordType3));
 }

 @Test
 public void testIsSubtypeRecordTypeToUnionType() {
 UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
 unionTypeBuilder.addAlternate(recordType1);
 unionTypeBuilder.addAlternate(registry.getNativeType(JSTypeNative.STRING_TYPE));
 JSType unionType = unionTypeBuilder.build();

 assertFalse(recordType1.isSubtype(unionType));
 }

 @Test
 public void testIsSubtypeUnionTypeToRecordType() {
 UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
 unionTypeBuilder.addAlternate(recordType1);
 unionTypeBuilder.addAlternate(registry.getNativeType(JSTypeNative.STRING_TYPE));
 JSType unionType = unionTypeBuilder.build();

 assertTrue(unionType.isSubtype(recordType1));
 }

 @Test
 public void testIsSubtypeRecordTypeToRecordTypeWithDifferentPropertyOrder() {
 RecordType recordType5 = new RecordType(registry, properties2, true);
 assertTrue(recordType1.isSubtype(recordType5));
 }
}