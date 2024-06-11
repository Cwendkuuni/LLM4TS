import org.junit.Test;
import org.junit.Assert;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("testLongOpt");
        Assert.assertEquals("testLongOpt", OptionBuilder.getLongopt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Assert.assertEquals(1, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(false);
        Assert.assertEquals(Option.UNINITIALIZED, OptionBuilder.getNumberOfArgs());
    }

    // Additional test cases for other methods should be added here

    @Test
    public void testCreate() {
        Option option = OptionBuilder.create('t');
        Assert.assertNotNull(option);
    }

    @Test
    public void testCreateWithLongOpt() {
        OptionBuilder.withLongOpt("createTest")
                    .withArgName("testArg")
                    .hasArg()
                    .isRequired();
        Option option = OptionBuilder.create();
        Assert.assertNotNull(option);
        Assert.assertEquals("createTest", option.getLongOpt());
        Assert.assertEquals("testArg", option.getArgName());
        Assert.assertEquals(1, option.getArgs());
        Assert.assertTrue(option.isRequired());
    }

    // Additional edge cases and exception handling scenarios should be tested

}