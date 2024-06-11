
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probe = new ProbeInformation("target", "type", "class", "name", "description", "probe", 10, "url");
        assertEquals("type", probe.getTargetType());
    }

    // Add more test methods here for other methods in the ProbeInformation class

}

// Add additional test methods for Edge cases and Exception handling scenarios