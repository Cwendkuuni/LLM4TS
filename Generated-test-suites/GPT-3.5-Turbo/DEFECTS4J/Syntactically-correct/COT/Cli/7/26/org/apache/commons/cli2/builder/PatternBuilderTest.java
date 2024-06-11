import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.validation.Validator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option = patternBuilder.create();
        assertNull(option);

        patternBuilder.reset();
        assertEquals(0, patternBuilder.options.size());
    }

    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.reset();
        assertEquals(0, patternBuilder.options.size());
    }

    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("!@: <%>/*");
        Option option = patternBuilder.create();
        assertNotNull(option);
    }
    
    @Test
    public void testWithPatternEmpty() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("");
        Option option = patternBuilder.create();
        assertNull(option);
    }

    @Test
    public void testCreateOption() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.createOption(' ', false, 'a');
        patternBuilder.createOption(':', true, 'b');
        patternBuilder.createOption('*', false, 'c');
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testValidator() {
        Validator validator = PatternBuilder.validator('@');
        assertTrue(validator instanceof ClassValidator);

        validator = PatternBuilder.validator('+');
        assertTrue(validator instanceof ClassValidator);

        validator = PatternBuilder.validator('%');
        assertTrue(validator instanceof NumberValidator);

        validator = PatternBuilder.validator('#');
        assertTrue(validator instanceof DateValidator);

        validator = PatternBuilder.validator('<');
        assertTrue(validator instanceof FileValidator);

        validator = PatternBuilder.validator('>');
        assertTrue(validator instanceof FileValidator);

        validator = PatternBuilder.validator('*');
        assertTrue(validator instanceof FileValidator);

        validator = PatternBuilder.validator('/');
        assertTrue(validator instanceof UrlValidator);

        validator = PatternBuilder.validator(':');
        assertNull(validator);
    }
}