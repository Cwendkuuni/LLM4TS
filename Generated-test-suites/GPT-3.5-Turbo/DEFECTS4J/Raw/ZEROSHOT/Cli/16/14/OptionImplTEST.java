import org.junit.Test;

import static org.mockito.Mockito.*;

public class OptionImplTest {

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        ListIterator arguments = mock(ListIterator.class);

        when(arguments.hasNext()).thenReturn(true);
        when(arguments.next()).thenReturn("test");

        OptionImpl optionImpl = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
                return true;
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting level, Set helpSettings) {

            }

            @Override
            public String getPreferredName() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public Set getPrefixes() {
                return null;
            }

            @Override
            public Set getTriggers() {
                return null;
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }

            @Override
            public Option findOption(String trigger) {
                return null;
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {

            }

        };

        optionImpl.canProcess(commandLine, arguments);

        verify(optionImpl, times(1)).canProcess(commandLine, arguments);
    }

    @Test
    public void testToString() {
        OptionImpl optionImpl = new OptionImpl(1, true) {

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting level, Set helpSettings) {

            }

            @Override
            public String getPreferredName() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public Set getPrefixes() {
                return null;
            }

            @Override
            public Set getTriggers() {
                return null;
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public Option findOption(String trigger) {
                return null;
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {

            }

        };

        optionImpl.toString();

        verify(optionImpl, times(1)).toString();
    }

    @Test
    public void testEquals() {
        OptionImpl optionImpl1 = new OptionImpl(1, true) {
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting level, Set helpSettings) {

            }

            @Override
            public String getPreferredName() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public Set getPrefixes() {
                return null;
            }

            @Override
            public Set getTriggers() {
                return null;
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public Option findOption(String trigger) {
                return null;
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {

            }
        };

        OptionImpl optionImpl2 = new OptionImpl(2, false) {
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting level, Set helpSettings) {

            }

            @Override
            public String getPreferredName() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public Set getPrefixes() {
                return null;
            }

            @Override
            public Set getTriggers() {
                return null;
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public Option findOption(String trigger) {
                return null;
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {

            }
        };

        optionImpl1.equals(optionImpl2);
        optionImpl2.equals(optionImpl1);

        verify(optionImpl1, times(1)).equals(optionImpl2);
        verify(optionImpl2, times(1)).equals(optionImpl1);
    }

    @Test
    public void testHashCode() {
        OptionImpl optionImpl = new OptionImpl(1, true) {
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting level, Set helpSettings) {

            }

            @Override
            public String getPreferredName() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public Set getPrefixes() {
                return null;
            }

            @Override
            public Set getTriggers() {
                return null;
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public Option findOption(String trigger) {
                return null;
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {

            }

        };

        optionImpl.hashCode();

        verify(optionImpl, times(1)).hashCode();
    }

    @Test
    public void testFindOption() {
        OptionImpl optionImpl = new OptionImpl(1, true) {
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting level, Set helpSettings) {

            }

            @Override
            public String getPreferredName() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public Set getPrefixes() {
                return null;
            }

            @Override
            public Set getTriggers() {
                return null;
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public Option findOption(String trigger) {
                return null;
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {

            }

        };

        optionImpl.findOption("trigger");

        verify(optionImpl, times(1)).findOption("trigger");
    }

    @Test
    public void testIsRequired() {
        OptionImpl optionImpl = new OptionImpl(1, true) {

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting level, Set helpSettings) {

            }

            @Override
            public String getPreferredName() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public Set getPrefixes() {
                return null;
            }

            @Override
            public Set getTriggers() {
                return null;
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public Option findOption(String trigger) {
                return null;
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {

            }

        };

        optionImpl.isRequired();

        verify(optionImpl, times(1)).isRequired();
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        OptionImpl optionImpl = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
                return false;
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting level, Set helpSettings) {

            }

            @Override
            public String getPreferredName() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public Set getPrefixes() {
                return null;
            }

            @Override
            public Set getTriggers() {
                return null;
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public Option findOption(String trigger) {
                return null;
            }
        };

        optionImpl.defaults(commandLine);

        verify(optionImpl, times(1)).defaults(commandLine);
    }

    @Test
    public void testCheckPrefixes() {
        Set prefixes = mock(Set.class);

        OptionImpl optionImpl = new OptionImpl(1, true) {
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting level, Set helpSettings) {

            }

            @Override
            public String getPreferredName() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public Set getPrefixes() {
                return prefixes;
            }

            @Override
            public Set getTriggers() {
                return null;
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public Option findOption(String trigger) {
                return null;
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {

            }

        };

        optionImpl.checkPrefixes(prefixes);

        verify(optionImpl, times(1)).checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefix() {
        Set prefixes = mock(Set.class);
        String trigger = "trigger";

        OptionImpl optionImpl = new OptionImpl(1, true) {
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting level, Set helpSettings) {

            }

            @Override
            public String getPreferredName() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public Set getPrefixes() {
                return prefixes;
            }

            @Override
            public Set getTriggers() {
                return null;
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public Option findOption(String trigger) {
                return null;
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {

            }
        };

        optionImpl.checkPrefix(prefixes, trigger);

        verify(optionImpl, times(1)).checkPrefix(prefixes, trigger);
    }
}