import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setup() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void create_withPattern_shouldCreateOption() {
        patternBuilder.withPattern("!@:/%");
        Option option = patternBuilder.create();
        Assert.assertNotNull(option);
    }

    @Test
    public void reset_shouldClearOptions() {
        patternBuilder.withPattern("!@:/%");
        patternBuilder.reset();
        Option option = patternBuilder.create();
        Assert.assertNull(option);
    }

    @Test
    public void withPattern_shouldSetOptionType() {
        patternBuilder.withPattern("!@:/%");
        Option option = patternBuilder.create();
        Assert.assertEquals('%', option.getArgument().getValidator().getSymbol());
    }

    @Test
    public void withPattern_shouldSetOptionRequired() {
        patternBuilder.withPattern("!@:/%");
        Option option = patternBuilder.create();
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void withPattern_shouldSetOptionShortName() {
        patternBuilder.withPattern("!@:/%");
        Option option = patternBuilder.create();
        Assert.assertEquals("/", option.getShortName());
    }

    @Test
    public void validator_shouldReturnValidatorInstance() {
        Validator validator = patternBuilder.validator('#');
        Assert.assertNotNull(validator);
    }

    @Test
    public void validator_shouldReturnNullForInvalidSymbol() {
        Validator validator = patternBuilder.validator('&');
        Assert.assertNull(validator);
    }
}