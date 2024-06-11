
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.DenseInstance;

@RunWith(JUnit4.class)
public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() {
        // Test basic functionality of loadARFF() method
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            // Check if dataset is not null
            Assert.assertNotNull(dataset);
            // Check if dataset is empty
            Assert.assertFalse(dataset.isEmpty());
        } catch (FileNotFoundException e) {
            // File not found exception
            Assert.fail("fileNotFoundException");
        }
    }

    @Test
    public void testLoadARFFWithClassIndex() {
        // Test basic functionality of loadARFF() method
        File file = new File("test.arff");
        int classIndex = 2;
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
            // Check if dataset is not null
            Assert.assertNotNull(dataset);
            // Check if dataset is empty
            Assert.assertFalse(dataset.isEmpty());
        } catch (FileNotFoundException e) {
            // File not found exception
            Assert.fail("fileNotFoundException");
        }
    }

    @Test
    public void testLoadARFFWithInvalidFile() {
        // Test exception handling for invalid file
        File file = new File("invalid.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            Assert.fail("No exception thrown for invalid file");
        } catch (FileNotFoundException e) {
            // File not found exception, expected behavior
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testLoadARFFWithInvalidFileAndClassIndex() {
        // Test exception handling for invalid file with class index
        File file = new File("invalid.arff");
        int classIndex = 2;
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
            Assert.fail("No exception thrown for invalid file");
        } catch (FileNotFoundException e) {
            // File not found exception, expected behavior
            Assert.assertTrue(true);
        }
    }
}

