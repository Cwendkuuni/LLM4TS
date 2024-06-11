import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        // Verify longopt is set correctly
        assertEquals("test", OptionBuilder.getLongopt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        // Verify numberOfArgs is set to 1
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        // Verify numberOfArgs is set to 1
        assertEquals(1, OptionBuilder.getNumberOfArgs());

        OptionBuilder.hasArg(false);
        // Verify numberOfArgs is set to Option.UNINITIALIZED
        assertEquals(Option.UNINITIALIZED, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("test");
        // Verify argName is set correctly
        assertEquals("test", OptionBuilder.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        // Verify required is set to true
        assertTrue(OptionBuilder.isRequired());

        OptionBuilder.isRequired(false);
        // Verify required is set to false
        assertFalse(OptionBuilder.isRequired());
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        // Verify valuesep is set correctly
        assertEquals(':', OptionBuilder.getValuesep());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        // Verify valuesep is set to '='
        assertEquals('=', OptionBuilder.getValuesep());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        // Verify numberOfArgs is set to Option.UNLIMITED_VALUES
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgsWithNum() {
        OptionBuilder.hasArgs(2);
        // Verify numberOfArgs is set to 2
        assertEquals(2, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        // Verify numberOfArgs is set to 1
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        // Verify optionalArg is set to true
        assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        // Verify numberOfArgs is set to Option.UNLIMITED_VALUES
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
        // Verify optionalArg is set to true
        assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgsWithNum() {
        OptionBuilder.hasOptionalArgs(2);
        // Verify numberOfArgs is set to 2
        assertEquals(2, OptionBuilder.getNumberOfArgs());
        // Verify optionalArg is set to true
        assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        // Verify type is set correctly
        assertEquals(String.class, OptionBuilder.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test");
        // Verify description is set correctly
        assertEquals("test", OptionBuilder.getDescription());
    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('t');
        // Verify option is created with correct attributes
        assertEquals("t", option.getOpt());
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertEquals((char) 0, option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("test");
        Option option = OptionBuilder.create();
        // Verify option is created with correct attributes
        assertNull(option.getOpt());
        assertEquals("test", option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertEquals((char) 0, option.getValueSeparator());
        assertEquals("arg", option.getArgName());

        // Verify OptionBuilder properties are reset
        assertNull(OptionBuilder.getLongopt());
        assertNull(OptionBuilder.getDescription());
        assertEquals(Option.UNINITIALIZED, OptionBuilder.getNumberOfArgs());
        assertNull(OptionBuilder.getType());
        assertEquals((char) 0, OptionBuilder.getValuesep());
        assertEquals("arg", OptionBuilder.getArgName());
    }

    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("t");
        // Verify option is created with correct attributes
        assertEquals("t", option.getOpt());
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertEquals((char) 0, option.getValueSeparator());
        assertEquals("arg", option.getArgName());

        // Verify OptionBuilder properties are reset
        assertNull(OptionBuilder.getLongopt());
        assertNull(OptionBuilder.getDescription());
        assertEquals(Option.UNINITIALIZED, OptionBuilder.getNumberOfArgs());
        assertNull(OptionBuilder.getType());
        assertEquals((char) 0, OptionBuilder.getValuesep());
        assertEquals("arg", OptionBuilder.getArgName());
    }

}