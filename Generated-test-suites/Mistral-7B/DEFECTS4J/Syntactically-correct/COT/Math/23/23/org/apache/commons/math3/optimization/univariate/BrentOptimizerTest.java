import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BrentOptimizerTest {

    private UnivariateFunction mockFunction;
    private BrentOptimizer brentOptimizer;

    @Before
    public void setUp() {
        mockFunction = Mockito.mock(UnivariateFunction.class);
        brentOptimizer = new BrentOptimizer(1e-3, 1e-3, null);
    }

    

    // Test constructor with relative and absolute thresholds and ConvergenceChecker
    @Test
    public void testBrentOptimizerConstructorWithThresholdsAndChecker() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(1e-3, 1e-3, null);
        assertNotNull(brentOptimizer);
    }

    // Test constructor with relative and absolute thresholds
    @Test
    public void testBrentOptimizerConstructorWithThresholds() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(1e-3, 1e-3);
        assertNotNull(brentOptimizer);
    }

    // Test doOptimize method with a simple function
    @Test
    public void testDoOptimizeWithSimpleFunction() {
        Mockito.when(mockFunction.value(0.0)).thenReturn(5.0);
        brentOptimizer.setFunction(mockFunction);
        brentOptimizer.optimize();
        UnivariatePointValuePair result = brentOptimizer.getOptimum();
        assertEquals(0.0, result.getPoint(), 0.01);
        assertEquals(5.0, result.getValue(), 0.01);
    }

    // Test doOptimize method with a function that has a minimum at x = -2
    @Test
    public void testDoOptimizeWithFunctionMinAtXNegative2() {
        Mockito.when(mockFunction.value(-2.0)).thenReturn(-10.0);
        brentOptimizer.setFunction(mockFunction);
        brentOptimizer.optimize();
        UnivariatePointValuePair result = brentOptimizer.getOptimum();
        assertEquals(-2.0, result.getPoint(), 0.01);
        assertEquals(-10.0, result.getValue(), 0.01);
    }

    // Test doOptimize method with a function that has a maximum at x = 2
    @Test
    public void testDoOptimizeWithFunctionMaxAtX2() {
        Mockito.when(mockFunction.value(2.0)).thenReturn(10.0);
        brentOptimizer.setFunction(mockFunction);
        brentOptimizer.optimize();
        UnivariatePointValuePair result = brentOptimizer.getOptimum();
        assertEquals(2.0, result.getPoint(), 0.01);
        assertEquals(10.0, result.getValue(), 0.01);
    }

    // Test doOptimize method with a function that has a minimum and maximum at x = 0
    @Test
    public void testDoOptimizeWithFunctionMinMaxAtX0() {
        Mockito.when(mockFunction.value(-1.0)).thenReturn(-5.0);
        Mockito.when(mockFunction.value(1.0)).thenReturn(5.0);
        Mockito.when(mockFunction.value(0.0)).thenReturn(0.0);
        brentOptimizer.setFunction(mockFunction);
        brentOptimizer.optimize();
        UnivariatePointValuePair result = brentOptimizer.getOptimum();
        assertEquals(0.0, result.getPoint(), 0.01);
        assertEquals(0.0, result.getValue(), 0.01);
    }

    // Test doOptimize method with a function that has a minimum at x = -5 and a maximum at x = 5
    @Test
    public void testDoOptimizeWithFunctionMinMaxAtXNegative5AndPositive5() {
        Mockito.when(mockFunction.value(-5.0)).thenReturn(-25.0);
        Mockito.when(mockFunction.value(5.0)).thenReturn(25.0);
        brentOptimizer.setFunction(mockFunction);
        brentOptimizer.optimize();
        UnivariatePointValuePair result = brentOptimizer.getOptimum();
        assertEquals(-5.0, result.getPoint(), 0.01);
        assertEquals(-25.0, result.getValue(), 0.01);
    }

    // Test doOptimize method with a function that has a minimum at x = -5 and a maximum at x = 5 with a tolerance less than threshold
    @Test
    public void testDoOptimizeWithFunctionMinMaxAtXNegative5AndPositive5WithToleranceLessThanThreshold() {
        Mockito.when(mockFunction.value(-5.0)).thenReturn(-24.99);
        Mockito.when(mockFunction.value(5.0)).thenReturn(25.01);
        brentOptimizer.setFunction(mockFunction);
        brentOptimizer.optimize();
        UnivariatePointValuePair result = brentOptimizer.getOptimum();
        assertEquals(-5.0, result.getPoint(), 0.01);
        assertEquals(-24.99, result.getValue(), 0.01);
    }

    // Test doOptimize method with a function that has a minimum at x = -5 and a maximum at x = 5 with a tolerance greater than threshold
    @Test(expected = org.apache.commons.math3.exception.GoalTypeMismatchedException.class)
    public void testDoOptimizeWithFunctionMinMaxAtXNegative5AndPositive5WithToleranceGreaterThanThreshold() {
        Mockito.when(mockFunction.value(-5.0)).thenReturn(-25.0);
        Mockito.when(mockFunction.value(5.0)).thenReturn(25.0);
        brentOptimizer.setFunction(mockFunction);
        brentOptimizer.optimize();
        brentOptimizer.setGoalType(GoalType.MAXIMIZE);
        brentOptimizer.optimize();
    }

    
}