import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OptionGroupTest
{
    private OptionGroup optionGroup;

    @Before
    public void setUp()
    {
        Option option1 = new Option("o1", "option1", false, "Description for option1");
        Option option2 = new Option("o2", "option2", false, "Description for option2");
        Option option3 = new Option("o3", "option3", false, "Description for option3");
        Option option4 = new Option("o4", "option4", false, "Description for option4");

        optionGroup = new OptionGroup("testGroup")
                .addOption(option1)
                .addOption(option2)
                .addOption(option3)
                .addOption(option4);
    }

    @Test
    public void testGetNames()
    {
        Collection<String> names = optionGroup.getNames();
        assertTrue(names.containsAll(Arrays.asList("o1", "o2", "o3", "o4")));
    }

    @Test
    public void testGetOptions()
    {
        Collection<Option> options = optionGroup.getOptions();
        assertEquals(4, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
        assertTrue(options.contains(option3));
        assertTrue(options.contains(option4));
    }

    @Test
    public void testSetSelected()
    {
        optionGroup.setSelected(option1);
        assertEquals(option1.getKey(), optionGroup.getSelected());

        assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(option1));

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals(option2.getKey(), optionGroup.getSelected());
    }

    @Test
    public void testIsRequired()
    {
        assertFalse(optionGroup.isRequired());

        OptionGroup requiredOptionGroup = new OptionGroup("testGroupRequired")
                .setRequired(true);

        assertTrue(requiredOptionGroup.isRequired());
    }

    @Test
    public void testToString()
    {
        assertEquals("[--option1, Description for option1, --option2, Description for option2, --option3, Description for option3, --option4, Description for option4]", optionGroup.toString());
    }

    @Test
    public void testSetSelectedWithAlreadySelectedOption() throws ParseException
    {
        OptionGroup optionGroup = new OptionGroup("testGroup")
                .addOption(new Option("o1", "o1", false, "option1"))
                .addOption(new Option("o2", "o2", false, "option2"))
                .addOption(new Option("o3", "o3", false, "option3"))
                .addOption(new Option("o4", "o4", false, "option4"))
                .setSelected(new Option("o1", "o1", false, "option1"));
        optionGroup.setSelected(new Option("o1", "o1", false, "option1"));
        assertEquals("o1", optionGroup.getSelected());
    }
}