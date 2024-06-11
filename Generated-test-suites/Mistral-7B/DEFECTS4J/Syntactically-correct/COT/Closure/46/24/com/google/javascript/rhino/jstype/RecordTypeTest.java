import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 RecordProperty property1 = new RecordProperty("property1", Integer.TYPE);
 RecordProperty property2 = new RecordProperty("property2", String.class);
 Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
 properties.put("property1", property1);
 properties.put("property2", property2);
 recordType = new RecordType(registry, properties);
 }

 @Test
 public void testConstructor() {
 RecordType recordType = new RecordType(registry, properties);
 assertNotNull(recordType.properties);
 assertEquals(2, recordType.properties.size());
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);
 assertTrue(recordType1.isEquivalentTo(recordType2));
 }

 @Test
 public void testGetImplicitPrototype() {
 ObjectType implicitPrototype = recordType.getImplicitPrototype();
 assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype.getTypeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDefinePropertyFrozen() {
 recordType.defineProperty("property3", Integer.TYPE, false, null);
 fail("Should not be able to define property on frozen record type");
 }

 @Test
 public void testGetLeastSupertype() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordTypeBuilder(registry)
 .addProperty("property1", Integer.TYPE, null)
 .addProperty("property2", String.class, null)
 .build();
 RecordType leastSupertype = recordType1.getLeastSupertype(recordType2);
 assertEquals(recordType1.getTypeName(), leastSupertype.getTypeName());
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordTypeBuilder(registry)
 .addProperty("property1", Integer.TYPE, null)
 .addProperty("property3", String.class, null)
 .build();
 RecordType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 assertNotNull(greatestSubtype);
 assertEquals(recordType2.getTypeName(), greatestSubtype.getTypeName());
 }

 @Test(expected = IllegalStateException.class)
 public void testIsSubtypeNull() {
 RecordType recordType = new RecordType(registry, properties);
 assertFalse(recordType.isSubtype(null));
 }

 @Test
 public void testIsSubtypeSelf() {
 RecordType recordType = new RecordType(registry, properties);
 assertTrue(recordType.isSubtype(recordType));
 }

 @Test
 public void testIsSubtypeRecordType() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordTypeBuilder(registry)
 .addProperty("property1", Integer.TYPE, null)
 .addProperty("property2", String.class, null)
 .build();
 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeObjectType() {
 RecordType recordType = new RecordType(registry, properties);
 assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
 .isSubtype(recordType));
 }

 @Test
 public void testIsSubtypeUnknownType() {
 RecordType recordType = new RecordType(registry, properties);
 assertTrue(recordType.isSubtype(JSType.UNKNOWN_TYPE));
 }

 @Test
 public void testIsSubtypeSameType() {
 RecordType recordType = new RecordType(registry, properties);
 assertTrue(recordType.isSubtype(recordType));
 }

 @Test
 public void testIsSubtypeDifferentTypes() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordTypeBuilder(registry)
 .addProperty("property1", Integer.TYPE, null)
 .addProperty("property2", String.class, null)
 .build();
 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithIncompatiblePropertyType() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordTypeBuilder(registry)
 .addProperty("property1", Integer.TYPE, null)
 .addProperty("property2", Double.TYPE, null)
 .build();
 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithMissingProperty() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordTypeBuilder(registry)
 .addProperty("property1", Integer.TYPE, null)
 .build();
 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithExtraProperty() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordTypeBuilder(registry)
 .addProperty("property1", Integer.TYPE, null)
 .addProperty("property3", String.class, null)
 .build();
 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testResolveInternal() {
 RecordType recordType = new RecordType(registry, properties);
 RecordProperty property1 = recordType.getProperty("property1");
 property1.setType(Float.TYPE);
 assertEquals(Float.TYPE, recordType.resolveInternal(null, null)
 .getPropertyType("property1").getTypeClass());
 }
}