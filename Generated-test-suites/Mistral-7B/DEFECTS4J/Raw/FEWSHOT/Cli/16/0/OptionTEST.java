package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> args = Arrays.asList("--option1", "value1", "--option2", "value2").listIterator();

        Option option1 = new MockOption("option1", "option1", "o1") {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
                commandLine.setValue("option1", "value1");
            }
        };

        Option option2 = new MockOption("option2", "option2", "o2") {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
                commandLine.setValue("option2", "value2");
            }
        };

        option1.process(commandLine, args);
        option2.process(commandLine, args);

        assertEquals("value1", commandLine.getValue("option1"));
        assertEquals("value2", commandLine.getValue("option2"));
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        Option option = new MockOption("option", "option", "o") {
            @Override
            public void defaults(WriteableCommandLine commandLine) {
                commandLine.setValue("option", "defaultValue");
            }
        };

        option.defaults(commandLine);

        assertEquals("defaultValue", commandLine.getValue("option"));
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        Option option = new MockOption("option", "option", "o") {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return "value".equals(argument);
            }
        };

        List<String> args = Arrays.asList("--option", "value").listIterator();

        assertTrue(option.canProcess(commandLine, args));
        assertFalse(option.canProcess(commandLine, args)); // should not be called again

        List<String> invalidArgs = Arrays.asList("--invalidOption", "--option", "invalidValue").listIterator();

        assertFalse(option.canProcess(commandLine, invalidArgs));
        assertFalse(option.canProcess(commandLine, invalidArgs)); // should not be called again
    }

    @Test
    public void testCanProcessList() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        Option option = new MockOption("option", "option", "o") {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator args) {
                return "value".equals(args.next());
            }
        };

        List<String> args = Arrays.asList("--option", "value").listIterator();

        assertTrue(option.canProcess(commandLine, args));
        assertFalse(option.canProcess(commandLine, args)); // should not be called again

        List<String> invalidArgs = Arrays.asList("--invalidOption", "--option", "invalidValue").listIterator();

        assertFalse(option.canProcess(commandLine, invalidArgs));
        assertFalse(option.canProcess(commandLine, invalidArgs)); // should not be called again
    }

    @Test
    public void testGetTriggers() {
        Option option = new MockOption("option", "option", "o") {
            @Override
            public Set getTriggers() {
                return new HashSet(Arrays.asList("--", "-o"));
            }
        };

        Set triggers = option.getTriggers();

        assertTrue(triggers.contains("--"));
        assertTrue(triggers.contains("-o"));
        assertEquals(2, triggers.size());
    }

    @Test
    public void testGetPrefixes() {
        Option option = new MockOption("option", "option", "o") {
            @Override
            public Set getPrefixes() {
                return new HashSet(Arrays.asList("--", "-"));
            }
        };

        Set prefixes = option.getPrefixes();

        assertTrue(prefixes.contains("--"));
        assertTrue(prefixes.contains("-"));
        assertEquals(2, prefixes.size());
    }

    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        Option option = new MockOption("option", "option", "o") {
            @Override
            public void validate(WriteableCommandLine commandLine) throws OptionException {
                if (!commandLine.hasOption("option")) {
                    throw new OptionException("option is required");
                }
            }
        };

        List<String> args = Arrays.asList("--option1", "value1", "--option2", "value2").listIterator();

        option.validate(commandLine);
        option.validate(commandLine); // should not be called again

        List<String> missingOptionArgs = Arrays.asList("--option1", "value1").listIterator();

        try {
            option.validate(commandLine);
            fail("OptionException expected");
        } catch (OptionException e) {
            // expected
        }

        List<String> invalidOptionArgs = Arrays.asList("--invalidOption", "value1").listIterator();

        try {
            option.validate(commandLine);
            fail("OptionException expected");
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testHelpLines() {
        Option option = new MockOption("option", "option", "o") {
            @Override
            public String getPreferredName() {
                return "preferredName";
            }

            @Override
            public String getDescription() {
                return "option description";
            }

            @Override
            public int getId() {
                return 1;
            }
        };

        Comparator<Option> comp = new Comparator<Option>() {
            @Override
            public int compare(Option o1, Option o2) {
                return o1.getId() - o2.getId();
            }
        };

        Set helpSettings = new HashSet();

        List<Option> options = new ArrayList();
        options.add(option);

        List<Option> helpLines = option.helpLines(1, helpSettings, comp);

        assertEquals(1, helpLines.size());

        String expectedHelpLine = "  -o, --option   preferredName\n" +
                "    option description\n";

        String helpLine = ((HelpLineImpl) helpLines.get(0)).toString();

        assertEquals(expectedHelpLine, helpLine);
    }

    @Test
    public void testAppendUsage() {
        Option option = new MockOption("option", "option", "o") {
            @Override
            public String getPreferredName() {
                return "preferredName";
            }

            @Override
            public String getDescription() {
                return "option description";
            }

            @Override
            public int getId() {
                return 1;
            }
        };

        StringBuffer buffer = new StringBuffer();

        option.appendUsage(buffer, new HashSet(), null);

        String expectedUsage = "  -o, --option preferredName\n" +
                "    option description\n";

        String usage = buffer.toString();

        assertEquals(expectedUsage, usage);
    }

    @Test
    public void testGetPreferredName() {
        Option option = new MockOption("option", "option", "o") {
            @Override
            public String getPreferredName() {
                return "preferredName";
            }
        };

        assertEquals("preferredName", option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        Option option = new MockOption("option", "option", "o") {
            @Override
            public String getDescription() {
                return "option description";
            }
        };

        assertEquals("option description", option.getDescription());
    }

    @Test
    public void testId() {
        Option option1 = new MockOption("option1", "option1", "o1") {
            @Override
            public int getId() {
                return 1;
            }
        };

        Option option2 = new MockOption("option2", "option2", "o2");

        assertEquals(1, option1.getId());
        assertEquals(0, option2.getId());
    }

    @Test
    public void testFindOption() {
        Option parentOption = new MockOption("parentOption", "parentOption", "p") {
            @Override
            public Option findOption(String trigger) {
                if ("childOption".equals(trigger)) {
                    return new MockOption("childOption", "childOption", "c");
                }
                return null;
            }
        };

        Option childOption = parentOption.findOption("childOption");

        assertSame(childOption, new MockOption("childOption", "childOption", "c"));
    }

    @Test
    public void testIsRequired() {
        Option option = new MockOption("option", "option", "o") {
            @Override
            public boolean isRequired() {
                return true;
            }
        };

        assertTrue(option.isRequired());
    }

    @Test
    public void testHasParent() {
        Option parentOption = new MockOption("parentOption", "parentOption", "p");

        Option childOption1 = new MockOption("childOption1", "childOption1", "c1") {
            @Override
            public Option getParent() {
                return parentOption;
            }
        };

        Option childOption2 = new MockOption("childOption2", "childOption2", "c2");

        parentOption.addOption(childOption1);

        assertEquals(childOption1, parentOption.findOption("c1"));
        assertNull(parentOption.findOption("c2"));
    }
}

class MockOption implements Option {
    private String name;
    private String shortName;
    private String description;
    private int id;
    private Set triggers;
    private Set prefixes;
    private boolean required;
    private Option parent;

    public MockOption(String name, String shortName, String description, int id, Set triggers, Set prefixes, boolean required, Option parent) {
        this.name = name;
        this.shortName = shortName;
        this.description = description;
        this.id = id;
        this.triggers = triggers;
        this.prefixes = prefixes;
        this.required = required;
        this.parent = parent;
    }

    public MockOption(String name, String shortName, String description, int id) {
        this(name, shortName, description, id, new HashSet(), new HashSet(), false, null);
    }

    @Override
    public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
    }

    @Override
    public void defaults(WriteableCommandLine commandLine) {
    }

    @Override
    public boolean canProcess(WriteableCommandLine commandLine, String argument) {
        return true;
    }

    @Override
    public boolean canProcess(WriteableCommandLine commandLine, ListIterator args) {
        return true;
    }

    @Override
    public Set getTriggers() {
        return triggers;
    }

    @Override
    public Set getPrefixes() {
        return prefixes;
    }

    @Override
    public void validate(WriteableCommandLine commandLine) throws OptionException {
    }

    @Override
    public List helpLines(int depth, Set helpSettings, Comparator comp) {
        return new ArrayList();
    }

    @Override
    public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
    }

    @Override
    public String getPreferredName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Option findOption(String trigger) {
        return null;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public Option getParent() {
        return parent;
    }

    @Override
    public void setParent(Option parent) {
    }
}