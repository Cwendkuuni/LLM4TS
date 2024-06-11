import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("test").create();
        assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg().create();
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testIsRequired() {
        Option option = OptionBuilder.isRequired().create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithArgName() {
        Option option = OptionBuilder.withArgName("name").create();
        assertEquals("name", option.getArgName());
    }

    @Test
    public void testWithOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg().create();
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testWithValuesSeparator() {
        Option option = OptionBuilder.withValueSeparator(';').create();
        assertEquals(';', option.getValueSeparator());
    }

    @Test
    public void testCreate() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testHasArgs() {
        Option option = OptionBuilder.hasArgs().create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testWithDescription() {
        Option option = OptionBuilder.withDescription("test description").create();
        assertEquals("test description", option.getDescription());
    }

    @Test
    public void testWithCustomType() {
        Option option = OptionBuilder.withType(Integer.class).create();
        assertEquals(Integer.class, option.getType());
    }
}