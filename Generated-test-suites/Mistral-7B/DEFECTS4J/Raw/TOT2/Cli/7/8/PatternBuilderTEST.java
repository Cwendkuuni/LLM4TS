import org.junit.Test;
import org.apache.commons.cli2.Option;
import static org.junit.Assert.assertNotNull;

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
        //Test if builder is reset
        Option option3 = builder.create();
        assertNotSame(option1, option3);
    }
}