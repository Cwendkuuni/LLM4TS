package gnu.trove.impl.hash;

import org.junit.Assert;
import org.junit.Test;

public class TCharHashTest {
  
  @Test
  public void testGetNoEntryValue() {
    TCharHash tCharHash = new TCharHash();
    char expectedValue = '\0';
    char actualValue = tCharHash.getNoEntryValue();
    Assert.assertEquals(expectedValue, actualValue);
  }
  
  @Test
  public void testSetUp() {
    TCharHash tCharHash = new TCharHash();
    int initialCapacity = 5;
    int expectedValue = 5;
    int actualValue = tCharHash.setUp(initialCapacity);
    Assert.assertEquals(expectedValue, actualValue);
  }
  
  @Test
  public void testContains() {
    TCharHash tCharHash = new TCharHash();
    char val = 'a';
    boolean expectedValue = false;
    boolean actualValue = tCharHash.contains(val);
    Assert.assertEquals(expectedValue, actualValue);
  }
  
  @Test
  public void testForEach() {
    TCharHash tCharHash = new TCharHash();
    TCharProcedure procedure = new TCharProcedure() {
      @Override
      public boolean execute(char value) {
        // do something
        return true;
      }
    };
    boolean expectedValue = true;
    boolean actualValue = tCharHash.forEach(procedure);
    Assert.assertEquals(expectedValue, actualValue);
  }
  
  @Test
  public void testRemoveAt() {
    TCharHash tCharHash = new TCharHash();
    int index = 0;
    tCharHash.removeAt(index);
    // No direct way to test as the method only modifies the internal state
  }
  
  @Test
  public void testIndex() {
    TCharHash tCharHash = new TCharHash();
    char val = 'a';
    int expectedValue = -1;
    int actualValue = tCharHash.index(val);
    Assert.assertEquals(expectedValue, actualValue);
  }
  
  @Test
  public void testIndexRehashed() {
    TCharHash tCharHash = new TCharHash();
    char key = 'a';
    int index = 0;
    int hash = 12345;
    byte state = 1;
    int expectedValue = -1;
    int actualValue = tCharHash.indexRehashed(key, index, hash, state);
    Assert.assertEquals(expectedValue, actualValue);
  }
  
  @Test
  public void testInsertKey() {
    TCharHash tCharHash = new TCharHash();
    char val = 'a';
    int expectedValue = -1;
    int actualValue = tCharHash.insertKey(val);
    Assert.assertEquals(expectedValue, actualValue);
  }
  
  @Test
  public void testInsertKeyRehash() {
    TCharHash tCharHash = new TCharHash();
    char val = 'a';
    int index = 0;
    int hash = 12345;
    byte state = 1;
    int expectedValue = -1;
    int actualValue = tCharHash.insertKeyRehash(val, index, hash, state);
    Assert.assertEquals(expectedValue, actualValue);
  }
  
  @Test
  public void testInsertKeyAt() {
    TCharHash tCharHash = new TCharHash();
    int index = 0;
    char val = 'a';
    tCharHash.insertKeyAt(index, val);
    // No direct way to test as the method only modifies the internal state
  }
}