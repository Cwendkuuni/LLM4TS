import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BrentOptimizerTest {

    // Test input parameters
    private double rel;
    private double abs;
    private ConvergenceChecker<UnivariatePointValuePair> checker;

    // Test subject
    private BrentOptimizer brentOptimizer;

    @Before
    public void setUp() {
        rel = 0.1;
        abs = 0.01;
        checker = new SimpleValueChecker();
    }

    @Test
    public void testConstructor_validParameters() {
        // Arrange
        brentOptimizer = new BrentOptimizer(rel, abs, checker);

        // Act & Assert
        assertEquals(rel, brentOptimizer.getRelativeThreshold(), 0.0);
        assertEquals(abs, brentOptimizer.getAbsoluteThreshold(), 0.0);
        assertEquals(checker, brentOptimizer.getConvergenceChecker());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_invalidRelativeThreshold() {
        // Arrange
        double invalidRel = -0.1;

        // Act
        brentOptimizer = new BrentOptimizer(invalidRel, abs, checker);

        // Assert
        fail("Expected NumberIsTooSmallException to be thrown");
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_invalidAbsoluteThreshold() {
        // Arrange
        double invalidAbs = -0.01;

        // Act
        brentOptimizer = new BrentOptimizer(rel, invalidAbs, checker);

        // Assert
        fail("Expected NotStrictlyPositiveException to be thrown");
    }

    @Test
    public void testDoOptimize_minimumCase() {
        // Arrange
        brentOptimizer = new BrentOptimizer(rel, abs, checker);
        brentOptimizer.setMin(1.0);
        brentOptimizer.setMax(10.0);
        brentOptimizer.setStartValue(5.0);
        brentOptimizer.setGoalType(GoalType.MINIMIZE);

        // Act
        UnivariatePointValuePair result = brentOptimizer.doOptimize();

        // Assert
        assertEquals(1.0, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    @Test
    public void testDoOptimize_maximumCase() {
        // Arrange
        brentOptimizer = new BrentOptimizer(rel, abs, checker);
        brentOptimizer.setMin(1.0);
        brentOptimizer.setMax(10.0);
        brentOptimizer.setStartValue(5.0);
        brentOptimizer.setGoalType(GoalType.MAXIMIZE);

        // Act
        UnivariatePointValuePair result = brentOptimizer.doOptimize();

        // Assert
        assertEquals(10.0, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    // Add more test cases as needed

}