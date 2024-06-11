import org.junit.Test;
import org.junit.Assert;

public class RecordTypeTest {

  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("a", new RecordProperty(new JSType(registry), new Node("a")));
    properties1.put("b", new RecordProperty(new JSType(registry), new Node("b")));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("a", new RecordProperty(new JSType(registry), new Node("a")));
    properties2.put("b", new RecordProperty(new JSType(registry), new Node("b")));
    RecordType recordType2 = new RecordType(registry, properties2);

    Assert.assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("a", new RecordProperty(new JSType(registry), new Node("a")));
    properties1.put("b", new RecordProperty(new JSType(registry), new Node("b")));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("a", new RecordProperty(new JSType(registry), new Node("a")));
    RecordType recordType2 = new RecordType(registry, properties2);

    Assert.assertTrue(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("a", new RecordProperty(new JSType(registry), new Node("a")));
    properties1.put("b", new RecordProperty(new JSType(registry), new Node("b")));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("a", new RecordProperty(new JSType(registry), new Node("a")));
    properties2.put("b", new RecordProperty(new JSType(registry), new Node("b")));
    properties2.put("c", new RecordProperty(new JSType(registry), new Node("c")));
    RecordType recordType2 = new RecordType(registry, properties2);

    Assert.assertEquals(recordType2, recordType1.getGreatestSubtypeHelper(recordType2));
  }

  @Test
  public void testToMaybeRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordProperty(new JSType(registry), new Node("a")));
    properties.put("b", new RecordProperty(new JSType(registry), new Node("b")));
    RecordType recordType = new RecordType(registry, properties);

    Assert.assertEquals(recordType, recordType.toMaybeRecordType());
  }

  @Test
  public void testIsRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordProperty(new JSType(registry), new Node("a")));
    properties.put("b", new RecordProperty(new JSType(registry), new Node("b")));
    RecordType recordType = new RecordType(registry, properties);

    Assert.assertTrue(recordType.isRecordType());
  }
}