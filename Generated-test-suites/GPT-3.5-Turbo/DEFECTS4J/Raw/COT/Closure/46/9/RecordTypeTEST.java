import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.jstype.RecordType;

public class RecordTypeTest {

  @Test
  public void testIsEquivalentTo() {
    RecordType record1 = new RecordType();
    RecordType record2 = new RecordType();
    
    // Test equivalence of two empty record types
    assertTrue(record1.isEquivalentTo(record2));
    
    // Test equivalence of two record types with same properties
    record1.addProperty("a", new JSType());
    record2.addProperty("a", new JSType());
    assertTrue(record1.isEquivalentTo(record2));
    
    // Test equivalence of two record types with different properties
    record2.addProperty("b", new JSType());
    assertFalse(record1.isEquivalentTo(record2));
  }
  
  @Test
  public void testIsSubtype() {
    RecordType record1 = new RecordType();
    RecordType record2 = new RecordType();
    
    // Test subtyping of two empty record types
    assertTrue(record1.isSubtype(record2));
    assertFalse(record2.isSubtype(record1));
    
    // Test subtyping of two record types with same properties
    record1.addProperty("a", new JSType());
    record2.addProperty("a", new JSType());
    assertTrue(record1.isSubtype(record2));
    assertFalse(record2.isSubtype(record1));
    
    // Test subtyping of two record types with different properties
    record2.addProperty("b", new JSType());
    assertFalse(record1.isSubtype(record2));
    assertFalse(record2.isSubtype(record1));
  }
  
  @Test
  public void testGetLeastSupertype() {
    RecordType record1 = new RecordType();
    RecordType record2 = new RecordType();
    RecordType record3 = new RecordType();
    
    // Test least supertype of two empty record types
    assertTrue(record1.getLeastSupertype(record2).isEmptyType());
    assertTrue(record2.getLeastSupertype(record1).isEmptyType());
    
    // Test least supertype of two record types with same properties
    record1.addProperty("a", new JSType());
    record2.addProperty("a", new JSType());
    assertTrue(record1.getLeastSupertype(record2).isEquivalentTo(record1));
    assertTrue(record2.getLeastSupertype(record1).isEquivalentTo(record1));
    
    // Test least supertype of two record types with different properties
    record2.addProperty("b", new JSType());
    assertTrue(record1.getLeastSupertype(record2).isEquivalentTo(record1));
    assertTrue(record2.getLeastSupertype(record1).isEquivalentTo(record1));
    
    // Test least supertype of two record types with overlapping properties
    record3.addProperty("a", new JSType());
    record3.addProperty("c", new JSType());
    assertTrue(record1.getLeastSupertype(record3).isEquivalentTo(record3));
    assertTrue(record3.getLeastSupertype(record1).isEquivalentTo(record3));
  }
  
  @Test
  public void testHasProperty() {
    RecordType record = new RecordType();
    
    // Test hasProperty method for empty record type
    assertFalse(record.hasProperty("a"));
    
    // Test hasProperty method for record type with properties
    record.addProperty("a", new JSType());
    assertTrue(record.hasProperty("a"));
    assertFalse(record.hasProperty("b"));
  }
  
  @Test
  public void testGetPropertyType() {
    RecordType record = new RecordType();
    
    // Test getPropertyType method for empty record type
    assertNull(record.getPropertyType("a"));
    
    // Test getPropertyType method for record type with properties
    JSType type = new JSType();
    record.addProperty("a", type);
    assertEquals(type, record.getPropertyType("a"));
  }

}