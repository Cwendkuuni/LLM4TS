import org.junit.Assert;
import org.junit.Test;

public class AbstractIntegratorTest {

    @Test
    public void testComputeDerivatives() {

        // Arrange
        AbstractIntegrator integrator = new AbstractIntegrator() {

            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // Not used in this test
            }
        };
        double t = 0.0;
        double[] y = new double[]{0.0};
        double[] yDot = new double[]{0.0};

        // Act
        integrator.computeDerivatives(t, y, yDot);

        // Assert
        Assert.assertEquals(1, integrator.getEvaluations());
    }

}