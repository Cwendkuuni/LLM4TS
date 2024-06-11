import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Assert;
import org.junit.Test;

public class RecordTypeTest {

 @Test
 public void testRecordTypeCreation() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> recordProperties = new Maps.NewHashMap<>();
 recordProperties.put("name", new RecordProperty("name", String.class));
 RecordType recordType = new RecordType(registry, recordProperties);

 Assert.assertEquals(String.class, recordType.getPropertyType("name"));
 }

 @Test
 public void testIsEquivalentTo() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> recordProperties1 = new Maps.NewHashMap<>();
 recordProperties1.put("name", new RecordProperty("name", String.class));
 RecordType recordType1 = new RecordType(registry, recordProperties1);

 Map<String, RecordProperty> recordProperties2 = new Maps.NewHashMap<>();
 recordProperties2.put("name", new RecordProperty("name", String.class));
 recordProperties2.put("age", new RecordProperty("age", Number.class));
 RecordType recordType2 = new RecordType(registry, recordProperties2);

 Map<String, RecordProperty> recordProperties3 = new Maps.NewHashMap<>();
 recordProperties3.put("age", new RecordProperty("age", Number.class));
 RecordType recordType3 = new RecordType(registry, recordProperties3);

 Assert.assertTrue(recordType1.isEquivalentTo(recordType1));
 Assert.assertTrue(recordType1.isEquivalentTo(recordType2));
 Assert.assertFalse(recordType1.isEquivalentTo(recordType3));
 }

 @Test
 public void testIsSubtype() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> recordProperties1 = new Maps.NewHashMap<>();
 recordProperties1.put("name", new RecordProperty("name", String.class));
 RecordType recordType1 = new RecordType(registry, recordProperties1);

 Map<String, RecordProperty> recordProperties2 = new Maps.NewHashMap<>();
 recordProperties2.put("name", new RecordProperty("name", String.class));
 recordProperties2.put("age", new RecordProperty("age", Number.class));
 RecordType recordType2 = new RecordType(registry, recordProperties2);

 Map<String, RecordProperty> recordProperties3 = new Maps.NewHashMap<>();
 recordProperties3.put("name", new RecordProperty("name", String.class));
 RecordType recordType3 = new RecordType(registry, recordProperties3);

 Map<String, RecordProperty> recordProperties4 = new Maps.NewHashMap<>();
 recordProperties4.put("age", new RecordProperty("age", Number.class));
 RecordType recordType4 = new RecordType(registry, recordProperties4);

 Map<String, RecordProperty> recordProperties5 = new Maps.NewHashMap<>();
 recordProperties5.put("age", new RecordProperty("age", String.class));
 RecordType recordType5 = new RecordType(registry, recordProperties5);

 Assert.assertTrue(recordType1.isSubtype(recordType1));
 Assert.assertTrue(recordType1.isSubtype(recordType2));
 Assert.assertTrue(recordType1.isSubtype(recordType3));
 Assert.assertTrue(recordType2.isSubtype(recordType1));
 Assert.assertTrue(recordType2.isSubtype(recordType2));
 Assert.assertTrue(recordType2.isSubtype(recordType3));
 Assert.assertFalse(recordType1.isSubtype(recordType4));
 Assert.assertFalse(recordType1.isSubtype(recordType5));
 Assert.assertFalse(recordType2.isSubtype(recordType4));
 Assert.assertFalse(recordType2.isSubtype(recordType5));
 Assert.assertFalse(recordType3.isSubtype(recordType4));
 Assert.assertFalse(recordType3.isSubtype(recordType5));
 Assert.assertFalse(recordType4.isSubtype(recordType1));
 Assert.assertFalse(recordType4.isSubtype(recordType2));
 Assert.assertFalse(recordType4.isSubtype(recordType3));
 Assert.assertFalse(recordType5.isSubtype(recordType1));
 Assert.assertFalse(recordType5.isSubtype(recordType2));
 Assert.assertFalse(recordType5.isSubtype(recordType3));
 }

 @Test
 public void testGetLeastSupertype() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> recordProperties1 = new Maps.NewHashMap<>();
 recordProperties1.put("name", new RecordProperty("name", String.class));
 RecordType recordType1 = new RecordType(registry, recordProperties1);

 Map<String, RecordProperty> recordProperties2 = new Maps.NewHashMap<>();
 recordProperties2.put("name", new RecordProperty("name", String.class));
 recordProperties2.put("age", new RecordProperty("age", Number.class));
 RecordType recordType2 = new RecordType(registry, recordProperties2);

 Map<String, RecordProperty> recordProperties3 = new Maps.NewHashMap<>();
 recordProperties3.put("name", new RecordProperty("name", String.class));
 recordProperties3.put("age", new RecordProperty("age", Number.class));
 recordProperties3.put("gender", new RecordProperty("gender", String.class));
 RecordType recordType3 = new RecordType(registry, recordProperties3);

 Assert.assertEquals(recordType1, recordType1.getLeastSupertype(recordType1));
 Assert.assertEquals(recordType1, recordType1.getLeastSupertype(recordType2));
 Assert.assertEquals(recordType2, recordType2.getLeastSupertype(recordType1));
 Assert.assertEquals(recordType2, recordType2.getLeastSupertype(recordType2));
 Assert.assertEquals(recordType3, recordType3.getLeastSupertype(recordType1));
 Assert.assertEquals(recordType2, recordType3.getLeastSupertype(recordType2));
 Assert.assertEquals(recordType3, recordType3.getLeastSupertype(recordType3));
 }

 @Test
 public void testDefineProperty() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> recordProperties = new Maps.NewHashMap<>();
 RecordType recordType = new RecordType(registry, recordProperties);

 Assert.assertFalse(recordType.hasProperty("name"));
 recordType.defineProperty("name", String.class, false, null);
 Assert.assertTrue(recordType.hasProperty("name"));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyFrozen() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> recordProperties = new Maps.NewHashMap<>();
 RecordType recordType = new RecordType(registry, recordProperties);
 recordType.isFrozen = true;
 recordType.defineProperty("name", String.class, false, null);
 }

 @Test
 public void testGetImplicitPrototype() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> recordProperties = new Maps.NewHashMap<>();
 RecordType recordType = new RecordType(registry, recordProperties);

 Assert.assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
 recordType.getImplicitPrototype());
 }

 @Test
 public void testGetLeastSupertypeWithEmptyObjectType() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> recordProperties = new Maps.NewHashMap<>();
 RecordType recordType = new RecordType(registry, recordProperties);

 Assert.assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
 recordType.getLeastSupertype(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE)));
 }

 @Test
 public void testGetLeastSupertypeWithNullType() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> recordProperties = new Maps.NewHashMap<>();
 RecordType recordType = new RecordType(registry, recordProperties);

 Assert.assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
 recordType.getLeastSupertype(null));
 }

 @Test
 public void testGetGreatestSubtypeHelperNullType() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> recordProperties = new Maps.NewHashMap<>();
 RecordType recordType = new RecordType(registry, recordProperties);

 Assert.assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE),
 recordType.getGreatestSubtypeHelper(null));
 }
}