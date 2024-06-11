import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ContinuedFractionTest {

    @Test
    public void testGetA() {
        // Test if getA returns the correct coefficient
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n * x;
            }

            @Override
            protected double getB(int n, double x) {
                return n;
            }
        };

        assertEquals(2.0, cf.getA(2, 1.0), 0.0001);
    }

    // Other test cases for each method can be added here

}