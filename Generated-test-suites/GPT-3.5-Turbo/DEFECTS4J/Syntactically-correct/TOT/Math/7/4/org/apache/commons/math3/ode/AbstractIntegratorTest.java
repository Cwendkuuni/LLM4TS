import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double t0;
    private double[] y0;
    private double t;
    private double[] y;

    @Before
    public void setUp() {
        integrator = new TestIntegrator();
        equations = new TestEquations();
        t0 = 0;
        y0 = new double[]{1, 2, 3};
        t = 1;
        y = new double[3];
    }

    @Test
    public void testIntegration() throws DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException, NoBracketingException {
        double result = integrator.integrate(equations, t0, y0, t, y);
        assertEquals(t, result, 0.0);
        assertEquals(y0[0] + t, y[0], 0.0);
        assertEquals(y0[1] + 2 * t, y[1], 0.0);
        assertEquals(y0[2] + 3 * t, y[2], 0.0);
    }

    private class TestIntegrator extends AbstractIntegrator {

        @Override
        public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
            double[] y = equations.getPrimaryState();
            y[0] = y0[0] + t;
            y[1] = y0[1] + 2 * t;
            y[2] = y0[2] + 3 * t;
        }
    }

    private class TestEquations implements FirstOrderDifferentialEquations {

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {
            yDot[0] = 1;
            yDot[1] = 2;
            yDot[2] = 3;
        }

        @Override
        public int getDimension() {
            return 3;
        }
    }

}