package net.sf.javaml.tools.data;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.DenseInstance;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertNotNull(dataset);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            fail("File not found exception thrown unexpectedly");
        }
    }

    @Test
    public void testLoadARFFWithClassIndex() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, 1);
            assertNotNull(dataset);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            fail("File not found exception thrown unexpectedly");
        }
    }

    @Test
    public void testLoadARFFWithInvalidFile() {
        File file = new File("invalidFile.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            fail("FileNotFoundException not thrown for invalid file");
        } catch (FileNotFoundException e) {
            // Expected exception
        }
    }

    @Test
    public void testLoadARFFWithInvalidClassIndex() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, -1);
            fail("IllegalArgumentException not thrown for invalid class index");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (FileNotFoundException e) {
            fail("Unexpected File not found exception thrown");
        }
    }

    @Test
    public void testLoadARFFWithInvalidDataValues() {
        File file = new File("test_invalid.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertNotNull(dataset);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            fail("File not found exception thrown unexpectedly");
        }
    }

    @Test
    public void testLoadARFFWithEmptyFile() {
        File file = new File("empty.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertNotNull(dataset);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            fail("File not found exception thrown unexpectedly");
        }
    }
}