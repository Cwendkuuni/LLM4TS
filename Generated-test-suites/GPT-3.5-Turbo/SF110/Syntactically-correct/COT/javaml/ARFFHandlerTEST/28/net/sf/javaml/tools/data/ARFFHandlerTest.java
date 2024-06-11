import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.DenseInstance;

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
        int classIndex = 0;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        assertNotNull(dataset);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFFileNotFound() throws FileNotFoundException {
        File file = new File("nonexistent.arff");
        ARFFHandler.loadARFF(file);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFWithClassIndexFileNotFound() throws FileNotFoundException {
        File file = new File("nonexistent.arff");
        int classIndex = 0;
        ARFFHandler.loadARFF(file, classIndex);
    }

    @Test
    public void testLoadARFFEmptyFile() throws FileNotFoundException {
        File file = new File("empty.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    @Test
    public void testLoadARFFWithClassIndexEmptyFile() throws FileNotFoundException {
        File file = new File("empty.arff");
        int classIndex = 0;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    // Add more test cases for potential edge cases and exception handling scenarios here

}

