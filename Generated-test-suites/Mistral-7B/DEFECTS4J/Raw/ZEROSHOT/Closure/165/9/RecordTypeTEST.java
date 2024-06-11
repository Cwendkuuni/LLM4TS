import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 RecordProperty prop1 = new RecordProperty("prop1", registry.getBooleanType());
 RecordProperty prop2 = new RecordProperty("prop2", registry.getStringType());
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("prop1", prop1);
 properties.put("prop2", prop2);
 recordType = new RecordType(registry, properties);
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);
 assertTrue(recordType1.isEquivalentTo(recordType2));
 }

 @Test
 public void testIsEquivalentToDifferentProperties() {
 Map<String, RecordProperty> propertiesDifferent = Maps.newHashMap();
 propertiesDifferent.put("prop1", prop1);
 propertiesDifferent.put("prop3", new RecordProperty("prop3", registry.getIntType()));
 RecordType recordTypeDifferent = new RecordType(registry, propertiesDifferent);
 assertFalse(recordType.isEquivalentTo(recordTypeDifferent));
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 assertTrue(recordType1.isSubtype(recordType2));

 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
 recordType3.defineProperty("prop1", registry.getStringType(), false, null);
 assertFalse(recordType1.isSubtype(recordType3));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType2.defineProperty("prop1", registry.getIntType(), false, null);

 JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 assertEquals(registry.getRecordType(properties), greatestSubtype);
 }

 @Test
 public void testGetGreatestSubtypeHelperWithSameType() {
 RecordType recordType1 = new RecordType(registry, properties);
 JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType1);
 assertEquals(recordType1, greatestSubtype);
 }

 @Test
 public void testGetGreatestSubtypeHelperWithNoType() {
 RecordType recordType1 = new RecordType(registry, properties);
 JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.NO_TYPE));
 assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), greatestSubtype);
 }

 @Test
 public void testGetGreatestSubtypeHelperWithNull() {
 RecordType recordType1 = new RecordType(registry, properties);
 JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(null);
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype);
 }

 @Test
 public void testIsFrozen() {
 RecordType recordType1 = new RecordType(registry, properties);
 assertFalse(recordType1.isFrozen);

 recordType1.defineProperty("prop3", registry.getIntType(), false, null);
 assertTrue(recordType1.isFrozen);
 }

 @Test
 public void testIsSubtypeWithRecordTypeAndObject() {
 RecordType recordType = new RecordType(registry, properties);
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 assertFalse(recordType.isSubtype(objectType));
 }

 @Test
 public void testIsSubtypeWithObjectAndRecordType() {
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 RecordType recordType = new RecordType(registry, properties);
 assertFalse(objectType.isSubtype(recordType));
 }

 @Test
 public void testIsSubtypeWithRecordTypeWithIncompatibleProperty() {
 Map<String, RecordProperty> propertiesIncompatible = Maps.newHashMap();
 propertiesIncompatible.put("prop1", new RecordProperty("prop1", registry.getIntType()));
 propertiesIncompatible.put("prop2", new RecordProperty("prop2", registry.getStringType()));
 RecordType recordTypeIncompatible = new RecordType(registry, propertiesIncompatible);
 RecordType recordType = new RecordType(registry, properties);
 assertFalse(recordType.isSubtype(recordTypeIncompatible));
 }

 @Test
 public void testIsSubtypeWithRecordTypeWithCompatibleProperty() {
 Map<String, RecordProperty> propertiesCompatible = new RecordProperty("prop1", registry.getIntType()).asMap();
 Map<String, RecordProperty> propertiesCompatibleWithCompatible = Maps.newHashMap();
 propertiesCompatibleWithCompatible.putAll(propertiesCompatible);
 propertiesCompatibleWithCompatible.put("prop2", new RecordProperty("prop2", registry.getIntType()));
 RecordType recordTypeCompatible = new RecordType(registry, propertiesCompatibleWithCompatible);
 RecordType recordType = new RecordType(registry, properties);
 assertTrue(recordType.isSubtype(recordTypeCompatible));
 }

 @Test
 public void testIsSubtypeWithRecordTypeAndRecordTypeWithIncompatibleProperty() {
 RecordType recordType1 = new RecordType(registry, properties);
 Map<String, RecordProperty> propertiesIncompatible2 = Maps.newHashMap();
 propertiesIncompatible2.put("prop2", new RecordProperty("prop2", registry.getIntType()));
 RecordType recordType2 = new RecordType(registry, propertiesIncompatible2);
 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithRecordTypeAndRecordTypeWithCompatibleProperty() {
 RecordType recordType1 = new RecordType(registry, properties);
 Map<String, RecordProperty> propertiesCompatible2 = Maps.newHashMap();
 propertiesCompatible2.putAll(properties);
 propertiesCompatible2.put("prop3", new RecordProperty("prop3", registry.getIntType()));
 RecordType recordType2 = new RecordType(registry, propertiesCompatible2);
 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithRecordTypeAndUnionType() {
 RecordType recordType = new RecordType(registry, properties);
 UnionType unionType = new UnionType(registry, new ObjectType[0]);
 assertFalse(recordType.isSubtype(unionType));
 }

 @Test
 public void testIsSubtypeWithUnionTypeAndRecordType() {
 UnionType unionType = new UnionType(registry, new ObjectType[0]);
 RecordType recordType = new RecordType(registry, properties);
 assertFalse(unionType.isSubtype(recordType));
 }

 @Test
 public void testIsSubtypeWithUnionTypeAndUnionType() {
 UnionType unionType1 = new UnionType(registry, new ObjectType[0]);
 UnionType unionType2 = new UnionType(registry, new ObjectType[0]);
 assertFalse(unionType1.isSubtype(unionType2));
 }

 @Test
 public void testIsSubtypeWithUnionTypeAndObject() {
 UnionType unionType = new UnionType(registry, new ObjectType[0]);
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 assertFalse(unionType.isSubtype(objectType));
 }

 @Test
 public void testIsSubtypeWithObjectAndUnionType() {
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 UnionType unionType = new UnionType(registry, new ObjectType[0]);
 assertFalse(objectType.isSubtype(unionType));
 }

 @Test
 public void testIsSubtypeWithObjectAndRecordType() {
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 RecordType recordType = new RecordType(registry, properties);
 assertTrue(objectType.isSubtype(recordType));
 }

 @Test
 public void testIsSubtypeWithRecordTypeAndRecordTypeWithSamePropertiesButDifferentNodes() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordProperty recordPropertyNode1 = new RecordProperty("prop1", registry.getIntType());
 RecordProperty recordPropertyNode2 = new RecordProperty("prop1", registry.getIntType());
 RecordType recordType2 = new RecordType(registry, properties);
 recordType2.setPropertyNode("prop1", recordPropertyNode2);
 assertFalse(recordType1.isEquivalentTo(recordType2));
 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithRecordTypeAndRecordTypeWithSamePropertiesButDifferentTypes() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordProperty recordPropertyType1 = new RecordProperty("prop1", registry.getIntType());
 RecordProperty recordPropertyType2 = new RecordProperty("prop1", registry.getBooleanType());
 RecordType recordType2 = new RecordType(registry, properties);
 recordType2.defineProperty("prop1", recordPropertyType2, false, null);
 assertFalse(recordType1.isSubtype(recordType2));
 }
}