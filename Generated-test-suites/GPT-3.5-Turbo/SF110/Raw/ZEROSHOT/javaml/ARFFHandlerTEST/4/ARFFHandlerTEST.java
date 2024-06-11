import org.junit.Test;
import static org.junit.Assert.*;
import java.io.FileNotFoundException;
import java.io.File;

public class ARFFHandlerTest {
    
    @Test
    public void testLoadARFF1() {
        try {
            File file = new File("testFile.arff");
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertNotNull(dataset);
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException: " + e.getMessage());
        }
    }
    
    @Test
    public void testLoadARFF2() {
        try {
            File file = new File("testFile.arff");
            int classIndex = 1;
            Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
            assertNotNull(dataset);
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException: " + e.getMessage());
        }
    }
    
    @Test
    public void testLoadARFF3() {
        try {
            File file = new File("testFile.arff");
            int classIndex = -1;
            Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
            assertNotNull(dataset);
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException: " + e.getMessage());
        }
    }
}