import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordProperty;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 RecordProperty recordProperty = new RecordProperty("propertyName", INT_TYPE);
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("propertyName", recordProperty);
 recordType = new RecordType(registry, properties);
 }

 @Test
 public void testConstructor() {
 RecordType expectedRecordType = new RecordType(registry, properties);
 assertEquals(expectedRecordType, recordType);
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType anotherRecordType = new RecordType(registry, properties);
 assertTrue(recordType.isEquivalentTo(anotherRecordType));
 }

 @Test
 public void testIsEquivalentToDifferentRecordTypes() {
 RecordType anotherRecordTypeWithDifferentProperties = new RecordType(registry,
 Maps.newHashMap());
 assertFalse(recordType.isEquivalentTo(anotherRecordTypeWithDifferentProperties));
 }

 @Test
 public void testGetImplicitPrototype() {
 ObjectType expectedPrototype = registry.getNativeObjectType(
 JSTypeNative.OBJECT_TYPE);
 ObjectType actualPrototype = recordType.getImplicitPrototype();
 assertEquals(expectedPrototype, actualPrototype);
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyFrozenRecordType() {
 recordType.freeze();
 try {
 recordType.defineProperty("newPropertyName", INT_TYPE, false, null);
 fail();
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testGetLeastSupertype() {
 RecordType anotherRecordType = new RecordType(registry, properties);
 RecordType expectedLeastSupertype = new RecordType(registry, properties);
 RecordType actualLeastSupertype = recordType.getLeastSupertype(anotherRecordType);
 assertEquals(expectedLeastSupertype, actualLeastSupertype);
 }

 @Test
 public void testGetLeastSupertypeDifferentRecordTypes() {
 RecordType anotherRecordTypeWithDifferentProperties = new RecordType(registry,
 Maps.newHashMap());
 RecordType expectedLeastSupertype = registry.getNativeType(
 JSTypeNative.NO_OBJECT_TYPE);
 RecordType actualLeastSupertype = recordType.getLeastSupertype(anotherRecordTypeWithDifferentProperties);
 assertEquals(expectedLeastSupertype, actualLeastSupertype);
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType anotherRecordTypeWithSameProperties = new RecordType(registry, properties);
 RecordType expectedGreatestSubtype = new RecordType(registry, properties);
 RecordType actualGreatestSubtype = recordType.getGreatestSubtypeHelper(anotherRecordTypeWithSameProperties);
 assertEquals(expectedGreatestSubtype, actualGreatestSubtype);
 }

 @Test
 public void testGetGreatestSubtypeHelperDifferentRecordTypes() {
 RecordType anotherRecordTypeWithDifferentProperties = new RecordType(registry,
 Maps.newHashMap());
 RecordType expectedGreatestSubtype = registry.getNativeType(
 JSTypeNative.NO_OBJECT_TYPE);
 RecordType actualGreatestSubtype = recordType.getGreatestSubtypeHelper(anotherRecordTypeWithDifferentProperties);
 assertEquals(expectedGreatestSubtype, actualGreatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 RecordType anotherRecordType = new RecordType(registry, properties);
 assertTrue(recordType.isSubtype(anotherRecordType));
 }

 @Test
 public void testIsSubtypeDifferentRecordTypes() {
 RecordType anotherRecordTypeWithDifferentProperties = new RecordType(registry,
 Maps.newHashMap());
 assertFalse(recordType.isSubtype(anotherRecordTypeWithDifferentProperties));
 }
}