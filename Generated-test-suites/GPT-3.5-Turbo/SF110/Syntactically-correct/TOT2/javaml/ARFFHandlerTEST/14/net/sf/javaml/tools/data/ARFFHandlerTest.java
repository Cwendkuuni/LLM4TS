package net.sf.javaml.tools.data;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.tools.data.ARFFHandler;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    @Test
    public void testLoadARFFWithClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file, 1);
        
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    @Test
    public void testLoadARFFWithInvalidFile() {
        File file = new File("invalidfile.arff");
        
        try {
            ARFFHandler.loadARFF(file);
            fail("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
            // Expected exception
        }
    }

    @Test
    public void testLoadARFFWithInvalidClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        
        try {
            ARFFHandler.loadARFF(file, 10);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test
    public void testLoadARFFWithIncorrectDataFormat() throws FileNotFoundException {
        File file = new File("incorrectformat.arff");
        
        try {
            ARFFHandler.loadARFF(file);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }
}