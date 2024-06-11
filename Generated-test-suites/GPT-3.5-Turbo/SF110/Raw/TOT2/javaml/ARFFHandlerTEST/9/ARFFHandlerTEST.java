import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFFWithDefaultClassIndex() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertNotNull(dataset);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            fail("Failed to load ARFF file with default class index");
        }
    }

    @Test
    public void testLoadARFFWithSpecifiedClassIndex() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, 1);
            assertNotNull(dataset);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            fail("Failed to load ARFF file with specified class index");
        }
    }

    @Test
    public void testLoadARFFWithInvalidFile() {
        File file = new File("invalidFile.arff");
        try {
            ARFFHandler.loadARFF(file);
            fail("Loaded ARFF file that does not exist");
        } catch (FileNotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testLoadARFFWithInvalidClassIndex() {
        File file = new File("test.arff");
        try {
            ARFFHandler.loadARFF(file, 5);
            fail("Loaded ARFF file with invalid class index");
        } catch (FileNotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testLoadARFFWithInvalidDataValue() {
        File file = new File("invalidData.arff");
        try {
            ARFFHandler.loadARFF(file);
            fail("Loaded ARFF file with invalid data value");
        } catch (NumberFormatException e) {
            assertTrue(true);
        }
    }
}