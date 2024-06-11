package org.apache.commons.cli2.option;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class OptionImplTest {

    @Mock
    private WriteableCommandLine commandLine;
    @Mock
    private Parser parser;
    @Mock
    private Set<String> prefixes;
    @Mock
    private Set<String> triggers;

    private OptionImpl option;

    @Before
    public void setUp() {
        option = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "testOption";
            }

            @Override
            public String getDescription() {
                return "Test option description";
            }

            @Override
            public Set<String> getPrefixes() {
                return prefixes;
            }

            @Override
            public Set<String> getTriggers() {
                return triggers;
            }

            @Override
            public boolean equals(final Object obj) {
                return super.equals(obj);
            }

            @Override
            public int hashCode() {
                return super.hashCode();
            }

            @Override
            public void defaults(final WriteableCommandLine commandLine) {
                // no-op
            }
        };
    }

    @Test
    public void testConstructor() {
        assertEquals(1, option.getId());
        assertTrue(option.isRequired());
        assertNull(option.getPreferredName());
        assertNull(option.getDescription());
        assertNull(option.getPrefixes());
        assertNull(option.getTriggers());
    }

    @Test
    public void testCanProcess() {
        Mockito.when(parser.parse(commandLine, Arrays.asList("test", "testOption")))
                .thenReturn(commandLine);

        Mockito.when(commandLine.hasOption(Mockito.eq("testOption")))
                .thenReturn(true);

        assertTrue(option.canProcess(commandLine, Arrays.asList("test", "testOption").listIterator()));

        Mockito.when(parser.parse(commandLine, Arrays.asList("test", "testOther")))
                .thenReturn(commandLine);

        assertFalse(option.canProcess(commandLine, Arrays.asList("test", "testOther").listIterator()));

        Mockito.when(parser.parse(commandLine, Arrays.asList()))
                .thenReturn(commandLine);

        assertFalse(option.canProcess(commandLine, Arrays.asList().listIterator()));
    }

    @Test
    public void testFindOption() {
        Mockito.when(triggers.contains("testTrigger"))
                .thenReturn(true);

        Option result = option.findOption("testTrigger");
        assertSame(option, result);

        Mockito.when(triggers.contains("otherTrigger"))
                .thenReturn(false);

        assertNull(option.findOption("otherTrigger"));
    }

    @Test
    public void testEquals() {
        Option otherOption = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "testOption";
            }

            @Override
            public String getDescription() {
                return "Test option description";
            }

            @Override
            public Set<String> getPrefixes() {
                return prefixes;
            }

            @Override
            public Set<String> getTriggers() {
                return triggers;
            }
        };

        assertEquals(option, otherOption);

        Option otherOption2 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "otherTestOption";
            }

            @Override
            public String getDescription() {
                return "Other test option description";
            }

            @Override
            public Set<String> getPrefixes() {
                return prefixes;
            }

            @Override
            public Set<String> getTriggers() {
                return triggers;
            }
        };

        assertFalse(option.equals(otherOption2));
    }

    @Test
    public void testHashCode() {
        int optionHashCode = option.hashCode();

        Option otherOption = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "testOption";
            }

            @Override
            public String getDescription() {
                return "Test option description";
            }

            @Override
            public Set<String> getPrefixes() {
                return prefixes;
            }

            @Override
            public Set<String> getTriggers() {
                return triggers;
            }
        };

        int otherOptionHashCode = otherOption.hashCode();

        assertEquals(optionHashCode, otherOptionHashCode);
    }
}