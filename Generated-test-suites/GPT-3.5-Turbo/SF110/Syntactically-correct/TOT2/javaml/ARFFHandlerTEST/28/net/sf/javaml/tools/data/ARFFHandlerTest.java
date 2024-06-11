package net.sf.javaml.tools.data;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_NoClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        // Add more assertions as needed
    }

    @Test
    public void testLoadARFF_WithClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file, 0);
        assertNotNull(dataset);
        // Add more assertions as needed
    }

    @Test
    public void testLoadARFF_EmptyFile() {
        File file = new File("empty.arff");
        try {
            ARFFHandler.loadARFF(file);
            fail("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
            // Expected exception
        }
    }

    // Add more test cases for edge cases and error scenarios

}