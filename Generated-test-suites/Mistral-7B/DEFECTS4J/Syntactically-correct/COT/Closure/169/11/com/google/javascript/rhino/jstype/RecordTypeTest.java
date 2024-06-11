import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;
 private RecordType recordTypeWithExtraProperty;
 private RecordType recordTypeWithSubsetProperties;
 private RecordType recordTypeWithIncompatibleProperty;
 private RecordType recordTypeWithUnknownProperty;
 private RecordType recordTypeWithNullProperty;
 private RecordType recordTypeWithEmptyProperties;

 @Before
 public void setup() {
 registry = new JSTypeRegistry();

 RecordProperty recordProperty1 = new RecordProperty("prop1", int.class);
 RecordProperty recordProperty2 = new RecordProperty("prop2", String.class);
 RecordProperty recordProperty3 = new RecordProperty("prop3", double.class);

 Map<String, RecordProperty> properties1 = Maps.newLinkedHashMap();
 properties1.put("prop1", recordProperty1);
 properties1.put("prop2", recordProperty2);
 recordType = new RecordType(registry, properties1, true);

 Map<String, RecordProperty> properties2 = Maps.newLinkedHashMap();
 properties2.putAll(properties1);
 properties2.put("prop3", recordProperty3);
 recordTypeWithExtraProperty = new RecordType(registry, properties2, true);

 Map<String, RecordProperty> properties3 = Maps.newLinkedHashMap();
 properties3.put("prop1", recordProperty1);
 recordTypeWithSubsetProperties = new RecordType(registry, properties3, true);

 Map<String, RecordProperty> properties4 = Maps.newLinkedHashMap();
 properties4.put("prop1", recordProperty1);
 properties4.put("prop2", recordProperty2);
 properties4.put("prop3", int.class);
 recordTypeWithIncompatibleProperty = new RecordType(registry, properties4, true);

 Map<String, RecordProperty> properties5 = Maps.newLinkedHashMap();
 recordTypeWithUnknownProperty = new RecordType(registry, properties5, true);

 Map<String, RecordProperty> properties6 = Maps.newLinkedHashMap();
 recordTypeWithNullProperty = new RecordType(registry, properties6, true);
 recordTypeWithNullProperty.setProperty("prop1", int.class, null);

 Map<String, RecordProperty> properties7 = Maps.newLinkedHashMap();
 recordTypeWithEmptyProperties = new RecordType(registry, properties7, true);
 }

 @Test
 public void testRecordTypeHasProperties() {
 String[] expectedProperties = {"prop1", "prop2", "prop3"};
 String[] actualProperties = new String[recordType.getProperties().size()];
 int i = 0;
 for (String property : recordType.getProperties().keySet()) {
 actualProperties[i++] = property;
 }
 assertArrayEquals(expectedProperties, actualProperties);
 }

 @Test
 public void testRecordTypeHasExtraProperty() {
 assertFalse(recordType.hasProperty("prop3"));
 assertTrue(recordTypeWithExtraProperty.hasProperty("prop3"));
 }

 @Test
 public void testRecordTypeWithSubsetPropertiesIsSubtype() {
 assertTrue(recordTypeWithSubsetProperties.isSubtype(recordType));
 }

 @Test
 public void testRecordTypeWithIncompatiblePropertyIsNotSubtype() {
 assertFalse(recordType.isSubtype(recordTypeWithIncompatibleProperty));
 }

 @Test
 public void testRecordTypeWithUnknownPropertyIsSubtype() {
 assertTrue(recordType.isSubtype(recordTypeWithUnknownProperty));
 }

 @Test
 public void testRecordTypeWithNullPropertyIsNotSubtype() {
 assertFalse(recordType.isSubtype(recordTypeWithNullProperty));
 }

 @Test
 public void testRecordTypeWithEmptyPropertiesIsSubtype() {
 assertTrue(recordType.isSubtype(recordTypeWithEmptyProperties));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyOnFrozenRecordType() {
 recordType.defineProperty("prop3", int.class, false, null);
 fail("Should throw IllegalStateException");
 }

 @Test
 public void testGetGreatestSubtypeWithEmptyObjectType() {
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(
 JSType.ObjectType);
 assertEquals(JSType.ObjectType.getGreatestSubtype(recordType.getRuntimeType()),
 greatestSubtype);
 }

 @Test
 public void testGetGreatestSubtypeWithRecordType() {
 RecordType otherRecordType = new RecordType(registry, properties1, false);
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(otherRecordType);

 UnionTypeBuilder builder = new UnionTypeBuilder(registry);
 for (ObjectType alt : registry.getEachReferenceTypeWithProperty("prop1")) {
 builder.addAlternate(alt);
 }
 UnionType expectedSubtype = builder.build();
 assertEquals(expectedSubtype.getLeastSupertype(JSType.ObjectType),
 greatestSubtype);
 }

 @Test
 public void testGetGreatestSubtypeWithUnknownType() {
 RecordType otherRecordType = new RecordType(registry, Maps.newLinkedHashMap(), false);
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(otherRecordType);
 // Use a non-empty record type to ensure that getGreatestSubtype returns
 // the native object type for unknown types.
 assertEquals(JSType.ObjectType.getGreatestSubtype(recordType.getRuntimeType()),
 greatestSubtype);
 }
}