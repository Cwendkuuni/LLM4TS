import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;

 @BeforeClass
 public static void setUpClass() {
 registry = new JSTypeRegistry();
 }

 @Before
 public void setUp() {
 recordType = new RecordType(registry, createPropertiesMap());
 }

 private Map<String, RecordProperty> createPropertiesMap() {
 Map<String, RecordProperty> propertiesMap = Maps.newHashMap();
 propertiesMap.put("a", new RecordProperty("a", JSType.INT));
 propertiesMap.put("b", new RecordProperty("b", JSType.STRING));
 return propertiesMap;
 }

 private RecordType recordType;

 @Test
 public void testCreateRecordType() {
 RecordType record = new RecordType(registry, createPropertiesMap());
 assertNotNull(record.properties);
 assertEquals(2, record.properties.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateRecordTypeWithNullRecordProperty() {
 RecordType record = new RecordType(registry, null);
 fail("Should throw an IllegalArgumentException!");
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType record1 = new RecordType(registry, createPropertiesMap());
 RecordType record2 = new RecordType(registry, createPropertiesMap());
 assertTrue(record1.isEquivalentTo(record2));
 }

 @Test
 public void testIsNotEquivalentToDifferentRecordTypes() {
 RecordType record1 = new RecordType(registry, createPropertiesMap());
 RecordType record2 = new RecordType(registry, createPropertiesMap());
 RecordType record3 = new RecordType(registry, Maps.newHashMap());
 assertFalse(record1.isEquivalentTo(record3));
 }

 @Test
 public void testGetImplicitPrototype() {
 ObjectType implicitPrototype = recordType.getImplicitPrototype();
 assertSame(JSTypeNative.OBJECT_TYPE, implicitPrototype);
 }

 @Test
 public void testDefineProperty() {
 RecordType record = new RecordType(registry, createPropertiesMap());
 record.defineProperty("c", JSType.FLOAT, false, null);
 assertEquals(3, record.getPropertyNames().length);
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWhenFrozen() {
 RecordType record = new RecordType(registry, createPropertiesMap());
 record.isFrozen = true;
 record.defineProperty("d", JSType.BOOLEAN, false, null);
 fail("Should throw an IllegalStateException!");
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType record1 = new RecordType(registry, createPropertiesMap());
 RecordType record2 = new RecordType(registry, createPropertiesMap());
 RecordType record3 = new RecordType(registry, createPropertiesMap());
 RecordType record4 = new RecordType(registry, createPropertiesMap());
 RecordType record5 = new RecordType(registry, createPropertiesMap());

 UnionType record5Union = new UnionTypeBuilder(registry)
 .addAlternate(record1)
 .addAlternate(record2)
 .addAlternate(record3)
 .addAlternate(record4)
 .build();

 JSType greatestSubtype = record1.getGreatestSubtypeHelper(record5);
 assertSame(record5Union, greatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 RecordType record1 = new RecordType(registry, createPropertiesMap());
 RecordType record2 = new RecordType(registry, createPropertiesMap());
 RecordType record3 = new RecordType(registry, createPropertiesMap());
 RecordType record4 = new RecordType(registry, createPropertiesMap());
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 // Test subclass is a subtype of superclass
 assertTrue(record1.isSubtype(record2));
 assertTrue(record1.isSubtype(record3));
 assertTrue(record1.isSubtype(record4));

 // Test empty object type is a subtype of all record types
 assertTrue(objectType.isSubtype(record1));
 assertTrue(objectType.isSubtype(record2));
 assertTrue(objectType.isSubtype(record3));
 assertTrue(objectType.isSubtype(record4));

 // Test record type with property A is a subtype of record type with property B
 RecordType record5 = new RecordType(registry, Maps.newHashMap());
 record5.defineProperty("A", JSType.INT, false, null);
 RecordType record6 = new RecordType(registry, Maps.newHashMap());
 record6.defineProperty("B", JSType.STRING, false, null);
 assertTrue(record1.isSubtype(record5));
 assertTrue(record5.isSubtype(record1));
 assertTrue(record5.isSubtype(record6));
 assertTrue(record6.isSubtype(record5));

 // Test record type with property A is not a subtype of record type with property C
 RecordType record7 = new RecordType(registry, Maps.newHashMap());
 record7.defineProperty("C", JSType.BOOLEAN, false, null);
 assertFalse(record1.isSubtype(record7));
 assertFalse(record7.isSubtype(record1));

 // Test record type with property A is a subtype of record type with property A
 RecordType record8 = new RecordType(registry, Maps.newHashMap());
 record8.defineProperty("A", JSType.INT, false, null);
 assertTrue(record1.isSubtype(record8));
 assertTrue(record8.isSubtype(record1));
 }

 @Test
 public void testToString() {
 RecordType record = new RecordType(registry, createPropertiesMap());
 assertEquals("RecordType[a=int, b=string]", record.toString());
 }
}