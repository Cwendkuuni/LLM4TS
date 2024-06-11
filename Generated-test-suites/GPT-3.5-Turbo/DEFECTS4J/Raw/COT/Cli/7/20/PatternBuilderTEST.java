import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.validation.Validator;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder pb = new PatternBuilder();
        Option option = pb.create();
        Assert.assertNotNull(option);
    }

    @Test
    public void testReset() {
        PatternBuilder pb = new PatternBuilder();
        pb.reset();
        Option option = pb.create();
        Assert.assertNotNull(option);
    }

    @Test
    public void testWithPattern() {
        PatternBuilder pb = new PatternBuilder();
        String pattern = "a";
        pb.withPattern(pattern);
        Option option = pb.create();
        Assert.assertNotNull(option);
    }

    @Test
    public void testValidator() {
        char c = 'a';
        Validator validator = PatternBuilder.validator(c);
        Assert.assertNull(validator);
    }

}