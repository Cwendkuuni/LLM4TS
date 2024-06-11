import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistance_int_int_float_float() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistance_float_float() {
        Distance distance = Distance.buildDistance(3.5f, 1.0f);
        
        assertEquals(2, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(3.5f, distance.getMean(), 0.001);
        assertEquals(1.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testBuildDistance_int_int() {
        Distance distance = Distance.buildDistance(0, 10);
        
        assertEquals(0, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(1.666f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance inputDistance = Distance.buildDistance(5, 20, 12.0f, 4.0f);
        
        Distance outputDistance = Distance.transformIntoCeleraAssemblerDistance(inputDistance);
        
        assertEquals(inputDistance.getMin(), outputDistance.getMin());
        assertEquals(inputDistance.getMax(), outputDistance.getMax());
        assertEquals(12.0f, outputDistance.getMean(), 0.001);
        assertEquals(3.995f, outputDistance.getStdDev(), 0.001);
    }
}