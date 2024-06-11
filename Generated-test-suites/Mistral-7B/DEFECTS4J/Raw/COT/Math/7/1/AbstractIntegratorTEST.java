import org.apache.commons.math3.ode.*;
import org.junit.Assert;
import org.junit.Test;

public class AbstractIntegratorTest {

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {};
        StepHandler handler = new StepHandler() {};
        integrator.addStepHandler(handler);
        Assert.assertEquals(1, integrator.getStepHandlers().size());
        integrator.clearStepHandlers();
        Assert.assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator() {};
        EventHandler handler = new EventHandler() {};
        integrator.addEventHandler(handler, 1.0, 1.0E-6, 10);
        Assert.assertEquals(1, integrator.getEventHandlers().size());
        integrator.clearEventHandlers();
        Assert.assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator() {};
        EventHandler handler = new EventHandler() {};
        integrator.addEventHandler(handler, 1.0, 1.0E-6, 10);
        integrator.clearEventHandlers();
        Assert.assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator() {};
        integrator.setMaxEvaluations(10);
        Assert.assertEquals(10, integrator.getMaxEvaluations());
        integrator.setMaxEvaluations(-1);
        Assert.assertEquals(-1, integrator.getMaxEvaluations());
    }

    @Test
    public void testIntegrate_Success() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator() {};
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }
        };
        integrator.setEquations(equations);
        double[] y0 = {1.0};
        double t = 10.0;
        double[] y = new double[y0.length];
        integrator.integrate(equations, 0.0, y0, t, y);
        Assert.assertArrayEquals(new double[]{10.0}, y, 0.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrate_DimensionMismatchException() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator() {};
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
                yDot[1] = y[1] * 0.1;
            }
        };
        integrator.setEquations(equations);
        double[] y0 = {1.0, 2.0};
        double t = 10.0;
        double[] y = new double[y0.length];
        integrator.integrate(equations, 0.0, y0, t, y);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrate_MaxCountExceededException() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            private int maxEvaluations = 1;

            @Override
            public int getMaxEvaluations() {
                return maxEvaluations;
            }

            @Override
            public void setMaxEvaluations(int maxEvaluations) {
                this.maxEvaluations = maxEvaluations;
            }
        };
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }
        };
        integrator.setEquations(equations);
        double[] y0 = {1.0};
        double t = 10.0;
        double[] y = new double[y0.length];
        integrator.integrate(equations, 0.0, y0, t, y);
        integrator.integrate(equations, 0.0, y0, t, y);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrate_NoBracketingException() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator() {};
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 0.0;
            }
        };
        integrator.setEquations(equations);
        double[] y0 = {1.0};
        double t = 1.0;
        double[] y = new double[y0.length];
        integrator.integrate(equations, 0.0, y0, t, y);
    }

}