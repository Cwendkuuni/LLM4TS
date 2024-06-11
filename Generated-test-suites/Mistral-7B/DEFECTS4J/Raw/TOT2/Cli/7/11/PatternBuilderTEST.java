import org.apache.commons.cli2.Option;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class PatternBuilderTest {
    @Test
    public void testCreate() {
        PatternBuilder pb = new PatternBuilder();
        Option option = pb.create();
        assertNotNull(option);
    }
}