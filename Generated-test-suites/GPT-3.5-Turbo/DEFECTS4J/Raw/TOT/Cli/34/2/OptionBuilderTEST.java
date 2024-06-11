import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OptionBuilderTest {

    private OptionBuilder optionBuilder;

    @Before
    public void setUp() {
        optionBuilder = new OptionBuilder();
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder result = optionBuilder.withLongOpt("testLongOpt");
        Assert.assertEquals("testLongOpt", OptionBuilder.getLongopt());
        Assert.assertEquals(optionBuilder, result);
    }

    @Test
    public void testHasArg() {
        OptionBuilder result = optionBuilder.hasArg();
        Assert.assertEquals(1, OptionBuilder.getNumberOfArgs());
        Assert.assertEquals(optionBuilder, result);
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder result1 = optionBuilder.hasArg(true);
        Assert.assertEquals(1, OptionBuilder.getNumberOfArgs());
        Assert.assertEquals(optionBuilder, result1);

        OptionBuilder result2 = optionBuilder.hasArg(false);
        Assert.assertEquals(Option.UNINITIALIZED, OptionBuilder.getNumberOfArgs());
        Assert.assertEquals(optionBuilder, result2);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder result = optionBuilder.withArgName("testArgName");
        Assert.assertEquals("testArgName", OptionBuilder.getArgName());
        Assert.assertEquals(optionBuilder, result);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder result = optionBuilder.isRequired();
        Assert.assertTrue(OptionBuilder.isRequired());
        Assert.assertEquals(optionBuilder, result);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder result = optionBuilder.withValueSeparator(':');
        Assert.assertEquals(':', OptionBuilder.getValuesep());
        Assert.assertEquals(optionBuilder, result);
    }

    @Test
    public void testWithDefaultValueSeparator() {
        OptionBuilder result = optionBuilder.withValueSeparator();
        Assert.assertEquals('=', OptionBuilder.getValuesep());
        Assert.assertEquals(optionBuilder, result);
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder result1 = optionBuilder.isRequired(true);
        Assert.assertTrue(OptionBuilder.isRequired());
        Assert.assertEquals(optionBuilder, result1);

        OptionBuilder result2 = optionBuilder.isRequired(false);
        Assert.assertFalse(OptionBuilder.isRequired());
        Assert.assertEquals(optionBuilder, result2);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder result = optionBuilder.hasArgs();
        Assert.assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
        Assert.assertEquals(optionBuilder, result);
    }

    @Test
    public void testHasArgsWithValue() {
        OptionBuilder result = optionBuilder.hasArgs(3);
        Assert.assertEquals(3, OptionBuilder.getNumberOfArgs());
        Assert.assertEquals(optionBuilder, result);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder result = optionBuilder.hasOptionalArg();
        Assert.assertEquals(1, OptionBuilder.getNumberOfArgs());
        Assert.assertTrue(OptionBuilder.isOptionalArg());
        Assert.assertEquals(optionBuilder, result);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder result = optionBuilder.hasOptionalArgs();
        Assert.assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
        Assert.assertTrue(OptionBuilder.isOptionalArg());
        Assert.assertEquals(optionBuilder, result);
    }

    @Test
    public void testHasOptionalArgsWithValue() {
        OptionBuilder result = optionBuilder.hasOptionalArgs(2);
        Assert.assertEquals(2, OptionBuilder.getNumberOfArgs());
        Assert.assertTrue(OptionBuilder.isOptionalArg());
        Assert.assertEquals(optionBuilder, result);
    }

    @Test
    public void testWithType() {
        OptionBuilder result = optionBuilder.withType(TestClass.class);
        Assert.assertEquals(TestClass.class, OptionBuilder.getType());
        Assert.assertEquals(optionBuilder, result);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder result = optionBuilder.withDescription("testDescription");
        Assert.assertEquals("testDescription", OptionBuilder.getDescription());
        Assert.assertEquals(optionBuilder, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithOpt() {
        optionBuilder.withLongOpt("testOpt").create('O');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullOpt() {
        optionBuilder.create(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        optionBuilder.create();
    }

    @Test
    public void testCreate() {
        Option option = optionBuilder.withLongOpt("testLongOpt")
                .hasArg()
                .withArgName("testArg")
                .isRequired()
                .withValueSeparator(':')
                .withType(TestClass.class)
                .withDescription("testDescription")
                .create('O');

        Assert.assertEquals("testLongOpt", option.getLongOpt());
        Assert.assertEquals(1, option.getArgs());
        Assert.assertEquals("testArg", option.getArgName());
        Assert.assertTrue(option.isRequired());
        Assert.assertEquals(':', option.getValueSeparator());
        Assert.assertEquals(TestClass.class, option.getType());
        Assert.assertEquals("testDescription", option.getDescription());
        Assert.assertEquals('O', option.getOpt());
    }

    private static class TestClass {
    }
}