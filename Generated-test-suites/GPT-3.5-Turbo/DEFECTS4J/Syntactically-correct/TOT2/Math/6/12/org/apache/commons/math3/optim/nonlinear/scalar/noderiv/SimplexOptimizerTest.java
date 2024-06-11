import static org.junit.Assert.*;
import org.junit.Test;

public class SimplexOptimizerTest {
    
    @Test
    public void testConstructor() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNotNull(optimizer);
    }
    
    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new Simplex();
        optimizer.setSimplex(simplex);
        assertNotNull(optimizer.getSimplex());
    }
    
    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        // Define the optimization function
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1];
            }
        };
        // Set optimization data
        InitialGuess initialGuess = new InitialGuess(new double[] { 1.0, 2.0 });
        AbstractSimplex simplex = new Simplex();
        optimizer.optimize(100, function, GoalType.MINIMIZE, initialGuess, simplex);
        assertNotNull(optimizer.getResult());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimizeInternal();
    }
}