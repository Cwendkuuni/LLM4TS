package org.apache.commons.math.ode.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math3.ode.AbstractStepInterpolator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.AbstractEvent;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // empty implementation
            }
        };
    }

    @Test
    public void testConstructor() {
        integrator = new AbstractIntegrator("TestIntegrator");
        assertEquals("TestIntegrator", integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                // implementation
            }
        });
        assertFalse(integrator.getStepHandlers().isEmpty());
    }

    @Test
    public void testClearStepHandlers() {
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                // implementation
            }
        });
        integrator.clearStepHandlers();
        assertTrue(integrator.getStepHandlers().isEmpty());
    }

    @Test
    public void testAddEventHandler() {
        integrator.addEventHandler(new AbstractEvent() {
            @Override
            public double getEventTime() {
                return 0.0;
            }
        }, 1.0, 1.0e-6, 10, new BracketingNthOrderBrentSolver(1.0e-6, 5));
        assertFalse(integrator.getEventHandlers().isEmpty());
    }

    @Test
    public void testClearEventHandler() {
        integrator.addEventHandler(new AbstractEvent() {
            @Override
            public double getEventTime() {
                return 0.0;
            }
        }, 1.0, 1.0e-6, 10, new BracketingNthOrderBrentSolver(1.0e-6, 5));
        integrator.clearEventHandlers();
        assertTrue(integrator.getEventHandlers().isEmpty());
    }

    @Test
    public void testGetCurrentStepStart() {
        integrator.setCurrentStepStart(1.0);
        assertEquals(1.0, integrator.getCurrentStepStart(), 1e-14);
    }

    @Test
    public void testGetCurrentSignedStepsize() {
        integrator.setCurrentSignedStepsize(1.0);
        assertEquals(1.0, integrator.getCurrentSignedStepsize(), 1e-14);
    }

    @Test
    public void testSetMaxEvaluations() {
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetMaxEvaluations() {
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        integrator.resetEvaluations();
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testSetEquations() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = t;
            }
        });
        integrator.setEquations(equations);
        assertEquals(equations, integrator.getEquations());
    }

    @Test
    public void testIntegrate_ThrowsMathIllegalArgumentException() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                throw new IllegalArgumentException("Test Error");
            }
        });
        integrator.setEquations(equations);
        assertThrows(MathIllegalArgumentException.class, () -> integrator.integrate(equations, 1.0));
    }

    @Test
    public void testIntegrate_ThrowsMathIllegalStateException() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = t;
            }
        });
        integrator.setEquations(equations);
        integrator.setMaxEvaluations(0);
        assertThrows(MathIllegalStateException.class, () -> integrator.integrate(equations, 1.0));
    }

    @Test
    public void testAcceptStep_ThrowsMathIllegalStateException() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = t;
            }
        });
        integrator.setEquations(equations);
        integrator.setStateInitialized(false);
        assertThrows(MathIllegalStateException.class, () -> integrator.acceptStep(new AbstractStepInterpolator() {
            @Override
            public double getGlobalPreviousTime() {
                return 0.0;
            }

            @Override
            public double getGlobalCurrentTime() {
                return 0.0;
            }
        }, new double[1], new double[1], 1.0));
    }

    @Test
    public void testAcceptStep_ReturnsCorrectTime() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = t;
            }
        });
        integrator.setEquations(equations);
        integrator.setStateInitialized(true);
        integrator.setCurrentStepStart(0.0);
        integrator.setMaxEvaluations(1000);
        integrator.setCurrentSignedStepsize(FastMath.nextUp(0.1));
        double[] y = new double[1];
        double[] yDot = new double[1];
        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double getGlobalPreviousTime() {
                return 0.0;
            }

            @Override
            public double getGlobalCurrentTime() {
                return 0.1;
            }
        };
        double tEnd = 0.2;
        integrator.acceptStep(interpolator, y, yDot, tEnd);
        assertEquals(0.1, integrator.getCurrentStepStart(), 1e-14);
        assertEquals(FastMath.nextUp(0.1), integrator.getCurrentSignedStepsize(), 1e-14);
        assertEquals(1, integrator.getEvaluations());
        assertTrue(integrator.isLastStep());
        assertEquals(tEnd, integrator.getCurrentStepStart());
        assertEquals(y, integrator.getPrimaryState());
    }

    @Test
    public void testAcceptStep_ThrowsMathIllegalStateException_NoInitialState() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = t;
            }
        });
        integrator.setEquations(equations);
        integrator.setStateInitialized(false);
        integrator.setCurrentStepStart(0.0);
        integrator.setMaxEvaluations(1000);
        integrator.setCurrentSignedStepsize(FastMath.nextUp(0.1));
        double[] y = new double[1];
        double[] yDot = new double[1];
        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double getGlobalPreviousTime() {
                return 0.0;
            }

            @Override
            public double getGlobalCurrentTime() {
                return 0.1;
            }
        };
        double tEnd = 0.2;
        assertThrows(MathIllegalStateException.class, () -> integrator.acceptStep(interpolator, y, yDot, tEnd));
    }

    @Test
    public void testAcceptStep_ThrowsMathIllegalStateException_MaxEvaluationsReached() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = t;
            }
        });
        integrator.setEquations(equations);
        integrator.setStateInitialized(true);
        integrator.setCurrentStepStart(0.0);
        integrator.setMaxEvaluations(1);
        integrator.setCurrentSignedStepsize(FastMath.nextUp(0.1));
        double[] y = new double[1];
        double[] yDot = new double[1];
        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double getGlobalPreviousTime() {
                return 0.0;
            }

            @Override
            public double getGlobalCurrentTime() {
                return 0.1;
            }
        };
        double tEnd = 0.2;
        integrator.acceptStep(interpolator, y, yDot, tEnd);
        integrator.acceptStep(interpolator, y, yDot, tEnd);
        assertThrows(MathIllegalStateException.class, () -> integrator.acceptStep(interpolator, y, yDot, tEnd));
        assertEquals(1, integrator.getEvaluations());
    }

    @Test
    public void testAcceptStep_ReturnsCorrectState() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = t;
            }
        });
        integrator.setEquations(equations);
        integrator.setStateInitialized(true);
        integrator.setCurrentStepStart(0.0);
        integrator.setMaxEvaluations(1000);
        integrator.setCurrentSignedStepsize(FastMath.nextUp(0.1));
        double[] y = new double[1];
        double[] yDot = new double[1];
        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double getGlobalPreviousTime() {
                return 0.0;
            }

            @Override
            public double getGlobalCurrentTime() {
                return 0.1;
            }
        };
        double tEnd = 0.2;
        integrator.acceptStep(interpolator, y, yDot, tEnd);
        assertEquals(0.1, integrator.getCurrentStepStart(), 1e-14);
        assertEquals(FastMath.nextUp(0.1), integrator.getCurrentSignedStepsize(), 1e-14);
        assertEquals(1, integrator.getEvaluations());
        assertTrue(integrator.isLastStep());
        assertEquals(tEnd, integrator.getCurrentStepStart());
        assertEquals(y, integrator.getPrimaryState());
        assertEquals(y[0] + FastMath.nextUp(0.1), y[0], 1e-14);
    }

    @Test
    public void testComputeDerivatives() {
        integrator.setStateInitialized(true);
        double t = 0.1;
        double[] y = new double[1];
        double[] yDot = new double[1];
        integrator.computeDerivatives(t, y, yDot);
        assertEquals(t, yDot[0], 1e-14);
    }

    @Test
    public void testComputeDerivatives_ThrowsMaxCountExceededException() {
        integrator.setStateInitialized(true);
        integrator.setMaxEvaluations(0);
        double t = 0.1;
        double[] y = new double[1];
        double[] yDot = new double[1];
        assertThrows(MaxCountExceededException.class, () -> integrator.computeDerivatives(t, y, yDot));
    }

}