import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class OptionBuilderTest {
  
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        // Verify longopt is correctly set
        assertEquals("test", OptionBuilder.create().getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        // Verify numberOfArgs is correctly set
        assertEquals(1, OptionBuilder.create().getArgs());
    }
  
    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        // Verify numberOfArgs is correctly set
        assertEquals(1, OptionBuilder.create().getArgs());
      
        OptionBuilder.hasArg(false);
        // Verify numberOfArgs is correctly set
        assertEquals(Option.UNINITIALIZED, OptionBuilder.create().getArgs());
    }
  
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("testArg");
        // Verify argName is correctly set
        assertEquals("testArg", OptionBuilder.create().getArgName());
    }
  
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        // Verify required is correctly set
        assertEquals(true, OptionBuilder.create().isRequired());
    }
  
    @Test
    public void testWithSeparator() {
        OptionBuilder.withValueSeparator(':');
        // Verify valueSeparator is correctly set
        assertEquals(':', OptionBuilder.create().getValueSeparator());
      
        OptionBuilder.withValueSeparator();
        // Verify valueSeparator is correctly set to '=' by default
        assertEquals('=', OptionBuilder.create().getValueSeparator());
    }
  
    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder.isRequired(true);
        // Verify required is correctly set
        assertEquals(true, OptionBuilder.create().isRequired());
      
        OptionBuilder.isRequired(false);
        // Verify required is correctly set
        assertEquals(false, OptionBuilder.create().isRequired());
    }
  
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        // Verify numberOfArgs is correctly set to UNLIMITED_VALUES
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
    }
  
    @Test
    public void testHasArgsWithNum() {
        OptionBuilder.hasArgs(5);
        // Verify numberOfArgs is correctly set
        assertEquals(5, OptionBuilder.create().getArgs());
    }
  
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        // Verify numberOfArgs is correctly set to 1
        assertEquals(1, OptionBuilder.create().getArgs());
        // Verify optionalArg is correctly set to true
        assertEquals(true, OptionBuilder.create().hasOptionalArg());
    }
  
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        // Verify numberOfArgs is correctly set to UNLIMITED_VALUES
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
        // Verify optionalArg is correctly set to true
        assertEquals(true, OptionBuilder.create().hasOptionalArg());
    }
  
    @Test
    public void testHasOptionalArgsWithNum() {
        OptionBuilder.hasOptionalArgs(5);
        // Verify numberOfArgs is correctly set
        assertEquals(5, OptionBuilder.create().getArgs());
        // Verify optionalArg is correctly set to true
        assertEquals(true, OptionBuilder.create().hasOptionalArg());
    }
  
    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        // Verify type is correctly set
        assertEquals(Integer.class, OptionBuilder.create().getType());
    }
  
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test description");
        // Verify description is correctly set
        assertEquals("test description", OptionBuilder.create().getDescription());
    }
  
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }
  
    @Test
    public void testCreateWithCharOpt() {
        Option option = OptionBuilder.create('t');
        // Verify option is created with the correct short opt
        assertEquals('t', option.getOpt());
    }
  
    @Test
    public void testCreateWithStringOpt() {
        Option option = OptionBuilder.create("testOpt");
        // Verify option is created with the correct long opt
        assertEquals("testOpt", option.getLongOpt());
    }
}