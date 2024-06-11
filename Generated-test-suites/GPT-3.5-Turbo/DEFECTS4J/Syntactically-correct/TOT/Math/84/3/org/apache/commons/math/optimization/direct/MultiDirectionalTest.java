import org.junit.Test;
import org.junit.Assert;

public class MultiDirectionalTest {

    @Test
    public void testConstructorWithDefaultCoefficients() {
        MultiDirectional multiDirectional = new MultiDirectional();
        
        Assert.assertEquals(2.0, multiDirectional.getKhi(), 0.0);
        Assert.assertEquals(0.5, multiDirectional.getGamma(), 0.0);
    }
    
    @Test
    public void testConstructorWithCustomCoefficients() {
        MultiDirectional multiDirectional = new MultiDirectional(1.5, 0.3);
        
        Assert.assertEquals(1.5, multiDirectional.getKhi(), 0.0);
        Assert.assertEquals(0.3, multiDirectional.getGamma(), 0.0);
    }
    
    @Test
    public void testIterateSimplex() {
        MultiDirectional multiDirectional = new MultiDirectional();
        
        // Set up simplex with arbitrary values
        RealPointValuePair[] simplex = new RealPointValuePair[4];
        simplex[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 0.0);
        simplex[1] = new RealPointValuePair(new double[]{1.2, 2.4}, 0.0);
        simplex[2] = new RealPointValuePair(new double[]{0.8, 1.6}, 0.0);
        simplex[3] = new RealPointValuePair(new double[]{0.6, 1.2}, 0.0);
        multiDirectional.setSimplex(simplex);
        
        try {
            multiDirectional.iterateSimplex(new Comparator<RealPointValuePair>() {
                public int compare(RealPointValuePair a, RealPointValuePair b) {
                    return 0; // Comparator implementation
                }
            });

            // Verify the changes made to the simplex
            Assert.assertArrayEquals(simplex, multiDirectional.getSimplex());
            Assert.assertEquals(simplex[0], multiDirectional.getSimplex()[0]);
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
    }
    
    // Other test cases for each method...

}