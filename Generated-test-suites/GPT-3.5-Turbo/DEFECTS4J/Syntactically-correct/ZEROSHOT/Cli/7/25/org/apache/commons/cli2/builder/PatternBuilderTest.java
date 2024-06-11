import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;
    private GroupBuilder groupBuilder;
    private DefaultOptionBuilder optionBuilder;
    private ArgumentBuilder argumentBuilder;

    @Before
    public void setUp() {
        groupBuilder = new GroupBuilder();
        optionBuilder = new DefaultOptionBuilder();
        argumentBuilder = new ArgumentBuilder();
        patternBuilder = new PatternBuilder(groupBuilder, optionBuilder, argumentBuilder);
    }

    @Test
    public void testCreate() {
        Option firstOption = new Option();
        firstOption.setName("firstOption");

        Option secondOption = new Option();
        secondOption.setName("secondOption");

        groupBuilder.reset();
        groupBuilder.withOption(firstOption);
        groupBuilder.withOption(secondOption);
        Option expectedOption = groupBuilder.create();

        Option actualOption = patternBuilder.create();

        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testReset() {
        Option option = new Option();
        option.setName("option");
        patternBuilder.reset();

        Option actualOption = patternBuilder.create();

        assertNull(actualOption);
    }

    @Test
    public void testWithPattern() {
        String pattern = "!@[*]/#";
        Argument argument = new Argument();
        Validator argumentValidator = new ClassValidator();
        argumentValidator.setInstance(true);
        argument.setValidator(argumentValidator);
        argument.setMinimum(1);
        argument.setMaximum(1);
        optionBuilder.reset();
        optionBuilder.withArgument(argument);
        optionBuilder.withShortName("*");
        optionBuilder.withRequired(true);
        Option expectedOption = optionBuilder.create();

        patternBuilder.withPattern(pattern);
        Option actualOption = patternBuilder.create();

        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testValidator() {
        char c = '@';
        Validator expectedValidator = new ClassValidator();
        ((ClassValidator) expectedValidator).setInstance(true);

        Validator actualValidator = patternBuilder.validator(c);

        assertEquals(expectedValidator, actualValidator);
    }
}