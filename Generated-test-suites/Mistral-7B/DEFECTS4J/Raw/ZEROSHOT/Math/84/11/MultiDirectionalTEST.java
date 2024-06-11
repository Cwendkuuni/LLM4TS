import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleOptimizationTest;
import org.apache.commons.math3.optim.direct.MultiDirectional;
import org.apache.commons.math3.optim.nonlinear.functions.RossHagenFunction;
import org.apache.commons.math3.optim.nonlinear.functions.RossHagenFunction2;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalOptimizer() throws Exception {
        Double[] xSmallest = new Double[]{0.0, 0.0};
        MultiDirectional md = new MultiDirectional(2.0, 0.5);

        // Test with RossHagen function
        SimpleOptimizationTest.testFunctionValue(new RossHagenFunction(), md, 0.1, 100, xSmallest);
        PointValuePair solution = md.optimize(new RossHagenFunction(), xSmallest, null);
        assertEquals(0.0, solution.getPoint()[0], 1e-8);
        assertEquals(0.0, solution.getPoint()[1], 1e-8);
        assertEquals(-0.5, solution.getValue(), 1e-8);

        // Test with RossHagen function 2
        SimpleOptimizationTest.testFunctionValue(new RossHagenFunction2(), md, 0.1, 100, xSmallest);
        solution = md.optimize(new RossHagenFunction2(), xSmallest, null);
        assertEquals(1.0 / Math.sqrt(2.0), solution.getPoint()[0], 1e-8);
        assertEquals(1.0 / Math.sqrt(2.0), solution.getPoint()[1], 1e-8);
        assertEquals(-1.0, solution.getValue(), 1e-8);

        // Test with custom function and convergence checker
        RealConvergenceChecker cc = new RealConvergenceChecker(1.0e-6, 100);
        SimpleOptimizationTest.testFunctionValue(new CustomFunction(), md, 0.1, 100, xSmallest, cc);
        solution = md.optimize(new CustomFunction(), xSmallest, cc);
        assertEquals(1.0, solution.getPoint()[0], 1e-8);
        assertEquals(1.0, solution.getPoint()[1], 1e-8);
        assertEquals(2.0, solution.getValue(), 1e-8);
    }

    private static class CustomFunction implements org.apache.commons.math3.optim.NonlinearFunction {
        @Override
        public double value(double[] point) {
            return point[0] * point[0] + point[1] * point[1];
        }

        @Override
        public int getNumberOfVariables() {
            return 2;
        }
    }

}