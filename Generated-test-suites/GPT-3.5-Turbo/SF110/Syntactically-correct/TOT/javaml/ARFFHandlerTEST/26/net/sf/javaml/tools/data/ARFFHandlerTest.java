import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import net.sf.javaml.core.*;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_validFile_noClassIndex() {
        File file = new File("valid_file.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertNotNull(dataset);
            assertEquals(2, dataset.size());
            // Assert other properties of the dataset
        } catch (FileNotFoundException e) {
            fail("File not found exception should not occur");
        }
    }

    @Test
    public void testLoadARFF_validFile_withClassIndex() {
        File file = new File("valid_file.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, 3);
            assertNotNull(dataset);
            assertEquals(2, dataset.size());
            // Assert other properties of the dataset
        } catch (FileNotFoundException e) {
            fail("File not found exception should not occur");
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_fileNotFound_noClassIndex() throws FileNotFoundException {
        File file = new File("nonexistent_file.arff");
        ARFFHandler.loadARFF(file);
        // FileNotFoundException should be thrown
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_fileNotFound_withClassIndex() throws FileNotFoundException {
        File file = new File("nonexistent_file.arff");
        ARFFHandler.loadARFF(file, 3);
        // FileNotFoundException should be thrown
    }

    @Test
    public void testLoadARFF_invalidFile_noClassIndex() {
        File file = new File("invalid_file.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            fail("Exception should have been thrown for invalid file");
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException should not be thrown for invalid file");
        } catch (Exception e) {
            // Exception should be thrown for invalid file
        }
    }

    @Test
    public void testLoadARFF_invalidFile_withClassIndex() {
        File file = new File("invalid_file.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, 3);
            fail("Exception should have been thrown for invalid file");
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException should not be thrown for invalid file");
        } catch (Exception e) {
            // Exception should be thrown for invalid file
        }
    }

    // Add more test cases for corner cases, boundary values, and error scenarios

}