import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
public class OptionImplTest {

    private OptionImpl option;
    private WriteableCommandLine commandLine;
    private ResourceHelper resourceHelper;

    @Before
    public void setup() {
        option = new OptionImpl(1, true);
        commandLine = Mockito.mock(WriteableCommandLine.class);
        resourceHelper = PowerMockito.mock(ResourceHelper.class);
    }

    @Test
    public void testConstructor() {
        assertNotNull(option);
    }

    @Test
    public void testCanProcess_whenCommandLineHasArgumentAndCanProcessShouldBeTrue_thenReturnTrue() {
        PowerMockito.when(commandLine.getArguments()).thenReturn(Arrays.asList("arg"));
        assertTrue(option.canProcess(commandLine, commandLine.listArguments()));
    }

    @Test
    public void testCanProcess_whenCommandLineHasNoArgumentAndCanProcessShouldBeFalse_thenReturnFalse() {
        PowerMockito.when(commandLine.getArguments()).thenReturn(Arrays.asList());
        assertFalse(option.canProcess(commandLine, commandLine.listArguments()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanProcess_whenCommandLineHasArgumentButOptionCannotProcessIt_thenThrowIllegalArgumentException() {
        PowerMockito.when(commandLine.getArguments()).thenReturn(Arrays.asList("arg"));
        PowerMockito.when(resourceHelper.getMessage(ResourceConstants.OPTION_TRIGGER_NEEDS_PREFIX, "arg", anyString()))
                .thenThrow(new IllegalArgumentException());

        option.canProcess(commandLine, commandLine.listArguments());
    }

    @Test
    public void testToString() {
        final String expected = "OptionImpl[id=1, required=true]";
        final String actual = option.toString();
        assertSame(expected, actual);
    }

    @Test
    public void testEquals_whenThisOptionIsEqualToAnotherOptionWithSameIdAndName_thenReturnTrue() {
        final OptionImpl anotherOption = new OptionImpl(1, true);

        assertTrue(option.equals(anotherOption));
    }

    @Test
    public void testEquals_whenThisOptionIsNotEqualToAnotherOptionWithDifferentId_thenReturnFalse() {
        final OptionImpl anotherOption = new OptionImpl(2, true);

        assertFalse(option.equals(anotherOption));
    }

    @Test
    public void testHashCode_whenThisOptionHasTheSamePropertiesAsAnotherOption_thenReturnSameHashCode() {
        final OptionImpl anotherOption = new OptionImpl(1, true);

        int thisHashCode = option.hashCode();
        int anotherHashCode = anotherOption.hashCode();

        assertTrue(thisHashCode == anotherHashCode);
    }

    @Test
    public void testFindOption_whenTriggerMatchesAnOption_thenReturnThatOption() {
        final OptionImpl anotherOption = new OptionImpl(2, true);

        Set<String> triggers = new HashSet<>(Arrays.asList("trigger", "anotherTrigger"));
        option.setTriggers(triggers);

        assertSame(option, option.findOption("trigger"));
    }

    @Test
    public void testFindOption_whenTriggerDoesNotMatchAnyOption_thenReturnNull() {
        final OptionImpl anotherOption = new OptionImpl(2, true);

        Set<String> triggers = new HashSet<>(Arrays.asList("trigger", "anotherTrigger"));
        option.setTriggers(triggers);

        assertNull(option.findOption("nonexistentTrigger"));
    }

    @Test
    public void testIsRequired_whenOptionIsRequired_thenReturnTrue() {
        assertTrue(option.isRequired());
    }

    @Test
    public void testIsRequired_whenOptionIsNotRequired_thenReturnFalse() {
        option = new OptionImpl(1, false);

        assertFalse(option.isRequired());
    }

    @Test
    public void testDefaults_whenOptionDoesNotHaveDefaultValue_thenDoNothing() {
        option.defaults(commandLine);
    }

    @Test
    public void testCheckPrefixes_whenPrefixesContainPrefixOfTrigger_thenDoNothing() {
        final Set<String> prefixes = new HashSet<>(Arrays.asList("prefix"));
        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes_whenPrefixesDoNotContainPrefixOfTrigger_thenThrowIllegalArgumentException() {
        final Set<String> prefixes = new HashSet<>();
        option.checkPrefixes(prefixes);
    }
}