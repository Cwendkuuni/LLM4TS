import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

 @BeforeClass
 public static void setup() {
 registry = new JSTypeRegistry();
 }

 private static JSTypeRegistry registry;

 @Test
 public void testRecordTypeConstruction() {
 RecordProperty[] recordProperties = new RecordProperty[] {
 new RecordProperty("name", registry.getStringType()),
 new RecordProperty("age", registry.getNumberType())
 };
 RecordType recordType = new RecordType(registry, Maps.newHashMap(), true);
 assertEquals("RecordType", recordType.getClassName());
 assertEquals(2, recordType.getPropertyCount());
 assertEquals("name", recordType.getPropertyNames()[0]);
 assertEquals("name", recordType.getPropertyNames()[1]);
 assertEquals(registry.getStringType(), recordType.getPropertyType("name"));
 assertEquals(registry.getNumberType(), recordType.getPropertyType("age"));
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 RecordProperty[] recordProperties1 = new RecordProperty[] {
 new RecordProperty("name", registry.getStringType()),
 new RecordProperty("age", registry.getNumberType())
 };
 RecordProperty[] recordProperties2 = new RecordProperty[] {
 new RecordProperty("age", registry.getNumberType()),
 new RecordProperty("name", registry.getStringType())
 };
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap(
 "name", recordProperties1[0]), "name");
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap(
 "age", recordProperties2[0]), "age");
 RecordType recordType3 = new RecordType(registry, Maps.newHashMap(), true);

 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType1, false));
 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));
 assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, false));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSType type1 = registry.getRecordType(Maps.newHashMap(), true);
 JSType type2 = registry.getRecordType(Maps.newHashMap(), true);
 JSType type3 = registry.getRecordType(Maps.newHashMap(
 "name", new RecordProperty("name", registry.getStringType())),
 "name");
 JSType type4 = registry.getRecordType(Maps.newHashMap(
 "age", new RecordProperty("age", registry.getNumberType())),
 "age");
 JSType type5 = registry.getRecordType(Maps.newHashMap(
 "name", new RecordProperty("name", registry.getStringType()),
 "age", new RecordProperty("age", registry.getNumberType())),
 "name");

 assertEquals(type1, type1.getGreatestSubtypeHelper(type1));
 assertEquals(type1, type1.getGreatestSubtypeHelper(type2));
 assertEquals(type3, type1.getGreatestSubtypeHelper(type3));
 assertEquals(type5, type1.getGreatestSubtypeHelper(type4));
 assertNull(type1.getGreatestSubtypeHelper(registry.getBooleanType()));
 }

 @Test
 public void testIsSubtype() {
 RecordProperty[] recordProperties1 = new RecordProperty[] {
 new RecordProperty("name", registry.getStringType()),
 new RecordProperty("age", registry.getNumberType())
 };
 RecordProperty[] recordProperties2 = new RecordProperty[] {
 new RecordProperty("age", registry.getNumberType()),
 new RecordProperty("name", registry.getStringType())
 };
 RecordType recordType1 = new RecordType(registry,
 Maps.newHashMap(
 "name", recordProperties1[0]), "name");
 RecordType recordType2 = new RecordType(registry,
 Maps.newHashMap(
 "age", recordProperties2[0]), "age");
 RecordType recordType3 = new RecordType(registry,
 Maps.newHashMap(), true);
 RecordType recordType4 = new RecordType(registry,
 Maps.newHashMap(
 "name", new RecordProperty("name", registry.getStringType()),
 "age", new RecordProperty("age", registry.getNumberType())),
 "name");
 ObjectType objectType = registry.getNativeObjectType(
 JSTypeNative.OBJECT_TYPE);

 assertTrue(recordType1.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(recordType2));
 assertFalse(recordType1.isSubtype(recordType3));
 assertFalse(recordType1.isSubtype(registry.getBooleanType()));
 assertTrue(recordType1.isSubtype(recordType4));
 }

 @Test(expected = IllegalStateException.class)
 public void testConstructorWithNullRecordProperty() {
 RecordProperty[] recordProperties = new RecordProperty[] {
 new RecordProperty("name", registry.getStringType()),
 null
 };
 RecordType recordType = new RecordType(registry,
 Maps.newHashMap(
 "name", recordProperties[0]), "name");
 }

 @Test
 public void testFreeze() {
 RecordProperty[] recordProperties = new RecordProperty[] {
 new RecordProperty("name", registry.getStringType()),
 new RecordProperty("age", registry.getNumberType())
 };
 RecordType recordType = new RecordType(registry,
 Maps.newHashMap(
 "name", recordProperties[0]), "name");
 recordType.freeze();
 assertFalse(recordType.isFrozen());
 recordType.freeze();
 assertTrue(recordType.isFrozen());
 }

 @Test
 public void testDefineProperty() {
 RecordProperty[] recordProperties = new RecordProperty[] {
 new RecordProperty("name", registry.getStringType()),
 new RecordProperty("age", registry.getNumberType())
 };
 RecordType recordType = new RecordType(registry,
 Maps.newHashMap(
 "name", recordProperties[0]), "name");
 assertFalse(recordType.defineProperty("name", registry.getStringType(),
 false, null));
 recordType.defineProperty("age", registry.getNumberType(), false, null);
 assertTrue(recordType.hasProperty("age"));
 }

 @Test
 public void testGetImplicitPrototype() {
 RecordProperty[] recordProperties = new RecordProperty[] {
 new RecordProperty("name", registry.getStringType()),
 new RecordProperty("age", registry.getNumberType())
 };
 RecordType recordType = new RecordType(registry,
 Maps.newHashMap(
 "name", recordProperties[0]), "name");
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
 recordType.getImplicitPrototype());
 }
}