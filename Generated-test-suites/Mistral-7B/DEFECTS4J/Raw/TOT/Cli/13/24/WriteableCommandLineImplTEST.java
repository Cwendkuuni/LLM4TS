import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option optionA = new Option("a", "append", true, "append text to file");
        Option optionB = new Option("b", "backup", false, "make a backup of the file");
        Option optionC = new Option("c", "count", true, "count number of lines in file");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(optionA, Arrays.asList("input.txt", "--count=5"));

        commandLine.addOption(optionB);
        commandLine.addSwitch(optionB, true);

        commandLine.addOption(optionC);
        commandLine.addValue(optionC, 3);

        List<Option> options = commandLine.getOptions();
        Assert.assertEquals(3, options.size());
        Assert.assertEquals(optionA, options.get(0));
        Assert.assertEquals(optionB, options.get(1));
        Assert.assertEquals(optionC, options.get(2));

        Assert.assertEquals(2, commandLine.getValues(optionC).size());
        Assert.assertEquals("3", commandLine.getValues(optionC).get(0).toString());
        Assert.assertEquals("5", commandLine.getValues(optionC).get(1).toString());

        Assert.assertTrue(commandLine.getSwitch(optionB));

        Map<String, Object> properties = commandLine.getProperties();
        Assert.assertNull(properties.get("unknownProperty"));
        Assert.assertEquals("input.txt", properties.get("input"));

        Assert.assertEquals(3, commandLine.getNormalised().size());
        Assert.assertEquals("--append=input.txt", commandLine.getNormalised().get(0).toString());
        Assert.assertEquals("--count=5", commandLine.getNormalised().get(1).toString());
        Assert.assertEquals("--count=3", commandLine.getNormalised().get(2).toString());

        try {
            commandLine.parse();
        } catch (ParseException e) {
            // expected as no valid command line arguments are provided
        }
    }
}