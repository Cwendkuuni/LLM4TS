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
    public void setUp()
    {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
    }

    @Test
    public void testFlattenEmptyArguments()
    {
        String[] arguments = new String[0];
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenSingleArgument()
    {
        String[] arguments = new String[]{"arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"arg"}, result);
    }

    @Test
    public void testFlattenSingleShortOption()
    {
        String[] arguments = new String[]{"-a"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-a"}, result);
    }

    @Test
    public void testFlattenSingleLongOption()
    {
        String[] arguments = new String[]{"--optionA"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--optionA"}, result);
    }

    @Test
    public void testFlattenSingleShortOptionWithArgument()
    {
        String[] arguments = new String[]{"-a", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-a", "arg"}, result);
    }

    @Test
    public void testFlattenSingleLongOptionWithArgument()
    {
        String[] arguments = new String[]{"--optionA", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--optionA", "arg"}, result);
    }

    @Test
    public void testFlattenMultipleOptions()
    {
        String[] arguments = new String[]{"-a", "--optionB", "-c=value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-a", "--optionB", "-c=value"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenUnknownOption()
    {
        String[] arguments = new String[]{"--unknownOption"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlattenStopAtNonOption()
    {
        String[] arguments = new String[]{"-a", "--unknownOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(new String[]{"-a", "--unknownOption"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption()
    {
        String[] arguments = new String[]{"-a", "--unknownOption", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(new String[]{"-a", "--unknownOption", "arg"}, result);
    }

    @Test
    public void testFlattenBurstOption()
    {
        String[] arguments = new String[]{"--longOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--longOption"}, result);
    }

    @Test
    public void testFlattenBurstOptionWithArgument()
    {
        String[] arguments = new String[]{"--longOptionValue"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--longOption", "Value"}, result);
    }
}