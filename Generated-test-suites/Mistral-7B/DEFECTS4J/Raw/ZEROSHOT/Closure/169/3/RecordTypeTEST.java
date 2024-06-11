import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 }

 @Test
 public void testCreateRecordType() {
 // Create a record type with 2 properties: a of type Number and b of type String.
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", new RecordProperty(JSType.forInstance(double.class), null));
 properties.put("b", new RecordProperty(JSType.forInstance(String.class), null));
 RecordType recordType = new RecordType(registry, properties, true);

 // Check properties.
 assertEquals(2, recordType.getPropertyCount());
 assertEquals(JSType.forInstance(double.class), recordType.getPropertyType("a"));
 assertEquals(JSType.forInstance(String.class), recordType.getPropertyType("b"));
 // Check toString.
 assertEquals("RecordType[a=Number, b=String]", recordType.toString());
 }

 @Test
 public void testCreateRecordTypeWithNullRecordProperty() {
 // Create a record type with a null RecordProperty.
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", null);
 RecordType recordType = new RecordType(registry, properties, true);

 // Check that an IllegalStateException is thrown.
 try {
 recordType.getPropertyType("a");
 fail("Expected an IllegalStateException to be thrown.");
 } catch (IllegalStateException e) {
 // Expected.
 }
 }

 @Test
 public void testCreateRecordWithSameProperties() {
 // Create 2 record types with the same properties.
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("a", new RecordProperty(JSType.forInstance(double.class), null));
 properties1.put("b", new RecordProperty(JSType.forInstance(String.class), null));
 RecordType recordType1 = new RecordType(registry, properties1, true);

 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("a", new RecordProperty(JSType.forInstance(double.class), null));
 properties2.put("b", new RecordProperty(JSType.forInstance(String.class), null));
 RecordType recordType2 = new RecordType(registry, properties2, true);

 // Check that both record types are equivalent.
 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));
 }

 @Test
 public void testCreateRecordWithDifferentProperties() {
 // Create 2 record types with different properties.
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("a", new RecordProperty(JSType.forInstance(double.class), null));
 properties1.put("b", new RecordProperty(JSType.forInstance(String.class), null));
 RecordType recordType1 = new RecordType(registry, properties1, true);

 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("a", new RecordProperty(JSType.forInstance(int.class), null));
 properties2.put("c", new RecordProperty(JSType.forInstance(boolean.class), null));
 RecordType recordType2 = new RecordType(registry, properties2, true);

 // Check that both record types are not equivalent.
 assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, false));
 }

 @Test
 public void testGetGreatestSubtype() {
 // Create 2 record types with different properties.
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("a", new RecordProperty(JSType.forInstance(double.class), null));
 properties1.put("b", new RecordProperty(JSType.forInstance(String.class), null));
 RecordType recordType1 = new RecordType(registry, properties1, true);

 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("a", new RecordProperty(JSType.forInstance(int.class), null));
 properties2.put("c", new RecordProperty(JSType.forInstance(boolean.class), null));
 RecordType recordType2 = new RecordType(registry, properties2, true);

 // The greatest subtype of recordType1 and recordType2 is an empty record type.
 JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);
 assertEquals(JSTypeNative.OBJECT_TYPE, greatestSubtype);

 // The greatest subtype of recordType2 and recordType1 is also an empty record type.
 greatestSubtype = recordType2.getGreatestSubtype(recordType1);
 assertEquals(JSTypeNative.OBJECT_TYPE, greatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 // Create 2 record types with different properties.
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("a", new RecordProperty(JSType.forInstance(double.class), null));
 properties1.put("b", new RecordProperty(JSType.forInstance(String.class), null));
 RecordType recordType1 = new RecordType(registry, properties1, true);

 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("a", new RecordProperty(JSType.forInstance(int.class), null));
 properties2.put("c", new RecordProperty(JSType.forInstance(boolean.class), null));
 RecordType recordType2 = new RecordType(registry, properties2, true);

 // recordType1 is a subtype of recordType2 because recordType2 has no declared properties.
 assertTrue(recordType1.isSubtype(recordType2));

 // recordType2 is not a subtype of recordType1 because it has a property "c" that recordType1 doesn't have.
 assertFalse(recordType2.isSubtype(recordType1));

 // A Number is a subtype of a RecordType with a property "a" of type Number.
 RecordProperty recordProperty = new RecordProperty(JSType.forInstance(double.class), null);
 RecordType recordType3 = new RecordType(registry, Maps.singletonMap("a", recordProperty), true);
 assertTrue(JSType.forInstance(double.class).isSubtype(recordType3));

 // A RecordType with a property "a" of type Number is a subtype of Number because Number is the greatest subtype of any type.
 assertTrue(recordType3.isSubtype(JSType.forInstance(double.class)));
 }
}