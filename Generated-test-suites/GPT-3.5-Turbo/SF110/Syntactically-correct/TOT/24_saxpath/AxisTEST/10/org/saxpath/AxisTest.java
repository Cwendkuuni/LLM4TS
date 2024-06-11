import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {

    @Before
    public void setup() {
        // Setup any necessary test data
    }

    @Test
    public void testLookup_ValidAxisNum_ReturnsCorrespondingAxisName() {
        int axisNum = Axis.CHILD;
        String expectedName = "child";
        
        String result = Axis.lookup(axisNum);
        
        assertEquals(expectedName, result);
    }

    @Test
    public void testLookup_InvalidAxisNum_ReturnsNull() {
        int axisNum = 100;
        
        String result = Axis.lookup(axisNum);
        
        assertNull(result);
    }

    @Test
    public void testLookup_ValidAxisName_ReturnsCorrespondingAxisNumber() {
        String axisName = "descendant";
        int expectedNumber = Axis.DESCENDANT;
        
        int result = Axis.lookup(axisName);
        
        assertEquals(expectedNumber, result);
    }

    @Test
    public void testLookup_InvalidAxisName_ReturnsZero() {
        String axisName = "invalid";
        int expectedNumber = Axis.INVALID_AXIS;
        
        int result = Axis.lookup(axisName);
        
        assertEquals(expectedNumber, result);
    }

    @Test
    public void testLookup_NullAxisName_ReturnsZero() {
        String axisName = null;
        int expectedNumber = Axis.INVALID_AXIS;
        
        int result = Axis.lookup(axisName);
        
        assertEquals(expectedNumber, result);
    }

    // Add more test cases for other methods if necessary

}