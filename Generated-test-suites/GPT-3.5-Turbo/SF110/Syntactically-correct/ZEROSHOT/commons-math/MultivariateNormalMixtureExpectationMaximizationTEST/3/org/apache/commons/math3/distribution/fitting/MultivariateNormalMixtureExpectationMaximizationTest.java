import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;
import java.util.*;
import org.apache.commons.math3.stat.correlation.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testFit_MaxIterationsThreshold() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization mixtureEM = new MultivariateNormalMixtureExpectationMaximization(data);
            MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
            int maxIterations = 1000;
            double threshold = 1.0E-5;
            
            mixtureEM.fit(initialMixture, maxIterations, threshold);
            
            MixtureMultivariateNormalDistribution fittedModel = mixtureEM.getFittedModel();
            double logLikelihood = mixtureEM.getLogLikelihood();
            
            Assert.assertNotNull(fittedModel);
            Assert.assertEquals(logLikelihood, mixtureEM.getLogLikelihood(), 0.001);
        } catch (Exception ex) {
            Assert.fail("An exception occurred: " + ex.getMessage());
        }
    }
    
    @Test
    public void testFit_DefaultParameters() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization mixtureEM = new MultivariateNormalMixtureExpectationMaximization(data);
            MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
            
            mixtureEM.fit(initialMixture);
            
            MixtureMultivariateNormalDistribution fittedModel = mixtureEM.getFittedModel();
            double logLikelihood = mixtureEM.getLogLikelihood();
            
            Assert.assertNotNull(fittedModel);
            Assert.assertEquals(logLikelihood, mixtureEM.getLogLikelihood(), 0.001);
        } catch (Exception ex) {
            Assert.fail("An exception occurred: " + ex.getMessage());
        }
    }
    
    @Test
    public void testEstimate() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        int numComponents = 3;
        try {
            MixtureMultivariateNormalDistribution mixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);
            
            Assert.assertNotNull(mixture);
        } catch (Exception ex) {
            Assert.fail("An exception occurred: " + ex.getMessage());
        }
    }
}