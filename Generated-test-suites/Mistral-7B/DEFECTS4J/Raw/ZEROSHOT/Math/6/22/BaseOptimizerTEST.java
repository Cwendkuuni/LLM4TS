import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MaxEval;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

 @Test
 public void testGetMaxEvaluations() {
 BaseOptimizer<Object> optimizer = new BaseOptimizer<Object>() {
 @Override
 public int getMaxEvaluations() {
 return 100;
 }
 };
 assertEquals("The maximum number of function evaluations should be 100", 100, optimizer.getMaxEvaluations());
 }

 @Test
 public void testGetEvaluations() {
 BaseOptimizer<Object> optimizer = new BaseOptimizer<Object>() {
 @Override
 public int getMaxEvaluations() {
 return 100;
 }
 };
 optimizer.getConvergenceChecker().converged(new Object[]{"x", 0.0});
 optimizer.optimize(new DummyObjectiveFunction(), new DummyPoint(), new MaxEval(50));
 assertEquals("The number of evaluations should be 50", 50, optimizer.getEvaluations());
 }

 @Test
 public void testGetConvergenceChecker() {
 BaseOptimizer<Object> optimizer = new BaseOptimizer<Object>() {
 @Override
 public ConvergenceChecker<Object> getConvergenceChecker() {
 return new DummyConvergenceChecker();
 }
 };
 assertSame("The convergence checker should be an instance of DummyConvergenceChecker", new DummyConvergenceChecker(), optimizer.getConvergenceChecker());
 }

 private static class DummyObjectiveFunction implements ObjectiveFunction {
 @Override
 public double value(Object[] parameters) {
 return 0.0;
 }
 }

 private static class DummyPoint implements Point {
 @Override
 public Object[] getCoordinates() {
 return new Object[0];
 }
 }

 private static class DummyConvergenceChecker implements ConvergenceChecker<Object> {
 @Override
 public boolean isConverged(Object optimizationData, Object... parameters) {
 return true;
 }

 @Override
 public String getMessage() {
 return null;
 }
 }
}