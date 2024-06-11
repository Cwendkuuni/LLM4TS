import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;

public class ARFFHandlerTest {

    private File datasetFile;
    private File emptyFile;
    private File invalidFile;
    private File nonExistentFile;
    
    @Before
    public void setUp() {
        datasetFile = new File("dataset.arff");
        emptyFile = new File("empty.arff");
        invalidFile = new File("invalid.arff");
        nonExistentFile = new File("nonexistent.arff");
    }
    
    @Test
    public void testLoadARFF_ValidFile_ReturnsDataset() throws FileNotFoundException {
        Dataset dataset = ARFFHandler.loadARFF(datasetFile);
        assertNotNull(dataset);
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_EmptyFile_ThrowsFileNotFoundException() throws FileNotFoundException {
        ARFFHandler.loadARFF(emptyFile);
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_InvalidFile_ThrowsFileNotFoundException() throws FileNotFoundException {
        ARFFHandler.loadARFF(invalidFile);
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_NonExistentFile_ThrowsFileNotFoundException() throws FileNotFoundException {
        ARFFHandler.loadARFF(nonExistentFile);
    }
    
    @Test
    public void testLoadARFF_ValidFileWithClassIndex_ReturnsDataset() throws FileNotFoundException {
        int classIndex = 2;
        Dataset dataset = ARFFHandler.loadARFF(datasetFile, classIndex);
        assertNotNull(dataset);
    }
    
    @Test
    public void testLoadARFF_ValidFileWithNegativeClassIndex_ReturnsDataset() throws FileNotFoundException {
        int classIndex = -1;
        Dataset dataset = ARFFHandler.loadARFF(datasetFile, classIndex);
        assertNotNull(dataset);
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_EmptyFileWithClassIndex_ThrowsFileNotFoundException() throws FileNotFoundException {
        int classIndex = 1;
        ARFFHandler.loadARFF(emptyFile, classIndex);
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_InvalidFileWithClassIndex_ThrowsFileNotFoundException() throws FileNotFoundException {
        int classIndex = 3;
        ARFFHandler.loadARFF(invalidFile, classIndex);
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_NonExistentFileWithClassIndex_ThrowsFileNotFoundException() throws FileNotFoundException {
        int classIndex = 0;
        ARFFHandler.loadARFF(nonExistentFile, classIndex);
    }
    
    @Test
    public void testLoadARFF_ValidFileWithInvalidData_ReturnsDataset() throws FileNotFoundException {
        Dataset dataset = ARFFHandler.loadARFF(datasetFile);
        assertNotNull(dataset);
    }
    
    @Test
    public void testLoadARFF_ValidFileWithInvalidDataAndClassIndex_ReturnsDataset() throws FileNotFoundException {
        int classIndex = 1;
        Dataset dataset = ARFFHandler.loadARFF(datasetFile, classIndex);
        assertNotNull(dataset);
    }
    
}