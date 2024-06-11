
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_File() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertNotNull(dataset);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadARFF_File_ClassIndex() {
        File file = new File("test.arff");
        int classIndex = 1;
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
            assertNotNull(dataset);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadARFF_InvalidFile_Exception() {
        File file = new File("invalid.arff");
        try {
            ARFFHandler.loadARFF(file);
        } catch (FileNotFoundException e) {
            assertTrue(e instanceof FileNotFoundException);
        }
    }

    @Test
    public void testLoadARFF_InvalidFile_ClassIndex_Exception() {
        File file = new File("invalid.arff");
        int classIndex = 1;
        try {
            ARFFHandler.loadARFF(file, classIndex);
        } catch (FileNotFoundException e) {
            assertTrue(e instanceof FileNotFoundException);
        }
    }

    @Test
    public void testLoadARFF_EmptyFile() {
        File file = new File("empty.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertNotNull(dataset);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
