import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Before
    public void setUp() {
        // Set up code
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("testOption");
        Option option = OptionBuilder.create();
        assertEquals("testOption", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("argName");
        Option option = OptionBuilder.create();
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("testDescription");
        Option option = OptionBuilder.create();
        assertEquals("testDescription", option.getDescription());
    }

    @Test
    public void testWithOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create();
        assertTrue(option.hasOptionalArg());
    }

    // Continue with more test cases for other methods...

}