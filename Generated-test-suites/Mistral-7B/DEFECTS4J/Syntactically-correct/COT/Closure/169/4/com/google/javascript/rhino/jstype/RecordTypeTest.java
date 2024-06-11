import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import static com.google.javascript.rhino.jstype.RecordType.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.JSTypeRegistry;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private ErrorReporter errorReporter;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 errorReporter = new ErrorReporter() {};
 }

 @Test
 public void testRecordTypeConstructor() {
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("name", new RecordProperty("name", StringType.getInstance()));
 properties.put("age", new RecordProperty("age", NumberType.getInstance()));
 RecordType recordType = new RecordType(registry, properties, true);

 assertEquals(RecordType.class, recordType.getClass());
 assertEquals(2, recordType.properties.size());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 RecordProperty p1 = new RecordProperty("p1", NumberType.getInstance());
 RecordProperty p2 = new RecordProperty("p2", NumberType.getInstance());
 RecordType rt1 = new RecordType(registry, Maps.newHashMap(), true);
 rt1.defineProperty("p1", p1.getType(), false, null);
 rt1.defineProperty("p2", p2.getType(), false, null);
 RecordType rt2 = new RecordType(registry, Maps.newHashMap(), true);
 rt2.defineProperty("p1", p1.getType(), false, null);
 rt2.defineProperty("p2", p2.getType(), false, null);

 assertTrue(rt1.checkRecordEquivalenceHelper(rt2, false));
 }

 @Test
 public void testCheckRecordEquivalenceHelper_false() {
 RecordProperty p1 = new RecordProperty("p1", NumberType.getInstance());
 RecordProperty p2 = new RecordProperty("p2", StringType.getInstance());
 RecordType rt1 = new RecordType(registry, Maps.newHashMap(), true);
 rt1.defineProperty("p1", p1.getType(), false, null);
 rt1.defineProperty("p2", p2.getType(), false, null);
 RecordType rt2 = new RecordType(registry, Maps.newHashMap(), true);
 rt2.defineProperty("p1", p1.getType(), false, null);
 rt2.defineProperty("p2", new RecordProperty("p2", BooleanType.getInstance()),
     false, null);

 assertFalse(rt1.checkRecordEquivalenceHelper(rt2, false));
 }

 @Test
 public void testCheckRecordEquivalenceHelper_tolerateUnknowns() {
 RecordProperty p1 = new RecordProperty("p1", NumberType.getInstance());
 RecordProperty p2 = new RecordProperty("p2", StringType.getInstance());
 RecordType rt1 = new RecordType(registry, Maps.newHashMap(), true);
 rt1.defineProperty("p1", p1.getType(), false, null);
 RecordType rt2 = new RecordType(registry, Maps.newHashMap(), true);
 rt2.defineProperty("p2", new RecordProperty("p2", BooleanType.getInstance()),
     false, null);

 assertTrue(rt1.checkRecordEquivalenceHelper(rt2, true));
 }

 @Test
 public void testIsSynthetic() {
 RecordProperty p1 = new RecordProperty("p1", NumberType.getInstance());
 RecordProperty p2 = new RecordProperty("p2", NumberType.getInstance());
 RecordType rt1 = new RecordType(registry, Maps.newHashMap(), true);
 rt1.defineProperty("p1", p1.getType(), false, null);
 RecordType rt2 = new RecordType(registry, Maps.newHashMap(), false);
 rt2.defineProperty("p1", p2.getType(), false, null);

 assertTrue(rt1.isDeclared());
 assertFalse(rt2.isDeclared());
 }

 @Test(expected = IllegalStateException.class)
 public void testNullRecordProperty() {
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("name", null);
 RecordType recordType = new RecordType(registry, properties, true);
 }

 @Test
 public void testGreatestSubtypeHelper() {
 RecordType typeA = new RecordType(registry, Maps.newHashMap(), true);
 typeA.defineProperty("p1", NumberType.getInstance(), false, null);
 typeA.defineProperty("p2", StringType.getInstance(), false, null);
 RecordType typeB = new RecordType(registry, Maps.newHashMap(), true);
 typeB.defineProperty("p1", NumberType.getInstance(), false, null);
 typeB.defineProperty("p2", StringType.getInstance(), false, null);
 RecordType typeC = new RecordType(registry, Maps.newHashMap(), true);
 typeC.defineProperty("p1", NumberType.getInstance(), false, null);
 typeC.defineProperty("p3", StringType.getInstance(), false, null);

 RecordType greatestSubtype = typeA.getGreatestSubtypeHelper(typeB);
 assertEquals(typeB, greatestSubtype);

 greatestSubtype = typeA.getGreatestSubtypeHelper(typeC);
 assertEquals(typeA, greatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 ObjectType typeA = registry.getNativeObjectType(ObjectType.OBJECT_TYPE);
 RecordType recordType = new RecordType(registry, Maps.newHashMap(), true);
 recordType.defineProperty("p1", NumberType.getInstance(), false, null);

 // Object is a supertype of RecordType
 assertTrue(typeA.isSubtype(recordType));

 // RecordType is a supertype of RecordType
 assertTrue(recordType.isSubtype(recordType));

 // RecordType with different properties is not a subtype of RecordType
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap(), true);
 recordType2.defineProperty("p2", StringType.getInstance(), false, null);
 assertFalse(recordType.isSubtype(recordType2));

 // RecordType with same properties but different types is not a subtype of RecordType
 recordType.defineProperty("p1", BooleanType.getInstance(), false, null);
 assertFalse(recordType.isSubtype(recordType));
 }

 @Test
 public void testIsSubtype_RecordType_RecordTypeBuilder() {
 RecordType recordType = new RecordType(registry, Maps.newHashMap(), true);
 recordType.defineProperty("p1", NumberType.getInstance(), false, null);

 RecordTypeBuilder builder = new RecordTypeBuilder(registry);
 builder.addProperty("p1", NumberType.getInstance());
 RecordType recordTypeBuilder = builder.build();

 assertTrue(recordType.isSubtype(recordTypeBuilder));
 }
}