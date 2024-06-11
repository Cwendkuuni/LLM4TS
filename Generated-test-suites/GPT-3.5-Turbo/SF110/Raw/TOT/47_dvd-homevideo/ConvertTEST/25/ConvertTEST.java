import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConvertTest {
    
    private Convert convert;
    
    @Before
    public void setUp() {
        convert = new Convert("format", "aspectRatio", new GUI());
    }

    // Test init method
    @Test
    public void testInit() {
        boolean result = convert.init();
        
        // Test that the method does not return an error
        assertFalse(result);
        // Test that the error flag is false
        assertFalse(convert.m_Error);
    }
    
    // Test run method
    @Test
    public void testRun() {
        convert.run();
        
        // Test that the error flag is false
        assertFalse(convert.m_Error);
    }
    
    // Test Transcode method
    @Test
    public void testTranscode() {
        convert.Transcode();
        
        // Test that the error flag is false
        assertFalse(convert.m_Error);
    }
    
    // Test matrix method
    @Test
    public void testMatrix() {
        convert.matrix();
        
        // TODO: Add assertions to test that the matrix file is created successfully
    }

}