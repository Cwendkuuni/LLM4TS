import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.testing.PolymerTestRunner;
import com.google.javascript.rhino.testing.type.RecordTypeBuilder;
import com.google.javascript.rhino.testing.type.UnionTypeBuilder;
import com.google.common.collect.Maps;

import java.util.SortedMap;
import java.util.Map;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @BeforeClass
 public static void setUpBeforeClass() throws Exception {
 PolymerTestRunner runner = new PolymerTestRunner();
 runner.init();
 }

 @Before
 public void setUp() throws Exception {
 registry = runner.getTypeRegistry();
 Map<String, RecordTypeBuilder.RecordProperty> properties =
 Maps.newHashMap();
 properties.put("name", RecordTypeBuilder.newRecordProperty("name", String.class));
 properties.put("age", RecordTypeBuilder.newRecordProperty("age", Number.class));
 recordType = new RecordType(registry, properties);
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);
 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());

 assertTrue(recordType1.isEquivalentTo(recordType1));
 assertTrue(recordType1.isEquivalentTo(recordType2));
 assertFalse(recordType1.isEquivalentTo(recordType3));
 }

 @Test
 public void testGetImplicitPrototype() {
 ObjectType implicitPrototype = recordType.getImplicitPrototype();
 assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype.getTypeID());
 }

 @Test
 public void testDefineProperty_nonFrozen() {
 String newPropertyName = "newProperty";
 JSType newPropertyType = Int32.INSTANCE;
 recordType.defineProperty(newPropertyName, newPropertyType, false, null);

 assertTrue(recordType.hasProperty(newPropertyName));
 assertEquals(newPropertyType, recordType.getPropertyType(newPropertyName));
 }

 @Test
 public void testDefineProperty_frozen() {
 recordType.isFrozen = true;
 String newPropertyName = "newProperty";
 JSType newPropertyType = Int32.INSTANCE;

 try {
 recordType.defineProperty(newPropertyName, newPropertyType, false, null);
 fail();
 } catch (IllegalStateException e) {
 assertEquals("Cannot define property 'newProperty' on a frozen record type!", e.getMessage());
 }
 }

 @Test
 public void testGetLeastSupertype() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("name", String.class, false, null);
 recordType2.defineProperty("age", Int32.INSTANCE, false, null);
 recordType2.freeze();

 JSType leastSupertype = recordType1.getLeastSupertype(recordType2);
 assertEquals(recordType1.getTypeID(), leastSupertype.getTypeID());
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("name", String.class, false, null);
 recordType2.defineProperty("age", Int32.INSTANCE, false, null);
 recordType2.defineProperty("id", Int32.INSTANCE, false, null);
 recordType2.freeze();

 JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 assertEquals(recordType2.getTypeID(), greatestSubtype.getTypeID());
 }

 @Test
 public void testIsSubtype() {
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("name", String.class, false, null);
 recordType2.defineProperty("age", Int32.INSTANCE, false, null);
 recordType2.freeze();

 // Empty record is a subtype of any record
 assertTrue(objectType.isSubtype(recordType1));
 // Record1 is a subtype of Record1
 assertTrue(recordType1.isSubtype(recordType1));
 // Record1 is a subtype of Record2 because it has all the properties of Record2
 assertTrue(recordType1.isSubtype(recordType2));
 // Record2 is not a subtype of Record1 because it has an additional property
 assertFalse(recordType2.isSubtype(recordType1));
 }

 @Test
 public void testResolveInternal() {
 String propertyName = "name";
 JSType propertyType = String.class;
 recordType.properties.put(propertyName, propertyType);
 recordType.isFrozen = true;

 RecordType resolvedRecordType = new RecordType(registry, properties);
 RecordTypeBuilder recordPropertyBuilder = new RecordTypeBuilder(registry);
 recordPropertyBuilder.addProperty(propertyName, propertyType, null);
 recordPropertyBuilder.freeze();
 JSType resolvedType = recordPropertyBuilder.build();

 RecordType expectedRecordType = new RecordType(registry, Maps.newHashMap());
 expectedRecordType.defineProperty(propertyName, propertyType, false, null);
 expectedRecordType.freeze();

 JSType result = recordType.resolveInternal(null, null);
 assertEquals(expectedRecordType.getTypeID(), result.getTypeID());
 }
}