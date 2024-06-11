import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;
     // Unit Test for the constructor
    @Test
    public void testPatternBuilder() {
        patternBuilder = new PatternBuilder();
        Assert.assertNotNull(patternBuilder);
    }

    // Unit Test for the create() method
    @Test
    public void testCreate() {
        patternBuilder = new PatternBuilder();
        Option option = patternBuilder.create();
        Assert.assertNotNull(option);
    }

    // Unit Test for the reset() method
    @Test
    public void testReset() {
        patternBuilder = new PatternBuilder();
        patternBuilder.create();
        patternBuilder.reset();
        Assert.assertEquals(0, patternBuilder.options.size());
    }

    // Unit Test for the withPattern() method with valid pattern
    @Test
    public void testWithPatternValidPattern() {
        patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("ab:c@d:e:f+g%h#i<j>k/l*{m}");
        Option option = patternBuilder.create();
        Assert.assertEquals("a", option.getLongOpt());
        Assert.assertEquals('b', option.getOpt());
        Argument argument = option.getArgument();
        Assert.assertEquals("c", argument.getName());
        Assert.assertEquals(ClassValidator.class, argument.getValidator());
        option = patternBuilder.create();
        Assert.assertEquals("d", option.getLongOpt());
        Assert.assertEquals(':', option.getOpt());
        Argument argument1 = option.getArgument();
        Assert.assertEquals("e", argument1.getName());
        Assert.assertNull(argument1.getValidator());
        option = patternBuilder.create();
        Assert.assertEquals("f", option.getLongOpt());
        Assert.assertEquals('+', option.getOpt());
        Argument argument2 = option.getArgument();
        Assert.assertEquals("g", argument2.getName());
        Assert.assertEquals(NumberValidator.class, argument2.getValidator());
        option = patternBuilder.create();
        Assert.assertEquals("h", option.getLongOpt());
        Assert.assertEquals('#', option.getOpt());
        Argument argument3 = option.getArgument();
        Assert.assertEquals("i", argument3.getName());
        Assert.assertEquals(DateValidator.class, argument3.getValidator());
        option = patternBuilder.create();
        Assert.assertEquals("j", option.getLongOpt());
        Assert.assertEquals('<', option.getOpt());
        Argument argument4 = option.getArgument();
        Assert.assertEquals("j", argument4.getName());
        Assert.assertEquals(FileValidator.class, argument4.getValidator());
        Assert.assertEquals(true, argument4.isFile());
        option = patternBuilder.create();
        Assert.assertEquals("k", option.getLongOpt());
        Assert.assertEquals('>', option.getOpt());
        Argument argument5 = option.getArgument();
        Assert.assertEquals("k", argument5.getName());
        Assert.assertEquals(FileValidator.class, argument5.getValidator());
        option = patternBuilder.create();
        Assert.assertEquals("l", option.getLongOpt());
        Assert.assertEquals('/', option.getOpt());
        Argument argument6 = option.getArgument();
        Assert.assertEquals("l", argument6.getName());
        Assert.assertEquals(UrlValidator.class, argument6.getValidator());
        option = patternBuilder.create();
        Assert.assertEquals("m", option.getLongOpt());
        Assert.assertEquals('{', option.getOpt());
        Argument argument7 = option.getArgument();
        Assert.assertEquals("m", argument7.getName());
        Assert.assertEquals(FileValidator.class, argument7.getValidator());
    }

    // Unit Test for the withPattern() method with invalid pattern
    @Test(expected = IllegalArgumentException.class)
    public void testWithPatternInvalidPattern() {
        patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("ab:c@d:e:f+g%h#i<j>k/l*{m}x");
    }

    // Unit Test for the validator() method
    @Test
    public void testValidator() {
        Assert.assertEquals(ClassValidator.class, PatternBuilder.validator('@').getClass());
        Assert.assertEquals(ClassValidator.class, PatternBuilder.validator('+').getClass());
        Assert.assertEquals(DateValidator.class, PatternBuilder.validator('#').getClass());
        Assert.assertEquals(FileValidator.class, PatternBuilder.validator('<').getClass());
        Assert.assertEquals(FileValidator.class, PatternBuilder.validator('>').getClass());
        Assert.assertEquals(FileValidator.class, PatternBuilder.validator('*').getClass());
        Assert.assertEquals(UrlValidator.class, PatternBuilder.validator('/').getClass());
    }
}