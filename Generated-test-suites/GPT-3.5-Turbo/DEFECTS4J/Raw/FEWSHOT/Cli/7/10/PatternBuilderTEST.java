import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {
    private PatternBuilder patternBuilder;
    
    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder();
    }
    
    @Test
    public void testCreateWithSingleOption() {
        patternBuilder.withPattern("a");
        Option option = patternBuilder.create();
        
        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        assertNull(option.getArgument());
    }
    
    @Test
    public void testCreateWithMultipleOptions() {
        patternBuilder.withPattern("a b");
        Option option = patternBuilder.create();
        
        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        assertNull(option.getArgument());

        option = option.getNextOption();
        
        assertEquals("b", option.getShortName());
        assertFalse(option.isRequired());
        assertNull(option.getArgument());
    }
    
    @Test
    public void testCreateWithRequiredOption() {
        patternBuilder.withPattern("!a");
        Option option = patternBuilder.create();
        
        assertEquals("a", option.getShortName());
        assertTrue(option.isRequired());
        assertNull(option.getArgument());
    }
    
    @Test
    public void testCreateWithArgument() {
        patternBuilder.withPattern("a:");
        Option option = patternBuilder.create();
        
        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(option.getArgument());
    }
    
    @Test
    public void testCreateWithRequiredArgument() {
        patternBuilder.withPattern("!a:");
        Option option = patternBuilder.create();
        
        assertEquals("a", option.getShortName());
        assertTrue(option.isRequired());
        assertNotNull(option.getArgument());
    }
    
    @Test
    public void testCreateWithMultipleArguments() {
        patternBuilder.withPattern("a:b");
        Option option = patternBuilder.create();
        
        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(option.getArgument());
        
        Argument argument = option.getArgument();
        
        assertEquals("b", argument.getName());
        assertFalse(argument.isRequired());
        assertEquals(1, argument.getMinimum());
        assertEquals(1, argument.getMaximum());
    }
    
    @Test
    public void testCreateWithOptionalArgument() {
        patternBuilder.withPattern("a:?b");
        Option option = patternBuilder.create();
        
        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(option.getArgument());
        
        Argument argument = option.getArgument();
        
        assertEquals("b", argument.getName());
        assertFalse(argument.isRequired());
        assertEquals(0, argument.getMinimum());
        assertEquals(1, argument.getMaximum());
    }
    
    @Test
    public void testCreateWithDifferentValidators() {
        patternBuilder.withPattern("a@");
        Option option = patternBuilder.create();
        
        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(option.getArgument());
        assertTrue(option.getArgument().getValidator() instanceof ClassValidator);
        
        patternBuilder.reset();
        
        patternBuilder.withPattern("a+");
        option = patternBuilder.create();
        
        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(option.getArgument());
        assertTrue(option.getArgument().getValidator() instanceof ClassValidator);
        
        patternBuilder.reset();
        
        patternBuilder.withPattern("a%");
        option = patternBuilder.create();
        
        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(option.getArgument());
        assertTrue(option.getArgument().getValidator() instanceof NumberValidator);
        
        patternBuilder.reset();
        
        patternBuilder.withPattern("a#");
        option = patternBuilder.create();
        
        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(option.getArgument());
        assertTrue(option.getArgument().getValidator() instanceof DateValidator);
        
        patternBuilder.reset();
        
        patternBuilder.withPattern("a<");
        option = patternBuilder.create();
        
        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(option.getArgument());
        assertTrue(option.getArgument().getValidator() instanceof FileValidator);
        assertTrue(option.getArgument().isExisting());
        assertTrue(option.getArgument().isFile());
        
        patternBuilder.reset();
        
        patternBuilder.withPattern("a>");
        option = patternBuilder.create();
        
        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(option.getArgument());
        assertTrue(option.getArgument().getValidator() instanceof FileValidator);
        assertFalse(option.getArgument().isExisting());
        assertFalse(option.getArgument().isFile());
        
        patternBuilder.reset();
        
        patternBuilder.withPattern("a/");
        option = patternBuilder.create();
        
        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(option.getArgument());
        assertTrue(option.getArgument().getValidator() instanceof UrlValidator);
    }
    
    @Test
    public void testReset() {
        patternBuilder.withPattern("a");
        Option option = patternBuilder.create();
        
        assertNotNull(option);
        
        patternBuilder.reset();
        
        option = patternBuilder.create();
        
        assertNull(option);
    }
}