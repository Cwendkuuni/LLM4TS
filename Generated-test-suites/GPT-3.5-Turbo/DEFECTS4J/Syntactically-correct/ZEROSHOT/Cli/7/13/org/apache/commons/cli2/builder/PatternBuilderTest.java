import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.GroupBuilder;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Value;
import org.apache.commons.cli2.builder.PatternBuilder;
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
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testReset() {
        patternBuilder.reset();
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testWithPattern() {
        patternBuilder.withPattern("!@:/%");
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testValidator() {
        Argument argument = new ArgumentBuilder().create();
        assertEquals(null, PatternBuilder.validator(' '));
    }
}