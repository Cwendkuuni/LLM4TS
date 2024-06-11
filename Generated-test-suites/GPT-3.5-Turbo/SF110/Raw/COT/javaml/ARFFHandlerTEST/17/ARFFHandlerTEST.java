
import static org.junit.Assert.*;
import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.ARFFHandler;
import org.junit.Test;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() {
        File file = new File("path/to/valid.arff");

        try {
            Dataset result = ARFFHandler.loadARFF(file);

            assertNotNull(result);
            // Add additional assertions based on the expected result of the method
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException should not be thrown");
        }
    }

    @Test
    public void testLoadARFFWithClassIndex() {
        File file = new File("path/to/valid.arff");
        int classIndex = 0;

        try {
            Dataset result = ARFFHandler.loadARFF(file, classIndex);
            assertNotNull(result);
            // Add additional assertions based on the expected result of the method
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException should not be thrown");
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFInvalidFile() throws FileNotFoundException {
        File file = new File("path/to/invalid.arff");
        ARFFHandler.loadARFF(file);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFInvalidFileWithClassIndex() throws FileNotFoundException {
        File file = new File("path/to/invalid.arff");
        int classIndex = 0;
        ARFFHandler.loadARFF(file, classIndex);
    }

    // Add additional test cases for other public methods in ARFFHandler if necessary

}

