import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder builder = OptionBuilder.withLongOpt("testLongOpt");
        Assert.assertEquals("testLongOpt", OptionBuilder.getLongopt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder builder = OptionBuilder.hasArg();
        Assert.assertEquals(1, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithArg() {
        OptionBuilder builder = OptionBuilder.hasArg(true);
        Assert.assertEquals(1, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder builder = OptionBuilder.withArgName("testArgName");
        Assert.assertEquals("testArgName", OptionBuilder.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder builder = OptionBuilder.isRequired();
        Assert.assertTrue(OptionBuilder.isRequired());
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder builder = OptionBuilder.withValueSeparator(':');
        Assert.assertEquals(':', OptionBuilder.getValuesep());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder builder = OptionBuilder.withValueSeparator();
        Assert.assertEquals('=', OptionBuilder.getValuesep());
    }

    @Test
    public void testIsRequiredBoolean() {
        OptionBuilder builder = OptionBuilder.isRequired(true);
        Assert.assertTrue(OptionBuilder.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder builder = OptionBuilder.hasArgs();
        Assert.assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgsInt() {
        OptionBuilder builder = OptionBuilder.hasArgs(2);
        Assert.assertEquals(2, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder builder = OptionBuilder.hasOptionalArg();
        Assert.assertEquals(1, OptionBuilder.getNumberOfArgs());
        Assert.assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder builder = OptionBuilder.hasOptionalArgs();
        Assert.assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
        Assert.assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgsInt() {
        OptionBuilder builder = OptionBuilder.hasOptionalArgs(3);
        Assert.assertEquals(3, OptionBuilder.getNumberOfArgs());
        Assert.assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder builder = OptionBuilder.withType(Integer.class);
        Assert.assertEquals(Integer.class, OptionBuilder.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder builder = OptionBuilder.withDescription("testDescription");
        Assert.assertEquals("testDescription", OptionBuilder.getDescription());
    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('t');
        Assert.assertEquals("t", option.getOpt());
    }

    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("testLongOpt");
        Option option = OptionBuilder.create();
        Assert.assertEquals("testLongOpt", option.getLongOpt());
    }

    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("t");
        Assert.assertEquals("t", option.getOpt());
    }
}