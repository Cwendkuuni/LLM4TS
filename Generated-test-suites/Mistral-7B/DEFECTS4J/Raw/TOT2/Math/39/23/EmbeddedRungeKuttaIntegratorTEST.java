import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.util.DoubleIntegrator;
import org.apache.commons.math3.ode.util.ODETestUtilities;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {

        // Create a simple test function for the integrator
        DoubleIntegrator function = new DoubleIntegrator() {
            @Override
            public double[] computeDerivatives(double t, double[] y) {
                double[] result = new double[y.length];
                for (int i = 0; i < y.length; i++) {
                    result[i] = y[i] * (1.0 - y[i]);
                }
                return result;
            }

            @Override
            public Class<? extends ExpandableStatefulODE> getEquationsClass() {
                return null;
            }
        };

        // Create an instance of the embedded Runge-Kutta integrator
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "RK4", false,
                new double[] {0.25, 0.25, 0.5, 0.5},
                new double[][] {
                        new double[] {0.25, 0},
                        new double[] {0.25, 0.25},
                        new double[] {0.5, 0.25},
                        new double[] {0.5, 0.5}},
                new double[] {0.25, 0.25, 0.5, 0.5},
                new RungeKuttaStepInterpolator() {{
                    setOrder(4);
                }},
                0.001, 0.01,
                new double[] {1.0e-8, 1.0e-8},
                new double[] {1.0e-8, 1.0e-8}) {

            @Override
            public int getOrder() {
                return 4;
            }
        };

        // Define test parameters
        double initialTime = 0.0;
        double finalTime = 1.0;
        double[] initialState = new double[] {0.1, 0.2};
        double[] expectedState = new double[] {0.36787944117144233, 0.4331271105555573};
        double tolerance = 1e-6;

        // Perform the integration test
        ExpandableStatefulODE equations = new ExpandableStatefulODE(function) {
            @Override
            public int getDimension() {
                return 2;
            }
        };
        integrator.integrate(equations, initialTime, initialState, finalTime);

        // Validate the test results
        double[] resultState = equations.getCompleteState();
        ODETestUtilities.compareArrays(expectedState, resultState, tolerance);
        assertEquals(finalTime, equations.getTime(), tolerance);
        assertFalse(Double.isNaN(equations.getTime()));
        assertNotSame(initialState, equations.getCompleteState());

        // Check that the integrator has kept the fsal property
        assertTrue(integrator.isFsal());

        // Check that the integrator has used the correct step size
        assertEquals(0.005, integrator.getLastStepSize(), tolerance);

        // Check that the integrator has reached the final time
        assertTrue(finalTime >= equations.getTime());

        // Reset the integrator for the next test
        integrator.resetInternalState();

    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testEmbeddedRungeKuttaIntegratorInvalidInitialState() {

        // Create a simple test function for the integrator
        DoubleIntegrator function = new DoubleIntegrator() {
            @Override
            public double[] computeDerivatives(double t, double[] y) {
                return new double[0];
            }

            @Override
            public Class<? extends ExpandableStatefulODE> getEquationsClass() {
                return null;
            }
        };

        // Create an instance of the embedded Runge-Kutta integrator
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "RK4", false,
                new double[] {0.25, 0.25, 0.5, 0.5},
                new double[][] {
                        new double[] {0.25, 0},
                        new double[] {0.25, 0.25},
                        new double[] {0.5, 0.25},
                        new double[] {0.5, 0.5}},
                new double[] {0.25, 0.25, 0.5, 0.5},
                new RungeKuttaStepInterpolator() {{
                    setOrder(4);
                }},
                0.001, 0.01,
                new double[] {1.0e-8, 1.0e-8},
                new double[] {1.0e-8, 1.0e-8}) {

            @Override
            public int getOrder() {
                return 4;
            }
        };

        // Define test parameters with an invalid initial state
        double initialTime = 0.0;
        double finalTime = 1.0;
        double[] initialState = new double[1]; // Invalid state length

        try {
            integrator.integrate(new ExpandableStatefulODE(function) {}, initialTime, initialState, finalTime);
            fail("Integrator should have thrown an exception");
        } catch (MathIllegalArgumentException e) {
            // Expected exception
        }

        // Reset the integrator for the next test
        integrator.resetInternalState();

    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testEmbeddedRungeKuttaIntegratorInvalidTimeStep() {

        // Create a simple test function for the integrator
        DoubleIntegrator function = new DoubleIntegrator() {
            @Override
            public double[] computeDerivatives(double t, double[] y) {
                return new double[0];
            }

            @Override
            public Class<? extends ExpandableStatefulODE> getEquationsClass() {
                return null;
            }
        };

        // Create an instance of the embedded Runge-Kutta integrator
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "RK4", false,
                new double[] {0.25, 0.25, 0.5, 0.5},
                new double[][] {
                        new double[] {0.25, 0},
                        new double[] {0.25, 0.25},
                        new double[] {0.5, 0.25},
                        new double[] {0.5, 0.5}},
                new double[] {0.25, 0.25, 0.5, 0.5},
                new RungeKuttaStepInterpolator() {{
                    setOrder(4);
                }},
                0.001, 0.01,
                new double[] {1.0e-8, 1.0e-8},
                new double[] {1.0e-8, 1.0e-8}) {

            @Override
            public int getOrder() {
                return 4;
            }
        };

        // Define test parameters with an invalid time step
        double initialTime = 0.0;
        double finalTime = 1.0;
        double[] initialState = new double[] {0.1, 0.2};
        double stepSize = -0.001;

        try {
            integrator.integrate(new ExpandableStatefulODE(function) {}, initialTime, initialState, finalTime, stepSize);
            fail("Integrator should have thrown an exception");
        } catch (MathIllegalArgumentException e) {
            // Expected exception
        }

        // Reset the integrator for the next test
        integrator.resetInternalState();

    }

    @Test(expected = MathIllegalStateException.class)
    public void testEmbeddedRungeKuttaIntegratorInvalidInitialCondition() {

        // Create a simple test function for the integrator
        DoubleIntegrator function = new DoubleIntegrator() {
            @Override
            public double[] computeDerivatives(double t, double[] y) {
                return new double[0];
            }

            @Override
            public Class<? extends ExpandableStatefulODE> getEquationsClass() {
                return null;
            }
        };

        // Create an instance of the embedded Runge-Kutta integrator
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "RK4", false,
                new double[] {0.25, 0.25, 0.5, 0.5},
                new double[][] {
                        new double[] {0.25, 0},
                        new double[] {0.25, 0.25},
                        new double[] {0.5, 0.25},
                        new double[] {0.5, 0.5}},
                new double[] {0.25, 0.25, 0.5, 0.5},
                new RungeKuttaStepInterpolator() {{
                    setOrder(4);
                }},
                0.001, 0.01,
                new double[] {1.0e-8, 1.0e-8},
                new double[] {1.0e-8, 1.0e-8}) {

            @Override
            public int getOrder() {
                return 4;
            }
        };

        // Define test parameters with an invalid initial condition
        double initialTime = 0.0;
        double finalTime = 1.0;
        double[] initialState = new double[] {Double.NaN};

        try {
            integrator.integrate(new ExpandableStatefulODE(function) {}, initialTime, initialState, finalTime);
            fail("Integrator should have thrown an exception");
        } catch (MathIllegalStateException e) {
            // Expected exception
        }

        // Reset the integrator for the next test
        integrator.resetInternalState();

    }

    @Test
    public void testEmbeddedRungeKuttaIntegratorResetInternalState() {

        // Create a simple test function for the integrator
        DoubleIntegrator function = new DoubleIntegrator() {
            @Override
            public double[] computeDerivatives(double t, double[] y) {
                return new double[0];
            }

            @Override
            public Class<? extends ExpandableStatefulODE> getEquationsClass() {
                return null;
            }
        };

        // Create an instance of the embedded Runge-Kutta integrator
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "RK4", false,
                new double[] {0.25, 0.25, 0.5, 0.5},
                new double[][] {
                        new double[] {0.25, 0},
                        new double[] {0.25, 0.25},
                        new double[] {0.5, 0.25},
                        new double[] {0.5, 0.5}},
                new double[] {0.25, 0.25, 0.5, 0.5},
                new RungeKuttaStepInterpolator() {{
                    setOrder(4);
                }},
                0.001, 0.01,
                new double[] {1.0e-8, 1.0e-8},
                new double[] {1.0e-8, 1.0e-8}) {

            @Override
            public int getOrder() {
                return 4;
            }
        };

        // Define test parameters
        double initialTime = 0.0;
        double finalTime = 1.0;
        double[] initialState = new double[] {0.1, 0.2};
        double[] expectedState = new double[] {0.36787944117144233, 0.4331271105555573};
        double tolerance = 1e-6;

        // Perform the integration test
        ExpandableStatefulODE equations = new ExpandableStatefulODE(function) {
            @Override
            public int getDimension() {
                return 2;
            }
        };
        integrator.integrate(equations, initialTime, initialState, finalTime);

        // Validate the test results
        double[] resultState = equations.getCompleteState();
        ODETestUtilities.compareArrays(expectedState, resultState, tolerance);
        assertEquals(finalTime, equations.getTime(), tolerance);
        assertFalse(Double.isNaN(equations.getTime()));
        assertNotSame(initialState, equations.getCompleteState());

        // Reset the integrator state
        integrator.resetInternalState();

        // Perform another integration test with the same parameters
        integrator.integrate(equations, initialTime, initialState, finalTime);

        // Validate the test results
        resultState = equations.getCompleteState();
        ODETestUtilities.compareArrays(expectedState, resultState, tolerance);
        assertEquals(finalTime, equations.getTime(), tolerance);
        assertFalse(Double.isNaN(equations.getTime()));
        assertNotSame(initialState, equations.getCompleteState());

    }

}