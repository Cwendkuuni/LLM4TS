import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class OptionBuilderTest {
    
    private OptionBuilder optionBuilder;
    
    @Before
    public void setUp() {
        optionBuilder = new OptionBuilder();
    }
    
    @Test
    public void testWithLongOpt() {
        OptionBuilder result = optionBuilder.withLongOpt("longopt");
        
        assertEquals("longopt", OptionBuilder.getLongopt());
        assertEquals(optionBuilder, result);
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder result = optionBuilder.hasArg();
        
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        assertEquals(optionBuilder, result);
    }
    
    @Test
    public void testHasArgWithFlag() {
        OptionBuilder result = optionBuilder.hasArg(true);
        
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        assertEquals(optionBuilder, result);
    }
    
    @Test
    public void testHasArgWithNoFlag() {
        OptionBuilder result = optionBuilder.hasArg(false);
        
        assertEquals(Option.UNINITIALIZED, OptionBuilder.getNumberOfArgs());
        assertEquals(optionBuilder, result);
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder result = optionBuilder.withArgName("argname");
        
        assertEquals("argname", OptionBuilder.getArgName());
        assertEquals(optionBuilder, result);
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder result = optionBuilder.isRequired();
        
        assertTrue(OptionBuilder.isRequired());
        assertEquals(optionBuilder, result);
    }
    
    @Test
    public void testWithValueSeparator() {
        OptionBuilder result = optionBuilder.withValueSeparator(':');
        
        assertEquals(':', OptionBuilder.getValuesep());
        assertEquals(optionBuilder, result);
    }
    
    @Test
    public void testWithValueSeparatorDefault() {
        OptionBuilder result = optionBuilder.withValueSeparator();
        
        assertEquals('=', OptionBuilder.getValuesep());
        assertEquals(optionBuilder, result);
    }
    
    @Test
    public void testIsRequiredWithValue() {
        OptionBuilder result = optionBuilder.isRequired(true);
        
        assertTrue(OptionBuilder.isRequired());
        assertEquals(optionBuilder, result);
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder result = optionBuilder.hasArgs();
        
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
        assertEquals(optionBuilder, result);
    }
    
    @Test
    public void testHasArgsWithNum() {
        OptionBuilder result = optionBuilder.hasArgs(2);
        
        assertEquals(2, OptionBuilder.getNumberOfArgs());
        assertEquals(optionBuilder, result);
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder result = optionBuilder.hasOptionalArg();
        
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
        assertEquals(optionBuilder, result);
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder result = optionBuilder.hasOptionalArgs();
        
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
        assertEquals(optionBuilder, result);
    }
    
    @Test
    public void testHasOptionalArgsWithNum() {
        OptionBuilder result = optionBuilder.hasOptionalArgs(2);
        
        assertEquals(2, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
        assertEquals(optionBuilder, result);
    }
    
    @Test
    public void testWithType() {
        OptionBuilder result = optionBuilder.withType(String.class);
        
        assertEquals(String.class, OptionBuilder.getType());
        assertEquals(optionBuilder, result);
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder result = optionBuilder.withDescription("description");
        
        assertEquals("description", OptionBuilder.getDescription());
        assertEquals(optionBuilder, result);
    }
    
    @Test
    public void testCreateWithChar() {
        Option option = new Option('o', "option");
        
        Option result = OptionBuilder.create('o');
        
        assertEquals(option, result);
    }
    
    @Test
    public void testCreate() {
        Option option = new Option(null);
        
        Option result = OptionBuilder.create();
        
        assertEquals(option, result);
    }
    
    @Test
    public void testCreateWithString() {
        Option option = new Option("opt");
        
        Option result = OptionBuilder.create("opt");
        
        assertEquals(option, result);
    }
}