import org.apache.commons.math.optimization.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.UnivariateRealOptimizer;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;



public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer optimizer;

    @Before
    public void setUp() {
        UnivariateRealOptimizer innerOptimizer = new InnerOptimizer();
        Random random = new Random();
        optimizer = new MultiStartUnivariateRealOptimizer(innerOptimizer, 5, random);
    }

    private static class InnerOptimizer implements UnivariateRealOptimizer {

        @Override
        public double getFunctionValue() {
            return 0;
        }

        @Override
        public double getResult() {
            return 0;
        }

        @Override
        public double getAbsoluteAccuracy() {
            return 0;
        }

        @Override
        public int getIterationCount() {
            return 0;
        }

        @Override
        public int getMaximalIterationCount() {
            return 0;
        }

        @Override
        public void resetAbsoluteAccuracy() {

        }

        @Override
        public void resetMaximalIterationCount() {

        }

        @Override
        public void setAbsoluteAccuracy(double v) {

        }

        @Override
        public void setMaximalIterationCount(int i) {

        }

        @Override
        public int getMaxEvaluations() {
            return 0;
        }

        @Override
        public void setMaxEvaluations(int i) {

        }

        @Override
        public int getEvaluations() {
            return 0;
        }

        @Override
        public double getRelativeAccuracy() {
            return 0;
        }

        @Override
        public void resetRelativeAccuracy() {

        }

        @Override
        public void setRelativeAccuracy(double v) {

        }

        @Override
        public double optimize(UnivariateRealFunction univariateRealFunction, GoalType goalType, double v, double v1) {
            return 0;
        }

        @Override
        public double optimize(UnivariateRealFunction univariateRealFunction, GoalType goalType, double v, double v1, double v2) {
            return 0;
        }
    }

    @Test
    public void testGetFunctionValue() {
        assertEquals(0.0, optimizer.getFunctionValue(), 0.0);
    }

    @Test
    public void testGetResult() {
        assertEquals(0.0, optimizer.getResult(), 0.0);
    }

    @Test
    public void testGetAbsoluteAccuracy() {
        assertEquals(0.0, optimizer.getAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testGetIterationCount() {
        assertEquals(0, optimizer.getIterationCount());
    }

    @Test
    public void testGetMaximalIterationCount() {
        assertEquals(0, optimizer.getMaximalIterationCount());
    }

    @Test
    public void testResetAbsoluteAccuracy() {
        optimizer.resetAbsoluteAccuracy();
    }

    @Test
    public void testResetMaximalIterationCount() {
        optimizer.resetMaximalIterationCount();
    }

    @Test
    public void testSetAbsoluteAccuracy() {
        optimizer.setAbsoluteAccuracy(0.0);
    }

    @Test
    public void testSetMaximalIterationCount() {
        optimizer.setMaximalIterationCount(0);
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(0, optimizer.getMaxEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        optimizer.setMaxEvaluations(0);
    }

    @Test
    public void testGetEvaluations() {
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetRelativeAccuracy() {
        assertEquals(0.0, optimizer.getRelativeAccuracy(), 0.0);
    }

    @Test
    public void testResetRelativeAccuracy() {
        optimizer.resetRelativeAccuracy();
    }

    @Test
    public void testSetRelativeAccuracy() {
        optimizer.setRelativeAccuracy(0.0);
    }

    @Test
    public void testOptimize() {
        assertEquals(0.0, optimizer.optimize(null, GoalType.MINIMIZE, 0.0, 1.0), 0.0);
    }

    @Test
    public void testOptimizeWithStartValue() {
        assertEquals(0.0, optimizer.optimize(null, GoalType.MINIMIZE, 0.0, 1.0, 0.5), 0.0);
    }

    @Test
    public void testGetOptima() {
        double[] optima = optimizer.getOptima();
        assertEquals(5, optima.length);
    }

    @Test
    public void testGetOptimaValues() {
        double[] optimaValues = optimizer.getOptimaValues();
        assertEquals(5, optimaValues.length);
    }
}

