import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class RecordTypeTest {

 private static RecordType createRecordType(JSTypeRegistry registry, Map<String, RecordProperty> properties) {
 return new RecordType(registry, properties, true);
 }

 @Test
 public void testRecordTypeConstruction() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", new RecordProperty("a", registry.getNumberType()));
 properties.put("b", new RecordProperty("b", registry.getStringType()));

 RecordType recordType = createRecordType(registry, properties);

 assertEquals("RecordType", recordType.getClass().getSimpleName());
 assertFalse(recordType.isSynthetic());
 assertEquals(2, recordType.getPropertyCount());

 assertEquals(registry.getNumberType(), recordType.getPropertyType("a"));
 assertEquals(registry.getStringType(), recordType.getPropertyType("b"));
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("a", new RecordProperty("a", registry.getNumberType()));
 properties1.put("b", new RecordProperty("b", registry.getStringType()));
 RecordType recordType1 = createRecordType(registry, properties1);

 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("a", new RecordProperty("a", registry.getNumberType()));
 properties2.put("b", new RecordProperty("b", registry.getNumberType()));
 RecordType recordType2 = createRecordType(registry, properties2);

 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));

 properties2.put("c", new RecordProperty("c", registry.getStringType()));
 assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, false));

 properties2.remove("c");
 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("a", new RecordProperty("a", registry.getNumberType()));
 RecordType recordType1 = createRecordType(registry, properties1);

 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("a", new RecordProperty("a", registry.getStringType()));
 RecordType recordType2 = createRecordType(registry, properties2);

 JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 assertEquals(recordType1, greatestSubtype);

 Map<String, RecordProperty> properties3 = Maps.newHashMap();
 properties3.put("a", new RecordProperty("a", registry.getBooleanType()));
 RecordType recordType3 = createRecordType(registry, properties3);

 greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType3);
 assertEquals(registry.getRecordType(Maps.newHashMap(), registry), greatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("a", new RecordProperty("a", registry.getNumberType()));
 RecordType recordType1 = createRecordType(registry, properties1);

 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("a", new RecordProperty("a", registry.getStringType()));
 RecordType recordType2 = createRecordType(registry, properties2);

 Map<String, RecordProperty> properties3 = Maps.newHashMap();
 properties3.put("a", new RecordProperty("a", registry.getNumberType()));
 RecordType recordType3 = createRecordType(registry, properties3);
 RecordType recordType4 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).toMaybeRecordType();

 assertTrue(recordType1.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
 assertTrue(recordType1.isSubtype(recordType3));
 assertTrue(recordType3.isSubtype(recordType1));
 assertFalse(recordType1.isSubtype(recordType2));
 assertFalse(recordType2.isSubtype(recordType1));
 assertFalse(recordType1.isSubtype(recordType4));
 assertFalse(recordType4.isSubtype(recordType1));
 }

 @Test
 public void testDefineProperty() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType = createRecordType(registry, Maps.newHashMap());

 Node propertyNode = Node.newIdent("p");
 JSType propertyType = registry.getNumberType();
 assertTrue(recordType.defineProperty("p", propertyType, false, propertyNode));
 assertEquals(propertyType, recordType.getPropertyType("p"));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWithNullPropertyType() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType = createRecordType(registry, Maps.newHashMap());
 Node propertyNode = Node.newIdent("p");

 recordType.defineProperty("p", null, false, propertyNode);
 }

 @Test
 public void testGetImplicitPrototype() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType = createRecordType(registry, Maps.newHashMap());
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
 }
}