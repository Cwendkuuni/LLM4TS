import java.io.File;
import java.io.FileNotFoundException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ARFFHandlerTest {
    @Test
    public void testLoadARFFWithoutClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        
        // Add test assertions here
    }
    
    @Test
    public void testLoadARFFWithClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 2;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        
        // Add test assertions here
    }
}