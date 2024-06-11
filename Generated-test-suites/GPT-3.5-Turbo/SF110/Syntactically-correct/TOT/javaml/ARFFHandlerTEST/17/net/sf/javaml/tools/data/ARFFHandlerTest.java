import net.sf.javaml.core.*;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_ValidFile_ReturnsDataset() throws FileNotFoundException {
        File file = new File("valid_file.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertTrue(dataset.size() > 0);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_InvalidFile_ThrowsFileNotFoundException() throws FileNotFoundException {
        File file = new File("invalid_file.arff");
        ARFFHandler.loadARFF(file);
    }

    @Test
    public void testLoadARFF_ValidFileWithClassIndex_ReturnsDatasetWithClassValues() throws FileNotFoundException {
        File file = new File("valid_file.arff");
        int classIndex = 3;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        assertNotNull(dataset);
        assertTrue(dataset.size() > 0);
        for (Instance instance : dataset) {
            assertNotNull(instance.classValue());
        }
    }

    @Test
    public void testLoadARFF_ValidFileWithoutClassIndex_ReturnsDatasetWithoutClassValues() throws FileNotFoundException {
        File file = new File("valid_file.arff");
        int classIndex = -1;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        assertNotNull(dataset);
        assertTrue(dataset.size() > 0);
        for (Instance instance : dataset) {
            assertNull(instance.classValue());
        }
    }
  
    @Test
    public void testLoadARFF_FileWithDataMode_ReturnsDatasetWithInstances() throws FileNotFoundException {
        File file = new File("file_with_data.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertTrue(dataset.size() > 0);
    }
  
    @Test
    public void testLoadARFF_FileWithoutDataMode_ReturnsEmptyDataset() throws FileNotFoundException {
        File file = new File("file_without_data.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }
}