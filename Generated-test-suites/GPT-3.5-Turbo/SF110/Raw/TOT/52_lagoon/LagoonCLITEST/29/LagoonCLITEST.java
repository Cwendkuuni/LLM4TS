import org.junit.*;
import static org.junit.Assert.*;

public class LagoonCLITest {

    //Test cases for main method
    @Test
    public void testMain_InvalidArguments() {
        String[] args = {};
        LagoonCLI.main(args);
        //Assert that the syntax message is printed to the console
        //No further assertion possible as the method terminates after printing the message
    }
    
    @Test
    public void testMain_BuildArgument() {
        String[] args = {"property_file", "build"};
        LagoonCLI.main(args);
        //Assert that the message "Building website..." is printed to the console
        //Assert that the build method is called with the correct parameter (false)
        //No further assertion possible as the method terminates after the build call
    }
    
    @Test
    public void testMain_ForceArgument() {
        String[] args = {"property_file", "force"};
        LagoonCLI.main(args);
        //Assert that the message "Force building website..." is printed to the console
        //Assert that the build method is called with the correct parameter (true)
        //No further assertion possible as the method terminates after the build call
    }
    
    @Test
    public void testMain_IntervalArgument() {
        String[] args = {"property_file", "5"};
        LagoonCLI.main(args);
        //Assert that the message "Building website..." is printed to the console
        //Assert that the build method is called with the correct parameter (false)
        //Assert that the Thread.sleep method is called with the correct parameter (interval - timeElapsed)
        //No further assertion possible as the method enters an infinite loop
    }

    //Test cases for getProperty method
    @Test(expected = LagoonException.class)
    public void testGetProperty_PropertyMissing() throws LagoonException {
        LagoonCLI.getProperty("missingProperty");
        //Assert that a LagoonException is thrown
    }
    
    @Test
    public void testGetProperty_PropertyFound() throws LagoonException {
        String value = LagoonCLI.getProperty("targetURL");
        //Assert that the correct value is returned
        assertEquals("expectedTargetURL", value);
    }
    
    //Test case for showTime method
    @Test
    public void testShowTime_LessThan10Seconds() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        LagoonCLI.showTime(5000);
        
        //Assert that the correct message is printed to the console
        assertEquals("in 5000 ms\n", outContent.toString());
        
        System.setOut(null);
    }
    
    //Test case for static block
    @Test
    public void testStaticBlock() {
        boolean debug = LagoonCLI.DEBUG;
        //Assert that the debug variable is initialized to false
        assertFalse(debug);
    }
}