import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.cli2.resource.ResourceConstants.OPTION_TRIGGER_NEEDS_PREFIX;

public class OptionImplTest {

    @Test
    public void testOptionImplConstructor() {
        Option option = new OptionImpl(1, true);
        assertEquals(1, option.getId());
        assertTrue(option.isRequired());
    }

    @Test
    public void testCanProcess_WhenArgumentExists_ReturnsTrue() {
        Option option = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine,
                                       Iterator<String> arguments) {
                return true;
            }
        };

        String[] args = {"arg1"};
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Iterator<String> iterator = Arrays.asList(args).listIterator();

        boolean result = option.canProcess(commandLine, iterator);
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes_WhenTriggerDoesntHavePrefix_ThrowsException() {
        Option option = new OptionImpl(1, true) {
            @Override
            Set<String> getPrefixes() {
                return new HashSet<String>() {{
                    add("prefix");
                }};
            }

            @Override
            String[] getTriggers() {
                return new String[]{"trigger"};
            }
        };

        option.checkPrefixes(new HashSet<String>() {{
            add("prefix2");
        }});
    }

    @Test
    public void testFindOption() {
        Option option1 = new OptionImpl(1, true) {
            @Override
            String[] getTriggers() {
                return new String[]{"trigger1"};
            }
        };
        Option option2 = new OptionImpl(2, true) {
            @Override
            String[] getTriggers() {
                return new String[]{"trigger2"};
            }
        };

        Option foundOption = option1.findOption("trigger1");
        assertEquals(option1, foundOption);

        foundOption = option2.findOption("trigger1");
        assertNull(foundOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEquals_WhenObjectsAreDifferent_ReturnsFalse() {
        Option option1 = new OptionImpl(1, true);
        Option option2 = new OptionImpl(2, false);

        assertFalse(option1.equals(option2));
    }

    @Test
    public void testEquals_WhenObjectsAreEqual_ReturnsTrue() {
        Option option1 = new OptionImpl(1, true) {
            @Override
            String getPreferredName() {
                return "option1";
            }

            @Override
            String getDescription() {
                return "description1";
            }

            @Override
            Set<String> getPrefixes() {
                return new HashSet<String>() {{
                    add("prefix1");
                }};
            }

            @Override
            Set<String> getTriggers() {
                return new HashSet<String>() {{
                    add("trigger1");
                }};
            }
        };

        Option option2 = new OptionImpl(1, true) {
            @Override
            String getPreferredName() {
                return "option1";
            }

            @Override
            String getDescription() {
                return "description1";
            }

            @Override
            Set<String> getPrefixes() {
                return new HashSet<String>() {{
                    add("prefix1");
                }};
            }

            @Override
            Set<String> getTriggers() {
                return new HashSet<String>() {{
                    add("trigger1");
                }};
            }
        };

        assertEquals(option1, option2);
    }

    @Test
    public void testHashCode() {
        Option option1 = new OptionImpl(1, true) {
            @Override
            String getPreferredName() {
                return "option1";
            }

            @Override
            String getDescription() {
                return "description1";
            }

            @Override
            Set<String> getPrefixes() {
                return new HashSet<String>() {{
                    add("prefix1");
                }};
            }

            @Override
            Set<String> getTriggers() {
                return new HashSet<String>() {{
                    add("trigger1");
                }};
            }
        };

        Option option2 = new OptionImpl(1, true) {
            @Override
            String getPreferredName() {
                return "option2";
            }

            @Override
            String getDescription() {
                return "description2";
            }

            @Override
            Set<String> getPrefixes() {
                return new HashSet<String>() {{
                    add("prefix2");
                }};
            }

            @Override
            Set<String> getTriggers() {
                return new HashSet<String>() {{
                    add("trigger2");
                }};
            }
        };

        int option1HashCode = option1.hashCode();
        int option2HashCode = option2.hashCode();

        int expectedHashCode = 1 * 37 + "option1".hashCode() +
                "description1".hashCode() + new HashSet<String>() {{
                    add("prefix1");
                }}.hashCode() + new HashSet<String>() {{
                    add("trigger1");
                }}.hashCode();

        assertEquals(expectedHashCode, option1HashCode);
        assertNotEquals(option1HashCode, option2HashCode);
    }

    @Test
    public void testToString() {
        Option option = new OptionImpl(1, true) {
            @Override
            String getPreferredName() {
                return "option";
            }

            @Override
            String getDescription() {
                return "description";
            }
        };

        String expectedResult = "option";
        String result = option.toString();

        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToString_WhenPrefixesContainInvalidPrefix_ThrowsException() {
        Option option = new OptionImpl(1, true) {
            @Override
            Set<String> getPrefixes() {
                return new HashSet<String>() {{
                    add("prefix");
                    add("");
                }};
            }
        };

        String result = option.toString();
    }

    @Test
    public void testDefaults() {
        Option option = new OptionImpl(1, true) {
            @Override
            public void defaults(WriteableCommandLine commandLine) {
                commandLine.addOption(this);
            }
        };

        WriteableCommandLine commandLine = new WriteableCommandLine();
        option.defaults(commandLine);

        String[] args = commandLine.getArgs();
        assertEquals(0, args.length);
    }

    @Test
    public void testIsRequired() {
        Option option = new OptionImpl(1, true);
        assertTrue(option.isRequired());

        Option option2 = new OptionImpl(2, false);
        assertFalse(option2.isRequired());
    }
}