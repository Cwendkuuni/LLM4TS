import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;

public class ARFFHandlerTest {
  
  @Test
  public void testLoadARFF() throws FileNotFoundException {
    File file = new File("test.arff");
    Dataset dataset = ARFFHandler.loadARFF(file);
    assertNotNull(dataset);
  }
  
  @Test
  public void testLoadARFFWithClassIndex() throws FileNotFoundException {
    File file = new File("test.arff");
    int classIndex = 1;
    Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
    assertNotNull(dataset);
  }
  
  @Test
  public void testLoadARFFWithInvalidFile() {
    File file = new File("invalid.arff");
    try {
      Dataset dataset = ARFFHandler.loadARFF(file);
      fail("Expected FileNotFoundException");
    } catch(FileNotFoundException e) {
      // expected exception
    }
  }
  
  @Test
  public void testLoadARFFWithInvalidClassIndex() throws FileNotFoundException {
    File file = new File("test.arff");
    int classIndex = 10;
    Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
    assertNotNull(dataset);
  }
  
  @Test
  public void testLoadARFFWithDataMissingClassValue() throws FileNotFoundException {
    File file = new File("test.arff");
    int classIndex = 1;
    Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
    assertNotNull(dataset);
    Instance instance = dataset.get(0);
    double[] values = instance.toDoubleArray();
    assertEquals(Double.NaN, values[classIndex], 0.0);
  }
  
  // Additional test cases can be added for other methods in the ARFFHandler class
  
}