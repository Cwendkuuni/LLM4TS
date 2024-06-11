package org.apache.commons.cli2.option;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.Parser;
import org.apache.commons.cli2.util.Arguments;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptionImplTest {

    @Test
    public void testOptionImplConstructor() {
        OptionImpl option = new OptionImpl(1, true);
        assertTrue(option.isRequired());
        assertEquals(1, option.getId());
    }

    @Test
    public void testCanProcessNullCommandLine() {
        OptionImpl option = new OptionImpl(1, false);
        assertFalse(option.canProcess(null, null));
    }

    @Test
    public void testCanProcessEmptyArgumentList() {
        OptionImpl option = new OptionImpl(1, true);
        assertFalse(option.canProcess(new Parser(), new Arguments()));
    }

    @Test
    public void testCanProcessNoMatch() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
        };
        assertFalse(option.canProcess(new Parser(), Arrays.asList("arg1", "arg2").iterator()));
    }

    @Test
    public void testCanProcessMatch() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return "arg".equals(argument);
            }
        };
        assertTrue(option.canProcess(new Parser(), Arrays.asList("arg").iterator()));
    }

    @Test
    public void testEqualsNull() {
        OptionImpl option = new OptionImpl(1, false);
        assertFalse(option.equals(null));
    }

    @Test
    public void testEqualsDifferentType() {
        OptionImpl option = new OptionImpl(1, false);
        assertFalse(option.equals("test"));
    }

    @Test
    public void testEqualsSameOption() {
        OptionImpl option1 = new OptionImpl(1, false);
        OptionImpl option2 = new OptionImpl(1, false);
        assertTrue(option1.equals(option2));
    }

    @Test
    public void testEqualsDifferentId() {
        OptionImpl option1 = new OptionImpl(1, false);
        OptionImpl option2 = new OptionImpl(2, false);
        assertFalse(option1.equals(option2));
    }

    @Test
    public void testEqualsDifferentPreferredName() {
        OptionImpl option1 = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "test";
            }
        };
        OptionImpl option2 = new OptionImpl(1, false);
        assertFalse(option1.equals(option2));
    }

    @Test
    public void testEqualsDifferentDescription() {
        OptionImpl option1 = new OptionImpl(1, false) {
            @Override
            public String getDescription() {
                return "test description";
            }
        };
        OptionImpl option2 = new OptionImpl(1, false);
        assertFalse(option1.equals(option2));
    }

    @Test
    public void testEqualsDifferentPrefixes() {
        OptionImpl option1 = new OptionImpl(1, false) {
            @Override
            public Set<String> getPrefixes() {
                return new HashSet<String>(Arrays.asList("test1", "test2"));
            }
        };
        OptionImpl option2 = new OptionImpl(1, false) {
            @Override
            public Set<String> getPrefixes() {
                return new HashSet<String>(Arrays.asList("test2", "test3"));
            }
        };
        assertFalse(option1.equals(option2));
    }

    @Test
    public void testEqualsDifferentTriggers() {
        OptionImpl option1 = new OptionImpl(1, false) {
            @Override
            public List<String> getTriggers() {
                return Arrays.asList("test1", "test2");
            }
        };
        OptionImpl option2 = new OptionImpl(1, false) {
            @Override
            public List<String> getTriggers() {
                return Arrays.asList("test2", "test3");
            }
        };
        assertFalse(option1.equals(option2));
    }

    @Test
    public void testHashCodeNullPreferredName() {
        OptionImpl option = new OptionImpl(1, false);
        int hashCode = option.hashCode();
        OptionImpl option2 = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return null;
            }
        };
        assertNotEquals(hashCode, option2.hashCode());
    }

    @Test
    public void testHashCodeNullDescription() {
        OptionImpl option = new OptionImpl(1, false);
        int hashCode = option.hashCode();
        OptionImpl option2 = new OptionImpl(1, false) {
            @Override
            public String getDescription() {
                return null;
            }
        };
        assertNotEquals(hashCode, option2.hashCode());
    }

    @Test
    public void testHashCodeNullPrefixes() {
        OptionImpl option = new OptionImpl(1, false);
        int hashCode = option.hashCode();
        OptionImpl option2 = new OptionImpl(1, false) {
            @Override
            public Set<String> getPrefixes() {
                return null;
            }
        };
        assertNotEquals(hashCode, option2.hashCode());
    }

    @Test
    public void testHashCodeNullTriggers() {
        OptionImpl option = new OptionImpl(1, false);
        int hashCode = option.hashCode();
        OptionImpl option2 = new OptionImpl(1, false) {
            @Override
            public List<String> getTriggers() {
                return null;
            }
        };
        assertNotEquals(hashCode, option2.hashCode());
    }

    @Test
    public void testHashCodeSameOption() {
        OptionImpl option = new OptionImpl(1, false);
        int hashCode = option.hashCode();
        OptionImpl option2 = new OptionImpl(1, false);
        assertEquals(hashCode, option2.hashCode());
    }

    @Test
    public void testHashCodeDifferentOptions() {
        OptionImpl option1 = new OptionImpl(1, false);
        OptionImpl option2 = new OptionImpl(2, false);
        assertNotEquals(option1.hashCode(), option2.hashCode());
    }

    @Test
    public void testFindOptionNullTrigger() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public List<String> getTriggers() {
                return Arrays.asList("test1", "test2");
            }
        };
        assertNull(option.findOption(null));
    }

    @Test
    public void testFindOptionNoMatch() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public List<String> getTriggers() {
                return Arrays.asList("test1", "test2");
            }
        };
        assertNull(option.findOption("test3"));
    }

    @Test
    public void testFindOptionMatch() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public List<String> getTriggers() {
                return Arrays.asList("test1", "test2");
            }
        };
        OptionImpl testOption = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "test1";
            }
        };
        assertEquals(testOption, option.findOption("test1"));
    }
}