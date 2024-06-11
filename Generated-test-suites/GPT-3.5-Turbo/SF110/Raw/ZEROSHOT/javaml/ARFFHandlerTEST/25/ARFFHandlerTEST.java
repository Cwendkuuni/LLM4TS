import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.DenseInstance;
import java.util.Arrays;

public class ARFFHandlerTest {

    private ARFFHandler arffHandler;

    @Before
    public void setUp() {
        arffHandler = new ARFFHandler();
    }

    @Test
    public void testLoadARFF_defaultClassIndex() {
        File file = new File("test.arff");
        try {
            Dataset dataset = arffHandler.loadARFF(file);
            assertNotNull(dataset);
            // Add relevant assertions here
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException should not be thrown");
        }
    }

    @Test
    public void testLoadARFF_customClassIndex() {
        File file = new File("test.arff");
        int classIndex = 3;
        try {
            Dataset dataset = arffHandler.loadARFF(file, classIndex);
            assertNotNull(dataset);
            // Add relevant assertions here
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException should not be thrown");
        }
    }

    // Add more test methods for each method in ARFFHandler class...

}