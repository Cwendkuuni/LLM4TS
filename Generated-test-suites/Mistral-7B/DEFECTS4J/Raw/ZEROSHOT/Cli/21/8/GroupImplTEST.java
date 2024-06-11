package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    @org.junit.Test
    public void testGroupImpl() {
        // Test case for empty Group
        List<Option> optionsEmpty = Collections.emptyList();
        String nameEmpty = null;
        String descriptionEmpty = null;
        int minimumEmpty = 0;
        int maximumEmpty = Integer.MAX_VALUE;
        boolean requiredEmpty = false;

        Group groupEmpty = new GroupImpl(optionsEmpty, nameEmpty, descriptionEmpty, minimumEmpty, maximumEmpty, requiredEmpty);

        assertEquals("Empty group should not have a name", null, groupEmpty.getPreferredName());
        assertEquals("Empty group should not have a description", null, groupEmpty.getDescription());
        assertEquals("Empty group should not have any options", 0, groupEmpty.getOptions().size());
        assertEquals("Empty group should not have any anonymous arguments", 0, groupEmpty.getAnonymous().size());
        assertTrue("Empty group should not be required", !groupEmpty.isRequired());

        // Test case for Group with options
        List<Option> optionsTest = new ArrayList<>();
        String nameTest = "test";
        String descriptionTest = "test description";
        int minimumTest = 1;
        int maximumTest = 3;
        boolean requiredTest = false;

        Option option1 = new Option("--option1", "test option 1", true, "t1");
        optionsTest.add(option1);
        Option option2 = new Option("--option2", "test option 2", false, "t2");
        optionsTest.add(option2);

        Group groupTest = new GroupImpl(optionsTest, nameTest, descriptionTest, minimumTest, maximumTest, requiredTest);

        assertEquals("Group with options should have the correct name", nameTest, groupTest.getPreferredName());
        assertEquals("Group with options should have the correct description", descriptionTest, groupTest.getDescription());
        assertEquals("Group with options should have the correct number of options", 2, groupTest.getOptions().size());
        assertEquals("Group with options should have the correct anonymous arguments", 0, groupTest.getAnonymous().size());
        assertFalse("Group with options should not be required", !groupTest.isRequired());

        // Test canProcess
        WriteableCommandLine commandLineTest = new WriteableCommandLine();
        String[] argsTest = new String[]{"--option1", "--option2", "--option3"};
        commandLineTest.parseArgument(argsTest);

        assertTrue("Group with options should be able to process the first option", groupTest.canProcess(commandLineTest, "--option1"));
        assertTrue("Group with options should be able to process the second option", groupTest.canProcess(commandLineTest, "--option2"));
        assertFalse("Group with options should not be able to process a non-existent option", groupTest.canProcess(commandLineTest, "--nonexistent"));

        // Test getTriggers
        Set<String> triggersTest = groupTest.getTriggers();
        assertEquals("Group with options should have the correct triggers", 2, triggersTest.size());
        assertTrue("Group with options should have the correct trigger for option 1", option1.getLongOpt().equals(triggersTest.iterator().next()));
        assertTrue("Group with options should have the correct trigger for option 2", option2.getLongOpt().equals(triggersTest.iterator().next()));

        // Test process
        List<String> argsProcessTest = new ArrayList<>();
        argsProcessTest.add("--option1");
        argsProcessTest.add("--option2");
        argsProcessTest.add("value1");
        argsProcessTest.add("value2");

        ListIterator<String> argsIterProcessTest = argsProcessTest.listIterator();

        try {
            groupTest.process(commandLineTest, argsIterProcessTest);

            // Test option1 process
            Option option1Processed = commandLineTest.getOption("t1");
            assertEquals("Option 1 should have been processed", option1.getValue(), option1Processed.getValue());

            // Test option2 process
            Option option2Processed = commandLineTest.getOption("t2");
            assertNull("Option 2 should not have been processed", option2Processed);

            // Test anonymous arguments
            Argument arg1Processed = null;
            Argument arg2Processed = null;
            for (Option option : groupTest.getOptions()) {
                if (option instanceof Argument) {
                    Argument arg = (Argument) option;
                    if (arg.getName().equals("arg1")) {
                        arg1Processed = commandLineTest.getArg(arg.getIndex());
                    }
                    if (arg.getName().equals("arg2")) {
                        arg2Processed = commandLineTest.getArg(arg.getIndex());
                    }
                }
            }
            assertNull("Anonymous argument 1 should not have been processed", arg1Processed);
            assertNull("Anonymous argument 2 should not have been processed", arg2Processed);
        } catch (OptionException e) {
            // This exception should not be thrown as the given arguments are valid
            assertFalse("OptionException should not be thrown", true);
        }

        // Test appendUsage and helpLines
        StringBuffer bufferUsageTest = new StringBuffer();
        Set<DisplaySetting> helpSettingsTest = new HashSet<>();

        groupTest.appendUsage(bufferUsageTest, helpSettingsTest, null);

        String usageStringTest = bufferUsageTest.toString();
        String expectedUsageTest = "--test\n" +
                "  test description\n" +
                "  --option1 <value>\n" +
                "  --option2\n";

        assertEquals("Usage string should match the expected one", expectedUsageTest, usageStringTest);

        List<HelpLine> helpLinesListTest = groupTest.helpLines(0, helpSettingsTest, null);

        String helpLinesStringTest = "";
        for (HelpLine helpLine : helpLinesListTest) {
            helpLinesStringTest += helpLine.getLine();
        }

        String expectedHelpLinesTest = "--test\n" +
                "  test description\n" +
                "  --option1 <value>\n" +
                "      test option 1 (value)\n" +
                "  --option2\n" +
                "      test option 2\n";

        assertEquals("Help lines string should match the expected one", expectedHelpLinesTest, helpLinesStringTest);

        // Test validate
        WriteableCommandLine commandLineValidateTest = new WriteableCommandLine();

        try {
            groupTest.validate(commandLineValidateTest);
        } catch (OptionException e) {
            // This exception should not be thrown as the command line is valid
            assertFalse("OptionException should not be thrown", true);
        }

        // Test validate with missing options
        commandLineValidateTest.clear();
        commandLineValidateTest.addOption(option1);
        commandLineValidateTest.addOption(option2);
        commandLineValidateTest.addOption("--nonexistent");

        assertThrows(OptionException.class, () -> groupTest.validate(commandLineValidateTest));

        // Test validate with too many options
        commandLineValidateTest.clear();
        commandLineValidateTest.addOption(option1);
        commandLineValidateTest.addOption(option2);
        commandLineValidateTest.addOption(option1);

        assertThrows(OptionException.class, () -> groupTest.validate(commandLineValidateTest));

        // Test validate with missing anonymous arguments
        commandLineValidateTest.clear();
        commandLineValidateTest.addOption(option1);
        commandLineValidateTest.addOption(option2);
        commandLineValidateTest.addOption("--arg1");

        assertThrows(OptionException.class, () -> groupTest.validate(commandLineValidateTest));

        // Test validate with too many anonymous arguments
        commandLineValidateTest.clear();
        commandLineValidateTest.addOption(option1);
        commandLineValidateTest.addOption(option2);
        commandLineValidateTest.addOption("--arg1");
        commandLineValidateTest.addOption("--arg2");

        assertThrows(OptionException.class, () -> groupTest.validate(commandLineValidateTest));

        // Test getMinimum and getMaximum
        assertEquals("Group should have the correct minimum", 1, groupTest.getMinimum());
        assertEquals("Group should have the correct maximum", 3, groupTest.getMaximum());
    }
}