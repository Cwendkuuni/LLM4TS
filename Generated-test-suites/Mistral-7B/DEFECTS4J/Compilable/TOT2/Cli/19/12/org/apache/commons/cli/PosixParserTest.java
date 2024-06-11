package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    /**
     * Test flatten method with simple options.
     */
    @Test
    public void testSimpleOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");

        String[] arguments = {"-a", "--b"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"-a", "--b"}, parsedArgs);
    }

    /**
     * Test flatten method with long option and value.
     */
    @Test
    public void testLongOptionWithValue() {
        Options options = new Options();
        options.addOption("longOption", true, "Long Option with value");

        String[] arguments = {"--longOption=value"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"--longOption=value"}, parsedArgs);
    }

    /**
     * Test flatten method with multiple long options with values.
     */
    @Test
    public void testMultipleLongOptionsWithValues() {
        Options options = new Options();
        options.addOption("longOption1", true, "Long Option 1 with value");
        options.addOption("longOption2", false, "Long Option 2 without value");
        options.addOption("longOption3", true, "Long Option 3 with value");

        String[] arguments = {"--longOption1=value1", "--longOption2", "--longOption3=value3"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"--longOption1=value1", "--longOption2", "--longOption3=value3"}, parsedArgs);
    }

    /**
     * Test flatten method with stopAtNonOption.
     */
    @Test
    public void testStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");

        String[] arguments = {"-a", "-b", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(new String[]{"-a", "-b"}, parsedArgs);
    }

    /**
     * Test bursting method with multiple short options.
     */
    @Test
    public void testBurstingWithMultipleShortOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");

        String[] arguments = {"-ab", "--longOption"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"-a", "--b", "--longOption"}, parsedArgs);
    }

    /**
     * Test bursting method with long option and short option.
     */
    @Test
    public void testBurstingWithLongAndShortOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("lo", true, "Long Option");

        String[] arguments = {"--lo=value", "-a"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"--lo=value", "--lo", "-a"}, parsedArgs);
    }

    /**
     * Test bursting method with multiple long options.
     */
    @Test
    public void testBurstingWithMultipleLongOptions() {
        Options options = new Options();
        options.addOption("lo1", true, "Long Option 1");
        options.addOption("lo2", true, "Long Option 2");

        String[] arguments = {"--lo1=value1", "--lo2=value2"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"--lo1=value1", "--lo2=value2"}, parsedArgs);
    }

    /**
     * Test bursting method with multiple options with values.
     */
    @Test
    public void testBurstingWithMultipleOptionsWithValues() {
        Options options = new Options();
        options.addOption("o1", true, "Option 1 with value");
        options.addOption("o2", true, "Option 2 with value");

        String[] arguments = {"--o1=value1", "--o2=value2"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"--o1=value1", "--o2=value2"}, parsedArgs);
    }

    /**
     * Test bursting method with multiple options and values and non-options.
     */
    @Test
    public void testBurstingWithMultipleOptionsValuesAndNonOptions() {
        Options options = new Options();
        options.addOption("o1", true, "Option 1 with value");
        options.addOption("o2", true, "Option 2 with value");

        String[] arguments = {"--o1=value1", "--o2=value2", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"--o1=value1", "--o2=value2"}, parsedArgs);
    }
}