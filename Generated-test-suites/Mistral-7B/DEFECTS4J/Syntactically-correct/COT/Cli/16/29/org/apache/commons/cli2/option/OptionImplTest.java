import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OptionImplTest {

    @Test
    public void testOptionImplConstructor() {
        Option option = new OptionImpl(1, false);
        Assert.assertEquals(1, option.getId());
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testCanProcessBasic() {
        Option option = mock(Option.class);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        ListIterator arguments = mock(ListIterator.class);

        when(commandLine.getArguments()).thenReturn(arguments);
        when(arguments.next()).thenReturn("arg1");

        boolean result = option.canProcess(commandLine, arguments);
        Assert.assertTrue(result);
    }

    @Test
    public void testCanProcessEndOfArguments() {
        Option option = mock(Option.class);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        ListIterator arguments = mock(ListIterator.class);

        when(commandLine.getArguments()).thenReturn(arguments);

        boolean result = option.canProcess(commandLine, arguments);
        Assert.assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithEmptyPrefixSet() {
        Option option = new OptionImpl(1, false);
        Set prefixes = new HashSet();

        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixesWithPreferredName() {
        Option option = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "prefix-test";
            }
        };
        Set prefixes = new HashSet();
        prefixes.add("test-prefix");

        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithTriggerWithoutPrefix() {
        Option option = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "prefix-test";
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet() {{
                    add("test-trigger");
                }};
            }
        };
        Set prefixes = new HashSet();
        prefixes.add("prefix-test-prefix");

        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixesWithTriggerWithPrefix() {
        Option option = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "prefix-test";
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet() {{
                    add("prefix-test-trigger");
                }};
            }
        };
        Set prefixes = new HashSet();
        prefixes.add("prefix-test");

        option.checkPrefixes(prefixes);
    }
}