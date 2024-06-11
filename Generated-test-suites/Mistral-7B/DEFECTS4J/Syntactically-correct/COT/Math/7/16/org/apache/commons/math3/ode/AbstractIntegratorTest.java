import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // empty integration method for testing
            }
        };
    }

    @Test
    public void testConstructor() {
        AbstractIntegrator integrator = new AbstractIntegrator("test");
        assertNotNull(integrator.getName());
        assertEquals("test", integrator.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullName() {
        new AbstractIntegrator(null);
    }

    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractIntegrator integrator, boolean isLastStep) {
                // empty handler for testing
            }
        });
        assertFalse(integrator.getStepHandlers().isEmpty());
    }

    @Test
    public void testGetStepHandlers() {
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractIntegrator integrator, boolean isLastStep) {
                // empty handler for testing
            }
        });
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractIntegrator integrator, boolean isLastStep) {
                // empty handler for testing
            }
        });
        List<StepHandler> handlers = integrator.getStepHandlers();
        assertEquals(2, handlers.size());
    }

    @Test
    public void testClearStepHandlers() {
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractIntegrator integrator, boolean isLastStep) {
                // empty handler for testing
            }
        });
        integrator.clearStepHandlers();
        assertTrue(integrator.getStepHandlers().isEmpty());
    }

    @Test
    public void testAddEventHandler() {
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateStep(AbstractIntegrator integrator, double t, double[] y,
                                    double[] yDot, int eventIndex) {
                // empty handler for testing
            }

            @Override
            public double getEventTime() {
                return 0;
            }
        }, 0, 1, 1, new BracketingNthOrderBrentSolver(1, 5));
        assertFalse(integrator.getEventHandlers().isEmpty());
    }

    @Test
    public void testGetEventHandlers() {
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateStep(AbstractIntegrator integrator, double t, double[] y,
                                    double[] yDot, int eventIndex) {
                // empty handler for testing
            }

            @Override
            public double getEventTime() {
                return 0;
            }
        }, 0, 1, 1, new BracketingNthOrderBrentSolver(1, 5));
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateStep(AbstractIntegrator integrator, double t, double[] y,
                                    double[] yDot, int eventIndex) {
                // empty handler for testing
            }

            @Override
            public double getEventTime() {
                return 1;
            }
        }, 0, 1, 1, new BracketingNthOrderBrentSolver(1, 5));
        List<EventHandler> handlers = integrator.getEventHandlers();
        assertEquals(2, handlers.size());
    }

    @Test
    public void testClearEventHandlers() {
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateStep(AbstractIntegrator integrator, double t, double[] y,
                                    double[] yDot, int eventIndex) {
                // empty handler for testing
            }

            @Override
            public double getEventTime() {
                return 0;
            }
        }, 0, 1, 1, new BracketingNthOrderBrentSolver(1, 5));
        integrator.clearEventHandlers();
        assertTrue(integrator.getEventHandlers().isEmpty());
    }

    @Test
    public void testGetCurrentStepStart() {
        integrator.initIntegration(0, new double[0], 1);
        integrator.setCurrentStepStart(2);
        assertEquals(2, integrator.getCurrentStepStart(), 1e-15);
    }

    @Test
    public void testGetCurrentSignedStepsize() {
        integrator.initIntegration(0, new double[0], 1);
        integrator.setCurrentSignedStepsize(3);
        assertEquals(3, integrator.getCurrentSignedStepsize(), 1e-15);
    }

    @Test
    public void testSetMaxEvaluations() {
        integrator.setMaxEvaluations(1);
        assertEquals(1, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetMaxEvaluations() {
        integrator.setMaxEvaluations(5);
        assertEquals(5, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        integrator.setMaxEvaluations(10);
        integrator.evaluate(0, new double[0], new double[0], 1, new double[0]);
        assertEquals(1, integrator.getEvaluations());
    }

    @Test(expected = AssertionError.class)
    public void testSetMaxEvaluationsNegative() {
        integrator.setMaxEvaluations(-1);
    }

    @Test
    public void testIntegrateWithEmptyEquations() {
        integrator.initIntegration(0, new double[0], 1);
        integrator.integrate(new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 0;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // empty method for testing
            }
        }, 1);
        assertEquals(1, integrator.getEvaluations());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateWithMismatchedDimensions() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 5;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // empty method for testing
            }
        };
        integrator.initIntegration(0, new double[3], 1);
        integrator.integrate(equations, 1);
    }

    @Test
    public void testIntegrate() {
        double[] y0 = new double[] {1, 2};
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0];
                yDot[1] = y[1];
            }
        };
        integrator.initIntegration(0, y0, 2);
        integrator.integrate(equations, 2);
        double[] y = integrator.getPrimaryState();
        assertArrayEquals(new double[] {1.27589828, 2.55179659}, y, 1e-15);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateWithTooSmallStep() {
        double[] y0 = new double[] {1, 2};
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0];
                yDot[1] = y[1];
            }
        };
        integrator.initIntegration(0, y0, 2);
        integrator.setMaxStep(1e-10);
        integrator.integrate(equations, 2);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateWithMaxEvaluationsReached() {
        double[] y0 = new double[] {1, 2};
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0];
                yDot[1] = y[1];
            }
        };
        integrator.initIntegration(0, y0, 2);
        integrator.setMaxEvaluations(0);
        integrator.integrate(equations, 2);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateWithNoBracketing() {
        double[] y0 = new double[] {1, 2};
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0];
                yDot[1] = y[1];
            }

            @Override
            public void addEventHandler(EventHandler eventHandler) {
                throw new UnsupportedOperationException();
            }
        };
        integrator.initIntegration(0, y0, 2);
        integrator.integrate(equations, 2);
    }

}