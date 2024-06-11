import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionTest {

    private Option option;

    @Before
    public void setUp() {
        option = new Option() {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {

            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {

            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
                return false;
            }

            @Override
            public Set getTriggers() {
                return null;
            }

            @Override
            public Set getPrefixes() {
                return null;
            }

            @Override
            public void validate(WriteableCommandLine commandLine) throws OptionException {

            }

            @Override
            public List helpLines(int depth, Set helpSettings, Comparator comp) {
                return null;
            }

            @Override
            public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {

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
            public int getId() {
                return 0;
            }

            @Override
            public Option findOption(String trigger) {
                return null;
            }

            @Override
            public boolean isRequired() {
                return false;
            }

            @Override
            public Option getParent() {
                return null;
            }

            @Override
            public void setParent(Option parent) {

            }
        };
    }

    @Test
    public void testProcess() {

    }

    @Test
    public void testDefaults() {

    }

    @Test
    public void testCanProcess() {

    }

    @Test
    public void testGetTriggers() {

    }

    @Test
    public void testGetPrefixes() {

    }

    @Test
    public void testValidate() {

    }

    @Test
    public void testHelpLines() {

    }

    @Test
    public void testAppendUsage() {

    }

    @Test
    public void testGetPreferredName() {

    }

    @Test
    public void testGetDescription() {

    }

    @Test
    public void testGetId() {

    }

    @Test
    public void testFindOption() {

    }

    @Test
    public void testIsRequired() {

    }

    @Test
    public void testGetParent() {

    }

    @Test
    public void testSetParent() {

    }
}