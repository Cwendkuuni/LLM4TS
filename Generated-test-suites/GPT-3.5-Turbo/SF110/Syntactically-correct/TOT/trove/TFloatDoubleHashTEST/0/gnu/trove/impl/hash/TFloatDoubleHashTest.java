
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TFloatDoubleHashTest {
  
  private TFloatDoubleHash hash;

  @Before
  public void setup() {
    hash = new TFloatDoubleHash();
  }
  
  @Test
  public void testGetNoEntryKey() {
    assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
  }
  
  @Test
  public void testGetNoEntryValue() {
    assertEquals(0.0, hash.getNoEntryValue(), 0.0001);
  }
  
  @Test
  public void testContains_shouldReturnTrueWhenValueExists() {
    hash.insertKey(1.0f);
    assertTrue(hash.contains(1.0f));
  }
  
  @Test
  public void testContains_shouldReturnFalseWhenValueDoesNotExist() {
    assertFalse(hash.contains(1.0f));
  }
  
  @Test
  public void testForEach_shouldReturnTrueWhenAllValuesProcessed() {
    hash.insertKey(1.0f);
    assertTrue(hash.forEach((value) -> true));
  }
  
  @Test
  public void testForEach_shouldReturnFalseWhenBreakExecuted() {
    hash.insertKey(1.0f);
    assertFalse(hash.forEach((value) -> false));
  }
  
  @Test
  public void testRemoveAt_shouldRemoveKeyAtSpecifiedIndex() {
    hash.insertKey(1.0f);
    assertEquals(1, hash.removeAt(0));
    assertFalse(hash.contains(1.0f));
  }
  
  @Test
  public void testInsertKey_shouldInsertKeyIntoHashTable() {
    int index = hash.insertKey(1.0f);
    assertTrue(hash.contains(1.0f));
    assertEquals(0, index);
  }
  
  @Test
  public void testInsertKey_shouldReturnNegativeIndexWhenKeyAlreadyExists() {
    hash.insertKey(1.0f);
    int index = hash.insertKey(1.0f);
    assertFalse(hash.contains(2.0f));
    assertTrue(index < 0);
  }
  
  @Test
  public void testInsertKey_shouldHandleCollisionAndInsertKey() {
    hash.insertKey(1.0f);
    hash.insertKey(2.0f);
    int index = hash.insertKey(3.0f);
    assertTrue(hash.contains(3.0f));
    assertTrue(index >= 0);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testInsertKey_shouldThrowExceptionWhenHashTableIsFull() {
    for (int i = 0; i < 100; i++) {
      hash.insertKey(i);
    }
    hash.insertKey(100.0f);
  }
}
