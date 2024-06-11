package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Category(org.apache.commons.math.classification.RegressionTest)
public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() throws Exception {
        equations = new TestDifferentialEquations();
        integrator = new RungeKuttaFehlberg78Method();
        integrator.setMinStep(1e-12);
        integrator.setMaxStep(1e-12);
    }

    @Test
    public void testIntegration() throws Exception {
        double[] y0 = new double[]{1.0};
        double t0 = 0.0;
        double tEnd = 1.0;
        double yEnd[] = new double[]{1.5};

        double result = integrator.integrate(equations, t0, y0, tEnd, yEnd);
        assertEquals(tEnd, result, 1e-12);
        assertArrayEquals(yEnd, y0, 1e-12);
    }

    @Test(expected = DerivativeException.class)
    public void testDerivativeException() throws Exception {
        equations = new TestDifferentialEquationsWithException();
        integrator.integrate(equations, 0.0, new double[1], 1.0, new double[1]);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegratorException() throws Exception {
        equations = new TestDifferentialEquationsWithIntegratorException();
        integrator.integrate(equations, 0.0, new double[1], 1.0, new double[1]);
    }

    @Test
    public void testIntegrationWithEvents() throws Exception {
        equations = new TestDifferentialEquationsWithEvents();
        integrator = new RungeKuttaFehlberg78Method();
        integrator.setMinStep(1e-12);
        integrator.setMaxStep(1e-12);

        StepHandler stepHandler = new MyStepHandler();
        integrator.addStepHandler(stepHandler);

        double[] y0 = new double[]{1.0};
        double t0 = 0.0;
        double tEnd = 1.0;
        double yEnd[] = new double[]{1.5};

        integrator.integrate(equations, t0, y0, tEnd, yEnd);
        assertTrue(stepHandler.isCalled());
        assertEquals(1, stepHandler.getNumberOfSteps(), 1e-12);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() throws Exception {
        equations = new TestDifferentialEquationsWithMaxCountExceededException();
        integrator.integrate(equations, 0.0, new double[1], 1.0, new double[1], 100);
    }

    @Test
    public void testIntegrationWithRelativeTolerance() throws Exception {
        equations = new TestDifferentialEquationsWithRelativeTolerance();
        integrator = new RungeKuttaFehlberg78Method();
        integrator.setMinStep(1e-12);
        integrator.setMaxStep(1e-12);
        integrator.setRelativeTolerance(1e-4);

        double[] y0 = new double[]{1.0};
        double t0 = 0.0;
        double tEnd = 1.0;
        double yEnd[] = new double[]{1.5};

        double result = integrator.integrate(equations, t0, y0, tEnd, yEnd);
        assertEquals(tEnd, result, 1e-4);
        assertArrayEquals(yEnd, y0, 1e-4);
    }

    @Test
    public void testIntegrationWithAbsoluteTolerance() throws Exception {
        equations = new TestDifferentialEquationsWithAbsoluteTolerance();
        integrator = new RungeKuttaFehlberg78Method();
        integrator.setMinStep(1e-12);
        integrator.setMaxStep(1e-12);
        integrator.setAbsoluteTolerance(1e-4);

        double[] y0 = new double[]{1.0};
        double t0 = 0.0;
        double tEnd = 1.0;
        double yEnd[] = new double[]{1.5};

        double result = integrator.integrate(equations, t0, y0, tEnd, yEnd);
        assertEquals(tEnd, result, 1e-4);
        assertArrayEquals(yEnd, y0, 1e-4);
    }

    @Test
    public void testIntegrationWithVectorTolerances() throws Exception {
        equations = new TestDifferentialEquationsWithVectorTolerances();
        integrator = new RungeKuttaFehlberg78Method();
        integrator.setMinStep(1e-12);
        integrator.setMaxStep(1e-12);
        integrator.setAbsoluteTolerance(new double[]{-1e-4, 1e-4});
        integrator.setRelativeTolerance(new double[]{1e-4, 1e-4});

        double[] y0 = new double[]{1.0, 1.0};
        double t0 = 0.0;
        double tEnd = 1.0;
        double yEnd1[] = new double[]{1.5, 1.5};
        double yEnd2[] = new double[]{1.5, 2.0};

        double result1 = integrator.integrate(equations, t0, y0, tEnd, yEnd1);
        assertEquals(tEnd, result1, 1e-4);
        assertArrayEquals(yEnd1, y0, 1e-4);

        double result2 = integrator.integrate(equations, t0, y0, tEnd, yEnd2);
        assertEquals(tEnd, result2, 1e-4);
        assertArrayEquals(yEnd2, y0, 1e-4);
    }

    static class TestDifferentialEquations implements FirstOrderDifferentialEquations {
        @Override
        public int getOrder() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = 0.5 * y[0];
        }
    }

    static class TestDifferentialEquationsWithException implements FirstOrderDifferentialEquations {
        @Override
        public int getOrder() {
            throw new DerivativeException("Test exception");
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = 0.5 * y[0];
        }
    }

    static class TestDifferentialEquationsWithIntegratorException implements FirstOrderDifferentialEquations {
        @Override
        public int getOrder() {
            throw new IntegratorException("Test exception");
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = 0.5 * y[0];
        }
    }

    static class TestDifferentialEquationsWithEvents implements FirstOrderDifferentialEquations {
        @Override
        public int getOrder() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = 0.5 * y[0];
        }

        @Override
        public List<Event> getEvents(double t, double[] y) {
            return Collections.singletonList(new When(1.0, 1.0));
        }
    }

    static class TestDifferentialEquationsWithMaxCountExceededException implements FirstOrderDifferentialEquations {
        @Override
        public int getOrder() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            throw new MaxCountExceededException("Test exception");
        }
    }

    static class TestDifferentialEquationsWithRelativeTolerance implements FirstOrderDifferentialEquations {
        @Override
        public int getOrder() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = 0.5 * y[0];
        }
    }

    static class TestDifferentialEquationsWithAbsoluteTolerance implements FirstOrderDifferentialEquations {
        @Override
        public int getOrder() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = 0.5 * y[0];
        }
    }

    static class TestDifferentialEquationsWithVectorTolerances implements FirstOrderDifferentialEquations {
        @Override
        public int getOrder() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = 0.5 * y[0];
            yPrime[1] = 0.5 * y[1];
        }
    }

    static class MyStepHandler implements StepHandler {
        private int numberOfSteps = 0;
        private boolean isCalled = false;

        @Override
        public void handleStep(StepHandler event, int type) {
            numberOfSteps++;
        }

        public boolean isCalled() {
            return isCalled;
        }

        public int getNumberOfSteps() {
            return numberOfSteps;
        }
    }
}