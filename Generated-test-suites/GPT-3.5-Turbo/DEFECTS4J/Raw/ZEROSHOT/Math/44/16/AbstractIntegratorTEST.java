import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {
    private AbstractIntegrator abstractIntegrator;
    private ExpandableStatefulODE expandable;
    
    @Before
    public void setUp() {
        abstractIntegrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) {
            }
        };
        
        expandable = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] + t;
                yDot[1] = y[1] - t;
            } 
        });
        expandable.setTime(0.0);
        expandable.setPrimaryState(new double[] {1.0, 2.0});
        abstractIntegrator.setEquations(expandable);
    }

    @After
    public void tearDown() {
        abstractIntegrator = null;
        expandable = null;
    }
    
    @Test
    public void testGetStepHandlers() {
        assertNotNull(abstractIntegrator.getStepHandlers());
    }
    
    @Test
    public void testClearStepHandlers() {
        abstractIntegrator.clearStepHandlers();
        assertTrue(abstractIntegrator.getStepHandlers().isEmpty());
    }
    
    @Test
    public void testGetEventHandlers() {
        assertNotNull(abstractIntegrator.getEventHandlers());
    }
    
    @Test
    public void testClearEventHandlers() {
        abstractIntegrator.clearEventHandlers();
        assertTrue(abstractIntegrator.getEventHandlers().isEmpty());
    }
    
    @Test
    public void testGetMaxEvaluations() {
        assertEquals(Integer.MAX_VALUE, abstractIntegrator.getMaxEvaluations());
    }
    
    @Test
    public void testSetMaxEvaluations() {
        abstractIntegrator.setMaxEvaluations(1000);
        assertEquals(1000, abstractIntegrator.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        assertEquals(0, abstractIntegrator.getEvaluations());
    }
    
    @Test
    public void testResetEvaluations() {
        abstractIntegrator.resetEvaluations();
        assertEquals(0, abstractIntegrator.getEvaluations());
    }
    
    @Test
    public void testIntegrate() {
        double[] y = new double[] {1.0, 2.0};
        double t = 1.0;
        double[] expected = new double[] {1.0, 2.0};
        assertArrayEquals(expected, y, 0.0001);
    }
    
    @Test
    public void testComputeDerivatives() {
        double t = 1.0;
        double[] y = new double[] {1.0, 2.0};
        double[] yDot = new double[y.length];
        abstractIntegrator.computeDerivatives(t, y, yDot);
        double[] expected = new double[] {2.0, -1.0};
        assertArrayEquals(expected, yDot, 0.0001);
    }
    
    @Test
    public void testSetStateInitialized() {
        abstractIntegrator.setStateInitialized(true);
        assertTrue(abstractIntegrator.statesInitialized);
    }
    
    @Test
    public void testAcceptStep() {
        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            protected void doCopyInterpolationData(StepInterpolator interpolator) {
            }

            @Override
            protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) {
            }
        };
        
        double[] y = new double[expandable.getPrimaryState().length];
        double[] yDot = new double[expandable.getPrimaryState().length];
        double tEnd = 1.0;
        double result = abstractIntegrator.acceptStep(interpolator, y, yDot, tEnd);
        assertEquals(0.0, result, 0.0001);
    }
    
    @Test
    public void testSanityChecks() {
        try {
            abstractIntegrator.sanityChecks(expandable, 0.00001);
            fail("Expected a NumberIsTooSmallException to be thrown");
        } catch (NumberIsTooSmallException e) {
            assertEquals(LocalizedFormats.TOO_SMALL_INTEGRATION_INTERVAL, e.getSpecifier());
            assertTrue(e.getArguments().contains(0.00001));
        }
    }
}