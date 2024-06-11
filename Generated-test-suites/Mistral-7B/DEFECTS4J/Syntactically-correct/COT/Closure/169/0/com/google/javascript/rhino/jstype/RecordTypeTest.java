import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class RecordTypeTest {

 private static RecordType createRecordType(JSTypeRegistry registry, Map<String, RecordProperty> properties) {
 return new RecordType(registry, properties, true);
 }

 @Test
 public void testCreateRecordType() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("age", new RecordProperty(IntType.getInstance(), new NodeConstant(false)));
 properties.put("name", new RecordProperty(StringType.getInstance(), new NodeConstant(false)));
 RecordType recordType = createRecordType(registry, properties);
 assertEquals(List.of("age", "name"), recordType.getPropertyNames());
 }

 @Test
 public void testIsSynthetic() {
 RecordType declaredRecordType = createRecordType(new JSTypeRegistry(), Maps.newHashMap());
 assertFalse(declaredRecordType.isSynthetic());
 RecordType syntheticRecordType = new RecordType(new JSTypeRegistry(), Maps.newHashMap(), false);
 assertTrue(syntheticRecordType.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties1 = Maps.newHashMap();
 properties1.put("a", new RecordProperty(IntType.getInstance(), new NodeConstant(false)));
 properties1.put("b", new RecordProperty(StringType.getInstance(), new NodeConstant(false)));
 RecordType recordType1 = createRecordType(registry, properties1);
 Map<String, RecordProperty> properties2 = Maps.newHashMap();
 properties2.put("a", new RecordProperty(IntType.getInstance(), new NodeConstant(false)));
 properties2.put("b", new RecordProperty(IntType.getInstance(), new NodeConstant(false)));
 RecordType recordType2 = createRecordType(registry, properties2);
 recordType1.freeze();
 recordType2.freeze();
 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));

 properties2.put("c", new RecordProperty(IntType.getInstance(), new NodeConstant(false)));
 assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, false));
 }

 @Test
 public void testDefineProperty() {
 RecordType recordType = createRecordType(new JSTypeRegistry(), Maps.newHashMap());
 assertTrue(recordType.defineProperty("age", IntType.getInstance(), false, new NodeConstant("")));

 recordType.freeze();
 assertFalse(recordType.defineProperty("age", IntType.getInstance(), false, new NodeConstant("")));
 }

 @Test(expected = IllegalStateException.class)
 public void testNullProperty() {
 RecordType recordType = createRecordType(new JSTypeRegistry(), Maps.newHashMap());
 recordType.defineProperty("age", null, false, new NodeConstant(""));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType1 = createRecordType(registry, Maps.newHashMap());
 RecordType recordType2 = createRecordType(registry, Maps.newHashMap());
 RecordType recordType3 = createRecordType(registry, Maps.newHashMap());
 recordType3.defineProperty("x", IntType.getInstance(), false, new NodeConstant(""));
 recordType3.defineProperty("y", StringType.getInstance(), false, new NodeConstant(""));

 RecordType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 assertEquals(recordType1, greatestSubtype);
 greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType3);
 assertEquals(recordType3, greatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType1 = createRecordType(registry, Maps.newHashMap());
 RecordType recordType2 = createRecordType(registry, Maps.newHashMap());
 RecordType recordType3 = createRecordType(registry, Maps.newHashMap());
 recordType3.defineProperty("x", IntType.getInstance(), false, new NodeConstant(""));
 recordType3.defineProperty("y", StringType.getInstance(), false, new NodeConstant(""));

 RecordType recordType1ToRecordType2 = new RecordType(registry, Maps.newHashMap(), true);
 recordType1ToRecordType2.defineProperty("x", IntType.getInstance(), false, new NodeConstant(""));
 recordType1ToRecordType2.defineProperty("y", StringType.getInstance(), false, new NodeConstant(""));
 assertTrue(recordType1.isSubtype(recordType1ToRecordType2));

 RecordType recordType1ToRecordType3 = new RecordType(registry, Maps.newHashMap(), true);
 recordType1ToRecordType3.defineProperty("x", IntType.getInstance(), false, new NodeConstant(""));
 recordType1ToRecordType3.defineProperty("y", StringType.getInstance(), false, new NodeConstant(""));
 recordType1ToRecordType3.defineProperty("z", IntType.getInstance(), false, new NodeConstant(""));
 assertFalse(recordType1.isSubtype(recordType1ToRecordType3));

 RecordType recordType2ToRecordType3 = new RecordType(registry, Maps.newHashMap(), true);
 recordType2ToRecordType3.defineProperty("x", IntType.getInstance(), false, new NodeConstant(""));
 recordType2ToRecordType3.defineProperty("y", StringType.getInstance(), false, new NodeConstant(""));
 recordType2ToRecordType3.defineProperty("z", IntType.getInstance(), false, new NodeConstant(""));
 assertTrue(recordType2.isSubtype(recordType2ToRecordType3));
 }

}