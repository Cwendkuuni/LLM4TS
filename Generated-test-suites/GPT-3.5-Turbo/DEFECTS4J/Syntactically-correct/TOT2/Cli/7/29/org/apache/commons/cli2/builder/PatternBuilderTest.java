import static org.junit.Assert.*;
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
        assertNotNull("Option should not be null", option);
    }

    @Test
    public void testReset() {
        patternBuilder.reset();
        Option option = patternBuilder.create();
        assertNull("Option should be null after reset", option);
    }

    @Test
    public void testWithPattern() {
        patternBuilder.withPattern("!@*");
        Option option = patternBuilder.create();
        assertNotNull("Option should not be null after setting pattern", option);
    }

    @Test
    public void testValidator() {
        Validator validator = PatternBuilder.validator('*');
        assertNotNull("Validator should not be null", validator);
    }
}