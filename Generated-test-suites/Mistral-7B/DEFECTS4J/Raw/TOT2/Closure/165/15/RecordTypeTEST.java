import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.TypeRegistry;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

 @Test
 public void testRecordTypeCreation() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
 properties.put("name", new RecordProperty("name", registry.stringType()));
 properties.put("age", new RecordProperty("age", registry.numberType()));

 RecordType recordType = new RecordType(registry, properties);

 assertEquals("{name: string, age: number}", recordType.toString());
 }

 @Test
 public void testRecordTypeFrozen() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
 properties.put("name", new RecordProperty("name", registry.stringType()));
 properties.put("age", new RecordProperty("age", registry.numberType()));

 RecordType recordType = new RecordType(registry, properties);
 recordType.defineProperty("gender", registry.stringType(), false, null);

 assertTrue(recordType.isFrozen());
 assertFalse(recordType.defineProperty("gender", registry.stringType(), false, null));
 }

 @Test
 public void testRecordTypeSubtyping() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> properties1 = Maps.newLinkedHashMap();
 properties1.put("name", new RecordProperty("name", registry.stringType()));
 properties1.put("age", new RecordProperty("age", registry.numberType()));
 RecordType recordType1 = new RecordType(registry, properties1);

 Map<String, RecordProperty> properties2 = Maps.newLinkedHashMap();
 properties2.put("name", new RecordProperty("name", registry.stringType()));
 properties2.put("age", new RecordProperty("age", registry.numberType()));
 properties2.put("gender", new RecordProperty("gender", registry.stringType()));
 RecordType recordType2 = new RecordType(registry, properties2);

 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testRecordTypeNoSupertype() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> properties1 = Maps.newLinkedHashMap();
 properties1.put("name", new RecordProperty("name", registry.stringType()));
 properties1.put("age", new RecordProperty("age", registry.numberType()));
 RecordType recordType1 = new RecordType(registry, properties1);

 Map<String, RecordProperty> properties2 = Maps.newLinkedHashMap();
 properties2.put("gender", new RecordProperty("gender", registry.stringType()));
 RecordType recordType2 = new RecordType(registry, properties2);

 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testRecordTypePropertyInference() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
 properties.put("x", new RecordProperty("x", null));
 RecordType recordType = new RecordType(registry, properties);

 Node node = new Node();
 node.setType(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
 node.setProperty("x", new Node("foo"));
 recordType.resolveInternal(null, null);

 JSType propertyType = recordType.getPropertyType("x");
 assertEquals(registry.stringType(), propertyType);
 }

 @Test(expected = IllegalStateException.class)
 public void testRecordTypePropertyNullType() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
 properties.put("x", new RecordProperty("x", null));
 RecordType recordType = new RecordType(registry, properties);
 }
}