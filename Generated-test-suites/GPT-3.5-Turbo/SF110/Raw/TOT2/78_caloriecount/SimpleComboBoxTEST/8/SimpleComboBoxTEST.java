import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {
    
    private SimpleComboBox simpleComboBox;
    
    @Before
    public void setUp() {
        simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
    }
    
    @Test
    public void testSetModel() {
        ComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        assertEquals(model, simpleComboBox.getModel());
    }
    
    @Test
    public void testGetSelectedValue() {
        Object value = simpleComboBox.getSelectedValue();
        assertNull(value);
    }
    
    @Test
    public void testSetSelectedValue() {
        simpleComboBox.setSelectedValue("Test");
        assertEquals("Test", simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt() {
        simpleComboBox.setSelectedValue(10);
        assertEquals(10, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedLong() {
        simpleComboBox.setSelectedValue(100L);
        assertEquals(100L, simpleComboBox.getSelectedLong());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntException() {
        simpleComboBox.setSelectedValue("String");
        simpleComboBox.getSelectedInt();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongException() {
        simpleComboBox.setSelectedValue("String");
        simpleComboBox.getSelectedLong();
    }
}