import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.AlreadySelectedException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest
{
    @Test
    public void testAddOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "option description 1");
        optionGroup.addOption(option);
        assertEquals(1, optionGroup.getOptions().size());
    }

    @Test
    public void testGetNames()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "option description 1");
        Option option2 = new Option("option2", "option description 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertEquals(2, optionGroup.getNames().size());
    }

    @Test
    public void testSetSelected_Null()
    {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetSelected_SameOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "option description 1");
        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);
        assertEquals(option1.getOpt(), optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_DifferentOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "option description 1");
        Option option2 = new Option("option2", "option description 2");
        optionGroup.addOption(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testToString()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "option description 1");
        Option option2 = new Option("option2", "option description 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[-option1 option description 1, --option2 option description 2]";
        assertEquals(expected, optionGroup.toString());
    }

    @Test
    public void testSetRequired_True()
    {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testSetRequired_False()
    {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }
}