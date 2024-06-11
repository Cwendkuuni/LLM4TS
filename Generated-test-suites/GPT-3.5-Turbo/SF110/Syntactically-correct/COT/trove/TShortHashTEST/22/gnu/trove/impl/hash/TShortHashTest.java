

import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {
  
  @Test
  public void testContains() {
    TShortHash instance = new TShortHash();
    assertFalse(instance.contains((short)0));
    
    instance.insertKey((short)0);
    assertTrue(instance.contains((short)0));
  }
  
  @Test
  public void testForEach() {
    TShortHash instance = new TShortHash();
    instance.insertKey((short)1);
    instance.insertKey((short)2);
    instance.insertKey((short)3);
    
    TShortProcedure procedure = new TShortProcedure() {
      int sum = 0;
      
      public boolean execute(short value) {
        sum += value;
        return true;
      }
      
      public int getTotalSum() {
        return sum;
      }
    };
    
    assertTrue(instance.forEach(procedure));
    assertEquals(6, procedure.getTotalSum());
  }
  
  @Test
  public void testRemoveAt() {
    TShortHash instance = new TShortHash();
    instance.insertKey((short)1);
    instance.insertKey((short)2);
    instance.insertKey((short)3);
    
    instance.removeAt(1);
    assertFalse(instance.contains((short)2));
  }
  
  @Test
  public void testInsertKey() {
    TShortHash instance = new TShortHash();
    int index = instance.insertKey((short)1);
    assertFalse(instance.consumeFreeSlot);
    assertEquals(0, index);
    
    index = instance.insertKey((short)2);
    assertFalse(instance.consumeFreeSlot);
    assertEquals(1, index);
    
    index = instance.insertKey((short)1);
    assertFalse(instance.consumeFreeSlot);
    assertEquals(-1, index);
  }
}
