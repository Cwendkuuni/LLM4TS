import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class OptionGroupTest {
    
    @Mock
    Option option1;
    
    @Mock
    Option option2;
    
    @Mock
    Option option3;
    
    OptionGroup optionGroup;
    
    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }
    
    @Test
    public void testAddOption() {
        optionGroup.addOption(option1);
        Collection<Option> options = optionGroup.getOptions();
        assertTrue(options.contains(option1));
    }
    
    @Test
    public void testGetNames() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection<String> names = optionGroup.getNames();
        assertEquals(2, names.size());
        assertTrue(names.contains(option1.getKey()));
        assertTrue(names.contains(option2.getKey()));
    }
    
    @Test
    public void testGetOptions() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection<Option> options = optionGroup.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }
    
    @Test
    public void testSetSelected() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        assertEquals(option1.getKey(), optionGroup.getSelected());
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelectedException() {
        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }
    
    @Test
    public void testSetSelected_NullOption() {
        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }
    
    @Test
    public void testGetSelected() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        assertEquals(option1.getKey(), optionGroup.getSelected());
    }
    
    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }
    
    @Test
    public void testIsRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }
    
    @Test
    public void testToString() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[";
        expected += "-" + option1.getOpt();
        expected += ", ";
        expected += "-" + option2.getOpt();
        expected += "]";
        assertEquals(expected, optionGroup.toString());
    }
}