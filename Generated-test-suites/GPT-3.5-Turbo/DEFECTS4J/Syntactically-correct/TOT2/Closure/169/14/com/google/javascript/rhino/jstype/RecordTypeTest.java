import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeProperties() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordProperty(new JSType(), null));
    properties.put("b", new RecordProperty(new JSType(), null));

    RecordType recordType = new RecordType(properties);
    
    assertEquals(2, recordType.getProperties().size());
    assertTrue(recordType.hasProperty("a"));
    assertTrue(recordType.hasProperty("b"));
  }

  @Test
  public void testAddProperty() {
    RecordType recordType = new RecordType(new HashMap<>());
    JSType type = new JSType();
    Node propertyNode = new Node();
    
    assertTrue(recordType.defineProperty("c", type, true, propertyNode));
    assertTrue(recordType.hasProperty("c"));
    assertEquals(type, recordType.getPropertyType("c"));
  }

  @Test
  public void testFrozenRecordType() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordProperty(new JSType(), null));
    RecordType recordType = new RecordType(properties);
    
    assertFalse(recordType.defineProperty("b", new JSType(), true, null));
    assertFalse(recordType.hasProperty("b"));
  }

  @Test
  public void testRecordTypeSubtyping() {
    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("x", new RecordProperty(new JSType(), null));
    RecordType recordTypeA = new RecordType(propertiesA);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("x", new RecordProperty(new JSType(), null));
    propertiesB.put("y", new RecordProperty(new JSType(), null));
    RecordType recordTypeB = new RecordType(propertiesB);

    assertTrue(RecordType.isSubtype(recordTypeB, recordTypeA));
    assertFalse(RecordType.isSubtype(recordTypeA, recordTypeB));
  }
}