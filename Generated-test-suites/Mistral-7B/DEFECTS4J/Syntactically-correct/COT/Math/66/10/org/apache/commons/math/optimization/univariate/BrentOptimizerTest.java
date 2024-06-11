import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(JUnit4.class)
public class BrentOptimizerTest {

 private UnivariateRealFunction function1 = new Function("f", x -> Math.pow(x, 3) - 2 * x + 1);
 private UnivariateRealFunction function2 = new Function("g", x -> Math.exp(Math.abs(x)) - 1);

 @Test
 public void testBrentOptimizerWithMinimizationGoal() throws Exception {
     BrentOptimizer optimizer = new BrentOptimizer();
     double min = -5.0;
     double max = 5.0;
     double startValue = 2.0;
     double expectedResult = -1.0;

     double result = optimizer.optimize(function1, GoalType.MINIMIZE, min, max, startValue);

     assertEquals(expectedResult, result, 0.0001);
 }

 @Test
 public void testBrentOptimizerWithMaximizationGoal() throws Exception {
     BrentOptimizer optimizer = new BrentOptimizer();
     double min = -5.0;
     double max = 5.0;
     double startValue = 2.0;
     double expectedResult = 1.0;

     double result = optimizer.optimize(function1, GoalType.MAXIMIZE, min, max, startValue);

     assertEquals(expectedResult, result, 0.0001);
 }

 @Test
 public void testBrentOptimizerWithEdgeCases() throws Exception {
     BrentOptimizer optimizer = new BrentOptimizer();

     assertThrows(NotStrictlyPositiveException.class, () -> optimizer.setAbsoluteAccuracy(-1.0));
     assertThrows(NotStrictlyPositiveException.class, () -> optimizer.setRelativeAccuracy(-1.0));
     assertThrows(NotStrictlyPositiveException.class, () -> optimizer.setMaxEvaluations(-1));
     assertThrows(NotStrictlyPositiveException.class, () -> optimizer.setMaximalIterationCount(-1));

     UnivariateRealFunction function = new Function("h", x -> Math.sqrt(Math.abs(x)) - 1);

     assertThrows(FunctionEvaluationException.class, () -> optimizer.optimize(function, GoalType.MINIMIZE, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY));
     assertThrows(FunctionEvaluationException.class, () -> optimizer.optimize(function, GoalType.MAXIMIZE, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
 }

 @Test
 public void testBrentOptimizerWithMaxIterationsExceededException() throws Exception {
     BrentOptimizer optimizer = new BrentOptimizer();
     optimizer.setMaxIterationsExceededExceptionMessage("Test Max Iterations Exceeded Exception");
     optimizer.setMaximalIterationCount(0);

     UnivariateRealFunction function = new Function("i", x -> Math.pow(x, 2));

     assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(function, GoalType.MINIMIZE, -1.0, 1.0, 0.0));
     assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(function, GoalType.MAXIMIZE, -1.0, 1.0, 0.0));
 }

 @Test
 public void testBrentOptimizerWithFunctionEvaluationException() throws Exception {
     BrentOptimizer optimizer = new BrentOptimizer() {
         @Override
         protected double doOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
             throw new FunctionEvaluationException("Test Function Evaluation Exception");
         }
     };

     UnivariateRealFunction function = new Function("j", x -> { throw new FunctionEvaluationException("Test Function Evaluation Exception"); });

     assertThrows(FunctionEvaluationException.class, () -> optimizer.optimize(function, GoalType.MINIMIZE, -1.0, 1.0, 0.0));
     assertThrows(FunctionEvaluationException.class, () -> optimizer.optimize(function, GoalType.MAXIMIZE, -1.0, 1.0, 0.0));
 }

 private static class Function implements UnivariateRealFunction {
     private final String name;
     private final FunctionEvaluation function;

     public Function(String name, FunctionEvaluation function) {
         this.name = name;
         this.function = function;
     }

     @Override
     public double value(double x) throws FunctionEvaluationException {
         return function.evaluate(x);
     }

     @FunctionalInterface
     private interface FunctionEvaluation {
         double evaluate(double x) throws FunctionEvaluationException;
     }
 }
}