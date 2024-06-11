import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private ExpandableStatefulODE equations;

    @Before
    public void setUp() {
        integrator = new MockIntegrator();

        FirstOrderDifferentialEquations ode = new MockEquations();
        double t0 = 0.0;
        double[] y0 = new double[] {1.0, 2.0, 3.0};
        equations = new ExpandableStatefulODE(ode);
        equations.setTime(t0);
        equations.setPrimaryState(y0);
        integrator.setEquations(equations);
    }

    @Test
    public void testIntegrate() {
        double t = 1.0;
        double[] expectedY = new double[] {3.0, 4.0, 5.0};
        double[] y = new double[3];

        integrator.integrate(equations, t, expectedY, t, y);

        assertArrayEquals(expectedY, y, 0.0001);
    }

    @Test
    public void testGetCurrentStepStart() {
        double expectedStepStart = 0.0;
        double stepStart = integrator.getCurrentStepStart();

        assertEquals(expectedStepStart, stepStart, 0.0001);
    }

    @Test
    public void testGetCurrentSignedStepsize() {
        double expectedStepSize = 0.0;
        double stepSize = integrator.getCurrentSignedStepsize();

        assertEquals(expectedStepSize, stepSize, 0.0001);
    }

    @Test
    public void testSetMaxEvaluations() {
        int expectedMaxEvaluations = 100;
        integrator.setMaxEvaluations(expectedMaxEvaluations);
        int maxEvaluations = integrator.getMaxEvaluations();

        assertEquals(expectedMaxEvaluations, maxEvaluations);
    }

    @Test
    public void testGetMaxEvaluations() {
        int expectedMaxEvaluations = Integer.MAX_VALUE;
        int maxEvaluations = integrator.getMaxEvaluations();

        assertEquals(expectedMaxEvaluations, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        int expectedEvaluations = 0;
        int evaluations = integrator.getEvaluations();

        assertEquals(expectedEvaluations, evaluations);
    }

    @Test
    public void testResetEvaluations() {
        int expectedEvaluations = 0;
        integrator.computeDerivatives(0.0, new double[3], new double[3]);
        integrator.resetEvaluations();
        int evaluations = integrator.getEvaluations();

        assertEquals(expectedEvaluations, evaluations);
    }
}