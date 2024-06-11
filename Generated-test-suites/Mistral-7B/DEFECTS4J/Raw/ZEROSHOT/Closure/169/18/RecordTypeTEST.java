import static org.junit.Assert.*;
import static com.google.javascript.rhino.testing.TypeCheckMode.DONT_CHECK;
import static com.google.javascript.rhino.testing.TypeCheckMode.STRICT;
import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.testing.TestContext;
import com.google.javascript.rhino.testing.TypedEval;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;
 private RecordType recordTypeWithExtraProperty;
 private RecordType recordTypeWithSubType;

 @BeforeClass
 public static void setupClass() {
 registry = new JSTypeRegistry();
 }

 @Before
 public void setup() {
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("name", new RecordProperty("name", registry.getStringType()));
 properties.put("age", new RecordProperty("age", registry.getNumberType()));
 recordType = new RecordType(registry, properties);

 Map<String, RecordProperty> propertiesWithExtraProperty =
 Maps.newHashMap(properties);
 propertiesWithExtraProperty.put("address",
 new RecordProperty("address", registry.getStringType()));
 recordTypeWithExtraProperty = new RecordType(registry, propertiesWithExtraProperty, false);

 Map<String, RecordProperty> propertiesWithSubType =
 Maps.newHashMap(properties);
 propertiesWithSubType.put("animal",
 new RecordProperty("animal", registry.getSubtypeOf(registry.getArrayType(registry.getNativeType(JSTypeNative.STRING_TYPE)), 1)));
 recordTypeWithSubType = new RecordType(registry, propertiesWithSubType, false);
 }

 @Test
 public void testGetPrettyPrint() {
 assertEquals("{name: string, age: number}", recordType.getPrettyPrint());
 }

 @Test
 public void testIsSynthetic() {
 assertTrue(recordTypeWithExtraProperty.isSynthetic());
 assertFalse(recordType.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 assertTrue(recordType.checkRecordEquivalenceHelper(recordType, true));
 assertFalse(recordType.checkRecordEquivalenceHelper(recordTypeWithExtraProperty, true));
 }

 @Test
 public void testIsSubtype() {
 // Record type is a subtype of itself
 assertTrue(recordType.isSubtype(recordType));

 // Record type is a subtype of a record type with the same properties
 Map<String, RecordProperty> propertiesSubset = Maps.newHashMap();
 propertiesSubset.putAll(recordType.getProperties());
 RecordType recordTypeSubset = new RecordType(registry, propertiesSubset, false);
 assertTrue(recordType.isSubtype(recordTypeSubset));

 // Record type with extra property is not a subtype of record type
 assertFalse(recordType.isSubtype(recordTypeWithExtraProperty));

 // Record type is a subtype of a record type with subtype
 Map<String, RecordProperty> propertiesSubtype = Maps.newHashMap(recordType.getProperties());
 propertiesSubtype.put("animal", new RecordProperty("animal", registry.getNativeType(JSTypeNative.ARRAY_TYPE).getSubtype(registry.getNativeType(JSTypeNative.STRING_TYPE), 1)));
 RecordType recordTypeSubtype = new RecordType(registry, propertiesSubtype, false);
 assertTrue(recordType.isSubtype(recordTypeSubtype));

 // Record type with subtype is a subtype of record type
 assertTrue(recordTypeSubtype.isSubtype(recordType));

 // Record type is not a subtype of a record type with different property types
 Map<String, RecordProperty> propertiesDifferent = Maps.newHashMap(recordType.getProperties());
 propertiesDifferent.put("name", new RecordProperty("name", registry.getNumberType()));
 RecordType recordTypeDifferent = new RecordType(registry, propertiesDifferent, false);
 assertFalse(recordType.isSubtype(recordTypeDifferent));
 }

 @Test
 public void testGetImplicitPrototype() {
 ObjectType implicitPrototype = recordType.getImplicitPrototype();
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
 }

 @Test
 public void testDefineProperty() {
 // Record cannot be frozen
 assertFalse(recordType.isFrozen());

 // Property can be added to record
 Node propertyNode = new Node.Name("newProperty");
 RecordProperty recordProperty = new RecordProperty("newProperty", registry.getBooleanType());
 recordType.defineProperty("newProperty", registry.getBooleanType(), false, propertyNode);
 assertTrue(recordType.hasProperty("newProperty"));

 // Property cannot be added to frozen record
 recordType.freeze();
 assertFalse(recordType.defineProperty("newProperty", registry.getBooleanType(), false, propertyNode));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 // Record type is a subtype of itself
 JSType recordTypeSelf = recordType.getGreatestSubtypeHelper(recordType);
 assertEquals(recordType, recordTypeSelf);

 // Record type is a subtype of record type with subtype
 JSType recordTypeSubtypeSelf = recordTypeSubtype.getGreatestSubtypeHelper(recordTypeSubtype);
 assertEquals(recordTypeSubtype, recordTypeSubtypeSelf);

 // Record type is not a subtype of a different record type
 JSType recordTypeDifferentSelf = recordType.getGreatestSubtypeHelper(recordTypeDifferent);
 assertEquals(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE), recordTypeDifferentSelf);

 // Record type with subtype is a subtype of a record type with a different property type
 JSType recordTypeSubtypeDifferent = new RecordType(registry, propertiesSubtype, false);
 JSType recordTypeDifferentSubtype = new RecordType(registry, properties, false);
 JSType recordTypeSubtypeDifferentSelf = recordTypeSubtypeDifferent.getGreatestSubtypeHelper(recordTypeDifferentSubtype);
 assertEquals(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE), recordTypeSubtypeDifferentSelf);

 // Record type is a subtype of a record type with extra property
 JSType recordTypeExtraSelf = recordType.getGreatestSubtypeHelper(recordTypeWithExtraProperty);
 assertEquals(recordType, recordTypeExtraSelf);
 }

 @Test
 public void testToMaybeRecordType() {
 assertEquals(recordType, recordType.toMaybeRecordType());
 }

 @Test
 public void testIsRecordType() {
 assertTrue(recordType.isRecordType());
 assertFalse(registry.getBooleanType().isRecordType());
 }

 @Test
 public void testIsSubtype() {
 // Record type is a subtype of itself
 assertTrue(recordType.isSubtype(recordType));

 // Record type is a subtype of a record type with the same properties
 RecordType recordTypeSubset = new RecordType(registry, propertiesSubset, false);
 assertTrue(recordType.isSubtype(recordTypeSubset));

 // Record type is a subtype of a record type with a subtype
 RecordType recordTypeSubtype = new RecordType(registry, propertiesSubtype, false);
 assertTrue(recordType.isSubtype(recordTypeSubtype));

 // Record type with subtype is a subtype of record type
 assertTrue(recordTypeSubtype.isSubtype(recordType));

 // Record type is a subtype of a record type with different property types
 RecordType recordTypeDifferent = new RecordType(registry, propertiesDifferent, false);
 assertFalse(recordType.isSubtype(recordTypeDifferent));

 // Record type is not a subtype of a different type
 JSType differentType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
 assertFalse(recordType.isSubtype(differentType));
 }

 @Test
 public void testGetProperties() {
 assertEquals(2, recordType.getProperties().size());
 }

 @Test
 public void testGetPropertyType() {
 assertEquals(registry.getStringType(), recordType.getPropertyType("name"));
 assertEquals(registry.getNumberType(), recordType.getPropertyType("age"));
 }

 @Test
 public void testGetPropertyNode() {
 Node nameNode = new Node.Name("name");
 Node ageNode = new Node.Name("age");
 assertEquals(nameNode, recordType.getPropertyNode("name"));
 assertEquals(ageNode, recordType.getPropertyNode("age"));
 }

 @Test
 public void testFreeze() {
 // Record can be frozen
 recordType.freeze();
 assertTrue(recordType.isFrozen());

 // Property cannot be added to frozen record
 Node newPropertyNode = new Node.Name("newProperty");
 RecordProperty newProperty = new RecordProperty("newProperty", registry.getBooleanType());
 assertFalse(recordType.defineProperty("newProperty", registry.getBooleanType(), false, newPropertyNode));

 // Property type cannot be changed on frozen record
 RecordProperty nameProperty = new RecordProperty("name", registry.getStringType());
 JSType newNameType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
 assertFalse(recordType.defineProperty("name", newNameType, false, nameProperty.getPropertyNode()));
 }

 @Test
 public void testEquals() {
 RecordType recordTypeCopy = new RecordType(registry, properties, false);
 assertEquals(recordType, recordTypeCopy);
 }

 @Test
 public void testHashCode() {
 int firstHashCode = recordType.hashCode();
 RecordType recordTypeCopy = new RecordType(registry, properties, false);
 assertEquals(firstHashCode, recordTypeCopy.hashCode());
 }

 @Test
 public void testToString() {
 assertEquals("RecordType{name=string, age=number}", recordType.toString());
 }

 @Test
 public void testToSource() {
 TypedEval eval = new TypedEval();
 TestContext context = eval.createContext(STRICT);
 eval.setTypeCheckMode(DONT_CHECK);
 context.setCurrentScope(context.getGlobalScope());
 context.setCurrentRecordType(recordType);

 String recordTypeSource = "{name: string, age: number}";
 String result = eval.runReturnString("recordTypeSource = '"+recordTypeSource+"'", context);
 assertEquals(recordTypeSource, result);
 }
}