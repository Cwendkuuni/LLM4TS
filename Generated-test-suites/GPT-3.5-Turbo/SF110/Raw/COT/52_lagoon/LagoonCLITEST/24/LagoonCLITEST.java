
import static org.junit.Assert.*;
import org.junit.Test;

public class LagoonCLITest {
    @Test
    public void testMain() {
        String propertyFile = "test.properties";
        String[] args = new String[]{propertyFile};
        LagoonCLI.main(args);
    }
    
    @Test
    public void testGetPropertyValue() throws LagoonException {
        String name = "targetURL";
        LagoonCLI.getProperty(name);
    }
    
    @Test
    public void testShowTime() {
        long ms = 5000;
        LagoonCLI.showTime(ms);
    }
}
