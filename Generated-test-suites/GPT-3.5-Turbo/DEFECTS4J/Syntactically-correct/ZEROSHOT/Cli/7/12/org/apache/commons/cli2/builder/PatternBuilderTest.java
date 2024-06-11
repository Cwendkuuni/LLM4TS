import org.junit.Assert;
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option = patternBuilder.create();
        Assert.assertNull(option);
    }

    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.reset();
        Option option = patternBuilder.create();
        Assert.assertNull(option);
    }

    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("!@:*");
        Option option = patternBuilder.create();
        Assert.assertNotNull(option);
    }

    @Test
    public void testValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Validator validator = patternBuilder.validator('#');
        Assert.assertNotNull(validator);
    }
}