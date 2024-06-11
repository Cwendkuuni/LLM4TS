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
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option = patternBuilder.create();
        assertNull(option);
    }
    
    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.reset();
        Option option = patternBuilder.create();
        assertNull(option);
    }
    
    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("!@:ik");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(2, option.getShortName().length()); 
        assertEquals("ik", option.getShortName());
        
        patternBuilder.reset();
        patternBuilder.withPattern("@!%<>");
        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(3, option.getShortName().length()); 
        assertEquals("<>", option.getShortName());
    }
    
    @Test
    public void testValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        ClassValidator classValidator = (ClassValidator)patternBuilder.validator('@');
        assertTrue(classValidator.isInstance());
        assertFalse(classValidator.isNullAllowed());
        
        NumberValidator numberValidator = (NumberValidator)patternBuilder.validator('%');
        assertFalse(numberValidator.isStrict());
        
        DateValidator dateValidator = (DateValidator)patternBuilder.validator('#');
        assertFalse(dateValidator.isStrict());
        
        FileValidator fileValidator = (FileValidator)patternBuilder.validator('<');
        assertTrue(fileValidator.isExisting());
        assertTrue(fileValidator.isFile());
        assertFalse(fileValidator.isReadAllowed());
        
        UrlValidator urlValidator = (UrlValidator)patternBuilder.validator('/');
        assertFalse(urlValidator.isProtocolRequired());
    }
    
}