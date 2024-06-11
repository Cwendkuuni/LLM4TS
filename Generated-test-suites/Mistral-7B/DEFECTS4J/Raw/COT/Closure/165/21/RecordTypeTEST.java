import static org.junit.Assert.*;
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
 RecordProperty recordProperty1 = new RecordProperty("property1", JSType.STRING);
 RecordProperty recordProperty2 = new RecordProperty("property2", JSType.NUMBER);
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("property1", recordProperty1);
 properties.put("property2", recordProperty2);
 recordType = new RecordType(registry, properties);
 }

 @Test
 public void testCreateRecordType() {
 // Basic functionality test
 assertEquals("{property1: string, property2: number}", recordType.toString());
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);
 assertTrue(recordType1.isEquivalentTo(recordType2));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordProperty recordProperty3 = new RecordProperty("property3", JSType.BOOLEAN);
 Map<String, RecordProperty> properties3 = Maps.newHashMap();
 properties3.put("property1", recordProperty1);
 properties3.put("property2", recordProperty2);
 properties3.put("property3", recordProperty3);
 RecordType recordType3 = new RecordType(registry, properties3);
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(recordType3);
 assertEquals(recordType, greatestSubtype);
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWhenFrozen() {
 recordType.defineProperty("property1", JSType.NUMBER, false, null);
 recordType.isFrozen = true;
 recordType.defineProperty("property1", JSType.STRING, false, null);
 }

 @Test
 public void testIsSubtype() {
 RecordType recordTypeWithMoreProperties = new RecordType(registry, properties);
 RecordType recordTypeWithLessProperties = new RecordType(registry, Maps.newHashMap());
 RecordType recordTypeWithSameProperties = new RecordType(registry, properties);
 RecordType recordTypeWithDifferentTypes = new RecordType(registry, properties);
 recordTypeWithDifferentTypes.getPropertyType("property1").setTypeName("String");

 assertTrue(recordType.isSubtype(recordTypeWithSameProperties));
 assertTrue(recordType.isSubtype(recordTypeWithMoreProperties));
 assertFalse(recordType.isSubtype(recordTypeWithLessProperties));
 assertFalse(recordType.isSubtype(recordTypeWithDifferentTypes));
 }

 @Test
 public void testGetImplicitPrototype() {
 ObjectType implicitPrototype = recordType.getImplicitPrototype();
 assertEquals("Object", implicitPrototype.getName());
 }
}