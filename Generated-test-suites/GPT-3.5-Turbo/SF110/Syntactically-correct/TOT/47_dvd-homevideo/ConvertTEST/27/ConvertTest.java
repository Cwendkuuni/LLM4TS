import static org.junit.Assert.*;
import org.junit.Test;

public class ConvertTest {
    
    @Test
    public void testInit(){
        GUI gui = new GUI(); //create a mock GUI object
        Convert convert = new Convert("dvd-ntsc", "16:9", gui);
        
        boolean result = convert.init();
        
        assertFalse(result); //assert that the method does not return an error
        
        //add additional tests for different scenarios
        
    }
    
    @Test
    public void testTranscode(){
        GUI gui = new GUI(); //create a mock GUI object
        Convert convert = new Convert("dvd-ntsc", "16:9", gui);
        
        convert.Transcode();
        
        //add assertions to check if the method performs the expected actions
        
    }
    
    @Test
    public void testMatrix(){
        GUI gui = new GUI(); //create a mock GUI object
        Convert convert = new Convert("dvd-ntsc", "16:9", gui);
        
        convert.matrix();
        
        //add assertions to check the generated matrix.txt file
        
    }
}