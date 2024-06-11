import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testConstructor() {
        Option rootOption = new Option("root", true, false, "root");
        List arguments = new ArrayList();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
        Assert.assertEquals("root", writeableCommandLine.toString());
    }

    // Implement test cases for other methods here

}