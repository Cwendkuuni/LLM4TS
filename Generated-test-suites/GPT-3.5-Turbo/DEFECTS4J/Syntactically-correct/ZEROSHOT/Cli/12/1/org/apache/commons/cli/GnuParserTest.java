import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());

        // Test case 1: arguments is empty
        String[] arguments = new String[0];
        String[] result = parser.flatten(options, arguments, true);
        Assert.assertEquals(0, result.length);

        // Test case 2: arguments contain only non-option arguments
        arguments = new String[]{"arg1", "arg2"};
        result = parser.flatten(options, arguments, true);
        Assert.assertEquals(2, result.length);
        Assert.assertEquals("arg1", result[0]);
        Assert.assertEquals("arg2", result[1]);

        // Test case 3: arguments contain options and non-option arguments
        arguments = new String[]{"-a", "arg1", "-b", "-c", "arg2"};
        result = parser.flatten(options, arguments, true);
        Assert.assertEquals(5, result.length);
        Assert.assertEquals("-a", result[0]);
        Assert.assertEquals("arg1", result[1]);
        Assert.assertEquals("-b", result[2]);
        Assert.assertEquals("-c", result[3]);
        Assert.assertEquals("arg2", result[4]);

        // Test case 4: arguments contain options and stopAtNonOption is false
        arguments = new String[]{"-a", "arg1", "-b", "-c", "arg2"};
        result = parser.flatten(options, arguments, false);
        Assert.assertEquals(4, result.length);
        Assert.assertEquals("-a", result[0]);
        Assert.assertEquals("arg1", result[1]);
        Assert.assertEquals("-b", result[2]);
        Assert.assertEquals("-c", result[3]);
    }
}