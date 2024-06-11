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

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 }

 /**
 * Test constructor.
 */
 @Test
 public void testRecordTypeConstructor() {
 // Given a map of properties
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", new RecordProperty("a", JSType.getIntType()));
 properties.put("b", new RecordProperty("b", JSType.getStringType()));

 // When a new RecordType is created
 RecordType recordType = new RecordType(registry, properties);

 // Then the RecordType has the expected properties
 assertEquals(2, recordType.getDeclaredPropertyNames().length);
 assertEquals("a", recordType.getDeclaredPropertyNames()[0]);
 assertEquals("b", recordType.getDeclaredPropertyNames()[1]);

 // And the properties have the expected types
 assertEquals(JSType.getIntType(), recordType.getPropertyType("a"));
 assertEquals(JSType.getStringType(), recordType.getPropertyType("b"));
 }

 /**
 * Test isEquivalentTo method.
 */
 @Test
 public void testIsEquivalentTo() {
 RecordType recordType1 = createRecordType("a", JSType.getIntType());
 RecordType recordType2 = createRecordType("a", JSType.getIntType());
 RecordType recordType3 = createRecordType("b", JSType.getStringType());

 // Given two RecordTypes with the same properties
 assertTrue(recordType1.isEquivalentTo(recordType2));

 // And two RecordTypes with different properties
 assertFalse(recordType1.isEquivalentTo(recordType3));

 // And a RecordType and a non-RecordType
 assertFalse(recordType1.isEquivalentTo(JSType.getIntType()));
 }

 /**
 * Test getImplicitPrototype method.
 */
 @Test
 public void testGetImplicitPrototype() {
 RecordType recordType = createRecordType("a", JSType.getIntType());

 // When getImplicitPrototype is called
 ObjectType implicitPrototype = recordType.getImplicitPrototype();

 // Then the implicit prototype is the native Object type
 assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype.getTypeKey());
 }

 /**
 * Test defineProperty method.
 */
 @Test
 public void testDefineProperty() {
 RecordType recordType = createRecordType("a", JSType.getIntType());

 // Given a RecordType with a single property "a" of type Int32
 // When a new property "c" of type String is defined
 recordType.defineProperty("c", JSType.getStringType(), false, null);

 // Then the RecordType has three properties: "a", "b" (default), and "c"
 assertEquals(3, recordType.getDeclaredPropertyNames().length);

 // And the new property has the expected type
 assertEquals(JSType.getStringType(), recordType.getPropertyType("c"));
 }

 /**
 * Test getGreatestSubtypeHelper method.
 */
 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType1 = createRecordType("a", JSType.getIntType());
 RecordType recordType2 = createRecordType("a", JSType.getIntType(), "x", JSType.getBooleanType());
 RecordType recordType3 = createRecordType("a", JSType.getIntType(), "x", JSType.getBooleanType(), "b", JSType.getStringType());

 UnionType recordType3Union = new UnionType(registry);
 recordType3Union.addAlternate(JSType.getNullType());
 recordType3Union.addAlternate(JSType.getBooleanType());
 recordType3Union.addAlternate(recordType3);

 // Given three RecordTypes with overlapping properties
 // When the greatest subtype of recordType1 and recordType2 is computed
 JSType greatestSubtype12 = recordType1.getGreatestSubtypeHelper(recordType2);

 // Then the greatest subtype of recordType1 and recordType2 is recordType3
 assertEquals(recordType3, greatestSubtype12.toMaybeRecordType());

 // And the greatest subtype of recordType1 and recordType2, then recordType3 and recordType3Union
 JSType greatestSubtype123 = recordType1.getGreatestSubtypeHelper(recordType3Union);
 assertEquals(recordType3, greatestSubtype123.toMaybeRecordType());
 }

 /**
 * Test isSubtype method.
 */
 @Test
 public void testIsSubtype() {
 RecordType recordType1 = createRecordType("a", JSType.getIntType());
 RecordType recordType2 = createRecordType("a", JSType.getIntType());
 RecordType recordType3 = createRecordType("b", JSType.getStringType());
 RecordType recordType4 = createRecordType("a", JSType.getNullType());
 RecordType recordType5 = createRecordType("a", JSType.getBooleanType());
 RecordType recordType6 = createRecordType("a", JSType.getIntType(), "b", JSType.getStringType());

 // Given five RecordTypes
 // When their isSubtype relationships are computed
 assertTrue(recordType1.isSubtype(recordType2));
 assertTrue(recordType2.isSubtype(recordType1));
 assertFalse(recordType1.isSubtype(recordType3));
 assertFalse(recordType3.isSubtype(recordType1));
 assertFalse(recordType1.isSubtype(recordType4));
 assertFalse(recordType4.isSubtype(recordType1));
 assertFalse(recordType1.isSubtype(recordType5));
 assertFalse(recordType5.isSubtype(recordType1));
 assertTrue(recordType6.isSubtype(recordType1));
 assertFalse(recordType1.isSubtype(recordType6));
 }

 /**
 * Test toMaybeRecordType method.
 */
 @Test
 public void testToMaybeRecordType() {
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType2 = recordType1.toMaybeRecordType();
 assertEquals(recordType1, recordType2);
 }

 /**
 * Helper method to create a RecordType with the given property names and types.
 */
 private RecordType createRecordType(String... propertyNamesAndTypes) {
 Map<String, RecordProperty> properties = Maps.newHashMap();
 for (String propertyNameAndType : propertyNamesAndTypes) {
 String propertyName = propertyNameAndType.substring(0, propertyNameAndType.indexOf(' '));
 JSType propertyType = JSType.fromString(propertyNameAndType.substring(propertyNameAndType.indexOf(' ') + 1));
 properties.put(propertyName, new RecordProperty(propertyName, propertyType));
 }
 return new RecordType(registry, properties);
 }
}