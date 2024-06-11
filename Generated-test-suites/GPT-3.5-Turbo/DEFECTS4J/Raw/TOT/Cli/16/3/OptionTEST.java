package org.apache.commons.cli2;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class OptionTest {

    private Option option;

    @Before
    public void setup() {
        option = new Option() {

            @Override
            public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public Set getTriggers() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Set getPrefixes() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void validate(WriteableCommandLine commandLine) throws OptionException {
                // TODO Auto-generated method stub
                
            }

            @Override
            public List<Option.HelpLine> helpLines(int depth, Set helpSettings, Comparator comp) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public String getPreferredName() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getDescription() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public int getId() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public Option findOption(String trigger) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean isRequired() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public Option getParent() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void setParent(Option parent) {
                // TODO Auto-generated method stub
                
            }
            
        };
    }

    @Test
    public void testProcess() {
        List<String> inputArgs = new ArrayList<>();
        inputArgs.add("arg1");
        inputArgs.add("arg2");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        ListIterator<String> argsIterator = inputArgs.listIterator();

        try {
            option.process(commandLine, argsIterator);
        } catch (OptionException e) {
            fail("Process should not throw any exception");
        }

        assertFalse("The argsIterator should be at the last index after processing", argsIterator.hasNext());
        assertEquals("The commandLine should contain the processed arguments", Arrays.asList("arg1", "arg2"), commandLine.getArgs());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        option.defaults(commandLine);

        assertTrue("The commandLine should not be empty after applying defaults", commandLine.hasArgs());
    }

    @Test
    public void testCanProcessArgument() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String argument = "arg";

        boolean canProcess = option.canProcess(commandLine, argument);

        assertFalse("The option should not be able to process the argument", canProcess);
    }

    @Test
    public void testCanProcessArguments() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");

        ListIterator<String> argumentsIterator = arguments.listIterator();

        boolean canProcess = option.canProcess(commandLine, argumentsIterator);

        assertFalse("The option should not be able to process the arguments", canProcess);
        assertTrue("The argumentsIterator should not be modified", equals(arguments, getArguments(argumentsIterator)));
    }

    @Test
    public void testGetTriggers() {
        Set<String> expectedTriggers = new HashSet<>(Arrays.asList("--", "-"));

        Set<String> triggers = option.getTriggers();

        assertEquals("The triggers of the option should be correct", expectedTriggers, triggers);
    }

    @Test
    public void testGetPrefixes() {
        Set<String> expectedPrefixes = new HashSet<>(Arrays.asList("--", "-"));

        Set<String> prefixes = option.getPrefixes();

        assertEquals("The prefixes of the option should be correct", expectedPrefixes, prefixes);
    }

    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        try {
            option.validate(commandLine);
        } catch (OptionException e) {
            fail("Validation should not throw any exception");
        }
    }

    @Test
    public void testHelpLines() {
        int depth = 0;
        Set<HelpSettings.Setting> helpSettings = new HashSet<>();
        Comparator<HelpLine> comp = Comparator.naturalOrder();

        List<HelpLine> helpLines = option.helpLines(depth, helpSettings, comp);

        assertNotNull("The help lines should not be null", helpLines);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set<HelpSettings.Setting> helpSettings = new HashSet<>();
        Comparator<Option> comp = Comparator.naturalOrder();

        option.appendUsage(buffer, helpSettings, comp);

        assertEquals("The buffer should contain the usage information", "Usage: ", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        String preferredName = option.getPreferredName();

        assertNull("The preferred name of the option should be null", preferredName);
    }

    @Test
    public void testGetDescription() {
        String description = option.getDescription();

        assertNull("The description of the option should be null", description);
    }

    @Test
    public void testGetId() {
        int id = option.getId();

        assertEquals("The id of the option should be 0", 0, id);
    }

    @Test
    public void testFindOption() {
        String trigger = "--option";

        Option matchingOption = option.findOption(trigger);

        assertNull("No matching option should be found", matchingOption);
    }

    @Test
    public void testIsRequired() {
        boolean isRequired = option.isRequired();

        assertFalse("The option should not be required", isRequired);
    }

    @Test
    public void testGetParent() {
        Option parent = option.getParent();

        assertNull("The parent of the option should be null", parent);
    }

    @Test
    public void testSetParent() {
        Option parent = new Option() {

            @Override
            public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public Set getTriggers() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Set getPrefixes() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void validate(WriteableCommandLine commandLine) throws OptionException {
                // TODO Auto-generated method stub
                
            }

            @Override
            public List<Option.HelpLine> helpLines(int depth, Set helpSettings, Comparator comp) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public String getPreferredName() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getDescription() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public int getId() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public Option findOption(String trigger) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean isRequired() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public Option getParent() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void setParent(Option parent) {
                // TODO Auto-generated method stub
                
            }
            
        };

        option.setParent(parent);

        assertSame("The parent option should be set correctly", parent, option.getParent());
    }

    private List<String> getArguments(ListIterator<String> argsIterator) {
        List<String> arguments = new ArrayList<>();
        while (argsIterator.hasNext()) {
            arguments.add(argsIterator.next());
        }
        return arguments;
    }

    private <T> boolean equals(List<T> list1, List<T> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        Iterator<T> it1 = list1.iterator();
        Iterator<T> it2 = list2.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            T obj1 = it1.next();
            T obj2 = it2.next();
            if (!obj1.equals(obj2)) {
                return false;
            }
        }
        return true;
    }

}