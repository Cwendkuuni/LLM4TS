import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @BeforeClass
 public static void setup() {
 registry = new JSTypeRegistry();
 }

 @Before
 public void setupTest() {
 RecordProperty recordProperty1 = new RecordProperty("property1", int.class);
 RecordProperty recordProperty2 = new RecordProperty("property2", float.class);
 Map<String, RecordProperty> map = Maps.newHashMap();
 map.put("property1", recordProperty1);
 map.put("property2", recordProperty2);
 recordType = new RecordType(registry, map);
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType recordType1 = new RecordType(registry, map);
 RecordType recordType2 = new RecordType(registry, map);
 assertTrue(recordType1.isEquivalentTo(recordType2));
 }

 @Test
 public void testIsNotEquivalentToDifferentProperties() {
 RecordType recordType1 = new RecordType(registry, map);
 Map<String, RecordProperty> map2 = Maps.newHashMap();
 map2.put("property1", recordProperty1);
 map2.put("property3", new RecordProperty("property3", boolean.class));
 RecordType recordType2 = new RecordType(registry, map2);
 assertFalse(recordType1.isEquivalentTo(recordType2));
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType1 = new RecordType(registry, map);
 RecordType recordType2 = new RecordType(registry, Map.of("property1", int.class));
 assertTrue(recordType2.isSubtype(recordType1));

 RecordType recordType3 = new RecordType(registry, Map.of("property1", int.class, "property2", double.class));
 assertFalse(recordType3.isSubtype(recordType1));
 }

 @Test
 public void testDefineProperty() {
 assertFalse(recordType.hasProperty("nonExistentProperty"));
 recordType.defineProperty("nonExistentProperty", int.class, false, null);
 assertTrue(recordType.hasProperty("nonExistentProperty"));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefineNullProperty() {
 recordType.defineProperty("property1", null, false, null);
 fail("IllegalStateException expected!");
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSType type1 = registry.getIntType();
 JSType type2 = registry.getIntType();
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(type1);
 assertEquals(type1, greatestSubtype);
 greatestSubtype = recordType.getGreatestSubtypeHelper(type2);
 assertEquals(type2, greatestSubtype);

 JSType type3 = registry.getNativeType(JSTypeNative.OBJECT_TYPE);
 greatestSubtype = recordType.getGreatestSubtypeHelper(type3);
 assertEquals(recordType, greatestSubtype);
 }

 @Test
 public void testGetGreatestSubtypeHelperDifferentRecordTypes() {
 RecordType recordType1 = new RecordType(registry, map);
 Map<String, RecordProperty> map2 = Maps.newHashMap();
 map2.put("property1", recordProperty1);
 map2.put("property3", new RecordProperty("property3", boolean.class));
 RecordType recordType2 = new RecordType(registry, map2);
 JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 assertEquals(recordType1, greatestSubtype);
 }

 @Test
 public void testGetGreatestSubtypeHelperNull() {
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(null);
 assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), greatestSubtype);
 }

 @Test
 public void testGetGreatestSubtypeHelperNonRecordType() {
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(registry.getNativeType(JSTypeNative.STRING_TYPE));
 assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), greatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType1 = new RecordType(registry, map);
 RecordType recordType2 = new RecordType(registry, Map.of("property1", int.class));
 assertTrue(recordType2.isSubtype(recordType1));

 RecordType recordType3 = new RecordType(registry, Map.of("property1", int.class, "property2", double.class));
 assertFalse(recordType3.isSubtype(recordType1));
 }

 @Test
 public void testToMaybeRecordType() {
 RecordType recordType = new RecordType(registry, map);
 assertEquals(recordType, recordType.toMaybeRecordType());
 }

 @Test
 public void testResolveInternal() {
 JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
 RecordType recordType = new RecordType(registry, map);
 Node propertyNode = Node.newString("property1");
 assertEquals(registry.getNativeType(JSTypeNative.ANY), recordType.getPropertyType("property1"));
 recordType.defineProperty("property1", int.class, false, propertyNode);
 recordType.resolveInternal(null, null);
 assertEquals(int.class, recordType.getPropertyType("property1"));
 }
}