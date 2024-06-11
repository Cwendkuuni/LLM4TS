import org.apache.commons.math3.ode.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                    MaxCountExceededException, NoBracketingException {
                // empty implementation
            }
        };
    }

    @Test
    public void testConstructor() {
        integrator = new AbstractIntegrator("TestIntegrator");
        assertNotNull(integrator);
        assertEquals("TestIntegrator", integrator.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullName() {
        integrator = new AbstractIntegrator(null);
    }

    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepHandler handler, boolean isLastStep) {
                // empty implementation
            }
        });
        assertTrue(integrator.getStepHandlers().size() == 1);
    }

    @Test
    public void testClearStepHandlers() {
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepHandler handler, boolean isLastStep) {
                // empty implementation
            }
        });
        assertTrue(integrator.getStepHandlers().size() == 1);
        integrator.clearStepHandlers();
        assertTrue(integrator.getStepHandlers().isEmpty());
    }

    @Test
    public void testSetMaxEvaluations() {
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetMaxEvaluations() {
        integrator.setMaxEvaluations(-1);
        assertFalse(integrator.getMaxEvaluations() < 0);
    }

    @Test
    public void testGetEvaluations() {
        integrator.evaluations.incrementCount();
        integrator.evaluations.incrementCount();
        integrator.evaluations.incrementCount();
        assertEquals(3, integrator.getEvaluations());
    }

    @Test
    public void testInitIntegration() {
        integrator.initIntegration(0, new double[0], 1);
        assertTrue(integrator.getStepHandlers().isEmpty());
        assertTrue(integrator.getEventHandlers().isEmpty());
        assertTrue(Double.isNaN(integrator.getCurrentStepStart()));
        assertTrue(Double.isNaN(integrator.getCurrentSignedStepsize()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEquationsNull() {
        integrator.setEquations(null);
    }

    @Test
    public void testIntegrateNullEquations() {
        expException("integrate", new NullPointerException("equations"));
        integrator.integrate(null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateNaNStartTime() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public double[] computeDerivatives(double t, double[] state) {
                return new double[0];
            }
        };
        integrator.setEquations(equations);
        integrator.initIntegration(Double.NaN, new double[0], 1);
        integrator.integrate(equations, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateNullState() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public double[] computeDerivatives(double t, double[] state) {
                return new double[0];
            }
        };
        integrator.setEquations(equations);
        integrator.initIntegration(0, null, 1);
        integrator.integrate(equations, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateNegativeTargetTime() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public double[] computeDerivatives(double t, double[] state) {
                return new double[0];
            }
        };
        integrator.setEquations(equations);
        integrator.initIntegration(0, new double[0], -1);
        integrator.integrate(equations, 0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateMismatchedDimension() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public double[] computeDerivatives(double t, double[] state) {
                return new double[3];
            }
        };
        integrator.setEquations(equations);
        integrator.initIntegration(0, new double[2], 1);
        integrator.integrate(equations, 0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateMaxEvaluations() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public double[] computeDerivatives(double t, double[] state) {
                return new double[0];
            }
        };
        integrator.setEquations(equations);
        integrator.setMaxEvaluations(0);
        integrator.initIntegration(0, new double[0], 1);
        integrator.integrate(equations, 0);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateNoBracketing() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public double[] computeDerivatives(double t, double[] state) {
                return new double[0];
            }
        };
        integrator.setEquations(equations);
        integrator.initIntegration(0, new double[0], 1);
        integrator.integrate(equations, Double.NaN);
    }

    private void expException(String method, Class<? extends Throwable> exceptionType) {
        try {
            integrator.integrate(new ExpandableStatefulODE() {
                @Override
                public double[] computeDerivatives(double t, double[] state) {
                    throw new RuntimeException("TestException");
                }
            }, 0);
            fail("Expected exception: " + exceptionType.getName());
        } catch (Throwable e) {
            if (exceptionType.isInstance(e)) {
                return;
            }
            throw new RuntimeException("Unexpected exception: " + e.getClass().getName(), e);
        }
    }

}