import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    // 

    // Extracted Public Methods - Basic JUnit 4 Test Cases

    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        assertNotNull(optimizer);

        BrentOptimizer optimizer2 = new BrentOptimizer(0.1, 0.01, null);
        assertNotNull(optimizer2);
    }

    public void testBrentOptimizerWithoutChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        assertNotNull(optimizer);
    }

    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        UnivariatePointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
    }

    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);

        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.5);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 0.75);
        UnivariatePointValuePair result = optimizer.best(a, b, true);

        assertNotNull(result);
    }

    // Potential Edge Cases and Exception Handling Scenarios - JUnit 4 Test Cases

    public void testNegativeRelativeThreshold() {
        try {
            BrentOptimizer optimizer = new BrentOptimizer(-0.1, 0.01);
            fail("Expected NumberIsTooSmallException to be thrown");
        } catch (NumberIsTooSmallException e) {
            assertEquals("Argument -0.1 is smaller than or equal to 4.44089209850063E-15 but must be strictly positive.",
                    e.getMessage());
        }
    }

    public void testNegativeAbsoluteThreshold() {
        try {
            BrentOptimizer optimizer = new BrentOptimizer(0.1, -0.01);
            fail("Expected NotStrictlyPositiveException to be thrown");
        } catch (NotStrictlyPositiveException e) {
            assertEquals("Negative or zero absolute threshold (-0.01)", e.getMessage());
        }
    }

    public void testZeroAbsoluteThreshold() {
        try {
            BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.0);
            fail("Expected NotStrictlyPositiveException to be thrown");
        } catch (NotStrictlyPositiveException e) {
            assertEquals("Negative or zero absolute threshold (0.0)", e.getMessage());
        }
    }

    public void testNullCheckerInConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01, null);
        assertNotNull(optimizer);
    }

    public void testEqualLoHiInDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        optimizer.setMin(2.0);
        optimizer.setMax(2.0);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
        assertEquals(2.0, result.getPoint(), 0.0);
    }

    public void testStopConditionReachedInDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        optimizer.setStartValue(1.0);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
    }

    public void testPGreaterThanQTimesBMinusXInDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        optimizer.setMin(1.9);
        optimizer.setMax(2.1);
        optimizer.setStartValue(1.95);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
    }

    public void testPLessThanQTimesAMinusXInDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        optimizer.setMin(1.9);
        optimizer.setMax(2.1);
        optimizer.setStartValue(2.05);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
    }

    public void testDLessThanTol1InDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        optimizer.setMin(0.5);
        optimizer.setMax(2.0);
        optimizer.setStartValue(0.6);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
    }

    public void testDGreaterThanTol1InDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        optimizer.setMin(0.5);
        optimizer.setMax(2.0);
        optimizer.setStartValue(0.8);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
    }

    public void testANullInBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 0.75);

        UnivariatePointValuePair result = optimizer.best(null, b, true);

        assertNotNull(result);
        assertEquals(b, result);
    }

    // 
}