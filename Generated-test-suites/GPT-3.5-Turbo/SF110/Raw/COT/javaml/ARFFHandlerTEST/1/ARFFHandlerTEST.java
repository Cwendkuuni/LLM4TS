import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;
import java.util.*;
import be.abeel.util.*;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_file() {
        File file = new File("data.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertNotNull(dataset);
            // Additional assertions for normal functionality
        } catch (FileNotFoundException e) {
            fail("File not found exception");
        }
    }

    @Test
    public void testLoadARFF_file_classIndex() {
        File file = new File("data.arff");
        int classIndex = 0;
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
            assertNotNull(dataset);
            // Additional assertions for normal functionality
        } catch (FileNotFoundException e) {
            fail("File not found exception");
        }
    }

    @Test
    public void testLoadARFF_invalidFile() {
        File file = new File("invalid.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            fail("FileNotFoundException not thrown");
        } catch (FileNotFoundException e) {
            // Exception handling scenario
            assertEquals("File not found exception", e.getMessage());
        }
    }

    @Test
    public void testLoadARFF_invalidFile_classIndex() {
        File file = new File("invalid.arff");
        int classIndex = 0;
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
            fail("FileNotFoundException not thrown");
        } catch (FileNotFoundException e) {
            // Exception handling scenario
            assertEquals("File not found exception", e.getMessage());
        }
    }

    @Test
    public void testLoadARFF_emptyFile() {
        File file = new File("empty.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            // Additional assertions for empty file handling
        } catch (FileNotFoundException e) {
            fail("File not found exception");
        }
    }

    @Test
    public void testLoadARFF_emptyFile_classIndex() {
        File file = new File("empty.arff");
        int classIndex = 0;
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
            // Additional assertions for empty file handling
        } catch (FileNotFoundException e) {
            fail("File not found exception");
        }
    }

    // Add more test cases for specific edge cases and exception handling scenarios

}