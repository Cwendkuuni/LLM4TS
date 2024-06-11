import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.Option;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("test").create();
        assertNotNull(option);
        assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg().create();
        assertNotNull(option);
        assertTrue(option.hasArg());
    }

    @Test
    public void testHasArgWithFalse() {
        Option option = OptionBuilder.hasArg(false).create();
        assertNotNull(option);
        assertFalse(option.hasArg());
    }

    @Test
    public void testWithArgName() {
        Option option = OptionBuilder.withArgName("name").create();
        assertNotNull(option);
        assertEquals("name", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        Option option = OptionBuilder.isRequired().create();
        assertNotNull(option);
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg().create();
        assertNotNull(option);
        assertTrue(option.hasOptionalArg());
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testWithOptionalArgs() {
        Option option = OptionBuilder.hasOptionalArgs().create();
        assertNotNull(option);
        assertTrue(option.hasOptionalArg());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testWithOptionalArgsNum() {
        Option option = OptionBuilder.hasOptionalArgs(2).create();
        assertNotNull(option);
        assertTrue(option.hasOptionalArg());
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testWithType() {
        Option option = OptionBuilder.withType(Integer.class).create();
        assertNotNull(option);
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        Option option = OptionBuilder.withDescription("description").create();
        assertNotNull(option);
        assertEquals("description", option.getDescription());
    }
}