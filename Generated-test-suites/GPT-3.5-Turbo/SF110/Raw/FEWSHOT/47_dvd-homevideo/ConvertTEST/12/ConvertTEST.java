import org.junit.Test;
import static org.junit.Assert.*;

public class ConvertTest {
    
    @Test
    public void testInit() {
        Convert convert = new Convert("format", "aspectRatio", new GUI());
        
        boolean result = convert.init();
        
        assertFalse(result);
    }
    
    @Test
    public void testRun() {
        Convert convert = new Convert("format", "aspectRatio", new GUI());
        
        convert.run();
    }
    
    @Test
    public void testTranscode() {
        Convert convert = new Convert("format", "aspectRatio", new GUI());
        
        convert.Transcode();
    }
    
    @Test
    public void testMatrix() {
        Convert convert = new Convert("format", "aspectRatio", new GUI());
        
        convert.matrix();
    }
}