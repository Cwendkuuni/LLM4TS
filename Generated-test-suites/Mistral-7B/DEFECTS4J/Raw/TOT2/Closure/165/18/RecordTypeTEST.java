import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

 @Test
 public void testRecordTypeCreation() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
 properties.put("name", new RecordProperty("name", String.class));
 properties.put("age", new RecordProperty("age", Integer.class));

 RecordType expectedRecordType = new RecordType(registry, properties);
 RecordType actualRecordType = new RecordType(registry, properties);

 assertEquals(expectedRecordType, actualRecordType);
 }

 @Test
 public void testRecordTypeIsSubtypeOfRecordTypeWithSameProperties() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
 properties.put("name", new RecordProperty("name", String.class));
 properties.put("age", new RecordProperty("age", Integer.class));

 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);

 assertTrue(recordType1.isSubtype(recordType2));
 assertTrue(recordType2.isSubtype(recordType1));
 }

 @Test
 public void testRecordTypeIsNotSubtypeOfRecordTypeWithDifferentProperties() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> properties1 = Maps.newLinkedHashMap();
 properties1.put("name", new RecordProperty("name", String.class));
 properties1.put("age", new RecordProperty("age", Integer.class));

 Map<String, RecordProperty> properties2 = Maps.newLinkedHashMap();
 properties2.put("name", new RecordProperty("name", String.class));
 properties2.put("age", new RecordProperty("age", Integer.class));
 properties2.put("gender", new RecordProperty("gender", String.class));

 RecordType recordType1 = new RecordType(registry, properties1);
 RecordType recordType2 = new RecordType(registry, properties2);

 assertFalse(recordType1.isSubtype(recordType2));
 assertFalse(recordType2.isSubtype(recordType1));
 }

 @Test
 public void testRecordTypeIsSubtypeOfObjectType() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
 properties.put("name", new RecordProperty("name", String.class));
 properties.put("age", new RecordProperty("age", Integer.class));

 RecordType recordType = new RecordType(registry, properties);
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 assertTrue(objectType.isSubtype(recordType));
 }

 @Test
 public void testRecordTypeIsNotSubtypeOfPrimitiveType() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
 properties.put("name", new RecordProperty("name", String.class));
 properties.put("age", new RecordProperty("age", Integer.class));

 RecordType recordType = new RecordType(registry, properties);
 JSType integerType = registry.getNativeType(JSTypeNative.INT_TYPE);

 assertFalse(integerType.isSubtype(recordType));
 }

 @Test
 public void testRecordTypeIsSubtypeOfUnionType() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
 properties.put("name", new RecordProperty("name", String.class));
 properties.put("age", new RecordProperty("age", Integer.class));

 RecordType recordType = new RecordType(registry, properties);
 UnionType unionType = new UnionTypeBuilder(registry)
 .addAlternate(registry.getNativeObjectType(JSTypeNative.NULL_TYPE))
 .addAlternate(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE))
 .build();

 assertTrue(recordType.isSubtype(unionType));
 }

 @Test
 public void testRecordTypeGetGreatestSubtype() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> propertiesA = Maps.newLinkedHashMap();
 propertiesA.put("name", new RecordProperty("name", String.class));
 propertiesA.put("age", new RecordProperty("age", Integer.class));

 Map<String, RecordProperty> propertiesB = Maps.newLinkedHashMap();
 propertiesB.put("name", new RecordProperty("name", String.class));
 propertiesB.put("age", new RecordProperty("age", Integer.class));
 propertiesB.put("gender", new RecordProperty("gender", String.class));

 Map<String, RecordProperty> propertiesC = Maps.newLinkedHashMap();
 propertiesC.put("name", new RecordProperty("name", String.class));
 propertiesC.put("age", new RecordProperty("age", Integer.class));
 propertiesC.put("gender", new RecordProperty("gender", String.class));
 propertiesC.put("city", new RecordProperty("city", String.class));

 RecordType recordTypeA = new RecordType(registry, propertiesA);
 RecordType recordTypeB = new RecordType(registry, propertiesB);
 RecordType recordTypeC = new RecordType(registry, propertiesC);

 JSType greatestSubtype = recordTypeA.getGreatestSubtype(recordTypeB);
 assertEquals(recordTypeC, greatestSubtype);
 }

 @Test
 public void testRecordTypeIsFrozen() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
 properties.put("name", new RecordProperty("name", String.class));
 properties.put("age", new RecordProperty("age", Integer.class));

 RecordType recordType = new RecordType(registry, properties);
 recordType.defineProperty("city", registry.getNativeType(JSTypeNative.STRING_TYPE), true, null);

 assertTrue(recordType.isFrozen());
 }
}