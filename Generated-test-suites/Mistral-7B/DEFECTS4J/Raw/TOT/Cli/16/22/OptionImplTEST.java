import org.junit.jupitermk.junit4.LauncherDiscovery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.annotations.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.apache.commons.cli2.CliFactory.withDefaults;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.stringContaining;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.DefaultParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;

@RunWith(PowerMockRunner.class)
@PrepareForTest(OptionImpl.class)
public class OptionImplTest {

    @Test
    public void testOptionImplCreation() {
        final Option option = new OptionBuilder("optionName")
                .withDescription("Description of the option")
                .create();
        assertNotNull(option);
        assertFalse(option.isRequired());
        assertEquals("optionName", option.getOpt());
        assertEquals("Description of the option", option.getDescription());
        assertEquals(Option.NO_ARGUMENTS, option.getArgName());
        assertNull(option.getValue());
        assertEquals(new HashSet<String>(), option.getPrefixes());
        assertEquals(new HashSet<String>(), option.getTriggers());
    }

    @Test
    public void testOptionRequiredCreation() {
        final Option option = new OptionBuilder("optionName")
                .withDescription("Description of the option")
                .isRequired(true)
                .create();
        assertNotNull(option);
        assertTrue(option.isRequired());
        assertEquals("optionName", option.getOpt());
        assertEquals("Description of the option", option.getDescription());
        assertEquals(Option.NO_ARGUMENTS, option.getArgName());
        assertNull(option.getValue());
        assertEquals(new HashSet<String>(), option.getPrefixes());
        assertEquals(new HashSet<String>(), option.getTriggers());
    }

    @Test
    public void testOptionCreationWithPrefix() {
        final Option option = new OptionBuilder("prefix-option")
                .withDescription("Description of the option")
                .withLongOpt("longPrefixOption")
                .create();
        assertEquals("prefix-option", option.getOpt());
        assertEquals("longPrefixOption", option.getLongOpt());
        assertEquals("Description of the option", option.getDescription());
        assertEquals(Option.NO_ARGUMENTS, option.getArgName());
        assertNull(option.getValue());
        assertEquals(new HashSet<String>(), option.getPrefixes());
        assertEquals(new HashSet<String>(), option.getTriggers());
        assertEquals(Arrays.asList("--prefix-option", "--longPrefixOption"), option.getPrefixes());
    }

    @Test
    public void testOptionCreationWithTrigger() {
        final Option option = new OptionBuilder("optionName")
                .withDescription("Description of the option")
                .withLongOpt("longOptionName")
                .create();
        assertEquals("optionName", option.getOpt());
        assertEquals("longOptionName", option.getLongOpt());
        assertEquals("Description of the option", option.getDescription());
        assertEquals(Option.NO_ARGUMENTS, option.getArgName());
        assertNull(option.getValue());
        assertEquals(new HashSet<String>(), option.getPrefixes());
        final Set<String> triggers = option.getTriggers();
        assertEquals(1, triggers.size());
        assertTrue(triggers.contains("optionName"));
        assertTrue(triggers.contains("longOptionName"));
    }

    @Test
    public void testOptionCreationWithPrefixAndTrigger() {
        final Option option = new OptionBuilder("prefix-trigger-option")
                .withDescription("Description of the option")
                .withLongOpt("longPrefixTriggerOption")
                .create();
        assertEquals("prefix-trigger-option", option.getOpt());
        assertEquals("longPrefixTriggerOption", option.getLongOpt());
        assertEquals("Description of the option", option.getDescription());
        assertEquals(Option.NO_ARGUMENTS, option.getArgName());
        assertNull(option.getValue());
        assertEquals(new HashSet<String>(), option.getPrefixes());
        assertEquals(new HashSet<String>(), option.getTriggers());
        assertEquals(Arrays.asList("--prefix-trigger-option", "--longPrefixTriggerOption"), option.getPrefixes());
        final Set<String> triggers = option.getTriggers();
        assertEquals(2, triggers.size());
        assertTrue(triggers.contains("prefix-trigger-option"));
        assertTrue(triggers.contains("longPrefixTriggerOption"));
    }

