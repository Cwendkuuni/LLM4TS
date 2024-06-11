
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        Option option1 = patternBuilder.create();
        assertNotNull(option1);
    }

    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("-o");
        patternBuilder.reset();
        
        Option option = patternBuilder.create();
        assertNull(option);
    }

    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("-o");
        
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testValidator() {
        ClassValidator classValidator = new ClassValidator();
        classValidator.setInstance(true);
        
        assertTrue(classValidator.getInstance());
    }
}
