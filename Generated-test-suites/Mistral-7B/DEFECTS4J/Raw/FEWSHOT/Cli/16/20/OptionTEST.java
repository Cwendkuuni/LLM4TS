package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.builder.Alert;
import org.apache.commons.cli2.builder.Default;
import org.apache.commons.cli2.builder.DefaultValue;
import org.apache.commons.cli2.builder.OptionBuilder.Builder;
import org.apache.commons.cli2.builder.OrderedOption;
import org.apache.commons.cli2.builder.RequiredOption;
import org.apache.commons.cli2.builder.Value;
import org.apache.commons.cli2.commandline.BasicParser;
import org.apache.commons.cli2.commandline.ParseContext;
import org.apache.commons.cli2.commandline.ParserResult;
import org.apache.commons.cli2.util.HelpFormatter;

@RunWith(Parameterized.class)
public class OptionTest {

    private Option option;
    private String name;
    private Set triggers;
    private Set prefixes;
    private boolean required;
    private boolean canProcess;
    private boolean isValid;

    public OptionTest(Option option, String name, Set triggers, Set prefixes, boolean required, boolean canProcess, boolean isValid) {
        this.option = option;
        this.name = name;
        this.triggers = triggers;
        this.prefixes = prefixes;
        this.required = required;
        this.canProcess = canProcess;
        this.isValid = isValid;
    }

