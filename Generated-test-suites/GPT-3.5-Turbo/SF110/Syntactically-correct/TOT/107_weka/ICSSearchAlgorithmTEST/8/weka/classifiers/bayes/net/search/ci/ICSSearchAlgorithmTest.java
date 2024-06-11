import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import weka.classifiers.bayes.net.*;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class ICSSearchAlgorithmTest {
    private ICSSearchAlgorithm icsSearchAlgorithm;
    private BayesNet bayesNet;
    private Instances instances;
    
    @Before
    public void setUp() {
        icsSearchAlgorithm = new ICSSearchAlgorithm();
        bayesNet = new BayesNet();
        try {
            instances = DataSource.read("C:\\eclipse\\workspace\\weka\\data\\contact-lenses.arff");
            instances.setClassIndex(instances.numAttributes() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSearch() {
        try {
            icsSearchAlgorithm.search(bayesNet, instances);
            assertNotNull(icsSearchAlgorithm.m_BayesNet);
            assertNotNull(icsSearchAlgorithm.m_instances);
            // Add more assertions 
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testMaxn() {
        assertEquals(instances.numAttributes(), icsSearchAlgorithm.maxn());
    }
    
    @Test
    public void testSetMaxCardinality() {
        int maxCardinality = 3;
        icsSearchAlgorithm.setMaxCardinality(maxCardinality);
        assertEquals(maxCardinality, icsSearchAlgorithm.getMaxCardinality());
    }
    
    @Test
    public void testGetOptions() {
        String[] options = icsSearchAlgorithm.getOptions();
        // Add assertions for options
    }
    
    // Add more test cases for other methods
    
}