import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.PosixParser;

public class PosixParserTest {

    @Test
    public void testFlattenWithOptions() throws ParseException {
        String[] args = {"--option1", "-o1", "value1", "--option2", "-o2", "value2"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--option1", "--option2"}, result);
        Assert.assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlattenWithSingleHyphen() {
        String[] args = {"-", "--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(null, args, false);

        Assert.assertArrayEquals(new String[]{"-", "--"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() {
        String[] args = {"--invalidOption"};
        PosixParser parser = new PosixParser();
        parser.flatten(null, args, false);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = {"--option1", "invalidOption", "--option2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(null, args, true);

        Assert.assertArrayEquals(new String[]{"--option1", "invalidOption", "--option2"}, result);
    }

    @Test
    public void testBurstingOption() {
        String[] args = {"--longOption", "value1", "--longOption", "value2"};
        Options options = new Options();
        options.addOption("longOption", true, "lv");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--longOption", "--longOption"}, result);
        Assert.assertEquals(options.getOption("longOption"), parser.getCurrentOption());
    }

    @Test
    public void testBurstingOptionWithValue() {
        String[] args = {"--longOption", "value1", "--longOption", "value2"};
        Options options = new Options();
        options.addOption("longOption", true, "lv");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--longOption=value1", "--longOption=value2"}, result);
        Assert.assertEquals(options.getOption("longOption"), parser.getCurrentOption());
    }

    @Test
    public void testBurstingOptionWithValueAndMultipleArguments() {
        String[] args = {"--longOption=value1,value2", "--longOption", "value3"};
        Options options = new Options();
        options.addOption("longOption", true, "lv");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--longOption=value1,value2", "--longOption"}, result);
        Assert.assertEquals(options.getOption("longOption"), parser.getCurrentOption());
    }

    @Test
    public void testBurstingInvalidOption() {
        String[] args = {"--invalidOption", "--longOption", "value1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(null, args, false);

        Assert.assertArrayEquals(new String[]{"--invalidOption", "--longOption"}, result);
        Assert.assertNull(parser.getCurrentOption());
    }
}