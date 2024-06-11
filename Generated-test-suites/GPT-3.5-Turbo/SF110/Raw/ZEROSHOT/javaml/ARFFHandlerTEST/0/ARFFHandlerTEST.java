import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.FileNotFoundException;
import be.abeel.util.LineIterator;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.ARFFHandler;
import java.util.Iterator;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFFWithoutClassIndex() throws FileNotFoundException {
        File file = new File("test_file.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }
    
    @Test
    public void testLoadARFFWithClassIndex() throws FileNotFoundException {
        File file = new File("test_file.arff");
        Dataset dataset = ARFFHandler.loadARFF(file, 1);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFFileNotFound() throws FileNotFoundException {
        File file = new File("nonexistent_file.arff");
        ARFFHandler.loadARFF(file);
    }
    
    @Test
    public void testLoadARFFInvalidData() throws FileNotFoundException {
        File file = new File("invalid_data.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }
    
    @Test
    public void testLoadARFFValidData() throws FileNotFoundException {
        File file = new File("valid_data.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(1, dataset.size());
        Iterator<Instance> it = dataset.iterator();
        Instance instance = it.next();
        assertNotNull(instance);
    }
}