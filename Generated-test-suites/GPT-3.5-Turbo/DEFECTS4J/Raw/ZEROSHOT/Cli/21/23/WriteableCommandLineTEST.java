import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    private WriteableCommandLine writeableCommandLine;
    private Option option;

    @Before
    public void setup() {
        writeableCommandLine = new WriteableCommandLine() {
            @Override
            public void addOption(Option option) {
            }

            @Override
            public void addValue(Option option, Object value) {
            }

            @Override
            public List getUndefaultedValues(Option option) {
                return new ArrayList();
            }

            @Override
            public void setDefaultValues(Option option, List defaultValues) {
            }

            @Override
            public void addSwitch(Option option, boolean value) throws IllegalStateException {
            }

            @Override
            public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            }

            @Override
            public void addProperty(Option option, String property, String value) {
            }

            @Override
            public void addProperty(String property, String value) {
            }

            @Override
            public boolean looksLikeOption(String argument) {
                return false;
            }
        };
        option = new Option("test");
    }

    @Test
    public void testAddOption() {
        writeableCommandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        writeableCommandLine.addValue(option, "value");
    }

    @Test
    public void testGetUndefaultedValues() {
        List values = writeableCommandLine.getUndefaultedValues(option);
        assert values.isEmpty();
    }

    @Test
    public void testSetDefaultValues() {
        List defaultValues = new ArrayList();
        writeableCommandLine.setDefaultValues(option, defaultValues);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch() {
        writeableCommandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        writeableCommandLine.setDefaultSwitch(option, true);
    }

    @Test
    public void testAddPropertyWithOption() {
        writeableCommandLine.addProperty(option, "property", "value");
    }

    @Test
    public void testAddPropertyWithoutOption() {
        writeableCommandLine.addProperty("property", "value");
    }

    @Test
    public void testLooksLikeOption() {
        boolean result = writeableCommandLine.looksLikeOption("argument");
        assert !result;
    }
}