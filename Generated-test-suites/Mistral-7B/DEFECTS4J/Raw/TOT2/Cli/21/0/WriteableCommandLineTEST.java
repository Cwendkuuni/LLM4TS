import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

public class WriteableCommandLineTest {

  @Test
  public void testAddOption() {
    WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
    Option option = new Option("o", "option");
    commandLine.addOption(option);
    assertNotNull(commandLine.getOptions().get(0));
  }
}