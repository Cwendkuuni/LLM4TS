import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeRegistry;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

 private RecordTypeRegistry registry;
 private RecordType recordType;

 @Before
 public void setUp() {
 registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", new RecordProperty("a", registry.getBooleanType()));
 properties.put("b", new RecordProperty("b", registry.getStringType()));
 recordType = new RecordType(registry, properties);
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);
 assertTrue(recordType1.isEquivalentTo(recordType2));
 }

 @Test
 public void testIsNotEquivalentToDifferentProperties() {
 Map<String, RecordProperty> differentProperties = Maps.newHashMap();
 differentProperties.put("a", new RecordProperty("a", registry.getBooleanType()));
 differentProperties.put("c", new RecordProperty("c", registry.getStringType()));
 RecordType differentRecordType = new RecordType(registry, differentProperties);
 assertFalse(recordType.isEquivalentTo(differentRecordType));
 }

 @Test
 public void testIsNotEquivalentToDifferentPropertyTypes() {
 RecordProperty booleanProp = new RecordProperty("a", registry.getBooleanType());
 RecordProperty stringProp = new RecordProperty("a", registry.getStringType());
 Map<String, RecordProperty> differentProperties = Maps.newHashMap();
 differentProperties.put("a", booleanProp);
 differentProperties.put("b", stringProp);
 RecordType differentRecordType = new RecordType(registry, differentProperties);
 assertFalse(recordType.isEquivalentTo(differentRecordType));
 }

 @Test(expected = IllegalStateException.class)
 public void testNullRecordProperty() {
 Map<String, RecordProperty> nullProperties = Maps.newHashMap();
 nullProperties.put(null, new RecordProperty("a", registry.getBooleanType()));
 try {
 new RecordType(registry, nullProperties);
 fail();
 } catch (IllegalStateException e) {
 assertEquals("RecordProperty associated with a property should not be null!", e.getMessage());
 }
 }

 @Test
 public void testDefineProperty() {
 String propertyName = "c";
 RecordType updatedRecordType = new RecordType(registry, properties);
 JSType booleanType = registry.getBooleanType();
 updatedRecordType.defineProperty(propertyName, booleanType, false, null);
 assertTrue(updatedRecordType.hasProperty(propertyName));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyFrozen() {
 recordType.isFrozen = true;
 String propertyName = "c";
 JSType booleanType = registry.getBooleanType();
 try {
 recordType.defineProperty(propertyName, booleanType, false, null);
 fail();
 } catch (IllegalStateException e) {
 assertEquals("Cannot define property on a frozen record type.", e.getMessage());
 }
 }

 @Test
 public void testLeastSupertype() {
 RecordType recordTypeOther = new RecordType(registry, Maps.newHashMap());
 recordTypeOther.defineProperties(
 new RecordProperty[] {
 new RecordProperty("a", registry.getStringType()),
 new RecordProperty("b", registry.getBooleanType()),
 new RecordProperty("c", registry.getNumberType())
 });
 JSType leastSupertype = recordType.getLeastSupertype(recordTypeOther);
 assertNotNull(leastSupertype);
 assertSame(recordTypeOther, leastSupertype);
 }

 @Test
 public void testLeastSupertypeDifferentProperties() {
 RecordType differentRecordType = new RecordType(registry, differentProperties);
 JSType leastSupertype = recordType.getLeastSupertype(differentRecordType);
 assertNotNull(leastSupertype);
 assertNotSame(recordType, leastSupertype);
 assertTrue(leastSupertype.isSubtype(recordType));
 assertTrue(leastSupertype.isSubtype(differentRecordType));
 }

 @Test
 public void testGreatestSubtypeHelper() {
 RecordType recordTypeOther = new RecordType(registry, properties);
 recordTypeOther.defineProperties(
 new RecordProperty[] {
 new RecordProperty("a", registry.getIntType()),
 new RecordProperty("b", registry.getBooleanType())
 });
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(recordTypeOther);
 assertNotNull(greatestSubtype);
 assertSame(recordType, greatestSubtype);
 }

 @Test
 public void testGreatestSubtypeHelperDifferentProperties() {
 RecordType differentRecordType = new RecordType(registry, differentProperties);
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(differentRecordType);
 assertNotNull(greatestSubtype);
 assertTrue(greatestSubtype.isUnknownType());
 }

 @Test
 public void testGetImplicitPrototype() {
 ObjectType implicitPrototype = recordType.getImplicitPrototype();
 assertNotNull(implicitPrototype);
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
 }

 @Test
 public void testIsSubtype() {
 // Record type should be a subtype of itself
 assertTrue(recordType.isSubtype(recordType));

 // Record type should not be a subtype of a non-record type
 assertFalse(recordType.isSubtype(registry.getBooleanType()));

 // Record type should be a subtype of a record type with the same properties
 RecordType recordTypeOther = new RecordType(registry, properties);
 assertTrue(recordType.isSubtype(recordTypeOther));

 // Record type should not be a subtype of a record type with fewer properties
 Map<String, RecordProperty> fewerProperties = Maps.newHashMap();
 fewerProperties.put("a", new RecordProperty("a", registry.getBooleanType()));
 RecordType fewerPropertyRecordType = new RecordType(registry, fewerProperties);
 assertFalse(recordType.isSubtype(fewerPropertyRecordType));

 // Record type should be a subtype of a record type with the same properties but different names
 Map<String, RecordProperty> differentNamesProperties = Maps.newHashMap();
 differentNamesProperties.put("x", new RecordProperty("a", registry.getBooleanType()));
 RecordType differentNamesPropertyRecordType = new RecordType(registry, differentNamesProperties);
 assertTrue(recordType.isSubtype(differentNamesPropertyRecordType));
 }

 @Test
 public void testGetLeastSupertype() {
 RecordType recordTypeOther = new RecordType(registry, properties);
 JSType leastSupertype = recordType.getLeastSupertype(recordTypeOther);
 assertNotNull(leastSupertype);
 assertSame(recordType, leastSupertype);
 }
}