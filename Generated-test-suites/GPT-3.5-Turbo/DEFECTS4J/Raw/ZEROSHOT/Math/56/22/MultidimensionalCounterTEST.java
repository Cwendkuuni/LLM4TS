import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.util.MathUtils;

public class MultidimensionalCounterTest {
  
  private MultidimensionalCounter counter;
  
  @Before
  public void setUp() {
    counter = new MultidimensionalCounter(2, 4, 3);
  }
  
  @Test
  public void testGetDimension() {
    assertEquals(3, counter.getDimension());
  }
  
  @Test(expected = NotStrictlyPositiveException.class)
  public void testInvalidSizeConstructor() {
    MultidimensionalCounter invalidCounter = new MultidimensionalCounter(0);
  }
  
  @Test
  public void testGetCounts() {
    int[] indices1 = counter.getCounts(0);
    assertEquals(0, indices1[0]);
    assertEquals(0, indices1[1]);
    assertEquals(0, indices1[2]);
    
    int[] indices2 = counter.getCounts(1);
    assertEquals(0, indices2[0]);
    assertEquals(0, indices2[1]);
    assertEquals(1, indices2[2]);
    
    int[] indices3 = counter.getCounts(12);
    assertEquals(1, indices3[0]);
    assertEquals(0, indices3[1]);
    assertEquals(0, indices3[2]);
    
    int[] indices4 = counter.getCounts(23);
    assertEquals(1, indices4[0]);
    assertEquals(3, indices4[1]);
    assertEquals(2, indices4[2]);
  }
  
  @Test(expected = OutOfRangeException.class)
  public void testInvalidGetCounts() {
    int[] indices = counter.getCounts(24);
  }
  
  @Test
  public void testGetCount() {
    int index1 = counter.getCount(0, 0, 0);
    assertEquals(0, index1);
    
    int index2 = counter.getCount(0, 0, 1);
    assertEquals(1, index2);
    
    int index3 = counter.getCount(0, 0, 2);
    assertEquals(2, index3);
    
    int index4 = counter.getCount(0, 1, 0);
    assertEquals(3, index4);
    
    int index5 = counter.getCount(1, 0, 0);
    assertEquals(12, index5);
    
    int index6 = counter.getCount(1, 3, 2);
    assertEquals(23, index6);
  }
  
  @Test(expected = DimensionMismatchException.class)
  public void testInvalidGetCount() {
    int index = counter.getCount(0, 0);
  }
  
  @Test(expected = OutOfRangeException.class)
  public void testGetCountOutofRange() {
    int index = counter.getCount(0, 0, 5);
  }
  
  @Test
  public void testGetSize() {
    assertEquals(24, counter.getSize());
  }
  
  @Test
  public void testGetSizes() {
    int[] sizes = counter.getSizes();
    assertEquals(2, sizes[0]);
    assertEquals(4, sizes[1]);
    assertEquals(3, sizes[2]);
  }
  
  @Test
  public void testToString() {
    assertEquals("[0][0][0]", counter.toString());
  }
}