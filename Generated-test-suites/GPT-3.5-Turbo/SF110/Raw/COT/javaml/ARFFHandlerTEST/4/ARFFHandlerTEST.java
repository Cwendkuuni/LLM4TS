import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.ARFFHandler;

public class ARFFHandlerTest {

    // Test loadARFF(File file)
    @Test
    public void testLoadARFF_File() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertNotNull(dataset);
            assertTrue(dataset.isEmpty());
        } catch (FileNotFoundException e) {
            fail("File not found exception should not be thrown");
        }
    }

    // Test loadARFF(File file, int classIndex)
    @Test
    public void testLoadARFF_FileInt() {
        File file = new File("test.arff");
        int classIndex = 2;
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
            assertNotNull(dataset);
            assertTrue(dataset.isEmpty());
        } catch (FileNotFoundException e) {
            fail("File not found exception should not be thrown");
        }
    }

    // Test loadARFF(File file) with FileNotFoundException
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_File_FileNotFoundException() throws FileNotFoundException {
        File file = new File("nonexistent.arff");
        ARFFHandler.loadARFF(file);
    }

    // Test loadARFF(File file, int classIndex) with FileNotFoundException
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_FileInt_FileNotFoundException() throws FileNotFoundException {
        File file = new File("nonexistent.arff");
        int classIndex = 2;
        ARFFHandler.loadARFF(file, classIndex);
    }

    // Test loadARFF(File file) with empty file
    @Test
    public void testLoadARFF_File_EmptyFile() {
        File file = new File("empty.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertNotNull(dataset);
            assertTrue(dataset.isEmpty());
        } catch (FileNotFoundException e) {
            fail("File not found exception should not be thrown");
        }
    }

    // Test loadARFF(File file, int classIndex) with empty file
    @Test
    public void testLoadARFF_FileInt_EmptyFile() {
        File file = new File("empty.arff");
        int classIndex = 2;
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
            assertNotNull(dataset);
            assertTrue(dataset.isEmpty());
        } catch (FileNotFoundException e) {
            fail("File not found exception should not be thrown");
        }
    }

    // Test loadARFF(File file) with data line containing invalid double value
    @Test
    public void testLoadARFF_File_InvalidDoubleValue() {
        File file = new File("invalid_double.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertNotNull(dataset);
            assertTrue(dataset.isEmpty());
        } catch (FileNotFoundException e) {
            fail("File not found exception should not be thrown");
        }
    }

    // Test loadARFF(File file, int classIndex) with data line containing invalid double value
    @Test
    public void testLoadARFF_FileInt_InvalidDoubleValue() {
        File file = new File("invalid_double.arff");
        int classIndex = 2;
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
            assertNotNull(dataset);
            assertTrue(dataset.isEmpty());
        } catch (FileNotFoundException e) {
            fail("File not found exception should not be thrown");
        }
    }
}

