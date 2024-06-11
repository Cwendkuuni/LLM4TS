import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testCreate() {
        Option option = patternBuilder.create();

        assertTrue(option instanceof Option);
        assertEquals(0, option.getOptions().size());
        assertEquals(null, option.getArgument());
        assertEquals(null, option.getShortName());
        assertEquals(false, option.isRequired());
    }

    @Test
    public void testReset() {
        patternBuilder.withPattern("!a");
        patternBuilder.withPattern(":b");

        patternBuilder.reset();

        Option option = patternBuilder.create();

        assertTrue(option instanceof Option);
        assertEquals(0, option.getOptions().size());
        assertEquals(null, option.getArgument());
        assertEquals(null, option.getShortName());
        assertEquals(false, option.isRequired());
    }

    @Test
    public void testWithPattern() {
        patternBuilder.withPattern("!a:@b");

        Option option = patternBuilder.create();

        assertTrue(option instanceof Option);
        assertEquals(1, option.getOptions().size());
        assertEquals(1, option.getArgument().getValidators().size());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());
        assertEquals("a", option.getArgument().getValidators().get(0));
        assertEquals(true, option.isRequired());

        Option subOption = option.getOptions().get(0);

        assertTrue(subOption instanceof Option);
        assertEquals(null, subOption.getOptions());
        assertEquals(null, subOption.getArgument());
        assertEquals("b", subOption.getShortName());
        assertEquals(false, subOption.isRequired());
    }

    @Test
    public void testValidator() {
        Validator validator = patternBuilder.validator('@');

        assertTrue(validator instanceof ClassValidator);
        assertEquals(true, ((ClassValidator)validator).isInstance());

        validator = patternBuilder.validator('+');

        assertTrue(validator instanceof ClassValidator);
        assertEquals(false, ((ClassValidator)validator).isInstance());

        validator = patternBuilder.validator('%');

        assertTrue(validator instanceof NumberValidator);

        validator = patternBuilder.validator('#');

        assertTrue(validator instanceof DateValidator);

        validator = patternBuilder.validator('<');

        assertTrue(validator instanceof FileValidator);
        assertEquals(true, ((FileValidator)validator).isExisting());
        assertEquals(true, ((FileValidator)validator).isFile());

        validator = patternBuilder.validator('>');

        assertTrue(validator instanceof FileValidator);

        validator = patternBuilder.validator('*');

        assertTrue(validator instanceof FileValidator);

        validator = patternBuilder.validator('/');

        assertTrue(validator instanceof UrlValidator);

        validator = patternBuilder.validator(':');

        assertEquals(null, validator);
    }

}