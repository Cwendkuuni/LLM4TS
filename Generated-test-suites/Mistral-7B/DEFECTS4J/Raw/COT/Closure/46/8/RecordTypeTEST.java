import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @BeforeClass
 public static void setupClass() {
 registry = new JSTypeRegistry();
 }

 @Before
 public void setup() {
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("property1", new RecordProperty("property1", JSType.STRING));
 recordType = new RecordType(registry, properties);
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType sameRecord = new RecordType(registry, properties);
 assertTrue(recordType.isEquivalentTo(sameRecord));
 }

 @Test
 public void testIsEquivalentToDifferentRecord() {
 RecordType differentRecord = new RecordType(registry, Maps.newHashMap());
 assertFalse(recordType.isEquivalentTo(differentRecord));
 }

 @Test
 public void testIsSubtypeOfRecordType() {
 RecordType subtype = new RecordType(registry, Maps.newHashMap());
 subtype.defineProperty("property1", JSType.NUMBER, false, null);
 RecordType supertype = new RecordType(registry, properties);
 assertTrue(subtype.isSubtype(supertype));
 }

 @Test
 public void testIsSubtypeOfObjectType() {
 RecordType recordType = new RecordType(registry, properties);
 assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isSubtype(recordType));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWhenFrozen() {
 recordType.defineProperty("property1", JSType.STRING, false, null);
 fail("Expected IllegalStateException when defining property on a frozen record type.");
 }

 @Test
 public void testGetLeastSupertype() {
 RecordType subtype1 = new RecordType(registry, Maps.newHashMap());
 subtype1.defineProperty("property1", JSType.NUMBER, false, null);
 RecordType subtype2 = new RecordType(registry, Maps.newHashMap());
 subtype2.defineProperty("property2", JSType.STRING, false, null);
 RecordType supertype = recordType.getLeastSupertype(subtype1);
 supertype.defineProperty("property2", JSType.STRING, false, null);
 RecordType greatestSubtype = recordType.getLeastSupertype(subtype2);
 greatestSubtype.defineProperty("property1", JSType.NUMBER, false, null);
 greatestSubtype.defineProperty("property2", JSType.STRING, false, null);

 assertEquals(greatestSubtype.getName(), supertype.getName());
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType = new RecordType(registry, properties);
 JSType nativeObjectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 RecordType subtype = new RecordType(registry, Maps.newHashMap());
 subtype.defineProperty("property1", JSType.NUMBER, false, null);
 JSType noType = JSType.NO_TYPE;
 JSType noObjectType = registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE);

 assertEquals(noObjectType, recordType.getGreatestSubtypeHelper(noType));
 assertEquals(nativeObjectType, recordType.getGreatestSubtypeHelper(subtype));

 RecordType recordWithPropertyConflict = new RecordType(registry, Maps.newHashMap());
 recordWithPropertyConflict.defineProperty("property1", JSType.BOOLEAN, false, null);
 RecordType recordWithSameProperties = new RecordType(registry, Maps.newHashMap());
 recordWithSameProperties.defineProperty("property1", JSType.STRING, false, null);
 assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE),
 recordType.getGreatestSubtypeHelper(recordWithPropertyConflict));
 assertEquals(recordWithSameProperties, recordType.getGreatestSubtypeHelper(recordWithSameProperties));
 }

 @Test
 public void testToString() {
 assertEquals("RecordType { property1: String }", recordType.toString());
 }
}