import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_File() throws FileNotFoundException {
        File file = new File("test.arff"); // Assume the test.arff file exists
        Dataset dataset = ARFFHandler.loadARFF(file);

        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    @Test
    public void testLoadARFF_File_ClassIndex() throws FileNotFoundException {
        File file = new File("test.arff"); // Assume the test.arff file exists
        int classIndex = 4; // Assume a valid class index

        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);

        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_File_FileNotFound() throws FileNotFoundException {
        File file = new File("nonexistent.arff");
        ARFFHandler.loadARFF(file);
    }

    @Test
    public void testLoadARFF_File_ClassIndex_InvalidClassIndex() throws FileNotFoundException {
        File file = new File("test.arff"); // Assume the test.arff file exists
        int classIndex = 10; // Assume an invalid class index

        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);

        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    @Test
    public void testLoadARFF_File_ClassIndex_InvalidData() throws FileNotFoundException {
        File file = new File("invalid.arff"); // Assume the invalid.arff file contains invalid data
        int classIndex = 0;

        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);

        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }
}