import static org.junit.Assert.*;
import org.junit.Test;

public class TFloatDoubleHashTest {

  @Test
  public void testGetNoEntryKey() {
    TFloatDoubleHash hash = new TFloatDoubleHash();
    assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
  }

  @Test
  public void testGetNoEntryValue() {
    TFloatDoubleHash hash = new TFloatDoubleHash();
    assertEquals(0.0, hash.getNoEntryValue(), 0.001);
  }

  @Test
  public void testSetUp() {
    TFloatDoubleHash hash = new TFloatDoubleHash();
    int capacity = 10;
    hash.setUp(capacity);
    assertEquals(capacity, hash._set.length);
  }

  @Test
  public void testContains() {
    TFloatDoubleHash hash = new TFloatDoubleHash();
    float key = 1.0f;
    hash.insertKey(key);
    assertTrue(hash.contains(key));
  }

  @Test
  public void testContains_NotFound() {
    TFloatDoubleHash hash = new TFloatDoubleHash();
    float key = 1.0f;
    assertFalse(hash.contains(key));
  }

  @Test
  public void testForEach() {
    TFloatDoubleHash hash = new TFloatDoubleHash();
    float key1 = 1.0f;
    float key2 = 2.0f;
    hash.insertKey(key1);
    hash.insertKey(key2);
  
    // Test procedure that sums up all the keys
    TFloatProcedure procedure = new TFloatProcedure() {
      float sum = 0.0f;
  
      public boolean execute(float value) {
        sum += value;
        return true;
      }
    };
  
    assertTrue(hash.forEach(procedure));
    assertEquals(key1 + key2, procedure.sum, 0.001);
  }

  @Test
  public void testRemoveAt() {
    TFloatDoubleHash hash = new TFloatDoubleHash();
    float key = 1.0f;
    hash.insertKey(key);
  
    int index = hash.index(key);
    hash.removeAt(index);
  
    assertFalse(hash.contains(key));
  }

  @Test(expected=IllegalStateException.class)
  public void testInsertKey_FullSet() {
    TFloatDoubleHash hash = new TFloatDoubleHash(2);
    float key1 = 1.0f;
    float key2 = 2.0f;
    float key3 = 3.0f;
  
    hash.insertKey(key1);
    hash.insertKey(key2);
    hash.insertKey(key3); // Should throw IllegalStateException
  }

  @Test
  public void testInsertKey() {
    TFloatDoubleHash hash = new TFloatDoubleHash(2);
    float key1 = 1.0f;
    float key2 = 2.0f;
  
    int index1 = hash.insertKey(key1);
    int index2 = hash.insertKey(key2);
  
    assertEquals(key1, hash._set[index1], 0.001);
    assertEquals(key2, hash._set[index2], 0.001);
  }

  @Test
  public void testXinsertKey() {
    TFloatDoubleHash hash = new TFloatDoubleHash(2);
    float key1 = 1.0f;
    float key2 = 2.0f;
  
    int index1 = hash.XinsertKey(key1);
    int index2 = hash.XinsertKey(key2);
  
    assertEquals(key1, hash._set[index1], 0.001);
    assertEquals(key2, hash._set[index2], 0.001);
  }
}