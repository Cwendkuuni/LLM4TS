import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EmptyEventManager;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RK4Integrator;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testRK2Integrator() {
        double[][] a = {{0.0, 0.5}, {0.5, 1.0}};
        double[] b = {0.25, 0.5};
        double minStep = 0.01;
        double maxStep = 1.0;
        double scalAbsoluteTolerance = 1e-10;
        double scalRelativeTolerance = 1e-6;
        EmbeddedRungeKuttaIntegrator rk2Integrator = new RK2Integrator("RK2", true, a, b, new RungeKuttaStepInterpolator(), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * 2;
            }
        };

        double[] y0 = {0.0};
        double tStart = 0.0;
        double tEnd = 5.0;

        double result = rk2Integrator.integrate(equations, tStart, y0, tEnd, new double[y0.length]);
        assertEquals(5.0, result, 1e-6);
    }

    @Test
    public void testRK2IntegratorWithEvents() {
        double[][] a = {{0.0, 0.5}, {0.5, 1.0}};
        double[] b = {0.25, 0.5};
        double minStep = 0.01;
        double maxStep = 1.0;
        double scalAbsoluteTolerance = 1e-10;
        double scalRelativeTolerance = 1e-6;
        EmptyEventManager manager = new EmptyEventManager();
        EmbeddedRungeKuttaIntegrator rk2Integrator = new RK2Integrator("RK2", true, a, b, new RungeKuttaStepInterpolator(), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance, manager);

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * 2;
            }
        };

        double[] y0 = {0.0};
        double tStart = 0.0;
        double tEnd = 5.0;

        double result = rk2Integrator.integrate(equations, tStart, y0, tEnd, new double[y0.length]);
        assertEquals(5.0, result, 1e-6);
        manager.assertIsEmpty();
    }

    @Test(expected = DeriveException.class)
    public void testRK2IntegratorWithDerivativeException() {
        double[][] a = {{0.0, 0.5}, {0.5, 1.0}};
        double[] b = {0.25, 0.5};
        double minStep = 0.01;
        double maxStep = 1.0;
        double scalAbsoluteTolerance = 1e-10;
        double scalRelativeTolerance = 1e-6;
        EmbeddedRungeKuttaIntegrator rk2Integrator = new RK2Integrator("RK2", true, a, b, new RungeKuttaStepInterpolator(), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) throws DerivativeException {
                throw new DerivativeException("Test Exception");
            }
        };

        double[] y0 = {0.0};
        double tStart = 0.0;
        double tEnd = 5.0;

        rk2Integrator.integrate(equations, tStart, y0, tEnd, new double[y0.length]);
    }

    @Test(expected = IntegratorException.class)
    public void testRK2IntegratorWithIntegratorException() {
        double[][] a = {{0.0, 0.5}, {0.5, 1.0}};
        double[] b = {0.25, 0.5};
        double minStep = 0.01;
        double maxStep = 1.0;
        double scalAbsoluteTolerance = 1e-10;
        double scalRelativeTolerance = 1e-6;
        EmbeddedRungeKuttaIntegrator rk2Integrator = new RK2Integrator("RK2", true, a, b, new RungeKuttaStepInterpolator(), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) throws IntegratorException {
                throw new IntegratorException("Test Exception");
            }
        };

        double[] y0 = {0.0};
        double tStart = 0.0;
        double tEnd = 5.0;

        rk2Integrator.integrate(equations, tStart, y0, tEnd, new double[y0.length]);
    }

    @Test
    public void testRK4Integrator() {
        double[][] a = {{0.0, 0.25, 0.25, 0.5}, {0.25, 0.25, 0.5, 0.75}, {0.25, 0.5, 0.75, 1.0}, {0.5, 0.75, 1.0, 1.0}};
        double[] b = {0.25, 0.25, 0.25, 0.25};
        double minStep = 0.01;
        double maxStep = 1.0;
        double scalAbsoluteTolerance = 1e-10;
        double scalRelativeTolerance = 1e-6;
        EmbeddedRungeKuttaIntegrator rk4Integrator = new RK4Integrator("RK4", false, a, b, new RungeKuttaStepInterpolator(), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.sin(y[0]);
            }
        };

        double[] y0 = {0.0};
        double tStart = 0.0;
        double tEnd = Math.PI;

        double result = rk4Integrator.integrate(equations, tStart, y0, tEnd, new double[y0.length]);
        assertEquals(Math.PI, result, 1e-6);
    }

    @Test
    public void testRK4IntegratorWithEvents() {
        double[][] a = {{0.0, 0.25, 0.25, 0.5}, {0.25, 0.25, 0.5, 0.75}, {0.25, 0.5, 0.75, 1.0}, {0.5, 0.75, 1.0, 1.0}};
        double[] b = {0.25, 0.25, 0.25, 0.25};
        double minStep = 0.01;
        double maxStep = 1.0;
        double scalAbsoluteTolerance = 1e-10;
        double scalRelativeTolerance = 1e-6;
        EmptyEventManager manager = new EmptyEventManager();
        EmbeddedRungeKuttaIntegrator rk4Integrator = new RK4Integrator("RK4", false, a, b, new RungeKuttaStepInterpolator(), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance, manager);

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.sin(y[0]);
            }
        };

        double[] y0 = {0.0};
        double tStart = 0.0;
        double tEnd = Math.PI;

        double result = rk4Integrator.integrate(equations, tStart, y0, tEnd, new double[y0.length]);
        assertEquals(Math.PI, result, 1e-6);
        manager.assertIsEmpty();
    }

    @Test(expected = DeriveException.class)
    public void testRK4IntegratorWithDerivativeException() {
        double[][] a = {{0.0, 0.25, 0.25, 0.5}, {0.25, 0.25, 0.5, 0.75}, {0.25, 0.5, 0.75, 1.0}, {0.5, 0.75, 1.0, 1.0}};
        double[] b = {0.25, 0.25, 0.25, 0.25};
        double minStep = 0.01;
        double maxStep = 1.0;
        double scalAbsoluteTolerance = 1e-10;
        double scalRelativeTolerance = 1e-6;
        EmbeddedRungeKuttaIntegrator rk4Integrator = new RK4Integrator("RK4", false, a, b, new RungeKuttaStepInterpolator(), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) throws DerivativeException {
                throw new DerivativeException("Test Exception");
            }
        };

        double[] y0 = {0.0};
        double tStart = 0.0;
        double tEnd = Math.PI;

        rk4Integrator.integrate(equations, tStart, y0, tEnd, new double[y0.length]);
    }

    @Test(expected = IntegratorException.class)
    public void testRK4IntegratorWithIntegratorException() {
        double[][] a = {{0.0, 0.25, 0.25, 0.5}, {0.25, 0.25, 0.5, 0.75}, {0.25, 0.5, 0.75, 1.0}, {0.5, 0.75, 1.0, 1.0}};
        double[] b = {0.25, 0.25, 0.25, 0.25};
        double minStep = 0.01;
        double maxStep = 1.0;
        double scalAbsoluteTolerance = 1e-10;
        double scalRelativeTolerance = 1e-6;
        EmbeddedRungeKuttaIntegrator rk4Integrator = new RK4Integrator("RK4", false, a, b, new RungeKuttaStepInterpolator(), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) throws IntegratorException {
                throw new IntegratorException("Test Exception");
            }
        };

        double[] y0 = {0.0};
        double tStart = 0.0;
        double tEnd = Math.PI;

        rk4Integrator.integrate(equations, tStart, y0, tEnd, new double[y0.length]);
    }

}