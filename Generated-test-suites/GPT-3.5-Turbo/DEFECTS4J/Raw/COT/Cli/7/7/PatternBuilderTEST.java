import org.junit.Test;

public class PatternBuilderTest {

    // Test create() method
    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option = patternBuilder.create();
        // Assert statement
    }

    // Test reset() method
    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.reset();
        // Assert statement
    }

    // Test withPattern() method
    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("examplePattern");
        // Assert statement
    }

    // Test validator() method for char '@'
    @Test
    public void testValidatorCharAt() {
        Validator validator = patternBuilder.validator('@');
        // Assert statement
    }

    // Test validator() method for char '+'
    @Test
    public void testValidatorCharPlus() {
        Validator validator = patternBuilder.validator('+');
        // Assert statement
    }

    // Test validator() method for char '%'
    @Test
    public void testValidatorCharPercent() {
        Validator validator = patternBuilder.validator('%');
        // Assert statement
    }

    // Test validator() method for char '#'
    @Test
    public void testValidatorCharHash() {
        Validator validator = patternBuilder.validator('#');
        // Assert statement
    }

    // Test validator() method for char '<'
    @Test
    public void testValidatorCharLessThan() {
        Validator validator = patternBuilder.validator('<');
        // Assert statement
    }

    // Test validator() method for char '>'
    @Test
    public void testValidatorCharGreaterThan() {
        Validator validator = patternBuilder.validator('>');
        // Assert statement
    }

    // Test validator() method for char '/'
    @Test
    public void testValidatorCharSlash() {
        Validator validator = patternBuilder.validator('/');
        // Assert statement
    }

    // Test validator() method for invalid char
    @Test
    public void testValidatorInvalidChar() {
        Validator validator = patternBuilder.validator('!');
        // Assert statement
    }
}