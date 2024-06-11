import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.ARFFHandler;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() throws FileNotFoundException {
        File file = new File("test.arff"); // replace with your test file
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        // add more assertions to test the specific behavior of loadARFF() method
    }

    @Test
    public void testLoadARFFWithClassIndex() throws FileNotFoundException {
        File file = new File("test.arff"); // replace with your test file
        int classIndex = 1; // replace with the desired value of classIndex
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        assertNotNull(dataset);
        // add more assertions to test the specific behavior of loadARFF() method with classIndex
    }

    // add more test methods to test all the other methods in ARFFHandler class

}