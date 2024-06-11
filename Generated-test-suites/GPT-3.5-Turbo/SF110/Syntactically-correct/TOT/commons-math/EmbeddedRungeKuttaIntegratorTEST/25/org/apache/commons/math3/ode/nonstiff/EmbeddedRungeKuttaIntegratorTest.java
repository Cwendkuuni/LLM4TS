import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    private Embedded1Integrator integrator;
    
    @Before
    public void setUp() {
        this.integrator = new Embedded1Integrator("Test1", true, new double[]{0.1, 0.2, 0.3}, new double[][]{{0.1, 0.2, 0.3}, {0.4, 0.5, 0.6}, {0.7, 0.8, 0.9}}, new double[]{0.1, 0.2, 0.3}, new RungeKuttaStepInterpolator(), 0.01, 1.0, 0.001, 0.01);
    }

    @Test
    public void testGetOrder() {
        assertEquals(1, this.integrator.getOrder());
    }
    
    @Test
    public void testGetSafety() {
        assertEquals(0.9, this.integrator.getSafety(), 0.0001);
    }
    
    @Test
    public void testSetSafety() {
        this.integrator.setSafety(0.7);
        assertEquals(0.7, this.integrator.getSafety(), 0.0001);
    }
    
    @Test
    public void testGetMinReduction() {
        assertEquals(0.2, this.integrator.getMinReduction(), 0.0001);
    }
    
    @Test
    public void testSetMinReduction() {
        this.integrator.setMinReduction(0.1);
        assertEquals(0.1, this.integrator.getMinReduction(), 0.0001);
    }
    
    @Test
    public void testGetMaxGrowth() {
        assertEquals(10.0, this.integrator.getMaxGrowth(), 0.0001);
    }
    
    @Test
    public void testSetMaxGrowth() {
        this.integrator.setMaxGrowth(5.0);
        assertEquals(5.0, this.integrator.getMaxGrowth(), 0.0001);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateWithSmallStepSize() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(2);
        equations.addSecondaryEquations(new SecondaryODE[2]);
        double[] y0 = new double[]{1.0, 2.0};
        equations.setTime(0.0);
        equations.setCompleteState(y0);
        double t = 1.0;
        
        this.integrator.integrate(equations, t);
    }
    
    @Test
    public void testIntegrateWithLargeStepSize() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(2);
        equations.addSecondaryEquations(new SecondaryODE[2]);
        double[] y0 = new double[]{1.0, 2.0};
        equations.setTime(0.0);
        equations.setCompleteState(y0);
        double t = 1.0;
        
        this.integrator.integrate(equations, t);
        
        assertEquals(t, equations.getTime(), 0.0001);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateWithDifferentDimensions() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(2);
        equations.addSecondaryEquations(new SecondaryODE[3]);
        double[] y0 = new double[]{1.0, 2.0};
        equations.setTime(0.0);
        equations.setCompleteState(y0);
        double t = 1.0;
        
        this.integrator.integrate(equations, t);
    }
}