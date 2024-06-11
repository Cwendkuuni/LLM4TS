import org.junit.*;
import static org.junit.Assert.*;

public class TCharHashTest {

  // Test cases for the constructor TCharHash()
  @Test
  public void testConstructor1() {
    TCharHash hash = new TCharHash();
    assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
    // Verify that the _set array is filled with no_entry_value
    for (int i = 0; i < hash._set.length; i++) {
      assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash._set[i]);
    }
  }

  // Test cases for the constructor TCharHash(int initialCapacity)
  @Test
  public void testConstructor2() {
    int initialCapacity = 10;
    TCharHash hash = new TCharHash(initialCapacity);
    assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
    // Verify that the _set array is filled with no_entry_value
    for (int i = 0; i < hash._set.length; i++) {
      assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash._set[i]);
    }
    assertEquals(initialCapacity, hash.capacity());
  }

  // Test cases for the constructor TCharHash(int initialCapacity, float loadFactor)
  @Test
  public void testConstructor3() {
    int initialCapacity = 10;
    float loadFactor = 0.75f;
    TCharHash hash = new TCharHash(initialCapacity, loadFactor);
    assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
    // Verify that the _set array is filled with no_entry_value
    for (int i = 0; i < hash._set.length; i++) {
      assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash._set[i]);
    }
    assertEquals(initialCapacity, hash.capacity());
    assertEquals(loadFactor, hash.loadFactor(), 0.001f);
  }

  // Test cases for the constructor TCharHash(int initialCapacity, float loadFactor, char no_entry_value)
  @Test
  public void testConstructor4() {
    int initialCapacity = 10;
    float loadFactor = 0.75f;
    char no_entry_value = 'X';
    TCharHash hash = new TCharHash(initialCapacity, loadFactor, no_entry_value);
    assertEquals(no_entry_value, hash.getNoEntryValue());
    // Verify that the _set array is filled with no_entry_value
    for (int i = 0; i < hash._set.length; i++) {
      assertEquals(no_entry_value, hash._set[i]);
    }
    assertEquals(initialCapacity, hash.capacity());
    assertEquals(loadFactor, hash.loadFactor(), 0.001f);
  }

  // Test cases for the method contains(char val)
  @Test
  public void testContains() {
    TCharHash hash = new TCharHash();
    hash.insertKey('A');
    hash.insertKey('B');
    hash.insertKey('C');
    assertTrue(hash.contains('A')); // Testing typical use case
    assertTrue(hash.contains('B')); // Testing typical use case
    assertTrue(hash.contains('C')); // Testing typical use case
    assertFalse(hash.contains('D')); // Testing typical use case
  }

  // Test cases for the method forEach(TCharProcedure procedure)
  @Test
  public void testForEach() {
    TCharHash hash = new TCharHash();
    hash.insertKey('A');
    hash.insertKey('B');
    hash.insertKey('C');
    hash.insertKey('D');
    TCharProcedure procedure = new TCharProcedure() {
      @Override
      public boolean execute(char value) {
        System.out.println("Value: " + value);
        return true;
      }
    };
    assertTrue(hash.forEach(procedure)); // Testing typical use case
  }

  // Test cases for the method removeAt(int index)
  @Test
  public void testRemoveAt() {
    TCharHash hash = new TCharHash();
    hash.insertKey('A');
    hash.insertKey('B');
    hash.insertKey('C');
    int index = hash.index('B');
    hash.removeAt(index);
    assertFalse(hash.contains('B')); // Testing typical use case
  }

  // Test cases for the method index(char val)
  @Test
  public void testIndex() {
    TCharHash hash = new TCharHash();
    hash.insertKey('A');
    hash.insertKey('B');
    hash.insertKey('C');
    assertEquals(1, hash.index('B')); // Testing typical use case
    assertEquals(-1, hash.index('D')); // Testing typical use case
    // Add more test cases here...
  }

  // Additional test cases

  // Test case for the method insertKey(char val) where the key already exists
  @Test
  public void testInsertKey_existingKey() {
    TCharHash hash = new TCharHash();
    hash.insertKey('A');
    hash.insertKey('B');
    hash.insertKey('C');
    assertEquals(-2, hash.insertKey('B')); // Testing typical use case
  }

  // Test case for the method insertKey(char val) with no free or removed slots available
  @Test(expected = IllegalStateException.class)
  public void testInsertKey_fullSet() {
    int initialCapacity = 3;
    TCharHash hash = new TCharHash(initialCapacity);
    hash.insertKey('A');
    hash.insertKey('B');
    hash.insertKey('C');
    hash.insertKey('D'); // Testing error scenario
  }

}