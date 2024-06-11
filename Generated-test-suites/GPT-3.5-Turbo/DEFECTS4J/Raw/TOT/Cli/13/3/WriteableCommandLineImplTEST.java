import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl cmdLine;
    private Option option1;
    private Option option2;
    private Option option3;


    @Before
    public void setUp() throws Exception {
        option1 = new DefaultOptionBuilder().withLongName("option1").create();
        option2 = new DefaultOptionBuilder().withLongName("option2").create();
        option3 = new ArgumentBuilder().create();

        cmdLine = new WriteableCommandLineImpl(option1, Arrays.asList("-f", "-option1Value"));

        cmdLine.addValue(option2, "option2Value");
        cmdLine.addSwitch(option3, true);
    }

    @Test
    public void testAddOption() {
        cmdLine.addOption(option3);

        assertTrue(cmdLine.hasOption(option3));
    }

    @Test
    public void testAddValue() {
        cmdLine.addValue(option1, "anotherOption1Value");
        cmdLine.addValue(option2, "anotherOption2Value");

        List<String> expectedValue1 = Arrays.asList("option1Value", "anotherOption1Value");
        List<String> expectedValue2 = Arrays.asList("option2Value", "anotherOption2Value");

        assertEquals(expectedValue1, cmdLine.getValues(option1, null));
        assertEquals(expectedValue2, cmdLine.getValues(option2, null));
    }

    @Test
    public void testAddSwitch() {
        Option option4 = new DefaultOptionBuilder().withLongName("option4").withSwitch(false).create();
        cmdLine.addSwitch(option4, false);

        assertTrue(cmdLine.hasOption(option4));
        assertFalse(cmdLine.getSwitch(option4, null));
    }

    @Test
    public void testHasOption() {
        assertTrue(cmdLine.hasOption(option1));
        assertTrue(cmdLine.hasOption(option2));
        assertTrue(cmdLine.hasOption(option3));

        assertFalse(cmdLine.hasOption(new DefaultOptionBuilder().withLongName("option4").create()));
    }

    @Test
    public void testGetOption() {
        assertEquals(option1, cmdLine.getOption("option1"));
        assertEquals(option2, cmdLine.getOption("option2"));
        assertEquals(option3, cmdLine.getOption(""));
    }

    @Test
    public void testGetValues() {
        List<String> expectedValue1 = Arrays.asList("option1Value");
        List<String> expectedValue2 = Arrays.asList("option2Value");
        List<String> expectedValue3 = Arrays.asList("true");

        assertEquals(expectedValue1, cmdLine.getValues(option1, null));
        assertEquals(expectedValue2, cmdLine.getValues(option2, null));
        assertEquals(expectedValue3, cmdLine.getValues(option3, null));
    }

    @Test
    public void testGetSwitch() {
        Option option4 = new DefaultOptionBuilder().withLongName("option4").withSwitch(false).create();
        cmdLine.addSwitch(option4, false);

        assertTrue(cmdLine.hasOption(option4));
        assertFalse(cmdLine.getSwitch(option4, null));
        assertFalse(cmdLine.getSwitch(option4, false));
        assertTrue(cmdLine.getSwitch(option4, true));
    }

    @Test
    public void testAddProperty() {
        cmdLine.addProperty("property1", "value1");
        cmdLine.addProperty("property2", "value2");

        assertEquals("value1", cmdLine.getProperty("property1", ""));
        assertEquals("value2", cmdLine.getProperty("property2", ""));
        assertEquals("", cmdLine.getProperty("property3", ""));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(cmdLine.looksLikeOption("-f"));
        assertFalse(cmdLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        assertEquals("-f -option1Value -- option3", cmdLine.toString());
    }

    @Test
    public void testGetOptions() {
        List<Option> expectedOptions = Arrays.asList(option1, option2, option3);

        assertEquals(expectedOptions, cmdLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        List<String> expectedTriggers = Arrays.asList("option1", "option2", "");

        assertEquals(new HashSet<>(expectedTriggers), cmdLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        List<String> expectedDefaultValues = Arrays.asList("defaultValue");

        cmdLine.setDefaultValues(option1, expectedDefaultValues);

        assertEquals(expectedDefaultValues, cmdLine.getValues(option1, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option4 = new DefaultOptionBuilder().withLongName("option4").withSwitch(false).create();
        cmdLine.setDefaultSwitch(option4, false);

        assertFalse(cmdLine.getSwitch(option4, null));
        assertFalse(cmdLine.getSwitch(option4, false));
        assertTrue(cmdLine.getSwitch(option4, true));
    }

    @Test
    public void testGetNormalised() {
        List<String> expectedNormalised = Arrays.asList("-f", "-option1Value", "--", "option2Value", "true");

        assertEquals(expectedNormalised, cmdLine.getNormalised());
    }
}