import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OptionsTest {

    private Options options;
    private Option option;
    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        options = new Options();
        option = mock(Option.class);
        optionGroup = mock(OptionGroup.class);
    }

    @Test
    public void testAddOptionGroup() {
        Option option1 = mock(Option.class);
        Option option2 = mock(Option.class);
        Option option3 = mock(Option.class);

        when(option1.getKey()).thenReturn("1");
        when(option2.getKey()).thenReturn("2");
        when(option3.getKey()).thenReturn("3");
        when(optionGroup.isRequired()).thenReturn(true);
        when(optionGroup.getOptions()).thenReturn(Arrays.asList(option1, option2, option3));

        options.addOptionGroup(optionGroup);

        assertTrue(options.getRequiredOptions().contains(optionGroup));
        verify(option1).setRequired(false);
        verify(option2).setRequired(false);
        verify(option3).setRequired(false);
        verify(optionGroup).getOptions();
    }

    @Test
    public void testAddOption() {
        when(option.getKey()).thenReturn("opt");
        when(option.hasLongOpt()).thenReturn(true);
        when(option.getLongOpt()).thenReturn("longOpt");
        when(option.isRequired()).thenReturn(true);

        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
        assertTrue(options.getRequiredOptions().contains("opt"));
        verify(option).setRequired(false);
        verify(option).getKey();
    }

    @Test
    public void testGetOption() {
        Option option1 = mock(Option.class);
        when(option1.getKey()).thenReturn("opt");

        options.addOption(option1);

        Option result = options.getOption("opt");

        assertEquals(option1, result);
        verify(option1, Mockito.times(2)).getKey();
        verify(option1).getKey();
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = mock(Option.class);
        Option option2 = mock(Option.class);
        Option option3 = mock(Option.class);

        when(option1.getKey()).thenReturn("opt1");
        when(option2.getKey()).thenReturn("opt2");
        when(option3.getKey()).thenReturn("opt3");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        List<String> result = options.getMatchingOptions("opt");

        assertEquals(Arrays.asList("opt1", "opt2", "opt3"), result);
        verify(option1, Mockito.times(2)).getKey();
        verify(option2, Mockito.times(2)).getKey();
        verify(option3, Mockito.times(2)).getKey();
        verify(option1).getKey();
        verify(option2).getKey();
        verify(option3).getKey();
    }

    @Test
    public void testHasOption() {
        when(option.getKey()).thenReturn("opt");

        options.addOption(option);

        assertTrue(options.hasOption("opt"));
        assertFalse(options.hasOption("notExist"));
        verify(option, Mockito.times(2)).getKey();
        verify(option).getKey();
    }

    @Test
    public void testToString() {
        Option option1 = mock(Option.class);
        when(option1.getKey()).thenReturn("opt1");
        options.addOption(option1);

        String result = options.toString();

        assertTrue(result.contains("opt1"));
        verify(option1, Mockito.times(2)).getKey();
        verify(option1).getKey();
    }
}