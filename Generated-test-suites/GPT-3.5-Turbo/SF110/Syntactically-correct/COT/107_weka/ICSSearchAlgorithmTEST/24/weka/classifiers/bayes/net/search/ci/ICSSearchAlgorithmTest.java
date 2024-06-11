import static org.junit.Assert.*;
import org.junit.Test;

public class ICSSearchAlgorithmTest {

    @Test
    public void testSearch() throws Exception {
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
        BayesNet bayesNet = new BayesNet();
        Instances instances = new Instances(new FileReader("C:\\eclipse\\workspace\\weka\\data\\contact-lenses.arff"));
        algorithm.search(bayesNet, instances);
        // Add assertions here to check the functionality of the method
    }

    @Test
    public void testCalcDependencyGraph() throws Exception {
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
        boolean[][] edges = new boolean[algorithm.maxn() + 1][];
        SeparationSet[][] sepsets = new SeparationSet[algorithm.maxn() + 1][];
        algorithm.calcDependencyGraph(edges, sepsets);
        // Add assertions here to check the functionality of the method
    }
    
    // Add more test methods for each public method in the ICSSearchAlgorithm class

    

    // Combine all the individual test methods into a single test suite

    
}