import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {
    
    private PatternBuilder patternBuilder;
    
    @Before
    public void setup() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testCreate() {
        Option option1 = new OptionBuilder().withShortName("a").create();
        Option option2 = new OptionBuilder().withShortName("b").create();
        patternBuilder.options.add(option1);
        patternBuilder.options.add(option2);
        
        Option result = patternBuilder.create();
        
        assertNull(patternBuilder.options);
        assertFalse(result.isChildGroupRequired());
        assertTrue(result.containsOption('a'));
        assertTrue(result.containsOption('b'));
    }

    @Test
    public void testReset() {
        Option option1 = new OptionBuilder().withShortName("a").create();
        patternBuilder.options.add(option1);
        
        patternBuilder.reset();
        
        assertTrue(patternBuilder.options.isEmpty());
    }

    @Test
    public void testWithPattern() {
        patternBuilder.withPattern("ab");
        
        Option result = patternBuilder.create();
        
        assertTrue(result.containsOption('a'));
        assertTrue(result.containsOption('b'));
    }

    @Test
    public void testValidator() {
        Validator validator1 = patternBuilder.validator('@');
        Validator validator2 = patternBuilder.validator('+');
        Validator validator3 = patternBuilder.validator(':');
        Validator validator4 = patternBuilder.validator('%');
        Validator validator5 = patternBuilder.validator('#');
        Validator validator6 = patternBuilder.validator('<');
        Validator validator7 = patternBuilder.validator('>');
        Validator validator8 = patternBuilder.validator('*');
        Validator validator9 = patternBuilder.validator('/');
        Validator validator10 = patternBuilder.validator('$');
        
        assertTrue(validator1 instanceof ClassValidator);
        assertTrue(validator2 instanceof ClassValidator);
        assertNull(validator3);
        assertTrue(validator4 instanceof NumberValidator);
        assertTrue(validator5 instanceof DateValidator);
        assertTrue(validator6 instanceof FileValidator);
        assertTrue(validator7 instanceof FileValidator);
        assertTrue(validator8 instanceof FileValidator);
        assertTrue(validator9 instanceof UrlValidator);
        assertNull(validator10);
    }
}