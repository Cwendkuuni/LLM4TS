import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeConstructor() {
    // Test RecordType constructor with valid properties
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordProperty(TYPE_1, propertyNode));
    properties.put("b", new RecordProperty(TYPE_2, propertyNode));
    
    RecordType recordType = new RecordType(registry, properties);
    
    assertNotNull(recordType);
  }
  
  @Test
  public void testIsEquivalentTo() {
    // Test isEquivalentTo method with equivalent record types
    RecordType recordType1 = createRecordType1();
    RecordType recordType2 = createRecordType1();
    
    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testDefineProperty() {
    // Test defineProperty method with a new property
    RecordType recordType = createRecordType1();
    boolean result = recordType.defineProperty("c", TYPE_3, false, propertyNode);
    
    assertTrue(result);
  }
  
  @Test
  public void testGetLeastSupertype() {
    // Test getLeastSupertype method with compatible record types
    RecordType recordType1 = createRecordType1();
    RecordType recordType2 = createRecordType2();
    
    JSType leastSupertype = recordType1.getLeastSupertype(recordType2);
    
    assertNotNull(leastSupertype);
  }
  
  @Test
  public void testGetGreatestSubtypeHelper() {
    // Test getGreatestSubtypeHelper method with compatible record types
    RecordType recordType1 = createRecordType1();
    RecordType recordType2 = createRecordType2();
    
    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    
    assertNotNull(greatestSubtype);
  }

  @Test
  public void testIsSubtype() {
    // Test isSubtype method with a subtype record type
    RecordType recordType1 = createRecordType1();
    RecordType recordType2 = createRecordType2();
    
    assertTrue(RecordType.isSubtype(recordType1, recordType2));
  }

  private RecordType createRecordType1() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordProperty(TYPE_1, propertyNode));
    properties.put("b", new RecordProperty(TYPE_2, propertyNode));
    
    return new RecordType(registry, properties);
  }

  private RecordType createRecordType2() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordProperty(TYPE_1, propertyNode));
    
    return new RecordType(registry, properties);
  }
}