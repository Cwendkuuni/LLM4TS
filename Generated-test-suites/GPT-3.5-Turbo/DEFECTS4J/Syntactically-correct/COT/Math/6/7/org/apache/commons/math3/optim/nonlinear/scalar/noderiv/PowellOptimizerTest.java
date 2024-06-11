import org.junit.Test;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;

public class PowellOptimizerTest {
    private static final double EPSILON = 1e-10;
    
    // Extract and list all the public methods including their signatures
    // test constructor with rel, abs, and checker
	@Test
	public void testConstructorWithRelAbsAndChecker() {
		double rel = 0.01;
		double abs = 0.01;
		ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>();
		PowellOptimizer powellOptimizer = new PowellOptimizer(rel, abs, checker);
		assertNotNull(powellOptimizer);
	}
	
	// test constructor with rel and abs
	@Test
	public void testConstructorWithRelAndAbs() {
		double rel = 0.01;
		double abs = 0.01;
		PowellOptimizer powellOptimizer = new PowellOptimizer(rel, abs);
		assertNotNull(powellOptimizer);
	}
    
    // test doOptimize method
    @Test
    public void testDoOptimize() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.01);
    
        // test the optimization result
        PointValuePair result = powellOptimizer.doOptimize();
        assertNotNull(result);
        assertEquals(2, result.getPoint().length);
        assertEquals(0.0, result.getPoint()[0], EPSILON);
        assertEquals(0.0, result.getPoint()[1], EPSILON);
    }
    
    // test newPointAndDirection method
    @Test
    public void testNewPointAndDirection() {
        double[] p = {0.0, 0.0};
        double[] d = {1.0, 0.0};
        double alphaMin = 0.5;
        
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.01);
        double[][] result = powellOptimizer.newPointAndDirection(p, d, alphaMin);
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals(2, result[0].length);
        assertEquals(0.5, result[0][0], EPSILON);
        assertEquals(0.0, result[0][1], EPSILON);
        assertEquals(1.0, result[1][0], EPSILON);
        assertEquals(0.0, result[1][1], EPSILON);
    }
    
    // test search method
    @Test
    public void testSearch() {
        double[] p = {0.0, 0.0};
        double[] d = {1.0, 0.0};
        
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.01);
        UnivariatePointValuePair result = powellOptimizer.search(p, d);
        assertNotNull(result);
        assertEquals(0.0, result.getPoint(), EPSILON);
        assertEquals(0.0, result.getValue(), EPSILON);
    }
}