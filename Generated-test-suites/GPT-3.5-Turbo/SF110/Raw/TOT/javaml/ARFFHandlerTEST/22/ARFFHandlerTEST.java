
import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import net.sf.javaml.core.*;
import java.util.*;

public class ARFFHandlerTest {

    private File file;
    private File invalidFile;
    private File emptyFile;

    @Before
    public void setUp() {
        file = new File("test.arff");
        invalidFile = new File("invalid.arff");
        emptyFile = new File("empty.arff");
    }

    @After
    public void tearDown() {
        file.delete();
        invalidFile.delete();
        emptyFile.delete();
    }

    @Test
    public void testLoadARFFFileValid() {
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertNotNull(dataset);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFFileNotFound() throws FileNotFoundException {
        ARFFHandler.loadARFF(invalidFile);
    }

    @Test
    public void testLoadARFFFileEmpty() {
        try {
            Dataset dataset = ARFFHandler.loadARFF(emptyFile);
            assertNotNull(dataset);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testLoadARFFFileWithClassIndex() {
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, 0);
            assertNotNull(dataset);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testLoadARFFFileWithInvalidClassIndex() {
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, 3);
            assertNotNull(dataset);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}
