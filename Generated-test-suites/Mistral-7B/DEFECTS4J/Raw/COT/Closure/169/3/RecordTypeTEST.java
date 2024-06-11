import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 RecordProperty property1 = new RecordProperty("property1", JSType.STRING);
 RecordProperty property2 = new RecordProperty("property2", JSType.NUMBER);
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("property1", property1);
 properties.put("property2", property2);
 recordType = new RecordType(registry, properties, true);
 }

 @Test
 public void testConstructor() {
 RecordType recordType = new RecordType(registry, properties, true);
 assertNotNull(recordType);
 }

 @Test
 public void testIsSynthetic() {
 RecordType recordType = new RecordType(registry, properties, false);
 assertFalse(recordType.isSynthetic());
 RecordType recordType1 = new RecordType(registry, properties, true);
 assertTrue(recordType1.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 RecordType recordType1 = new RecordType(registry, properties, true);
 RecordType recordType2 = new RecordType(registry, properties, true);
 RecordType recordType3 = new RecordType(registry, properties, true);
 RecordType recordType4 = new RecordType(registry, properties, true);
 RecordType recordType5 = new RecordType(registry, properties, true);
 RecordType recordType6 = new RecordType(registry, properties, true);

 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType1, true));
 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));
 assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, true)); // different instance
 assertFalse(recordType1.checkRecordEquivalenceHelper(recordType4, false)); // different properties
 assertFalse(recordType1.checkRecordEquivalenceHelper(recordType5, true)); // different instance
 assertFalse(recordType1.checkRecordEquivalenceHelper(recordType6, false)); // different properties
 }

 @Test
 public void testGetImplicitPrototype() {
 RecordType recordType = new RecordType(registry, properties, true);
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 assertEquals(objectType, recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 RecordType recordType = new RecordType(registry, properties, true);
 String propertyName = "property3";
 JSType propertyType = JSType.BOOLEAN;
 recordType.defineProperty(propertyName, propertyType, false, null);
 assertTrue(recordType.hasProperty(propertyName));
 assertEquals(propertyType, recordType.getPropertyType(propertyName));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWithNullProperty() {
 RecordType recordType = new RecordType(registry, properties, true);
 String propertyName = "property3";
 JSType propertyType = JSType.BOOLEAN;
 recordType.defineProperty(propertyName, propertyType, false, null);
 recordType.defineProperty(propertyName, null, false, null);
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType1 = new RecordType(registry, properties, true);
 RecordType recordType2 = new RecordType(registry, properties, true);
 RecordType recordType3 = new RecordType(registry, properties, true);
 RecordType recordType4 = new RecordType(registry, properties, true);
 RecordType recordType5 = new RecordType(registry, properties, true);

 JSType type1 = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
 JSType type2 = registry.getNativeType(JSTypeNative.BOOLEAN_TYPE);
 JSType type3 = registry.getNativeType(JSTypeNative.STRING_TYPE);
 JSType type4 = registry.getNativeType(JSTypeNative.OBJECT_TYPE);
 JSType type5 = JSType.VOID;
 JSType type6 = JSType.UNKNOWN;

 assertEquals(type1, recordType1.getGreatestSubtypeHelper(type1));
 assertEquals(type1, recordType1.getGreatestSubtypeHelper(type2));
 assertEquals(type4, recordType1.getGreatestSubtypeHelper(type3));
 assertEquals(type5, recordType1.getGreatestSubtypeHelper(type4));
 assertEquals(type5, recordType1.getGreatestSubtypeHelper(type5));
 assertEquals(type6, recordType1.getGreatestSubtypeHelper(null));
 assertEquals(type6, recordType1.getGreatestSubtypeHelper(recordType2));
 assertEquals(type4, recordType1.getGreatestSubtypeHelper(recordType3));
 assertEquals(type4, recordType1.getGreatestSubtypeHelper(recordType4));
 assertEquals(type4, recordType1.getGreatestSubtypeHelper(recordType5));

 assertNotEquals(type1, recordType1.getGreatestSubtypeHelper(type5));
 assertNotEquals(type1, recordType1.getGreatestSubtypeHelper(null));
 assertNotEquals(type1, recordType1.getGreatestSubtypeHelper(recordType5));
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType1 = new RecordType(registry, properties, true);
 RecordType recordType2 = new RecordType(registry, properties, true);
 RecordType recordType3 = new RecordType(registry, properties, true);
 RecordType recordType4 = new RecordType(registry, properties, true);
 RecordType recordType5 = new RecordType(registry, properties, true);
 RecordType recordType6 = new RecordType(registry, properties, true);
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 assertTrue(recordType1.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(objectType));
 assertTrue(recordType1.isSubtype(recordType2));
 assertTrue(objectType.isSubtype(recordType1));
 assertTrue(objectType.isSubtype(recordType2));
 assertTrue(recordType2.isSubtype(recordType1));
 assertTrue(recordType2.isSubtype(objectType));
 assertTrue(recordType1.isSubtype(recordType3));
 assertTrue(recordType3.isSubtype(recordType1));
 assertTrue(recordType3.isSubtype(objectType));

 assertFalse(recordType1.isSubtype(recordType5));
 assertFalse(recordType5.isSubtype(recordType1));
 assertFalse(recordType1.isSubtype(recordType6));
 assertFalse(recordType6.isSubtype(recordType1));
 assertFalse(recordType1.isSubtype(null));
 assertFalse(null.isSubtype(recordType1));
 }

 @Test
 public void testToMaybeRecordType() {
 RecordType recordType = new RecordType(registry, properties, true);
 assertEquals(recordType, recordType.toMaybeRecordType());
 }
}