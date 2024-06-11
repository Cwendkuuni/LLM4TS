package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.RootFunction;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.special.E;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest {

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @Test
    public void testIntegration() throws Exception {
        // Create a simple differential equation
        class TestEquations implements FirstOrderDifferentialEquations {
            private final double a;
            public TestEquations(double a) {
                this.a = a;
            }
            @Override
            public int getDimension() {
                return 1;
            }
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = a * Math.exp(-t) * y[0];
            }
        }

        // Set up the integrator
        double[] c = {0.0, 0.5, 0.5, 1.0};
        double[][] a = {
            {0.0},
            {0.5, 0.5},
            {0.5, 0.5, 1.0},
            {1.0, 0.5, 0.5, 1.0}
        };
        double[] b = {0.0, 0.5, 0.5, 1.0};
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK4", c, a, b, new RungeKuttaStepInterpolator(), 0.01) {
            @Override
            protected void checkSanity(FirstOrderDifferentialEquations equations) {
                // No check for this test
            }
        };

        // Test a simple integration
        double initialTime = 0.0;
        double initialValue = 1.0;
        double finalTime = 10.0;
        double[] expectedValues = new double[] {
            1.0, 3.131495971482871, 6.265373432209724, 10.0
        };
        Double[] yWrapped = new Double[expectedValues.length];
        System.arraycopy(expectedValues, 0, yWrapped, 0, expectedValues.length);
        double[] y = new double[expectedValues.length];
        System.arraycopy(yWrapped, 0, y, 0, expectedValues.length);
        Double[] resultWrapped = integrator.integrate(new TestEquations(1.0), initialTime, y, finalTime, yWrapped);
        double[] result = new double[expectedValues.length];
        System.arraycopy(resultWrapped, 0, result, 0, expectedValues.length);
        assertArrayEquals(expectedValues, result, 0.001);

        // Test integration with events
        class TestRootFunction implements RootFunction {
            @Override
            public double value(double t, double[] y) {
                return Math.exp(y[0]) - 3.0;
            }
            @Override
            public int getDimensions() {
                return 1;
            }
        }
        RootFunction rootFunction = new TestRootFunction();
        CombinedEventsManager manager = new CombinedEventsManager();
        manager.addEvent(1.0, new Event() {
            @Override
            public int getId() {
                return 1;
            }
            @Override
            public double value(double t, double[] y) {
                return rootFunction.value(t, y);
            }
            @Override
            public String getDescription() {
                return "Test event";
            }
        });
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandler event, int type) {
                System.out.println("Step handler called with type: " + type);
            }
        };
        manager.addStepHandler(stepHandler);
        integrator = new RungeKuttaIntegrator("RK4", c, a, b, new RungeKuttaStepInterpolator(), 0.01) {
            @Override
            protected void checkSanity(FirstOrderDifferentialEquations equations) {
                // No check for this test
            }
            @Override
            public void setEquations(FirstOrderDifferentialEquations equations) {
                super.setEquations(equations);
                setEventsManager(manager);
            }
        };
        initialTime = 0.0;
        initialValue = 1.0;
        finalTime = 4.0;
        y = new double[] {initialValue};
        yWrapped = new Double[] {initialValue};
        result = integrator.integrate(new TestEquations(1.0), initialTime, y, finalTime, yWrapped);
        resultWrapped = integrator.getSolutions();
        assertEquals(1, manager.size());
        assertEquals(1, manager.getEvent(0).getId());
        assertArrayEquals(new double[] {1.0, 2.718281828459045}, result, 0.001);
        assertArrayEquals(new double[] {1.0, 2.718281828459045}, resultWrapped, 0.001);
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * Test with invalid input.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidIntegration() throws Exception {
        // Create a simple differential equation
        class TestEquations implements FirstOrderDifferentialEquations {
            @Override
            public int getDimension() {
                return 1;
            }
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 1.0;
            }
        }

        // Set up the integrator
        double[] c = {0.0, 0.5, 0.5, 1.0};
        double[][] a = {
            {0.0},
            {0.5, 0.5},
            {0.5, 0.5, 1.0},
            {1.0, 0.5, 0.5, 1.0}
        };
        double[] b = {0.0, 0.5, 0.5, 1.0};
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK4", c, a, b, new RungeKuttaStepInterpolator(), 0.01) {
            @Override
            protected void checkSanity(FirstOrderDifferentialEquations equations) {
                // No check for this test
            }
        };

        // Test with invalid input
        Double[] yWrapped = new Double[1];
        double[] y = new double[1];
        y[0] = Double.NaN;
        integrator.integrate(new TestEquations(1.0), 0.0, y, 1.0, yWrapped);
    }

}