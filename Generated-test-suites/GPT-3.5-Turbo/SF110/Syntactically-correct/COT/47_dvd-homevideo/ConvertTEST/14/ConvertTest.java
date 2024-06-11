
import org.junit.Before;
import org.junit.Test;

public class ConvertTest {
    
    private Convert convert;
    
    @Before
    public void setUp() {
        convert = new Convert("format", "aspectRatio", new GUI());
    }
    
    @Test
    public void testInit() {
        boolean result = convert.init();
        // Add test assertions here
    }
    
    @Test
    public void testRun() {
        convert.run();
        // Add test assertions here
    }
    
    @Test
    public void testTranscode() {
        convert.Transcode();
        // Add test assertions here
    }
    
    @Test
    public void testMatrix() {
        convert.matrix();
        // Add test assertions here
    }
}
