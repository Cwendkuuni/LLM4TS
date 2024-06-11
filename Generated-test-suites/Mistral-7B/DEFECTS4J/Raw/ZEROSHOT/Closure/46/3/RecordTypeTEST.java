import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.TypeFactory;
import com.google.javascript.rhino.jstype.TypeRegistry;
import org.junit.Test;

public class RecordTypeTest {

 private TypeRegistry registry;
 private ErrorReporter errorReporter;

 @BeforeClass
 public static void setup() {
 TypeFactory typeFactory = new TypeFactory();
 registry = typeFactory.getRegistry();
 }

 @Before
 public void setupEach() {
 errorReporter = new ErrorReporter() {};
 }

 @Test
 public void testRecordTypeCreation() {
 RecordProperty prop1 = new RecordProperty("name", registry.getStringType());
 RecordProperty prop2 = new RecordProperty("age", registry.getNumberType());

 RecordTypeBuilder builder = new RecordTypeBuilder(registry);
 builder.addProperty("name", registry.getStringType(), null);
 builder.addProperty("age", registry.getNumberType(), null);

 RecordType recordType = new RecordType(registry, builder.build());

 assertEquals(2, recordType.getPropertyCount());
 assertEquals("name", recordType.getPropertyNames()[0]);
 assertEquals("age", recordType.getPropertyNames()[1]);
 assertEquals(registry.getStringType(), recordType.getPropertyType("name"));
 assertEquals(registry.getNumberType(), recordType.getPropertyType("age"));
 }

 @Test
 public void testRecordTypeEquivalence() {
 RecordType recordType1 = createRecordType("name", registry.getStringType(),
 "age", registry.getNumberType());
 RecordType recordType2 = createRecordType("name", registry.getStringType(),
 "age", registry.getNumberType());
 RecordType recordType3 = createRecordType("name", registry.getStringType(),
 "age", registry.getNumberType(), "id", registry.getNumberType());

 assertEquals(recordType1, recordType1);
 assertEquals(recordType1, recordType2);
 assertNotEquals(recordType1, recordType3);
 }

 @Test
 public void testRecordTypeSubtype() {
 RecordType recordType1 = createRecordType("name", registry.getStringType(),
 "age", registry.getNumberType());
 RecordType recordType2 = createRecordType("name", registry.getStringType(),
 "age", registry.getNumberType(), "id", registry.getNumberType());

 assertTrue(recordType1.isSubtype(recordType2));

 RecordType recordType3 = createRecordType("name", registry.getStringType(),
 "age", registry.getNumberType(), "id", registry.getInt32Type());

 assertFalse(recordType1.isSubtype(recordType3));
 }

 @Test
 public void testRecordTypeFreeze() {
 RecordType recordType = createRecordType("name", registry.getStringType(),
 "age", registry.getNumberType());
 recordType.freeze();

 RecordProperty newProp = new RecordProperty("newProp", registry.getNumberType());
 RecordTypeBuilder newBuilder = new RecordTypeBuilder(registry);
 newBuilder.addProperty("newProp", registry.getNumberType(), null);

 assertFalse(recordType.defineProperty("newProp", registry.getNumberType(),
 false, Node.NOP));
 }

 @Test
 public void testRecordTypeLeastSupertype() {
 RecordType recordType1 = createRecordType("name", registry.getStringType(),
 "age", registry.getNumberType());
 RecordType recordType2 = createRecordType("name", registry.getStringType(),
 "age", registry.getNumberType(), "id", registry.getNumberType());
 RecordType recordType3 = createRecordType("name", registry.getStringType(),
 "age", registry.getNumberType(), "id", registry.getInt32Type());

 assertEquals(recordType1, recordType1.getLeastSupertype(recordType1));
 assertEquals(recordType1, recordType1.getLeastSupertype(recordType2));
 assertEquals(recordType2, recordType1.getLeastSupertype(recordType3));
 }

 @Test
 public void testRecordTypeGreatestSubtype() {
 RecordType recordType1 = createRecordType("name", registry.getStringType(),
 "age", registry.getNumberType());
 RecordType recordType2 = createRecordType("name", registry.getStringType(),
 "age", registry.getNumberType(), "id", registry.getNumberType());
 RecordType recordType3 = createRecordType("name", registry.getStringType(),
 "age", registry.getNumberType());

 assertEquals(recordType1, recordType1.getGreatestSubtype(recordType1));
 assertEquals(recordType1, recordType1.getGreatestSubtype(recordType3));
 assertEquals(recordType2, recordType1.getGreatestSubtype(recordType2));
 }

 private RecordType createRecordType(String... propertyNamesAndTypes) {
 RecordTypeBuilder builder = new RecordTypeBuilder(registry);
 for (int i = 0; i < propertyNamesAndTypes.length; i += 2) {
 builder.addProperty(propertyNamesAndTypes[i],
 TypeFactory.getJSType(propertyNamesAndTypes[i + 1]), null);
 }
 return new RecordType(registry, builder.build());
 }
}