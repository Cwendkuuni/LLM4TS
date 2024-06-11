import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest
{
    @Test
    public void testFlattenSimpleOption() throws ParseException
    {
        String[] args = {"--option", "value"};
        Options options = new Options();
        options.addOption("option", true, "This is an option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--option", "value"}, parsedArgs);
    }

    @Test
    public void testFlattenOptionWithValue() throws ParseException
    {
        String[] args = {"--option", "value1", "value2"};
        Options options = new Options();
        options.addOption("option", true, "This is an option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--option", "value1", "value2"}, parsedArgs);
    }

    @Test
    public void testFlattenNoOption() throws ParseException
    {
        String[] args = {"--no-option", "value"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--no-option", "value"}, parsedArgs);
    }

    @Test
    public void testFlattenLongOption() throws ParseException
    {
        String[] args = {"--long-option", "value"};
        Options options = new Options();
        options.addOption("longOption", true, "This is a long option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--long-option", "value"}, parsedArgs);
    }

    @Test
    public void testFlattenLongOptionWithValue() throws ParseException
    {
        String[] args = {"--long-option", "value1", "value2"};
        Options options = new Options();
        options.addOption("longOption", true, "This is a long option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--long-option", "value1", "value2"}, parsedArgs);
    }

    @Test
    public void testFlattenShortOption() throws ParseException
    {
        String[] args = {"-o", "value"};
        Options options = new Options();
        options.addOption("o", true, "This is a short option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-o", "value"}, parsedArgs);
    }

    @Test
    public void testFlattenShortOptionWithValue() throws ParseException
    {
        String[] args = {"-o", "value1", "value2"};
        Options options = new Options();
        options.addOption("o", true, "This is a short option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-o", "value1", "value2"}, parsedArgs);
    }

    @Test
    public void testFlattenShortOptionWithArgument() throws ParseException
    {
        String[] args = {"-o", "arg"};
        Options options = new Options();
        options.addOption("o", "arg", "This is a short option with an argument");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-o", "arg"}, parsedArgs);
    }

    @Test
    public void testFlattenMixedOptions() throws ParseException
    {
        String[] args = {"--long-option", "-o", "value", "--no-option"};
        Options options = new Options();
        options.addOption("longOption", true, "This is a long option");
        options.addOption("o", true, "This is a short option");
        options.addOption("noOption", false, "This is a no-option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--long-option", "-o", "value", "--no-option"}, parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testFlattenUnknownOption() throws ParseException
    {
        String[] args = {"--unknown-option", "value"};
        Options options = new Options();
        options.addOption("o", true, "This is an option");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenStopAtNonOption() throws ParseException
    {
        String[] args = {"--option", "value", "non-option"};
        Options options = new Options();
        options.addOption("option", true, "This is an option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"--option", "value"}, parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidToken() throws ParseException
    {
        String[] args = {"--invalid-token", "value"};
        Options options = new Options();
        options.addOption("o", true, "This is an option");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenEmptyArgumentArray() throws ParseException
    {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, new String[0], false);

        Assert.assertArrayEquals(new String[0], parsedArgs);
    }
}