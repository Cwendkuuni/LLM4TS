import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
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
        Option option1 = new OptionBuilder()
            .withShortName("a")
            .withArgument(new ArgumentBuilder()
                .withMinimum(1)
                .withMaximum(1)
                .create())
            .create();

        Option option2 = new OptionBuilder()
            .withShortName("b")
            .withArgument(null)
            .create();

        patternBuilder.withPattern("a!@");
        assertEquals(option1, patternBuilder.create());

        patternBuilder.withPattern("b");
        assertEquals(option2, patternBuilder.create());
    }

    @Test
    public void testReset() {
        patternBuilder.withPattern("a!@");
        patternBuilder.reset();
        assertNull(patternBuilder.create());
    }

    @Test
    public void testWithPattern() {
        Option option1 = new OptionBuilder()
            .withShortName("a")
            .withArgument(new ArgumentBuilder()
                .withMinimum(1)
                .withMaximum(1)
                .withValidator(ClassValidator.getInstance())
                .create())
            .create();

        Option option2 = new OptionBuilder()
            .withShortName("b")
            .create();

        patternBuilder.withPattern("a!@");
        assertEquals(option1, patternBuilder.create());

        patternBuilder.reset();

        patternBuilder.withPattern("b");
        assertEquals(option2, patternBuilder.create());
    }

    @Test
    public void testValidator() {
        Validator validator1 = PatternBuilder.validator('@');
        assertTrue(validator1 instanceof ClassValidator);
        assertTrue(((ClassValidator) validator1).isInstance());

        Validator validator2 = PatternBuilder.validator('+');
        assertTrue(validator2 instanceof ClassValidator);

        Validator validator3 = PatternBuilder.validator('%');
        assertTrue(validator3 instanceof NumberValidator
            && ((NumberValidator) validator3).getValidatorType() ==
            NumberValidator.NUMBER_TYPE);

        Validator validator4 = PatternBuilder.validator('#');
        assertTrue(validator4 instanceof DateValidator);

        Validator validator5 = PatternBuilder.validator('<');
        assertTrue(validator5 instanceof FileValidator
            && ((FileValidator) validator5).isExisting()
            && ((FileValidator) validator5).isFile());

        Validator validator6 = PatternBuilder.validator('>');
        assertTrue(validator6 instanceof FileValidator);

        Validator validator7 = PatternBuilder.validator('*');
        assertTrue(validator7 instanceof FileValidator);

        Validator validator8 = PatternBuilder.validator('/');
        assertTrue(validator8 instanceof UrlValidator);

        Validator validator9 = PatternBuilder.validator(':');
        assertNull(validator9);
    }
}