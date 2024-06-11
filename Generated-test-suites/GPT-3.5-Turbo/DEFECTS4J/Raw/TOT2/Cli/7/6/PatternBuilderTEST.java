import static org.junit.Assert.*;
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        assertNotNull(patternBuilder.create());
    }

    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.reset();
        assertTrue(patternBuilder.reset().reset().reset().reset() instanceof PatternBuilder);
    }

    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("-% -@");
        assertNotNull(patternBuilder.create());
    }

    @Test
    public void testValidator() {
        char c = '@';
        assertNotNull(PatternBuilder.validator(c));
    }
}