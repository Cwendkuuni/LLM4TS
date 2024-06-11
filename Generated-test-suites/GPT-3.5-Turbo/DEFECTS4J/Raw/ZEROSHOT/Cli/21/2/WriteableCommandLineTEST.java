import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLine() {

            @Override
            public void addOption(final Option option) {
                // TODO Auto-generated method stub

            }

            @Override
            public void addValue(final Option option, final Object value) {
                // TODO Auto-generated method stub

            }

            @Override
            public List getUndefaultedValues(final Option option) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void setDefaultValues(final Option option, final List defaultValues) {
                // TODO Auto-generated method stub

            }

            @Override
            public void addSwitch(final Option option, final boolean value) throws IllegalStateException {
                // TODO Auto-generated method stub

            }

            @Override
            public void setDefaultSwitch(final Option option, final Boolean defaultSwitch) {
                // TODO Auto-generated method stub

            }

            @Override
            public void addProperty(final Option option, final String property, final String value) {
                // TODO Auto-generated method stub

            }

            @Override
            public void addProperty(final String property, final String value) {
                // TODO Auto-generated method stub

            }

            @Override
            public boolean looksLikeOption(final String argument) {
                // TODO Auto-generated method stub
                return false;
            }
        };
    }
    // TODO: Add tests for all methods in WriteableCommandLine interface

    @Test
    public void testAddOption() {
        // TODO: Test addOption method

    }

    @Test
    public void testAddValue() {
        // TODO: Test addValue method

    }

    @Test
    public void testGetUndefaultedValues() {
        // TODO: Test getUndefaultedValues method

    }

    @Test
    public void testSetDefaultValues() {
        // TODO: Test setDefaultValues method

    }

    @Test
    public void testAddSwitch() {
        // TODO: Test addSwitch method

    }

    @Test
    public void testSetDefaultSwitch() {
        // TODO: Test setDefaultSwitch method

    }

    @Test
    public void testAddPropertyOption() {
        // TODO: Test addPropertyOption method

    }

    @Test
    public void testAddPropertyString() {
        // TODO: Test addPropertyString method

    }

    @Test
    public void testLooksLikeOption() {
        // TODO: Test looksLikeOption method

    }
}