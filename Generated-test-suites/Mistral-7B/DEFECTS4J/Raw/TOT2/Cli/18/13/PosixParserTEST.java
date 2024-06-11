import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArgs() {
        String[] args = {};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlattenSingleArg() {
        String[] args = {"arg1"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"arg1"}, result);
    }

    @Test
    public void testFlattenSingleOption() {
        String[] args = {"-o", "output.txt"};
        Options options = new Options().addOption("o", true, "output file");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-o", "output.txt"}, result);
    }

    @Test
    public void testFlattenMultipleOptions() {
        String[] args = {"-a", "-b", "--optionC=valueC"};
        Options options = new Options()
                .addOption("a", true, "optionA")
                .addOption("b", true, "optionB")
                .addOption("c", true, "optionC");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a", "-b", "--optionC=valueC"}, result);
    }

    @Test
    public void testFlattenLongOption() {
        String[] args = {"--longOption=value"};
        Options options = new Options().addOption("longOption", true, "long option");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--longOption=value"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        String[] args = {"--invalidOption"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = {"arg1", "--", "arg3", "arg4"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"arg1", "--", "arg3", "arg4"}, result);
    }
}