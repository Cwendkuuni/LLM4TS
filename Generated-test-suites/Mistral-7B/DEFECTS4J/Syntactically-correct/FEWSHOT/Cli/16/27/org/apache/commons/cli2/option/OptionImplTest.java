package org.apache.commons.cli2.option;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Before;
import org.junit.Test;

public class OptionImplTest {
    private static final int ID = 1;
    private static final String PREFERRED_NAME = "opt";
    private static final String DESCRIPTION = "description for opt";
    private static final String[] TRIGGERS = {"-o", "--opt"};
    private static final String[] PREFIXES = {"--global"};

    private OptionImpl option;
    private WriteableCommandLine commandLine;
    private ResourceHelper resourceHelper;

    @Before
    public void setUp() {
        option = new OptionImpl(ID, true) {
            @Override
            public String getPreferredName() {
                return PREFERRED_NAME;
            }

            @Override
            public String getDescription() {
                return DESCRIPTION;
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList(PREFIXES));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList(TRIGGERS));
            }
        };

        commandLine = new WriteableCommandLine();
        resourceHelper = ResourceHelper.getResourceHelper();
    }

    @Test
    public void testCanProcess() {
        Set<String> arguments = new HashSet<>();
        arguments.add("--global");
        arguments.add("opt");
        arguments.add("value");

        assertTrue(option.canProcess(commandLine, arguments.listIterator()));

        arguments.clear();
        arguments.add("--global");
        arguments.add("--opt");
        arguments.add("value");

        assertTrue(option.canProcess(commandLine, arguments.listIterator()));

        arguments.clear();
        arguments.add("--global");
        arguments.add("--opt");
        arguments.add("");

        assertFalse(option.canProcess(commandLine, arguments.listIterator()));

        arguments.clear();
        arguments.add("--global");
        arguments.add("--opt");
        arguments.add("value");
        arguments.add("another");

        assertFalse(option.canProcess(commandLine, arguments.listIterator()));
    }

    @Test
    public void testCheckPrefixes() {
        Set<String> prefixes = new HashSet<>(Arrays.asList(PREFIXES));

        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithMissingPrefix() {
        Set<String> prefixes = new HashSet<>();

        option.checkPrefixes(prefixes);
    }

    @Test
    public void testEquals() {
        OptionImpl option1 = new OptionImpl(ID, true) {
            @Override
            public String getPreferredName() {
                return PREFERRED_NAME;
            }

            @Override
            public String getDescription() {
                return DESCRIPTION;
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList(PREFIXES));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList(TRIGGERS));
            }
        };

        OptionImpl option2 = new OptionImpl(ID, true) {
            @Override
            public String getPreferredName() {
                return PREFERRED_NAME;
            }

            @Override
            public String getDescription() {
                return DESCRIPTION;
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList(PREFIXES));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList(TRIGGERS));
            }
        };

        OptionImpl option3 = new OptionImpl(ID + 1, true) {
            @Override
            public String getPreferredName() {
                return PREFERRED_NAME;
            }

            @Override
            public String getDescription() {
                return DESCRIPTION;
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList(PREFIXES));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList(TRIGGERS));
            }
        };

        OptionImpl option4 = new OptionImpl(ID, false) {
            @Override
            public String getPreferredName() {
                return PREFERRED_NAME;
            }

            @Override
            public String getDescription() {
                return DESCRIPTION;
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList(PREFIXES));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList(TRIGGERS));
            }
        };

        assertTrue(option1.equals(option2));
        assertTrue(option1 == option2);
        assertFalse(option1.equals(option3));
        assertFalse(option1.equals(option4));
    }

    @Test
    public void testFindOption() {
        OptionImpl option1 = new OptionImpl(ID, true) {
            @Override
            public String getPreferredName() {
                return "opt1";
            }

            @Override
            public String getDescription() {
                return DESCRIPTION;
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList(PREFIXES));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList(TRIGGERS));
            }
        };

        OptionImpl option2 = new OptionImpl(ID, true) {
            @Override
            public String getPreferredName() {
                return PREFERRED_NAME;
            }

            @Override
            public String getDescription() {
                return DESCRIPTION;
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList(PREFIXES));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList(Arrays.asList("-o", "--opt").toArray(new String[0])));
            }
        };

        OptionImpl option3 = new OptionImpl(ID, true) {
            @Override
            public String getPreferredName() {
                return PREFERRED_NAME;
            }

            @Override
            public String getDescription() {
                return DESCRIPTION;
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList(Arrays.asList("--global", "--local").toArray(new String[0])));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList(Arrays.asList("-o", "--opt").toArray(new String[0])));
            }
        };

        OptionImpl option4 = new OptionImpl(ID, false) {
            @Override
            public String getPreferredName() {
                return PREFERRED_NAME;
            }

            @Override
            public String getDescription() {
                return DESCRIPTION;
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>();
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList(TRIGGERS));
            }
        };

        assertNull(option.findOption(null));
        assertNull(option.findOption(""));
        assertNull(option.findOption("-"));
        assertNull(option.findOption("--"));

        assertNull(option1.findOption(null));
        assertNull(option1.findOption(""));
        assertNull(option1.findOption("-o"));
        assertNull(option1.findOption("--opt"));
        assertNull(option1.findOption("--global"));
        assertNull(option1.findOption("--global opt"));
        assertNull(option1.findOption("--global opt value"));
        assertNull(option1.findOption("--local opt"));
        assertNull(option1.findOption("--local opt value"));

        assertNull(option2.findOption(null));
        assertNull(option2.findOption(""));
        assertNull(option2.findOption("-o"));
        assertNull(option2.findOption("--opt"));
        assertNull(option2.findOption("--global"));
        assertNull(option2.findOption("--global opt"));
        assertNull(option2.findOption("--global opt value"));
        assertNull(option2.findOption("--local"));
        assertNull(option2.findOption("--local opt"));
        assertNull(option2.findOption("--local opt value"));

        assertNull(option3.findOption(null));
        assertNull(option3.findOption(""));
        assertNull(option3.findOption("-o"));
        assertNull(option3.findOption("--opt"));
        assertNull(option3.findOption("--global"));
        assertNull(option3.findOption("--global opt"));
        assertNull(option3.findOption("--global opt value"));
        assertNull(option3.findOption("--local"));
        assertNull(option3.findOption("--local opt"));
        assertNull(option3.findOption("--local opt value"));

        assertNull(option4.findOption(null));
        assertNull(option4.findOption(""));
        assertNull(option4.findOption("-o"));
        assertNull(option4.findOption("--opt"));
        assertNull(option4.findOption("--global"));
        assertNull(option4.findOption("--global opt"));
        assertNull(option4.findOption("--global opt value"));
        assertNull(option4.findOption("--local"));
        assertNull(option4.findOption("--local opt"));
        assertNull(option4.findOption("--local opt value"));

        assertEquals(option1, option1.findOption("--global opt"));
        assertEquals(option1, option1.findOption("--global opt value"));
        assertEquals(option2, option2.findOption("--global opt"));
        assertEquals(option2, option2.findOption("--global opt value"));
        assertEquals(option3, option3.findOption("--global opt"));
        assertEquals(option3, option3.findOption("--global opt value"));
    }

    @Test
    public void testFindOptionWithEmptyTrigger() {
        OptionImpl option = new OptionImpl(ID, true) {
            @Override
            public String getPreferredName() {
                return PREFERRED_NAME;
            }

            @Override
            public String getDescription() {
                return DESCRIPTION;
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>();
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>();
            }
        };

        assertNull(option.findOption(null));
        assertNull(option.findOption(""));
        assertNull(option.findOption("-"));
        assertNull(option.findOption("--"));
    }

    @Test
    public void testGetId() {
        assertEquals(ID, option.getId());
    }

    @Test
    public void testIsRequired() {
        assertTrue(option.isRequired());
    }

    @Test
    public void testToString() {
        assertEquals("--opt", option.toString());
    }
}