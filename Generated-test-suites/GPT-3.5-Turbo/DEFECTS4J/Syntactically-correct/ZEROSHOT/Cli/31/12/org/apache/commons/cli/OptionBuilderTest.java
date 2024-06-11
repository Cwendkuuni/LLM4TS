import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("long opt");
        Assert.assertEquals("long opt", optionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Assert.assertEquals(1, optionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        Assert.assertEquals(1, optionBuilder.numberOfArgs);

        optionBuilder = OptionBuilder.hasArg(false);
        Assert.assertEquals(-1, optionBuilder.numberOfArgs);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("arg name");
        Assert.assertEquals("arg name", optionBuilder.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Assert.assertTrue(optionBuilder.required);
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Assert.assertEquals(':', optionBuilder.valuesep);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        Assert.assertEquals('=', optionBuilder.valuesep);
    }

    @Test
    public void testIsRequiredBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
        Assert.assertTrue(optionBuilder.required);

        optionBuilder = OptionBuilder.isRequired(false);
        Assert.assertFalse(optionBuilder.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Assert.assertEquals(-2, optionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgsInt() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Assert.assertEquals(2, optionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Assert.assertEquals(1, optionBuilder.numberOfArgs);
        Assert.assertTrue(optionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Assert.assertEquals(-2, optionBuilder.numberOfArgs);
        Assert.assertTrue(optionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgsInt() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
        Assert.assertEquals(2, optionBuilder.numberOfArgs);
        Assert.assertTrue(optionBuilder.optionalArg);
    }

    @Test
    public void testWithType() {
        Object type = new Object();
        OptionBuilder optionBuilder = OptionBuilder.withType(type);
        Assert.assertEquals(type, optionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        Assert.assertEquals("description", optionBuilder.description);
    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('c');
        Assert.assertEquals("c", option.getOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCharIllegalArgumentException() {
        OptionBuilder.create(' ');
    }

    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("long opt");
        Option option = OptionBuilder.create();
        Assert.assertEquals("long opt", option.getLongOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateIllegalArgumentException() {
        OptionBuilder.reset();
        OptionBuilder.create();
    }

    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("opt");
        Assert.assertEquals("opt", option.getOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateStringIllegalArgumentException() {
        OptionBuilder.create(null);
    }

}