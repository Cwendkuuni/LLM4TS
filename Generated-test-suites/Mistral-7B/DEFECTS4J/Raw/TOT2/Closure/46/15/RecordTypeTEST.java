import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

 @BeforeClass
 public static void setup() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType = new RecordType(registry, Maps.newHashMap());
 // Adding some test properties
 recordType.defineProperty("name", registry.StringType, false, null);
 recordType.defineProperty("age", registry.NumberType, false, null);
 recordType.defineProperty("isStudent", registry.BooleanType, false, null);
 }

 @Test
 public void testRecordTypeIsEquivalentToSelf() {
 RecordType recordType = new RecordType(JSTypeRegistry.getInstance(),
 Maps.newHashMap(Maps.newHashMap().put("name",
 new RecordProperty("name", JSTypeNative.STRING_TYPE))));
 assertTrue(recordType.isEquivalentTo(recordType));
 }

 @Test
 public void testRecordTypeIsSubtypeOfSelf() {
 RecordType recordType = new RecordType(JSTypeRegistry.getInstance(),
 Maps.newHashMap(Maps.newHashMap().put("name",
 new RecordProperty("name", JSTypeNative.STRING_TYPE))));
 assertTrue(recordType.isSubtype(recordType));
 }

 @Test
 public void testRecordTypeIsSubtypeOfSamePropertiesWithSameTypes() {
 RecordType recordType1 = new RecordType(JSTypeRegistry.getInstance(),
 Maps.newHashMap(Maps.newHashMap().put("name",
 new RecordProperty("name", JSTypeNative.STRING_TYPE)),
 Maps.newHashMap().put("age",
 new RecordProperty("age", JSTypeNative.NUMBER_TYPE))));
 RecordType recordType2 = new RecordType(JSTypeRegistry.getInstance(),
 Maps.newHashMap(Maps.newHashMap().put("name",
 new RecordProperty("name", JSTypeNative.STRING_TYPE)),
 Maps.newHashMap().put("age",
 new RecordProperty("age", JSTypeNative.NUMBER_TYPE))));
 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testRecordTypeIsSubtypeOfSamePropertiesWithDifferentTypes() {
 RecordType recordType1 = new RecordType(JSTypeRegistry.getInstance(),
 Maps.newHashMap(Maps.newHashMap().put("name",
 new RecordProperty("name", JSTypeNative.STRING_TYPE)),
 Maps.newHashMap().put("age",
 new RecordProperty("age", JSTypeNative.NUMBER_TYPE))));
 RecordType recordType2 = new RecordType(JSTypeRegistry.getInstance(),
 Maps.newHashMap(Maps.newHashMap().put("name",
 new RecordProperty("name", JSTypeNative.NUMBER_TYPE)),
 Maps.newHashMap().put("age",
 new RecordProperty("age", JSTypeNative.STRING_TYPE))));
 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testRecordTypeIsSubtypeOfSuperProperties() {
 RecordType recordType1 = new RecordType(JSTypeRegistry.getInstance(),
 Maps.newHashMap(Maps.newHashMap().put("name",
 new RecordProperty("name", JSTypeNative.STRING_TYPE)),
 Maps.newHashMap().put("age",
 new RecordProperty("age", JSTypeNative.NUMBER_TYPE))));
 RecordType recordType2 = new RecordType(JSTypeRegistry.getInstance(),
 Maps.newHashMap(Maps.newHashMap().put("name",
 new RecordProperty("name", JSTypeNative.STRING_TYPE))));
 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testRecordTypeIsNotSubtypeOfSubProperties() {
 RecordType recordType1 = new RecordType(JSTypeRegistry.getInstance(),
 Maps.newHashMap(Maps.newHashMap().put("name",
 new RecordProperty("name", JSTypeNative.STRING_TYPE)),
 Maps.newHashMap().put("age",
 new RecordProperty("age", JSTypeNative.NUMBER_TYPE))));
 RecordType recordType2 = new RecordType(JSTypeRegistry.getInstance(),
 Maps.newHashMap(Maps.newHashMap().put("name",
 new RecordProperty("name", JSTypeNative.STRING_TYPE)),
 Maps.newHashMap().put("age",
 new RecordProperty("age", JSTypeNative.OBJECT_TYPE))));
 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test(expected = IllegalStateException.class)
 public void testRecordTypeFrozen() {
 RecordType recordType = new RecordType(JSTypeRegistry.getInstance(),
 Maps.newHashMap(Maps.newHashMap().put("name",
 new RecordProperty("name", JSTypeNative.STRING_TYPE)),
 Maps.newHashMap().put("age",
 new RecordProperty("age", JSTypeNative.NUMBER_TYPE))));
 recordType.isFrozen = true;
 recordType.defineProperty("newProperty", JSTypeRegistry.getInstance().AnyType, false, null);
 }

 @Test
 public void testRecordTypeNullProperties() {
 RecordType recordType = new RecordType(JSTypeRegistry.getInstance(), null);
 assertFalse(recordType.isSubtype(recordType));
 }

 @Test
 public void testRecordTypeEmptyProperties() {
 RecordType recordType = new RecordType(JSTypeRegistry.getInstance(), Maps.<String, RecordProperty>newHashMap());
 assertFalse(recordType.isSubtype(recordType));
 }
}