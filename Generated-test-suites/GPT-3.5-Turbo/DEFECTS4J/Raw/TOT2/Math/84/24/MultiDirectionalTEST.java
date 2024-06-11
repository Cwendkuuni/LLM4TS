import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.optimization.direct.MultiDirectional;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.RealConvergenceChecker;

public class MultiDirectionalTest {

    @Test
    public void testDefaultConstructor() {
        MultiDirectional multiDirectional = new MultiDirectional();
        Assert.assertEquals(2.0, multiDirectional.getKhi(), 0.0001);
        Assert.assertEquals(0.5, multiDirectional.getGamma(), 0.0001);
    }

    @Test
    public void testCustomConstructor() {
        MultiDirectional multiDirectional = new MultiDirectional(1.0, 0.1);
        Assert.assertEquals(1.0, multiDirectional.getKhi(), 0.0001);
        Assert.assertEquals(0.1, multiDirectional.getGamma(), 0.0001);
    }

    @Test
    public void testIterateSimplex() {
        MultiDirectional multiDirectional = new MultiDirectional();
        multiDirectional.setConvergenceChecker(new RealConvergenceChecker());
        multiDirectional.setSimplex(new RealPointValuePair[]{new RealPointValuePair(new double[]{0.0, 0.0}, 0.0)});
        try {
            multiDirectional.iterateSimplex(new Comparator<RealPointValuePair>() {
                @Override
                public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                    return Double.compare(o1.getValue(), o2.getValue());
                }
            });
        } catch (Exception ex) {
            Assert.fail("Exception thrown during iterateSimplex method.");
        }
    }

    // Add additional test cases for other public methods in MultiDirectional class

}