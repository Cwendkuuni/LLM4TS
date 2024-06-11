import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.cli.CommonProps.*;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        String[] args = new String[]{"java", "-Xmx512m", "--foo", "-o", "out.txt", "--bar=value"};
        Options options = new Options();
        options.addOption(OPTION_LONG_FOO, OPTION_SHORT_F, "foo", "Option for foo");
        options.addOption(OPTION_LONG_OVERWRITE_OUTPUT_FILE, OPTION_SHORT_O, "o", "Option to overwrite output file");
        options.addOption(OPTION_LONG_BAR, OPTION_SHORT_B, "bar", "Option for bar with value");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"java", "--foo", "-Xmx512m", "--", "-o", "out.txt", "--bar=value"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        String[] args = new String[]{"java", "--invalid", "option"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
        fail("Should throw ParseException for invalid option");
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = new String[]{"java", "--foo", "-Xmx512m", "--", "--bar=value"};
        Options options = new Options();
        options.addOption(OPTION_LONG_FOO, OPTION_SHORT_F, "foo", "Option for foo");
        options.addOption(OPTION_LONG_BAR, OPTION_SHORT_B, "bar", "Option for bar with value");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"java", "--foo", "--", "--bar=value"}, result);
    }

    @Test
    public void testFlattenBurstedOption() {
        String[] args = new String[]{"java", "--long-option-with-value"};
        Options options = new Options();
        options.addOption(OPTION_LONG_LONG_OPTION_WITH_VALUE, "", "option", "Option with long id and value");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"java", "--", "--long-option-with-value"}, result);
    }
}