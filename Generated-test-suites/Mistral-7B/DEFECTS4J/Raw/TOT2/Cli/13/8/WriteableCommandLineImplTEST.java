import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testConstructor() {
        Option option = new Option("option", true, "o");
        List<String> args = new ArrayList<>();
        args.add("arg1");
        args.add("arg2");

        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(option, args);
        Assert.assertNotNull(writeableCommandLineImpl);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("option", true, "o");
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(null, null);
        writeableCommandLineImpl.addOption(option);
        Option optionFromList = writeableCommandLineImpl.getOption("option");

        Assert.assertEquals(option, optionFromList);
    }
}