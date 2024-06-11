import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("option");
        // Additional Assertions
        Assert.assertEquals("option", OptionBuilder.getLongOpt());
        // Reset OptionBuilder
        OptionBuilder.reset();
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        // Additional Assertions
        Assert.assertEquals(1, OptionBuilder.getNumberOfArgs());
        // Reset OptionBuilder
        OptionBuilder.reset();
    }

    @Test
    public void testHasArgBoolean() {
        OptionBuilder.hasArg(true);
        // Additional Assertions
        Assert.assertEquals(1, OptionBuilder.getNumberOfArgs());
        // Reset OptionBuilder
        OptionBuilder.reset();
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("argName");
        // Additional Assertions
        Assert.assertEquals("argName", OptionBuilder.getArgName());
        // Reset OptionBuilder
        OptionBuilder.reset();
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        // Additional Assertions
        Assert.assertTrue(OptionBuilder.isRequired());
        // Reset OptionBuilder
        OptionBuilder.reset();
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        // Additional Assertions
        Assert.assertEquals(':', OptionBuilder.getValueSeparator());
        // Reset OptionBuilder
        OptionBuilder.reset();
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        // Additional Assertions
        Assert.assertEquals('=', OptionBuilder.getValueSeparator());
        // Reset OptionBuilder
        OptionBuilder.reset();
    }

    @Test
    public void testIsRequiredBoolean() {
        OptionBuilder.isRequired(true);
        // Additional Assertions
        Assert.assertTrue(OptionBuilder.isRequired());
        // Reset OptionBuilder
        OptionBuilder.reset();
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        // Additional Assertions
        Assert.assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
        // Reset OptionBuilder
        OptionBuilder.reset();
    }

    @Test
    public void testHasArgsInt() {
        OptionBuilder.hasArgs(3);
        // Additional Assertions
        Assert.assertEquals(3, OptionBuilder.getNumberOfArgs());
        // Reset OptionBuilder
        OptionBuilder.reset();
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        // Additional Assertions
        Assert.assertTrue(OptionBuilder.isOptionalArg());
        Assert.assertEquals(1, OptionBuilder.getNumberOfArgs());
        // Reset OptionBuilder
        OptionBuilder.reset();
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        // Additional Assertions
        Assert.assertTrue(OptionBuilder.isOptionalArg());
        Assert.assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
        // Reset OptionBuilder
        OptionBuilder.reset();
    }

    @Test
    public void testHasOptionalArgsInt() {
        OptionBuilder.hasOptionalArgs(2);
        // Additional Assertions
        Assert.assertTrue(OptionBuilder.isOptionalArg());
        Assert.assertEquals(2, OptionBuilder.getNumberOfArgs());
        // Reset OptionBuilder
        OptionBuilder.reset();
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        // Additional Assertions
        Assert.assertEquals(String.class, OptionBuilder.getType());
        // Reset OptionBuilder
        OptionBuilder.reset();
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        // Additional Assertions
        Assert.assertEquals("description", OptionBuilder.getDescription());
        // Reset OptionBuilder
        OptionBuilder.reset();
    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('a');
        // Additional Assertions
        Assert.assertEquals("a", option.getOpt());
        Assert.assertNull(option.getLongOpt());
        Assert.assertNull(option.getValueSeparator());
        Assert.assertEquals(0, option.getArgs());
        Assert.assertFalse(option.hasArg());
        // Reset OptionBuilder
        OptionBuilder.reset();
    }

    @Test
    public void testCreate() {
        Option option = OptionBuilder.create();
        // Additional Assertions
        Assert.assertNull(option.getOpt());
        Assert.assertNotNull(option.getLongOpt());
        Assert.assertNull(option.getValueSeparator());
        Assert.assertEquals(0, option.getArgs());
        Assert.assertFalse(option.hasArg());
        // Reset OptionBuilder
        OptionBuilder.reset();
    }

    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("option");
        // Additional Assertions
        Assert.assertEquals("option", option.getOpt());
        Assert.assertNull(option.getLongOpt());
        Assert.assertNull(option.getValueSeparator());
        Assert.assertEquals(0, option.getArgs());
        Assert.assertFalse(option.hasArg());
        // Reset OptionBuilder
        OptionBuilder.reset();
    }

    // Additional test cases for potential edge cases and exception handling scenarios
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testWithOptions() {
        // Test creating an option with multiple options
        Option option = OptionBuilder.withLongOpt("option")
                                   .hasArg()
                                   .withArgName("argName")
                                   .isRequired()
                                   .create();

        // Additional Assertions
        Assert.assertEquals("option", option.getLongOpt());
        Assert.assertEquals(1, option.getArgs());
        Assert.assertTrue(option.hasArg());
        Assert.assertTrue(option.isRequired());
        // Reset OptionBuilder
        OptionBuilder.reset();
    }
}