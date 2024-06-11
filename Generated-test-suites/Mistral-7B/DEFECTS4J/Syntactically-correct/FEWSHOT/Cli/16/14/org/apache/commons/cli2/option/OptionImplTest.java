package org.apache.commons.cli2.option;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Before;
import org.junit.Test;

public class OptionImplTest {
    private OptionImpl option;
    private WriteableCommandLine commandLine;
    private List<String> arguments;
    private ResourceHelper resourceHelper;

    @Before
    public void setup() {
        option = new OptionImpl(1, true) {
            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("--", "-p"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("option1", "opt", "trigger1"));
            }
        };
        commandLine = mock(WriteableCommandLine.class);
        arguments = Arrays.asList("--option1", "value1");
        resourceHelper = mock(ResourceHelper.class);
    }

    @Test
    public void canProcess() {
        when(commandLine.getArgs()).thenReturn(arguments.subList(1, arguments.size()));
        when(resourceHelper.getMessage(ResourceConstants.OPTION_TRIGGER_NEEDS_PREFIX, anyString(), anyString())).thenThrow(new IllegalArgumentException());

        assertFalse(option.canProcess(commandLine, arguments.listIterator()));
    }

    @Test
    public void canProcessWithPrefix() {
        when(commandLine.getArgs()).thenReturn(arguments.subList(1, arguments.size()));

        assertTrue(option.canProcess(commandLine, arguments.listIterator()));
    }

    @Test
    public void canProcessWithNoPrefix() {
        List<String> noPrefixArguments = Arrays.asList("value1", "--option1");
        when(commandLine.getArgs()).thenReturn(noPrefixArguments.subList(1, noPrefixArguments.size()));

        assertFalse(option.canProcess(commandLine, noPrefixArguments.listIterator()));
    }

    @Test
    public void checkPrefixes() {
        when(resourceHelper.getMessage(ResourceConstants.OPTION_TRIGGER_NEEDS_PREFIX, anyString(), anyString())).thenReturn("prefix message");

        option.checkPrefixes(Arrays.asList("--", "-p", "--prefix"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkPrefixesWithMissingPrefix() {
        when(resourceHelper.getMessage(ResourceConstants.OPTION_TRIGGER_NEEDS_PREFIX, anyString(), anyString())).thenReturn("prefix message");

        option.checkPrefixes(Arrays.asList("--", "-q", "--prefix"));
    }

    @Test
    public void equals() {
        OptionImpl option2 = new OptionImpl(1, true) {
            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("--", "-p"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("option1", "opt", "trigger1"));
            }
        };

        OptionImpl option3 = new OptionImpl(1, true) {
            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("--", "-p"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("option1", "opt", "trigger2"));
            }
        };

        OptionImpl option4 = new OptionImpl(2, false);

        assertTrue(option.equals(option));
        assertTrue(option.equals(option2));
        assertFalse(option.equals(option3));
        assertFalse(option.equals(option4));
    }

    @Test
    public void hashCode() {
        OptionImpl option2 = new OptionImpl(1, true) {
            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("--", "-p"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("option1", "opt", "trigger1"));
            }
        };

        OptionImpl option3 = new OptionImpl(1, true) {
            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("--", "-p"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("option1", "opt", "trigger2"));
            }
        };

        OptionImpl option4 = new OptionImpl(2, false);

        int optionHashCode = option.hashCode();
        int option2HashCode = option2.hashCode();
        int option3HashCode = option3.hashCode();
        int option4HashCode = option4.hashCode();

        assertTrue(option.hashCode() == optionHashCode);
        assertTrue(option.hashCode() == option2.hashCode());
        assertFalse(option.hashCode() == option3.hashCode());
        assertFalse(option.hashCode() == option4.hashCode());
    }

    @Test
    public void findOption() {
        OptionImpl option2 = new OptionImpl(2, true) {
            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("--", "-p"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("option2", "opt2", "trigger2"));
            }
        };

        assertNull(option.findOption("option3"));
        assertNull(option.findOption("opt"));
        assertNull(option.findOption("trigger2"));
        assertNull(option.findOption("option2"));
        assertNotNull(option.findOption("option1"));
    }

    @Test
    public void isRequired() {
        assertTrue(option.isRequired());
    }

    @Test
    public void defaults() {
        option.defaults(commandLine);
        assertNull(commandLine.getValue("option1"));
    }
}