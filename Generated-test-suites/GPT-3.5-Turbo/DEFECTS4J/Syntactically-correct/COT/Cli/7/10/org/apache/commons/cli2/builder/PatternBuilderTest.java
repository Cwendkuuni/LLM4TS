import org.apache.commons.cli2.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();

        Option option = patternBuilder.create();

        assertNotNull(option);
    }

    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();

        patternBuilder.reset();

        Option option = patternBuilder.create();

        assertNull(option);
    }

    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();

        patternBuilder.withPattern("-a");

        Option option = patternBuilder.create();

        assertNotNull(option);
        assertEquals("-a", option.getShortName());
    }

}