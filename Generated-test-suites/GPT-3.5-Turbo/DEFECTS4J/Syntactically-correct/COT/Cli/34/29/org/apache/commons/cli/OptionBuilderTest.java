import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("longOpt");
        Option option = OptionBuilder.create();
        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create('a');
        assertTrue(option.hasArg());
    }

    @Test
    public void testHasArgWithParameter() {
        OptionBuilder.hasArg(true);
        Option option = OptionBuilder.create("opt");
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
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create();
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testWithValueSeparatorDefault() {
        OptionBuilder.withValueSeparator();
        Option option = OptionBuilder.create();
        assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testIsRequiredWithParameter() {
        OptionBuilder.isRequired(true);
        Option option = OptionBuilder.create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasArgsWithParameter() {
        OptionBuilder.hasArgs(2);
        Option option = OptionBuilder.create();
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgsWithParameter() {
        OptionBuilder.hasOptionalArgs(3);
        Option option = OptionBuilder.create();
        assertEquals(3, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        Option option = OptionBuilder.create();
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        Option option = OptionBuilder.create();
        assertEquals("description", option.getDescription());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateWithNullLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithCharOpt() {
        OptionBuilder.withLongOpt("longOpt");
        Option option = OptionBuilder.create('c');
        assertEquals('c', option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testCreateWithStringOpt() {
        OptionBuilder.withLongOpt("longOpt");
        Option option = OptionBuilder.create("opt");
        assertEquals("opt", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
    }

}