import static org.junit.Assert.*;
import org.junit.Test;

public class PatternBuilderTest {
    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("-v");
        assertEquals(Option, patternBuilder.create().getClass());
    }

    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("-v");
        patternBuilder.reset();
        assertTrue(patternBuilder.options.isEmpty());
    }

    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("-v");
        assertEquals("-v", patternBuilder.options.get(0).getShortName());
        assertEquals(false, patternBuilder.options.get(0).isRequired());
    }

    @Test
    public void testValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("@v");
        assertEquals(ClassValidator.class, patternBuilder.options.get(0).getArgument().getValidator().getClass());
    }
}