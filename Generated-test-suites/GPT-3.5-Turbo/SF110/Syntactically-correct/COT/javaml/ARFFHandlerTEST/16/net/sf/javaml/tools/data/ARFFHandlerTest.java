import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.DenseInstance;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    @Test
    public void testLoadARFFWithClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 1;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFFileNotFound() throws FileNotFoundException {
        File file = new File("nonexistent.arff");
        ARFFHandler.loadARFF(file);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFWithClassIndexFileNotFound() throws FileNotFoundException {
        File file = new File("nonexistent.arff");
        int classIndex = 1;
        ARFFHandler.loadARFF(file, classIndex);
    }

    @Test
    public void testLoadARFFWithInvalidValues() throws FileNotFoundException {
        File file = new File("invalid_values.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(1, dataset.size());
        Instance instance = dataset.get(0);
        double[] expectedValues = {1.0, Double.NaN, 3.0};
        String expectedClassValue = "class";
        assertArrayEquals(expectedValues, instance.getValues(), 0.001);
        assertEquals(expectedClassValue, instance.classValue());
    }

    @Test
    public void testLoadARFFWithInvalidValuesAndClassIndex() throws FileNotFoundException {
        File file = new File("invalid_values.arff");
        int classIndex = 1;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        assertNotNull(dataset);
        assertEquals(1, dataset.size());
        Instance instance = dataset.get(0);
        double[] expectedValues = {1.0, 3.0};
        String expectedClassValue = "class";
        assertArrayEquals(expectedValues, instance.getValues(), 0.001);
        assertEquals(expectedClassValue, instance.classValue());
    }
}