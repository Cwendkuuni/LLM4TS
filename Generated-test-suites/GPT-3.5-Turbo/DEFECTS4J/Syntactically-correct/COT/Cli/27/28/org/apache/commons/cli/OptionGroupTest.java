import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "optionA");
        optionGroup.addOption(option);
        Map<String, Option> expectedOptionMap = new HashMap<>();
        expectedOptionMap.put("a", option);
        Assert.assertEquals(expectedOptionMap, optionGroup.getOptionMap());
    }
    
    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option optionA = new Option("a", "optionA");
        Option optionB = new Option("b", "optionB");
        optionGroup.addOption(optionA);
        optionGroup.addOption(optionB);
        Collection<String> expectedNames = List.of("a", "b");
        Assert.assertEquals(expectedNames, optionGroup.getNames());
    }
    
    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option optionA = new Option("a", "optionA");
        Option optionB = new Option("b", "optionB");
        optionGroup.addOption(optionA);
        optionGroup.addOption(optionB);
        Collection<Option> expectedOptions = List.of(optionA, optionB);
        Assert.assertEquals(expectedOptions, optionGroup.getOptions());
    }
    
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option optionA = new Option("a", "optionA");
        Option optionB = new Option("b", "optionB");
        optionGroup.addOption(optionA);
        optionGroup.addOption(optionB);
        optionGroup.setSelected(optionA);
        Assert.assertEquals("a", optionGroup.getSelected());
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedWithAlreadySelectedOption() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option optionA = new Option("a", "optionA");
        Option optionB = new Option("b", "optionB");
        optionGroup.addOption(optionA);
        optionGroup.addOption(optionB);
        optionGroup.setSelected(optionA);
        optionGroup.setSelected(optionB);
    }
    
    @Test
    public void testGetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option optionA = new Option("a", "optionA");
        Option optionB = new Option("b", "optionB");
        optionGroup.addOption(optionA);
        optionGroup.addOption(optionB);
        optionGroup.setSelected(optionA);
        Assert.assertEquals("a", optionGroup.getSelected());
    }
    
    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());
    }
    
    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option optionA = new Option("a", "optionA");
        Option optionB = new Option("b", "optionB");
        optionA.setDescription("Option A");
        optionB.setDescription("Option B");
        optionGroup.addOption(optionA);
        optionGroup.addOption(optionB);
        String expectedString = "[--optionA Option A, --optionB Option B]";
        Assert.assertEquals(expectedString, optionGroup.toString());
    }
}