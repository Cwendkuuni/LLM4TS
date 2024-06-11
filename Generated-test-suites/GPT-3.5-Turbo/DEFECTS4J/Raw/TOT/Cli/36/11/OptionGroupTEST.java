import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {
    
    private OptionGroup optionGroup;
    private Option option1;
    private Option option2;
    private Option option3;
    private Option option4;
    
    @Before
    public void setUp() {
        optionGroup = new OptionGroup();

        option1 = new Option("a", "option1", false, "Option 1");
        optionGroup.addOption(option1);

        option2 = new Option("b", "option2", false, "Option 2");
        optionGroup.addOption(option2);

        option3 = new Option("c", "option3", false, "Option 3");
        optionGroup.addOption(option3);
        
        option4 = new Option("d", "option4", false, "Option 4");
    }

    @Test
    public void testAddOption() {
        OptionGroup result = optionGroup.addOption(option4);
        assertEquals(optionGroup, result);
    }

    @Test
    public void testGetNames() {
        Collection<String> names = optionGroup.getNames();
        List<String> expectedNames = Arrays.asList("a", "b", "c");
        assertTrue(names.containsAll(expectedNames));
        assertTrue(expectedNames.containsAll(names));
    }

    @Test
    public void testGetOptions() {
        Collection<Option> options = optionGroup.getOptions();
        List<Option> expectedOptions = Arrays.asList(option1, option2, option3);
        assertTrue(options.containsAll(expectedOptions));
        assertTrue(expectedOptions.containsAll(options));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
        
        optionGroup.setSelected(option2);
        assertEquals("b", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelectedException() throws AlreadySelectedException {
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
        optionGroup.setSelected(option3);
        optionGroup.setSelected(option1);
    }

    @Test
    public void testSetSelected_NullOption() throws AlreadySelectedException {
        optionGroup.setSelected(option1);
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testIsSelected() throws AlreadySelectedException {
        optionGroup.setSelected(option1);
        assertTrue(optionGroup.isSelected(option1));
        assertFalse(optionGroup.isSelected(option2));
        
        optionGroup.setSelected(option2);
        assertFalse(optionGroup.isSelected(option1));
        assertTrue(optionGroup.isSelected(option2));
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
        
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        String expected = "[-a Option 1, -b Option 2, -c Option 3]";
        assertEquals(expected, optionGroup.toString());
    }
}