import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testCreateMethodWithSingleOption() {
        patternBuilder.withPattern("a");

        Option option = patternBuilder.create();

        Assert.assertNotNull(option);
    }

    @Test
    public void testCreateMethodWithMultipleOptions() {
        patternBuilder.withPattern("a:b:c");

        Option option = patternBuilder.create();

        Assert.assertNotNull(option);
    }

    @Test
    public void testResetMethod() {
        patternBuilder.withPattern("a");
        patternBuilder.reset();

        Option option = patternBuilder.create();

        Assert.assertNull(option);
    }

    @Test
    public void testWithPatternMethodWithValidPattern() {
        patternBuilder.withPattern("a:!c");

        Option option = patternBuilder.create();

        Assert.assertNotNull(option);
    }

    @Test
    public void testWithPatternMethodWithInvalidPattern() {
        patternBuilder.withPattern("*");

        Option option = patternBuilder.create();

        Assert.assertNull(option);
    }
}