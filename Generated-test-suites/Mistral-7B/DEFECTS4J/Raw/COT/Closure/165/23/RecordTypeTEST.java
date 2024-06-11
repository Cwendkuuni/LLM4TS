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
 public void setup() {
 registry = new JSTypeRegistry();
 }

 /**
 * Test method for {@link RecordType#RecordType(JSTypeRegistry, Map)}
 */
 @Test
 public void testRecordTypeConstruction() {
 RecordProperty property1 = new RecordProperty("name", JSType.STRING);
 RecordProperty property2 = new RecordPersonProperty("age", JSType.NUMBER);
 Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
 properties.put("name", property1);
 properties.put("age", property2);

 RecordType recordType = new RecordType(registry, properties);
 assertEquals(recordType.getClass(), RecordType.class);
 }

 /**
 * Test method for {@link RecordType#isEquivalentTo(JSType)}
 */
 @Test
 public void testIsEquivalentTo() {
 RecordType recordType1 = new RecordType(registry, Map.of("name", JSType.STRING));
 RecordType recordType2 = new RecordType(registry, Map.of("name", JSType.STRING));
 RecordType recordType3 = new RecordType(registry, Map.of("age", JSType.NUMBER));
 RecordType recordType4 = new RecordType(registry, Map.of("name", JSType.STRING, "age", JSType.NUMBER));

 assertTrue(recordType1.isEquivalentTo(recordType2));
 assertFalse(recordType1.isEquivalentTo(recordType3));
 assertFalse(recordType1.isEquivalentTo(recordType4));
 }

 /**
 * Test method for {@link RecordType#getImplicitPrototype()}
 */
 @Test
 public void testGetImplicitPrototype() {
 RecordType recordType = new RecordType(registry, Map.of("name", JSType.STRING));
 assertEquals(recordType.getImplicitPrototype(), registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
 }

 /**
 * Test method for {@link RecordType#defineProperty(String, JSType, boolean, Node)}
 */
 @Test
 public void testDefineProperty() {
 RecordType recordType = new RecordType(registry, Map.of("name", JSType.STRING));

 Node nameNode = Node.newIdent("name");
 recordType.defineProperty("name", JSType.STRING, false, nameNode);

 assertEquals(recordType.getPropertyType("name"), JSType.STRING);
 }

 /**
 * Test method for {@link RecordType#getGreatestSubtypeHelper(JSType)}
 */
 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType1 = new RecordType(registry, Map.of("name", JSType.STRING));
 RecordType recordType2 = new RecordType(registry, Map.of("name", JSType.STRING, "age", JSType.NUMBER));
 RecordType recordType3 = new RecordType(registry, Map.of("name", JSType.STRING, "age", JSType.NUMBER, "city", JSType.STRING));

 JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 assertEquals(recordType2, greatestSubtype);

 greatestSubtype = recordType2.getGreatestSubtypeHelper(recordType3);
 assertEquals(recordType3, greatestSubtype);
 }

 /**
 * Test method for {@link RecordType#isSubtype(JSType, JSType)}
 */
 @Test
 public void testIsSubtype() {
 RecordType recordType1 = new RecordType(registry, Map.of("name", JSType.STRING));
 RecordType recordType2 = new RecordType(registry, Map.of("name", JSType.STRING, "age", JSType.NUMBER));
 RecordType recordType3 = new RecordType(registry, Map.of("name", JSType.STRING, "age", JSType.NUMBER, "city", JSType.STRING));
 RecordType recordType4 = new RecordType(registry, Map.of("name", JSType.STRING, "age", JSType.NUMBER, "city", JSType.STRING, "id", JSType.NUMBER));
 RecordType recordType5 = new RecordType(registry, Map.of("id", JSType.NUMBER));

 // Tests the isSubtype method with recordType1 as the base type
 assertTrue(recordType1.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
 // Testing the isSubtype method with recordType2 as the base type
 assertTrue(recordType1.isSubtype(recordType2));
 // Testing the isSubtype method with recordType3 as the base type
 // Since recordType3 has an additional property 'city' that is not present in recordType1,
 // the isSubtype method should return false.
 assertFalse(recordType1.isSubtype(recordType3));
 // Testing the isSubtype method with recordType4 as the base type
 // Since recordType4 has a property 'id' that is not present in recordType1,
 // the isSubtype method should return false.
 assertFalse(recordType1.isSubtype(recordType4));
 // Testing the isSubtype method with recordType5 as the base type
 // Since recordType5 does not have any property in common with recordType1,
 // the isSubtype method should return false.
 assertFalse(recordType1.isSubtype(recordType5));

 // Tests the isSubtype method with recordType2 as the base type
 assertTrue(recordType2.isSubtype(recordType1));
 assertTrue(recordType2.isSubtype(recordType2));
 assertTrue(recordType2.isSubtype(recordType3));
 // Testing the isSubtype method with recordType3 as the base type
 // Since recordType3 has the same properties as recordType2, and also an additional property 'city',
 // the isSubtype method should return true.
 assertTrue(recordType3.isSubtype(recordType2));
 // Testing the isSubtype method with recordType4 as the base type
 // Since recordType4 has all the properties of recordType3, and also an additional property 'id',
 // the isSubtype method should return true.
 assertTrue(recordType3.isSubtype(recordType4));
 // Testing the isSubtype method with recordType5 as the base type
 // Since recordType5 has no property in common with recordType3,
 // the isSubtype method should return false.
 assertFalse(recordType3.isSubtype(recordType5));
 }

 /**
 * Test method for {@link RecordType#toMaybeRecordType()}
 */
 @Test
 public void testToMaybeRecordType() {
 RecordType recordType = new RecordType(registry, Map.of("name", JSType.STRING));
 assertEquals(recordType, recordType.toMaybeRecordType());
 }

 /**
 * Test method for {@link RecordType#isSubtype(JSType, JSType)} with a null type.
 */
 @Test(expected = NullPointerException.class)
 public void testIsSubtypeWithNullType() {
 RecordType recordType = new RecordType(registry, Map.of("name", JSType.STRING));
 assertFalse(recordType.isSubtype(null));
 }

 /**
 * Test method for {@link RecordType#resolveInternal(ErrorReporter, StaticScope)}
 */
 @Test
 public void testResolveInternal() {
 RecordType recordType = new RecordType(registry, Map.of("name", JSType.STRING));
 JSType resolvedType = recordType.resolveInternal(new ErrorReporter() {}, new StaticScope<JSType>() {});
 assertEquals(recordType, resolvedType);
 }

 /**
 * Test method for edge cases and exceptions
 */
 @Test
 public void testEdgeCasesAndExceptions() {
 RecordType recordType = new RecordType(registry, Map.of("name", JSType.STRING));

 // Testing exception when passing a null map to the constructor
 assertThrows(IllegalArgumentException.class, () -> new RecordType(registry, null));

 // Testing exception when defining a property with null type
 RecordProperty nullProperty = new RecordProperty("age", null);
 assertThrows(IllegalArgumentException.class,
 () -> recordType.defineProperty("age", null, false, Node.NULL));

 // Testing exception when defining a property with null property node
 assertThrows(IllegalArgumentException.class,
 () -> recordType.defineProperty("age", JSType.NUMBER, false, Node.NULL));

 // Testing exception when defining a property with a name that already exists
 String existingPropertyName = "name";
 assertThrows(IllegalArgumentException.class,
 () -> recordType.defineProperty(existingPropertyName, JSType.NUMBER, false, Node.NULL));

 // Testing edge case when defining a property with the same name but different type
 String differentPropertyName = "age";
 assertFalse(recordType.hasProperty(differentPropertyName));
 RecordProperty differentProperty = new RecordProperty(differentPropertyName, JSType.BOOLEAN);
 recordType.defineProperty(differentPropertyName, JSType.BOOLEAN, false, Node.NULL);
 assertTrue(recordType.hasProperty(differentPropertyName));
 assertEquals(JSType.BOOLEAN, recordType.getPropertyType(differentPropertyName));

 // Testing edge case when merging two record types
 RecordType recordType1 = new RecordType(registry, Map.of("name", JSType.STRING));
 RecordType recordType2 = new RecordType(registry, Map.of("age", JSType.NUMBER));
 RecordType mergedType = new RecordType(registry, Map.of("name", JSType.STRING, "age", JSType.NUMBER));
 assertEquals(mergedType, recordType1.getGreatestSubtypeHelper(recordType2));
 }

 /**
 * Test method for {@link RecordType#isSubtype(JSType, JSType)} with a circular reference
 */
 @Test
 public void testIsSubtypeWithCircularReference() {
 RecordProperty recordProp1 = new RecordProperty("a", JSType.ANY);
 RecordProperty recordProp2 = new RecordProperty("b", new RecordTypeBuilder(registry).build(Map.of("a", recordProp1)));
 Map<String, RecordProperty> properties = Map.of("a", recordProp1, "b", recordProp2);
 RecordType circularRecordType = new RecordType(registry, properties);

 RecordType baseType = new RecordType(registry, Map.of("b", circularRecordType));
 assertFalse(circularRecordType.isSubtype(baseType));
 }

 /**
 * Test method for {@link RecordType#isSubtype(JSType, JSType)} with UnionTypes
 */
 @Test
 public void testIsSubtypeWithUnionTypes() {
 RecordType recordType1 = new RecordType(registry, Map.of("name", JSType.STRING));
 RecordType recordType2 = new RecordType(registry, Map.of("name", JSType.STRING, "age", JSType.NUMBER));
 RecordType recordType3 = new RecordType(registry, Map.of("name", JSType.STRING, "age", JSType.NUMBER, "city", JSType.STRING));

 UnionTypeBuilder unionBuilder = new UnionTypeBuilder(registry);
 unionBuilder.addAlternate(recordType2);
 unionBuilder.addAlternate(recordType3);
 JSType unionType = unionBuilder.build();

 RecordType recordType4 = new RecordType(registry, Map.of("name", JSType.STRING, "age", JSType.NUMBER, "city", JSType.STRING, "id", JSType.NUMBER));

 // Testing the isSubtype method with recordType1 as the base type
 assertTrue(recordType1.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
 // Testing the isSubtype method with unionType as the base type
 assertTrue(recordType1.isSubtype(unionType));
 // Testing the isSubtype method with recordType2 as the base type
 assertTrue(recordType1.isSubtype(recordType2));
 // Testing the isSubtype method with recordType3 as the base type
 assertTrue(recordType1.isSubtype(recordType3));
 // Testing the isSubtype method with recordType4 as the base type
 // Since recordType4 has all the properties of recordType3, and also an additional property 'id',
 // the isSubtype method should return true.
 assertTrue(recordType3.isSubtype(recordType4));
 // Testing the isSubtype method with recordType4 as the base type
 // Since recordType1 has all the properties of recordType4, but recordType4 also has an additional property 'id',
 // the isSubtype method should return false.
 assertFalse(recordType4.isSubtype(recordType1));
 }
}