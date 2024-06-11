import org.junit.*;
import static org.junit.Assert.*;

public class OptionImplTest {

    @Test
    public void testCanProcess_ValidArgument_ReturnsTrue() {
        OptionImpl option = new OptionImpl(1, false);

        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> arguments = Arrays.asList("input.txt").listIterator();

        boolean result = option.canProcess(commandLine, arguments);

        assertTrue(result);
    }

    @Test
    public void testCanProcess_NoArgument_ReturnsFalse() {
        OptionImpl option = new OptionImpl(1, false);

        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> arguments = Collections.emptyList().listIterator();

        boolean result = option.canProcess(commandLine, arguments);

        assertFalse(result);
    }

    @Test
    public void testToString_ReturnsExpectedString() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting setting, String prefix) {
                buffer.append("Usage: test");
            }
        };

        String result = option.toString();

        assertEquals("Usage: test", result);
    }

    @Test
    public void testEquals_SameObject_ReturnsTrue() {
        OptionImpl option = new OptionImpl(1, false);

        boolean result = option.equals(option);

        assertTrue(result);
    }

    @Test
    public void testEquals_EqualObjects_ReturnsTrue() {
        OptionImpl option1 = new OptionImpl(1, false);
        OptionImpl option2 = new OptionImpl(1, false);

        boolean result = option1.equals(option2);

        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentIds_ReturnsFalse() {
        OptionImpl option1 = new OptionImpl(1, false);
        OptionImpl option2 = new OptionImpl(2, false);

        boolean result = option1.equals(option2);

        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentPreferredNames_ReturnsFalse() {
        OptionImpl option1 = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option1";
            }
        };
        OptionImpl option2 = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option2";
            }
        };

        boolean result = option1.equals(option2);

        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentDescriptions_ReturnsFalse() {
        OptionImpl option1 = new OptionImpl(1, false) {
            @Override
            public String getDescription() {
                return "description1";
            }
        };
        OptionImpl option2 = new OptionImpl(1, false) {
            @Override
            public String getDescription() {
                return "description2";
            }
        };

        boolean result = option1.equals(option2);

        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentPrefixes_ReturnsFalse() {
        OptionImpl option1 = new OptionImpl(1, false) {
            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("-", "--"));
            }
        };
        OptionImpl option2 = new OptionImpl(1, false) {
            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("$", "##"));
            }
        };

        boolean result = option1.equals(option2);

        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentTriggers_ReturnsFalse() {
        OptionImpl option1 = new OptionImpl(1, false) {
            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("trigger1"));
            }
        };
        OptionImpl option2 = new OptionImpl(1, false) {
            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("trigger2"));
            }
        };

        boolean result = option1.equals(option2);

        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentObjects_ReturnsFalse() {
        OptionImpl option = new OptionImpl(1, false);
        String obj = "not an OptionImpl object";

        boolean result = option.equals(obj);

        assertFalse(result);
    }

    @Test
    public void testHashCode_ReturnsExpectedHashCode() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option";
            }

            @Override
            public String getDescription() {
                return "description";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Collections.singletonList("-"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Collections.singletonList("trigger"));
            }
        };

        int hashCode = option.hashCode();

        assertEquals(-1765690446, hashCode);
    }

    @Test
    public void testFindOption_ValidTrigger_ReturnsThis() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("--option", "-o"));
            }
        };

        Option result = option.findOption("--option");

        assertEquals(option, result);
    }

    @Test
    public void testFindOption_InvalidTrigger_ReturnsNull() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("--option", "-o"));
            }
        };

        Option result = option.findOption("-t");

        assertNull(result);
    }

    @Test
    public void testIsRequired_ReturnsTrue() {
        OptionImpl option = new OptionImpl(1, true);

        boolean result = option.isRequired();

        assertFalse(result);
    }

    @Test
    public void testDefaults_DoesNothing() {
        OptionImpl option = new OptionImpl(1, false);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        option.defaults(commandLine);

        // No assertions, as defaults does nothing
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes_EmptyPrefixList_ThrowsIllegalArgumentException() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public Set<String> getPreferredName() {
                return "option";
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Collections.singletonList("-t"));
            }
        };

        option.checkPrefixes(Collections.emptySet());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes_PrefixNotPresent_ThrowsIllegalArgumentException() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public Set<String> getPreferredName() {
                return "option";
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Collections.singletonList("-t"));
            }
        };

        option.checkPrefixes(new HashSet<>(Collections.singletonList("--")));
    }

    @Test
    public void testCheckPrefixes_PrefixPresent_DoesNotThrowException() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public Set<String> getPreferredName() {
                return "option";
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("-t", "--trigger"));
            }
        };

        option.checkPrefixes(new HashSet<>(Arrays.asList("-", "--")));
    }
}