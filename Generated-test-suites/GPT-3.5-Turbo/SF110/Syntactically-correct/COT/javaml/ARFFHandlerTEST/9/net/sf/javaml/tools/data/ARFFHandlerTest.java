import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class ARFFHandlerTest {
    
    @Test
    public void testLoadARFF() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
    }

    @Test
    public void testLoadARFFWithClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 2;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        assertNotNull(dataset);
    }
    
    @Test
    public void testLoadARFFInvalidFile() {
        File file = new File("invalid.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            fail("Expected FileNotFoundException to be thrown");
        } catch (FileNotFoundException e) {
            // Exception caught as expected
        }
    }
    
    @Test
    public void testLoadARFFInvalidClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 100;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        assertNotNull(dataset);
    }
    
    @Test
    public void testLoadARFFInvalidDataFormat() throws FileNotFoundException {
        File file = new File("test_invalid_data.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            fail("Expected NumberFormatException to be thrown");
        } catch (NumberFormatException e) {
            // Exception caught as expected
        }
    }

}