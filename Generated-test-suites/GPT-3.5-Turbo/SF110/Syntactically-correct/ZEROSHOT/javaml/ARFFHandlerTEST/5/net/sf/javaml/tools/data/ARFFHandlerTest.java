import org.junit.*;
import java.io.*;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        
        // Assert dataset is not null
        Assert.assertNotNull(dataset);
        
        // Assert dataset is an instance of DefaultDataset
        Assert.assertTrue(dataset instanceof DefaultDataset);
    }
    
    @Test
    public void testLoadARFFWithClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 2;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        
        // Assert dataset is not null
        Assert.assertNotNull(dataset);
        
        // Assert dataset is an instance of DefaultDataset
        Assert.assertTrue(dataset instanceof DefaultDataset);
    }
}