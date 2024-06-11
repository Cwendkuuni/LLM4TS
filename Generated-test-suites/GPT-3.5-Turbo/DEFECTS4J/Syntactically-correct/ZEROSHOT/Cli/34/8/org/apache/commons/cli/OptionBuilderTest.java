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
    public void testHasArg_boolean() {
        OptionBuilder.hasArg(true);
        Assert.assertEquals(1, OptionBuilder.getNumberOfArgs());
        OptionBuilder.hasArg(false);
        Assert.assertEquals(-1, OptionBuilder.getNumberOfArgs());
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
    public void testWithValueSeparator_char() {
        OptionBuilder.withValueSeparator(':');
        Assert.assertEquals(':', OptionBuilder.getValuesep());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        Assert.assertEquals('=', OptionBuilder.getValuesep());
    }

    @Test
    public void testIsRequired_boolean() {
        OptionBuilder.isRequired(true);
        Assert.assertTrue(OptionBuilder.isRequired());
        OptionBuilder.isRequired(false);
        Assert.assertFalse(OptionBuilder.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Assert.assertEquals(Integer.MAX_VALUE, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgs_int() {
        OptionBuilder.hasArgs(2);
        Assert.assertEquals(2, OptionBuilder.getNumberOfArgs());
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
        Assert.assertEquals(Integer.MAX_VALUE, OptionBuilder.getNumberOfArgs());
        Assert.assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgs_int() {
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

    @Test
    public void testCreate_char() {
        Option option = OptionBuilder.create('t');
        Assert.assertEquals("t", option.getOpt());
    }

    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("test");
        Option option = OptionBuilder.create();
        Assert.assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testCreate_String() {
        Option option = OptionBuilder.create("test");
        Assert.assertEquals("test", option.getOpt());
    }
}