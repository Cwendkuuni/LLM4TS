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
    public void testCreate_OneOption_ReturnsOption() {
        patternBuilder.withPattern("a");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("a", option.getShortName());
    }

    @Test
    public void testCreate_RequiredOption_ReturnsOptionWithRequiredTrue() {
        patternBuilder.withPattern("!a");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertTrue(option.isRequired());
    }

    @Test
    public void testCreate_WithArgument_ReturnsOptionWithArgument() {
        patternBuilder.withPattern("a+");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertNotNull(option.getArgument());
    }

    @Test
    public void testCreate_WithArgumentMinOne_ReturnsOptionWithArgumentMinOne() {
        patternBuilder.withPattern("a*");
        Option option = patternBuilder.create();
        assertNotNull(option);
        Argument argument = option.getArgument();
        assertNotNull(argument);
        assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testCreate_MultipleOptions_ReturnsGroupedOption() {
        patternBuilder.withPattern("a").withPattern("b").withPattern("c");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertTrue(option instanceof Group);
        Group group = (Group) option;
        assertEquals(3, group.getOptions().size());
    }

    @Test
    public void testReset_AfterCreation_ReturnsNewPatternBuilder() {
        patternBuilder.withPattern("a");
        patternBuilder.reset();
        Option option = patternBuilder.create();
        assertNull(option);
    }

    @Test
    public void testWithPattern_ValidPattern_CreatesOptionWithCorrectValidators() {
        patternBuilder.withPattern("@a:%b#c<d>e/f");
        Option option = patternBuilder.create();
        assertNotNull(option);

        Argument arg1 = option.getArgument();
        assertNotNull(arg1);
        Validator validator1 = arg1.getValidator();
        assertTrue(validator1 instanceof ClassValidator);
        ClassValidator classValidator = (ClassValidator) validator1;
        assertTrue(classValidator.isInstance());

        Argument arg2 = option.getArgument();
        assertNotNull(arg2);
        Validator validator2 = arg2.getValidator();
        assertNull(validator2); // No validator needed for a string

        Argument arg3 = option.getArgument();
        assertNotNull(arg3);
        Validator validator3 = arg3.getValidator();
        assertTrue(validator3 instanceof DateValidator);

        Argument arg4 = option.getArgument();
        assertNotNull(arg4);
        Validator validator4 = arg4.getValidator();
        assertTrue(validator4 instanceof FileValidator);
        FileValidator fileValidator = (FileValidator) validator4;
        assertTrue(fileValidator.isExisting());
        assertTrue(fileValidator.isFile());

        Argument arg5 = option.getArgument();
        assertNotNull(arg5);
        Validator validator5 = arg5.getValidator();
        assertTrue(validator5 instanceof UrlValidator);
    }

}