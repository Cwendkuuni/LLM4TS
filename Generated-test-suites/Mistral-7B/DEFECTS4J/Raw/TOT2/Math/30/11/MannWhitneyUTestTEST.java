import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest mannWhitneyUTest;
    private double[] x;
    private double[] y;
    private double[] xWithDuplicates;
    private double[] yWithDuplicates;
    private double[] xWithNaN;
    private double[] yWithNaN;

    @Before
    public void setUp() {
        mannWhitneyUTest = new MannWhitneyUTest();
    }

    @Test
    public void testMannWhitneyUWithEqualSamples() {
        x = new double[]{1.0, 1.1, 1.2, 1.3};
        y = new double[]{1.0, 1.1, 1.2, 1.3};

        double result = mannWhitneyUTest.mannWhitneyU(x, y);
        Assert.assertEquals(6.0, result, 0.0);
    }

    @Test
    public void testMannWhitneyUWithDifferentSamples() {
        x = new double[]{1.0, 2.0, 3.0, 4.0};
        y = new double[]{5.0, 6.0, 7.0, 8.0};

        double result = mannWhitneyUTest.mannWhitneyU(x, y);
        Assert.assertEquals(10.0, result, 0.0);
    }

    @Test
    public void testMannWhitneyUTestWithEqualSamples() {
        x = new double[]{1.0, 1.1, 1.2, 1.3};
        y = new double[]{1.0, 1.1, 1.2, 1.3};

        double pValue = mannWhitneyUTest.mannnWhitneyUTest(x, y);
        Assert.assertEquals(1.0, pValue, 0.001);
    }

    @Test
    public void testMannWhitneyUTestWithDifferentSamples() {
        x = new double[]{1.0, 2.0, 3.0, 4.0};
        y = new double[]{5.0, 6.0, 7.0, 8.0};

        double pValue = mannWhitneyUTest.mannnWhitneyUTest(x, y);
        Assert.assertEquals(0.0, pValue, 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUWithNullArgument() {
        mannWhitneyUTest.mannWhitneyU(null, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUWithNoData() {
        mannWhitneyUTest.mannWhitneyU(new double[0], y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestWithNullArgumentForX() {
        mannWhitneyUTest.mannnWhitneyUTest(null, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestWithNoDataForX() {
        mannWhitneyUTest.mannnWhitneyUTest(new double[0], y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestWithNoDataForY() {
        mannWhitneyUTest.mannnWhitneyUTest(x, new double[0]);
    }

    @Test
    public void testMannWhitneyUWithTies() {
        x = new double[]{1.0, 1.1, 1.2, 1.3, 1.3};
        y = new double[]{1.5, 1.5, 1.5, 1.5, 1.5};

        double result = mannWhitneyUTest.mannWhitneyU(x, y);
        Assert.assertEquals(11.0, result, 0.0);
    }

    @Test
    public void testMannWhitneyUTestWithTies() {
        x = new double[]{1.0, 1.1, 1.2, 1.3, 1.3};
        y = new double[]{1.5, 1.5, 1.5, 1.5, 1.5};

        double pValue = mannWhitneyUTest.mannnWhitneyUTest(x, y);
        Assert.assertEquals(1.0, pValue, 0.001);
    }

    @Test
    public void testMannWhitneyUWithDuplicatesInX() {
        Random random = new Random();
        int size = 10;
        xWithDuplicates = new double[size];
        y = new double[size];

        for (int i = 0; i < size; i++) {
            xWithDuplicates[i] = random.nextDouble();
            y[i] = random.nextDouble();
        }
        int index = random.nextInt(size);
        xWithDuplicates[index] = xWithDuplicates[index];

        try {
            mannWhitneyUTest.mannWhitneyU(xWithDuplicates, y);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testMannWhitneyUTestWithDuplicatesInX() {
        Random random = new Random();
        int size = 10;
        xWithDuplicates = new double[size];
        y = new double[size];

        for (int i = 0; i < size; i++) {
            xWithDuplicates[i] = random.nextDouble();
            y[i] = random.nextDouble();
        }
        int index = random.nextInt(size);
        xWithDuplicates[index] = xWithDuplicates[index];

        try {
            mannWhitneyUTest.mannnWhitneyUTest(xWithDuplicates, y);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testMannWhitneyUWithNaNInX() {
        xWithNaN = new double[]{1.0, 1.1, 1.2, 1.3, Double.NaN};
        y = new double[]{2.0, 3.0, 4.0, 5.0};

        try {
            mannWhitneyUTest.mannWhitneyU(xWithNaN, y);
            Assert.fail();
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testMannWhitneyUTestWithNaNInX() {
        xWithNaN = new double[]{1.0, 1.1, 1.2, 1.3, Double.NaN};
        y = new double[]{2.0, 3.0, 4.0, 5.0};

        try {
            mannWhitneyUTest.mannnWhitneyUTest(xWithNaN, y);
            Assert.fail();
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testMannWhitneyUWithNaNInY() {
        x = new double[]{1.0, 2.0, 3.0, 4.0};
        yWithNaN = new double[]{1.0, 2.0, 3.0, Double.NaN};

        try {
            mannWhitneyUTest.mannWhitneyU(x, yWithNaN);
            Assert.fail();
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testMannWhitneyUTestWithNaNInY() {
        x = new double[]{1.0, 2.0, 3.0, 4.0};
        yWithNaN = new double[]{1.0, 2.0, 3.0, Double.NaN};

        try {
            mannWhitneyUTest.mannnWhitneyUTest(x, yWithNaN);
            Assert.fail();
        } catch (NullArgumentException e) {
            // Expected
        }
    }
}