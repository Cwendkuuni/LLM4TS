import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordProperty prop1 = new RecordProperty(new JSType(), new Node());
    RecordProperty prop2 = new RecordProperty(new JSType(), new Node());
    
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("property1", prop1);
    properties.put("property2", prop2);
    
    RecordType recordType = new RecordType(registry, properties);
    
    assertNotNull(recordType);
    assertTrue(recordType.isFrozen());
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, new HashMap<>());
    
    String propertyName = "testProperty";
    JSType type = new JSType();
    Node propertyNode = new Node();
    
    boolean result = recordType.defineProperty(propertyName, type, false, propertyNode);
    
    assertTrue(result);
    assertTrue(recordType.hasProperty(propertyName));
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordProperty prop1 = new RecordProperty(new JSType(), new Node());
    RecordProperty prop2 = new RecordProperty(new JSType(), new Node());
    
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("property1", prop1);
    properties.put("property2", prop2);
    
    RecordType recordTypeA = new RecordType(registry, properties);
    RecordType recordTypeB = new RecordType(registry, properties);
    
    boolean result = RecordType.isSubtype(recordTypeA, recordTypeB);
    
    assertTrue(result);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordProperty prop1 = new RecordProperty(new JSType(), new Node());
    RecordProperty prop2 = new RecordProperty(new JSType(), new Node());
    
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("property1", prop1);
    properties.put("property2", prop2);
    
    RecordType recordTypeA = new RecordType(registry, properties);
    RecordType recordTypeB = new RecordType(registry, new HashMap<>());
    
    JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
    
    assertNotNull(greatestSubtype);
  }

  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordProperty prop1 = new RecordProperty(new JSType(), new Node());
    RecordProperty prop2 = new RecordProperty(new JSType(), new Node());
    
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("property1", prop1);
    properties.put("property2", prop2);
    
    RecordType recordTypeA = new RecordType(registry, properties);
    RecordType recordTypeB = new RecordType(registry, properties);
    
    boolean result = recordTypeA.isEquivalentTo(recordTypeB);
    
    assertTrue(result);
  }
}