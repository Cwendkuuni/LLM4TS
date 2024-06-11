import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

public class OptionBuilderTest {

    private OptionBuilder optionBuilder;

    @Before
    public void setUp() {
        optionBuilder = new OptionBuilder();
    }

    // Testing withLongOpt() method

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("testOption");
        Option option = OptionBuilder.create("a");
        
        Assert.assertEquals("testOption", option.getLongOpt());
    }

    // Testing hasArg() method

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create("a");

        Assert.assertEquals(1, option.getArgs());
    }

    // Testing hasArg(boolean hasArg) method

    @Test
    public void testHasArg_CustomValue() {
        OptionBuilder.hasArg(true);
        Option option = OptionBuilder.create("a");

        Assert.assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasArg_CustomValue_False() {
        OptionBuilder.hasArg(false);
        Option option = OptionBuilder.create("a");

        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    // Testing withArgName() method

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("testArgName");
        Option option = OptionBuilder.create("a");

        Assert.assertEquals("testArgName", option.getArgName());
    }

    // Testing isRequired() method

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create();

        Assert.assertTrue(option.isRequired());
    }

    // Testing withValueSeparator() method

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create('a');

        Assert.assertEquals(':', option.getValueSeparator());
    }

    // Testing withValueSeparator() method with default separator '='

    @Test
    public void testWithValueSeparator_Default() {
        OptionBuilder.withValueSeparator();
        Option option = OptionBuilder.create('a');

        Assert.assertEquals('=', option.getValueSeparator());
    }

    // Testing isRequired(boolean newRequired) method

    @Test
    public void testIsRequired_CustomValue() {
        OptionBuilder.isRequired(true);
        Option option = OptionBuilder.create();

        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testIsRequired_CustomValue_False() {
        OptionBuilder.isRequired(false);
        Option option = OptionBuilder.create();

        Assert.assertFalse(option.isRequired());
    }

    // Testing hasArgs() method

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.create();

        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    // Testing hasArgs(int num) method

    @Test
    public void testHasArgs_CustomValue() {
        OptionBuilder.hasArgs(3);
        Option option = OptionBuilder.create();

        Assert.assertEquals(3, option.getArgs());
    }

    // Testing hasOptionalArg() method

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create();

        Assert.assertTrue(option.hasOptionalArg());
    }

    // Testing hasOptionalArgs() method

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create();

        Assert.assertTrue(option.hasOptionalArgs());
    }

    // Testing hasOptionalArgs(int numArgs) method

    @Test
    public void testHasOptionalArgs_CustomValue() {
        OptionBuilder.hasOptionalArgs(2);
        Option option = OptionBuilder.create();

        Assert.assertTrue(option.hasOptionalArgs());
        Assert.assertEquals(2, option.getArgs());
    }

    // Testing withType() method

    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        Option option = OptionBuilder.create();

        Assert.assertEquals(Integer.class, option.getType());
    }

    // Testing withDescription() method

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("testDescription");
        Option option = OptionBuilder.create();

        Assert.assertEquals("testDescription", option.getDescription());
    }

    // Testing create(char opt) method

    @Test
    public void testCreate_CharOpt() {
        Option option = OptionBuilder.create('a');

        Assert.assertEquals("a", option.getOpt());
    }

    // Testing create() method

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_NoLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testCreate_LongOpt() {
        OptionBuilder.withLongOpt("testOption");
        Option option = OptionBuilder.create();

        Assert.assertEquals("testOption", option.getLongOpt());
    }

    // Testing create(String opt) method

    @Test
    public void testCreate_StringOpt() {
        Option option = OptionBuilder.create("a");

        Assert.assertEquals("a", option.getOpt());
    }

}