
import org.junit.Test;

import static org.junit.Assert.*;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        // create a new PatternBuilder
        PatternBuilder patternBuilder = new PatternBuilder();

        // add options to the PatternBuilder
        patternBuilder.withPattern("!a@b:$c");

        // create an Option instance
        Option option = patternBuilder.create();

        // check if the created Option instance is not null
        assertNotNull(option);
    }

    @Test
    public void testReset() {
        // create a new PatternBuilder
        PatternBuilder patternBuilder = new PatternBuilder();

        // add options to the PatternBuilder
        patternBuilder.withPattern("!a@b:$c");

        // reset the PatternBuilder
        patternBuilder.reset();

        // check if the options are cleared
        assertEquals(0, patternBuilder.getOptions().size());
    }

    @Test
    public void testWithPattern() {
        // create a new PatternBuilder
        PatternBuilder patternBuilder = new PatternBuilder();

        // add options to the PatternBuilder
        patternBuilder.withPattern("!a@b:$c");

        // check if the options are added correctly
        assertEquals(3, patternBuilder.getOptions().size());
    }
}
