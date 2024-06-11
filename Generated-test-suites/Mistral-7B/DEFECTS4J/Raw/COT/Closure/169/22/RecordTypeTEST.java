import com.google.javascript.rhino.*;
import static com.google.javascript.rhino.Node.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeTest {

 private static JSTypeRegistry registry = new JSTypeRegistry();
 private static ErrorReporter t = ErrorReporter.NULL;
 private static RecordType recordType;

 @Test
 public void testRecordTypeConstructor() {
 RecordType recordType = new RecordType(registry, Maps.newHashMap(), true);
 assertNotNull(recordType);
 }

 @Test
 public void testIsSynthetic() {
 RecordType recordType = new RecordType(registry, Maps.newHashMap(), false);
 assertTrue(recordType.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap(), true);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap(), true);
 RecordType recordType3 = new RecordType(registry, Maps.newHashMap(), true);
 recordType1.defineProperty("prop1", INT_TYPE, false, null);
 recordType1.defineProperty("prop2", STRING_TYPE, false, null);
 recordType2.defineProperty("prop1", INT_TYPE, false, null);
 recordType2.defineProperty("prop2", STRING_TYPE, false, null);
 recordType2.defineProperty("prop3", INT_TYPE, false, null);
 recordType3.defineProperty("prop1", INT_TYPE, false, null);
 recordType3.defineProperty("prop2", STRING_TYPE, false, null);
 recordType3.defineProperty("prop3", STRING_TYPE, false, null);

 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType1, true));
 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));
 assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, true));
 }

 @Test
 public void testGetImplicitPrototype() {
 RecordType recordType = new RecordType(registry, Maps.newHashMap(), true);
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
 recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 RecordType recordType = new RecordType(registry, Maps.newHashMap(), true);
 recordType.defineProperty("prop1", INT_TYPE, false, null);
 recordType.defineProperty("prop2", STRING_TYPE, false, null);

 assertTrue(recordType.hasProperty("prop1"));
 assertTrue(recordType.hasProperty("prop2"));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyNullRecordProperty() {
 RecordType recordType = new RecordType(registry, Maps.newHashMap(), true);
 Map<String, RecordProperty> properties = recordType.getProperties();
 properties.put("prop1", null);
 recordType.defineProperties(properties);
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap(), true);
 recordType1.defineProperty("prop1", INT_TYPE, false, null);
 recordType1.defineProperty("prop2", STRING_TYPE, false, null);

 RecordType recordType2 = new RecordType(registry, Maps.newHashMap(), true);
 recordType2.defineProperty("prop1", INT_TYPE, false, null);
 recordType2.defineProperty("prop3", INT_TYPE, false, null);

 RecordType recordType3 = new RecordType(registry, Maps.newHashMap(), true);
 recordType3.defineProperty("prop2", STRING_TYPE, false, null);
 recordType3.defineProperty("prop3", INT_TYPE, false, null);

 RecordType recordType4 = new RecordType(registry, Maps.newHashMap(), true);
 recordType4.defineProperty("prop3", STRING_TYPE, false, null);
 recordType4.defineProperty("prop4", INT_TYPE, false, null);

 JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 assertEquals(recordType2, greatestSubtype);

 greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType3);
 greatestSubtype = recordType1.getGreatestSubtypeHelper(grearestSubtype);
 assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), greatestSubtype);

 greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType4);
 greatestSubtype = recordType1.getGreatestSubtypeHelper(grearestSubtype);
 assertEquals(recordType3, greatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap(), true);
 recordType1.defineProperty("prop1", INT_TYPE, false, null);
 recordType1.defineProperty("prop2", STRING_TYPE, false, null);

 RecordType recordType2 = new RecordType(registry, Maps.newHashMap(), true);
 recordType2.defineProperty("prop1", INT_TYPE, false, null);
 recordType2.defineProperty("prop2", STRING_TYPE, false, null);
 recordType2.defineProperty("prop3", INT_TYPE, false, null);

 RecordType recordType3 = new RecordType(registry, Maps.newHashMap(), true);
 recordType3.defineProperty("prop1", INT_TYPE, false, null);
 recordType3.defineProperty("prop2", STRING_TYPE, false, null);
 recordType3.defineProperty("prop4", INT_TYPE, false, null);

 RecordType recordType4 = new RecordType(registry, Maps.newHashMap(), true);
 recordType4.defineProperty("prop1", INT_TYPE, false, null);
 recordType4.defineProperty("prop2", STRING_TYPE, false, null);
 recordType4.defineProperty("prop5", INT_TYPE, false, null);

 assertTrue(recordType1.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(recordType2));
 assertFalse(recordType1.isSubtype(recordType3));
 assertFalse(recordType1.isSubtype(recordType4));
 }

 @Test
 public void testResolveInternal() {
 RecordType recordType = new RecordType(registry, Maps.newHashMap(), true);
 recordType.defineProperty("prop1", INT_TYPE, false, null);
 recordType.defineProperty("prop2", STRING_TYPE, false, null);

 JSType resolvedType = recordType.resolveInternal(t, null);
 assertEquals(recordType, resolvedType);
 }
}