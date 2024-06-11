package net.sf.javaml.tools.data;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFFWithClassIndex() {
        File file = new File("data.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, 1);
            assertNotNull(dataset);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            fail("File not found exception should not be thrown");
        }
    }

    @Test
    public void testLoadARFFWithoutClassIndex() {
        File file = new File("data.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertNotNull(dataset);
        } catch (FileNotFoundException e) {
            fail("File not found exception should not be thrown");
        }
    }

    @Test
    public void testLoadARFFInvalidFile() {
        File file = new File("invalid.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            fail("FileNotFoundException should be thrown");
        } catch (FileNotFoundException e) {
            // Exception expected
        }
    }

    @Test
    public void testLoadARFFWithInvalidData() {
        File file = new File("data_invalid.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, 1);
            fail("NumberFormatException should be thrown");
        } catch (NumberFormatException e) {
            // Exception expected
        } catch (FileNotFoundException e) {
            fail("File not found exception should not be thrown");
        }
    }

    @Test
    public void testLoadARFFIncorrectClassIndex() {
        File file = new File("data.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, 5);
            fail("IndexOutOfBoundsException should be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Exception expected
        } catch (FileNotFoundException e) {
            fail("File not found exception should not be thrown");
        }
    }
}