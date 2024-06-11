import static org.junit.Assert.*;
import com.google.javascript.rhino.*;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @BeforeClass
 public static void setup() {
 registry = new JSTypeRegistry();
 }

 @Before
 public void setUp() {
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
 public void testIsSubtypeOfRecordType() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 assertFalse(recordType2.isSubtype(recordType1));

 RecordType recordType3 = new RecordType(registry, properties);
 RecordType recordType4 = new RecordType(registry, properties);
 recordType4.freeze();
 assertFalse(recordType3.isSubtype(recordType4));
 }

 @Test
 public void testIsSubtypeOfNativeType() {
 RecordType recordType = new RecordType(registry, properties);
 assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWhenFrozen() {
 recordType.freeze();
 try {
 recordType.defineProperty("prop1", registry.getStringType(), false, null);
 } catch (IllegalStateException e) {
 assertEquals("Cannot define property 'prop1' on RecordType instance.", e.getMessage());
 }
 }

 @Test
 public void testGetLeastSupertype() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("prop1", registry.getStringType(), false, null);
 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
 recordType3.defineProperty("prop1", registry.getStringType(), false, null);
 recordType3.defineProperty("prop2", registry.getNumberType(), false, null);

 JSType leastSupertype = recordType1.getLeastSupertype(recordType2);
 assertEquals(recordType3.getClass(), leastSupertype.getClass());
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("prop1", registry.getStringType(), false, null);
 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
 recordType3.addProperty("prop1", registry.getStringType(), null);
 recordType3.addProperty("prop2", registry.getNumberType(), null);

 JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType3);
 assertEquals(recordType1.getClass(), greatestSubtype.getClass());
 }

 @Test
 public void testDefinePropertyInferred() {
 recordType.defineProperty("prop1", registry.getStringType(), true, null);
 RecordProperty prop1 = recordType.getProperty("prop1");
 assertEquals(registry.getStringType().getClass(), prop1.getType().getClass());
 }

 @Test
 public void testToString() {
 assertEquals("RecordType {prop1: String, prop2: Number}", recordType.toString());
 }

}