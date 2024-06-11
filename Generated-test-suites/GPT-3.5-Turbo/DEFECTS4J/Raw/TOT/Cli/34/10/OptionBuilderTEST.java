import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Before
    public void setup() {
        // Set up any necessary data before each test
    }

    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("test").create(); // Test with Long Option
        assertEquals("test", option.getLongOpt());
    }
    
    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg().create(); // Test with Option having an argument value
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
    }
    
    @Test
    public void testHasArgWithValue() {
        Option option = OptionBuilder.hasArg(false).create(); // Test with Option without an argument value
        assertFalse(option.hasArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
    }
    
    @Test
    public void testWithArgName() {
        Option option = OptionBuilder.withArgName("name").create(); // Test with Option having an argument name
        assertEquals("name", option.getArgName());
    }
    
    @Test
    public void testIsRequired() {
        Option option = OptionBuilder.isRequired().create(); // Test with Required Option
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testWithSeparator() {
        Option option = OptionBuilder.withValueSeparator(':').create(); // Test with Option having a value separator
        assertEquals(':', option.getValueSeparator());
    }
    
    @Test
    public void testWithDefaultSeparator() {
        Option option = OptionBuilder.withValueSeparator().create(); // Test with Option having default value separator
        assertEquals('=', option.getValueSeparator());
    }
    
    @Test
    public void testWithRequired() {
        Option option = OptionBuilder.isRequired(false).create(); // Test with Optional Option
        assertFalse(option.isRequired());
    }
    
    @Test
    public void testHasArgs() {
        Option option = OptionBuilder.hasArgs().create(); // Test with Option having unlimited argument values
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }
    
    @Test
    public void testHasArgsWithNum() {
        Option option = OptionBuilder.hasArgs(3).create(); // Test with Option having specified number of argument values
        assertEquals(3, option.getArgs());
    }
    
    @Test
    public void testHasOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg().create(); // Test with Option having optional argument value
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testHasOptionalArgs() {
        Option option = OptionBuilder.hasOptionalArgs().create(); // Test with Option having unlimited optional argument values
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testHasOptionalArgsWithNum() {
        Option option = OptionBuilder.hasOptionalArgs(2).create(); // Test with Option having specified number of optional argument values
        assertEquals(2, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testWithType() {
        Option option = OptionBuilder.withType(Integer.class).create(); // Test with Option having a specified type for argument value
        assertEquals(Integer.class, option.getType());
    }
    
    @Test
    public void testWithDescription() {
        Option option = OptionBuilder.withDescription("description").create(); // Test with Option having a description
        assertEquals("description", option.getDescription());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create(); // Test creating an Option without a long option
    }
    
    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t'); // Test creating an Option with a character option
        assertEquals("t", option.getOpt());
    }
    
    @Test
    public void testCreateWithOpt() {
        Option option = OptionBuilder.create("opt"); // Test creating an Option with a string option
        assertEquals("opt", option.getOpt());
    }

}