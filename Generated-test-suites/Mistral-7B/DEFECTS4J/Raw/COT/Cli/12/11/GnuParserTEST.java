import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class GnuParserTest {

    @Test
    public void testFlatten_NoOptionsAndNoHyphen() {
        String[] arguments = new String[]{"arg1", "arg2"};
        Options options = new Options();
        String[] expected = new String[]{"arg1", "arg2"};

        String[] result = new GnuParser().flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleOptionWithLongName() {
        String[] arguments = new String[]{"--longOption", "arg2"};
        Options options = new Options();
        options.addOption("longOption", true, "Description");
        String[] expected = new String[]{"--longOption", "arg2"};

        String[] result = new GnuParser().flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleOptionWithShortName() {
        String[] arguments = new String[]{"-o", "arg2"};
        Options options = new Options();
        options.addOption("o", true, "Description");
        String[] expected = new String[]{"-o", "arg2"};

        String[] result = new GnuParser().flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleOptionWithValue() {
        String[] arguments = new String[]{"-Dproperty=value"};
        Options options = new Options();
        String[] expected = new String[]{"-D", "property", "value"};

        String[] result = new GnuParser().flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_NonOptionBeforeOption() {
        String[] arguments = new String[]{"arg1", "-o", "arg3"};
        Options options = new Options();
        options.addOption("o", true, "Description");

        new GnuParser().flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_NonOptionAfterOptionWithValue() {
        String[] arguments = new String[]{"-Dproperty=value", "arg2"};
        Options options = new Options();
        options.addOption("D", true, "Description"); // wrong short option

        try {
            new GnuParser().flatten(options, arguments, false);
        } catch (ParseException e) {
            Assert.assertEquals("Unrecognized option: D", e.getMessage());
            throw e;
        }
    }

    @Test(expected = ParseException.class)
    public void testFlatten_DuplicateShortOptions() {
        String[] arguments = new String[]{"-o", "-o", "arg3"};
        Options options = new Options();
        options.addOption("o", true, "Description");

        try {
            new GnuParser().flatten(options, arguments, false);
        } catch (ParseException e) {
            Assert.assertEquals("Duplicate option: -o", e.getMessage());
            throw e;
        }
    }

    @Test(expected = ParseException.class)
    public void testFlatten_DuplicateLongOptions() {
        String[] arguments = new String[]{"--longOption", "--longOption", "arg3"};
        Options options = new Options();
        options.addOption("longOption", true, "Description");

        try {
            new GnuParser().flatten(options, arguments, false);
        } catch (ParseException e) {
            Assert.assertEquals("Duplicate option: --longOption", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = new String[]{"arg1", "-o", "arg3", "arg4"};
        Options options = new Options();
        options.addOption("o", true, "Description");

        String[] expected = new String[]{"arg1", "-o"};

        String[] result = new GnuParser().flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_DoubleDash() {
        String[] arguments = new String[]{"--", "arg1", "arg2"};
        Options options = new Options();

        String[] expected = new String[]{"--", "arg1", "arg2"};

        String[] result = new GnuParser().flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_EmptyArguments() {
        String[] arguments = new String[]{};
        Options options = new Options();

        String[] expected = new String[]{};

        String[] result = new GnuParser().flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }
}