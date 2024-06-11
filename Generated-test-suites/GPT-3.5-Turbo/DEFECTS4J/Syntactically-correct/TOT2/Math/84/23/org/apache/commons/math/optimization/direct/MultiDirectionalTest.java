import static org.junit.Assert.*;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testDefaultConstructor() {
        MultiDirectional multiDirectional = new MultiDirectional();
        assertNotNull(multiDirectional);
    }

    @Test
    public void testCustomConstructor() {
        MultiDirectional multiDirectional = new MultiDirectional(3.0, 0.7);
        assertNotNull(multiDirectional);
    }

    @Test
    public void testIterateSimplex() {
        MultiDirectional multiDirectional = new MultiDirectional();
        // Initial simplex vertices
        double[] point1 = {1.0, 2.0};
        double[] point2 = {3.0, 4.0};
        double[] point3 = {5.0, 6.0};
        double[] point4 = {7.0, 8.0};
        RealPointValuePair vertex1 = new RealPointValuePair(point1, 10.0);
        RealPointValuePair vertex2 = new RealPointValuePair(point2, 20.0);
        RealPointValuePair vertex3 = new RealPointValuePair(point3, 30.0);
        RealPointValuePair vertex4 = new RealPointValuePair(point4, 40.0);
        RealPointValuePair[] original = {vertex1, vertex2, vertex3, vertex4};
        multiDirectional.setSimplex(original);

        try {
            multiDirectional.iterateSimplex(new Comparator<RealPointValuePair>() {
                public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                    return Double.compare(o1.getValue(), o2.getValue());
                }
            });
        } catch (Exception e) {
            fail("Unexpected exception in testIterateSimplex");
        }
    }

    // Additional test cases for other methods can be added here

}