    @Test
    public void testOptionProcessing() {
        final Option option = new OptionBuilder("optionName")
                .withDescription("Description of the option")
                .isRequired(true)
                .create();
        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd = mock(CommandLine.class);
        final Options options = new Options();
        when(cmd.getArgs()).thenReturn(new String[]{"optionName", "value"});
        when(cmd.hasOption("optionName")).thenReturn(true);
        when(cmd.getOptionValue("optionName")).thenReturn("value");
        when(cmd.getOptionNames()).thenReturn(new String[]{"optionName"});
        when(cmd.getOptionValues("optionName")).thenReturn(new String[]{"value"});
        when(cmd.getOptionNames(false)).thenReturn(new String[]{"optionName"});
        when(cmd.getOptionValues(false, "optionName")).thenReturn(new String[]{"value"});
        when(cmd.getOptionValues(false, "otherOptionName")).thenReturn(new String[0]);
        when(cmd.hasOption(stringContaining("optionName"))).thenReturn(true);
        when(cmd.hasOption(stringContaining("otherOptionName"))).thenReturn(false);
        when(cmd.hasOption("--longOptionName")).thenReturn(false);

        options.addOption(option);
        final HelpFormatter formatter = new HelpFormatter();

        option.process(cmd, options, parser, formatter);
        assertEquals("value", option.getValue());
        assertTrue(option.isParsed());
        assertFalse(option.isAllowedByContext());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testOptionProcessWithMissingPrefix() {
        final Option option = new OptionBuilder("optionName")
                .withDescription("Description of the option")
                .create();
        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd = mock(CommandLine.class);
        final Options options = new Options();
        when(cmd.getArgs()).thenReturn(new String[]{"otherOptionName"});
        when(cmd.getOptionNames()).thenReturn(new String[]{"optionName"});
        when(cmd.hasOption("optionName")).thenReturn(false);

        options.addOption(option);
        final HelpFormatter formatter = new HelpFormatter();

        option.process(cmd, options, parser, formatter);
    }

    @Test
    public void testOptionEquals() {
        final Option option1 = new OptionBuilder("optionName1")
                .create();
        final Option option2 = new OptionBuilder("optionName2")
                .create();
        final Option option3 = new OptionBuilder("optionName1")
                .create();
        assertEquals(option1, option1);
        assertEquals(option1, option3);
        assertNotEquals(option1, option2);
    }

    @Test
    public void testOptionHashCode() {
        final Option option1 = new OptionBuilder("optionName1")
                .create();
        final Option option2 = new OptionBuilder("optionName2")
                .create();
        final Option option3 = new OptionBuilder("optionName1")
                .create();
        assertEquals(option1.hashCode(), option1.hashCode());
        assertEquals(option1.hashCode(), option3.hashCode());
        assertNotEquals(option1.hashCode(), option2.hashCode());
    }

    @Test
    public void testOptionFindOption() {
        final Option option1 = new OptionBuilder("optionName1")
                .create();
        final Option option2 = new OptionBuilder("optionName2")
                .create();

        assertEquals(option1, option1.findOption("optionName1"));
        assertEquals(option1, option1.findOption("--optionName1"));
        assertEquals(option1, option1.findOption("-o"));
        assertEquals(option1, option1.findOption("-o", "--optionName1"));
        assertEquals(option1, option1.findOption("optionName1", "--optionName1"));
        assertEquals(option1, option1.findOption("optionName1", "-o"));
        assertEquals(option1, option1.findOption("optionName1", "--optionName1", "-o"));
        assertEquals(option1, option1.findOption("optionName1", "--optionName2"));
        assertEquals(option1, option1.findOption("optionName1", "--longOptionName1"));
        assertEquals(option1, option1.findOption("optionName1", "otherOptionName"));
        assertEquals(option1, option1.findOption(null, null));
        assertEquals(option1, option1.findOption(null, "optionName1"));
        assertEquals(option1, option1.findOption("", "optionName1"));
        assertEquals(option1, option1.findOption("", "--optionName1"));
        assertEquals(option1, option1.findOption("", "-o"));
        assertEquals(option1, option1.findOption("", "-o", "--optionName1"));
        assertEquals(option1, option1.findOption("", "optionName1"));
        assertEquals(option1, option1.findOption("", "--optionName1", "-o"));
        assertEquals(option1, option1.findOption("", "--optionName1", "--optionName2"));
        assertEquals(option1, option1.findOption("", "--optionName1", "otherOptionName"));
        assertEquals(option1, option1.findOption("", "otherOptionName", "--optionName1"));
        assertEquals(option1, option1.findOption("", "otherOptionName", "-o"));
        assertEquals(option1, option1.findOption("", "otherOptionName", "--optionName1", "-o"));
        assertEquals(option1, option1.findOption("", "otherOptionName", "--optionName2"));
        assertEquals(option1, option1.findOption("", "otherOptionName", "otherOptionName"));
        assertEquals(null, option1.findOption("optionName2"));
        assertEquals(null, option1.findOption("--longOptionName1"));
        assertEquals(null, option1.findOption("otherOptionName"));
        assertEquals(null, option1.findOption("--longOptionName2"));
        assertEquals(null, option1.findOption("--longOptionName3"));
        assertEquals(null, option1.findOption("--longOptionName1", "--longOptionName2"));
        assertEquals(null, option1.findOption("--longOptionName1", "--longOptionName3"));
        assertEquals(null, option1.findOption("--longOptionName1", "--longOptionName1"));
        assertEquals(null, option1.findOption("--longOptionName1", "--longOptionName1", "--longOptionName2"));
        assertEquals(null, option1.findOption("--longOptionName1", "--longOptionName1", "--longOptionName3"));
    }

    @Test
    public void testOptionCanProcess() {
        final Option option = new OptionBuilder("optionName")
                .create();
        final CommandLine cmd = mock(CommandLine.class);
        final ListIterator<String> arguments = mock(ListIterator.class);
        when(arguments.hasNext()).thenReturn(false);
        when(arguments.next()).thenReturn("value");
        when(cmd.getParsedArgs()).thenReturn(arguments);

        assertFalse(option.canProcess(cmd, arguments));

        when(arguments.next()).thenReturn("--optionName");
        assertTrue(option.canProcess(cmd, arguments));

        when(arguments.next()).thenReturn("value2");
        assertTrue(option.canProcess(cmd, arguments));

        when(arguments.next()).thenReturn(null);
        assertFalse(option.canProcess(cmd, arguments));
    }

    @Test
    public void testOptionToString() {
        final Option option = new OptionBuilder("optionName")
                .create();
        final StringBuilder buffer = new StringBuilder();
        option.appendUsage(buffer, DisplaySetting.DEFAULT, null);

        assertEquals(
                "--optionName <value>\n" +
                "Description of the option\n",
                buffer.toString());
    }

    @Test
    public void testOptionDefaults() {
        final Option option = new OptionBuilder("optionName")
                .create();
        final CommandLine cmd = mock(CommandLine.class);
        final Options options = new Options();
        when(cmd.getArgs()).thenReturn(new String[0]);
        when(cmd.getOptionValues(false, null)).thenReturn(new String[0]);
        when(cmd.getOptionValues(false, "optionName")).thenReturn(new String[0]);
        when(cmd.getOptionNames()).thenReturn(new String[0]);

        option.defaults(cmd);
        assertFalse(option.isParsed());
        assertFalse(option.isAllowedByContext());
    }

    @Test
    public void testOptionCheckPrefixes() {
        final Option option = new OptionBuilder("optionName")
                .withPrefixes(new HashSet<String>(Arrays.asList("--prefix1", "--prefix2")))
                .create();
        final Set<String> prefixes = new HashSet<String>(Arrays.asList("--prefix1", "--prefix3"));

        option.checkPrefixes(prefixes);
        assertFalse(option.isParsed());
        assertTrue(option.isAllowedByContext());

        when(cmd.getOptionValues(false, null)).thenReturn(new String[0]);
        when(cmd.getOptionValues(false, "optionName")).thenReturn(new String[0]);
        when(cmd.getOptionNames()).thenReturn(new String[0]);

        option.defaults(cmd);
        assertTrue(option.isParsed());
        assertTrue(option.isAllowedByContext());

        option.checkPrefixes(prefixes);
        assertTrue(option.isParsed());
        assertFalse(option.isAllowedByContext());
    }
}