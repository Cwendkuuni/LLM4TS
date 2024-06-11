import org.apache.commons.cli2.Option;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PatternBuilderTest {
    private PatternBuilder builder;

    @Before
    public void setUp() {
        builder = new PatternBuilder();
    }

    @Test
    public void testCreate() {
        Option option1 = builder.create();
        Option option2 = builder.create();
        assertNotNull(option1);
        assertNotNull(option2);
        assertTrue(option1 != option2);
    }
}