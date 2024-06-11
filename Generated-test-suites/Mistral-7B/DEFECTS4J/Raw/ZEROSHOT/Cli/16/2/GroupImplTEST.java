package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.ReverseStringComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class GroupImplTest {

    @org.junit.Test
    public void testGroupImpl() {
        List<Option> optionsList = new ArrayList<>();

        Option option1 = new Option("option1", "Option 1", true, "o1");
        Option option2 = new Option("option2", "Option 2", false, "o2");
        Option option3 = new Option("option3", "Option 3", false, "o3");
        Option option4 = new Option("option4", "Option 4", false, "o4");
        Option option5 = new Option("option5", "Option 5", false, "o5");
        Argument arg1 = new Argument("arg1", "Argument 1", true);
        Argument arg2 = new Argument("arg2", "Argument 2", true);

        optionsList.add(option1);
        optionsList.add(option2);
        optionsList.add(option3);
        optionsList.add(option4);
        optionsList.add(option5);

        List<Argument> anonymousArgList = new ArrayList<>();
        anonymousArgList.add(arg1);
        anonymousArgList.add(arg2);

        Group group = new GroupImpl(optionsList, "MyGroup", "Description of MyGroup", 1, 3, anonymousArgList);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        // test canProcess method
        String[] testArgs = {"o1", "o2", "o3", "o1", "o3", "arg1", "arg2"};
        for (String testArg : testArgs) {
            boolean result = group.canProcess(commandLine, testArg);
            assertTrue("Unexpected result for testArg: " + testArg, result);
        }

        // test getPrefixes method
        Set<String> prefixes = group.getPrefixes();
        assertEquals("Unexpected size of prefixes", 0, prefixes.size());

        // test getTriggers method
        Set<String> triggers = group.getTriggers();
        List<String> triggersList = new ArrayList<>(triggers);
        Collections.sort(triggersList);
        List<String> expectedTriggersList = Collections.singletonList("MyGroup");
        assertEquals("Unexpected triggers", expectedTriggersList, triggersList);

        // test process method
        commandLine.parse(testArgs);
        group.process(commandLine, commandLine.getArgList().listIterator());

        // test getName method
        assertEquals("Unexpected name", "MyGroup", group.getPreferredName());

        // test getDescription method
        assertEquals("Unexpected description", "Description of MyGroup", group.getDescription());

        // test appendUsage method
        StringBuilder buffer = new StringBuilder();
        Set<DisplaySetting> helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);
        group.appendUsage(buffer, helpSettings, null);
        String expectedUsage = "| MyGroup (Description of MyGroup)\n";
        String actualUsage = buffer.toString();
        assertEquals("Unexpected usage", expectedUsage, actualUsage);

        // test helpLines method
        List<HelpLine> helpLines = group.helpLines(1, helpSettings, null);
        assertEquals("Unexpected number of helpLines", 4, helpLines.size());
    }
}