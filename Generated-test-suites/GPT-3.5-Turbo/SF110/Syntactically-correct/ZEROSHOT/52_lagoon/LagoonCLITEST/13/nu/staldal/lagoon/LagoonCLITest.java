package nu.staldal.lagoon;

import org.junit.Test;
import static org.junit.Assert.*;

public class LagoonCLITest {

    @Test
    public void testMain() {
        String[] args = new String[2];
        args[0] = "property_file";
        args[1] = "interval";
        final String expectedErrorMsg = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
        try {
            LagoonCLI.main(args);
        } catch (Exception e){
            assertEquals(expectedErrorMsg, e.getMessage());
        }
    }

    @Test
    public void testGetProperty() {
        String name = "targetURL";
        try {
            LagoonCLI.getProperty(name);
        } catch (LagoonException e){
            String expectedErrorMsg = "Property " + name + " not specified";
            assertEquals(expectedErrorMsg, e.getMessage());
        }
    }

    @Test
    public void testShowTime() {
        long ms = 5000;
        String expectedOutput = "in 5 s";
        assertTrue(expectedOutput.equals(LagoonCLI.showTime(ms)));
    }
}