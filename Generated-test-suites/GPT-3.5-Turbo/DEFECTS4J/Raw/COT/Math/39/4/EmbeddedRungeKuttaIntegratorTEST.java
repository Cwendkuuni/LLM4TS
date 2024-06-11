import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;

    @Before
    public void setUp() {
        // Initialize the integrator
        double[] c = {1.0};
        double[][] a = {{1.0}};
        double[] b = {0.5, 0.5};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        double minStep = 0.01;
        double maxStep = 1.0;
        double scalAbsoluteTolerance = 1e-6;
        double scalRelativeTolerance = 1e-6;

        integrator = new EmbeddedRungeKuttaIntegrator("testIntegrator", true, c, a, b, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    @Test
    public void testIntegrate() {
        // Create test functions and equations
        TestFunction[] testFunctions = new TestFunction[] {
            new TestFunction() {
                public double value(double x) {
                    return x;
                }
            },
            new TestFunction() {
                public double value(double x) {
                    return x * x;
                }
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = testFunctions[0].value(t);
                yDot[1] = testFunctions[1].value(t);
            }

            public int getDimension() {
                return 2;
            }
        };        

        // Define initial conditions and target time
        ExpandableStatefulODE expandableODE = new ExpandableStatefulODE(equations);
        double[] initialState = {0.0, 0.0};
        expandableODE.setTime(0.0);
        expandableODE.setPrimaryState(initialState, 0.0);
        double targetTime = 1.0;

        // Integrate the equations        
        integrator.integrate(expandableODE, targetTime);

        // Check the result
        double[] finalState = expandableODE.getPrimaryState();
        assertEquals(targetTime, expandableODE.getTime(), 1e-10);
        assertEquals(testFunctions[0].value(targetTime), finalState[0], 1e-10);
        assertEquals(testFunctions[1].value(targetTime), finalState[1], 1e-10);
    }
}