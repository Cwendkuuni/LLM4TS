import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.apache.commons.math3.ode.test.ODETestUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbedgedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {

        // Testing a basic example with a simple function
        double[][] funcValues = {
            {0.0, 1.0},
            {1.0, 2.0},
            {2.0, 3.0},
            {3.0, 4.0}
        };

        double[] initialValues = {0.0, 1.0};

        // Create a test integrator with given Butcher array
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[]{0.25, 0.25, 0.5, 0.25},
                new double[][]{{0.25, 0.0, 0.0, 0.0},
                        {0.0, 0.25, 0.0, 0.0},
                        {0.0, 0.0, 0.5, 0.0},
                        {0.0, 0.0, 0.0, 0.5}},
                new double[]{0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator()) {
                    @Override
                    protected int getOrder() {
                        return 4;
                    }
                };

        // Create a test ODE and set up test handlers
        ExpandableStatefulODE ode = new ExpandableStatefulODE(2) {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public int getPrimaryDim() {
                return 2;
            }

            @Override
            public int getSecondaryDim() {
                return 0;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                for (int i = 0; i < y.length; i++) {
                    yDot[i] = funcValues[(int) Math.floor(t / 1.0)][i + 1];
                }
            }
        };
        ode.setStepHandler(new StepHandlerAdapter() {
            @Override
            public void handleStep(double t, double[] y) {
                System.out.println("t=" + t + ", y=" + ODETestUtils.format(y));
            }
        });

        // Integrate the test ODE using the test integrator
        integrator.integrate(ode, 6.0);

        // Verify the results
        double[] expectedFinalValues = {3.0, 4.0};
        assertArrayEquals(expectedFinalValues, ode.getCompleteState(), 1e-6);

        // Test integration with a function that has a singularity
        double[][] funcValuesSingularity = {
            {0.0, 1.0},
            {1.0, 2.0},
            {2.0, 3.0},
            {3.0, 1e-5}, // Singularity
            {4.0, 4.0},
            {5.0, 5.0}
        };

        double[] initialValuesSingularity = {0.0, 1.0};

        integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[]{0.25, 0.25, 0.5, 0.25},
                new double[][]{{0.25, 0.0, 0.0, 0.0},
                        {0.0, 0.25, 0.0, 0.0},
                        {0.0, 0.0, 0.5, 0.0},
                        {0.0, 0.0, 0.0, 0.5}},
                new double[]{0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator()) {
                    @Override
                    protected int getOrder() {
                        return 4;
                    }
                };

        ode = new ExpandableStatefulODE(2) {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public int getPrimaryDim() {
                return 2;
            }

            @Override
            public int getSecondaryDim() {
                return 0;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                for (int i = 0; i < y.length; i++) {
                    yDot[i] = funcValuesSingularity[(int) Math.floor(t / 1.0)][i + 1];
                }
            }
        };

        integrator.integrate(ode, 6.0);

        double[] expectedFinalValuesSingularity = {3.0, 1e-5};
        assertArrayEquals(expectedFinalValuesSingularity, ode.getCompleteState(), 1e-6);

        // Test exception handling
        double[] initialValuesException = {0.0, 0.0};
        ExpandableStatefulODE odeException = new ExpandableStatefulODE(2) {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public int getPrimaryDim() {
                return 2;
            }

            @Override
            public int getSecondaryDim() {
                return 0;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                throw new MathIllegalArgumentException("Invalid input");
            }
        };

        integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[]{0.25, 0.25, 0.5, 0.25},
                new double[][]{{0.25, 0.0, 0.0, 0.0},
                        {0.0, 0.25, 0.0, 0.0},
                        {0.0, 0.0, 0.5, 0.0},
                        {0.0, 0.0, 0.0, 0.5}},
                new double[]{0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator()) {
                    @Override
                    protected int getOrder() {
                        return 4;
                    }
                };

        try {
            integrator.integrate(odeException, 6.0);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        // Test exception handling with a null ODE
        ExpandableStatefulODE odeNull = null;
        try {
            integrator.integrate(odeNull, 6.0);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        // Test exception handling with a null initial value array
        double[] initialValuesNull = null;
        try {
            integrator.integrate(ode, 6.0, initialValuesNull);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        // Test exception handling with a negative initial step size
        integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[]{0.25, 0.25, 0.5, 0.25},
                new double[][]{{0.25, 0.0, 0.0, 0.0},
                        {0.0, 0.25, 0.0, 0.0},
                        {0.0, 0.0, 0.5, 0.0},
                        {0.0, 0.0, 0.0, 0.5}},
                new double[]{0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator()) {
                    @Override
                    protected int getOrder() {
                        return 4;
                    }
                };

        integrator.setMinStep(-1.0);
        try {
            integrator.integrate(ode, 6.0);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        // Test exception handling with a null tolerance array
        integrator.setTolerances(null);
        try {
            integrator.integrate(ode, 6.0);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        // Test exception handling with an empty tolerance array
        integrator.setTolerances(new double[0]);
        try {
            integrator.integrate(ode, 6.0);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        // Test exception handling with negative tolerance values
        integrator.setTolerances(new double[]{-1.0, -1.0});
        try {
            integrator.integrate(ode, 6.0);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

    }

}