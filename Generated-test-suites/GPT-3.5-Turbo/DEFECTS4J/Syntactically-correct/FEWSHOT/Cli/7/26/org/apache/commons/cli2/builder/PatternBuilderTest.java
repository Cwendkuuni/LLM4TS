import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.apache.commons.cli2.ArgumentBuilder;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DefaultOptionBuilder;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.GroupBuilder;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.validation.Validator;
import org.apache.commons.cli2.builder.PatternBuilder;

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
        
        PatternBuilder result = patternBuilder.reset();

        assertEquals(patternBuilder, result);
    }

    @Test
    public void testValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        Validator validator1 = patternBuilder.validator('@');
        Validator validator2 = patternBuilder.validator('+');
        Validator validator3 = patternBuilder.validator('%');
        Validator validator4 = patternBuilder.validator('#');
        Validator validator5 = patternBuilder.validator('<');
        Validator validator6 = patternBuilder.validator('>');
        Validator validator7 = patternBuilder.validator('*');
        Validator validator8 = patternBuilder.validator('/');
        Validator validator9 = patternBuilder.validator(':');
        Validator validator10 = patternBuilder.validator(' ');

        assertEquals(ClassValidator.class, validator1.getClass());
        assertEquals(ClassValidator.class, validator2.getClass());
        assertEquals(NumberValidator.class, validator3.getClass());
        assertEquals(DateValidator.class, validator4.getClass());
        assertEquals(FileValidator.class, validator5.getClass());
        assertEquals(FileValidator.class, validator6.getClass());
        assertEquals(FileValidator.class, validator7.getClass());
        assertEquals(UrlValidator.class, validator8.getClass());
        assertNull(validator9);
        assertNull(validator10);
    }

    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        patternBuilder.withPattern("!@/test:*");

        Option option = patternBuilder.create();
        Argument argument = option.getArgument();
        Validator validator = argument.getValidator();

        assertEquals('!', option.getShortName().charAt(0));
        assertEquals(ClassValidator.class, validator.getClass());
        assertEquals(1, argument.getMinimum());
        assertEquals(1, argument.getMaximum());

        patternBuilder.reset();

        patternBuilder.withPattern("@t#");

        option = patternBuilder.create();
        argument = option.getArgument();
        validator = argument.getValidator();

        assertEquals('t', option.getShortName().charAt(0));
        assertEquals(ClassValidator.class, validator.getClass());
        assertEquals(1, argument.getMinimum());
        assertEquals(1, argument.getMaximum());

        patternBuilder.reset();

        patternBuilder.withPattern("#>");

        option = patternBuilder.create();
        argument = option.getArgument();
        validator = argument.getValidator();

        assertEquals('#', option.getShortName().charAt(0));
        assertEquals(DateValidator.class, validator.getClass());
        assertEquals(1, argument.getMinimum());
        assertEquals(1, argument.getMaximum());

        patternBuilder.reset();

        patternBuilder.withPattern("<:p");

        option = patternBuilder.create();
        argument = option.getArgument();
        validator = argument.getValidator();

        assertEquals('<', option.getShortName().charAt(0));
        assertEquals(FileValidator.class, validator.getClass());
        assertEquals(1, argument.getMinimum());
        assertEquals(1, argument.getMaximum());

        patternBuilder.reset();

        patternBuilder.withPattern("/");

        option = patternBuilder.create();
        argument = option.getArgument();
        validator = argument.getValidator();

        assertEquals('/', option.getShortName().charAt(0));
        assertEquals(UrlValidator.class, validator.getClass());
        assertEquals(1, argument.getMinimum());
        assertEquals(1, argument.getMaximum());
    }
}