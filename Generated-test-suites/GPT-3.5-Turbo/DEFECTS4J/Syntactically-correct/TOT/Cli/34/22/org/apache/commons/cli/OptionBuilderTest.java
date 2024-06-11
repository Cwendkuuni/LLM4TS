import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("longOpt");
        // Assert whether longopt is set correctly
        Assert.assertEquals("longOpt", OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        // Assert whether numberOfArgs is set as 1
        Assert.assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        // Assert whether numberOfArgs is set as 1
        Assert.assertEquals(1, OptionBuilder.numberOfArgs);

        OptionBuilder.hasArg(false);
        // Assert whether numberOfArgs is set as Option.UNINITIALIZED
        Assert.assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("argName");
        // Assert whether argName is set correctly
        Assert.assertEquals("argName", OptionBuilder.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        // Assert whether required is set as true
        Assert.assertTrue(OptionBuilder.required);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        // Assert whether valuesep is set correctly
        Assert.assertEquals(':', OptionBuilder.valuesep);
    }

    @Test
    public void testWithValueSeparator_default() {
        OptionBuilder.withValueSeparator();
        // Assert whether valuesep is set as '='
        Assert.assertEquals('=', OptionBuilder.valuesep);
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder.isRequired(true);
        // Assert whether required is set as true
        Assert.assertTrue(OptionBuilder.required);

        OptionBuilder.isRequired(false);
        // Assert whether required is set as false
        Assert.assertFalse(OptionBuilder.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        // Assert whether numberOfArgs is set as Option.UNLIMITED_VALUES
        Assert.assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgsWithNum() {
        OptionBuilder.hasArgs(2);
        // Assert whether numberOfArgs is set as 2
        Assert.assertEquals(2, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        // Assert whether numberOfArgs is set as 1
        Assert.assertEquals(1, OptionBuilder.numberOfArgs);
        // Assert whether optionalArg is set as true
        Assert.assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        // Assert whether numberOfArgs is set as Option.UNLIMITED_VALUES
        Assert.assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        // Assert whether optionalArg is set as true
        Assert.assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder.hasOptionalArgs(3);
        // Assert whether numberOfArgs is set as 3
        Assert.assertEquals(3, OptionBuilder.numberOfArgs);
        // Assert whether optionalArg is set as true
        Assert.assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        // Assert whether type is set as Integer.class
        Assert.assertEquals(Integer.class, OptionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        // Assert whether description is set correctly
        Assert.assertEquals("description", OptionBuilder.description);
    }

    @Test
    public void testCreate_char() {
        OptionBuilder.withLongOpt("longOpt");
        Option option = OptionBuilder.create('C');
        // Assert whether created option is not null
        Assert.assertNotNull(option);
        // Assert whether option has the correct details
        Assert.assertEquals('C', option.getOpt());
        Assert.assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("longOpt");
        Option option = OptionBuilder.create();
        // Assert whether created option is not null
        Assert.assertNotNull(option);
        // Assert whether option has the correct details
        Assert.assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testCreate_string() {
        OptionBuilder.withLongOpt("longOpt");
        Option option = OptionBuilder.create("opt");
        // Assert whether created option is not null
        Assert.assertNotNull(option);
        // Assert whether option has the correct details
        Assert.assertEquals("opt", option.getOpt());
        Assert.assertEquals("longOpt", option.getLongOpt());
    }

}