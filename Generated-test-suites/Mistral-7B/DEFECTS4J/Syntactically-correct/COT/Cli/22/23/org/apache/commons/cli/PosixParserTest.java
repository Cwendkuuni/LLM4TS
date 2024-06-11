import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class PosixParserTest
{
    @Test
    public void testFlatten_EmptyArguments()
    {
        PosixParser parser = new PosixParser();
        Options options = mock(Options.class);
        String[] arguments = new String[0];

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlatten_SingleOption()
    {
        PosixParser parser = new PosixParser();
        Options options = mock(Options.class);
        String[] arguments = new String[]{"--option"};

        when(options.hasOption("option")).thenReturn(true);

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--option"}, result);
    }

    @Test
    public void testFlatten_SingleValueOption()
    {
        PosixParser parser = new PosixParser();
        Options options = mock(Options.class);
        String[] arguments = new String[]{"--option=value"};

        when(options.hasOption("option")).thenReturn(true);
        when(options.getOption("option")).thenReturn(new Option("option", "option", true, "option description"));

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--option", "value"}, result);
    }

    @Test
    public void testFlatten_MultipleOptions()
    {
        PosixParser parser = new PosixParser();
        Options options = mock(Options.class);
        String[] arguments = new String[]{"--option1", "--option2", "--option3=value3"};

        when(options.hasOption("option1")).thenReturn(true);
        when(options.hasOption("option2")).thenReturn(true);
        when(options.getOption("option3")).thenReturn(new Option("option3", "option3", true, "option3 description"));

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--option1", "--option2", "--option3", "value3"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOption()
    {
        PosixParser parser = new PosixParser();
        Options options = mock(Options.class);
        String[] arguments = new String[]{"--option", "nonOption"};

        when(options.hasOption("option")).thenReturn(true);

        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(new String[]{"--option"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption()
    {
        PosixParser parser = new PosixParser();
        Options options = mock(Options.class);
        String[] arguments = new String[]{"--invalidOption", "value"};

        when(options.hasOption("invalidOption")).thenReturn(false);

        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_MissingOption()
    {
        PosixParser parser = new PosixParser();
        Options options = mock(Options.class);
        String[] arguments = new String[]{"--option", "--missingOption"};

        when(options.hasOption("option")).thenReturn(true);

        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_MissingValueOption()
    {
        PosixParser parser = new PosixParser();
        Options options = mock(Options.class);
        String[] arguments = new String[]{"--option"};

        when(options.hasOption("option")).thenReturn(true);
        when(options.getOption("option")).thenReturn(new Option("option", "option", true, "option description"));

        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidValueOption()
    {
        PosixParser parser = new PosixParser();
        Options options = mock(Options.class);
        String[] arguments = new String[]{"--option=invalidValue"};

        when(options.hasOption("option")).thenReturn(true);
        when(options.getOption("option")).thenReturn(new Option("option", "option", true, "option description"));

        parser.flatten(options, arguments, false);
    }
}