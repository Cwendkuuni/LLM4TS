import org.apache.commons.cli2.BaseParser;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseContext;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new DefaultWriteableCommandLine();
    }

    @Test
    public void testAddOption() {
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().size());
        assertSame(option, commandLine.getOptions().get(0));
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("o").longOpt("option").valueSeparator('-').buildValueSetter();
        option.setValueSeparator('-');
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(2, commandLine.getValuesForOption("option").size());
        assertEquals("value1", commandLine.getValuesForOption("option").get(0));
        assertEquals("value2", commandLine.getValuesForOption("option").get(1));

        assertNull(commandLine.getValuesForOption("nonexistent"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = Option.builder("o").longOpt("option").build();
        List<String> defaultValues = new ArrayList<>(Arrays.asList("value1", "value2"));
        commandLine.setDefaultValues(option, defaultValues);

        assertFalse(commandLine.getValuesForOption("option").isEmpty());
        assertEquals(2, commandLine.getValuesForOption("option").size());
        assertEquals("value1", commandLine.getValuesForOption("option").get(0));
        assertEquals("value2", commandLine.getValuesForOption("option").get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder("s").longOpt("switch").build();

        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getOptionState(option).isSet());

        try {
            commandLine.addSwitch(option, false);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // expected
        }

        commandLine.setDefaultSwitch(option, false);
        assertFalse(commandLine.getOptionState(option).isSet());
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getPropertyValue("property"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--option"));
        assertTrue(commandLine.looksLikeOption("-o"));
        assertFalse(commandLine.looksLikeOption("value"));
        assertFalse(commandLine.looksLikeOption("--value"));
    }

    @Test
    public void testParse() {
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);

        List<String> args = Arrays.asList("--option value1", "--option value2");
        ParseContext parseContext = new DefaultParseContext();
        BaseParser parser = new DefaultParser();
        try {
            parser.parse(commandLine, args.toArray(new String[0]), parseContext);
        } catch (Exception e) {
            fail("Exception expected");
        }

        assertEquals(2, commandLine.getValuesForOption("option").size());
        assertEquals("value1", commandLine.getValuesForOption("option").get(0));
        assertEquals("value2", commandLine.getValuesForOption("option").get(1));
    }
}