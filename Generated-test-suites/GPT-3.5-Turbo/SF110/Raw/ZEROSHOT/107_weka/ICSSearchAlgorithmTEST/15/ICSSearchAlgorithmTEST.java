import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import weka.core.Instances;
import java.io.FileReader;

public class ICSSearchAlgorithmTest {
  
  private ICSSearchAlgorithm searchAlgorithm;

  @Before
  public void setUp() {
    searchAlgorithm = new ICSSearchAlgorithm();
  }

  @Test
  public void testMaxn() {
    Instances instances = new Instances(new FileReader("C:\\eclipse\\workspace\\weka\\data\\contact-lenses.arff"));
    int expectedMaxn = instances.numAttributes();
    int actualMaxn = searchAlgorithm.maxn();
    assertEquals(expectedMaxn, actualMaxn);
  }

  @Test
  public void testSetMaxCardinality() {
    int expectedMaxCardinality = 3;
    searchAlgorithm.setMaxCardinality(expectedMaxCardinality);
    int actualMaxCardinality = searchAlgorithm.getMaxCardinality();
    assertEquals(expectedMaxCardinality, actualMaxCardinality);
  }

  @Test
  public void testSearch() {
    //TODO: Implement test for search method
  }

  @Test
  public void testCalcDependencyGraph() {
    //TODO: Implement test for calcDependencyGraph method
  }

  @Test
  public void testCalcVeeNodes() {
    //TODO: Implement test for calcVeeNodes method
  }

  @Test
  public void testCalcArcDirections() {
    //TODO: Implement test for calcArcDirections method
  }

  @Test
  public void testSetOptions() {
    //TODO: Implement test for setOptions method
  }

  @Test
  public void testGetOptions() {
    //TODO: Implement test for getOptions method
  }
  
  //TODO: Add more tests to cover all methods in ICSSearchAlgorithm class

}