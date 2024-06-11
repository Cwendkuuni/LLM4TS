import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeTest {
  
  @Test
  public void testConstructor() {
    // Typical use case
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordProperty(TYPE_1, node));
    RecordType recordType = new RecordType(registry, properties);
    
    // Edge case: empty properties map
    try {
      RecordType emptyRecordType = new RecordType(registry, new HashMap<>());
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected exception
    }
  }
  
  @Test
  public void testIsEquivalentTo() {
    // Typical use case
    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);
    assertTrue(recordType1.isEquivalentTo(recordType2));
    
    // Edge case: properties not matching
    assertFalse(recordType1.isEquivalentTo(recordType3));
  }
  
  @Test
  public void testGetLeastSupertype() {
    // Typical use case
    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);
    JSType leastSupertype = recordType1.getLeastSupertype(recordType2);
    assertEquals(expectedLeastSupertype, leastSupertype);
    
    // Edge case: no common properties
    JSType leastSupertypeNoCommonProps = recordType1.getLeastSupertype(recordType3);
    assertEquals(expectedLeastSupertypeNoCommonProps, leastSupertypeNoCommonProps);
  }
  
  @Test
  public void testGetGreatestSubtypeHelper() {
    // Typical use case
    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(otherType);
    assertEquals(expectedGreatestSubtype, greatestSubtype);
    
    // Edge case: otherType is NO_TYPE
    JSType greatestSubtypeNoType = recordType1.getGreatestSubtypeHelper(NO_TYPE);
    assertEquals(NO_TYPE, greatestSubtypeNoType);
  }
  
  @Test
  public void testIsSubtype() {
    // Typical use case
    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);
    assertTrue(recordType1.isSubtype(recordType2));
    
    // Edge case: not a record type
    ObjectType objectType = new ObjectType(registry);
    assertFalse(recordType1.isSubtype(objectType));
  }
}