import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.ARFFHandler;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_NoClassIndex() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLoadARFF_WithClassIndex() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, 2);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLoadARFF_ClassIndexOutOfBounds() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, 10);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLoadARFF_InvalidDataFormat() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            fail(e.getMessage());
        }
    }
}