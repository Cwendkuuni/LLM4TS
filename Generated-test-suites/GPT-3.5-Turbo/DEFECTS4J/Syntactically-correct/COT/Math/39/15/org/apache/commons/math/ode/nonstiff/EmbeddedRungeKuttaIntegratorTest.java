import org.junit.Test;
import org.apache.commons.math.ode.ExpandableStatefulODE;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testConstructor() {
        double[] c = {1};
        double[][] a = {{0}};
        double[] b = {1};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        double minStep = 0.1;
        double maxStep = 1.0;
        double scalAbsoluteTolerance = 1e-6;
        double scalRelativeTolerance = 1e-6;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Test", false, c, a, b,
                                                                                   prototype, minStep, maxStep,
                                                                                   scalAbsoluteTolerance, scalRelativeTolerance);

        // TODO: add assertions to check integration object properties
    }
    
    @Test
    public void testIntegrate() {
        // TODO: add test code to test the integrate method
    }

}