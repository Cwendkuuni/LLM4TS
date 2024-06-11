import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    // create a map of properties
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordProperty(TYPE_1, propertyNode1));
    properties.put("b", new RecordProperty(TYPE_2, propertyNode2));
    
    RecordType record = new RecordType(registry, properties);
    
    assertNotNull(record);
    assertTrue(record.isFrozen());
  }
  
  @Test
  public void testIsEquivalentTo() {
    RecordType record1 = createRecordType("a", TYPE_1);
    RecordType record2 = createRecordType("b", TYPE_2, "a", TYPE_1);
    RecordType record3 = createRecordType("a", TYPE_3);
    
    assertTrue(record1.isEquivalentTo(record1));
    assertFalse(record1.isEquivalentTo(record2));
    assertFalse(record2.isEquivalentTo(record3));
  }
  
  @Test
  public void testIsSubtype() {
    RecordType record1 = createRecordType("a", TYPE_1);
    RecordType record2 = createRecordType("b", TYPE_2, "a", TYPE_1);
    RecordType record3 = createRecordType("a", TYPE_3);
    
    assertTrue(record2.isSubtype(record1));
    assertFalse(record3.isSubtype(record2));
  }
  
  // additional test cases for other methods can be added here
  
  private RecordType createRecordType(String property1, JSType type1, String... otherProperties) {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put(property1, new RecordProperty(type1, propertyNode1));
    
    for (int i = 0; i < otherProperties.length; i += 2) {
      properties.put(otherProperties[i], new RecordProperty(otherProperties[i+1], propertyNode2));
    }
    
    return new RecordType(registry, properties);
  }

}