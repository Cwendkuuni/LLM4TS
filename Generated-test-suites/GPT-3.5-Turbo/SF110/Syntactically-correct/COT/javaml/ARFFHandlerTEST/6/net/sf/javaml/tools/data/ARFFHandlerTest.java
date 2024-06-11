
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_File() {
        File file = new File("test.arff");
        try {
            Dataset result = ARFFHandler.loadARFF(file);
            assertNotNull(result);
            // Additional assertions based on expected behavior
        } catch (FileNotFoundException e) {
            fail("File not found");
        }
    }
    
    @Test
    public void testLoadARFF_File_ClassIndex() {
        File file = new File("test.arff");
        int classIndex = 1;
        try {
            Dataset result = ARFFHandler.loadARFF(file, classIndex);
            assertNotNull(result);
            // Additional assertions based on expected behavior
        } catch (FileNotFoundException e) {
            fail("File not found");
        }
    }

}

