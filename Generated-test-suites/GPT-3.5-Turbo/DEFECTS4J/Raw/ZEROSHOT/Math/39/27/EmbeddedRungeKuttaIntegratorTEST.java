import org.junit.Test;

import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testIntegrate() {
        // Create the integrator object
        EmbeddedRungeKuttaIntegrator integrator = new MyRungeKuttaIntegrator();

        // Create the differential equations to be solved
        MyDifferentialEquations equations = new MyDifferentialEquations();

        // Set initial conditions
        double[] y0 = {1, 0};
        equations.setInitialConditions(y0);

        // Set integration parameters
        double t0 = 0;
        double t = 10;
        double[] y = new double[y0.length];

        // Integrate the equations
        integrator.integrate(equations, t);

        // Get the final solution
        equations.getSolution(y);

        // Test the final solution
        double[] expected = {22026, 0};
        assertArrayEquals(expected, y, 1e-5);
    }
}

class MyRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {

    public MyRungeKuttaIntegrator() {
        super("MyRungeKuttaIntegrator", false, new double[]{}, new double[][]{}, new double[]{}, null, 0, 0, new double[]{}, new double[]{});
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
        return 0;
    }
}

class MyDifferentialEquations implements ExpandableStatefulODE {

    private double[] state;

    public void setInitialConditions(double[] y0) {
        state = new double[y0.length];
        System.arraycopy(y0, 0, state, 0, y0.length);
    }

    public void getSolution(double[] y) {
        System.arraycopy(state, 0, y, 0, state.length);
    }

    @Override
    public double getTime() {
        return 0;
    }

    @Override
    public void setTime(double t) {

    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {

    }

    @Override
    public int getDimension() {
        return state.length;
    }

    @Override
    public double[] getCompleteState() {
        return state;
    }

    @Override
    public void setCompleteState(double[] completeState) {
        state = new double[completeState.length];
        System.arraycopy(completeState, 0, state, 0, completeState.length);
    }

    @Override
    public boolean isInitialized() {
        return true;
    }

    @Override
    public double[] getSecondaryState(int secondaryStateIndex) {
        return null;
    }

    @Override
    public void setSecondaryState(int secondaryStateIndex, double[] secondaryState) {

    }
}