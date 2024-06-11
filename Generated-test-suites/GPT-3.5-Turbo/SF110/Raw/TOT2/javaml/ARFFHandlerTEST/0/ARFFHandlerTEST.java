import static org.junit.Assert.*;
import org.junit.Test;
import net.sf.javaml.core.Dataset;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_singleFile() {
        // Test loading ARFF file with default class index
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            // Add assertions here to check if dataset loaded correctly
        } catch (Exception e) {
            fail("Exception thrown while loading ARFF file");
        }
    }
}