import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PosixParser
 */
public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        // given
        String[] args = {"--foo", "--bar=123", "-abc", "-d", "-e=456", "--", "-f", "-g", "-h=789", "-i", "--"};
        Options options = new Options();
        Option fooOption = new Option("foo", true, true, "foo option");
        Option barOption = new Option("bar", true, true, "bar option");
        Option dOption = new Option("d", true, false, "d option");
        Option eOption = new Option("e", true, true, "e option");
        Option fOption = new Option("f", true, false, "f option");
        Option gOption = new Option("g", true, false, "g option");
        Option hOption = new Option("h", true, true, "h option");
        Option iOption = new Option("i", true, false, "i option");

        options.addOption(fooOption);
        options.addOption(barOption);
        options.addOption(dOption);
        options.addOption(eOption);
        options.addOption(fOption);
        options.addOption(gOption);
        options.addOption(hOption);
        options.addOption(iOption);

        PosixParser parser = new PosixParser();

        // when
        String[] result = parser.flatten(options, args, false);

        // then
        Assert.assertArrayEquals(new String[]{"--foo", "--bar", "123", "-abc", "--d", "-e", "456", "--", "-f", "-g", "--h", "789", "--", "-i"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() throws ParseException {
        // given
        String[] args = {"--foo", "--bar=123", "-xyz", "-d", "-e=456", "--", "-f", "-g", "--h", "789", "-i", "--invalidOption"};
        Options options = new Options();
        Option fooOption = new Option("foo", true, true, "foo option");
        Option barOption = new Option("bar", true, true, "bar option");
        Option dOption = new Option("d", true, false, "d option");
        Option eOption = new Option("e", true, true, "e option");
        Option fOption = new Option("f", true, false, "f option");
        Option gOption = new Option("g", true, false, "g option");
        Option hOption = new Option("h", true, true, "h option");
        Option iOption = new Option("i", true, false, "i option");

        options.addOption(fooOption);
        options.addOption(barOption);
        options.addOption(dOption);
        options.addOption(eOption);
        options.addOption(fOption);
        options.addOption(gOption);
        options.addOption(hOption);
        options.addOption(iOption);

        PosixParser parser = new PosixParser();

        // when and then
        parser.flatten(options, args, false);
        Assert.fail("Expected ParseException");
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        // given
        String[] args = {"--foo", "--bar=123", "-abc", "-d", "-e=456", "--", "-f", "-g", "--h", "789", "-i", "--invalidOption", "arg"};
        Options options = new Options();
        Option fooOption = new Option("foo", true, true, "foo option");
        Option barOption = new Option("bar", true, true, "bar option");
        Option dOption = new Option("d", true, false, "d option");
        Option eOption = new Option("e", true, true, "e option");
        Option fOption = new Option("f", true, false, "f option");
        Option gOption = new Option("g", true, false, "g option");
        Option hOption = new Option("h", true, true, "h option");
        Option iOption = new Option("i", true, false, "i option");

        options.addOption(fooOption);
        options.addOption(barOption);
        options.addOption(dOption);
        options.addOption(eOption);
        options.addOption(fOption);
        options.addOption(gOption);
        options.addOption(hOption);
        options.addOption(iOption);

        PosixParser parser = new PosixParser();

        // when
        String[] result = parser.flatten(options, args, true);

        // then
        Assert.assertArrayEquals(new String[]{"--foo", "--bar", "123", "-abc", "-d", "-e", "456", "--", "-f", "-g", "--h", "789", "--invalidOption"}, result);
        Assert.assertArrayEquals(new String[]{"arg"}, Arrays.copyOfRange(result, result.length - 1, result.length));
    }
}