
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    @Test
    public void testGetSelectedValue_NullModel_ReturnNull() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValue_ValidModel_ReturnSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Value 1");
        model.addElement("Value 2");
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(1);
        assertEquals("Value 2", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue_ValidModel_SetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Value 1");
        model.addElement("Value 2");
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue("Value 2");
        assertEquals("Value 2", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt_NullSelectedValue_ReturnNegativeOne() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_IntegerSelectedValue_ReturnSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(10);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
        assertEquals(10, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong_NullSelectedValue_ReturnNegativeOne() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertEquals(-1L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_LongSelectedValue_ReturnSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(100L);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
        assertEquals(100L, simpleComboBox.getSelectedLong());
    }
    
    // Include additional tests for edge cases and exception handling scenarios

}

