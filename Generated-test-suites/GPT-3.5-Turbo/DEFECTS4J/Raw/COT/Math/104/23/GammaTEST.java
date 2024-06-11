import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;

public class GammaTest {

  @Test
  public void testLogGamma() {
    // Test when x is 0
    assertEquals(Double.NaN, Gamma.logGamma(0.0), 0.0001);
    
    // Test when x is a positive integer
    assertEquals(0.0, Gamma.logGamma(1.0), 0.0001);
    assertEquals(0.6931471805599453, Gamma.logGamma(2.0), 0.0001);
    assertEquals(1.791759469228055, Gamma.logGamma(3.0), 0.0001);
    
    // Test when x is a positive number
    assertEquals(0.5723649429247001, Gamma.logGamma(0.7), 0.0001);
    assertEquals(4.852030263919617, Gamma.logGamma(5.0), 0.0001);
    
    // Test when x is negative
    assertEquals(Double.NaN, Gamma.logGamma(-1.5), 0.0001);
  }

  @Test
  public void testRegularizedGammaP() throws MathException {
    // Test when a and x are positive integers
    assertEquals(0.0, Gamma.regularizedGammaP(1.0, 1.0), 0.0001);
    assertEquals(0.5, Gamma.regularizedGammaP(2.0, 1.0), 0.0001);
    assertEquals(1.0, Gamma.regularizedGammaP(3.0, 3.0), 0.0001);
    
    // Test when a and x are positive numbers
    assertEquals(0.07185781207348403, Gamma.regularizedGammaP(0.5, 0.1), 0.0001);
    assertEquals(0.5324544471667894, Gamma.regularizedGammaP(1.5, 2.0), 0.0001);
    
    // Test when a is negative
    assertEquals(Double.NaN, Gamma.regularizedGammaP(-1.5, 0.5), 0.0001);
  }

  @Test
  public void testRegularizedGammaQ() throws MathException {
    // Test when a and x are positive integers
    assertEquals(1.0, Gamma.regularizedGammaQ(1.0, 1.0), 0.0001);
    assertEquals(0.5, Gamma.regularizedGammaQ(2.0, 1.0), 0.0001);
    assertEquals(0.0, Gamma.regularizedGammaQ(3.0, 3.0), 0.0001);
    
    // Test when a and x are positive numbers
    assertEquals(0.9281421879265159, Gamma.regularizedGammaQ(0.5, 0.1), 0.0001);
    assertEquals(0.4675455528332106, Gamma.regularizedGammaQ(1.5, 2.0), 0.0001);
    
    // Test when a is negative
    assertEquals(Double.NaN, Gamma.regularizedGammaQ(-1.5, 0.5), 0.0001);
  }
}