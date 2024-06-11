import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli2.Option;
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
        patternBuilder.reset();
        //Check that options are cleared
        assertEquals(0, patternBuilder.options.size());
    }

    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("!@example");
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testValidator() {
        char c = '@';
        Validator validator = validator(c);
        assertNotNull(validator);
    }

    @Test
    public void testValidatorWithInvalidChar() {
        char c = '!';
        Validator validator = validator(c);
        assertNull(validator);
    }
}