import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }

    @After
    public void tearDown() {
        parser = null;
        options = null;
    }

    @Test
    public void testFlatten_StopAtNonOption_StopAtHyphen() {
        String[] args = {"--option1", "value1", "-abc", "value2", "--", "nonOption", "-"};
        
        options.addOption(Option.builder().longOpt("option1").hasArg().build());
        options.addOption(Option.builder().longOpt("abc").hasArg().build());

        String[] expected = {"--option1", "value1", "-a", "-b", "-c", "value2", "--", "nonOption", "-"};
        String[] actual = parser.flatten(options, args, true);
        
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_StopAtNonOption_ContinueBursting() {
        String[] args = {"--option1", "value1", "-abc", "value2", "--", "nonOption", "-"};
        
        options.addOption(Option.builder().longOpt("option1").hasArg().build());
        options.addOption(Option.builder().longOpt("abc").hasArg().build());

        String[] expected = {"--option1", "value1", "-a", "-b", "-c", "value2", "--", "nonOption", "-"};
        String[] actual = parser.flatten(options, args, false);
        
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_NoStopAtNonOption_ContinueBursting() {
        String[] args = {"--option1", "value1", "-abc", "value2", "--", "nonOption", "-"};
        
        options.addOption(Option.builder().longOpt("option1").hasArg().build());
        options.addOption(Option.builder().longOpt("abc").hasArg().build());

        String[] expected = {"--option1", "value1", "-a", "-b", "-c", "value2", "--", "nonOption", "-"};
        String[] actual = parser.flatten(options, args, false);
        
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_StopAtNonOption_InvalidOption() {
        String[] args = {"--option1", "value1", "-abc", "value2", "-xyz", "--", "nonOption", "-"};
        
        options.addOption(Option.builder().longOpt("option1").hasArg().build());
        options.addOption(Option.builder().longOpt("abc").hasArg().build());

        String[] expected = {"--option1", "value1", "-a", "-b", "-c", "-xyz", "--", "nonOption", "-"};
        String[] actual = parser.flatten(options, args, true);
        
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_StopAtNonOption_NoOption() {
        String[] args = {"nonOption1", "nonOption2", "--", "nonOption3"};
        
        String[] expected = {"nonOption1", "nonOption2", "--", "nonOption3"};
        String[] actual = parser.flatten(options, args, true);
        
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_StopAtNonOption_EmptyArgs() {
        String[] args = {};
        
        String[] expected = {};
        String[] actual = parser.flatten(options, args, true);
        
        assertArrayEquals(expected, actual);
    }
}