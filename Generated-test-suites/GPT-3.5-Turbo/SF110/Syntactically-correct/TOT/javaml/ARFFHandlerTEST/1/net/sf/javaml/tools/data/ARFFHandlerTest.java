
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import net.sf.javaml.core.Dataset;
import java.io.File;
import java.io.FileNotFoundException;

public class ARFFHandlerTest {

    private final String filePath = "test.arff";
    private final File invalidFile = new File("invalidfile.arff");
    private final File nonExistingFile = new File("nonexistingfile.arff");

    @Before
    public void setUp() {
        // Create a valid ARFF file for testing
        String content = "@relation iris\n\n@attribute sepallength numeric\n@data\n5.1,3.5,1.4,0.2,Iris-setosa\n";
        try {
            File file = new File(filePath);
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
        } catch (Exception e) {
            fail("Failed to create test file");
        }
    }

    @Test
    public void testLoadARFF_ValidFile() {
        try {
            Dataset dataset = ARFFHandler.loadARFF(new File(filePath));
            assertNotNull("Dataset should not be null", dataset);
            assertEquals("Dataset size should be 1", 1, dataset.size());
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException should not be thrown");
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_InvalidFile() throws FileNotFoundException {
        ARFFHandler.loadARFF(invalidFile);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_NonExistingFile() throws FileNotFoundException {
        ARFFHandler.loadARFF(nonExistingFile);
    }

    @Test
    public void testLoadARFF_ClassIndex() {
        try {
            Dataset dataset = ARFFHandler.loadARFF(new File(filePath), 4);
            assertNotNull("Dataset should not be null", dataset);
            assertEquals("Dataset size should be 1", 1, dataset.size());
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException should not be thrown");
        }
    }
}
