import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private ErrorReporter errorReporter;
 private RecordType recordType;

 @Before
 public void setUp() {
 registry = mock(JSTypeRegistry.class);
 errorReporter = mock(ErrorReporter.class);
 recordType = new RecordType(registry, Maps.newHashMap());
 }

 @Test
 public void testRecordTypeCreation() {
 RecordType recordType = new RecordType(registry, Maps.newHashMap());
 assertNotNull(recordType);
 }

 @Test(expected = IllegalStateException.class)
 public void testRecordTypeCreationWithNullProperty() {
 RecordTypeBuilder builder = new RecordTypeBuilder(registry);
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("property1", null);
 RecordType recordType = new RecordType(registry, properties);
 }

 @Test
 public void testIsEquivalentToSameInstance() {
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType2 = recordType1;
 assertTrue(recordType1.isEquivalentTo(recordType2));
 }

 @Test
 public void testIsEquivalentToDifferentInstances() {
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 assertFalse(recordType1.isEquivalentTo(recordType2));
 }

 @Test
 public void testIsEquivalentToSameProperties() {
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 recordType1.defineProperty("property1", JSType.getBooleanType(), false, null);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("property1", JSType.getBooleanType(), false, null);
 assertTrue(recordType1.isEquivalentTo(recordType2));
 }

 @Test
 public void testIsEquivalentToDifferentProperties() {
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 recordType1.defineProperty("property1", JSType.getBooleanType(), false, null);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("property1", JSType.getIntType(), false, null);
 assertFalse(recordType1.isEquivalentTo(recordType2));
 }

 @Test
 public void testGetImplicitPrototype() {
 ObjectType implicitPrototype = recordType.getImplicitPrototype();
 assertSame(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyFrozenRecord() {
 recordType.isFrozen = true;
 recordType.defineProperty("property1", JSType.getBooleanType(), false, null);
 }

 @Test
 public void testLeastSupertype() {
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 recordType1.defineProperty("property1", JSType.getBooleanType(), false, null);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("property1", JSType.getBooleanType(), false, null);
 recordType2.defineProperty("property2", JSType.getIntType(), false, null);

 RecordType leastSupertype = recordType1.getLeastSupertype(recordType2);
 assertSame(recordType2, leastSupertype);
 }

 @Test
 public void testGreatestSubtypeHelperDifferentTypes() {
 JSType type1 = JSType.getBooleanType();
 JSType type2 = JSType.getIntType();
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(type1);
 assertSame(registry.getNativeObjectType(JSTypeNative.NO_TYPE), greatestSubtype);
 }

 @Test
 public void testGetGreatestSubtypeHelperSameTypes() {
 JSType type1 = JSType.getBooleanType();
 JSType type2 = JSType.getBooleanType();
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(type1);
 assertSame(type1, greatestSubtype);
 }

 @Test
 public void testGetGreatestSubtypeHelperObjectType() {
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(objectType);
 assertSame(objectType, greatestSubtype);
 }

 @Test
 public void testIsSubtypeSameTypes() {
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 recordType1.defineProperty("property1", JSType.getBooleanType(), false, null);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("property1", JSType.getBooleanType(), false, null);
 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeDifferentTypes() {
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 recordType1.defineProperty("property1", JSType.getBooleanType(), false, null);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("property1", JSType.getIntType(), false, null);
 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeRecordTypeToObjectType() {
 RecordType recordType = new RecordType(registry, Maps.newHashMap());
 recordType.defineProperty("property1", JSType.getBooleanType(), false, null);
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 assertFalse(recordType.isSubtype(objectType));
 }

 @Test
 public void testIsSubtypeObjectTypeToRecordType() {
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 RecordType recordType = new RecordType(registry, Maps.newHashMap());
 recordType.defineProperty("property1", JSType.getBooleanType(), false, null);
 assertTrue(objectType.isSubtype(recordType));
 }

 @Test
 public void testIsSubtypeRecordTypeToSubtypeRecordType() {
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 recordType1.defineProperty("property1", JSType.getBooleanType(), false, null);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("property1", JSType.getBooleanType(), false, null);
 recordType2.defineProperty("property2", JSType.getIntType(), false, null);
 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeRecordTypeToSupertypeRecordType() {
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 recordType1.defineProperty("property1", JSType.getBooleanType(), false, null);
 recordType1.defineProperty("property2", JSType.getIntType(), false, null);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("property1", JSType.getBooleanType(), false, null);
 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testToString() {
 String toString = recordType.toString();
 assertNotNull(toString);
 }
}