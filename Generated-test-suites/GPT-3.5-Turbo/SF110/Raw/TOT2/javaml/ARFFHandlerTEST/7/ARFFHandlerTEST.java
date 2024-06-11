
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;

public class ARFFHandlerTest {
    @Test
    public void testLoadARFF() {
        File file = new File("test.arff");
        try {
            Dataset result = ARFFHandler.loadARFF(file);
            assertNotNull(result);
        } catch (FileNotFoundException e) {
            fail("File not found exception");
        }
    }
}
