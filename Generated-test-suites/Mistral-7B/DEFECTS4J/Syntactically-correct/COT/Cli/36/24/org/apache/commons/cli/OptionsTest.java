import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.junit.Test;

public class OptionsTest
{
    @Test
    public void testAddOptionGroup()
    {
        // given
        Options options = new Options();

        OptionGroup optionGroup = new OptionGroup("testGroup", "test Group");

        // when
        options.addOptionGroup(optionGroup);

        // then
        assertNotNull(options.getOptionGroups());
        assertTrue(options.getOptionGroups().contains(optionGroup));

        Option option = new Option("test", "test", false, "Test Option");
        optionGroup.addOption(option);

        assertSame(option, options.getOption(option.getKey()));
        assertSame(optionGroup, options.getOptionGroup(option));
    }

    @Test
    public void testAddOption()
    {
        // given
        Options options = new Options();

        // when
        options.addOption("t", "test", false, "Test Option");

        // then
        Option option = options.getOption("t");
        assertEquals("Test Option", option.getDescription());
        assertEquals(false, option.isRequired());

        // given
        options = new Options();

        // when
        options.addOption("t", "test", true, "Test Option");

        // then
        option = options.getOption("t");
        assertEquals("Test Option", option.getDescription());
        assertEquals(true, option.isRequired());

        // given
        options = new Options();

        // when
        options.addOption("t", "test", false, "Test Option");
        options.addOption("t", "testLong", false, "Test Option Long");

        // then
        List<Option> optionsList = options.helpOptions();
        assertEquals(2, optionsList.size());
        assertFalse(Collections.disjoint(Arrays.asList("t", "test"), optionsList));
        assertFalse(Collections.disjoint(Arrays.asList("t", "testLong"), optionsList));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionWithInvalidOptionGroup()
    {
        // given
        Options options = new Options();

        OptionGroup optionGroup = null;

        // when
        options.addOptionGroup(optionGroup);

        // then
        // an exception should be thrown
        options.addOption("t", "test", false, "Test Option");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionWithNullOption()
    {
        // given
        Options options = new Options();

        // when
        options.addOption(null);

        // then
        // an exception should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionWithDuplicateOption()
    {
        // given
        Options options = new Options();

        // when
        options.addOption("t", "test", false, "Test Option");
        options.addOption("t", "test", false, "Test Option");

        // then
        // an exception should be thrown
    }

    @Test
    public void testGetOption()
    {
        // given
        Options options = new Options();

        Option option = new Option("test", "test", false, "Test Option");
        options.addOption(option);

        // when
        Option result = options.getOption("test");

        // then
        assertSame(option, result);
    }

    @Test
    public void testGetOptionWithInvalidOption()
    {
        // given
        Options options = new Options();

        // when
        Option result = options.getOption("invalid");

        // then
        assertNull(result);
    }

    @Test
    public void testHasOption()
    {
        // given
        Options options = new Options();

        Option option = new Option("t", "test", false, "Test Option");
        options.addOption(option);

        // when
        boolean result = options.hasOption("t");

        // then
        assertTrue(result);
    }

    @Test
    public void testHasOptionWithInvalidOption()
    {
        // given
        Options options = new Options();

        // when
        boolean result = options.hasOption("invalid");

        // then
        assertFalse(result);
    }

    @Test
    public void testGetMatchingOptions()
    {
        // given
        Options options = new Options();

        Option option1 = new Option("t1", "test1", false, "Test Option 1");
        Option option2 = new Option("t2", "test2", false, "Test Option 2");
        Option option3 = new Option("t3", "test3", false, "Test Option 3");
        Option option4 = new Option("t4", "test4", false, "Test Option 4");
        Option option5 = new Option("t5", "test5", false, "Test Option 5");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);
        options.addOption(option5);

        // when
        List<Option> result = options.getMatchingOptions("t");

        // then
        assertEquals(2, result.size());
        assertTrue(Collections.disjoint(result, Arrays.asList(option1, option2)));
        assertTrue(Collections.disjoint(result, Arrays.asList(option3, option4, option5)));
        assertTrue(Collections.disjoint(Arrays.asList("t1", "t2"), result));
        assertTrue(Collections.disjoint(Arrays.asList("t3", "t4", "t5"), result));

        // when
        result = options.getMatchingOptions("test");

        // then
        assertEquals(5, result.size());
        assertTrue(Collections.disjoint(result, Arrays.asList(option1, option2, option3, option4, option5)));
        assertFalse(Collections.disjoint(Arrays.asList("t1", "t2"), result));
        assertFalse(Collections.disjoint(Arrays.asList("t3", "t4", "t5"), result));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMatchingOptionsWithInvalidOption()
    {
        // given
        Options options = new Options();

        Option option = new Option("t", "test", false, "Test Option");
        options.addOption(option);

        // when
        List<Option> result = options.getMatchingOptions("invalid");

        // then
        // an exception should be thrown
    }

    @Test
    public void testToString()
    {
        // given
        Options options = new Options();

        Option option1 = new Option("t1", "test1", false, "Test Option 1");
        Option option2 = new Option("t2", "test2", false, "Test Option 2");
        Option option3 = new Option("t3", "test3", false, "Test Option 3");
        Option option4 = new Option("t4", "test4", false, "Test Option 4");
        Option option5 = new Option("t5", "test5", false, "Test Option 5");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);
        options.addOption(option5);

        // when
        String result = options.toString();

        // then
        String expected = "[ Options: [ short {" +
                "Option{key='t1', longOpt='', value=null, hasArg=false, description='Test Option 1'}, " +
                "Option{key='t2', longOpt='', value=null, hasArg=false, description='Test Option 2'}, " +
                "Option{key='t3', longOpt='', value=null, hasArg=false, description='Test Option 3'}, " +
                "Option{key='t4', longOpt='', value=null, hasArg=false, description='Test Option 4'}, " +
                "Option{key='t5', longOpt='', value=null, hasArg=false, description='Test Option 5'} " +
                "] [ long <empty> ]]";

        assertEquals(expected, result);
    }

    @Test
    public void testGetRequiredOptions()
    {
        // given
        Options options = new Options();

        Option option1 = new Option("t1", "test1", false, "Test Option 1");
        Option option2 = new Option("t2", "test2", true, "Test Option 2");

        options.addOption(option1);
        options.addOption(option2);

        // when
        List<Option> result = options.getRequiredOptions();

        // then
        assertEquals(1, result.size());
        assertEquals(option2, result.get(0));
    }

    @Test
    public void testGetRequiredOptionsEmpty()
    {
        // given
        Options options = new Options();

        // when
        List<Option> result = options.getRequiredOptions();

        // then
        assertTrue(result.isEmpty());
    }
}