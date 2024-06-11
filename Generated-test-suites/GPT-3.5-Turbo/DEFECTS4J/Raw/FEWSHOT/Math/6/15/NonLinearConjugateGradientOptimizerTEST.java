package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.setInitialStep(0.5);
        assertEquals(0.5, optimizer.getInitialStep(), 0);
    }

    @Test
    public void testSetInitialStep_NegativeValue() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.setInitialStep(-1);
        assertEquals(1.0, optimizer.getInitialStep(), 0);
    }

    @Test
    public void testDoOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(0.1);
        optimizer.setMaxEvaluations(100);
        optimizer.setStartPoint(new double[] {0, 0});
        optimizer.setGoalType(GoalType.MINIMIZE);
        double[] expectedPoint = new double[] {0, 0};
        double expectedValue = 0;
        try {
            PointValuePair result = optimizer.doOptimize();
            double[] actualPoint = result.getPoint();
            double actualValue = result.getValue();
            for (int i = 0; i < expectedPoint.length; i++) {
                assertEquals(expectedPoint[i], actualPoint[i], 0);
            }
            assertEquals(expectedValue, actualValue, 0);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDoOptimize_NonPositiveInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.setInitialStep(0);
        optimizer.setMaxEvaluations(100);
        optimizer.setStartPoint(new double[] {0, 0});
        optimizer.setGoalType(GoalType.MINIMIZE);
        double[] expectedPoint = new double[] {0, 0};
        double expectedValue = 0;
        try {
            PointValuePair result = optimizer.doOptimize();
            double[] actualPoint = result.getPoint();
            double actualValue = result.getValue();
            for (int i = 0; i < expectedPoint.length; i++) {
                assertEquals(expectedPoint[i], actualPoint[i], 0);
            }
            assertEquals(expectedValue, actualValue, 0);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}