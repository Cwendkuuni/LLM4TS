import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

 @Test
 public void testRecordTypeCreation() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> properties = new Maps.HashMap<>();
 properties.put("name", new RecordProperty("name", String.class));
 properties.put("age", new RecordProperty("age", Integer.class));

 RecordType recordType = new RecordType(registry, properties);

 assertEquals("{name: String, age: Integer}", recordType.toString());
 }

 @Test
 public void testRecordTypeSubtyping() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> propertiesA = new Maps.HashMap<>();
 propertiesA.put("name", new RecordProperty("name", String.class));
 propertiesA.put("age", new RecordProperty("age", Integer.class));
 RecordType recordTypeA = new RecordType(registry, propertiesA);

 Map<String, RecordProperty> propertiesB = new Maps.HashMap<>();
 propertiesB.put("name", new RecordProperty("name", String.class));
 propertiesB.put("age", new RecordProperty("age", Integer.class));
 propertiesB.put("gender", new RecordProperty("gender", String.class));
 RecordType recordTypeB = new RecordType(registry, propertiesB);

 RecordType recordTypeC = recordTypeA.getGreatestSubtype(recordTypeB);

 assertEquals("{name: String, age: Integer}", recordTypeC.toString());
 }

 @Test
 public void testRecordTypeImmutable() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> properties = new Maps.HashMap<>();
 properties.put("name", new RecordProperty("name", String.class));
 properties.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType = new RecordType(registry, properties);

 recordType.defineProperty("gender", Integer.class, false, null);

 assertFalse(recordType.hasProperty("gender"));
 }

 @Test(expected = IllegalStateException.class)
 public void testRecordTypeNullProperty() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> properties = new Maps.HashMap<>();
 properties.put("name", new RecordProperty("name", String.class));
 properties.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType = new RecordType(registry, properties);

 RecordProperty nullProperty = null;
 recordType.defineProperty("gender", nullProperty, false, null);
 }

 @Test
 public void testRecordTypeEquals() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> propertiesA = new Maps.HashMap<>();
 propertiesA.put("name", new RecordProperty("name", String.class));
 propertiesA.put("age", new RecordProperty("age", Integer.class));
 RecordType recordTypeA = new RecordType(registry, propertiesA);

 Map<String, RecordProperty> propertiesB = new Maps.HashMap<>();
 propertiesB.put("name", new RecordProperty("name", String.class));
 propertiesB.put("age", new RecordProperty("age", Integer.class));
 propertiesB.put("gender", new RecordProperty("gender", String.class));
 RecordType recordTypeB = new RecordType(registry, propertiesB);

 Map<String, RecordProperty> propertiesC = new Maps.HashMap<>();
 propertiesC.put("name", new RecordProperty("name", String.class));
 propertiesC.put("age", new RecordProperty("age", Integer.class));
 RecordType recordTypeC = new RecordType(registry, propertiesC);

 assertEquals(recordTypeA, recordTypeA);
 assertEquals(recordTypeA, recordTypeC);
 assertNotEquals(recordTypeA, recordTypeB);
 }

 @Test
 public void testRecordTypeIsSubtype() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> propertiesA = new Maps.HashMap<>();
 propertiesA.put("name", new RecordProperty("name", String.class));
 propertiesA.put("age", new RecordProperty("age", Integer.class));
 RecordType recordTypeA = new RecordType(registry, propertiesA);

 Map<String, RecordProperty> propertiesB = new Maps.HashMap<>();
 propertiesB.put("age", new RecordProperty("age", Integer.class));
 propertiesB.put("gender", new RecordProperty("gender", String.class));
 RecordType recordTypeB = new RecordType(registry, propertiesB);

 Map<String, RecordProperty> propertiesC = new Maps.HashMap<>();
 propertiesC.put("name", new RecordProperty("name", String.class));
 propertiesC.put("age", new RecordProperty("age", Integer.class));
 RecordType recordTypeC = new RecordType(registry, propertiesC);

 RecordType recordTypeD = new RecordType(registry, Collections.emptyMap());

 assertTrue(recordTypeA.isSubtype(recordTypeA));
 assertTrue(recordTypeA.isSubtype(recordTypeC));
 assertTrue(recordTypeC.isSubtype(recordTypeA));
 assertTrue(recordTypeA.isSubtype(recordTypeD));
 assertTrue(recordTypeD.isSubtype(recordTypeA));
 assertFalse(recordTypeA.isSubtype(recordTypeB));
 assertFalse(recordTypeB.isSubtype(recordTypeA));
 }

 @Test
 public void testRecordTypeNotSubtype() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> propertiesA = new Maps.HashMap<>();
 propertiesA.put("name", new RecordProperty("name", String.class));
 propertiesA.put("age", new RecordProperty("age", Integer.class));
 RecordType recordTypeA = new RecordType(registry, propertiesA);

 Map<String, RecordProperty> propertiesB = new Maps.HashMap<>();
 propertiesB.put("name", new RecordProperty("name", String[].class));
 RecordType recordTypeB = new RecordType(registry, propertiesB);

 assertFalse(recordTypeA.isSubtype(recordTypeB));
 }

 @Test
 public void testRecordTypeToString() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> properties = new Maps.HashMap<>();
 properties.put("name", new RecordProperty("name", String.class));
 properties.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType = new RecordType(registry, properties);

 assertEquals("{name: String, age: Integer}", recordType.toString());
 }

 @Test
 public void testRecordTypeWithEmptyPropertyMap() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 RecordType recordType = new RecordType(registry, Collections.emptyMap());
 assertEquals("{}", recordType.toString());
 }

 @Test
 public void testRecordTypeFrozen() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> properties = new Maps.HashMap<>();
 properties.put("name", new RecordProperty("name", String.class));
 properties.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType = new RecordType(registry, properties);
 recordType.freeze();

 assertFalse(recordType.isFrozen());

 recordType.defineProperty("gender", Integer.class, false, null);
 assertTrue(recordType.isFrozen());
 }
}