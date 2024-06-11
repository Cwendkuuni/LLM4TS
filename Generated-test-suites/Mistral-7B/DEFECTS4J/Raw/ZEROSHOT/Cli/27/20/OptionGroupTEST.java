import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Test class for OptionGroup.
 */
public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(new Option(null, "a", false, "Option A"));
        optionGroup.addOption(new Option(null, "b", false, "Option B"));
        optionGroup.addOption(new Option(null, "c", false, "Option C"));
        this.optionGroup = optionGroup;
    }

    @Test
    public void testGetNames() {
        Collection names = optionGroup.getNames();
        List expectedNames = Arrays.asList("a", "b", "c");
        assertEquals(expectedNames, names);
    }

    @Test
    public void testGetOptions() {
        Collection options = optionGroup.getOptions();
        List expectedOptions = Arrays.asList(optionGroup.getOption("a"),
                optionGroup.getOption("b"), optionGroup.getOption("c"));
        assertEquals(expectedOptions, options);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedNull() {
        optionGroup.setSelected(null);
    }

    @Test
    public void testSetSelected() {
        Option optionA = optionGroup.getOption("a");
        optionGroup.setSelected(optionA);
        String selected = optionGroup.getSelected();
        assertEquals(optionA.getOpt(), selected);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() {
        Option optionA = optionGroup.getOption("a");
        optionGroup.setSelected(optionA);
        optionGroup.setSelected(optionA);
    }

    @Test
    public void testIsRequired() {
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        String expected = "[--Option A Option A\n--Option B Option B\n--Option C Option C\n]";
        String actual = optionGroup.toString();
        assertEquals(expected, actual);
    }
}