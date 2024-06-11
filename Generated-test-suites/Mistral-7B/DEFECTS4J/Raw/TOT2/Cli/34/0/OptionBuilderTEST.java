import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

    

    @Test
    public void testWithLongOpt() {
        OptionBuilder ob = OptionBuilder.withLongOpt("longOpt");
        Option option = ob.create();
        Assert.assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder ob = OptionBuilder.hasArg();
        Option option = ob.create();
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder ob = OptionBuilder.hasOptionalArg();
        Option option = ob.create();
        Assert.assertEquals(1, option.getArgs().length);
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder ob = OptionBuilder.hasArgs();
        Option option = ob.create();
        Assert.assertArrayEquals(new String[]{"value1", "value2"}, option.getArgs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithEmptyLongOpt() {
        OptionBuilder ob = OptionBuilder.create();
        ob.create(' ');
    }

    
}