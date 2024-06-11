
import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {
    @Test
    public void testBuildDistance_withMinMeanStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withMeanStdDev() {
        Distance distance = Distance.buildDistance(5.0f, 2.0f);
        
        assertEquals(3, distance.getMin());
        assertEquals(7, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withMinMax() {
        Distance distance = Distance.buildDistance(1, 10);
        
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001);
        assertEquals(1.5f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_withDeltaLessThanMean() {
        Distance distance = Distance.buildDistance(5, 15, 10.0f, 4.0f);
        
        distance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        assertEquals(5, distance.getMin());
        assertEquals(15, distance.getMax());
        assertEquals(10.0f, distance.getMean(), 0.001);
        assertEquals(4.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_withDeltaGreaterThanMean() {
        Distance distance = Distance.buildDistance(3, 7, 5.0f, 2.0f);
        
        distance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        assertEquals(3, distance.getMin());
        assertEquals(7, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(1.995f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testEquals_sameObject_returnsTrue() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        
        assertTrue(distance.equals(distance));
    }
    
    @Test
    public void testEquals_nullObject_returnsFalse() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        
        assertFalse(distance.equals(null));
    }
    
    @Test
    public void testEquals_differentClass_returnsFalse() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Object obj = new Object();
        
        assertFalse(distance.equals(obj));
    }
    
    @Test
    public void testEquals_sameValues_returnsTrue() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        
        assertTrue(distance1.equals(distance2));
    }
    
    @Test
    public void testEquals_differentValues_returnsFalse() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(2, 10, 5.0f, 2.0f);
        
        assertFalse(distance1.equals(distance2));
    }
    
    @Test
    public void testToString_returnsFormattedString() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        
        assertEquals("5.000, std: 2.000", distance.toString());
    }
}
