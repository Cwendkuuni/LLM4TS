import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
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
        patternBuilder.withPattern("abc");
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testWithPattern_customValidators() {
        patternBuilder.withPattern("@abc");
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testWithPattern_numberValidator() {
        patternBuilder.withPattern("%abc");
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testWithPattern_dateValidator() {
        patternBuilder.withPattern("#abc");
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testWithPattern_fileValidatorExisting() {
        patternBuilder.withPattern("<abc");
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testWithPattern_fileValidator() {
        patternBuilder.withPattern(">*abc");
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testWithPattern_urlValidator() {
        patternBuilder.withPattern("/abc");
        Option option = patternBuilder.create();
        assertNotNull(option);
    }
}