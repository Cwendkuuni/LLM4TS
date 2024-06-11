import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.builder.PatternBuilder;
import org.apache.commons.cli2.validation.NumberValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {

    private CommandLineParser parser;

    @Before
    public void setUp() {
        PatternBuilder builder = new PatternBuilder()
                .withPattern("-a @java.lang.String -b +org.apache.commons.cli2.validation.NumberValidator -c #2015-12-31 -d <./file.txt> -e /http://www.example.com -f /path/to/file.log* -g /path/to/file.log -h :help -i %d -j %d");
        parser = new DefaultParser();
    }

    @Test
    public void testValidArgs() {
        try {
            CommandLine cmd = parser.parse(builder.create());
            Assert.assertNotNull(cmd);
            Assert.assertEquals("help", cmd.getOptionValue("h"));
            Assert.assertEquals("2015-12-31", cmd.getOptionValue("c"));
            Assert.assertEquals("path/to/file.log", cmd.getOptionValue("d"));
            Assert.assertEquals("http://www.example.com", cmd.getOptionValue("e"));
            Assert.assertEquals("path/to/file.log", cmd.getOptionValue("g"));
            Assert.assertNull(cmd.getOptionValue("i"));
            Assert.assertNull(cmd.getOptionValue("j"));
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test(expected = ParseException.class)
    public void testInvalidArgs() {
        try {
            CommandLine cmd = parser.parse(builder.create());
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        Assert.fail();
    }

    @Test
    public void testHelp() {
        HelpFormatter formatter = new HelpFormatter();
        Option help = OptionBuilder.withLongOpt("help")
                .isRequired(false)
                .hasArg(false)
                .desc("Print this help message.")
                .build();
        OptionBuilder ob = new OptionBuilder("a");
        ob.withLongOpt("argA")
                .withDescription("An argument for option A")
                .isRequired(false)
                .hasArg(true);

        PatternBuilder pb = new PatternBuilder()
                .withPattern("-a @java.lang.String {argA=AN_ARGUMENT} -h");
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(pb.create());
            formatter.printHelp("PatternBuilderTest", pb.create().getOptions(), true);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testValidate() {
        PatternBuilder pb = new PatternBuilder()
                .withPattern("-a @java.lang.String -b +org.apache.commons.cli2.validation.NumberValidator -c #2015-12-31 -d <./file.txt> -e /http://www.example.com -f /path/to/file.log* -g /path/to/file.log -h :help -i %d -j %d");

        Option optionA = (Option) pb.create().getOptions().iterator().next();
        Option optionB = (Option) pb.create().getOptions().iterator().next();
        Option optionC = (Option) pb.create().getOptions().iterator().next();
        Option optionD = (Option) pb.create().getOptions().iterator().next();
        Option optionE = (Option) pb.create().getOptions().iterator().next();
        Option optionF = (Option) pb.create().getOptions().iterator().next();
        Option optionG = (Option) pb.create().getOptions().iterator().next();
        Option optionH = (Option) pb.create().getOptions().iterator().next();
        Option optionI = (Option) pb.create().getOptions().iterator().next();
        Option optionJ = (Option) pb.create().getOptions().iterator().next();

        Assert.assertTrue(optionA.getValidator() instanceof ClassValidator);
        Assert.assertTrue(optionB.getValidator() instanceof NumberValidator);
        Assert.assertTrue(optionC.getValidator() instanceof DateValidator);
        Assert.assertTrue(optionD.getValidator() instanceof FileValidator);
        Assert.assertTrue(optionE.getValidator() instanceof UrlValidator);
        Assert.assertTrue(optionF.getValidator() instanceof FileValidator);
        Assert.assertNull(optionG.getValidator());
        Assert.assertNull(optionH.getValidator());
        Assert.assertNull(optionI.getValidator());
        Assert.assertNull(optionJ.getValidator());

    }
}