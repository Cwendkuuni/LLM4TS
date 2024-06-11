import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PosixParserTest
{
    @Test
    public void testFlattenEmptyArgs() throws ParseException
    {
        Options options = new Options();
        String[] args = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(0, result.length);
    }

    @Test
    public void testFlattenNoOptions() throws ParseException
    {
        Options options = new Options();
        String[] args = {"-a", "-b", "-c"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-a", "-b", "-c"}, result);
    }

    @Test
    public void testFlattenOptionsWithValues() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "a");
        options.addOption("b", true, "b");
        String[] args = {"--a", "--b=1", "--c"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--a", "--b", "--c"}, result);
    }

    @Test
    public void testFlattenLongOptions() throws ParseException
    {
        Options options = new Options();
        options.addOption("longOption", true, "longOptionValue");
        String[] args = {"--longOption", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--longOption", "value"}, result);
    }

    @Test
    public void testFlattenLongOptionWithValue() throws ParseException
    {
        Options options = new Options();
        options.addOption("longOption", true, "longOptionValue");
        String[] args = {"--longOption", "value1", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--longOption", "value1", "value2"}, result);
    }

    @Test
    public void testFlattenInvalidOption() throws ParseException
    {
        Options options = new Options();
        String[] args = {"--invalid"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--invalid"}, result);
    }

    @Test
    public void testFlattenNoOptionsAfterNonOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "a");
        String[] args = {"--nonOption", "-a", "--option"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"--nonOption", "-a", "--option"}, result);
    }

    @Test
    public void testFlattenSingleDashAsOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("singleDash", false);
        String[] args = {"-"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertTrue(result.length > 0);
        assertSame(options.getOption("singleDash"), result[0]);
    }

    @Test
    public void testFlattenSingleDashAsNonOption() throws ParseException
    {
        Options options = new Options();
        String[] args = {"-"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlattenTwoDashAsOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("twoDashes", false);
        String[] args = {"--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertTrue(result.length > 0);
        assertSame(options.getOption("twoDashes"), result[0]);
    }

    @Test
    public void testFlattenTwoDashesAsSeparator() throws ParseException
    {
        Options options = new Options();
        String[] args = {"--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidLongOptionWithValue() throws ParseException
    {
        Options options = new Options();
        options.addOption("invalidLongOption", true, "invalidLongOptionValue");
        String[] args = {"--invalidLongOption=value"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidShortOptionWithValue() throws ParseException
    {
        Options options = new Options();
        options.addOption("i", true, "iValue");
        String[] args = {"-i=value"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }
}