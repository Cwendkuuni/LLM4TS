import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private ExpandableStatefulODE equations;

    @Before
    public void setUp() {
        integrator = new AbstractIntegrator("testIntegrator") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // empty implementation
            }
        };
        equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }
        });
    }

    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                Assert.assertTrue(isLastStep);
            }
        });
        integrator.getStepHandlers().forEach(handler -> Assert.assertNotNull(handler));
    }

    @Test
    public void testAddEventHandler() {
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void handle(double t, double[] y, double tStep, double[] yDot, int ls,
                              EventState eventState) {
                Assert.assertFalse(ls);
            }
        }, 0.1, 0.001, 10, new BracketingNthOrderBrentSolver(0.001, 5));
        integrator.getEventHandlers().forEach(handler -> Assert.assertNotNull(handler));
    }

    @Test
    public void testIntegrate() throws MathIllegalStateException, MathIllegalArgumentException {
        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, new double[1], 1.0, new double[1]);
        Assert.assertEquals(1.0, integrator.getCurrentStepStart(), 0.0);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateWithWrongInitialCondition()
            throws MathIllegalStateException, MathIllegalArgumentException {
        ExpandableStatefulODE wrongEquations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }
        });
        wrongEquations.setPrimaryState(new double[1]);
        integrator.setEquations(wrongEquations);
        integrator.integrate(equations, 0.0, new double[1], 1.0, new double[1]);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateWithWrongFinalCondition()
            throws MathIllegalStateException, MathIllegalArgumentException {
        integrator.integrate(equations, 0.0, new double[1], 2.0, new double[1]);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateWithMaxEvaluationsReached()
            throws MathIllegalStateException, MathIllegalArgumentException {
        integrator.setMaxEvaluations(1);
        integrator.integrate(equations, 0.0, new double[1], 1.0, new double[1]);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testComputeDerivativesWithMaxEvaluationsReached()
            throws MaxCountExceededException {
        integrator.computeDerivatives(0.0, new double[1], new double[1]);
        integrator.computeDerivatives(0.0, new double[1], new double[1]);
        integrator.computeDerivatives(0.0, new double[1], new double[1]);
        integrator.computeDerivatives(0.0, new double[1], new double[1]);
        integrator.computeDerivatives(0.0, new double[1], new double[1]);
    }

    @Test
    public void testResetEvaluations() {
        integrator.resetEvaluations();
        integrator.computeDerivatives(0.0, new double[1], new double[1]);
        integrator.computeDerivatives(0.0, new double[1], new double[1]);
        integrator.computeDerivatives(0.0, new double[1], new double[1]);
        integrator.resetEvaluations();
        integrator.computeDerivatives(0.0, new double[1], new double[1]);
        integrator.computeDerivatives(0.0, new double[1], new double[1]);
        integrator.computeDerivatives(0.0, new double[1], new double[1]);
        integrator.resetEvaluations();
        integrator.computeDerivatives(0.0, new double[1], new double[1]);
        integrator.computeDerivatives(0.0, new double[1], new double[1]);
        integrator.computeDerivatives(0.0, new double[1], new double[1]);
        Assert.assertEquals(0, integrator.getEvaluations());
    }

    @Test(expected = IllegalStateException.class)
    public void testAcceptStepWithInvalidPreviousTime()
            throws MathIllegalStateException, MathIllegalArgumentException {
        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double getGlobalPreviousTime() {
                throw new IllegalStateException();
            }
        };
        integrator.acceptStep(interpolator, new double[1], new double[1], 1.0);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testAcceptStepWithStepError()
            throws MathIllegalStateException, MathIllegalArgumentException {
        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double getGlobalPreviousTime() {
                return 0.0;
            }

            @Override
            public double getGlobalCurrentTime() {
                return 1.0;
            }

            @Override
            public void setInterpolatedTime(double time) {
                throw new MathIllegalStateException();
            }

            @Override
            public double[] getInterpolatedState() {
                throw new MathIllegalStateException();
            }
        };
        integrator.acceptStep(interpolator, new double[1], new double[1], 1.0);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testAcceptStepWithEventError()
            throws MathIllegalStateException, MathIllegalArgumentException {
        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double getGlobalPreviousTime() {
                return 0.0;
            }

            @Override
            public double getGlobalCurrentTime() {
                return 1.0;
            }

            @Override
            public void setInterpolatedTime(double time) {
                throw new MathIllegalStateException();
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[1];
            }

            @Override
            public EventState getEventState() {
                throw new MathIllegalStateException();
            }
        };
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void handle(double t, double[] y, double tStep, double[] yDot, int ls,
                              EventState eventState) {
                throw new MathIllegalStateException();
            }
        }, 0.0, 0.0, 0, null);
        integrator.acceptStep(interpolator, new double[1], new double[1], 1.0);
    }
}