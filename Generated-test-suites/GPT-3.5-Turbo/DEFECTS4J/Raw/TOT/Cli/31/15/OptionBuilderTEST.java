
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        Assert.assertEquals("test", OptionBuilder.getLongopt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Assert.assertEquals(1, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithValue() {
        OptionBuilder.hasArg(false);
        Assert.assertEquals(Option.UNINITIALIZED, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("arg");
        Assert.assertEquals("arg", OptionBuilder.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Assert.assertTrue(OptionBuilder.isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        Assert.assertEquals(':', OptionBuilder.getValuesep());
    }

    @Test
    public void testWithDefaultValueSeparator() {
        OptionBuilder.withValueSeparator();
        Assert.assertEquals('=', OptionBuilder.getValuesep());
    }

    @Test
    public void testIsRequiredWithValue() {
        OptionBuilder.isRequired(true);
        Assert.assertTrue(OptionBuilder.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Assert.assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgsWithValue() {
        OptionBuilder.hasArgs(2);
        Assert.assertEquals(2, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Assert.assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Assert.assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
        Assert.assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgsWithValue() {
        OptionBuilder.hasOptionalArgs(3);
        Assert.assertEquals(3, OptionBuilder.getNumberOfArgs());
        Assert.assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        Assert.assertEquals(String.class, OptionBuilder.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test description");
        Assert.assertEquals("test description", OptionBuilder.getDescription());
    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('t');
        Assert.assertNotNull(option);
        Assert.assertEquals("t", option.getOpt());
    }

    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("test");
        Option option = OptionBuilder.create();
        Assert.assertNotNull(option);
        Assert.assertEquals("test", option.getLongOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.reset();
        OptionBuilder.create();
    }

    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("test");
        Assert.assertNotNull(option);
        Assert.assertEquals("test", option.getOpt());
    }
}

