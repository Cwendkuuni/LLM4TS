import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        ComboBoxModel newModel = new SimpleComboBoxModel();
        comboBox.setModel(newModel);
        assertTrue(comboBox.getModel() instanceof SimpleComboBoxModel);
    }

    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("item1");
        model.addElement("item2");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedIndex(1);
        assertEquals("item2", comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("item1");
        model.addElement("item2");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedValue("item2");
        assertEquals("item2", comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(5);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedIndex(0);
        assertEquals(5, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(100L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedIndex(0);
        assertEquals(100L, comboBox.getSelectedLong());
    }
    
    // Edge cases and error scenarios
    
    @Test
    public void testGetSelectedIntInvalid() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("string");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedIndex(0);
        try {
            comboBox.getSelectedInt();
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testGetSelectedLongInvalid() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("string");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedIndex(0);
        try {
            comboBox.getSelectedLong();
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }
}