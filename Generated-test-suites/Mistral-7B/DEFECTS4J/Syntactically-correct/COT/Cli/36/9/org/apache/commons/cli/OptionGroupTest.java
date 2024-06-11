import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test class for OptionGroup.
 */
public class OptionGroupTest
{
    @Test
    public void testAddOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "Optional description for option1");
        Option addedOption = optionGroup.addOption(option);
        assertEquals(option, addedOption);
    }

    @Test
    public void testGetNames()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Optional description for option1");
        Option option2 = new Option("option2", "Optional description for option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<String> names = new ArrayList<>(optionGroup.getNames());

        assertEquals(2, names.size());
        assertTrue(names.contains("option1"));
        assertTrue(names.contains("option2"));
    }

    @Test
    public void testGetOptions()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Optional description for option1");
        Option option2 = new Option("option2", "Optional description for option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<Option> options = new ArrayList<>(optionGroup.getOptions());

        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedNullOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setSelected(null);
    }

    @Test
    public void testSetSelectedSameOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "Optional description for option1");
        optionGroup.addOption(option);
        optionGroup.setSelected(option);
        optionGroup.setSelected(option);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedDifferentOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Optional description for option1");
        Option option2 = new Option("option2", "Optional description for option2");
        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelectedNull()
    {
        OptionGroup optionGroup = new OptionGroup();
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testGetSelectedSetOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "Optional description for option1");
        optionGroup.addOption(option);
        optionGroup.setSelected(option);
        assertEquals(option.getKey(), optionGroup.getSelected());
    }

    @Test
    public void testSetRequiredFalse()
    {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testSetRequiredTrue()
    {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToStringEmptyOptionGroup()
    {
        OptionGroup optionGroup = new OptionGroup();
        assertEquals("[]", optionGroup.toString());
    }

    @Test
    public void testToStringSingleOption()
    {
        Option option = new Option("option", "Optional description for option");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option);
        String expected = String.format("[%s]", option.getOpt());
        assertEquals(expected, optionGroup.toString());
    }

    @Test
    public void testToStringMultipleOptions()
    {
        Option option1 = new Option("option1", "Optional description for option1");
        Option option2 = new Option("option2", "Optional description for option2");
        Option option3 = new Option("option3", "Optional description for option3");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        HashSet<String> expected = new HashSet<String>(){{
            add(String.format("[%s, %s, %s]", option1.getOpt(), option2.getOpt(), option3.getOpt()));
            add(String.format("[--option1, --option2, --option3]", option1.getLongOpt(), option2.getLongOpt(), option3.getLongOpt()));
        }};
        List<String> strings = new ArrayList<>(Arrays.asList(optionGroup.toString().split("\\[")));
        assertEquals(expected.size(), strings.size());
        for (String string : strings) {
            assertTrue(expected.contains(string));
        }
    }
}