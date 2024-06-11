import static org.junit.Assert.*;
import org.junit.Test;
import net.sf.javaml.core.Dataset;
import java.io.File;
import java.io.FileNotFoundException;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertNotNull(dataset);
            // Add more assertions here to check the functionality of the method
        } catch (FileNotFoundException e) {
            fail("File not found exception");
        }
    }

    @Test
    public void testLoadARFFWithClassIndex() {
        File file = new File("test.arff");
        int classIndex = 2;
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
            assertNotNull(dataset);
            // Add more assertions here to check the functionality of the method with classIndex
        } catch (FileNotFoundException e) {
            fail("File not found exception");
        }
    }

    // Add more test cases here to cover the identified edge cases and exceptions

}



// Include the generated test cases here

