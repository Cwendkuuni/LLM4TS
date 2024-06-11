import static org.junit.Assert.*;
import static com.google.javascript.rhino.JstypeTest.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 RecordProperty prop1 = new RecordProperty("prop1", intType());
 RecordProperty prop2 = new RecordProperty("prop2", booleanType());
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
 public void testGetImplicitPrototype() {
 ObjectType implicitPrototype = recordType.getImplicitPrototype();
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
 implicitPrototype);
 }

 @Test
 public void testDefineProperty() {
 String propertyName = "newProperty";
 JSType newPropertyType = stringType();
 RecordProperty newProperty = new RecordProperty(propertyName, newPropertyType);
 recordType.defineProperty(propertyName, newPropertyType, false, null);
 assertTrue(recordType.hasProperty(propertyName));
 assertEquals(newPropertyType, recordType.getPropertyType(propertyName));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyFrozen() {
 recordType.isFrozen = true;
 String propertyName = "newProperty";
 JSType newPropertyType = stringType();
 recordType.defineProperty(propertyName, newPropertyType, false, null);
 fail("Should not be able to define property on a frozen record type");
 }

 @Test
 public void testGetLeastSupertype() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);
 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
 recordType3.defineProperty("prop1", intType(), false, null);
 JSType leastSupertype = recordType1.getLeastSupertype(recordType2);
 assertEquals(recordType3, leastSupertype);
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("prop1", intType(), false, null);
 JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 assertEquals(recordType1, greatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);
 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType4 = new RecordType(registry, Maps.newHashMap());
 recordType3.defineProperty("prop1", intType(), false, null);
 recordType4.defineProperty("prop2", intType(), false, null);

 RecordType recordType5 = new RecordType(registry, Maps.newHashMap());
 recordType5.defineProperty("prop1", recordType3.getType(), false, null);
 recordType5.defineProperty("prop2", recordType4.getType(), false, null);

 assertTrue(recordType1.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(recordType2));
 assertTrue(recordType1.isSubtype(recordType3));
 assertFalse(recordType1.isSubtype(recordType4));
 assertFalse(recordType1.isSubtype(recordType5));
 }
}