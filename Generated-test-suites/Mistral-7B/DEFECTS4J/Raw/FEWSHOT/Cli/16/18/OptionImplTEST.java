package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.util.TestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptionImplTest {

    @Test
    public void testOptionImplConstruction() {
        OptionImpl option = new OptionImpl(1, true);
        assertEquals(1, option.getId());
        assertTrue(option.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionImplWithEmptyPrefixSetThrowsException() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            protected void checkPrefixes(Set prefixes) {
                super.checkPrefixes(prefixes);
            }
        };
        Set<String> prefixes = new HashSet<>();
        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCanProcessOptionImplWithSingleArgument() {
        OptionImpl option = new OptionImplWithArg() {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }
        };
        WriteableCommandLine commandLine = TestUtils.createCommandLine();
        List<String> args = Arrays.asList("myOption", "myArgument");
        commandLine.parse(args.iterator());

        assertTrue(option.canProcess(commandLine, args.iterator()));
    }

    @Test
    public void testCanProcessOptionImplWithMultipleArguments() {
        OptionImpl option = new OptionImplWithArg() {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return argument.equals("myArgument");
            }
        };
        WriteableCommandLine commandLine = TestUtils.createCommandLine();
        List<String> args = Arrays.asList("myOption", "anotherArgument", "myArgument");
        commandLine.parse(args.iterator());

        assertTrue(option.canProcess(commandLine, args.iterator()));
    }

    @Test
    public void testCanProcessOptionImplWithNoArgument() {
        OptionImpl option = new OptionImpl() {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
                return true;
            }
        };
        WriteableCommandLine commandLine = TestUtils.createCommandLine();
        List<String> args = Arrays.asList("myOption");
        commandLine.parse(args.iterator());

        assertTrue(option.canProcess(commandLine, args.listIterator()));
    }

    @Test
    public void testCanProcessOptionImplWithNoArgumentAndNoPrefix() {
        OptionImpl option = new OptionImpl() {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
                return false;
            }
        };
        WriteableCommandLine commandLine = TestUtils.createCommandLine();
        List<String> args = Arrays.asList("myOption");
        commandLine.parse(args.iterator());

        assertFalse(option.canProcess(commandLine, args.listIterator()));
    }

    @Test
    public void testCanProcessOptionImplWithNoArgumentAndPrefix() {
        OptionImpl option = new OptionImpl() {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
                return arguments.next().equals("--myOption");
            }
        };
        WriteableCommandLine commandLine = TestUtils.createCommandLine();
        List<String> args = Arrays.asList("--myOption");
        commandLine.parse(args.iterator());

        assertTrue(option.canProcess(commandLine, args.listIterator()));
    }

    @Test
    public void testCanProcessOptionImplWithArgumentAndPrefix() {
        OptionImpl option = new OptionImpl() {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
                return arguments.next().equals("--myOption") && arguments.hasNext() && arguments.next().equals("myArgument");
            }
        };
        WriteableCommandLine commandLine = TestUtils.createCommandLine();
        List<String> args = Arrays.asList("--myOption", "myArgument");
        commandLine.parse(args.iterator());

        assertTrue(option.canProcess(commandLine, args.listIterator()));
    }

    @Test
    public void testCanProcessOptionImplWithArgumentButNoPrefix() {
        OptionImpl option = new OptionImpl() {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
                return arguments.next().equals("myOption") && arguments.hasNext() && arguments.next().equals("myArgument");
            }
        };
        WriteableCommandLine commandLine = TestUtils.createCommandLine();
        List<String> args = Arrays.asList("myOption", "myArgument");
        commandLine.parse(args.iterator());

        assertTrue(option.canProcess(commandLine, args.listIterator()));
    }

    @Test
    public void testToStringOptionImpl() {
        OptionImpl option = new OptionImpl() {
            @Override
            public String toString() {
                return "My Option";
            }
        };
        assertEquals("My Option", option.toString());
    }

    @Test
    public void testToStringOptionImplWithPrefix() {
        OptionImpl option = new OptionImpl() {
            @Override
            public String toString() {
                return "--myOption";
            }
        };
        assertEquals("--myOption", option.toString());
    }

    @Test
    public void testEqualsOptionImpl() {
        OptionImpl option1 = new OptionImplWithArg() {
            @Override
            public String getPreferredName() {
                return "option1";
            }
        };
        OptionImpl option2 = new OptionImplWithArg() {
            @Override
            public String getPreferredName() {
                return "option1";
            }
        };
        assertTrue(option1.equals(option2));
        OptionImpl option3 = new OptionImplWithArg() {
            @Override
            public String getPreferredName() {
                return "option2";
            }
        };
        assertFalse(option1.equals(option3));
        OptionImpl option4 = null;
        assertFalse(option1.equals(option4));
    }

    @Test
    public void testHashCodeOptionImpl() {
        OptionImpl option1 = new OptionImplWithArg() {
            @Override
            public String getPreferredName() {
                return "option1";
            }
        };
        OptionImpl option2 = new OptionImplWithArg() {
            @Override
            public String getPreferredName() {
                return "option1";
            }
        };
        assertEquals(option1.hashCode(), option2.hashCode());

        OptionImpl option3 = new OptionImplWithArg() {
            @Override
            public String getPreferredName() {
                return "option2";
            }
        };
        assertNotEquals(option1.hashCode(), option3.hashCode());

        OptionImpl option4 = null;
        assertNotEquals(option1.hashCode(), option4.hashCode());
    }

    @Test
    public void testFindOptionOptionImpl() {
        OptionImpl option1 = new OptionImplWithArg() {
            @Override
            public String getPreferredName() {
                return "option1";
            }
            @Override
            public Iterable<String> getTriggers() {
                return new ArrayList<String>() {{
                    add("option1");
                    add("--o1");
                }};
            }
        };
        OptionImpl option2 = new OptionImplWithArg() {
            @Override
            public String getPreferredName() {
                return "option2";
            }
            @Override
            public Iterable<String> getTriggers() {
                return new ArrayList<String>() {{
                    add("option2");
                    add("--o2");
                }};
            }
        };

        OptionImpl foundOption = option1.findOption("--o1");
        assertEquals(option1, foundOption);

        foundOption = option1.findOption("--o2");
        assertEquals(option2, foundOption);

        foundOption = option1.findOption("option1");
        assertEquals(option1, foundOption);

        foundOption = option1.findOption("option3");
        assertNull(foundOption);
    }

    @Test
    public void testFindOptionOptionImplWithEmptyTriggerList() {
        OptionImpl option = new OptionImpl() {
            @Override
            public Iterable<String> getTriggers() {
                return new ArrayList<>();
            }
        };
        assertNull(option.findOption("trigger"));
    }

    @Test
    public void testIsRequiredOptionImpl() {
        OptionImpl option = new OptionImpl(1, true);
        assertTrue(option.isRequired());

        OptionImpl option2 = new OptionImpl(2, false);
        assertFalse(option2.isRequired());
    }

    @Test
    public void testDefaultsOptionImpl() {
        OptionImpl option = new OptionImpl() {
            @Override
            public void defaults(WriteableCommandLine commandLine) {
                commandLine.setValue("myOption", "myValue");
            }
        };
        WriteableCommandLine commandLine = TestUtils.createCommandLine();
        option.defaults(commandLine);
        assertEquals("myValue", commandLine.getValue("myOption"));
    }

    @Test
    public void testCheckPrefixesOptionImpl() {
        OptionImpl option = new OptionImplWithArg() {
            @Override
            public Iterable<String> getPrefixes() {
                return new ArrayList<String>() {{
                    add("pfx1");
                    add("pfx2");
                }};
            }
        };
        Set<String> prefixes = new HashSet<String>() {{
            add("pfx1");
        }};
        option.checkPrefixes(prefixes);

        prefixes = new HashSet<String>() {{
            add("pfx1");
            add("pfx3");
        }};
        try {
            option.checkPrefixes(prefixes);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(ResourceConstants.OPTION_TRIGGER_NEEDS_PREFIX, e.getMessage());
        }
    }

    private static class OptionImplWithArg extends OptionImpl {
        private final Iterable<String> triggers;

        OptionImplWithArg() {
            super(1, false);
            this.triggers = new ArrayList<String>() {{
                add("myOption");
            }};
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, String argument) {
            return true;
        }

        @Override
        public Iterable<String> getTriggers() {
            return this.triggers;
        }

        @Override
        public Iterable<String> getPrefixes() {
            return new ArrayList<String>();
        }

        @Override
        public String getPreferredName() {
            return "myOption";
        }

        @Override
        public String getDescription() {
            return "My Option Description";
        }
    }

    private static WriteableCommandLine createCommandLine() {
        ResourceHelper helper = ResourceHelper.getResourceHelper();
        return new TestWriteableCommandLine(helper);
    }
}

class TestWriteableCommandLine extends WriteableCommandLine {
    public TestWriteableCommandLine(ResourceHelper helper) {
        super(helper);
        setHelpOption(false);
    }

    @Override
    public void setValue(String key, String value) {
        super.setValue(key, value);
        System.out.println("setValue: key=" + key + ", value=" + value);
    }
}