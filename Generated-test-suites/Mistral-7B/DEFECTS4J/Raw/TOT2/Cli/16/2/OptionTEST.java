import org.apache.commons.cli2.*;
import org.junit.Assert;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new BasicParseResults();
        ListIterator<String> args = Arrays.asList("--option", "value").listIterator();
        Option option = new MockOption(); // Use a MockOption implementation here
        option.process(commandLine, args);
        Assert.assertEquals("value", commandLine.getOptionValues("option").get(0));
    }
}