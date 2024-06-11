import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.FirstOrderIntegrator;
import org.apache.commons.math.ode.nonstiff.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class AbstractIntegratorTest {

    @Test
    public void testIntegration() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = t;
            }
        };

        FirstOrderIntegrator integrator = new ClassicalRungeKuttaIntegrator(0.01);

        double[] y = new double[1];
        y[0] = 0;

        double t = 0;
        double tEnd = 10;

        integrator.integrate(equations, t, y, tEnd, y);

        assertEquals(tEnd, integrator.getCurrentStepStart(), 0);
        assertTrue(integrator.getMaxEvaluations() >= integrator.getEvaluations());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegration_DimensionMismatchException() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = t;
                yDot[1] = t * t;
            }
        };

        FirstOrderIntegrator integrator = new ClassicalRungeKuttaIntegrator(0.01);

        double[] y = new double[1];
        y[0] = 0;

        double t = 0;
        double tEnd = 10;

        integrator.integrate(equations, t, y, tEnd, y);
    }

    @Test
    public void testIntegration_MaxEvaluations() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = t;
            }
        };

        FirstOrderIntegrator integrator = new ClassicalRungeKuttaIntegrator(0.01);
        integrator.setMaxEvaluations(10);

        double[] y = new double[1];
        y[0] = 0;

        double t = 0;
        double tEnd = 10;

        try {
            integrator.integrate(equations, t, y, tEnd, y);
            fail("Expected MathIllegalStateException");
        } catch (MathIllegalStateException e) {
            // expected
        }

        assertEquals(10, integrator.getEvaluations());
    }

}