import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {
  
  private TCharHash tCharHash;

  @Before
  public void setUp() {
    tCharHash = new TCharHash();
  }

  @Test
  public void testContains() {
    char val = 'a';
    tCharHash.insertKey(val);
    assertTrue(tCharHash.contains(val));
  }

  @Test
  public void testForEach() {
    tCharHash.insertKey('a');
    tCharHash.insertKey('b');
    tCharHash.insertKey('c');
    
    TCharProcedure procedure = new TCharProcedure() {
      public boolean execute(char val) {
        System.out.println("Value: " + val);
        return true;
      }
    };
    
    assertTrue(tCharHash.forEach(procedure));
  }
  
  @Test
  public void testRemoveAt() {
    char val = 'a';
    tCharHash.insertKey(val);
    tCharHash.removeAt(0);
    assertFalse(tCharHash.contains(val));
  }

  @Test
  public void testIndex() {
    char val = 'a';
    tCharHash.insertKey(val);
    assertEquals(0, tCharHash.index(val));
  }

  @Test
  public void testInsertKey() {
    char val = 'a';
    assertEquals(0, tCharHash.insertKey(val));
    assertFalse(tCharHash.contains(val));
  }

}