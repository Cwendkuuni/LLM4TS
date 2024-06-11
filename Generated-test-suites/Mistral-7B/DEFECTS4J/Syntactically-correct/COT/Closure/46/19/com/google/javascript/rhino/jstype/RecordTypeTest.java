import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.SortedMap;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.common.collect.Maps;

public class RecordTypeTest {

 @Test
 public void testRecordTypeCreation() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> recordProperties = Maps.newHashMap();
 recordProperties.put("name", new RecordProperty("name", String.class));
 recordProperties.put("age", new RecordProperty("age", Integer.class));

 RecordType recordType = new RecordType(registry, recordProperties);

 Assert.assertEquals(recordType.getClass(), RecordType.class);
 Assert.assertEquals(recordType.properties.size(), 2);
 }

 @Test
 public void testIsEquivalentTo() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
 recordProperties1.put("name", new RecordProperty("name", String.class));
 recordProperties1.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType1 = new RecordType(registry, recordProperties1);

 Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
 recordProperties2.put("name", new RecordProperty("name", String.class));
 recordProperties2.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType2 = new RecordType(registry, recordProperties2);

 Assert.assertTrue(recordType1.isEquivalentTo(recordType2));
 }

 @Test
 public void testIsEquivalentTo_DifferentOrder() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
 recordProperties1.put("name", new RecordProperty("name", String.class));
 recordProperties1.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType1 = new RecordType(registry, recordProperties1);

 Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
 recordProperties2.put("age", new RecordProperty("age", Integer.class));
 recordProperties2.put("name", new RecordProperty("name", String.class));
 RecordType recordType2 = new RecordType(registry, recordProperties2);

 Assert.assertTrue(recordType1.isEquivalentTo(recordType2));
 }

 @Test
 public void testIsEquivalentTo_DifferentTypes() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
 recordProperties1.put("name", new RecordProperty("name", String.class));
 recordProperties1.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType1 = new RecordType(registry, recordProperties1);

 Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
 recordProperties2.put("name", new RecordProperty("name", Double.class));
 recordProperties2.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType2 = new RecordType(registry, recordProperties2);

 Assert.assertFalse(recordType1.isEquivalentTo(recordType2));
 }

 @Test
 public void testIsSubtype() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
 recordProperties1.put("name", new RecordProperty("name", String.class));
 recordProperties1.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType1 = new RecordType(registry, recordProperties1);

 Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
 recordProperties2.put("name", new RecordProperty("name", String.class));
 recordProperties2.put("age", new RecordProperty("age", Integer.class));
 recordProperties2.put("id", new RecordProperty("id", Integer.class));
 RecordType recordType2 = new RecordType(registry, recordProperties2);

 Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
 recordProperties3.put("name", new RecordProperty("name", String.class));
 recordProperties3.put("age", new RecordProperty("age", Long.class));
 RecordType recordType3 = new RecordType(registry, recordProperties3);

 Map<String, RecordProperty> recordProperties4 = Maps.newHashMap();
 recordProperties4.put("name", new RecordProperty("name", String.class));
 recordProperties4.put("id", new RecordProperty("id", Integer.class));
 recordProperties4.put("age", new RecordProperty("age", Long.class));
 RecordType recordType4 = new RecordType(registry, recordProperties4);

 Assert.assertTrue(recordType1.isSubtype(recordType2));
 Assert.assertTrue(recordType2.isSubtype(recordType1));
 Assert.assertTrue(recordType1.isSubtype(recordType3));
 Assert.assertTrue(recordType3.isSubtype(recordType1));
 Assert.assertTrue(recordType2.isSubtype(recordType4));
 Assert.assertTrue(recordType4.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtype_NoRecordType() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> recordProperties = Maps.newHashMap();
 recordProperties.put("name", new RecordProperty("name", String.class));
 recordProperties.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType = new RecordType(registry, recordProperties);

 JSType otherType = null;
 Assert.assertFalse(recordType.isSubtype(otherType));
 }

 @Test
 public void testIsSubtype_DifferentTypes_NoRecord() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> recordProperties = Maps.newHashMap();
 recordProperties.put("name", new RecordProperty("name", String.class));
 recordProperties.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType = new RecordType(registry, recordProperties);

 JSType otherType = registry.getNativeType(JSTypeNative.STRING_TYPE);
 Assert.assertFalse(recordType.isSubtype(otherType));
 }

 @Test
 public void testIsSubtype_EmptyRecordType() {
 JSTypeRegistry registry = new JSTypeRegistry();

 RecordType emptyRecordType = new RecordType(registry, Maps.newHashMap());

 Map<String, RecordProperty> recordProperties = Maps.newHashMap();
 recordProperties.put("name", new RecordProperty("name", String.class));
 recordProperties.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType = new RecordType(registry, recordProperties);

 Assert.assertTrue(emptyRecordType.isSubtype(recordType));
 }

 @Test
 public void testDefineProperty() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> recordProperties = Maps.newHashMap();
 recordProperties.put("name", new RecordProperty("name", String.class));
 recordProperties.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType = new RecordType(registry, recordProperties);

 Assert.assertFalse(recordType.hasProperty("newProperty"));

 recordType.defineProperty("newProperty", JSType.UNDEFINED, false, null);
 Assert.assertTrue(recordType.hasProperty("newProperty"));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefineProperty_Frozen() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> recordProperties = Maps.newHashMap();
 recordProperties.put("name", new RecordProperty("name", String.class));
 recordProperties.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType = new RecordType(registry, recordProperties);
 recordType.isFrozen = true;

 recordType.defineProperty("newProperty", JSType.UNDEFINED, false, null);
 }

 @Test
 public void testGetLeastSupertype() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
 recordPropertiesA.put("name", new RecordProperty("name", String.class));
 recordPropertiesA.put("age", new RecordProperty("age", Integer.class));
 RecordType recordTypeA = new RecordType(registry, recordPropertiesA);

 Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
 recordPropertiesB.put("name", new RecordProperty("name", String.class));
 recordPropertiesB.put("age", new RecordProperty("age", Integer.class));
 recordPropertiesB.put("city", new RecordProperty("city", String.class));
 RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

 JSType leastSupertype = recordTypeA.getLeastSupertype(recordTypeB);
 Assert.assertEquals(leastType.getClass(), RecordType.class);
 Assert.assertEquals(leastSupertype.properties.size(), 2);
 Assert.assertTrue(leastSupertype.hasProperty("name"));
 Assert.assertTrue(leastSupertype.hasProperty("age"));

 Map<String, RecordProperty> recordPropertiesC = Maps.newHashMap();
 recordPropertiesC.put("age", new RecordProperty("age", Integer.class));
 RecordType recordTypeC = new RecordType(registry, recordPropertiesC);

 JSType greatestSubtype = recordTypeB.getGreatestSubtypeHelper(recordTypeC);
 Assert.assertEquals(greatestSubtype.getClass(), RecordType.class);
 Assert.assertEquals(greatestSubtype.properties.size(), 2);
 Assert.assertTrue(greatestSubtype.hasProperty("name"));
 Assert.assertTrue(greatestSubtype.hasProperty("age"));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
 recordPropertiesA.put("name", new RecordProperty("name", String.class));
 recordPropertiesA.put("age", new RecordProperty("age", Integer.class));
 RecordType recordTypeA = new RecordType(registry, recordPropertiesA);

 Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
 recordPropertiesB.put("name", new RecordProperty("name", String.class));
 recordPropertiesB.put("age", new RecordProperty("age", Integer.class));
 recordPropertiesB.put("city", new RecordProperty("city", String.class));
 RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

 Map<String, RecordProperty> recordPropertiesC = Maps.newHashMap();
 recordPropertiesC.put("name", new RecordProperty("name", String.class));
 recordPropertiesC.put("city", new RecordProperty("city", String.class));
 RecordType recordTypeC = new RecordType(registry, recordPropertiesC);

 Map<String, RecordProperty> recordPropertiesD = Maps.newHashMap();
 recordPropertiesD.put("city", new RecordProperty("city", String.class));
 RecordType recordTypeD = new RecordType(registry, recordPropertiesD);

 JSType greatestSubtype = recordTypeB.getGreatestSubtypeHelper(recordTypeC);
 Assert.assertEquals(greatestSubtype.getClass(), RecordType.class);
 Assert.assertEquals(greatestSubtype.properties.size(), 1);
 Assert.assertTrue(greatestSubtype.hasProperty("city"));

 JSType greatestSubtype2 = recordTypeB.getGreatestSubtypeHelper(recordTypeD);
 Assert.assertEquals(greatestSubtype2.getClass(), RecordType.class);
 Assert.assertEquals(greatestSubtype2.properties.size(), 1);
 Assert.assertTrue(greatestSubtype2.hasProperty("name"));
 }

 @Test
 public void testResolveInternal() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> recordProperties = Maps.newHashMap();
 recordProperties.put("name", new RecordProperty("name", String.class));
 recordProperties.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType = new RecordType(registry, recordProperties);

 ErrorReporter errorReporter = new ErrorReporter() {};
 StaticScope<JSType> scope = new StaticScope<JSType>() {};

 JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
 Assert.assertEquals(resolvedType.getClass(), RecordType.class);
 Assert.assertEquals(resolvedType.properties.size(), 2);
 }
}