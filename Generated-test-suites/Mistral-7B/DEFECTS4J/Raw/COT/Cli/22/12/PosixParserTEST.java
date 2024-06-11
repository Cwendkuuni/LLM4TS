import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PosixParserTest
{
    @Test
    public void testFlatten_EmptyArguments_NoOptions()
    {
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_EmptyArguments_ValidOption()
    {
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_SingleArgument_NoOptions()
    {
        String[] arguments = {"arg"};
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("arg", result[0]);
    }

    @Test
    public void testFlatten_SingleArgument_ValidOption()
    {
        String[] arguments = {"arg"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_MultipleArguments_NoOptions()
    {
        String[] arguments = {"arg1", "arg2"};
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("arg1", result[0]);
        assertEquals("arg2", result[1]);
    }

    @Test
    public void testFlatten_MultipleArguments_ValidOption()
    {
        String[] arguments = {"arg1", "arg2"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption_StopAtNonOption()
    {
        String[] arguments = {"--invalid", "arg"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");

        parser.flatten(options, arguments, true);

        assertNull(options); // Should throw ParseException
    }

    @Test
    public void testFlatten_LongOption_StopAtNonOption()
    {
        String[] arguments = {"--long-option", "arg"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("lo", true, "Long option");

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("--long-option", result[0]);
    }

    @Test
    public void testFlatten_LongOptionWithValue_StopAtNonOption()
    {
        String[] arguments = {"--long-option=value", "arg"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("lo", true, "Long option");

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("--long-option=value", result[0]);
    }

    @Test
    public void testFlatten_LongOptionWithValue_NoStopAtNonOption()
    {
        String[] arguments = {"--long-option=value", "arg"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("lo", true, "Long option");

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("--lo", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_SingleDash_StopAtNonOption()
    {
        String[] arguments = {"-"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] result = parser.flatten(options, arguments, true);

        assertNull(result);
    }

    @Test
    public void testFlatten_SingleDash_NoStopAtNonOption()
    {
        String[] arguments = {"-"};
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_TwoDash_StopAtNonOption()
    {
        String[] arguments = {"--"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] result = parser.flatten(options, arguments, true);

        assertNull(result);
    }

    @Test
    public void testFlatten_TwoDash_NoStopAtNonOption()
    {
        String[] arguments = {"--"};
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("--", result[0]);
    }

    @Test
    public void testFlatten_ProperlyFormattedOptions()
    {
        String[] arguments = {"--option1", "--option2=value", "--option3", "--option4=value2"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o1", true, "Option 1");
        options.addOption("o2", true, "Option 2");
        options.addOption("o3", false, "Option 3");
        options.addOption("o4", true, "Option 4");

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(4, result.length);
        assertEquals("o1", result[0]);
        assertEquals("o2", result[1]);
        assertEquals("o3", result[2]);
        assertEquals("o4", result[3]);
    }
}