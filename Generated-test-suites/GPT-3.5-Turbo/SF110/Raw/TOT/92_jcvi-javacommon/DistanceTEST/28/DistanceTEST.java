import org.junit.*;
import static org.junit.Assert.*;

public class DistanceTest {
    
    private Distance distance;
    
    @Before
    public void setUp() {
        distance = new Distance(0,100,50f,16.667f);
    }
    
    @After
    public void tearDown() {
        distance = null;
    }
    
    // Test buildDistance(int min, int max, float mean, float stdDev)
    // Typical case
    @Test
    public void testBuildDistanceWithValidInputs() {
        Distance newDistance = Distance.buildDistance(0, 100, 50f, 16.667f);
        assertEquals(0, newDistance.getMin());
        assertEquals(100, newDistance.getMax());
        assertEquals(50f, newDistance.getMean(), 0.001);
        assertEquals(16.667f, newDistance.getStdDev(), 0.001);
    }
    
    // Edge case: min and max are equal
    @Test
    public void testBuildDistanceWithEqualMinAndMax() {
        Distance newDistance = Distance.buildDistance(50, 50, 50f, 16.667f);
        assertEquals(50, newDistance.getMin());
        assertEquals(50, newDistance.getMax());
        assertEquals(50f, newDistance.getMean(), 0.001);
        assertEquals(16.667f, newDistance.getStdDev(), 0.001);
    }
    
    // Error case: stdDev is negative
    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNegativeStdDev() {
        Distance newDistance = Distance.buildDistance(0, 100, 50f, -10f);
    }
    
    // Test buildDistance(float mean, float stdDev)
    // Typical case
    @Test
    public void testBuildDistanceWithValidMeanAndStdDev() {
        Distance newDistance = Distance.buildDistance(50f, 16.667f);
        assertEquals(25, newDistance.getMin());
        assertEquals(75, newDistance.getMax());
        assertEquals(50f, newDistance.getMean(), 0.001);
        assertEquals(16.667f, newDistance.getStdDev(), 0.001);
    }
    
    // Test buildDistance(float mean, float stdDev)
    // Edge case: stdDev is zero
    @Test
    public void testBuildDistanceWithZeroStdDev() {
        Distance newDistance = Distance.buildDistance(50f, 0f);
        assertEquals(50, newDistance.getMin());
        assertEquals(50, newDistance.getMax());
        assertEquals(50f, newDistance.getMean(), 0.001);
        assertEquals(0f, newDistance.getStdDev(), 0.001);
    }
    
    // Test buildDistance(float mean, float stdDev)
    // Error case: mean is negative
    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithNegativeMean() {
        Distance newDistance = Distance.buildDistance(-50f, 16.667f);
    }
    
    // Test buildDistance(int min, int max)
    // Typical case
    @Test
    public void testBuildDistanceWithValidMinMax() {
        Distance newDistance = Distance.buildDistance(0, 100);
        assertEquals(0, newDistance.getMin());
        assertEquals(100, newDistance.getMax());
        assertEquals(50f, newDistance.getMean(), 0.001);
        assertEquals(16.667f, newDistance.getStdDev(), 0.001);
    }
    
    // Test buildDistance(int min, int max)
    // Edge case: min and max are equal
    @Test
    public void testBuildDistanceWithEqualMinMax() {
        Distance newDistance = Distance.buildDistance(50, 50);
        assertEquals(50, newDistance.getMin());
        assertEquals(50, newDistance.getMax());
        assertEquals(50f, newDistance.getMean(), 0.001);
        assertEquals(0f, newDistance.getStdDev(), 0.001);
    }
    
    // Test buildDistance(int min, int max)
    // Error case: min is greater than max
    @Test(expected = IllegalArgumentException.class)
    public void testBuildDistanceWithInvalidMinMax() {
        Distance newDistance = Distance.buildDistance(100, 0);
    }
    
    // Test transformIntoCeleraAssemblerDistance(Distance distance)
    // Typical case: mean is less than delta
    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithMeanLessThanDelta() {
        Distance newDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(0, newDistance.getMin());
        assertEquals(100, newDistance.getMax());
        assertEquals(50f, newDistance.getMean(), 0.001);
        assertEquals(-0.005f, newDistance.getStdDev(), 0.001);
    }
    
    // Test transformIntoCeleraAssemblerDistance(Distance distance)
    // Typical case: mean is equal to delta
    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithMeanEqualToDelta() {
        distance = new Distance(3,97,50f,16.667f);
        Distance newDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(3, newDistance.getMin());
        assertEquals(97, newDistance.getMax());
        assertEquals(50f, newDistance.getMean(), 0.001);
        assertEquals(16.667f, newDistance.getStdDev(), 0.001);
    }
    
    // Test transformIntoCeleraAssemblerDistance(Distance distance)
    // Typical case: mean is greater than delta
    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithMeanGreaterThanDelta() {
        distance = new Distance(5,95,50f,16.667f);
        Distance newDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(5, newDistance.getMin());
        assertEquals(95, newDistance.getMax());
        assertEquals(50f, newDistance.getMean(), 0.001);
        assertEquals(16.667f, newDistance.getStdDev(), 0.001);
    }
    
    // Test transformIntoCeleraAssemblerDistance(Distance distance)
    // Edge case: mean is equal to delta + 1.015
    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithMeanEqualToDeltaPlus1() {
        distance = new Distance(4,96,50f,16.667f);
        Distance newDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(4, newDistance.getMin());
        assertEquals(96, newDistance.getMax());
        assertEquals(50f, newDistance.getMean(), 0.001);
        assertEquals(16.667f, newDistance.getStdDev(), 0.001);
    }
    
    // Test transformIntoCeleraAssemblerDistance(Distance distance)
    // Error case: distance is null
    @Test(expected = NullPointerException.class)
    public void testTransformIntoCeleraAssemblerDistanceWithNullDistance() {
        Distance newDistance = Distance.transformIntoCeleraAssemblerDistance(null);
    }
    
    // Test hashCode()
    @Test
    public void testHashCode() {
        int expectedHashCode = 955620766;  // Calculate the expected hash code using the formula provided in the Java class
        assertEquals(expectedHashCode, distance.hashCode());
    }
    
    // Test equals(Object obj)
    // Typical case: distance object is equal to itself
    @Test
    public void testEqualsWithSameObject() {
        assertTrue(distance.equals(distance));
    }
    
    // Test equals(Object obj)
    // Typical case: distance object is equal to another distance object with the same values
    @Test
    public void testEqualsWithEqualObjects() {
        Distance newDistance = new Distance(0,100,50f,16.667f);
        assertTrue(distance.equals(newDistance));
    }
    
    // Test equals(Object obj)
    // Typical case: distance object is not equal to another distance object with different values
    @Test
    public void testEqualsWithDifferentObjects() {
        Distance newDistance = new Distance(25,75,50f,16.667f);
        assertFalse(distance.equals(newDistance));
    }
    
    // Test equals(Object obj)
    // Edge case: distance object is not equal to null
    @Test
    public void testEqualsWithNullObject() {
        assertFalse(distance.equals(null));
    }
    
    // Test equals(Object obj)
    // Edge case: distance object is not equal to an object of a different class
    @Test
    public void testEqualsWithDifferentClassObject() {
        assertFalse(distance.equals("distance"));
    }
    
    // Test toString()
    @Test
    public void testToString() {
        String expectedString = "50.000, std: 16.667";
        assertEquals(expectedString, distance.toString());
    }
}