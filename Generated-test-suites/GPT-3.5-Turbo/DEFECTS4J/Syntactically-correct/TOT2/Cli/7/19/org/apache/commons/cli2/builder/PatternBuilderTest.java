import static org.junit.Assert.*;
import org.junit.Test;

public class PatternBuilderTest {
    PatternBuilder patternBuilder = new PatternBuilder();

    @Test
    public void testCreate() {
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testReset() {
        patternBuilder.reset();
        assertTrue(patternBuilder.reset().equals(patternBuilder));
    }

    @Test
    public void testWithPattern() {
        patternBuilder.withPattern("!@:123");
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testValidator() {
        Validator validator = PatternBuilder.validator('@');
        assertNotNull(validator);
    }
}