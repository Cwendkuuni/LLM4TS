import org.junit.Test;
import org.apache.commons.math.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math.ode.events.EventHandler;
import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math.ode.sampling.StepHandler;
import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.util.Incrementor;
import org.apache.commons.math.util.Precision;
import org.junit.Assert;

public class AbstractIntegratorTest {
    
    private AbstractIntegrator integrator;
    private ExpandableStatefulODE equations;

    @Test
    public void testIntegrate() throws MathIllegalStateException, MathIllegalArgumentException {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // Compute derivatives
            }

            @Override
            public int getDimension() {
                return 1;
            }
        };
        
        double t0 = 0;
        double[] y0 = new double[]{1};
        double t = 1;
        double[] y = new double[1];
        
        double finalTime = integrator.integrate(equations, t0, y0, t, y);
        
        double expectedFinalTime = t;
        
        Assert.assertEquals(expectedFinalTime, finalTime, Precision.EPSILON);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrate_DimensionMismatchException() throws MathIllegalStateException, MathIllegalArgumentException {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // Compute derivatives
            }

            @Override
            public int getDimension() {
                return 2;
            }
        };
        
        double t0 = 0;
        double[] y0 = new double[]{1};
        double t = 1;
        double[] y = new double[1];
        
        double finalTime = integrator.integrate(equations, t0, y0, t, y);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testIntegrate_DimensionMismatchException2() throws MathIllegalStateException, MathIllegalArgumentException {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // Compute derivatives
            }

            @Override
            public int getDimension() {
                return 1;
            }
        };
        
        double t0 = 0;
        double[] y0 = new double[]{1};
        double t = 1;
        double[] y = new double[2];
        
        double finalTime = integrator.integrate(equations, t0, y0, t, y);
    }
    
    @Test
    public void testIntegrate_setEquations() throws MathIllegalStateException, MathIllegalArgumentException {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // Compute derivatives
            }

            @Override
            public int getDimension() {
                return 1;
            }
        };
        
        double t0 = 0;
        double[] y0 = new double[]{1};
        double t = 1;
        double[] y = new double[1];
        
        ExpandableStatefulODE expandable = new ExpandableStatefulODE(equations);
        integrator.setEquations(expandable);
        
        double finalTime = integrator.integrate(expandable, t0, y0, t, y);
        
        double expectedFinalTime = t;
        
        Assert.assertEquals(expectedFinalTime, finalTime, Precision.EPSILON);
    }
    
}