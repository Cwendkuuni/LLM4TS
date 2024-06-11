import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InlineCostEstimatorTest {

    private InlineCostEstimator estimator;
    private Node rootNode;

    @Before
    public void setUp() {
        estimator = new InlineCostEstimator();
        rootNode = new Node();
    }

    @Test
    public void testGetCost() {
        int cost = InlineCostEstimator.getCost(rootNode);
        assertEquals(0, cost);
    }

    @Test
    public void testGetCostWithThreshold() {
        int cost = InlineCostEstimator.getCost(rootNode, 100);
        assertEquals(0, cost);
    }

    @Test
    public void testCompiledSizeEstimatorAdd() {
        CompiledSizeEstimator estimator = new CompiledSizeEstimator(100);
        estimator.add(rootNode);
        int cost = estimator.getCost();
        assertEquals(0, cost);
    }

    @Test
    public void testCompiledSizeEstimatorGetCost() {
        CompiledSizeEstimator estimator = new CompiledSizeEstimator(100);
        int cost = estimator.getCost();
        assertEquals(0, cost);
    }

    @Test
    public void testCompiledSizeEstimatorContinueProcessing() {
        CompiledSizeEstimator estimator = new CompiledSizeEstimator(100);
        boolean continueProcessing = estimator.continueProcessing();
        assertTrue(continueProcessing);
    }

    @Test
    public void testCompiledSizeEstimatorGetLastChar() {
        CompiledSizeEstimator estimator = new CompiledSizeEstimator(100);
        char lastChar = estimator.getLastChar();
        assertEquals('\0', lastChar);
    }

    @Test
    public void testCompiledSizeEstimatorAppend() {
        CompiledSizeEstimator estimator = new CompiledSizeEstimator(100);
        estimator.append("test");
        int cost = estimator.getCost();
        assertEquals(4, cost);
        char lastChar = estimator.getLastChar();
        assertEquals('t', lastChar);
    }

    @Test
    public void testCompiledSizeEstimatorAddIdentifier() {
        CompiledSizeEstimator estimator = new CompiledSizeEstimator(100);
        estimator.addIdentifier("test");
        int cost = estimator.getCost();
        assertEquals(2, cost);
    }

}