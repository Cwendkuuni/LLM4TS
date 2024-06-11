import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testIsEquivalentTo() {
    // Test method functionality
    RecordType record1 = new RecordType();
    RecordType record2 = new RecordType();
    
    record1.addProperty("a", JSType.STRING, null);
    record1.addProperty("b", JSType.NUMBER, null);
    
    record2.addProperty("a", JSType.STRING, null);
    record2.addProperty("b", JSType.NUMBER, null);
    
    assertTrue(record1.isEquivalentTo(record2));
    assertTrue(record2.isEquivalentTo(record1));
  }

  @Test
  public void testGetImplicitPrototype() {
    // Test method functionality
    RecordType record = new RecordType();
    
    assertEquals(JSType.OBJECT_TYPE, record.getImplicitPrototype());
  }

  @Test
  public void testDefineProperty() {
    // Test method functionality
    RecordType record = new RecordType();
    
    assertTrue(record.defineProperty("a", JSType.STRING, false, null));
    assertEquals(JSType.STRING, record.getPropertyType("a"));
    assertFalse(record.defineProperty("a", JSType.NUMBER, false, null));
    assertEquals(JSType.STRING, record.getPropertyType("a"));
  }

  @Test
  public void testGetLeastSupertype() {
    // Test method functionality
    RecordType record1 = new RecordType();
    
    record1.addProperty("a", JSType.STRING, null);
    record1.addProperty("b", JSType.NUMBER, null);
    
    RecordType record2 = new RecordType();
    
    record2.addProperty("a", JSType.STRING, null);
    record2.addProperty("b", JSType.NUMBER, null);
    
    RecordType result = record1.getLeastSupertype(record2);
    
    assertEquals(record1, result);
    assertEquals(record2, result);
  }
  
  @Test
  public void testGetGreatestSubtypeHelper() {
    // Test method functionality
    RecordType record1 = new RecordType();
    
    record1.addProperty("a", JSType.STRING, null);
    
    RecordType record2 = new RecordType();
    
    record2.addProperty("a", JSType.STRING, null);
    record2.addProperty("b", JSType.NUMBER, null);
    
    RecordType result = record2.getGreatestSubtypeHelper(record1);
    
    assertTrue(result.hasProperty("a"));
    assertFalse(result.hasProperty("b"));
  }

  @Test
  public void testIsSubtype() {
    // Test method functionality
    RecordType record1 = new RecordType();
    
    record1.addProperty("a", JSType.STRING, null);
    record1.addProperty("b", JSType.NUMBER, null);
    
    RecordType record2 = new RecordType();
    
    record2.addProperty("a", JSType.STRING, null);
    
    assertTrue(record2.isSubtype(record1));
    assertFalse(record1.isSubtype(record2));
  }
  
  @Test
  public void testResolveInternal() {
    // Test method functionality
    RecordType record = new RecordType();
    
    record.addProperty("a", JSType.STRING, null);
    record.addProperty("b", JSType.NUMBER, null);
    
    StaticScope<JSType> scope = new StaticScope<>();
    JSType resolvedType = record.resolveInternal(null, scope);
    
    assertTrue(resolvedType.equals(record));
  }

}