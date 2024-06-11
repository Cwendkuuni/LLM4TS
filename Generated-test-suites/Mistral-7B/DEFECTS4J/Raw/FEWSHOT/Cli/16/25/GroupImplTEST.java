package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionGroupImpl;
import org.apache.commons.cli2.OptionGroupImpl.Minimum;
import org.apache.commons.cli2.OptionGroupImpl.Maximum;
import org.apache.commons.cli2.OptionGroupImpl.Anonymous;
import org.apache.commons.cli2.OptionGroupImpl.Prefixes;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.TestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        final Option opt1 = OptionBuilder.withName("opt1")
                                           .withDescription("option 1")
                                           .create();

        final Option opt2 = OptionBuilder.withName("opt2")
                                           .withDescription("option 2")
                                           .create();

        final Option opt3 = OptionBuilder.withName("opt3")
                                           .withDescription("option 3")
                                           .create();

        final Option arg1 = OptionBuilder.withName("arg1")
                                          .withDescription("argument 1")
                                          .isRequired(false)
                                          .create(new String[]{"arg1"});

        final Option arg2 = OptionBuilder.withName("arg2")
                                          .withDescription("argument 2")
                                          .isRequired(false)
                                          .create(new String[]{"arg2"});

        final List options = Arrays.asList(opt1, opt2, opt3);

        final GroupImpl group = new GroupImpl(options, "group", "Group of options", Minimum.ONE, Maximum.UNLIMITED);

        assertEquals("group", group.getPreferredName());
        assertEquals("Group of options", group.getDescription());
        assertEquals(Minimum.ONE, group.getMinimum());
        assertEquals(Maximum.UNLIMITED, group.getMaximum());
        assertTrue(group.canProcess(TestUtils.createTestCommandLine(), null));
        assertEquals(Collections.unmodifiableList(options), group.getOptions());
        assertSame(opt1, group.findOption("opt1"));
        assertSame(opt2, group.findOption("opt2"));
        assertSame(opt3, group.findOption("opt3"));
        assertSame(arg1, group.findOption("arg1"));
        assertSame(arg2, group.findOption("arg2"));
        assertNull(group.findOption("nonexistent"));
        assertFalse(group.isRequired());

        final StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, Collections.emptySet(), null);
        assertTrue(buffer.toString().contains("group"));
        assertTrue(buffer.toString().contains("option 1"));
        assertTrue(buffer.toString().contains("option 2"));
        assertTrue(buffer.toString().contains("option 3"));
        assertTrue(buffer.toString().contains("argument 1"));
        assertTrue(buffer.toString().contains("argument 2"));

        final List helpLines = group.helpLines(1, Collections.emptySet(), null);
        assertEquals(13, helpLines.size());
        assertTrue(helpLines.get(0).toString().contains("group"));
        assertTrue(helpLines.get(1).toString().contains("option 1"));
        assertTrue(helpLines.get(1).toString().contains("argument 1"));
        assertTrue(helpLines.get(2).toString().contains("option 2"));
        assertTrue(helpLines.get(2).toString().contains("argument 2"));
        assertTrue(helpLines.get(3).toString().contains("option 3"));

        final Set helpSettings = new HashSet();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);

        buffer.setLength(0);
        group.appendUsage(buffer, helpSettings, null);
        assertTrue(buffer.toString().contains("group ("));
        assertTrue(buffer.toString().contains("option 1"));
        assertTrue(buffer.toString().contains("option 2"));
        assertTrue(buffer.toString().contains("option 3"));
        assertTrue(buffer.toString().contains("argument 1"));
        assertTrue(buffer.toString().contains("argument 2"));
        assertTrue(buffer.toString().contains(")"));

        final Map<String, Option> optionMap = new HashMap<>();
        optionMap.put("opt1", opt1);
        optionMap.put("opt2", opt2);
        optionMap.put("opt3", opt3);
        optionMap.put("arg1", arg1);
        optionMap.put("arg2", arg2);

        final GroupImpl groupWithMap = new GroupImpl(Collections.emptyList(), "group_map", "Group of options with map", Minimum.ONE, Maximum.UNLIMITED);
        groupWithMap.setOptionMap(optionMap);

        final String trigger = "opt1";
        final Option found = groupWithMap.findOption(trigger);
        assertSame(opt1, found);

        final String triggerArg = "arg1";
        final Option foundArg = groupWithMap.findOption(triggerArg);
        assertSame(arg1, foundArg);

        final Option invalidOption = OptionBuilder.withName("invalid_option")
                                                  .withDescription("invalid option")
                                                  .create();

        groupWithMap.addOption(invalidOption);
        final Set triggers = groupWithMap.getTriggers();
        assertTrue(triggers.contains("opt1"));
        assertTrue(triggers.contains("opt2"));
        assertTrue(triggers.contains("opt3"));
        assertTrue(triggers.contains("arg1"));
        assertTrue(triggers.contains("arg2"));
        assertTrue(triggers.contains("invalid_option"));

        final Set prefixes = groupWithMap.getPrefixes();
        assertTrue(prefixes.isEmpty());

        final List<Option> anonymousOptions = new ArrayList<>();
        anonymousOptions.add(arg1);
        anonymousOptions.add(arg2);

        final GroupImpl groupAnonymous = new GroupImpl(Collections.emptyList(), "group_anonymous", "Group of options with anonymous", Minimum.ZERO, Maximum.UNLIMITED);
        groupAnonymous.setAnonymous(anonymousOptions);

        final String arg = "arg1";
        final boolean processed = groupAnonymous.canProcess(TestUtils.createTestCommandLine(), arg);
        assertTrue(processed);

        final HelpLine helpLine = new HelpLineImpl(groupAnonymous, 1);
        helpLine.print();
        assertTrue(helpLine.toString().contains("group_anonymous"));
        assertTrue(helpLine.toString().contains("argument 1"));
        assertTrue(helpLine.toString().contains("argument 2"));
    }
}