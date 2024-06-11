import org.junit.Test;
import static org.junit.Assert.*;

// Test cases for PatternBuilder class
public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.reset();
    }

    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("ab");
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testValidator() {
        Validator validator1 = PatternBuilder.validator('@');
        Validator validator2 = PatternBuilder.validator(':');
        Validator validator3 = PatternBuilder.validator('%');
        Validator validator4 = PatternBuilder.validator('#');
        Validator validator5 = PatternBuilder.validator('<');
        Validator validator6 = PatternBuilder.validator('>');
        Validator validator7 = PatternBuilder.validator('*');
        Validator validator8 = PatternBuilder.validator('/');

        assertNotNull(validator1);
        assertNull(validator2);
        assertNotNull(validator3);
        assertNotNull(validator4);
        assertNotNull(validator5);
        assertNotNull(validator6);
        assertNull(validator7);
        assertNotNull(validator8);
    }

}