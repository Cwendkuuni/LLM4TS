import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder(
            new GroupBuilder(),
            new DefaultOptionBuilder(),
            new ArgumentBuilder());
    }

    @Test
    public void testCreate() {
        Option option = patternBuilder.create();
        Assert.assertNotNull(option);
        Assert.assertTrue(option instanceof Option);
    }

    @Test
    public void testReset() {
        patternBuilder.reset();
    }

    @Test
    public void testWithPattern() {
        patternBuilder.withPattern("a");
        Option option = patternBuilder.create();
        Assert.assertEquals("a", option.getShortName());
    }

    @Test
    public void testWithPatternMultipleOptions() {
        patternBuilder.withPattern("ab");
        Option option = patternBuilder.create();
        Assert.assertEquals(2, option.getOptions().size());
        Assert.assertEquals("a", option.getOptions().get(0).getShortName());
        Assert.assertEquals("b", option.getOptions().get(1).getShortName());
    }

    @Test
    public void testValidator() {
        Assert.assertTrue(PatternBuilder.validator('@') instanceof ClassValidator);
        Assert.assertTrue(PatternBuilder.validator('+') instanceof ClassValidator);
        Assert.assertTrue(PatternBuilder.validator('%') instanceof NumberValidator);
        Assert.assertTrue(PatternBuilder.validator('#') instanceof DateValidator);
        Assert.assertTrue(PatternBuilder.validator('<') instanceof FileValidator);
        Assert.assertTrue(PatternBuilder.validator('>') instanceof FileValidator);
        Assert.assertTrue(PatternBuilder.validator('*') instanceof FileValidator);
        Assert.assertTrue(PatternBuilder.validator('/') instanceof UrlValidator);
    }
}