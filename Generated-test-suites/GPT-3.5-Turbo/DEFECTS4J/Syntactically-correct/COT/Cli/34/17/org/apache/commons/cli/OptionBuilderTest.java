import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("option");
        Assert.assertEquals("option", OptionBuilder.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Assert.assertEquals(1, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithValue() {
        OptionBuilder.hasArg(true);
        Assert.assertEquals(1, OptionBuilder.getNumberOfArgs());
        OptionBuilder.hasArg(false);
        Assert.assertEquals(Option.UNINITIALIZED, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("name");
        Assert.assertEquals("name", OptionBuilder.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Assert.assertTrue(OptionBuilder.isRequired());
    }

    @Test
    public void testWithIsRequired() {
        OptionBuilder.isRequired(false);
        Assert.assertFalse(OptionBuilder.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Assert.assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgsWithValue() {
        OptionBuilder.hasArgs(3);
        Assert.assertEquals(3, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Assert.assertEquals(1, OptionBuilder.getNumberOfArgs());
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
        OptionBuilder.hasOptionalArgs(2);
        Assert.assertEquals(2, OptionBuilder.getNumberOfArgs());
        Assert.assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        Assert.assertEquals(String.class, OptionBuilder.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        Assert.assertEquals("description", OptionBuilder.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithLongOptAndDescription() {
        Option option = OptionBuilder.withLongOpt("option").withDescription("description").create();
        Assert.assertEquals("option", option.getLongOpt());
        Assert.assertEquals("description", option.getDescription());
    }

    @Test
    public void testCreateWithCharacter() {
        Option option = OptionBuilder.create('o');
        Assert.assertEquals("o", option.getOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidCharacter() {
        OptionBuilder.create('$');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullOpt() {
        OptionBuilder.create(null);
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test
    public void testWithLongOptNull() {
        OptionBuilder.withLongOpt(null);
        Assert.assertNull(OptionBuilder.getLongOpt());
    }

    @Test
    public void testWithArgNameNull() {
        OptionBuilder.withArgName(null);
        Assert.assertNull(OptionBuilder.getArgName());
    }

    @Test
    public void testWithDescriptionNull() {
        OptionBuilder.withDescription(null);
        Assert.assertNull(OptionBuilder.getDescription());
    }

    @Test
    public void testWithDescriptionEmpty() {
        OptionBuilder.withDescription("");
        Assert.assertEquals("", OptionBuilder.getDescription());
    }

    @Test
    public void testWithRequiredFalse() {
        OptionBuilder.isRequired(false);
        Assert.assertFalse(OptionBuilder.isRequired());
    }

    @Test
    public void testWithRequiredDefaultValue() {
        Assert.assertFalse(OptionBuilder.isRequired());
    }

    @Test
    public void testWithArgsNegativeValue() {
        OptionBuilder.hasArgs(-1);
        Assert.assertEquals(-1, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithOptionalArgsNegativeValue() {
        OptionBuilder.hasOptionalArgs(-1);
        Assert.assertEquals(-1, OptionBuilder.getNumberOfArgs());
        Assert.assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testWithOptionalArgsZeroValue() {
        OptionBuilder.hasOptionalArgs(0);
        Assert.assertEquals(0, OptionBuilder.getNumberOfArgs());
        Assert.assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testCreateMultipleTimes() {
        OptionBuilder.withLongOpt("option1");
        Option option1 = OptionBuilder.create();
        OptionBuilder.withLongOpt("option2");
        Option option2 = OptionBuilder.create();
        Assert.assertEquals("option1", option1.getLongOpt());
        Assert.assertEquals("option2", option2.getLongOpt());
    }

    @Test
    public void testCreateWithDefaultValueSeparator() {
        OptionBuilder.withValueSeparator();
        Option option = OptionBuilder.create('D');
        Assert.assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testCreateWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create('D');
        Assert.assertEquals(':', option.getValueSeparator());
    }
}