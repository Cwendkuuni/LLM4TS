import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.ode.AbstractStepInterpolator;
import org.apache.commons.math3.ode.EventState;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.StepHandler;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Incrementor;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class AbstractIntegratorTest {

    @Test
    public void testAbstractIntegrator() throws Exception {
        // Test constructor with name
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        assertEquals("TestIntegrator", integrator.getName());

        // Test constructor without name
        AbstractIntegrator integrator2 = new AbstractIntegrator();
        assertEquals(null, integrator2.getName());

        // Test addStepHandler
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                // do nothing
            }
        });
        assertEquals(1, integrator.getStepHandlers().size());

        // Test addEventHandler
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateState(double t, double[] y, double[] yDot, EventState state) {
                state.setAccepted(true);
            }

            @Override
            public void handleEvent(double t, double[] y, double[] yDot) {
                // do nothing
            }
        }, 0.0, 1.0, 5, new BracketingNthOrderBrentSolver(1.0, 5));
        assertEquals(1, integrator.getEventHandlers().size());

        // Test getCurrentStepStart
        integrator.integrate(new TestEquations(1.0), 0.0, new double[1], 1.0, new double[1]);
        assertEquals(1.0, integrator.getCurrentStepStart(), 1e-14);

        // Test getCurrentSignedStepsize
        integrator.integrate(new TestEquations(1.0), 0.0, new double[1], 1.0, new double[1]);
        assertEquals(Double.NaN, integrator.getCurrentSignedStepsize(), 1e-14);

        // Test getMaxEvaluations
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());

        // Test getEvaluations
        integrator.integrate(new TestEquations(1.0), 0.0, new double[1], 1.0, new double[1]);
        integrator.integrate(new TestEquations(1.0), 1.0, new double[1], 2.0, new double[1]);
        assertEquals(2, integrator.getEvaluations());

        // Test resetEvaluations
        integrator.resetEvaluations();
        assertEquals(0, integrator.getEvaluations());

        // Test setEquations
        integrator.setEquations(new TestEquations(1.0));

        // Test integrate with valid inputs
        double[] y = new double[1];
        double[] yDot = new double[1];
        integrator.integrate(new TestEquations(1.0), 0.0, new double[1], 1.0, y);
        assertEquals(1.0, y[0], 1e-14);

        // Test integrate with invalid input: different dimensions
        assertThrows(DimensionMismatchException.class, () -> {
            integrator.integrate(new TestEquations(2.0), 0.0, new double[1], 1.0, y);
        });

        // Test integrate with invalid input: wrong input size
        assertThrows(DimensionMismatchException.class, () -> {
            integrator.integrate(new TestEquations(1.0), 0.0, new double[2], 1.0, y);
        });

        // Test integrate with invalid input: wrong output size
        assertThrows(DimensionMismatchException.class, () -> {
            integrator.integrate(new TestEquations(1.0), 0.0, new double[1], 1.0, new double[2]);
        });

        // Test integrate with invalid input: negative max evaluations
        integrator.setMaxEvaluations(-1);
        assertThrows(IllegalArgumentException.class, () -> {
            integrator.integrate(new TestEquations(1.0), 0.0, new double[1], 1.0, y);
        });

        // Test integrate with wrong max evaluations
        integrator.setMaxEvaluations(0);
        assertThrows(MaxCountExceededException.class, () -> {
            integrator.integrate(new TestEquations(1.0), 0.0, new double[1], 1.0, y);
        });

        // Test integrate with wrong input: negative time
        integrator.setMaxEvaluations(10);
        assertThrows(IllegalArgumentException.class, () -> {
            integrator.integrate(new TestEquations(1.0), -1.0, new double[1], 1.0, y);
        });

        // Test integrate with wrong input: null input
        integrator.setMaxEvaluations(10);
        assertThrows(NullPointerException.class, () -> {
            integrator.integrate(new TestEquations(1.0), null, new double[1], 1.0, y);
        });

        // Test integrate with wrong input: null output
        integrator.setMaxEvaluations(10);
        assertThrows(NullPointerException.class, () -> {
            integrator.integrate(new TestEquations(1.0), 0.0, null, 1.0, y);
        });

        // Test integrate with wrong input: null derivatives
        integrator.setMaxEvaluations(10);
        assertThrows(NullPointerException.class, () -> {
            integrator.integrate(new TestEquations(1.0), 0.0, new double[1], 1.0, null);
        });

        // Test acceptStep with valid inputs
        AbstractStepInterpolator interpolator = new TestAbstractStepInterpolator();
        integrator.acceptStep(interpolator, y, yDot, 1.0);
        assertEquals(1.0, interpolator.getGlobalCurrentTime(), 1e-14);

        // Test acceptStep with wrong input: null interpolator
        assertThrows(NullPointerException.class, () -> {
            integrator.acceptStep(null, y, yDot, 1.0);
        });

        // Test acceptStep with wrong input: null state
        assertThrows(NullPointerException.class, () -> {
            integrator.acceptStep(interpolator, null, yDot, 1.0);
        });

        // Test acceptStep with wrong input: null derivatives
        assertThrows(NullPointerException.class, () -> {
            integrator.acceptStep(interpolator, y, null, 1.0);
        });

        // Test acceptStep with wrong input: negative time
        assertThrows(IllegalArgumentException.class, () -> {
            integrator.acceptStep(interpolator, y, yDot, -1.0);
        });

        // Test sanityChecks with valid inputs
        integrator.integrate(new TestEquations(1.0), 0.0, new double[1], 1.0, y);
        integrator.sanityChecks(new TestEquations(1.0), 1.0);

        // Test sanityChecks with wrong input: negative time
        assertThrows(NumberIsTooSmallException.class, () -> {
            integrator.sanityChecks(new TestEquations(1.0), -1.0);
        });

        // Test sanityChecks with wrong input: zero time difference
        integrator.sanityChecks(new TestEquations(1.0), 0.0);

        // Test sanityChecks with wrong input: null equations
        assertThrows(NullPointerException.class, () -> {
            integrator.sanityChecks(null, 1.0);
        });

    }

}

class TestEquations implements FirstOrderDifferentialEquations {

    private final double time;

    public TestEquations(double time) {
        this.time = time;
    }

    @Override
    public int getDimension() {
        return 1;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = y[0] + 1.0;
    }

    @Override
    public void setTime(double t) {
        this.time = t;
    }

    @Override
    public double getTime() {
        return time;
    }

    @Override
    public void setPrimaryState(double[] y) {
        // do nothing
    }

    @Override
    public double[] getPrimaryState() {
        return new double[1];
    }

}

class TestAbstractStepInterpolator extends AbstractStepInterpolator {

    @Override
    public double getGlobalPreviousTime() {
        return 0.0;
    }

    @Override
    public double getGlobalCurrentTime() {
        return 0.0;
    }

    @Override
    public double[] getInterpolatedState() {
        return new double[1];
    }

    @Override
    public void setInterpolatedTime(double t) {
        // do nothing
    }

    @Override
    public void setSoftPreviousTime(double t) {
        // do nothing
    }

    @Override
    public void setSoftCurrentTime(double t) {
        // do nothing
    }

}