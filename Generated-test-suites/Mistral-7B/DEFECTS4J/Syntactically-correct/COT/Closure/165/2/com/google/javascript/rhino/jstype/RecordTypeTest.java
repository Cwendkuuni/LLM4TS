import static org.junit.Assert.*;
import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 }

 @Test
 public void testRecordTypeCreation() {
 RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
 RecordProperty prop2 = new RecordProperty("prop2", registry.getNumberType());
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("prop1", prop1);
 properties.put("prop2", prop2);

 RecordType recordType = new RecordType(registry, properties);
 assertEquals("RecordType", recordType.getClassName());
 assertEquals(2, recordType.getLength());

 assertTrue(recordType.hasProperty("prop1"));
 assertTrue(recordType.hasProperty("prop2"));
 assertFalse(recordType.hasProperty("nonExistentProperty"));
 }

 @Test
 public void testIsEquivalentTo() {
 RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
 RecordProperty prop2 = new RecordProperty("prop2", registry.getNumberType());
 RecordProperty prop3 = new RecordProperty("prop3", registry.getBooleanType());
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("prop1", prop1);
 properties1.put("prop2", prop2);
 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("prop1", prop1);
 properties2.put("prop2", prop2);
 properties2.put("prop3", prop3);

 RecordType recordType1 = new RecordType(registry, properties1);
 RecordType recordType2 = new RecordType(registry, properties2);
 RecordType recordType3 = new RecordType(registry, properties1);
 RecordType recordType4 = new RecordType(registry, properties2);
 RecordType recordType5 = new RecordType(registry, properties1);
 RecordType recordType6 = new RecordType(registry, properties3);

 assertTrue(recordType1.isEquivalentTo(recordType3));
 assertTrue(recordType1.isEquivalentTo(recordType5));
 assertFalse(recordType1.isEquivalentTo(recordType2));
 assertFalse(recordType1.isEquivalentTo(recordType4));
 assertFalse(recordType1.isEquivalentTo(recordType6));
 }

 @Test
 public void testIsSubtype() {
 RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
 RecordProperty prop2 = new RecordProperty("prop2", registry.getNumberType());
 RecordProperty prop3 = new RecordProperty("prop3", registry.getBooleanType());
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("prop1", prop1);
 properties1.put("prop2", prop2);
 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("prop1", prop1);
 properties2.put("prop2", prop2);
 properties2.put("prop3", prop3);
 Map<String, RecordProperty> properties3 = Maps.newHashMap();
 properties3.put("prop1", prop1);
 properties3.put("prop3", prop3);

 RecordType recordType1 = new RecordType(registry, properties1);
 RecordType recordType2 = new RecordType(registry, properties2);
 RecordType recordType3 = new RecordType(registry, properties3);
 RecordType recordType4 = new RecordType(registry, null);
 RecordType recordType5 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 assertTrue(recordType1.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(recordType3));
 assertFalse(recordType1.isSubtype(recordType2));
 assertFalse(recordType1.isSubtype(recordType4));
 assertFalse(recordType1.isSubtype(recordType5));
 }

 @Test
 public void testGetImplicitPrototype() {
 RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
 RecordProperty prop2 = new RecordProperty("prop2", registry.getNumberType());
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("prop1", prop1);
 properties.put("prop2", prop2);

 RecordType recordType = new RecordType(registry, properties);
 ObjectType prototype = recordType.getImplicitPrototype();
 assertEquals("Object", prototype.getClassName());
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyFrozen() {
 RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
 RecordProperty prop2 = new RecordProperty("prop2", registry.getNumberType());
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("prop1", prop1);
 properties.put("prop2", prop2);
 RecordType recordType = new RecordType(registry, properties);
 recordType.defineProperty("prop1", registry.getNumberType(), false, null);
 recordType.isFrozen = true;
 recordType.defineProperty("prop1", registry.getStringType(), false, null);
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
 RecordProperty prop2 = new RecordProperty("prop2", registry.getNumberType());
 RecordProperty prop3 = new RecordProperty("prop3", registry.getBooleanType());
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("prop1", prop1);
 properties1.put("prop2", prop2);
 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("prop1", prop1);
 properties2.put("prop2", prop2);
 properties2.put("prop3", prop3);
 Map<String, RecordProperty> properties3 = Maps.newHashMap();
 properties3.put("prop1", prop1);
 properties3.put("prop3", prop3);
 Map<String, RecordProperty> properties4 = Maps.newHashMap();
 properties4.put("prop3", prop3);
 Map<String, RecordProperty> properties5 = Maps.newHashMap();
 properties5.put("prop1", prop1);
 properties5.put("prop2", prop2);
 properties5.put("prop4", registry.getNumberType());

 RecordType recordType1 = new RecordType(registry, properties1);
 RecordType recordType2 = new RecordType(registry, properties2);
 RecordType recordType3 = new RecordType(registry, properties3);
 RecordType recordType4 = new RecordType(registry, properties4);
 RecordType recordType5 = new RecordType(registry, properties5);

 UnionTypeBuilder builder = new UnionTypeBuilder(registry);
 assertNotNull(recordType1.getGreatestSubtypeHelper(recordType2));
 assertNotNull(recordType1.getGreatestSubtypeHelper(recordType3));
 assertNull(recordType1.getGreatestSubtypeHelper(recordType4));
 assertNull(recordType1.getGreatestSubtypeHelper(recordType5));
 assertNotNull(recordType2.getGreatestSubtypeHelper(recordType1));
 assertNotNull(recordType2.getGreatestSubtypeHelper(recordType3));
 assertNull(recordType2.getGreatestSubtypeHelper(recordType4));
 assertNull(recordType2.getGreatestSubtypeHelper(recordType5));
 assertNotNull(recordType3.getGreatestSubtypeHelper(recordType1));
 assertNotNull(recordType3.getGreatestSubtypeHelper(recordType2));
 assertNull(recordType3.getGreatestSubtypeHelper(recordType4));
 assertNull(recordType3.getGreatestSubtypeHelper(recordType5));
 assertNull(recordType4.getGreatestSubtypeHelper(recordType1));
 assertNull(recordType4.getGreatestSubtypeHelper(recordType2));
 assertNull(recordType4.getGreatestSubtypeHelper(recordType3));
 assertNull(recordType4.getGreatestSubtypeHelper(recordType5));
 assertNotNull(recordType5.getGreatestSubtypeHelper(recordType1));
 assertNotNull(recordType5.getGreatestSubtypeHelper(recordType2));
 assertNotNull(recordType5.getGreatestSubtypeHelper(recordType3));
 assertNull(recordType5.getGreatestSubtypeHelper(recordType4));
 }
}