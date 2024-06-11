import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class PatternBuilderTest {
    private PatternBuilder patternBuilder;

    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testCreate() {
        Option option = patternBuilder.create();
        assertNull(option);

        Option option1 = new DefaultOptionBuilder().withShortName("a").create();
        patternBuilder.withPattern("a");
        Option option2 = patternBuilder.create();
        assertEquals(option1.getShortName(), option2.getShortName());

        patternBuilder.reset();
        Option option3 = new DefaultOptionBuilder().withShortName("a").withRequired(true).withArgument(new ArgumentBuilder().create()).create();
        patternBuilder.withPattern("!a");
        Option option4 = patternBuilder.create();
        assertEquals(option3.isRequired(), option4.isRequired());

        patternBuilder.reset();
        Option option5 = new GroupBuilder().withOption(new DefaultOptionBuilder().withShortName("a").create()).create();
        patternBuilder.withPattern("{a}");
        Option option6 = patternBuilder.create();
        assertEquals(option5.getOptions().size(), option6.getOptions().size());
    }

    @Test
    public void testReset() {
        patternBuilder.reset();
        Option option = patternBuilder.create();
        assertNull(option);
    }

    @Test
    public void testWithPattern() {
        patternBuilder.withPattern("a");
        Option option = new DefaultOptionBuilder().withShortName("a").create();
        assertEquals(option.getShortName(), patternBuilder.create().getShortName());

        patternBuilder.reset();
        patternBuilder.withPattern("!a");
        Option option1 = new DefaultOptionBuilder().withShortName("a").withRequired(true).withArgument(new ArgumentBuilder().create()).create();
        Option option2 = patternBuilder.create();
        assertEquals(option1.isRequired(), option2.isRequired());

        patternBuilder.reset();
        patternBuilder.withPattern("{a}");
        Option option3 = new GroupBuilder().withOption(new DefaultOptionBuilder().withShortName("a").create()).create();
        Option option4 = patternBuilder.create();
        assertEquals(option3.getOptions().size(), option4.getOptions().size());
    }

    @Test
    public void testValidator() {
        Validator validator = PatternBuilder.validator('@');
        assertNotNull(validator);
        assertTrue(validator instanceof ClassValidator);
        assertTrue(((ClassValidator) validator).isInstance());

        Validator validator1 = PatternBuilder.validator('+');
        assertNotNull(validator1);
        assertTrue(validator1 instanceof ClassValidator);
        assertFalse(((ClassValidator) validator1).isInstance());

        Validator validator2 = PatternBuilder.validator('%');
        assertNotNull(validator2);
        assertTrue(validator2 instanceof NumberValidator);

        Validator validator3 = PatternBuilder.validator('#');
        assertNotNull(validator3);
        assertTrue(validator3 instanceof DateValidator);

        Validator validator4 = PatternBuilder.validator('<');
        assertNotNull(validator4);
        assertTrue(validator4 instanceof FileValidator);
        assertTrue(((FileValidator) validator4).isExisting());
        assertTrue(((FileValidator) validator4).isFile());

        Validator validator5 = PatternBuilder.validator('>');
        assertNotNull(validator5);
        assertTrue(validator5 instanceof FileValidator);

        Validator validator6 = PatternBuilder.validator('*');
        assertNotNull(validator6);
        assertTrue(validator6 instanceof FileValidator);

        Validator validator7 = PatternBuilder.validator('/');
        assertNotNull(validator7);
        assertTrue(validator7 instanceof UrlValidator);

        Validator validator8 = PatternBuilder.validator(':');
        assertNull(validator8);
    }
}