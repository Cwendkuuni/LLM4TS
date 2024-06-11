import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AxisTest {
    
    @Test
    public void testLookupWithValidAxisNum() {
        int axisNum = 1;
        String expected = "child";
        String actual = Axis.lookup(axisNum);
        assertEquals(expected, actual);
        
        axisNum = 7;
        expected = "following";
        actual = Axis.lookup(axisNum);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLookupWithInvalidAxisNum() {
        int axisNum = 0;
        String expected = null;
        String actual = Axis.lookup(axisNum);
        assertEquals(expected, actual);
        
        axisNum = 14;
        expected = null;
        actual = Axis.lookup(axisNum);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLookupWithValidAxisName() {
        String axisName = "descendant";
        int expected = 2;
        int actual = Axis.lookup(axisName);
        assertEquals(expected, actual);
        
        axisName = "preceding";
        expected = 8;
        actual = Axis.lookup(axisName);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLookupWithInvalidAxisName() {
        String axisName = "invalid";
        int expected = 0;
        int actual = Axis.lookup(axisName);
        assertEquals(expected, actual);
        
        axisName = "nonexistent";
        expected = 0;
        actual = Axis.lookup(axisName);
        assertEquals(expected, actual);
    }
}