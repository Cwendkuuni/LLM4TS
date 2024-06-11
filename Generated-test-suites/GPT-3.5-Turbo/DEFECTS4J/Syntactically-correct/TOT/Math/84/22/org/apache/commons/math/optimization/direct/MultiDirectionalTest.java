import org.junit.Assert;
import org.junit.Test;

public class MultiDirectionalTest {

    // Testing the default constructor
    @Test
    public void testDefaultConstructor() {
        MultiDirectional multiDirectional = new MultiDirectional();
        Assert.assertEquals(2.0, multiDirectional.getKhi(), 0.0);
        Assert.assertEquals(0.5, multiDirectional.getGamma(), 0.0);
    }

    // Testing the parameterized constructor
    @Test
    public void testParameterizedConstructor() {
        MultiDirectional multiDirectional = new MultiDirectional(3.0, 0.8);
        Assert.assertEquals(3.0, multiDirectional.getKhi(), 0.0);
        Assert.assertEquals(0.8, multiDirectional.getGamma(), 0.0);
    }

    // Testing the iterateSimplex method when comparator returns negative
    @Test
    public void testIterateSimplexComparatorReturnsNegative() {
        // Initialize MultiDirectional object with different coefficients to verify their update
        MultiDirectional multiDirectional = new MultiDirectional(2.0, 0.5);

        // For simplicity, assume that the original and reflected are equal and both are the best vertex
        RealPointValuePair[] simplex = new RealPointValuePair[2];
        simplex[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 10.0);
        simplex[1] = new RealPointValuePair(new double[]{1.0, 2.0}, 10.0);
        multiDirectional.setSimplex(simplex);

        try {
            multiDirectional.iterateSimplex(new Comparator<RealPointValuePair>() {
                public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                    return -1;
                }
            });
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // Verify that the simplex did not change
        Assert.assertEquals(simplex, multiDirectional.getSimplex());
    }

    // Testing the iterateSimplex method when comparator returns positive
    @Test
    public void testIterateSimplexComparatorReturnsPositive() {
        // Initialize MultiDirectional object with different coefficients to verify their update
        MultiDirectional multiDirectional = new MultiDirectional(2.0, 0.5);

        // For simplicity, assume that the original and reflected are equal and both are the best vertex
        RealPointValuePair[] simplex = new RealPointValuePair[2];
        simplex[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 10.0);
        simplex[1] = new RealPointValuePair(new double[]{1.0, 2.0}, 10.0);
        multiDirectional.setSimplex(simplex);

        try {
            multiDirectional.iterateSimplex(new Comparator<RealPointValuePair>() {
                public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                    return 1;
                }
            });
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // Verify that the simplex did not change
        Assert.assertEquals(simplex, multiDirectional.getSimplex());
    }

    // Testing the evaluateNewSimplex method
    @Test
    public void testEvaluateNewSimplex() {
        MultiDirectional multiDirectional = new MultiDirectional();

        // For simplicity, assume a 2-dimensional simplex with original as the best vertex
        RealPointValuePair[] original = new RealPointValuePair[2];
        original[0] = new RealPointValuePair(new double[]{2.0, 4.0}, 20.0);
        original[1] = new RealPointValuePair(new double[]{3.0, 5.0}, 30.0);
        multiDirectional.setSimplex(original);

        try {
            RealPointValuePair newSimplex = multiDirectional.evaluateNewSimplex(original, 0.5,
                    new Comparator<RealPointValuePair>() {
                        public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                            if (o1.getValue() < o2.getValue()) {
                                return -1;
                            } else if (o1.getValue() > o2.getValue()) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }
                    });
            Assert.assertNotNull(newSimplex);
            Assert.assertEquals(2.0, newSimplex.getValue(), 0.0);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

}