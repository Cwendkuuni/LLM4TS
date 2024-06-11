import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;

public class MannWhitneyUTestTest {
  
  @Test
  public void testMannWhitneyU_WithValidData_ReturnsCorrectU() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {1, 2, 3, 4, 5};
    double[] y = {6, 7, 8, 9, 10};
    double expectedU = 25.0;
    
    try {
      double actualU = test.mannWhitneyU(x, y);
      assertEquals(expectedU, actualU, 0.0001);
    } catch (NullArgumentException | NoDataException e) {
      fail("Expected no exception to be thrown");
    }
  }

  @Test
  public void testMannWhitneyU_WithNullArguments_ThrowsNullArgumentException() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = null;
    double[] y = {1, 2, 3};
    
    assertThrows(NullArgumentException.class, () -> {
      test.mannWhitneyU(x, y);
    });
    
    assertThrows(NullArgumentException.class, () -> {
      test.mannWhitneyU(y, x);
    });
  }

  @Test
  public void testMannWhitneyU_WithEmptyArrays_ThrowsNoDataException() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {};
    double[] y = {1, 2, 3};
    
    assertThrows(NoDataException.class, () -> {
      test.mannWhitneyU(x, y);
    });
    
    assertThrows(NoDataException.class, () -> {
      test.mannWhitneyU(y, x);
    });
  }
  
  @Test
  public void testMannWhitneyUTest_WithValidData_ReturnsCorrectPValue() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {1, 2, 3, 4, 5};
    double[] y = {6, 7, 8, 9, 10};
    double expectedPValue = 0.030299819166272148;
    
    try {
      double actualPValue = test.mannWhitneyUTest(x, y);
      assertEquals(expectedPValue, actualPValue, 0.0001);
    } catch (NullArgumentException | NoDataException |
             ConvergenceException | MaxCountExceededException e) {
      fail("Expected no exception to be thrown");
    }
  } 

  @Test
  public void testMannWhitneyUTest_WithNullArguments_ThrowsNullArgumentException() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = null;
    double[] y = {1, 2, 3};
     
    assertThrows(NullArgumentException.class, () -> {
      test.mannWhitneyUTest(x, y);
    });
     
    assertThrows(NullArgumentException.class, () -> {
      test.mannWhitneyUTest(y, x);
    });
  }
 
  @Test
  public void testMannWhitneyUTest_WithEmptyArrays_ThrowsNoDataException() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {};
    double[] y = {1, 2, 3};
     
    assertThrows(NoDataException.class, () -> {
      test.mannWhitneyUTest(x, y);
    });
     
    assertThrows(NoDataException.class, () -> {
      test.mannWhitneyUTest(y, x);
    });
  }
  
  @Test
  public void testMannWhitneyUTest_WithConvergenceError_ThrowsConvergenceException() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {1, 2, 3, 4, 5};
    double[] y = {6, 7, 8, 9, 10};
     
    assertThrows(ConvergenceException.class, () -> {
      test.mannWhitneyUTest(x, y);
    });
     
    assertThrows(ConvergenceException.class, () -> {
      test.mannWhitneyUTest(y, x);
    });
  }
  
  @Test
  public void testMannWhitneyUTest_WithExceededMaxCount_ThrowsMaxCountExceededException() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {1, 2, 3, 4, 5};
    double[] y = {6, 7, 8, 9, 10};
     
    assertThrows(MaxCountExceededException.class, () -> {
      test.mannWhitneyUTest(x, y);
    });
     
    assertThrows(MaxCountExceededException.class, () -> {
      test.mannWhitneyUTest(y, x);
    });
  }

}