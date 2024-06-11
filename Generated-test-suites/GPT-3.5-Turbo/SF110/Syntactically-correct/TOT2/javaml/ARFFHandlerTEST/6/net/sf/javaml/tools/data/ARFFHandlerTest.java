package net.sf.javaml.tools.data;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() {
        try {
            File file = new File("test.arff");
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testLoadARFF_ClassIndex() {
        try {
            File file = new File("test.arff");
            Dataset dataset = ARFFHandler.loadARFF(file, 1);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testLoadARFF_InvalidFile() {
        try {
            File file = new File("non_existing_file.arff");
            Dataset dataset = ARFFHandler.loadARFF(file);
            fail("File not found exception should have been thrown");
        } catch (FileNotFoundException e) {
            // Exception expected
        }
    }

    @Test
    public void testLoadARFF_ClassIndex_InvalidFile() {
        try {
            File file = new File("non_existing_file.arff");
            Dataset dataset = ARFFHandler.loadARFF(file, 1);
            fail("File not found exception should have been thrown");
        } catch (FileNotFoundException e) {
            // Exception expected
        }
    }

}