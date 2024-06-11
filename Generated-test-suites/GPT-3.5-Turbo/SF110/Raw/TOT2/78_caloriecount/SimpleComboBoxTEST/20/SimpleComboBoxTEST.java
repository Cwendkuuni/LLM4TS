
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        ComboBoxModel newModel = new SimpleComboBoxModel();
        comboBox.setModel(newModel);
        
        assertEquals(newModel, comboBox.getModel());
    }

    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("test");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        assertEquals("test", comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedValue("test");
        
        assertEquals("test", model.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(5);
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        assertEquals(5, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(100L);
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        assertEquals(100L, comboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("test");
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.getSelectedInt();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("test");
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.getSelectedLong();
    }

}
