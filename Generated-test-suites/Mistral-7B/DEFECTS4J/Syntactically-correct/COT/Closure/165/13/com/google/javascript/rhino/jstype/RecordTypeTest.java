import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @Before
 public void setup() {
 registry = new JSTypeRegistry();
 RecordProperty prop1 = new RecordProperty("prop1", Integer.TYPE);
 RecordProperty prop2 = new RecordProperty("prop2", Double.TYPE);
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("prop1", prop1);
 properties.put("prop2", prop2);
 recordType = new RecordType(registry, properties);
 }

 @Test
 public void testRecordTypeCreation() {
 JSType recordTypeInstance = recordType.getInstanceType();
 assertEquals("RecordType", recordTypeInstance.getName());
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType recordType2 = new RecordType(registry, properties);
 assertTrue(recordType.isEquivalentTo(recordType2));
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType2 = new RecordType(registry, properties);
 RecordType recordType3 = new RecordType(registry, Maps.newLinkedHashMap(properties));
 RecordType recordType4 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType5 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType6 = new RecordType(registry, Maps.newHashMap());
 recordType6.setFrozen(true);

 assertTrue(recordType.isSubtype(recordType));
 assertTrue(recordType.isSubtype(recordType2));
 assertFalse(recordType.isSubtype(recordType3));
 assertFalse(recordType.isSubtype(recordType4));
 assertFalse(recordType.isSubtype(recordType5));
 assertFalse(recordType.isSubtype(recordType6));
 }

 @Test(expected = IllegalStateException.class)
 public void testNullRecordProperty() {
 RecordProperty nullProp = null;
 Map<String, RecordProperty> nullProperties = Maps.newHashMap();
 nullProperties.put("nullProp", nullProp);
 new RecordType(registry, nullProperties);
 }

 @Test
 public void testDefineProperty() {
 String newProperty = "newProperty";
 JSType newPropertyType = String.class;
 recordType.defineProperty(newProperty, newPropertyType, false, null);
 RecordProperty newRecordProperty = recordType.getProperty(newProperty);
 assertEquals(newPropertyType, newRecordProperty.getType());
 }

 @Test(expected = IllegalStateException.class)
 public void testFreezeAndDefineProperty() {
 recordType.setFrozen(true);
 recordType.defineProperty("newProperty", Integer.TYPE, false, null);
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType2 = new RecordType(registry, properties);
 RecordType recordType3 = new RecordType(registry, Maps.newLinkedHashMap(properties));
 RecordType recordType4 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType5 = new RecordType(registry, Maps.newLinkedHashMap(properties));
 recordType5.setFrozen(true);

 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(recordType2);
 assertEquals(recordType, greatestSubtype);

 greatestSubtype = recordType.getGreatestSubtypeHelper(recordType3);
 assertEquals(recordType, greatestSubtype);

 greatestSubtype = recordType.getGreatestSubtypeHelper(recordType4);
 assertEquals(recordType, greatestSubtype);

 greatestSubtype = recordType.getGreatestSubtypeHelper(recordType5);
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype);
 }

 @Test
 public void testIsSubtypeOfObjectType() {
 JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 assertTrue(recordType.isSubtype(objectType));
 }

 @Test
 public void testGetImplicitPrototype() {
 JSType implicitPrototype = recordType.getImplicitPrototype();
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
 }
}