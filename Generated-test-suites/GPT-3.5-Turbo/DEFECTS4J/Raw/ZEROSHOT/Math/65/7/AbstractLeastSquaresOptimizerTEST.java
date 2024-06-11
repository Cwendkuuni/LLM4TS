import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testIncrementIterationsCounter() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };
        
        int maxIterations = optimizer.getMaxIterations();
        int iterations = optimizer.getIterations();
        
        try {
            optimizer.incrementIterationsCounter();
            int newIterations = optimizer.getIterations();
            assertEquals(iterations + 1, newIterations);
        } catch (OptimizationException e) {
            fail("OptimizationException thrown");
        }
        
        optimizer.setMaxIterations(0);
        try {
            optimizer.incrementIterationsCounter();
            fail("OptimizationException not thrown");
        } catch (OptimizationException e) {
            assertEquals("Maximal number of iterations (0) exceeded", e.getMessage());
        }
        
        optimizer.setMaxIterations(maxIterations);
    }
    
    @Test
    public void testUpdateResidualsAndCost() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };
        
        optimizer.rows = 3;
        optimizer.targetValues = new double[]{1, 2, 3};
        optimizer.objective = new double[]{4, 5, 6};
        optimizer.residualsWeights = new double[]{1, 1, 1};
        optimizer.residuals = new double[3];
        
        double expectedCost = Math.sqrt((1-4)*(1-4) + (2-5)*(2-5) + (3-6)*(3-6));
        
        try {
            optimizer.updateResidualsAndCost();
            
            assertArrayEquals(new double[]{1-4, 2-5, 3-6}, optimizer.residuals, 0);
            assertEquals(expectedCost, optimizer.cost, 0);
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException thrown");
        }
        
        optimizer.targetValues = new double[]{};
        try {
            optimizer.updateResidualsAndCost();
            fail("FunctionEvaluationException not thrown");
        } catch (FunctionEvaluationException e) {
            assertEquals("Dimension mismatch: 0 != 3", e.getMessage());
        }
        
        optimizer.targetValues = new double[]{1, 2, 3};
        optimizer.maxEvaluations = 0;
        try {
            optimizer.updateResidualsAndCost();
            fail("FunctionEvaluationException not thrown");
        } catch (FunctionEvaluationException e) {
            assertEquals("Maximal number of evaluations (0) exceeded", e.getMessage());
        }
        
        optimizer.maxEvaluations = Integer.MAX_VALUE;
    }
    
    @Test
    public void testGetRMS() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };
        
        optimizer.rows = 3;
        optimizer.residualsWeights = new double[]{1, 1, 1};
        optimizer.residuals = new double[]{1, 2, 3};
        
        double expectedRms = Math.sqrt((1*1 + 2*2 + 3*3) / 3);
        
        assertEquals(expectedRms, optimizer.getRMS(), 0);
    }
    
    @Test
    public void testGetChiSquare() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };
        
        optimizer.rows = 3;
        optimizer.residualsWeights = new double[]{1, 1, 1};
        optimizer.residuals = new double[]{1, 2, 3};
        
        double expectedChiSquare = 1*1 + 2*2 + 3*3;
        
        assertEquals(expectedChiSquare, optimizer.getChiSquare(), 0);
    }
    
}