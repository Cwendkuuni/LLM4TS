import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("testLongOpt");
        // Assert that longopt has been set correctly
        Assert.assertEquals("testLongOpt", OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        // Assert that numberOfArgs has been set correctly
        Assert.assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgWithCondition() {
        OptionBuilder.hasArg(true);
        // Assert that numberOfArgs has been set correctly
        Assert.assertEquals(1, OptionBuilder.numberOfArgs);

        OptionBuilder.hasArg(false);
        // Assert that numberOfArgs has been set correctly
        Assert.assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("testArgName");
        // Assert that argName has been set correctly
        Assert.assertEquals("testArgName", OptionBuilder.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        // Assert that required has been set correctly
        Assert.assertTrue(OptionBuilder.required);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        // Assert that valuesep has been set correctly
        Assert.assertEquals(':', OptionBuilder.valuesep);
    }

    @Test
    public void testWithValueSeparatorDefault() {
        OptionBuilder.withValueSeparator();
        // Assert that valuesep has been set correctly
        Assert.assertEquals('=', OptionBuilder.valuesep);
    }

    @Test
    public void testIsRequiredWithCondition() {
        OptionBuilder.isRequired(true);
        // Assert that required has been set correctly
        Assert.assertTrue(OptionBuilder.required);

        OptionBuilder.isRequired(false);
        // Assert that required has been set correctly
        Assert.assertFalse(OptionBuilder.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        // Assert that numberOfArgs has been set correctly
        Assert.assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgsWithNumber() {
        OptionBuilder.hasArgs(2);
        // Assert that numberOfArgs has been set correctly
        Assert.assertEquals(2, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        // Assert that numberOfArgs has been set correctly
        Assert.assertEquals(1, OptionBuilder.numberOfArgs);
        // Assert that optionalArg has been set correctly
        Assert.assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        // Assert that numberOfArgs has been set correctly
        Assert.assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        // Assert that optionalArg has been set correctly
        Assert.assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgsWithNumber() {
        OptionBuilder.hasOptionalArgs(3);
        // Assert that numberOfArgs has been set correctly
        Assert.assertEquals(3, OptionBuilder.numberOfArgs);
        // Assert that optionalArg has been set correctly
        Assert.assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        // Assert that type has been set correctly
        Assert.assertEquals(String.class, OptionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("testDescription");
        // Assert that description has been set correctly
        Assert.assertEquals("testDescription", OptionBuilder.description);
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        // Assert that the option has been created correctly
        Assert.assertEquals("t", option.getOpt());
    }

    @Test
    public void testCreateWithoutOpt() {
        try {
            OptionBuilder.create();
        } catch (IllegalArgumentException e) {
            // Assert that the correct exception is thrown
            Assert.assertEquals("must specify longopt", e.getMessage());
        }
    }

    @Test
    public void testCreateWithOpt() {
        Option option = OptionBuilder.create("testOpt");
        // Assert that the option has been created correctly
        Assert.assertEquals("testOpt", option.getOpt());
    }

}