import org.junit.Test;
import org.apache.commons.cli2.Option;
import static org.junit.Assert.assertNotNull;

public class PatternBuilderTest {
    @Test
    public void testCreateOption() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option = patternBuilder.create();
        assertNotNull(option);
    }
}