import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.apache.commons.cli.Option;

public class OptionBuilderTest {
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("testLongOpt");
        Option option = OptionBuilder.create();
        assertEquals("testLongOpt", option.getLongOpt());
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
    }
    
    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
        
        OptionBuilder.hasArg(false);
        Option option2 = OptionBuilder.create();
        assertEquals(Option.UNINITIALIZED, option2.getArgs());
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("testArgName");
        Option option = OptionBuilder.create();
        assertEquals("testArgName", option.getArgName());
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create();
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testWithValueSeparatorWithChar() {
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create();
        assertEquals(':', option.getValueSeparator());
    }
    
    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        Option option = OptionBuilder.create();
        assertEquals('=', option.getValueSeparator());
    }
    
    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder.isRequired(true);
        Option option = OptionBuilder.create();
        assertTrue(option.isRequired());
        
        OptionBuilder.isRequired(false);
        Option option2 = OptionBuilder.create();
        assertTrue(!option2.isRequired());
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }
    
    @Test
    public void testHasArgsWithInt() {
        OptionBuilder.hasArgs(3);
        Option option = OptionBuilder.create();
        assertEquals(3, option.getArgs());
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
        assertTrue(option.hasOptionalArg());
        assertEquals(1, option.getArgs());
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testHasOptionalArgsWithInt() {
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
        OptionBuilder.withDescription("testDescription");
        Option option = OptionBuilder.create();
        assertEquals("testDescription", option.getDescription());
    }
    
    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
    }
    
    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("testLongOpt");
        Option option = OptionBuilder.create();
        assertEquals("testLongOpt", option.getLongOpt());
        
        OptionBuilder.reset();
        try {
          OptionBuilder.create();
        } catch (IllegalArgumentException e) {
          assertEquals("must specify longopt", e.getMessage());
        }
    }
    
    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("testOpt");
        assertEquals("testOpt", option.getOpt());
    }
}