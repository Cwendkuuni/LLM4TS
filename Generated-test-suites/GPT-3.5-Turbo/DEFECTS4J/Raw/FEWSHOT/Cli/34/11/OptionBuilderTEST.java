import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionBuilderTest {
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("longopt");
        // Verify the value of longopt
        assertEquals("longopt", OptionBuilder.getLongopt());
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        // Verify the value of numberOfArgs
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        // Verify the value of numberOfArgs
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        
        OptionBuilder.hasArg(false);
        // Verify the value of numberOfArgs
        assertEquals(-1, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("argName");
        // Verify the value of argName
        assertEquals("argName", OptionBuilder.getArgName());
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        // Verify the value of required
        assertEquals(true, OptionBuilder.isRequired());
    }
    
    @Test
    public void testWithValueseparator() {
        OptionBuilder.withValueSeparator(':');
        // Verify the value of valuesep
        assertEquals(':', OptionBuilder.getValuesep());
    }
    
    @Test
    public void testWithValueseparatorDefault() {
        OptionBuilder.withValueSeparator();
        // Verify the value of valuesep
        assertEquals('=', OptionBuilder.getValuesep());
    }
    
    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder.isRequired(true);
        // Verify the value of required
        assertEquals(true, OptionBuilder.isRequired());
        
        OptionBuilder.isRequired(false);
        // Verify the value of required
        assertEquals(false, OptionBuilder.isRequired());
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        // Verify the value of numberOfArgs
        assertEquals(-2, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasArgsWithNum() {
        OptionBuilder.hasArgs(2);
        // Verify the value of numberOfArgs
        assertEquals(2, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        // Verify the value of numberOfArgs
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        // Verify the value of optionalArg
        assertEquals(true, OptionBuilder.isOptionalArg());
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        // Verify the value of numberOfArgs
        assertEquals(-2, OptionBuilder.getNumberOfArgs());
        // Verify the value of optionalArg
        assertEquals(true, OptionBuilder.isOptionalArg());
    }
    
    @Test
    public void testHasOptionalArgsWithNum() {
        OptionBuilder.hasOptionalArgs(3);
        // Verify the value of numberOfArgs
        assertEquals(3, OptionBuilder.getNumberOfArgs());
        // Verify the value of optionalArg
        assertEquals(true, OptionBuilder.isOptionalArg());
    }
    
    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        // Verify the value of type
        assertEquals(Integer.class, OptionBuilder.getType());
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        // Verify the value of description
        assertEquals("description", OptionBuilder.getDescription());
    }
    
    @Test
    public void testCreateWithChar() throws IllegalArgumentException {
        Option option = OptionBuilder.create('o');
        // Verify the value of option
        assertEquals('o', option.getOpt());
    }
    
    @Test
    public void testCreate() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("longopt");
        Option option = OptionBuilder.create();
        // Verify the value of longopt
        assertEquals("longopt", option.getLongOpt());
    }
    
    @Test
    public void testCreateWithString() throws IllegalArgumentException {
        Option option = OptionBuilder.create("opt");
        // Verify the value of opt
        assertEquals("opt", option.getOpt());
    }
}