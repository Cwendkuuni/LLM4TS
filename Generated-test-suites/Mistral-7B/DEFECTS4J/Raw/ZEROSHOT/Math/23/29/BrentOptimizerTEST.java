import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.initializer.ValueInitializer;
import org.apache.commons.math3.optim.nonlinear.simple.RationalFunction;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 @Test
 public void testBrentOptimizer() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

 // Test function: x^2
 UnivariateFunction function = new RationalFunction(2, 1);
 double x = 3.0;
 double result = optimizer.optimize(function, x, new ValueInitializer(x));
 assertEquals("x^2 minimum not found", -3.0, result.getValue(), 1e-8);
 assertEquals("x^2 minimum not found", x, result.getPoint().getX(), 1e-8);

 // Test function: sqrt(x^2 + 1)
 UnivariateFunction functionSqrt = new RationalFunction(2, 1) {
 @Override
 public double value(double x) {
 return Math.sqrt(x * x + 1);
 }
 };
 double xSqrt = 1.0;
 result = optimizer.optimize(functionSqrt, xSqrt, new ValueInitializer(xSqrt));
 assertEquals("sqrt(x^2 + 1) minimum not found", 0.0, result.getValue(), 1e-8);
 assertEquals("sqrt(x^2 + 1) minimum not found", xSqrt, result.getPoint().getX(), 1e-8);
 }
}