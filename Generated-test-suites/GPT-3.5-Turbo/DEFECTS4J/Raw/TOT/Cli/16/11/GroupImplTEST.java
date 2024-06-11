import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.*;
import org.apache.commons.cli2.option.Option;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class GroupImplTest {

    private GroupImpl group;
    private Option option1;
    private Option option2;
    private Option option3;

    @Before
    public void setup() {
        option1 = new OptionImpl("opt1", "Option 1");
        option2 = new OptionImpl("opt2", "Option 2");
        option3 = new OptionImpl("opt3", "Option 3");

        group = new GroupImpl(Arrays.asList(option1, option2, option3), "Group1", "Group Description", 1, 3);
    }

    @Test
    public void testCanProcess_ValidOption_ReturnsTrue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addOption(option1);

        assertTrue(group.canProcess(commandLine, "opt1"));
    }

    @Test
    public void testCanProcess_InvalidOption_ReturnsFalse() {
        assertFalse(group.canProcess(new WriteableCommandLineImpl(), "opt4"));
    }

    @Test
    public void testGetPrefixes_ReturnsPrefixes() {
        assertEquals(0, group.getPrefixes().size());
    }

    @Test
    public void testGetTriggers_ReturnsTriggers() {
        assertEquals(3, group.getTriggers().size());
    }

    @Test
    public void testProcess_ValidOption_CallsOptionProcess() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator arguments = Arrays.asList("opt1").listIterator();

        group.process(commandLine, arguments);

        assertTrue(commandLine.hasOption(option1));
    }

    @Test
    public void testProcess_InvalidOption_DoesNotCallOptionProcess() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator arguments = Arrays.asList("opt4").listIterator();

        group.process(commandLine, arguments);

        assertFalse(commandLine.hasOption(option1));
    }

    @Test
    public void testValidate_TooManyOptions_ThrowsOptionException() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addOption(option3);

        try {
            group.validate(commandLine);
            fail("Expected OptionException to be thrown");
        } catch (OptionException e) {
            assertEquals(ResourceConstants.UNEXPECTED_TOKEN, e.getMessageKey());
            assertEquals("opt1", e.getMessageFormatArgs()[0]);
        }
    }

    @Test
    public void testValidate_TooFewOptions_ThrowsOptionException() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        try {
            group.validate(commandLine);
            fail("Expected OptionException to be thrown");
        } catch (OptionException e) {
            assertEquals(ResourceConstants.MISSING_OPTION, e.getMessageKey());
        }
    }

    @Test
    public void testGetPreferredName_ReturnsName() {
        assertEquals("Group1", group.getPreferredName());
    }

    @Test
    public void testGetDescription_ReturnsDescription() {
        assertEquals("Group Description", group.getDescription());
    }

    @Test
    public void testGetOptions_ReturnsOptions() {
        List<Option> expected = Arrays.asList(option1, option2, option3);

        assertEquals(expected, group.getOptions());
    }

    @Test
    public void testFindOption_ValidTrigger_ReturnsOption() {
        assertEquals(option1, group.findOption("opt1"));
        assertEquals(option2, group.findOption("opt2"));
        assertEquals(option3, group.findOption("opt3"));
    }

    @Test
    public void testFindOption_InvalidTrigger_ReturnsNull() {
        assertNull(group.findOption("opt4"));
    }

}