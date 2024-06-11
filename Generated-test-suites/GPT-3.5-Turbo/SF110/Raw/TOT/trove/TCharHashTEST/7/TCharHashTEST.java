import org.junit.*;
import static org.junit.Assert.*;

public class TCharHashTest {
  
  // Test case for constructor TCharHash()
  @Test
  public void testTCharHashConstructor() {
    TCharHash tCharHash = new TCharHash();
    assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
  }
  
  // Test case for constructor TCharHash(int initialCapacity)
  @Test
  public void testTCharHashConstructorWithInitialCapacity() {
    int initialCapacity = 5;
    TCharHash tCharHash = new TCharHash(initialCapacity);
    assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
  }
  
  // Test case for constructor TCharHash(int initialCapacity, float loadFactor)
  @Test
  public void testTCharHashConstructorWithInitialCapacityAndLoadFactor() {
    int initialCapacity = 5;
    float loadFactor = 0.8f;
    TCharHash tCharHash = new TCharHash(initialCapacity, loadFactor);
    assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
  }
  
  // Test case for constructor TCharHash(int initialCapacity, float loadFactor, char no_entry_value)
  @Test
  public void testTCharHashConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
    int initialCapacity = 5;
    float loadFactor = 0.8f;
    char no_entry_value = 'X';
    TCharHash tCharHash = new TCharHash(initialCapacity, loadFactor, no_entry_value);
    assertEquals(no_entry_value, tCharHash.getNoEntryValue());
  }
  
  // Test case for contains(char val) - val exists in the hash
  @Test
  public void testContainsWhenValueExists() {
    TCharHash tCharHash = new TCharHash(5);
    tCharHash.insertKey('A');
    assertTrue(tCharHash.contains('A'));
  }
  
  // Test case for contains(char val) - val does not exist in the hash
  @Test
  public void testContainsWhenValueDoesNotExist() {
    TCharHash tCharHash = new TCharHash(5);
    assertFalse(tCharHash.contains('B'));
  }
  
  // Test case for forEach(TCharProcedure procedure) - execute procedure for every element
  @Test
  public void testForEachWithValidProcedure() {
    TCharHash tCharHash = new TCharHash(5);
    tCharHash.insertKey('A');
    tCharHash.insertKey('B');
    tCharHash.insertKey('C');
    TCharProcedure procedure = new TCharProcedure() {
      @Override
      public boolean execute(char value) {
        System.out.println(value);
        return true;
      }
    };
    assertTrue(tCharHash.forEach(procedure));
  }
  
  // Test case for forEach(TCharProcedure procedure) - stop executing procedure
  @Test
  public void testForEachWithInvalidProcedure() {
    TCharHash tCharHash = new TCharHash(5);
    tCharHash.insertKey('A');
    tCharHash.insertKey('B');
    tCharHash.insertKey('C');
    TCharProcedure procedure = new TCharProcedure() {
      @Override
      public boolean execute(char value) {
        if (value == 'B') {
          return false;
        }
        System.out.println(value);
        return true;
      }
    };
    assertFalse(tCharHash.forEach(procedure));
  }
  
  // Test case for removeAt(int index)
  @Test
  public void testRemoveAt() {
    TCharHash tCharHash = new TCharHash(5);
    tCharHash.insertKey('A');
    tCharHash.removeAt(0);
    assertFalse(tCharHash.contains('A'));
  }
  
  // Test case for index(char val) - val exists in the hash
  @Test
  public void testIndexWhenValueExists() {
    TCharHash tCharHash = new TCharHash(5);
    tCharHash.insertKey('A');
    assertEquals(0, tCharHash.index('A'));
  }
  
  // Test case for index(char val) - val does not exist in the hash
  @Test
  public void testIndexWhenValueDoesNotExist() {
    TCharHash tCharHash = new TCharHash(5);
    assertEquals(-1, tCharHash.index('B'));
  }
  
  // Test case for insertKey(char val) - val does not exist in the hash and no free slots available
  @Test(expected = IllegalStateException.class)
  public void testInsertKeyWhenValueDoesNotExistAndNoFreeSlots() {
    TCharHash tCharHash = new TCharHash(5);
    tCharHash.insertKey('A');
    tCharHash.insertKey('B');
    tCharHash.insertKey('C');
    tCharHash.insertKey('D');
    tCharHash.insertKey('E');
    tCharHash.insertKey('F');
  }
  
  // Test case for insertKey(char val) - val does not exist in the hash and free slot available
  @Test
  public void testInsertKeyWhenValueDoesNotExistAndFreeSlots() {
    TCharHash tCharHash = new TCharHash(5);
    tCharHash.insertKey('A');
    tCharHash.insertKey('B');
    tCharHash.removeAt(1);
    tCharHash.insertKey('C');
    assertTrue(tCharHash.contains('C'));
  }
  
  // Test case for insertKey(char val) - val already exists in the hash
  @Test
  public void testInsertKeyWhenValueExists() {
    TCharHash tCharHash = new TCharHash(5);
    tCharHash.insertKey('A');
    assertEquals(-1, tCharHash.insertKey('A'));
  }
  
  // Test case for insertKeyRehash(char val, int index, int hash, byte state)
  @Test
  public void testInsertKeyRehash() {
    TCharHash tCharHash = new TCharHash(5);
    tCharHash.insertKey('A');
    tCharHash.insertKey('B');
    tCharHash.insertKey('C');
    tCharHash.removeAt(0);
    tCharHash.insertKey('D');
    assertTrue(tCharHash.contains('D'));
  }
  
  // Test case for insertKeyAt(int index, char val)
  @Test
  public void testInsertKeyAt() {
    TCharHash tCharHash = new TCharHash(5);
    tCharHash.insertKeyAt(0, 'A');
    assertTrue(tCharHash.contains('A'));
  }
  
}