import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.validation.Validator;

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
        patternBuilder.withPattern("!@:<>*/");
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Validator validator1 = patternBuilder.validator('@');
        assertTrue(validator1 instanceof ClassValidator);
        Validator validator2 = patternBuilder.validator('+');
        assertTrue(validator2 instanceof ClassValidator);
        Validator validator3 = patternBuilder.validator('%');
        assertTrue(validator3 instanceof NumberValidator);
        Validator validator4 = patternBuilder.validator('#');
        assertTrue(validator4 instanceof DateValidator);
        Validator validator5 = patternBuilder.validator('<');
        assertTrue(validator5 instanceof FileValidator);
        Validator validator6 = patternBuilder.validator('>');
        assertTrue(validator6 instanceof FileValidator);
        Validator validator7 = patternBuilder.validator('*');
        assertTrue(validator7 instanceof FileValidator);
        Validator validator8 = patternBuilder.validator('/');
        assertTrue(validator8 instanceof UrlValidator);
        Validator validator9 = patternBuilder.validator(':');
        assertNull(validator9);
    }

}