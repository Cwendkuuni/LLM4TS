import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parser.DefaultParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class OptionTest {

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = new MockOption("option");

        List<String> args = new ArrayList<>();
        args.add("--option");
        args.add("value");

        option.process(commandLine, args.listIterator());

        Assert.assertEquals("value", commandLine.getOptionValues("option"));
    }

    @Test(expected = OptionException.class)
    public void testProcessWithInvalidArgument() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = new MockOption("option");

        List<String> args = new ArrayList<>();
        args.add("--option");
        args.add("invalid");

        option.process(commandLine, args.listIterator());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = new MockOption("option");

        option.defaults(commandLine);

        Assert.assertNull(commandLine.getOptionValues("option"));
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = new MockOption("option");

        List<String> args = new ArrayList<>();
        args.add("--option");
        args.add("value");

        Assert.assertTrue(option.canProcess(commandLine, args.listIterator()));

        args.set(1, "invalid");

        Assert.assertFalse(option.canProcess(commandLine, args.listIterator()));
    }

    @Test
    public void testGetTriggers() {
        Option option = new MockOption("option");

        Set<String> triggers = option.getTriggers();

        Assert.assertNotNull(triggers);
        Assert.assertEquals(1, triggers.size());
        Assert.assertEquals("--option", triggers.iterator().next());
    }

    @Test
    public void testGetPrefixes() {
        Option option = new MockOption("option");

        Set<String> prefixes = option.getPrefixes();

        Assert.assertNotNull(prefixes);
        Assert.assertEquals(1, prefixes.size());
        Assert.assertEquals("--", prefixes.iterator().next());
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidCommandLine() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = new MockOption("option");

        option.validate(commandLine);

        commandLine.addOptionValue("option", "invalid");

        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        Option option = new MockOption("option") {
            @Override
            public List helpLines(int depth, Set helpSettings, Comparator comp) {
                return new ArrayList() {{
                    add(new HelpLineImpl("Option test", "option", getDescription()));
                }};
            }
        };

        List<HelpLine> helpLines = option.helpLines(1, new LinkedHashMap<>(), null);

        Assert.assertEquals(1, helpLines.size());
        HelpLine helpLine = helpLines.get(0);
        Assert.assertEquals("Option test", helpLine.getHeader());
        Assert.assertEquals("option", helpLine.getOptionSpec().getOption());
        Assert.assertEquals("Option test", helpLine.getOptionSpec().getLongOpt());
        Assert.assertEquals(option.getDescription(), helpLine.getDescription());
    }

    @Test
    public void testAppendUsage() {
        Option option = new MockOption("option") {
            @Override
            public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
                buffer.append("--option <value>");
            }
        };

        StringBuffer buffer = new StringBuffer();
        option.appendUsage(buffer, new LinkedHashMap<>(), null);

        Assert.assertEquals("--option <value>", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        Option option = new MockOption("option") {
            @Override
            public String getPreferredName() {
                return "test";
            }
        };

        Assert.assertEquals("test", option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        Option option = new MockOption("option") {
            @Override
            public String getDescription() {
                return "Test option";
            }
        };

        Assert.assertEquals("Test option", option.getDescription());
    }

    @Test
    public void testGetId() {
        Option option = new MockOption("option") {
            @Override
            public int getId() {
                return 1;
            }
        };

        Assert.assertEquals(1, option.getId());
    }

    @Test
    public void testFindOption() {
        Option option1 = new MockOption("option1");
        Option option2 = new MockOption("option2");

        Option parent = new Option() {
            @Override
            public Option findOption(String trigger) {
                if ("option1".equals(trigger)) {
                    return option1;
                } else if ("option2".equals(trigger)) {
                    return option2;
                }
                return null;
            }
        };

        Assert.assertEquals(option1, parent.findOption("option1"));
        Assert.assertEquals(option2, parent.findOption("option2"));
        Assert.assertNull(parent.findOption("unknown"));
    }

    @Test
    public void testIsRequired() {
        Option option = new MockOption("option") {
            @Override
            public boolean isRequired() {
                return true;
            }
        };

        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testGetParent() {
        Option parent = new Option() {};

        Option option = new MockOption("option") {
            @Override
            public Option getParent() {
                return parent;
            }
        };

        Assert.assertEquals(parent, option.getParent());
    }

    @Test
    public void testSetParent() {
        Option parent = new Option() {};

        Option option = new MockOption("option") {
            @Override
            public void setParent(Option parent) {
                this.parent = parent;
            }
        };

        option.setParent(parent);

        Assert.assertEquals(parent, option.getParent());
    }

    private static class MockOption implements Option {
        private String name;
        private Option parent;

        public MockOption(String name) {
            this.name = name;
        }

        @Override
        public void process(WriteableCommandLine commandLine, ListIterator args)
                throws OptionException {
        }

        @Override
        public void defaults(WriteableCommandLine commandLine) {
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, String argument) {
            return true;
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
            return true;
        }

        @Override
        public Set getTriggers() {
            return new TreeSet<>();
        }

        @Override
        public Set getPrefixes() {
            return new TreeSet<>(Arrays.asList("--", "-"));
        }

        @Override
        public void validate(WriteableCommandLine commandLine)
                throws OptionException {
        }

        @Override
        public List helpLines(int depth, Set helpSettings, Comparator comp) {
            return null;
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
            return "";
        }

        @Override
        public int getId() {
            return 0;
        }

        @Override
        public Option findOption(String trigger) {
            return null;
        }

        @Override
        public boolean isRequired() {
            return false;
        }

        @Override
        public Option getParent() {
            return parent;
        }

        @Override
        public void setParent(Option parent) {
        }
    }
}