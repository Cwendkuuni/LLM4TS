import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt");
        Assert.assertEquals(optionBuilder, OptionBuilder.instance);
        // Typical Use Case
        Assert.assertEquals("testLongOpt", OptionBuilder.longopt);
        // Edge Case
        Assert.assertNotEquals(null, OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Assert.assertEquals(optionBuilder, OptionBuilder.instance);
        // Typical Use Case
        Assert.assertEquals(1, OptionBuilder.numberOfArgs);
        // Edge Case
        Assert.assertNotEquals(0, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgWithFlag() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        Assert.assertEquals(optionBuilder, OptionBuilder.instance);
        // Typical Use Case
        Assert.assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        // Edge Case
        Assert.assertNotEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("testArgName");
        Assert.assertEquals(optionBuilder, OptionBuilder.instance);
        // Typical Use Case
        Assert.assertEquals("testArgName", OptionBuilder.argName);
        // Edge Case
        Assert.assertNotEquals(null, OptionBuilder.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Assert.assertEquals(optionBuilder, OptionBuilder.instance);
        // Typical Use Case
        Assert.assertTrue(OptionBuilder.required);
        // Edge Case
        Assert.assertNotEquals(false, OptionBuilder.required);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Assert.assertEquals(optionBuilder, OptionBuilder.instance);
        // Typical Use Case
        Assert.assertEquals(':', OptionBuilder.valuesep);
        // Edge Case
        Assert.assertNotEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testWithDefaultValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        Assert.assertEquals(optionBuilder, OptionBuilder.instance);
        // Typical Use Case
        Assert.assertEquals('=', OptionBuilder.valuesep);
        // Edge Case
        Assert.assertNotEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testIsRequiredWithFlag() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
        Assert.assertEquals(optionBuilder, OptionBuilder.instance);
        // Typical Use Case
        Assert.assertFalse(OptionBuilder.required);
        // Edge Case
        Assert.assertNotEquals(true, OptionBuilder.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Assert.assertEquals(optionBuilder, OptionBuilder.instance);
        // Typical Use Case
        Assert.assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        // Edge Case
        Assert.assertNotEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgsWithValue() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Assert.assertEquals(optionBuilder, OptionBuilder.instance);
        // Typical Use Case
        Assert.assertEquals(2, OptionBuilder.numberOfArgs);
        // Edge Case
        Assert.assertNotEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Assert.assertEquals(optionBuilder, OptionBuilder.instance);
        // Typical Use Case
        Assert.assertEquals(1, OptionBuilder.numberOfArgs);
        Assert.assertTrue(OptionBuilder.optionalArg);
        // Edge Case
        Assert.assertNotEquals(0, OptionBuilder.numberOfArgs);
        Assert.assertNotEquals(false, OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Assert.assertEquals(optionBuilder, OptionBuilder.instance);
        // Typical Use Case
        Assert.assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        Assert.assertTrue(OptionBuilder.optionalArg);
        // Edge Case
        Assert.assertNotEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        Assert.assertNotEquals(false, OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgsWithValue() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        Assert.assertEquals(optionBuilder, OptionBuilder.instance);
        // Typical Use Case
        Assert.assertEquals(3, OptionBuilder.numberOfArgs);
        Assert.assertTrue(OptionBuilder.optionalArg);
        // Edge Case
        Assert.assertNotEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        Assert.assertNotEquals(false, OptionBuilder.optionalArg);
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Assert.assertEquals(optionBuilder, OptionBuilder.instance);
        // Typical Use Case
        Assert.assertEquals(Integer.class, OptionBuilder.type);
        // Edge Case
        Assert.assertNotEquals(null, OptionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("testDescription");
        Assert.assertEquals(optionBuilder, OptionBuilder.instance);
        // Typical Use Case
        Assert.assertEquals("testDescription", OptionBuilder.description);
        // Edge Case
        Assert.assertNotEquals(null, OptionBuilder.description);
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        // Typical Use Case
        Assert.assertEquals("t", option.getOpt());
        Assert.assertEquals(null, option.getDescription());
        Assert.assertEquals(null, option.getLongOpt());
        Assert.assertEquals(false, option.isRequired());
        Assert.assertEquals(false, option.hasOptionalArg());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertEquals(null, option.getType());
        Assert.assertEquals((char) 0, option.getValueSeparator());
        Assert.assertEquals(null, option.getArgName());
        // Edge Case
        Assert.assertNotEquals(' ', option.getOpt());
        Assert.assertNotEquals(0, option.getArgs());
    }

    @Test
    public void testCreateWithoutOpt() {
        // Edge Case
        try {
            Option option = OptionBuilder.create();
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("must specify longopt", e.getMessage());
        }
    }

    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("testOption");
        // Typical Use Case
        Assert.assertEquals("testOption", option.getOpt());
        // Edge Case
        Assert.assertNotEquals(null, option.getOpt());
    }
}