import static org.junit.Assert.*;
import org.junit.Test;

public class DistanceTest {
    
    @Test
    public void testBuildDistance1() {
        Distance distance = Distance.buildDistance(1, 5, 3.0f, 1.5f);
        assertEquals(1, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(3.0f, distance.getMean(), 0.001);
        assertEquals(1.5f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testCalculateDelta() {
        float delta = Distance.calculateDelta(2.0f);
        assertEquals(6.0f, delta, 0.001);
    }

    // additional test cases would be added for other methods

}