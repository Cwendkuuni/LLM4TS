import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.node.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
 RecordProperty prop2 = new RecordProperty("prop2", registry.getNumberType());
 Map<String, RecordProperty> properties = Maps.newHashMap();
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
 public void testIsSubtype() {
 RecordType recordType1 = new RecordType(registry, properties);
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 // Record type is a subtype of Object type
 assertTrue(recordType1.isSubtype(objectType));

 // Object type is not a subtype of record type
 assertFalse(objectType.isSubtype(recordType1));

 // Subtype relationship between two record types
 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("prop1", new RecordProperty("prop1", registry.getStringType()));
 RecordType recordType2 = new RecordType(registry, properties2);
 assertTrue(recordType1.isSubtype(recordType2));
 assertFalse(recordType2.isSubtype(recordType1));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType3 = new RecordType(registry, properties);
 recordType3.defineProperty("prop3", registry.getNumberType(), false, null);

 JSType type1 = recordType1.getGreatestSubtypeHelper(recordType2);
 assertEquals(recordType2, type1);

 JSType type2 = recordType1.getGreatestSubtypeHelper(recordType3);
 assertEquals(recordType3, type2);
 }

 @Test
 public void testIsSubtypeRecordTypeAndObjectType() {
 RecordType recordType = new RecordType(registry, properties);
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 // Object type is a subtype of record type
 assertTrue(objectType.isSubtype(recordType));

 // Record type is not a subtype of Object type
 assertFalse(recordType.isSubtype(objectType));
 }

 @Test
 public void testHasProperty() {
 RecordType recordType = new RecordType(registry, properties);
 assertTrue(recordType.hasProperty("prop1"));
 assertFalse(recordType.hasProperty("prop3"));
 }

 @Test
 public void testGetPropertyType() {
 RecordType recordType = new RecordType(registry, properties);
 assertEquals(registry.getStringType(), recordType.getPropertyType("prop1"));
 assertEquals(registry.getNumberType(), recordType.getPropertyType("prop2"));
 assertNull(recordType.getPropertyType("prop3"));
 }

 @Test
 public void testGetPropertyNode() {
 RecordType recordType = new RecordType(registry, properties);
 assertNull(recordType.getPropertyNode("prop1"));
 assertNull(recordType.getPropertyNode("prop3"));
 }

 @Test
 public void testDefineProperty() {
 RecordType recordType = new RecordType(registry, properties);
 recordType.defineProperty("prop3", registry.getStringType(), false, null);
 RecordProperty prop3 = new RecordProperty("prop3", registry.getStringType());
 assertEquals(prop3, recordType.getProperty("prop3"));
 }

 @Test (expected = IllegalStateException.class)
 public void testDefinePropertyWithNullRecordProperty() {
 RecordType recordType = new RecordType(registry, properties);
 recordType.defineProperty("prop3", null, false, null);
 }
}