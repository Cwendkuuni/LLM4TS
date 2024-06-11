package org.apache.commons.cli2.option;

import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionGroup;
import org.apache.commons.cli2.ParseContext;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private OptionGroup optionGroup;
    private ParseContext parseContext;

    @Before
    public void setUp() {
        final List options = Arrays.asList(
                OptionBuilder.withName("a")
                        .withDescription("Option A")
                        .createOption(),
                OptionBuilder.withName("b")
                        .withDescription("Option B")
                        .createOption(),
                OptionBuilder.withName("c")
                        .withDescription("Option C")
                        .createOption(),
                OptionBuilder.withName("group")
                        .withDescription("Group of options")
                        .withMinValues(1)
                        .withMaxValues(2)
                        .isRequired()
                        .createGroup()
        );

        optionGroup = new OptionGroupImpl(options, "group");
        parseContext = new ParseContext();
    }

    @Test
    public void testCanProcess() {
        // Test option A
        assertTrue(optionGroup.canProcess(parseContext, "a"));

        // Test option B
        assertTrue(optionGroup.canProcess(parseContext, "b"));

        // Test option C
        assertTrue(optionGroup.canProcess(parseContext, "c"));

        // Test trigger 'group'
        assertTrue(optionGroup.canProcess(parseContext, "group"));

        // Test invalid trigger
        assertFalse(optionGroup.canProcess(parseContext, "d"));

        // Test invalid trigger with options
        assertFalse(optionGroup.canProcess(parseContext, "d"));

        // Test invalid trigger with group
        assertFalse(optionGroup.canProcess(parseContext, "group"));
        assertFalse(optionGroup.canProcess(parseContext, "group d"));
    }

    @Test
    public void testProcess() {
        // Test valid option
        parseContext.parse(optionGroup, new String[]{"-a", "value_a"});
        assertEquals("value_a", parseContext.getOptionValue("a"));

        // Test valid group
        parseContext.parse(optionGroup, new String[]{"--group"});
        assertTrue(parseContext.hasOption("group"));

        // Test valid group with options
        parseContext.parse(optionGroup, new String[]{"--group", "-a", "value_a"});
        assertTrue(parseContext.hasOption("group"));
        assertEquals("value_a", parseContext.getOptionValue("a"));

        // Test invalid option with group
        parseContext.parse(optionGroup, new String[]{"-d", "--group"});
        assertFalse(parseContext.hasOption("d"));

        // Test invalid option with group and value
        parseContext.parse(optionGroup, new String[]{"-d", "--group", "value_d"});
        assertFalse(parseContext.hasOption("d"));

        // Test valid option with group
        parseContext.parse(optionGroup, new String[]{"-a", "--group", "value_g"});
        assertTrue(parseContext.hasOption("group"));
        assertEquals("value_g", parseContext.getOptionValue("group"));
        assertTrue(parseContext.hasOption("a"));
        assertEquals("value_a", parseContext.getOptionValue("a"));
    }

    @Test
    public void testValidate() {
        // Test valid input
        parseContext.parse(optionGroup, new String[]{"-a", "value_a", "--group"});
        assertEquals(2, parseContext.getNumOptions());
        assertEquals(1, parseContext.getNumGroups());

        // Test missing option
        parseContext.parse(optionGroup, new String[]{"--group"});
        assertEquals(1, parseContext.getNumOptions());
        assertEquals(1, parseContext.getNumGroups());
        assertTrue(parseContext.hasOption("group"));
        assertFalse(parseContext.hasOption("a"));

        // Test too many options
        parseContext.parse(optionGroup, new String[]{"-a", "value_a", "-a", "value_a"});
        assertEquals(2, parseContext.getNumOptions());
        assertEquals(1, parseContext.getNumGroups());
        assertTrue(parseContext.hasOption("a"));
        assertEquals("value_a", parseContext.getOptionValue("a"));
        assertTrue(parseContext.hasOption("a"));
        assertEquals("value_a", parseContext.getOptionValue("a"));

        try {
            parseContext.parse(optionGroup, new String[]{"-a", "value_a", "-a", "value_a", "-a"});
            // The test should fail here
        } catch (OptionException e) {
            // Expected
        }
    }

    @Test
    public void testAppendUsage() {
        final StringBuffer buffer = new StringBuffer();
        optionGroup.appendUsage(buffer, null, null);
        assertEquals("-gROUP- OPTIONS:\n" +
                "  --group  GROUP\n" +
                "  --group=VALUE  GROUP\n" +
                "OPTIONS:\n" +
                "  -a  Option A\n" +
                "  -a=VALUE  Option A\n" +
                "  -b  Option B\n" +
                "  -b=VALUE  Option B\n" +
                "  -c  Option C\n" +
                "  -c=VALUE  Option C\n", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        final List helpLines = optionGroup.helpLines(1, null, null);
        assertEquals(15, helpLines.size());
        final String helpLine = (String) helpLines.get(0);
        assertEquals("-gROUP- OPTIONS:", helpLine.substring(0, 22));
    }
}