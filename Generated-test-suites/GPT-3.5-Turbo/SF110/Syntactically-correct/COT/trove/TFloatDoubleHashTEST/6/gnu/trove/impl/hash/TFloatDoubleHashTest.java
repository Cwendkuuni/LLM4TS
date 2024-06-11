
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {
  
  @Test
  public void testContains() {
    TFloatDoubleHash hash = new TFloatDoubleHash();
    assertFalse(hash.contains(0.0f));
    
    hash.insertKey(0.0f);
    assertTrue(hash.contains(0.0f));
    
    hash.insertKey(1.0f);
    assertFalse(hash.contains(2.0f));
  }
  
  @Test
  public void testForEach() {
    TFloatDoubleHash hash = new TFloatDoubleHash();
    TFloatProcedure procedure = new TFloatProcedure() {
      public boolean execute(float val) {
        System.out.println(val);
        return true;
      }
    };
    
    hash.insertKey(0.0f);
    hash.insertKey(1.0f);
    assertTrue(hash.forEach(procedure));
  }
  
  @Test
  public void testRemoveAt() {
    TFloatDoubleHash hash = new TFloatDoubleHash();
    hash.insertKey(0.0f);
    hash.insertKey(1.0f);
    hash.insertKey(2.0f);
    
    assertEquals(3, hash.size());
    
    hash.removeAt(1);
    
    assertEquals(2, hash.size());
    assertFalse(hash.contains(1.0f));
  }
  
  @Test
  public void testInsertKey() {
    TFloatDoubleHash hash = new TFloatDoubleHash();
    hash.insertKey(0.0f);
    hash.insertKey(1.0f);
    hash.insertKey(2.0f);
    
    assertEquals(3, hash.size());
    assertTrue(hash.contains(0.0f));
    assertTrue(hash.contains(1.0f));
    assertTrue(hash.contains(2.0f));
  }
}
