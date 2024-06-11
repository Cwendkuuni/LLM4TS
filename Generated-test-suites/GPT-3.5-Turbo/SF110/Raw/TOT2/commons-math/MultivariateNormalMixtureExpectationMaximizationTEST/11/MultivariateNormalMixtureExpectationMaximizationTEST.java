import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;
import java.util.*;
import org.apache.commons.math3.stat.correlation.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {
    
    @Test
    public void testConstructor_validData() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        assertNotNull(m);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_invalidData() {
        double[][] data = {};
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
    }
    
    @Test
    public void testFit_withMaxIterationsAndThreshold() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        m.fit(initialMixture, 100, 0.0001);
        assertNotNull(m.getFittedModel());
        assertTrue(m.getLogLikelihood() >= 0.0);
    }
    
    @Test
    public void testFit_withoutMaxIterationsAndThreshold() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        m.fit(initialMixture);
        assertNotNull(m.getFittedModel());
        assertTrue(m.getLogLikelihood() >= 0.0);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimate_invalidDataLength() {
        double[][] data = {{1.0, 2.0}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testEstimate_invalidNumComponents() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testEstimate_invalidNumComponentsLarge() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
    }
    
}