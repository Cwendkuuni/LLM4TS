import org.junit.Test;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.OptionException;

public class GroupImplTest {

  @Test
  public void testCanProcess() {
    GroupImpl group = new GroupImpl(new ArrayList(), "test", "test group", 0, 0);
    WriteableCommandLine commandLine = null;
    String arg = "test";
    assert group.canProcess(commandLine, arg) == false;
  }

  @Test
  public void testGetPrefixes() {
    GroupImpl group = new GroupImpl(new ArrayList(), "test", "test group", 0, 0);
    assert group.getPrefixes().isEmpty();
  }

  @Test
  public void testGetTriggers() {
    GroupImpl group = new GroupImpl(new ArrayList(), "test", "test group", 0, 0);
    assert group.getTriggers().isEmpty();
  }

  @Test
  public void testProcess() throws OptionException {
    GroupImpl group = new GroupImpl(new ArrayList(), "test", "test group", 0, 0);
    WriteableCommandLine commandLine = null;
    ListIterator arguments = null;
    group.process(commandLine, arguments);
  }

  @Test
  public void testValidate() throws OptionException {
    GroupImpl group = new GroupImpl(new ArrayList(), "test", "test group", 0, 0);
    WriteableCommandLine commandLine = null;
    group.validate(commandLine);
  }

  @Test
  public void testGetPreferredName() {
    GroupImpl group = new GroupImpl(new ArrayList(), "test", "test group", 0, 0);
    assert group.getPreferredName().equals("test");
  }

  @Test
  public void testGetDescription() {
    GroupImpl group = new GroupImpl(new ArrayList(), "test", "test group", 0, 0);
    assert group.getDescription().equals("test group");
  }

  @Test
  public void testAppendUsage() {
    GroupImpl group = new GroupImpl(new ArrayList(), "test", "test group", 0, 0);
    StringBuffer buffer = new StringBuffer();
    Set helpSettings = new HashSet();
    Comparator comp = null;
    group.appendUsage(buffer, helpSettings, comp);
  }

  @Test
  public void testHelpLines() {
    GroupImpl group = new GroupImpl(new ArrayList(), "test", "test group", 0, 0);
    int depth = 0;
    Set helpSettings = new HashSet();
    Comparator comp = null;
    group.helpLines(depth, helpSettings, comp);
  }

  @Test
  public void testGetOptions() {
    GroupImpl group = new GroupImpl(new ArrayList(), "test", "test group", 0, 0);
    assert group.getOptions().isEmpty();
  }

  @Test
  public void testGetAnonymous() {
    GroupImpl group = new GroupImpl(new ArrayList(), "test", "test group", 0, 0);
    assert group.getAnonymous().isEmpty();
  }

  @Test
  public void testFindOption() {
    GroupImpl group = new GroupImpl(new ArrayList(), "test", "test group", 0, 0);
    String trigger = "test";
    assert group.findOption(trigger) == null;
  }

  @Test
  public void testGetMinimum() {
    GroupImpl group = new GroupImpl(new ArrayList(), "test", "test group", 0, 0);
    assert group.getMinimum() == 0;
  }

  @Test
  public void testGetMaximum() {
    GroupImpl group = new GroupImpl(new ArrayList(), "test", "test group", 0, 0);
    assert group.getMaximum() == 0;
  }

  @Test
  public void testIsRequired() {
    GroupImpl group = new GroupImpl(new ArrayList(), "test", "test group", 0, 0);
    assert group.isRequired() == false;
  }

  @Test
  public void testDefaults() {
    GroupImpl group = new GroupImpl(new ArrayList(), "test", "test group", 0, 0);
    WriteableCommandLine commandLine = null;
    group.defaults(commandLine);
  }

}