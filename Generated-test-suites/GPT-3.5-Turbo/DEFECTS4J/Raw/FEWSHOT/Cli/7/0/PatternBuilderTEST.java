import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testCreate() {
        Option option1 = new Option.Builder()
                .withShortName("a")
                .withDescription("Option a")
                .withRequired(true)
                .create();

        Option option2 = new Option.Builder()
                .withShortName("b")
                .withDescription("Option b")
                .withRequired(false)
                .create();

        patternBuilder.options.add(option1);
        patternBuilder.options.add(option2);

        Option option = patternBuilder.create();

        assertEquals(option1, option);
    }

    @Test
    public void testReset() {
        Option option = new Option.Builder()
                .withShortName("a")
                .withDescription("Option a")
                .withRequired(true)
                .create();

        patternBuilder.options.add(option);
        patternBuilder.reset();

        assertEquals(0, patternBuilder.options.size());
    }

    @Test
    public void testCreateOption() {
        Argument argument = new ArgumentBuilder().create();
        Option option = new Option.Builder()
                .withShortName("a")
                .withDescription("Option a")
                .withArgument(argument)
                .withRequired(true)
                .create();

        patternBuilder.options.add(option);
        patternBuilder.createOption('@', true, 'a');
        assertEquals(option, patternBuilder.options.iterator().next());
    }

    @Test
    public void testWithPattern() {
        Option option1 = new Option.Builder()
                .withShortName("a")
                .withDescription("Option a")
                .withRequired(true)
                .create();

        Option option2 = new Option.Builder()
                .withShortName("b")
                .withDescription("Option b")
                .withRequired(false)
                .create();

        patternBuilder.withPattern("a");
        assertEquals(option1, patternBuilder.options.iterator().next());

        patternBuilder.withPattern("b");
        assertEquals(option2, patternBuilder.options.iterator().next());

    }

    @Test
    public void testValidator() {
        Validator validator = patternBuilder.validator('@');
        assertEquals(ClassValidator.class, validator.getClass());

        validator = patternBuilder.validator('+');
        assertEquals(ClassValidator.class, validator.getClass());

        validator = patternBuilder.validator('%');
        assertEquals(NumberValidator.class, validator.getClass());

        validator = patternBuilder.validator('#');
        assertEquals(DateValidator.class, validator.getClass());

        validator = patternBuilder.validator('<');
        assertEquals(FileValidator.class, validator.getClass());

        validator = patternBuilder.validator('>');
        assertEquals(FileValidator.class, validator.getClass());

        validator = patternBuilder.validator('/');
        assertEquals(UrlValidator.class, validator.getClass());

        validator = patternBuilder.validator(':');
        assertNull(validator);
    }
}