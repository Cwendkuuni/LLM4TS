import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.parse.DefaultParser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GroupImplTest {
    private Options options;
    private HelpFormatter formatter;
    private CommandLine cmd;

    @Before
    public void setUp() {
        List<Option> optionsList = new ArrayList<>();
        options = new Options();

        // Add a group with name "group1" and minimum 1 maximum 3 options
        Option group1 = new Option.Builder("g1")
                .longOpt("group1")
                .minimum(1)
                .maximum(3)
                .group(new GroupImpl(optionsList, "group1", null, 1, 3))
                .build();
        options.addOption(group1);

        optionsList.add(new Option.Builder("a")
                .longOpt("optionA")
                .valueRequired(false)
                .build());
        optionsList.add(new Option.Builder("b")
                .longOpt("optionB")
                .valueRequired(true)
                .build());
        optionsList.add(new Option.Builder("c")
                .longOpt("optionC")
                .valueRequired(false)
                .build());

        // Add a group with name "group2" and no minimum or maximum options
        Option group2 = new Option.Builder("g2")
                .longOpt("group2")
                .group(new GroupImpl(new ArrayList<>(), "group2", null, 0, 0))
                .build();
        options.addOption(group2);

        formatter = new HelpFormatter();
    }

    @Test
    public void testGroupWithMinMax() throws Exception {
        DefaultParser parser = new DefaultParser();
        cmd = parser.parse(options, new String[]{"-g1", "--optionA"});
        assertEquals("Option '--optionA' is accepted", true, cmd.hasOption("optionA"));

        cmd = parser.parse(options, new String[]{"-g1", "--optionA", "--optionA", "--optionA"});
        assertEquals("Three '--optionA' options are accepted", true, cmd.hasOption("optionA"));
        assertEquals("Total number of options is 3", 3, cmd.getOptions().size());

        cmd = parser.parse(options, new String[]{"-g1", "--optionA", "--optionB", "--optionC"});
        assertEquals("Three '--optionA' options are accepted and '--optionB' and '--optionC' are not",
                3, cmd.getOptions(group1).size());
        assertEquals("'--optionB' and '--optionC' are not present", false, cmd.hasOption("optionB"));
        assertEquals("'--optionC' is not present", false, cmd.hasOption("optionC"));

        cmd = parser.parse(options, new String[]{"-g1", "--optionA", "--optionB", "--optionC", "--optionA"});
        assertEquals("Four '--optionA' options are not accepted", false, cmd.hasOption("optionA"));
    }

    @Test(expected = OptionException.class)
    public void testGroupWithMinMaxError() throws Exception {
        DefaultParser parser = new DefaultParser();
        cmd = parser.parse(options, new String[]{"-g1"});
    }

    @Test
    public void testGroupWithNoMinMax() throws Exception {
        cmd = parser.parse(options, new String[]{"-g2", "--optionA", "--optionB"});
        assertEquals("Options can be added to this group any number", true, cmd.hasOption("optionA"));
        assertEquals("Options can be added to this group any number", true, cmd.hasOption("optionB"));
    }

    @Test
    public void testPrintHelp() {
        HelpPrinter printer = new HelpPrinter(System.out, options, null, null, true);
        printer.printHelp("test", "1.0");
    }
}