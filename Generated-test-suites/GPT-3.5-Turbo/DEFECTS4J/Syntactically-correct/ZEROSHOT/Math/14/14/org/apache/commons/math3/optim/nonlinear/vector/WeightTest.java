import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WeightTest {

    private Weight weight;

    @Before
    public void setup() {
        double[] diagonal = {1.0, 2.0, 3.0, 4.0};
        weight = new Weight(diagonal);
    }

    @Test
    public void testGetWeight() {
        double[][] expectedData = {{1.0, 0.0, 0.0, 0.0},
                                   {0.0, 2.0, 0.0, 0.0},
                                   {0.0, 0.0, 3.0, 0.0},
                                   {0.0, 0.0, 0.0, 4.0}};
        RealMatrix expected = new Array2DRowRealMatrix(expectedData);

        RealMatrix actual = weight.getWeight();

        assertTrue(actual.isEqualTo(expected));
    }
}