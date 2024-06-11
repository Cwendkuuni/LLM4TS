import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt_Success() {
        OptionBuilder.withLongOpt("test");
        Option option = OptionBuilder.create();
        assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testHasArg_Success() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
    }

    @Test
    public void testWithArgName_Success() {
        OptionBuilder.withArgName("test");
        Option option = OptionBuilder.create();
        assertEquals("test", option.getArgName());
    }

    @Test
    public void testIsRequired_Success() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithDescription_Success() {
        OptionBuilder.withDescription("test");
        Option option = OptionBuilder.create();
        assertEquals("test", option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_NoLongOpt_ExceptionThrown() {
        OptionBuilder.create();
    }

    @Test
    public void testCreate_Success() {
        OptionBuilder.withLongOpt("test")
                     .hasArg()
                     .withArgName("test")
                     .isRequired()
                     .withDescription("test");
        Option option = OptionBuilder.create();
        assertEquals("test", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("test", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals("test", option.getDescription());
    }

}