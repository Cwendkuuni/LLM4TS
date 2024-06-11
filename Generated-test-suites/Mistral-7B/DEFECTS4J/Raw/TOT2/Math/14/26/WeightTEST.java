import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;
import org.junit.Test;

public class WeightTest {

 // Test cases from Expert 1
 // ...

 // Test cases from Expert 2
 // ...

 /**
  * Test getWeight() with a Weight object created with a square RealMatrix.
  */
 @Test
 public void testGetWeightWithSquareRealMatrix() {
 RealMatrix weight = new Array2DRowRealMatrix(3, 3, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0);
 Weight weightObject = new Weight(weight);
 RealMatrix expectedWeight = weight.copy();
 RealMatrix weightCopy = weightObject.getWeight();
 assertEquals(expectedWeight, weightCopy);
 }

 /**
  * Test getWeight() with a Weight object created with a non-square RealMatrix.
  */
 @Test(expected = NonSquareMatrixException.class)
 public void testGetWeightWithNonSquareRealMatrix() {
 RealMatrix weight = new Array2DRowRealMatrix(3, 2, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0);
 Weight weightObject = new Weight(weight);
 RealMatrix weightCopy = weightObject.getWeight();
 fail("Should fail with NonSquareMatrixException");
 }

}