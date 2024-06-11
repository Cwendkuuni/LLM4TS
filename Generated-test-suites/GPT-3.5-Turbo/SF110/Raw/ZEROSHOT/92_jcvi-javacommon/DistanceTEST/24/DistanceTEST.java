import org.junit.*;
import static org.junit.Assert.*;

public class DistanceTest {
    private Distance distance;
    
    @Before
    public void setUp() {
        distance = Distance.buildDistance(10, 20, 15.0f, 2.5f);
    }
    
    @Test
    public void testBuildDistanceWithMinAndMaxAndMeanAndStdDev() {
        Distance result = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        assertEquals(distance, result);
    }
    
    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        Distance result = Distance.buildDistance(15.0f, 2.5f);
        assertEquals(distance, result);
    }
    
    @Test
    public void testBuildDistanceWithMinAndMax() {
        Distance result = Distance.buildDistance(10, 20);
        assertEquals(distance, result);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistanceLessThanDelta() {
        distance = Distance.buildDistance(5, 25, 15.0f, 2.5f);
        Distance result = Distance.transformIntoCeleraAssemblerDistance(distance);
        Distance expected = new Distance(5, 25, 15.0f, 4.995f);
        assertEquals(expected, result);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistanceGreaterThanDelta() {
        Distance result = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(distance, result);
    }
    
    @Test
    public void testGetMean() {
        float result = distance.getMean();
        assertEquals(15.0f, result, 0.001);
    }
    
    @Test
    public void testGetStdDev() {
        float result = distance.getStdDev();
        assertEquals(2.5f, result, 0.001);
    }
    
    @Test
    public void testGetMin() {
        int result = distance.getMin();
        assertEquals(10, result);
    }
    
    @Test
    public void testGetMax() {
        int result = distance.getMax();
        assertEquals(20, result);
    }
    
    @Test
    public void testHashCode() {
        int result = distance.hashCode();
        assertEquals(-1500175296, result);
    }
    
    @Test
    public void testEquals() {
        boolean result = distance.equals(distance);
        assertTrue(result);
    }
    
    @Test
    public void testEqualsWithDifferentObject() {
        boolean result = distance.equals("distance");
        assertFalse(result);
    }
    
    @Test
    public void testEqualsWithDifferentMax() {
        Distance other = Distance.buildDistance(10, 30, 15.0f, 2.5f);
        boolean result = distance.equals(other);
        assertFalse(result);
    }
    
    @Test
    public void testEqualsWithDifferentMean() {
        Distance other = Distance.buildDistance(10, 20, 16.0f, 2.5f);
        boolean result = distance.equals(other);
        assertFalse(result);
    }
    
    @Test
    public void testEqualsWithDifferentMin() {
        Distance other = Distance.buildDistance(5, 20, 15.0f, 2.5f);
        boolean result = distance.equals(other);
        assertFalse(result);
    }
    
    @Test
    public void testEqualsWithDifferentStdDev() {
        Distance other = Distance.buildDistance(10, 20, 15.0f, 3.0f);
        boolean result = distance.equals(other);
        assertFalse(result);
    }
    
    @Test
    public void testToString() {
        String result = distance.toString();
        assertEquals("15.000, std: 2.500", result);
    }

}