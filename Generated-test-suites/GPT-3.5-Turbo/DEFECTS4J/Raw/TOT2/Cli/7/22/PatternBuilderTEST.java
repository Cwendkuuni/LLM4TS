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
        assertEquals(0, patternBuilder.reset().options.size());
    }

    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("!@: %");
        assertEquals(1, patternBuilder.options.size());
    }

    @Test
    public void testValidator() {
        assertEquals(ClassValidator.class, PatternBuilder.validator('@').getClass());
        assertEquals(NumberValidator.class, PatternBuilder.validator('%').getClass());
        assertEquals(DateValidator.class, PatternBuilder.validator('#').getClass());
        assertEquals(FileValidator.class, PatternBuilder.validator('<').getClass());
        assertEquals(UrlValidator.class, PatternBuilder.validator('/').getClass());
        assertNull(PatternBuilder.validator('x'));
    }
}