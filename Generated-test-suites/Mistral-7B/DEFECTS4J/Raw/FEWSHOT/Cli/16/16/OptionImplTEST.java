package org.apache.commons.cli2.option;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

public class OptionImplTest {

    @Test
    public void testOptionImplConstructor() {
        final OptionImpl option = new AbstractOption() {
            @Override
            public String getPreferredName() {
                return "option1";
            }

            @Override
            public String getDescription() {
                return "option1 description";
            }

            @Override
            public int getId() {
                return 1;
            }

            @Override
            public List<String> getPrefixes() {
                return Arrays.asList("--opt1", "-o1");
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("option1", "o1");
            }
        };

        assertEquals(1, option.getId());
        assertTrue(option.isRequired());
        assertEquals("option1", option.getPreferredName());
        assertEquals("option1 description", option.getDescription());
        assertEquals(Arrays.asList("--opt1", "-o1"), option.getPrefixes());
        assertEquals(Arrays.asList("option1", "o1"), option.getTriggers());
    }

    @Test
    public void testCanProcess() {
        final OptionImpl option = new AbstractOption() {
            @Override
            public String getPreferredName() {
                return "option1";
            }

            @Override
            public String getDescription() {
                return "option1 description";
            }

            @Override
            public int getId() {
                return 1;
            }

            @Override
            public List<String> getPrefixes() {
                return Arrays.asList("--opt1", "-o1");
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("option1", "o1");
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine,
                                       ListIterator<String> arguments) {
                return arguments.next().equals("option1");
            }
        };

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        final List<String> arguments = new ArrayList<>();
        arguments.add("option1");
        arguments.add("arg1");

        assertTrue(option.canProcess(commandLine, arguments.listIterator()));
    }

    @Test
    public void testCanProcess_noMatch() {
        final OptionImpl option = new AbstractOption() {
            @Override
            public String getPreferredName() {
                return "option1";
            }

            @Override
            public String getDescription() {
                return "option1 description";
            }

            @Override
            public int getId() {
                return 1;
            }

            @Override
            public List<String> getPrefixes() {
                return Arrays.asList("--opt1", "-o1");
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("option1", "o1");
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine,
                                       ListIterator<String> arguments) {
                return arguments.next().equals("arg1");
            }
        };

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        final List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");

        assertFalse(option.canProcess(commandLine, arguments.listIterator()));
    }

    @Test
    public void testCheckPrefixes() {
        final OptionImpl option = new AbstractOption() {
            @Override
            public String getPreferredName() {
                return "option1";
            }

            @Override
            public String getDescription() {
                return "option1 description";
            }

            @Override
            public int getId() {
                return 1;
            }

            @Override
            public List<String> getPrefixes() {
                return Arrays.asList("--opt1", "-o1");
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("option1", "o1");
            }
        };

        final Set<String> prefixes = new HashSet<>(Arrays.asList("--opt", "-o"));
        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes_prefixNotFound() {
        final OptionImpl option = new AbstractOption() {
            @Override
            public String getPreferredName() {
                return "option1";
            }

            @Override
            public String getDescription() {
                return "option1 description";
            }

            @Override
            public int getId() {
                return 1;
            }

            @Override
            public List<String> getPrefixes() {
                return Arrays.asList("--opt2", "-o2");
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("option1", "o1");
            }
        };

        final Set<String> prefixes = new HashSet<>(Arrays.asList("--opt", "-o"));
        option.checkPrefixes(prefixes);
    }
}