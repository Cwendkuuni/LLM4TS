package net.sf.javaml.tools.data;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFFWithNoClassIndex() {
        File file = new File("test.arff");
        Dataset dataset = null;
        try {
            dataset = ARFFHandler.loadARFF(file);
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException should not be thrown");
        }
        assertNotNull("Dataset should not be null", dataset);
        // Add more assertions here if needed
    }

    @Test
    public void testLoadARFFWithClassIndex() {
        File file = new File("test.arff");
        Dataset dataset = null;
        try {
            dataset = ARFFHandler.loadARFF(file, 0);
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException should not be thrown");
        }
        assertNotNull("Dataset should not be null", dataset);
        // Add more assertions here if needed
    }

    // Add more test cases for edge cases and exceptions here

}