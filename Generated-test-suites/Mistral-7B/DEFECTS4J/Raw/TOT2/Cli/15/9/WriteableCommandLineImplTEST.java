// WriteableCommandLineImplTest.java
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testConstructor() {
        Option rootOption = new Option("rootOption", "rootOption description");
        List arguments = Arrays.asList("arg1", "arg2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        Assert.assertEquals(Collections.unmodifiableList(arguments), commandLine.getNormalised());
    }

    // Add your test cases for other methods here
}