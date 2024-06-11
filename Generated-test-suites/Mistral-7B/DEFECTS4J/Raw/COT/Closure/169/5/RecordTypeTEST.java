import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.SortedMap;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @Before
 public void setup() {
 registry = new JSTypeRegistry();
 RecordProperty recordProperty = new RecordProperty("name", String.class);
 Map<String, RecordProperty> recordProperties = Maps.newHashMap();
 recordProperties.put("name", recordProperty);
 recordType = new RecordType(registry, recordProperties, true);
 }

 @Test
 public void testRecordTypeConstruction() {
 assertNotNull(recordType);
 assertEquals(recordType.getClass(), RecordType.class);
 assertEquals(recordType.getKind(), JSType.Kind.OBJECT);
 }

 @Test
 public void testGetImplicitPrototype() {
 ObjectType implicitPrototype = recordType.getImplicitPrototype();
 assertNotNull(implicitPrototype);
 assertEquals(implicitPrototype.getClass(), ObjectType.class);
 }

 @Test
 public void testIsSynthetic() {
 assertFalse(recordType.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 RecordType recordType1 = new RecordType(registry, recordProperties, true);
 RecordType recordType2 = new RecordType(registry, recordProperties, true);
 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));
 }

 @Test(expected = IllegalStateException.class)
 public void testRecordEquivalenceHelperWithNullRecordProperty() {
 RecordType recordType1 = new RecordType(registry, null, true);
 RecordType recordType2 = new RecordType(registry, recordProperties, true);
 }

 @Test
 public void testDefineProperty() {
 assertFalse(recordType.defineProperty("name2", Integer.class, false, null));
 assertTrue(recordType.hasProperty("name"));
 assertFalse(recordType.hasProperty("name2"));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType1 = new RecordType(registry, recordProperties, true);
 RecordType recordType2 = new RecordType(registry, recordProperties, true);
 RecordType recordType3 = new RecordType(registry, Maps.newHashMap(), true);

 JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 assertEquals(recordType1.getClass(), greatestSubtype.getClass());

 greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType3);
 assertEquals(recordType1.getClass(), greatestSubtype.getClass());
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType1 = new RecordType(registry, recordProperties, true);
 RecordType recordType2 = new RecordType(registry, recordProperties, true);
 RecordType recordType3 = new RecordType(registry, Maps.newHashMap(), true);
 RecordType recordType4 = new RecordType(registry, recordProperties, false);

 assertTrue(recordType1.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(recordType2));
 assertTrue(recordType1.isSubtype(recordType3));
 assertFalse(recordType1.isSubtype(recordType4));
 assertFalse(recordType3.isSubtype(recordType1));
 assertFalse(recordType3.isSubtype(recordType4));
 }

 @Test
 public void testIsSubtypeWithRecordTypeWithSamePropertiesButDifferentTypes() {
 RecordType recordType1 = new RecordType(registry, recordProperties, true);
 Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
 recordProperties2.putAll(recordProperties);
 recordProperties2.put("name", new RecordProperty("name", Integer.class));
 RecordType recordType2 = new RecordType(registry, recordProperties2, true);
 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithRecordTypeWithDifferentProperties() {
 RecordType recordType1 = new RecordType(registry, recordProperties, true);
 Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
 recordProperties2.put("name", new RecordProperty("name", Integer.class));
 recordProperties2.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType2 = new RecordType(registry, recordProperties2, true);
 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithRecordTypeWithSuperTypeRecordType() {
 RecordType recordType1 = new RecordType(registry, recordProperties, true);
 RecordType recordType2 = new RecordType(registry, recordProperties, false);
 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithRecordTypeWithSuperTypeObjectType() {
 RecordType recordType = new RecordType(registry, recordProperties, true);
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 assertTrue(recordType.isSubtype(objectType));
 }

 @Test
 public void testIsSubtypeWithRecordTypeWithSuperTypeUnionType() {
 RecordType recordType = new RecordType(registry, recordProperties, true);
 UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
 unionTypeBuilder.addAlternate(registry.getNativeObjectType(JSTypeNative.NUMBER_TYPE));
 JSType unionType = unionTypeBuilder.build();
 assertTrue(recordType.isSubtype(unionType));
 }

 @Test
 public void testIsSubtypeWithRecordTypeWithIncompatiblePropertyType() {
 RecordType recordType1 = new RecordType(registry, recordProperties, true);
 Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
 recordProperties2.put("name", new RecordProperty("name", Long.class));
 RecordType recordType2 = new RecordType(registry, recordProperties2, true);
 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithRecordTypeWithMissingProperty() {
 RecordType recordType1 = new RecordType(registry, recordProperties, true);
 Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
 recordProperties2.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType2 = new RecordType(registry, recordProperties2, true);
 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithRecordTypeWithUnknownProperty() {
 RecordType recordType1 = new RecordType(registry, recordProperties, true);
 Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
 recordProperties2.put("age", new RecordProperty("age", JSType.UNKNOWN));
 RecordType recordType2 = new RecordType(registry, recordProperties2, true);
 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithRecordTypeWithMixedProperties() {
 RecordType recordType1 = new RecordType(registry, recordProperties, true);
 Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
 recordProperties2.put("name", new RecordProperty("name", String.class));
 recordProperties2.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType2 = new RecordType(registry, recordProperties2, true);
 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithRecordTypeWithUnknownPropertyAndMixedProperties() {
 RecordType recordType1 = new RecordType(registry, recordProperties, true);
 Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
 recordProperties2.put("name", new RecordProperty("name", String.class));
 recordProperties2.put("age", new RecordProperty("age", JSType.UNKNOWN));
 RecordType recordType2 = new RecordType(registry, recordProperties2, true);
 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithRecordTypeWithInvariantPropertyAndUnknownProperty() {
 RecordType recordType1 = new RecordType(registry, recordProperties, true);
 RecordProperty recordProperty = recordProperties.get("name");
 recordProperty.setInvariant(true);
 Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
 recordProperties2.put("name", new RecordProperty("name", JSType.UNKNOWN));
 recordProperties2.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType2 = new RecordType(registry, recordProperties2, true);
 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithRecordTypeWithInvariantPropertyAndInvariantProperty() {
 RecordType recordType1 = new RecordType(registry, recordProperties, true);
 RecordProperty recordProperty = recordProperties.get("name");
 recordProperty.setInvariant(true);
 Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
 recordProperties2.put("name", new RecordProperty("name", String.class));
 recordProperties2.put("name", new RecordProperty("name", String.class));
 RecordType recordType2 = new RecordType(registry, recordProperties2, true);
 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithRecordTypeWithInvariantPropertyAndNonInvariantProperty() {
 RecordType recordType1 = new RecordType(registry, recordProperties, true);
 RecordProperty recordProperty = recordProperties.get("name");
 recordProperty.setInvariant(true);
 Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
 recordProperties2.put("name", new RecordProperty("name", String.class));
 recordProperties2.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType2 = new RecordType(registry, recordProperties2, true);
 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithRecordTypeWithNonInvariantPropertyAndInvariantProperty() {
 RecordType recordType1 = new RecordType(registry, recordProperties, true);
 Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
 recordProperties2.put("name", new RecordProperty("name", JSType.UNKNOWN));
 recordProperties2.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType2 = new RecordType(registry, recordProperties2, true);
 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithRecordTypeWithNonInvariantPropertyAndNonInvariantProperty() {
 RecordType recordType1 = new RecordType(registry, recordProperties, true);
 Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
 recordProperties2.put("name", new RecordProperty("name", Integer.class));
 recordProperties2.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType2 = new RecordType(registry, recordProperties2, true);
 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testResolveInternal() {
 RecordType recordType = new RecordType(registry, recordProperties, true);
 ErrorReporter errorReporter = new ErrorReporter();
 StaticScope<JSType> scope = new StaticScope<JSType>(registry);
 JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
 assertEquals(recordType.getClass(), resolvedType.getClass());
 }
}