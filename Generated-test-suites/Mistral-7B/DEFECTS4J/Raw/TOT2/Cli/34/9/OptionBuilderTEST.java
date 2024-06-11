import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

    

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong");
        Option option = optionBuilder.create();
        Assert.assertEquals("optionLong", option.getLongOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.create();
        Assert.fail("An IllegalArgumentException should be thrown");
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.VALUE_REQUIRED, option.getArgs());
    }

    @Test
    public void testHasArgWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
        Option option = optionBuilder.create();
        Assert.assertEquals("argName", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create();
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasArgsWithNumArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        Option option = optionBuilder.create();
        Assert.assertEquals(3, option.getArgs().getValue());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.optionalArg);
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Option option = optionBuilder.create();
        Assert.assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Test Option Description");
        Option option = optionBuilder.create();
        Assert.assertEquals("Test Option Description", option.getDescription());
    }

    
}