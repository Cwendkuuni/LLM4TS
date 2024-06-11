import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithIntParameters() {
        Distance distance = Distance.buildDistance(10, 20);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        
        float mean = (float) ((10 / 2.0) + (20 / 2.0));
        float stdDev = (float) ((20 - 10) / 6.0);
        assertEquals(mean, distance.getMean(), 0.001);
        assertEquals(stdDev, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithFloatParameters() {
        Distance distance = Distance.buildDistance(15.0f, 2.0f);
        float delta = 3.0f * 2.0f;
        int plusDelta = (int)(15.0f + delta);
        int minusDelta = (int)(15.0f - delta);
        assertEquals(minusDelta, distance.getMin());
        assertEquals(plusDelta, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithIntFloatParameters() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(10, 20, 15.0f, 2.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        assertTrue(distance.equals(transformedDistance));
        
        Distance distance2 = new Distance(5, 15, 7.0f, 1.0f);
        Distance transformedDistance2 = Distance.transformIntoCeleraAssemblerDistance(distance2);
        
        float correctedStdDev = (7.0f - 1.015f) / 3.0f;
        assertEquals(correctedStdDev, transformedDistance2.getStdDev(), 0.001);
        assertNotEquals(distance2.getStdDev(), transformedDistance2.getStdDev(), 0.001);
        assertEquals(distance2.getMin(), transformedDistance2.getMin());
        assertEquals(distance2.getMax(), transformedDistance2.getMax());
        assertEquals(distance2.getMean(), transformedDistance2.getMean(), 0.001);
    }
    
    @Test
    public void testHashCodeAndEquals() {
        Distance distance = new Distance(10, 20, 15.0f, 2.0f);
        Distance distanceSame = new Distance(10, 20, 15.0f, 2.0f);
        Distance distanceDifferent = new Distance(5, 15, 7.0f, 1.0f);
        
        assertEquals(distance.hashCode(), distanceSame.hashCode());
        assertNotEquals(distance.hashCode(), distanceDifferent.hashCode());
        
        assertTrue(distance.equals(distance));
        assertFalse(distance.equals(null));
        assertFalse(distance.equals("random string"));
        assertTrue(distance.equals(distanceSame));
        assertFalse(distance.equals(distanceDifferent));
    }
    
    @Test
    public void testToString() {
        Distance distance = new Distance(10, 20, 15.0f, 2.0f);
        assertEquals("15.000, std: 2.000", distance.toString());
    }
}