import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.optim.BaseUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateRealPointValuePair;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.Assert;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest<Func extends UnivariateRealFunction> {

    @Test
    public void testConstructor() {
        BaseUnivariateRealOptimizer<Func> optimizer = new BrentOptimizer<>();
        MultiStartUnivariateRealOptimizer<Func> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 2, new RandomDataGenerator());
    }

    @Test
    public void testSetConvergenceChecker() {
        BaseUnivariateRealOptimizer<Func> optimizer = new BrentOptimizer<>();
        MultiStartUnivariateRealOptimizer<Func> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 2, new RandomDataGenerator());
        ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        multiStartOptimizer.setConvergenceChecker(convergenceChecker);
        Assert.assertSame(convergenceChecker, multiStartOptimizer.getConvergenceChecker());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseUnivariateRealOptimizer<Func> optimizer = new BrentOptimizer<>();
        MultiStartUnivariateRealOptimizer<Func> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 2, new RandomDataGenerator());
        ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        multiStartOptimizer.setConvergenceChecker(convergenceChecker);
        Assert.assertSame(convergenceChecker, multiStartOptimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        BaseUnivariateRealOptimizer<Func> optimizer = new BrentOptimizer<>();
        MultiStartUnivariateRealOptimizer<Func> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 2, new RandomDataGenerator());
        Assert.assertSame(optimizer.getMaxEvaluations(), multiStartOptimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseUnivariateRealOptimizer<Func> optimizer = new BrentOptimizer<>();
        MultiStartUnivariateRealOptimizer<Func> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 2, new RandomDataGenerator());
        optimizer.optimize(mock(UnivariateRealFunction.class), GoalType.MINIMIZE, 1.0, 2.0);
        Assert.assertEquals(1, multiStartOptimizer.getEvaluations());
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaBeforeOptimization() {
        BaseUnivariateRealOptimizer<Func> optimizer = new BrentOptimizer<>();
        MultiStartUnivariateRealOptimizer<Func> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 2, new RandomDataGenerator());
        multiStartOptimizer.getOptima();
    }

    @Test
    public void testSetMaxEvaluations() {
        BaseUnivariateRealOptimizer<Func> optimizer = new BrentOptimizer<>();
        MultiStartUnivariateRealOptimizer<Func> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 2, new RandomDataGenerator());
        multiStartOptimizer.setMaxEvaluations(100);
        Assert.assertEquals(100, multiStartOptimizer.getMaxEvaluations());
        optimizer.setMaxEvaluations(50);
        Assert.assertEquals(50, optimizer.getMaxEvaluations());
        Assert.assertEquals(100, multiStartOptimizer.getMaxEvaluations());
    }

    @Test
    public void testOptimizeMinFunction() throws FunctionEvaluationException, ConvergenceException {
        BaseUnivariateRealOptimizer<Func> optimizer = new BrentOptimizer<>();
        MultiStartUnivariateRealOptimizer<Func> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 2, new RandomDataGenerator());
        UnivariateRealFunction function = new UnivariateFunctionNegativeSquareRoot();
        double[] expectedOptimaX = {1.5, 2.5};
        double expectedOptimaF = {-1.5, -2.5};
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 5.0);
        for (int i = 0; i < 2; ++i) {
            Assert.assertNotNull(optima[i]);
            Assert.assertEquals(expectedOptimaX[i], optima[i].getPoint().getValue(), 0.01);
            Assert.assertEquals(expectedOptimaF[i], optima[i].getValue(), 0.01);
        }
    }

    @Test
    public void testOptimizeMinFunctionFailedConvergence() throws FunctionEvaluationException, ConvergenceException {
        BaseUnivariateRealOptimizer<Func> optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<Func> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 2, new RandomDataGenerator());
        UnivariateRealFunction function = new UnivariateFunctionSine();
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0);
        Assert.assertArrayEquals(new UnivariateRealPointValuePair[2], optima);
    }

    @Test
    public void testOptimizeMaxFunction() throws FunctionEvaluationException, ConvergenceException {
        BaseUnivariateRealOptimizer<Func> optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<Func> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 2, new RandomDataGenerator());
        UnivariateRealFunction function = new UnivariateFunctionAbsoluteValue();
        double[] expectedOptimaX = {1.5, 2.5};
        double expectedOptimaF = {1.5, 2.5};
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 5.0);
        for (int i = 0; i < 2; ++i) {
            Assert.assertNotNull(optima[i]);
            Assert.assertEquals(expectedOptimaX[i], optima[i].getPoint().getValue(), 0.01);
            Assert.assertEquals(expectedOptimaF[i], optima[i].getValue(), 0.01);
        }
    }

    @Test
    public void testGetOptima() throws FunctionEvaluationException, ConvergenceException {
        BaseUnivariateRealOptimizer<Func> optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<Func> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 2, new RandomDataGenerator());
        UnivariateRealFunction function = new UnivariateFunctionNegativeSquareRoot();
        multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 5.0);
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        for (int i = 0; i < 2; ++i) {
            Assert.assertNotNull(optima[i]);
            Assert.assertEquals(i == 0 ? 1.5 : 2.5, optima[i].getPoint().getValue(), 0.01);
            Assert.assertEquals((i == 0) ? -1.5 : -2.5, optima[i].getValue(), 0.01);
        }
        UnivariateRealPointValuePair[] expectedEmptyOptima = new UnivariateRealPointValuePair[0];
        multiStartOptimizer.optimize(mock(UnivariateRealFunction.class), GoalType.MINIMIZE, 0.0, 5.0, 1.0);
        Assert.assertArrayEquals(expectedEmptyOptima, multiStartOptimizer.getOptima());
    }
}

class UnivariateFunctionNegativeSquareRoot implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return Math.sqrt(Math.abs(x)) * -1;
    }
}

class UnivariateFunctionSine implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return Math.sin(x);
    }
}

class UnivariateFunctionAbsoluteValue implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return Math.abs(x);
    }
}