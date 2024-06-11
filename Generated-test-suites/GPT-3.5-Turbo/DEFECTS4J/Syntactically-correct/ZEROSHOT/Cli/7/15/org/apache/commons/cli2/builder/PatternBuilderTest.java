import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option = patternBuilder.create();
        assertNull(option);
    }
    
    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.reset();
        Option option = patternBuilder.create();
        assertNull(option);
    }
    
    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("!@:file");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(1, option.getOptions().size());
        assertEquals("file", option.getOptions().get(0).toString());
        assertEquals(true, option.isRequired());
    }
    
    @Test
    public void testWithPattern_multipleOptions() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("<file1>:file2");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(2, option.getOptions().size());
        assertEquals("file2", option.getOptions().get(1).toString());
        assertEquals(false, option.getOptions().get(1).isRequired());
    }
    
    @Test
    public void testValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Validator validator = patternBuilder.validator(':');
        assertNull(validator);
        validator = patternBuilder.validator('@');
        assertNotNull(validator);
    }
}