    @Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][] {
            new Object[] { OptionBuilder.noArg(), "testOption", Collections.singleton(new String[]{"testOption"}), new HashSet<String>(Arrays.asList(new String[]{"-t", "--testOption"})), false, true, true },
            new Object[] { OptionBuilder.withLongOpt("testLongOption"), "testLongOption", Collections.singleton(new String[]{"--testOption"}), new HashSet<String>(Arrays.asList(new String[]{"--testLongOption"})), false, true, true },
            new Object[] { OptionBuilder.hasArg("testArg"), "testOption", new HashSet<String>(Arrays.asList(new String[]{"--testOption="})), new HashSet<String>(Arrays.asList(new String[]{"-t", "--testOption"})), false, true, true },
            new Object[] { OptionBuilder.hasOptionalArg("testArg"), "testOption", new HashSet<String>(Arrays.asList(new String[]{"--testOption=", "--testOption"})), new HashSet<String>(Arrays.asList(new String[]{"-t", "--testOption"})), false, true, true },
            new Object[] { OptionBuilder.valueSeparator(';'), OptionBuilder.hasArg("testArg").required(), "testOption", new HashSet<String>(Arrays.asList(new String[]{"--testOption"})), true, true, false },
            new Object[] { OptionBuilder.valueSeparator(','), OptionBuilder.hasArg("testArg").required(), "testOption", new HashSet<String>(Arrays.asList(new String[]{"--testOption", "--testOption="})), true, false, false },
            new Object[] { OptionBuilder.withValue("testValue"), OptionBuilder.hasArg(false), "testOption", new HashSet<String>(Arrays.asList(new String[]{"--testOption"})), false, true, true },
            new Object[] { OptionBuilder.withValue("testValue").required(), OptionBuilder.hasArg(false), "testOption", new HashSet<String>(Arrays.asList(new String[]{"--testOption"})), true, true, false },
            new Object[] { OptionBuilder.withArgParser(BasicParser.class), OptionBuilder.hasArg(false), "testOption", new HashSet<String>(Arrays.asList(new String[]{"--testOption"})), false, false, true },
            new Object[] { OptionBuilder.withArgParser(BasicParser.class), OptionBuilder.hasArg(false), "testOption", new HashSet<String>(Arrays.asList(new String[]{"--testOption"})), false, false, false },
            new Object[] { OptionBuilder.withArgParser(OrderedOption.class), OptionBuilder.hasArg(false), "testOption", new HashSet<String>(Arrays.asList(new String[]{"--testOption"})), false, false, true },
            new Object[] { OptionBuilder.withArgParser(OrderedOption.class), OptionBuilder.hasArg(false), "testOption", new HashSet<String>(Arrays.asList(new String[]{"--testOption", "--testOption2"})), false, false, false },
            new Object[] { OptionBuilder.withArgParser(RequiredOption.class), OptionBuilder.hasArg(false), "testOption", new HashSet<String>(Arrays.asList(new String[]{"--testOption"})), true, true, false },
            new Object[] { OptionBuilder.withArgParser(RequiredOption.class), OptionBuilder.hasArg(false), "testOption", new HashSet<String>(Arrays.asList(new String[]{"--testOption"})), true, false, false },
            new Object[] { OptionBuilder.withArgParser(Value.class), OptionBuilder.hasArg(true), "testOption", new HashSet<String>(Arrays.asList(new String[]{"--testOption"})), false, true, true },
            new Object[] { OptionBuilder.withArgParser(Value.class), OptionBuilder.hasArg(true), "testOption", new HashSet<String>(Arrays.asList(new String[]{"--testOption"})), false, false, false },
            new Object[] { OptionBuilder.withArgParser(DefaultValue.class), OptionBuilder.hasArg(false), "testOption", new HashSet<String>(Arrays.asList(new String[]{"--testOption"})), false, true, true },
            new Object[] { OptionBuilder.withArgParser(DefaultValue.class), OptionBuilder.hasArg(false), "testOption", new HashSet<String>(Arrays.asList(new String[]{"--testOption"})), false, false, true },
            new Object[] { OptionBuilder.withArgParser(Default.class), OptionBuilder.hasArg(false), "testOption", new HashSet<String>(Arrays.asList(new String[]{"--testOption"})), false, true, true },
            new Object[] { OptionBuilder.withArgParser(Default.class), OptionBuilder.hasArg(false), "testOption", new HashSet<String>(Arrays.asList(new String[]{"--testOption"})), false, false, true },
            new Object[] { OptionBuilder.withArgParser(Alert.class), OptionBuilder.hasArg(false), "testOption", new HashSet<String>(Arrays.asList(new String[]{"--testOption"})), false, false, true }
        });
    }

    @Test
    public void testGetTriggers() {
        assertEquals(triggers, option.getTriggers());
    }

    @Test
    public void testGetPrefixes() {
        assertEquals(prefixes, option.getPrefixes());
    }

    @Test
    public void testIsRequired() {
        assertEquals(required, option.isRequired());
    }

    @Test
    public void testFindOption() {
        Option testOption = OptionBuilder.noArg(name).build();
        Option test2Option = OptionBuilder.noArg("test2Option").build();
        Option test3Option = OptionBuilder.hasArg("test3Arg").build();

        Option result = testOption.findOption("--testOption");
        assertSame(testOption, result);

        result = testOption.findOption("--testOption2");
        assertNull(result);

        result = testOption.findOption("--test2Option");
        assertNull(result);

        result = testOption.findOption("--test3Option");
        assertNull(result);

        result = testOption.findOption("--test");
        assertNull(result);

        result = testOption.findOption("--testOption=");
        assertNull(result);

        result = testOption.findOption("--testOption=" + name);
        assertSame(testOption, result);

        result = testOption.findOption("--test3Option=" + name);
        assertNull(result);

        List<Option> options = Arrays.asList(testOption, test2Option, test3Option);
        Option parser = OptionBuilder.withArgParser(BasicParser.class).hasArg(false).build();
        Option combinedOption = OptionBuilder.withValues(parser).build();
        options.add(combinedOption);
        result = testOption.findOption("--test3Option");
        assertNull(result);

        result = testOption.findOption("--test3Option=" + name);
        assertNull(result);
    }

    @Test
    public void testCanProcess() {
        if (canProcess) {
            ParseContext context = new ParseContext();
            context.setArguments(new String[]{"--testOption", "testValue"});
            assertTrue(option.canProcess(context, 0));
        } else {
            ParseContext context = new ParseContext();
            context.setArguments(new String[]{"--testOption", "testValue"});
            assertFalse(option.canProcess(context, 0));
        }
    }

    @Test
    public void testValidate() {
        if (isValid) {
            try {
                option.validate(new BasicCommandLine());
            } catch (OptionException e) {
                fail("validate should not throw an exception");
            }
        } else {
            try {
                option.validate(new BasicCommandLine());
                fail("validate should have thrown an exception");
            } catch (OptionException e) {
                // expected
            }
        }
    }

    @Test
    public void testProcess() {
        BasicCommandLine cmd = new BasicCommandLine();
        ListIterator<String> args = Arrays.asList("--testOption", "testValue").listIterator();

        option.process(cmd, args);
        assertEquals("testValue", cmd.getOptionValue(name));
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine cmd = new BasicCommandLine();
        option.defaults(cmd);

        if (option instanceof OptionBuilder) {
            OptionBuilder builder = (OptionBuilder) option;
            if (builder.getValue() != null) {
                assertEquals(builder.getValue().getValue(), cmd.getOptionValue(name));
            }
            if (builder.hasDefaultValue()) {
                assertEquals(builder.getDefaultValue(), cmd.getOptionValue(name));
            }
            if (builder.hasArg()) {
                assertEquals(builder.getArgName(), cmd.getOptionValue(name));
            }
            if (builder.hasValueSeparators()) {
                assertEquals(builder.getValueSeparator(), cmd.getOptionValues(name));
            }
        }
    }

    @Test
    public void testHelpLines() {
        final int depth = 1;
        final Set helpSettings = new LinkedHashSet<>();
        final Comparator comp = null;

        List<HelpLine> lines = option.helpLines(depth, helpSettings, comp);
        assertFalse(lines.isEmpty());

        HelpFormatter formatter = new HelpFormatter();
        String help = formatter.format(lines, false);

        System.out.println(help);
    }

    @Test
    public void testAppendUsage() {
        final StringBuffer buffer = new StringBuffer();
        final Set helpSettings = new LinkedHashSet<>();
        final Comparator comp = null;

        option.appendUsage(buffer, helpSettings, comp);
        assertTrue(buffer.length() > 0);

        HelpFormatter formatter = new HelpFormatter();
        String help = formatter.format(option.helpLines(0, helpSettings, comp), false);
        assertEquals(buffer.toString(), help);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals(name, option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        String description = option.getDescription();
        if (description != null) {
            assertTrue(description.length() > 0);
        }
    }

    @Test
    public void testGetId() {
        assertEquals(getId(), option.getId());
    }
}