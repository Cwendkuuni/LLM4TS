import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLineImpl;

public class WriteableCommandLineImplTest {
    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList());
        Option option = new Option("option2", "o2", false, "Description of option2");
        commandLine.addOption(option);

        List options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.get(0));

        Option optionFromName = commandLine.getOption("o2");
        Assert.assertEquals(option, optionFromName);

        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(1, optionTriggers.size());
        Assert.assertEquals("o2", optionTriggers.iterator().next().toString());
    }
}