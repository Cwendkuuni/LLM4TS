import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

 // Basic test case for constructor
 @Test
 public void testRecordTypeConstruction() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = new Maps.HashMap<>();
 properties.put("name", new RecordProperty("name", registry.getStringType()));
 RecordType recordType = new RecordType(registry, properties);
 assertNotNull(recordType);
 }

 // Test isEquivalentTo method
 @Test
 public void testIsEquivalentTo() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties1 = new Maps.HashMap<>();
 properties1.put("name", new RecordProperty("name", registry.getStringType()));
 RecordType recordType1 = new RecordType(registry, properties1);

 Map<String, RecordProperty> properties2 = new Maps.HashMap<>();
 properties2.put("name", new RecordProperty("name", registry.getStringType()));
 properties2.put("age", new RecordProperty("age", registry.getNumberType()));
 RecordType recordType2 = new RecordType(registry, properties2);

 RecordType recordType3 = new RecordType(registry, properties1);
 assertTrue(recordType1.isEquivalentTo(recordType3));
 assertFalse(recordType1.isEquivalentTo(recordType2));
 }

 // Test getLeastSupertype method
 @Test
 public void testGetLeastSupertype() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType type1 = new RecordType(registry, Maps.newLinkedHashMap());
 RecordType type2 = new RecordType(registry, Maps.newLinkedHashMap("a", registry.getBooleanType()));
 RecordType type3 = new RecordType(registry, Maps.newLinkedHashMap("a", registry.getBooleanType(), "b", registry.getNumberType()));

 RecordType leastSupertype = type1.getLeastSupertype(type3);
 assertEquals(type2, leastSupertype);
 }

 // Test getGreatestSubtypeHelper method with empty record type
 @Test
 public void testGetGreatestSubtypeHelperEmptyRecordType() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType1 = new RecordType(registry, Maps.newLinkedHashMap());
 RecordType recordType2 = new RecordType(registry, Maps.newLinkedHashMap("a", registry.getStringType()));

 RecordType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype);
 }

 // Test getGreatestSubtypeHelper method with conflicting properties
 @Test(expected = IllegalStateException.class)
 public void testGetGreatestSubtypeHelperConflictingProperties() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType1 = new RecordType(registry, Maps.newLinkedHashMap("a", registry.getStringType(), "a", registry.getNumberType()));
 RecordType recordType2 = new RecordType(registry, Maps.newLinkedHashMap("a", registry.getStringType()));

 recordType1.getGreatestSubtypeHelper(recordType2);
 }

 // Test isSubtype method
 @Test
 public void testIsSubtype() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType1 = new RecordType(registry, Maps.newLinkedHashMap("a", registry.getStringType(), "b", registry.getNumberType()));
 RecordType recordType2 = new RecordType(registry, Maps.newLinkedHashMap("a", registry.getStringType()));
 RecordType recordType3 = new RecordType(registry, Maps.newLinkedHashMap("a", registry.getNumberType()));

 assertTrue(recordType1.isSubtype(recordType2));
 assertFalse(recordType1.isSubtype(recordType3));
 }

 // Test resolveInternal method
 @Test
 public void testResolveInternal() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = new Maps.HashMap<>();
 properties.put("name", new RecordProperty("name", registry.getStringType()));
 RecordType recordType = new RecordType(registry, properties);
 Node propertyNode = Node.newName("name");
 JSType resolvedType = recordType.resolveInternal(null, null);
 assertEquals(registry.getStringType(), resolvedType);
 }
}