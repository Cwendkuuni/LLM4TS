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
        assertEquals(0, patternBuilder.reset().size());
    }

    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("!:*");
        assertEquals(1, patternBuilder.size());
        patternBuilder.withPattern("%#");
        assertEquals(2, patternBuilder.size());
    }

    @Test
    public void testValidator() {
        char type1 = '@';
        char type2 = '#';
        assertEquals(ClassValidator.class, validator(type1).getClass());
        assertEquals(DateValidator.class, validator(type2).getClass());
    }
}