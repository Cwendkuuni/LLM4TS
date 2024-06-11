import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.ArgumentBuilder;
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
        assertNull(option);
    }

    @Test
    public void testReset() {
        patternBuilder.reset();
        Option option = patternBuilder.create();
        assertNull(option);
    }

    @Test
    public void testWithPattern() {
        patternBuilder.withPattern("-a");

        Option option = patternBuilder.create();
        assertEquals("-a", option.getShortName());
        assertNull(option.getArgument());

        patternBuilder.reset();

        patternBuilder.withPattern("-f");

        option = patternBuilder.create();
        assertEquals("-f", option.getShortName());
        assertNull(option.getArgument());
    }

    @Test
    public void testValidator() {
        char c = '@';
        assertTrue(patternBuilder.validator(c) instanceof ClassValidator);

        c = '+';
        assertTrue(patternBuilder.validator(c) instanceof ClassValidator);

        c = '%';
        assertTrue(patternBuilder.validator(c) instanceof NumberValidator);

        c = '#';
        assertTrue(patternBuilder.validator(c) instanceof DateValidator);

        c = '<';
        assertTrue(patternBuilder.validator(c) instanceof FileValidator);

        c = '>';
        assertTrue(patternBuilder.validator(c) instanceof FileValidator);

        c = '*';
        assertTrue(patternBuilder.validator(c) instanceof FileValidator);

        c = '/';
        assertTrue(patternBuilder.validator(c) instanceof UrlValidator);

        c = ':';
        assertNull(patternBuilder.validator(c));
    }
}