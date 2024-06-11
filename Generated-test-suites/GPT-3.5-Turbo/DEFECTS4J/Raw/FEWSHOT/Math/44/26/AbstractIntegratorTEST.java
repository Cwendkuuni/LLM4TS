import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractIntegratorTest {

    @Test
    public void testComputeDerivatives() {
        FirstOrderDifferentialEquations equations = new TestEquations();
        double t = 0.0;
        double[] y = {1.0, 2.0, 3.0};
        double[] yDot = new double[y.length];

        AbstractIntegrator integrator = new TestIntegrator();
        integrator.setEquations(equations);
        try {
            integrator.computeDerivatives(t, y, yDot);
        } catch (MaxCountExceededException e) {
            fail("MaxCountExceededException thrown");
        }

        assertEquals(3, yDot.length);
        assertEquals(0.0, yDot[0], 1e-15);
        assertEquals(-2.0, yDot[1], 1e-15);
        assertEquals(-6.0, yDot[2], 1e-15);
    }

    public class TestEquations implements FirstOrderDifferentialEquations {
        public int getDimension() {
            return 3;
        }
        public void computeDerivatives(double t, double[] y, double[] yDot)
            throws MaxCountExceededException {
            yDot[0] = 0.0;
            yDot[1] = -2.0;
            yDot[2] = -6.0;
        }
    }

    public class TestIntegrator extends AbstractIntegrator {
        public TestIntegrator() {
            super("TestIntegrator");
        }
        public void integrate(ExpandableStatefulODE equations, double t)
            throws MathIllegalStateException, MathIllegalArgumentException {
            double[] y = equations.getPrimaryState();
            double[] yDot = new double[y.length];
            computeDerivatives(t, y, yDot);
        }
    }

}