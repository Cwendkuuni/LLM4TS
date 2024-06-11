import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.cli2.Argument;
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
    public void setUp() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testCreateWithSingleOption() {
        patternBuilder.createOption('t', false, 'u');
        Option option = patternBuilder.create();
        assertTrue(option instanceof Option);
        assertEquals("t", option.getShortName());
        assertFalse(option.isRequired());
    }

    @Test
    public void testCreateWithMultipleOptions() {
        patternBuilder.createOption('s', false, 'u');
        patternBuilder.createOption('i', true, 'v');
        Option option = patternBuilder.create();
        assertTrue(option instanceof Option);
        assertEquals("s", option.getShortName());
        assertFalse(option.isRequired());
        assertTrue(option.getArgument() instanceof Argument);
    }

    @Test
    public void testReset() {
        patternBuilder.createOption('a', false, 'b');
        patternBuilder.reset();
        Option option = patternBuilder.create();
        assertNull(option);
    }

    @Test
    public void testWithPattern() {
        patternBuilder.withPattern("s!i@:u");
        Option option = patternBuilder.create();
        assertTrue(option instanceof Option);
        assertEquals("s", option.getShortName());
        assertTrue(option.isRequired());
        assertTrue(option.getArgument().getValidator() instanceof ClassValidator);
    }

    @Test
    public void testValidator() {
        assertTrue(PatternBuilder.validator('@') instanceof ClassValidator);
        assertTrue(PatternBuilder.validator('#') instanceof DateValidator);
        assertTrue(PatternBuilder.validator('<') instanceof FileValidator);
        assertTrue(PatternBuilder.validator('/') instanceof UrlValidator);
        assertNull(PatternBuilder.validator(':'));
    }

}