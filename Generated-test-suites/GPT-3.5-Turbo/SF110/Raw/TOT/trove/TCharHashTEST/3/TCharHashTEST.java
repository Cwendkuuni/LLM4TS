import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TCharHashTest {
  
  private TCharHash charHash;
  
  @Before
  public void setUp() {
    charHash = new TCharHash();
  }
  
  @Test
  public void testContains() {
    assertFalse(charHash.contains('a')); // Test when char not present in hash
    
    charHash.insertKey('a');
    assertTrue(charHash.contains('a')); // Test when char is present in hash
  }
  
  @Test
  public void testForEach() {
    charHash.insertKey('a');
    charHash.insertKey('b');
    charHash.insertKey('c');
    
    StringBuilder sb = new StringBuilder();
    TCharProcedure procedure = new TCharProcedure() {
      @Override
      public boolean execute(char value) {
        sb.append(value);
        return true;
      }
    };
    
    assertTrue(charHash.forEach(procedure)); // Test when procedure returns true
    assertEquals("abc", sb.toString()); // Verify if the procedure is executed for all chars
    
    TCharProcedure returnFalseProcedure = new TCharProcedure() {
      @Override
      public boolean execute(char value) {
        return false;
      }
    };
    
    assertFalse(charHash.forEach(returnFalseProcedure)); // Test when procedure returns false
  }
  
  @Test
  public void testRemoveAt() {
    charHash.insertKey('a');
    charHash.insertKey('b');
    
    assertTrue(charHash.contains('a'));
    assertTrue(charHash.contains('b'));
    
    charHash.removeAt(0);
    assertFalse(charHash.contains('a')); // Test removing the first element
    assertTrue(charHash.contains('b')); // Verify the other element is still present
    
    charHash.removeAt(0);
    assertFalse(charHash.contains('b')); // Test removing the last element
  }
  
  @Test
  public void testIndex() {
    assertEquals(-1, charHash.index('a')); // Test for empty hash
    
    charHash.insertKey('a');
    assertEquals(0, charHash.index('a')); // Test for a single element in hash
    
    charHash.insertKey('b');
    assertEquals(1, charHash.index('b')); // Test for multiple elements in hash
    
    charHash.removeAt(0);
    assertEquals(-1, charHash.index('a')); // Test for removed element
    
    charHash.insertKey('a');
    assertEquals(0, charHash.index('a')); // Test for re-inserted element
  }
  
  @Test
  public void testInsertKey() {
    assertEquals(0, charHash.insertKey('a')); // Test inserting first element
    assertEquals(1, charHash.insertKey('b')); // Test inserting second element
    assertEquals(-1, charHash.insertKey('a')); // Test inserting already existing element
    
    charHash.removeAt(0);
    assertEquals(0, charHash.insertKey('a')); // Test inserting element after removal
    
    try {
      for (int i = 0; i < 10; i++) {
        charHash.insertKey((char) i);
      }
      fail("Expected IllegalStateException to be thrown"); // Verify if exception is thrown when full
    } catch (IllegalStateException e) {
      // Exception caught as expected
    }
  }
  
  @Test
  public void testSetUp() {
    assertEquals(10, charHash.setUp(10)); // Test setting up hash with initial capacity
    
    charHash.insertKey('a');
    assertEquals(0, charHash.setUp(5)); // Test setting up with a smaller capacity when elements present
    
    charHash.removeAt(0);
    assertEquals(10, charHash.setUp(8)); // Test setting up with a larger capacity after removal
  }
  
}