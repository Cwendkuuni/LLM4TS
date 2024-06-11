import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.DenseInstance;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() throws FileNotFoundException {
        // Test with empty file
        Dataset result = ARFFHandler.loadARFF(new File("empty.arff"));
        assertEquals(new DefaultDataset(), result);

        // Test with file containing only comments
        result = ARFFHandler.loadARFF(new File("comments.arff"));
        assertEquals(new DefaultDataset(), result);

        // Test with valid dataset file
        result = ARFFHandler.loadARFF(new File("validDataset.arff"));
        assertEquals(10, result.size());

        // Test with invalid dataset file
        result = ARFFHandler.loadARFF(new File("invalidDataset.arff"));
        assertNull(result);
    }

    @Test
    public void testLoadARFFWithClassIndex() throws FileNotFoundException {
        // Test with empty file
        Dataset result = ARFFHandler.loadARFF(new File("empty.arff"), 0);
        assertEquals(new DefaultDataset(), result);

        // Test with file containing only comments
        result = ARFFHandler.loadARFF(new File("comments.arff"), 1);
        assertEquals(new DefaultDataset(), result);

        // Test with valid dataset file
        result = ARFFHandler.loadARFF(new File("validDataset.arff"), 2);
        assertEquals(10, result.size());

        // Test with invalid dataset file
        result = ARFFHandler.loadARFF(new File("invalidDataset.arff"), 3);
        assertNull(result);
    }
}