import org.junit.Test;

public class LagoonCLITest {

    @Test
    public void testMain() {
        String[] args = new String[2];
        args[0] = "property_file";
        args[1] = "interval";
        
        LagoonCLI.main(args);
        
        // Add assertions here to verify the expected behavior of the method
    }

    @Test
    public void testGetProperty() throws LagoonException {
        String name = "property_name";
        
        String result = LagoonCLI.getProperty(name);
        
        // Add assertions here to verify the expected behavior of the method
    }

    @Test
    public void testShowTime() {
        long ms = 1000;
        
        LagoonCLI.showTime(ms);
        
        // Add assertions here to verify the expected behavior of the method
    }
}