package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OptionGroupTest {
    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option1 = Mockito.mock(Option.class);
        Option option2 = Mockito.mock(Option.class);
        OptionGroup result = optionGroup.addOption(option1).addOption(option2);

        assertNotNull(result);
        assertEquals(2, optionGroup.getOptions().size());
        Mockito.verify(option1).getKey();
        Mockito.verify(option2).getKey();
        Mockito.verifyNoMoreInteractions(option1, option2);
    }

    @Test
    public void testGetNames() {
        Option option1 = Mockito.mock(Option.class);
        Option option2 = Mockito.mock(Option.class);
        Mockito.when(option1.getKey()).thenReturn("opt1");
        Mockito.when(option2.getKey()).thenReturn("opt2");
        optionGroup.addOption(option1).addOption(option2);

        Collection result = optionGroup.getNames();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertFalse(result.contains(null));
        Mockito.verify(option1, Mockito.atLeastOnce()).getKey();
        Mockito.verify(option2, Mockito.atLeastOnce()).getKey();
        Mockito.verifyNoMoreInteractions(option1, option2);
    }

    @Test
    public void testGetOptions() {
        Option option1 = Mockito.mock(Option.class);
        Option option2 = Mockito.mock(Option.class);
        Mockito.when(option1.getDescription()).thenReturn("desc1");
        Mockito.when(option2.getDescription()).thenReturn("desc2");
        optionGroup.addOption(option1).addOption(option2);

        Collection result = optionGroup.getOptions();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertFalse(result.contains(null));
        Mockito.verify(option1).getDescription();
        Mockito.verify(option2).getDescription();
        Mockito.verifyNoMoreInteractions(option1, option2);
    }

    @Test
    public void testSetSelected() {
        Option option1 = Mockito.mock(Option.class);
        Option option2 = Mockito.mock(Option.class);
        Mockito.when(option1.getOpt()).thenReturn("opt1");
        Mockito.when(option2.getOpt()).thenReturn("opt2");
        optionGroup.addOption(option1).addOption(option2);

        optionGroup.setSelected(option1);

        assertEquals("opt1", optionGroup.getSelected());
        Mockito.verify(option1).getOpt();
        Mockito.verify(option2).getOpt();
        Mockito.verifyNoMoreInteractions(option1, option2);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelectedException() throws AlreadySelectedException {
        Option option1 = Mockito.mock(Option.class);
        Option option2 = Mockito.mock(Option.class);
        Mockito.when(option1.getOpt()).thenReturn("opt1");
        Mockito.when(option2.getOpt()).thenReturn("opt2");
        optionGroup.addOption(option1).addOption(option2);
        optionGroup.setSelected(option1);

        optionGroup.setSelected(option2);

        Mockito.verify(option1).getOpt();
        Mockito.verify(option2).getOpt();
        Mockito.verifyNoMoreInteractions(option1, option2);
    }

    @Test
    public void testSetSelected_NullOption() {
        Option option1 = Mockito.mock(Option.class);
        Option option2 = Mockito.mock(Option.class);
        Mockito.when(option1.getOpt()).thenReturn("opt1");
        Mockito.when(option2.getOpt()).thenReturn("opt2");
        optionGroup.addOption(option1).addOption(option2);
        optionGroup.setSelected(option1);

        optionGroup.setSelected(null);

        assertEquals(null, optionGroup.getSelected());
        Mockito.verify(option1).getOpt();
        Mockito.verifyNoMoreInteractions(option1, option2);
    }

    @Test
    public void testGetSelected() {
        Option option1 = Mockito.mock(Option.class);
        Mockito.when(option1.getOpt()).thenReturn("opt1");
        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);

        String result = optionGroup.getSelected();

        assertEquals("opt1", result);
        Mockito.verify(option1).getOpt();
        Mockito.verifyNoMoreInteractions(option1);
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);

        assertEquals(true, optionGroup.isRequired());
    }

    @Test
    public void testIsRequired() {
        optionGroup.setRequired(false);

        assertEquals(false, optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = Mockito.mock(Option.class);
        Option option2 = Mockito.mock(Option.class);
        Mockito.when(option1.getOpt()).thenReturn("opt1");
        Mockito.when(option1.getDescription()).thenReturn("desc1");
        Mockito.when(option2.getLongOpt()).thenReturn("longOpt2");
        Mockito.when(option2.getDescription()).thenReturn("desc2");
        optionGroup.addOption(option1).addOption(option2);

        String result = optionGroup.toString();

        assertEquals("[opt1 desc1, --longOpt2 desc2]", result);
        Mockito.verify(option1).getOpt();
        Mockito.verify(option1).getDescription();
        Mockito.verify(option2).getLongOpt();
        Mockito.verify(option2).getDescription();
        Mockito.verifyNoMoreInteractions(option1, option2);
    }
}