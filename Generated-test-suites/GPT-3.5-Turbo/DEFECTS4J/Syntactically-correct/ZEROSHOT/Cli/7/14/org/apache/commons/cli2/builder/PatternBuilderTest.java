import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testCreate() {
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testReset() {
        patternBuilder.reset();
        Option option = patternBuilder.create();
        assertNull(option);
    }

    @Test
    public void testWithPattern() {
        String pattern = "!@:";
        patternBuilder.withPattern(pattern);
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testValidator() {
        char c = '@';
        Validator validator = patternBuilder.validator(c);
        assertNotNull(validator);
    }

    @Test
    public void testPatternBuilderConstructor() {
        GroupBuilder gbuilder = new GroupBuilder();
        DefaultOptionBuilder obuilder = new DefaultOptionBuilder();
        ArgumentBuilder abuilder = new ArgumentBuilder();
        PatternBuilder patternBuilder = new PatternBuilder(gbuilder, obuilder, abuilder);
        assertNotNull(patternBuilder);
    }

    @Test
    public void testCreateOption() {
        char type = '@';
        boolean required = true;
        char opt = 'a';
        patternBuilder.createOption(type, required, opt);
        Option option = patternBuilder.create();
        assertNotNull(option);
    }
}