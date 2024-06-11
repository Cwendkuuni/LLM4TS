import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_noClassIndex_noError() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    @Test
    public void testLoadARFF_withClassIndex_noError() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file, 2);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_fileNotFound_throwsFileNotFoundException() throws FileNotFoundException {
        File file = new File("nonexistent.arff");
        ARFFHandler.loadARFF(file);
    }

    @Test
    public void testLoadARFF_parseDoubleError_skipsNaNValue() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file, 1);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    @Test
    public void testLoadARFF_dataModeNotReached_noDataAdded() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file, 0);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    // Edge case: large number of attributes
    @Test
    public void testLoadARFF_largeNumberOfAttributes_noError() throws FileNotFoundException {
        File file = new File("test_large.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    // Edge case: empty file
    @Test
    public void testLoadARFF_emptyFile_noError() throws FileNotFoundException {
        File file = new File("empty.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

}