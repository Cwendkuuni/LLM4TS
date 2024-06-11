
import org.junit.Test;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Group;
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

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option = patternBuilder.create();
        Assert.assertNull(option);
    }

    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("!a:b#!c@:d%e/f*g>h/i");
        Option option = patternBuilder.create();
        Assert.assertNotNull(option);
    }

    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.reset();
        Option option = patternBuilder.create();
        Assert.assertNull(option);
    }

    @Test
    public void testValidator() {
        char[] supportedValidators = {'@', '+', '%', '#', '<', '>', '*', '/'};

        for(char c : supportedValidators) {
            Validator validator = PatternBuilder.validator(c);
            Assert.assertNotNull(validator);
        }

        char unsupportedValidator = ':';
        Validator validator = PatternBuilder.validator(unsupportedValidator);
        Assert.assertNull(validator);
    }
    
}
