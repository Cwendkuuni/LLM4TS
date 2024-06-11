import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parser.DefaultParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Option.class)
public class OptionTest {

    private Option option;
    private WriteableCommandLine commandLine;
    private ListIterator<String> argsIterator;

    @Before
    public void setUp() {
        commandLine = Mockito.mock(WriteableCommandLine.class);
        argsIterator = Mockito.mock(ListIterator.class);
        option = createMockOption();
    }

    @Test
    public void testProcess() throws OptionException {
        PowerMockito.when(argsIterator.hasNext()).thenReturn(true);
        PowerMockito.when(argsIterator.next()).thenReturn("--option");

        option.process(commandLine, argsIterator);

        PowerMockito.verifyStatic();
        Option.process(commandLine, argsIterator);
    }

    @Test(expected = OptionException.class)
    public void testProcessWithException() throws OptionException {
        PowerMockito.when(argsIterator.hasNext()).thenReturn(true);
        PowerMockito.when(argsIterator.next()).thenReturn("invalidArgument");

        option.process(commandLine, argsIterator);

        PowerMockito.verifyStatic();
        Option.process(commandLine, argsIterator);
    }

    @Test
    public void testCanProcess() {
        Set triggers = new HashSet(Arrays.asList("--option"));
        Set prefixes = new HashSet(Arrays.asList("--"));

        assertTrue(option.canProcess(commandLine, " --option arg"));
        assertTrue(option.canProcess(commandLine, "--option"));
        assertFalse(option.canProcess(commandLine, "--other-option"));
        assertFalse(option.canProcess(commandLine, "arg"));

        PowerMockito.verifyStatic();
        Set set = Option.getTriggers();
        assertEquals(triggers, set);
        set = Option.getPrefixes();
        assertEquals(prefixes, set);
    }

    @Test
    public void testValidate() throws OptionException {
        PowerMockito.when(commandLine.getValue("optionValue")).thenReturn("validValue");

        option.validate(commandLine);

        PowerMockito.verifyStatic();
        Option.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithException() throws OptionException {
        PowerMockito.when(commandLine.getValue("optionValue")).thenReturn("invalidValue");

        option.validate(commandLine);

        PowerMockito.verifyStatic();
        Option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        Set helpSettings = new HashSet();
        Comparator comp = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return 0;
            }
        };

        List helpLines = option.helpLines(1, helpSettings, comp);

        assertNotNull(helpLines);
        assertEquals(1, helpLines.size());

        PowerMockito.verifyStatic();
        Option.helpLines(1, helpSettings, comp);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet();
        Comparator comp = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return 0;
            }
        };

        option.appendUsage(buffer, helpSettings, comp);

        PowerMockito.verifyStatic();
        Option.appendUsage(buffer, helpSettings, comp);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("option", option.getPreferredName());

        PowerMockito.verifyStatic();
        String name = Option.getPreferredName();
        assertEquals("option", name);
    }

    @Test
    public void testGetDescription() {
        assertEquals("This is the description of the option", option.getDescription());

        PowerMockito.verifyStatic();
        String description = Option.getDescription();
        assertEquals("This is the description of the option", description);
    }

    @Test
    public void testGetId() {
        assertEquals(1, option.getId());

        PowerMockito.verifyStatic();
        int id = Option.getId();
        assertEquals(1, id);
    }

    @Test
    public void testFindOption() {
        Option otherOption = createMockOption();

        assertNull(option.findOption("trigger"));
        assertNull(option.findOption("otherTrigger"));
        assertSame(option, option.findOption(null));
        assertSame(option, option.findOption(""));
        assertSame(option, option.findOption(""));
        assertSame(option, option.findOption("--option"));
        assertSame(option, option.findOption("--opt ion"));
        assertSame(option, option.findOption("--OPTION"));
        assertSame(option, option.findOption("--OPTION"));
        assertSame(option, option.findOption("--OPTION "));
        assertSame(option, option.findOption("--OPTION "));
        assertSame(otherOption, option.findOption("--otherOption"));
        assertNull(option.findOption("--otherOption2"));

        PowerMockito.verifyStatic();
        Option foundOption = Option.findOption(null, "trigger");
        assertNull(foundOption);
        foundOption = Option.findOption(null, "otherTrigger");
        assertNull(foundOption);
        foundOption = Option.findOption(null, "");
        assertSame(option, foundOption);
        foundOption = Option.findOption(null, "");
        assertSame(option, foundOption);
        foundOption = Option.findOption(null, "OPTION");
        assertSame(option, foundOption);
        foundOption = Option.findOption(null, "OPTION");
        assertSame(option, foundOption);
        foundOption = Option.findOption(null, "OPTION ");
        assertSame(option, foundOption);
        foundOption = Option.findOption(null, "OPTION ");
        assertSame(option, foundOption);
        foundOption = Option.findOption(null, "OTHEROPTION");
        assertNull(foundOption);
        foundOption = Option.findOption(null, "OTHEROPTION2");
        assertNull(foundOption);
        foundOption = Option.findOption(null, "--otherOption");
        assertSame(otherOption, foundOption);
        foundOption = Option.findOption(null, "--otherOption2");
        assertNull(foundOption);
    }

    @Test
    public void testIsRequired() {
        assertFalse(option.isRequired());

        PowerMockito.verifyStatic();
        boolean isRequired = Option.isRequired();
        assertFalse(isRequired);
    }

    @Test
    public void testGetParent() {
        assertNull(option.getParent());

        PowerMockito.verifyStatic();
        Option parent = Option.getParent();
        assertNull(parent);
    }

    @Test
    public void testSetParent() {
        Option parentOption = createMockOption();

        option.setParent(parentOption);

        PowerMockito.verifyStatic();
        Option.setParent(parentOption);
    }

    private Option createMockOption() {
        return PowerMockito.mock(Option.class);
    }
}