import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.optim.BaseUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealPointValuePair;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer<TestFunction> optimizer;
    private BaseUnivariateRealOptimizer<TestFunction> optimizerWrapper;
    private TestFunction testFunction;
    private RandomDataGenerator generator;

    @Before
    public void setUp() {
        optimizerWrapper = new FibonacciSearchRealOptimizer<>();
        optimizer = new MultiStartUnivariateRealOptimizer<>(optimizerWrapper, 3, new RandomDataGenerator());
        testFunction = new TestFunction();
        generator = new RandomDataGenerator();
    }

    @Test
    public void testOptimize_Minimize_SingleStart() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealPointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, -10.0, 10.0);
        Assert.assertEquals(1.0, result.getPoint(), 0.01);
        Assert.assertEquals(-1.0, result.getValue(), 0.01);
    }

    @Test
    public void testOptimize_Minimize_MultipleStarts() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealPointValuePair[] optima = optimizer.optimize(testFunction, GoalType.MINIMIZE, -10.0, 10.0);
        Assert.assertEquals(3, optima.length);
        for (UnivariateRealPointValuePair pair : optima) {
            if (pair != null) {
                Assert.assertEquals(1.0, pair.getPoint(), 0.01);
                Assert.assertEquals(-1.0, pair.getValue(), 0.01);
            }
        }
    }

    @Test
    public void testOptimize_Maximize_SingleStart() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealPointValuePair result = optimizer.optimize(testFunction, GoalType.MAXIMIZE, -10.0, 10.0);
        Assert.assertEquals(9.0, result.getPoint(), 0.01);
        Assert.assertEquals(1.0, result.getValue(), 0.01);
    }

    @Test
    public void testOptimize_Maximize_MultipleStarts() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealPointValuePair[] optima = optimizer.optimize(testFunction, GoalType.MAXIMIZE, -10.0, 10.0);
        Assert.assertEquals(3, optima.length);
        for (UnivariateRealPointValuePair pair : optima) {
            if (pair != null) {
                Assert.assertEquals(9.0, pair.getPoint(), 0.01);
                Assert.assertEquals(1.0, pair.getValue(), 0.01);
            }
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimize_NoConvergence() throws FunctionEvaluationException, ConvergenceException {
        optimizer = new MultiStartUnivariateRealOptimizer<>(optimizerWrapper, 3, new RandomDataGenerator());
        optimizer.optimize(new NonConvergingFunction(), GoalType.MINIMIZE, -10.0, 10.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimize_FunctionEvaluationException() throws FunctionEvaluationException, ConvergenceException {
        optimizer = new MultiStartUnivariateRealOptimizer<>(optimizerWrapper, 3, new RandomDataGenerator());
        optimizer.optimize(new ThrowingFunction(), GoalType.MINIMIZE, -10.0, 10.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxEvaluations_NegativeValue() {
        optimizer = new MultiStartUnivariateRealOptimizer<>(optimizerWrapper, 3, new RandomDataGenerator());
        optimizer.setMaxEvaluations(-1);
    }

    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> mockChecker = mock(ConvergenceChecker.class);
        optimizer.setConvergenceChecker(mockChecker);
        verify(mockChecker).setOptimizer(optimizer);
    }

    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> mockChecker = mock(ConvergenceChecker.class);
        optimizer.setConvergenceChecker(mockChecker);
        ConvergenceChecker<UnivariateRealPointValuePair> result = optimizer.getConvergenceChecker();
        Assert.assertSame(mockChecker, result);
    }

    @Test
    public void testSetMaxEvaluations() {
        optimizer.setMaxEvaluations(5);
        Assert.assertEquals(5, optimizer.getMaxEvaluations());
        optimizerWrapper.setMaxEvaluations(10);
        Assert.assertEquals(5, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        optimizer.optimize(testFunction, GoalType.MINIMIZE, -10.0, 10.0);
        Assert.assertEquals(3 * optimizerWrapper.getMaxEvaluations(), optimizer.getEvaluations());
    }

    @Test
    public void testGetOptima() {
        UnivariateRealPointValuePair[] optima = optimizer.optimize(testFunction, GoalType.MINIMIZE, -10.0, 10.0);
        UnivariateRealPointValuePair[] result = optimizer.getOptima();
        Assert.assertArrayEquals(optima, result);
    }

    private static class TestFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 1.0;
        }

        @Override
        public double derivative(double x) {
            return 2.0 * x;
        }

        @Override
        public double[] grad(double x) {
            return new double[]{2.0 * x};
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }

        @Override
        public void checkDerivative(double x, double expected) throws FunctionEvaluationException {
            double actual = derivative(x);
            Assert.assertEquals(expected, actual, 0.01);
        }

        @Override
        public void checkGradient(double x, double[] expected) throws FunctionEvaluationException {
            double[] actual = grad(x);
            Assert.assertArrayEquals(expected, actual);
        }

        @Override
        public void checkValue(double x, double expected) throws FunctionEvaluationException {
            double actual = value(x);
            Assert.assertEquals(expected, actual, 0.01);
        }

        @Override
        public void checkBounds(double lowerBound, double upperBound) throws FunctionEvalationException {
            checkValue(lowerBound, Double.NEGATIVE_INFINITY);
            checkValue(upperBound, Double.POSITIVE_INFINITY);
        }
    }

    private static class FibonacciSearchRealOptimizer<FUNC extends UnivariateRealFunction>
            implements BaseUnivariateRealOptimizer<FUNC> {

        @Override
        public void setMaxEvaluations(int maxEvaluations) {
        }

        @Override
        public void setConvergenceChecker(ConvergenceChecker<PointValuePair> convergenceChecker) {
        }

        @Override
        public PointValuePair optimize(FUNC f, GoalType goal, double min, double max)
                throws FunctionEvaluationException, ConvergenceException {
            double a = min;
            double b = max;
            double c;
            int k = 0;
            while (true) {
                int n = (k >> 1) + 1;
                double alfa = FastMath.pow(2.0, n) - 1.0;
                double fvalA = f.value(a + alfa * (b - a) / (alfa + 1.0));
                double fvalB = f.value(b);
                double fvalC;
                if (goal == GoalType.MINIMIZE) {
                    if (fvalA < fvalB) {
                        b = a + alfa;
                        a = a + alfa * (b - a) / (alfa + 1.0);
                    } else {
                        c = a + (b - a) / 2.0;
                        fvalC = f.value(c);
                        if (fvalC <= fvalA) {
                            b = c;
                        } else {
                            a = c;
                        }
                        k++;
                    }
                } else {
                    if (fvalB < fvalA) {
                        a = a + alfa;
                        b = b + alfa;
                    } else {
                        c = a + (b - a) / 2.0;
                        fvalC = f.value(c);
                        if (fvalC >= fvalB) {
                            a = c;
                        } else {
                            b = c;
                        }
                        k++;
                    }
                }
                if (k >= 2 * Math.log(Math.max(1.0, Math.abs(b - a) / 0.01)) + 1) {
                    break;
                }
            }
            return new PointValuePair(a, f.value(a));
        }
    }

    private static class NonConvergingFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public double derivative(double x) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public double[] grad(double x) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }

        @Override
        public void checkDerivative(double x, double expected) throws FunctionEvaluationException {
            Assert.fail("Not supported yet.");
        }

        @Override
        public void checkGradient(double x, double[] expected) throws FunctionEvaluationException {
            Assert.fail("Not supported yet.");
        }

        @Override
        public void checkValue(double x, double expected) throws FunctionEvaluationException {
            Assert.fail("Not supported yet.");
        }

        @Override
        public void checkBounds(double lowerBound, double upperBound) throws FunctionEvaluationException {
            Assert.fail("Not supported yet.");
        }
    }

    private static class ThrowingFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            throw new FunctionEvaluationException(LocalizedFormats.INTEGER_ARGUMENT, 0);
        }

        @Override
        public double derivative(double x) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public double[] grad(double x) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }

        @Override
        public void checkDerivative(double x, double expected) throws FunctionEvaluationException {
            Assert.fail("Not supported yet.");
        }

        @Override
        public void checkGradient(double x, double[] expected) throws FunctionEvaluationException {
            Assert.fail("Not supported yet.");
        }

        @Override
        public void checkValue(double x, double expected) throws FunctionEvaluationException {
            throw new FunctionEvaluationException(LocalizedFormats.INTEGER_ARGUMENT, 0);
        }

        @Override
        public void checkBounds(double lowerBound, double upperBound) throws FunctionEvaluationException {
            Assert.fail("Not supported yet.");
        }
    }
}