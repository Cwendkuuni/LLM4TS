import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;

public class PatternBuilderTest {

    @Test
    public void testCreate_singleOption() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a");
        Option option = builder.create();
        
        assertEquals("a", option.getShortName());
        assertNull(option.getArgument());
        assertFalse(option.isRequired());
    }
    
    @Test
    public void testCreate_optionWithArgument() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:");
        Option option = builder.create();
        
        assertEquals("a", option.getShortName());
        assertNotNull(option.getArgument());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());
        assertNull(option.getArgument().getValidator());
    }
    
    @Test
    public void testCreate_optionWithRequiredArgument() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a!");
        Option option = builder.create();
        
        assertEquals("a", option.getShortName());
        assertNotNull(option.getArgument());
        assertTrue(option.isRequired());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());
        assertNull(option.getArgument().getValidator());
    }
    
    @Test
    public void testCreate_optionWithMultipleArguments() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a*");
        Option option = builder.create();
        
        assertEquals("a", option.getShortName());
        assertNotNull(option.getArgument());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(Integer.MAX_VALUE, option.getArgument().getMaximum());
        assertNull(option.getArgument().getValidator());
    }
    
    @Test
    public void testCreate_optionWithSpecificArgumentType() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a@");
        Option option = builder.create();
        
        assertEquals("a", option.getShortName());
        assertNotNull(option.getArgument());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());
        assertTrue(option.getArgument().getValidator() instanceof ClassValidator);
        assertTrue(((ClassValidator) option.getArgument().getValidator()).isInstance());
    }
    
    @Test
    public void testCreate_optionWithNumberArgument() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a%");
        Option option = builder.create();
        
        assertEquals("a", option.getShortName());
        assertNotNull(option.getArgument());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());
        assertTrue(option.getArgument().getValidator() instanceof NumberValidator);
    }
    
    @Test
    public void testCreate_optionWithDateArgument() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a#");
        Option option = builder.create();
        
        assertEquals("a", option.getShortName());
        assertNotNull(option.getArgument());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());
        assertTrue(option.getArgument().getValidator() instanceof DateValidator);
    }
    
    @Test
    public void testCreate_optionWithExistingFileArgument() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a<");
        Option option = builder.create();
        
        assertEquals("a", option.getShortName());
        assertNotNull(option.getArgument());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());
        assertTrue(option.getArgument().getValidator() instanceof FileValidator);
        assertTrue(((FileValidator) option.getArgument().getValidator()).isExisting());
        assertTrue(((FileValidator) option.getArgument().getValidator()).isFile());
    }
    
    @Test
    public void testCreate_optionWithFileArgument() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a>");
        Option option = builder.create();
        
        assertEquals("a", option.getShortName());
        assertNotNull(option.getArgument());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());
        assertTrue(option.getArgument().getValidator() instanceof FileValidator);
    }
    
    @Test
    public void testCreate_optionWithUrlArgument() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a/");
        Option option = builder.create();
        
        assertEquals("a", option.getShortName());
        assertNotNull(option.getArgument());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());
        assertTrue(option.getArgument().getValidator() instanceof UrlValidator);
    }
    
    @Test
    public void testCreate_multipleOptions() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("abcdef");
        Option option = builder.create();
        
        assertEquals("a", option.getShortName());
        assertNull(option.getArgument());
        assertFalse(option.isRequired());
        
        builder.reset();
        builder.withPattern("a:bcde");
        option = builder.create();
        
        assertEquals("a", option.getShortName());
        assertNotNull(option.getArgument());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());
        assertNull(option.getArgument().getValidator());
        
        builder.reset();
        builder.withPattern("a!:bcde");
        option = builder.create();
        
        assertEquals("a", option.getShortName());
        assertNotNull(option.getArgument());
        assertTrue(option.isRequired());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());
        assertNull(option.getArgument().getValidator());
        
        builder.reset();
        builder.withPattern("a*bcde");
        option = builder.create();
        
        assertEquals("a", option.getShortName());
        assertNotNull(option.getArgument());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(Integer.MAX_VALUE, option.getArgument().getMaximum());
        assertNull(option.getArgument().getValidator());
    }

    @Test
    public void testReset() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a");
        Option option = builder.create();
        builder.reset();
        
        assertEquals(0, builder.options.size());
    }
}