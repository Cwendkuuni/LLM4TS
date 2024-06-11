import org.apache.commons.cli2.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {

    @Test
    public void testConstructor() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Assert.assertNotNull(patternBuilder);
    }

    @Test
    public void testOverloadedConstructor() {
        PatternBuilder patternBuilder = new PatternBuilder(
            new GroupBuilder(),
            new DefaultOptionBuilder(),
            new ArgumentBuilder());
        Assert.assertNotNull(patternBuilder);
    }

    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option = patternBuilder.create();
        Assert.assertNotNull(option);
        PatternBuilder patternBuilder2 = new PatternBuilder();
        Option option2 = patternBuilder2.create();
        Assert.assertNotSame(option, option2);
    }

    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option = patternBuilder.create();
        patternBuilder.reset();
        Option option2 = patternBuilder.create();
        Assert.assertNotSame(option, option2);
    }

    @Test
    public void testEmptyPatternString() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Assert.assertThrows(IllegalArgumentException.class, () -> patternBuilder.withPattern(""));
    }

    @Test
    public void testOptionalOptionCharacterWithNoArgument() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("+");
        Option option = patternBuilder.create();
        Assert.assertNotNull(option);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCharacterAfterValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("%a@");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCharacterBeforeRequiredOption() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("!@");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCharactersInPatternString() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("!@%");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCharacterBeforeValidators() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("^%");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgumentCharacter() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("!a");
    }

}