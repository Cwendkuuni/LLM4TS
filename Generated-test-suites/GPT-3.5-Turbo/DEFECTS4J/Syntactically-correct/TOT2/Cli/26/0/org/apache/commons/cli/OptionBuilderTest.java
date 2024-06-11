import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("testOption").create();
        assertEquals("testOption", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg().create();
        assertTrue(option.hasArg());
    }

    @Test
    public void testIsRequired() {
        Option option = OptionBuilder.isRequired().create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithArgName() {
        Option option = OptionBuilder.withArgName("testArg").create();
        assertEquals("testArg", option.getArgName());
    }

    @Test
    public void testWithDescription() {
        Option option = OptionBuilder.withDescription("Test Description").create();
        assertEquals("Test Description", option.getDescription());
    }

    @Test
    public void testHasArgs() {
        Option option = OptionBuilder.hasArgs().create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testWithType() {
        Option option = OptionBuilder.withType(Integer.class).create();
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testCreateCharOption() {
        Option option = OptionBuilder.create('c');
        assertEquals("c", option.getOpt());
    }

    @Test
    public void testCreateStringOption() {
        Option option = OptionBuilder.create("testOption");
        assertEquals("testOption", option.getOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }
}