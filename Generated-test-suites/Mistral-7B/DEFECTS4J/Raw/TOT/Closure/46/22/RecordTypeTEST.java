import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RecordTypeTest {

 private static JSTypeRegistry registry;

 @BeforeClass
 public static void setup() {
 registry = new JSTypeRegistry();
 }

 @Test
 public void testRecordTypeCreation() {
 RecordProperty prop1 = new RecordProperty("name", String.class);
 RecordProperty prop2 = new RecordProperty("age", Integer.class);
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("name", prop1);
 properties.put("age", prop2);

 RecordType expectedType = new RecordType(registry, properties);
 JSType actualType = new RecordType(registry, properties);

 assertEquals(expectedType, actualType);
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType expectedType1 = new RecordType(registry, Maps.newHashMap());
 RecordType expectedType2 = new RecordType(registry, Maps.newHashMap());
 RecordType actualType1 = new RecordType(registry, Maps.newHashMap());
 RecordType actualType2 = new RecordType(registry, Maps.newHashMap());

 expectedType1.defineProperty("name", String.class, false, Node.NULL);
 expectedType1.defineProperty("age", Integer.class, false, Node.NULL);
 expectedType2.defineProperty("name", String.class, false, Node.NULL);
 expectedType2.defineProperty("age", Integer.class, false, Node.NULL);
 expectedType2.defineProperty("gender", String.class, false, Node.NULL);

 actualType1.defineProperty("name", String.class, false, Node.NULL);
 actualType1.defineProperty("age", Integer.class, false, Node.NULL);
 actualType2.defineProperty("name", String.class, false, Node.NULL);
 actualType2.defineProperty("age", Integer.class, false, Node.NULL);
 actualType2.defineProperty("gender", String.class, false, Node.NULL);

 assertEquals(expectedType1, actualType1);
 assertEquals(expectedType1, actualType2);
 }

 @Test
 public void testGetImplicitPrototype() {
 RecordType recordType = new RecordType(registry, Maps.newHashMap());
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
 recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 RecordType recordType = new RecordType(registry, Maps.newHashMap());
 recordType.defineProperty("name", String.class, false, Node.NULL);
 recordType.defineProperty("age", Integer.class, false, Node.NULL);

 assertEquals(String.class, recordType.getPropertyType("name"));
 assertEquals(Integer.class, recordType.getPropertyType("age"));
 }

 @Test
 public void testGetLeastSupertype() {
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 recordType1.defineProperty("name", String.class, false, Node.NULL);
 recordType1.defineProperty("age", Integer.class, false, Node.NULL);

 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("name", String.class, false, Node.NULL);
 recordType2.defineProperty("age", Integer.class, false, Node.NULL);
 recordType2.defineProperty("gender", String.class, false, Node.NULL);

 RecordType leastSupertype = recordType1.getLeastSupertype(recordType2);
 RecordTypeBuilder builder = new RecordTypeBuilder(registry);
 builder.addProperty("name", String.class, Node.NULL);
 builder.addProperty("age", Integer.class, Node.NULL);
 assertEquals(builder.build(), leastSupertype);
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType1.defineProperty("name", String.class, false, Node.NULL);
 recordType1.defineProperty("age", Integer.class, false, Node.NULL);
 recordType2.defineProperty("name", String.class, false, Node.NULL);
 recordType2.defineProperty("age", Integer.class, false, Node.NULL);
 recordType2.defineProperty("gender", String.class, false, Node.NULL);
 JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

 assertEquals(recordType2, greatestSubtype);
 }
}