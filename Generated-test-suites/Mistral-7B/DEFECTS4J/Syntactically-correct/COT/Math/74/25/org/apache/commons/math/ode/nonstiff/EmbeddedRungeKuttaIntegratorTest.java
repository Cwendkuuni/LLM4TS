import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EmbeddedRungeKuttaIntegratorTest {

    @Mock
    private FirstOrderDifferentialEquations equations;

    @InjectMocks
    private EmbeddedRungeKuttaIntegrator integrator;

    @Test
    public void testConstructorWithScalableTolerances() {
        // Initialize test data and call the constructor
        // ...
        // Assertions
        // ...
    }

    @Test
    public void testConstructorWithVectorTolerances() {
        // Initialize test data and call the constructor
        // ...
        // Assertions
        // ...
    }

    @Test
    public void testGetOrder() {
        int order = integrator.getOrder();
        assertEquals(expectedOrder, order);
    }

    @Test
    public void testGetSafety() {
        double safety = integrator.getSafety();
        assertEquals(expectedSafety, safety, 0.0);
    }

    @Test
    public void testSetSafety() {
        integrator.setSafety(expectedSafety);
        double safety = integrator.getSafety();
        assertEquals(expectedSafety, safety, 0.0);
    }

    @Test
    public void testIntegrate() throws DerivativeException, IntegratorException {
        double[] y0 = new double[]{0.0, 0.0};
        double t0 = 0.0;
        double t = 1.0;
        double[] y = new double[]{1.0, 1.0};

        // Set up expectations for the differential equations
        // ...

        // Call the integrate method
        double actualT = integrator.integrate(equations, t0, y0, t, y);

        // Assertions
        assertEquals(expectedT, actualT, epsilon);
        // ... more assertions on y if necessary
    }

    @Test
    public void testGetMinReduction() {
        double minReduction = integrator.getMinReduction();
        assertEquals(expectedMinReduction, minReduction, epsilon);
    }

    @Test
    public void testSetMinReduction() {
        integrator.setMinReduction(expectedMinReduction);
        double minReduction = integrator.getMinReduction();
        assertEquals(expectedMinReduction, minReduction, epsilon);
    }

    @Test
    public void testGetMaxGrowth() {
        double maxGrowth = integrator.getMaxGrowth();
        assertEquals(expectedMaxGrowth, maxGrowth, epsilon);
    }

    @Test
    public void testSetMaxGrowth() {
        integrator.setMaxGrowth(expectedMaxGrowth);
        double maxGrowth = integrator.getMaxGrowth();
        assertEquals(expectedMaxGrowth, maxGrowth, epsilon);
    }
}