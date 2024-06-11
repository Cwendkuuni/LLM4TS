import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.*;
import org.apache.commons.math3.optim.point.RealPointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

 private final DoubleFunction function = new AbstractDoubleFunction() {
 @Override
 public double value(double x) {
 return Math.pow(Math.sin(x), 2);
 }
 };

 @Test
 public void testMultiDirectionalOptimizer() throws OptimizationException, FunctionEvaluationException {
 // Test default constructor
 MultiDirectional md = new MultiDirectional();

 // Test optimization with simple bounds
 RealVectorInitializer initializer = new DenseInitializer(new double[]{-10.0, 10.0});
 OptimizationProblem problem = new OptimizationProblem(function, initializer);
 RealConvergenceChecker checker = new RelativeErrorConvergenceChecker(1.0e-8);
 MultiDirectional optimizer = new MultiDirectional();
 optimizer.setConvergenceChecker(checker);
 optimizer.optimize(problem);
 RealPointValuePair result = optimizer.getBestSolution();
 double expected = Math.PI;
 assertEquals("The optimization result is incorrect.", expected, result.getPoint()[0], 1e-8);

 // Test optimization with custom coefficients
 MultiDirectional mdCustom = new MultiDirectional(3.0, 0.3);
 optimizer = mdCustom;
 optimizer.optimize(problem);
 result = optimizer.getBestSolution();
 expected = Math.PI * Math.sqrt(5.0 / 3.0);
 assertEquals("The optimization result is incorrect.", expected, result.getPoint()[0], 1e-8);
 }
}