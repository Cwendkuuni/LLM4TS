package org.apache.commons.cli;

 import org.junit.Before;
 import org.junit.Test;

 import java.util.Collection;

 import static org.junit.Assert.assertEquals;
 import static org.junit.Assert.assertFalse;
 import static org.junit.Assert.assertNotNull;
 import static org.junit.Assert.assertNull;
 import static org.junit.Assert.assertTrue;

 public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("f", "foo", false, "foo option");
        optionGroup.addOption(option);
        Collection<Option> options = optionGroup.getOptions();
        assertNotNull(options);
        assertEquals(1, options.size());
        assertTrue(options.contains(option));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("f", "foo", false, "foo option");
        Option option2 = new Option("b", "bar", false, "bar option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection<String> names = optionGroup.getNames();
        assertNotNull(names);
        assertEquals(2, names.size());
        assertTrue(names.contains("f"));
        assertTrue(names.contains("b"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("f", "foo", false, "foo option");
        Option option2 = new Option("b", "bar", false, "bar option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection<Option> options = optionGroup.getOptions();
        assertNotNull(options);
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("f", "foo", false, "foo option");
        Option option2 = new Option("b", "bar", false, "bar option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        assertEquals("f", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelectedException() {
        Option option1 = new Option("f", "foo", false, "foo option");
        Option option2 = new Option("b", "bar", false, "bar option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetSelected_NullOption() {
        Option option1 = new Option("f", "foo", false, "foo option");
        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testIsRequired() {
        assertFalse(optionGroup.isRequired());
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("f", "foo", false, "foo option");
        Option option2 = new Option("b", "bar", false, "bar option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[ -f, --bar ]";
        String actual = optionGroup.toString();
        assertEquals(expected, actual);
    }
 }