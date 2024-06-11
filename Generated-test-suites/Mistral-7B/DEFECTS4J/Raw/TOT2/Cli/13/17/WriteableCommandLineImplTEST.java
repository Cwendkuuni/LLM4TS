import org.apache.commons.cli2.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
  @Test
  public void testAddOption() {
    WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("test", true, "t"), Collections.emptyList());
    Option option = new Option("test2", false, "t2");
    writeableCommandLine.addOption(option);
    List options = writeableCommandLine.getOptions();
    assertTrue(options.contains(option));
  }
}