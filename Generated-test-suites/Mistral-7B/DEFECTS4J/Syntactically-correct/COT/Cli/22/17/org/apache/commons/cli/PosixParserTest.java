import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class PosixParserTest
{
    private Options options;

    @Before
    public void setup()
    {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C with arg");
        options.addOption("d", 4, "Option D with arg");
    }

    @Test
    public void testFlatten_EmptyArguments()
    {
        String[] args = {};
        PosixParser parser = new PosixParser();
        String[] expected = {};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleOption()
    {
        String[] args = {"--a"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--a"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleValueOption()
    {
        String[] args = {"--a", "value"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--a", "value"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionWithArg()
    {
        String[] args = {"--b", "arg"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--b", "arg"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_UnknownOption()
    {
        String[] args = {"--x", "arg"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_StopAtNonOption()
    {
        String[] args = {"--", "arg1", "--b", "arg2", "--"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--", "arg1", "--b", "arg2"};
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleHyphen()
    {
        String[] args = {"-"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleHyphens()
    {
        String[] args = {"--", "-", "--"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--", "-", "--"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOption()
    {
        String[] args = {"--longOption"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--longOption"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithArg()
    {
        String[] args = {"--longOption", "value"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--longOption", "value"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ShortOption()
    {
        String[] args = {"-a"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-a"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ShortOptionWithArg()
    {
        String[] args = {"-a", "value"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-a", "value"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ShortOptionWithArgAndStopAtNonOption()
    {
        String[] args = {"-a", "--", "arg2"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-a", "--", "arg2"};
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ShortOptionWithMultipleArgs()
    {
        String[] args = {"-a", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-a", "arg1", "arg2"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ShortOptionWithMultipleArgsAndStopAtNonOption()
    {
        String[] args = {"-a", "arg1", "--", "arg3"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-a", "arg1", "--", "arg3"};
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleOptions()
    {
        String[] args = {"--a", "--b"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--a", "--b"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleOptionsWithArgs()
    {
        String[] args = {"--a", "arg1", "--b", "arg2"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--a", "arg1", "--b", "arg2"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MixedOptions()
    {
        String[] args = {"--a", "--b=value", "-a", "--c", "-c", "arg"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--a", "--b=value", "-a", "--c", "-c", "arg"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken()
    {
        PosixParser parser = new PosixParser();
        List tokens = parser.burstToken("-ca", false);
        List expected = Arrays.asList("-c", "-a");
        Assert.assertEquals(expected, tokens);

        tokens = parser.burstToken("-c--d", false);
        expected = Arrays.asList("-c", "--", "d");
        Assert.assertEquals(expected, tokens);

        tokens = parser.burstToken("-c--d--e", false);
        expected = Arrays.asList("-c", "--", "d", "--", "e");
        Assert.assertEquals(expected, tokens);

        tokens = parser.burstToken("-ca--d", false);
        expected = Arrays.asList("-c", "-a", "--", "d");
        Assert.assertEquals(expected, tokens);

        tokens = parser.burstToken("-ca--d--e", false);
        expected = Arrays.asList("-c", "-a", "--", "d", "--", "e");
        Assert.assertEquals(expected, tokens);

        tokens = parser.burstToken("-c--d", true);
        expected = Arrays.asList("-c", "--", "d");
        Assert.assertEquals(expected, tokens);

        tokens = parser.burstToken("-ca--d--e", true);
        expected = Arrays.asList("-c", "-a", "--", "d", "--", "e");
        Assert.assertEquals(expected, tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBurstToken_Null()
    {
        PosixParser parser = new PosixParser();
        parser.burstToken(null, false);
    }
}