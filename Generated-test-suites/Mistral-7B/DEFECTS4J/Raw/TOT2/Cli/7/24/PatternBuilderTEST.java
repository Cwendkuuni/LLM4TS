import org.apache.commons.cli2.Option;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class PatternBuilderTest {
    @Test
    public void testCreateOption() {
        PatternBuilder builder = new PatternBuilder();
        Option option = builder.create();
        assertNotNull(option);
    }
}