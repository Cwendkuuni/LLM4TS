package net.sf.javaml.tools.data;
import static org.junit.Assert.*;
import org.junit.Test;
import net.sf.javaml.core.Dataset;
import java.io.File;
import java.io.FileNotFoundException;

public class ARFFHandlerTest {
    
    @Test
    public void testLoadARFF_file() {
        File file = new File("test.arff");
        Dataset data = null;
        try {
            data = ARFFHandler.loadARFF(file);
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException thrown for valid file path");
        }
        assertNotNull(data);
        // Add more assertions here if needed
    }
    
    @Test
    public void testLoadARFF_file_classIndex() {
        File file = new File("test.arff");
        Dataset data = null;
        try {
            data = ARFFHandler.loadARFF(file, 1);
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException thrown for valid file path");
        }
        assertNotNull(data);
        // Add more assertions here if needed
    }
    
    // Edge cases and exception handling scenarios
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_invalidFile() throws FileNotFoundException {
        File file = new File("nonexistentfile.arff");
        ARFFHandler.loadARFF(file);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testLoadARFF_invalidClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        ARFFHandler.loadARFF(file, -2);
    }
    
    // Add more edge case tests here
    
}