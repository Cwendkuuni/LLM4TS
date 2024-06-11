import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong");
        assertEquals("optionLong", optionBuilder.getLongOpt());
    }

    @Test(expected = IllegalStateException.class)
    public void testWithLongOptWithoutReset() {
        OptionBuilder.withLongOpt("optionLong");
        OptionBuilder.withLongOpt("optionLong2");
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertEquals(1, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        assertEquals("argName", optionBuilder.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertTrue(optionBuilder.isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', optionBuilder.getValueSeparator());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgsWithNum() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(5);
        assertEquals(5, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertEquals(1, optionBuilder.getNumberOfArgs());
        assertTrue(optionBuilder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.getNumberOfArgs());
        assertTrue(optionBuilder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(5);
        assertEquals(5, optionBuilder.getNumberOfArgs());
        assertTrue(optionBuilder.isOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        assertEquals(Integer.class, optionBuilder.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("option description");
        assertEquals("option description", optionBuilder.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.reset();
        OptionBuilder.create('a');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutDescription() {
        OptionBuilder.reset();
        OptionBuilder.withLongOpt("longopt");
        OptionBuilder.create('b');
    }

    @Test
    public void testCreateWithValidOptions() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt");
        OptionBuilder.isRequired();
        OptionBuilder.hasArg();
        OptionBuilder.withValueSeparator(':');
        OptionBuilder.hasOptionalArgs();
        OptionBuilder.withType(Integer.class);
        OptionBuilder.withDescription("option description");

        Option option = OptionBuilder.create('c');

        assertEquals("longopt", option.getLongOpt());
        assertTrue(option.isRequired());
        assertEquals(1, option.getNumberOfArgs());
        assertEquals(':', option.getValueSeparator());
        assertTrue(option.isOptionalArg());
        assertEquals(Option.UNLIMITED_VALUES, option.getNumberOfArgs());
        assertEquals(Integer.class, option.getType());
        assertEquals("option description", option.getDescription());
    }

    
}