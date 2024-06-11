import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt");
        assertNotNull(optionBuilder);
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertNotNull(optionBuilder);
    }
    
    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        assertNotNull(optionBuilder);
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("testArgName");
        assertNotNull(optionBuilder);
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertNotNull(optionBuilder);
    }
    
    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertNotNull(optionBuilder);
    }
    
    @Test
    public void testWithValueSeparatorDefault() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        assertNotNull(optionBuilder);
    }
    
    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
        assertNotNull(optionBuilder);
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertNotNull(optionBuilder);
    }
    
    @Test
    public void testHasArgsWithNum() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        assertNotNull(optionBuilder);
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertNotNull(optionBuilder);
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        assertNotNull(optionBuilder);
    }
    
    @Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
        assertNotNull(optionBuilder);
    }
    
    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Double.class);
        assertNotNull(optionBuilder);
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("testDescription");
        assertNotNull(optionBuilder);
    }
    
    @Test
    public void testCreateWithChar() {
        try {
            Option option = OptionBuilder.create('t');
            assertNotNull(option);
            assertEquals('t', option.getOpt());
        } catch (IllegalArgumentException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testCreate() {
        try {
            Option option = OptionBuilder.create();
            assertNotNull(option);
        } catch (IllegalArgumentException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testCreateWithString() {
        try {
            Option option = OptionBuilder.create("test");
            assertNotNull(option);
            assertEquals("test", option.getOpt());
        } catch (IllegalArgumentException e) {
            fail("Exception should not be thrown");
        }
    }  
}