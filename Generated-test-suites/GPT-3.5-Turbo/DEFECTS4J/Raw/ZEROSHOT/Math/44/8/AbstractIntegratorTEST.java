import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractIntegratorTest {

    @Test
    public void testIntegrate() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegratorMock();
        double[] y0 = {0.0};
        double[] y = new double[y0.length];
        double t = 0.0;
        double[] expectedY = {1.0};
        double expectedT = 1.0;

        integrator.integrate(null, t, y0, t, y);

        assertArrayEquals(expectedY, y, 0.0);
        assertEquals(expectedT, t, 0.0);
    }

    private class AbstractIntegratorMock extends AbstractIntegrator {

        public AbstractIntegratorMock() {
            super();
        }

        @Override
        public void integrate(ExpandableStatefulODE equations, double t) {
            equations.setTime(t);
            double[] y = equations.getPrimaryState();
            y[0] = 1.0;
        }
    }

}