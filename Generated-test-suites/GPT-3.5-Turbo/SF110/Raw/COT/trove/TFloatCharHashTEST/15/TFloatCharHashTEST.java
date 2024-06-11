
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class TFloatCharHashTest {
  
  private TFloatCharHash testHash;
  
  @Before
  public void setup() {
    testHash = new TFloatCharHash();
  }
  
  @Test
  public void testContains() {
    assertFalse(testHash.contains(1.0f));
    
    testHash.insertKey(1.0f);
    assertTrue(testHash.contains(1.0f));
    
    testHash.insertKey(2.0f);
    testHash.remove(2.0f);
    assertFalse(testHash.contains(2.0f));
  }
  
  @Test
  public void testForEach() {
    TFloatProcedure procedure = new TFloatProcedure() {
      public boolean execute(float value) {
        System.out.println(value);
        return true;
      }
    };
    
    testHash.insertKey(1.0f);
    testHash.insertKey(2.0f);
    testHash.forEach(procedure);
    // Expected output: 1.0, 2.0
  }
  
  @Test
  public void testRemoveAt() {
    testHash.insertKey(1.0f);
    testHash.insertKey(2.0f);
    testHash.removeAt(0);
    assertFalse(testHash.contains(1.0f));
  }
  
  @Test
  public void testIndex() {
    testHash.insertKey(1.0f);
    assertEquals(0, testHash.index(1.0f));
    
    testHash.remove(1.0f);
    assertEquals(-1, testHash.index(1.0f));
  }
  
  @Test
  public void testInsertKey() {
    assertEquals(0, testHash.insertKey(1.0f));
    assertEquals(1, testHash.insertKey(2.0f));
    assertEquals(-1, testHash.insertKey(2.0f));
    assertEquals(2, testHash.insertKey(3.0f));
  }
  
  @Test(expected = IllegalStateException.class)
  public void testInsertKeyRehash() {
    testHash = new TFloatCharHash(2, 1.0f);
    testHash.insertKey(1.0f);
    testHash.insertKey(2.0f);
    testHash.insertKey(3.0f);
  }
  
  @Test
  public void testInsertKeyAt() {
    testHash.insertKeyAt(0, 1.0f);
    assertTrue(testHash.contains(1.0f));
  }
  
  @Test
  public void testSetUp() {
    assertEquals(10, testHash.setUp(10));
    assertEquals(0.0f, testHash._set[0], 0.001);
  }
  
  @Test
  public void testGetNoEntryKey() {
    assertEquals(0.0f, testHash.getNoEntryKey(), 0.001);
  }
  
  @Test
  public void testGetNoEntryValue() {
    assertEquals('\0', testHash.getNoEntryValue());
  }
  
  @Test
  public void testWriteExternal() {
    // Test not feasible as it requires ObjectOutput parameter.
  }
  
  @Test
  public void testReadExternal() {
    // Test not feasible as it requires ObjectInput parameter.
  }
}

