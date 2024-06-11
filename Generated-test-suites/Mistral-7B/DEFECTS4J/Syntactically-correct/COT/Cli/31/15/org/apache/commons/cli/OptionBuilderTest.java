import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    

    @Test
    public void testWithLongOpt() {
        OptionBuilder builder = OptionBuilder.withLongOpt("optionLongName");
        assertEquals("optionLongName", builder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder builder = OptionBuilder.hasArg();
        assertEquals(1, builder.numberOfArgs);
    }

    @Test
    public void testHasArgWithFalse() {
        OptionBuilder builder = OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, builder.numberOfArgs);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder builder = OptionBuilder.withArgName("argName");
        assertEquals("argName", builder.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder builder = OptionBuilder.isRequired();
        assertTrue(builder.required);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder builder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', builder.valuesep);
    }

    @Test
    public void testWithValueSeparatorEmpty() {
        OptionBuilder builder = OptionBuilder.withValueSeparator();
        assertEquals('=', builder.valuesep);
    }

    @Test
    public void testIsRequiredWithFalse() {
        OptionBuilder builder = OptionBuilder.isRequired(false);
        assertFalse(builder.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder builder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, builder.numberOfArgs);
    }

    @Test
    public void testHasArgsWithNumber() {
        OptionBuilder builder = OptionBuilder.hasArgs(3);
        assertEquals(3, builder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder builder = OptionBuilder.hasOptionalArg();
        assertTrue(builder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder builder = OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, builder.numberOfArgs);
        assertTrue(builder.optionalArg);
    }

    @Test
    public void testHasOptionalArgsWithNumber() {
        OptionBuilder builder = OptionBuilder.hasOptionalArgs(5);
        assertEquals(5, builder.numberOfArgs);
        assertTrue(builder.optionalArg);
    }

    @Test
    public void testWithType() {
        OptionBuilder builder = OptionBuilder.withType("String");
        assertNotNull(builder.type);
        assertEquals("java.lang.String", builder.type.getClass().getName());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder builder = OptionBuilder.withDescription("Test Option Description");
        assertNotNull(builder.description);
        assertEquals("Test Option Description", builder.description);
    }

    @Test
    public void testCreateWithLongOpt() throws IllegalArgumentException {
        OptionBuilder builder = OptionBuilder.withLongOpt("optionLongName")
                .withDescription("Test Option Description")
                .hasArg()
                .withValueSeparator(':');

        Option option = builder.create('T');
        assertEquals("optionLongName", option.getLongOpt());
        assertEquals("Test Option Description", option.getDescription());
        assertEquals(Option.VALUE_REQUIRED, option.getArgType());
        assertEquals(1, option.getArgs().length);
        assertEquals(':', option.getValueSeparator());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertNull(option.getType());
    }

    @Test
    public void testCreateWithShortOpt() throws IllegalArgumentException {
        OptionBuilder builder = OptionBuilder.withLongOpt("optionLongName")
                .withDescription("Test Option Description")
                .isRequired()
                .hasArgs()
                .withValueSeparator(' ')
                .withType(String[].class);

        Option option = builder.create('t');
        assertEquals("optionLongName", option.getLongOpt());
        assertEquals("Test Option Description", option.getDescription());
        assertEquals(Option.VALUE_REQUIRED, option.getArgType());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        assertEquals(' ', option.getValueSeparator());
        assertNotNull(option.getType());
        assertEquals(String[].class, option.getType().getClass());
    }

    
}