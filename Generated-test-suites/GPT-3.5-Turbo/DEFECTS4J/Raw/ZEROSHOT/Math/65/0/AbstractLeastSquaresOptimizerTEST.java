import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.optimization.general.AbstractLeastSquaresOptimizer;

public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };
    }

    @Test
    public void testSetMaxIterations() {
        optimizer.setMaxIterations(100);
        Assert.assertEquals(100, optimizer.getMaxIterations());
    }

    @Test
    public void testGetIterations() {
        optimizer.incrementIterationsCounter();
        Assert.assertEquals(1, optimizer.getIterations());
        optimizer.incrementIterationsCounter();
        Assert.assertEquals(2, optimizer.getIterations());
    }

    @Test
    public void testSetMaxEvaluations() {
        optimizer.setMaxEvaluations(200);
        Assert.assertEquals(200, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        optimizer.updateResidualsAndCost();
        Assert.assertEquals(1, optimizer.getEvaluations());
        optimizer.updateResidualsAndCost();
        Assert.assertEquals(2, optimizer.getEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() throws Exception {
        optimizer.updateJacobian();
        Assert.assertEquals(1, optimizer.getJacobianEvaluations());
        optimizer.updateJacobian();
        Assert.assertEquals(2, optimizer.getJacobianEvaluations());
    }

    @Test
    public void testSetConvergenceChecker() {
        optimizer.setConvergenceChecker(new VectorialConvergenceChecker() {
            @Override
            public boolean converged(int iteration, VectorialPointValuePair previous, VectorialPointValuePair current) {
                return false;
            }
        });
        Assert.assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testIncrementIterationsCounter() {
        try {
            optimizer.incrementIterationsCounter();
            Assert.assertEquals(1, optimizer.getIterations());
            optimizer.incrementIterationsCounter();
            Assert.assertEquals(2, optimizer.getIterations());
        } catch (OptimizationException ex) {
            Assert.fail("OptimizationException should not be thrown");
        }
    }
}