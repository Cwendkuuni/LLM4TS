import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {
    
    private SimpleComboBox simpleComboBox;
    
    @Before
    public void setUp() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(model);
    }
    
    @Test
    public void testSetModel() {
        assertNull(simpleComboBox.mySimpleModel);
        
        SimpleComboBoxModel newModel = new SimpleComboBoxModel();
        simpleComboBox.setModel(newModel);
        
        assertEquals(newModel, simpleComboBox.mySimpleModel);
    }
    
    @Test
    public void testGetSelectedValue() {
        assertNull(simpleComboBox.getSelectedValue());
        
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Item 1");
        model.addElement("Item 2");
        model.setSelectedItem("Item 2");
        simpleComboBox.setModel(model);
        
        assertEquals("Item 2", simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Item 1");
        model.addElement("Item 2");
        simpleComboBox.setModel(model);
        
        simpleComboBox.setSelectedValue("Item 2");
        
        assertEquals("Item 2", simpleComboBox.mySimpleModel.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(10);
        simpleComboBox.setModel(model);
        
        assertEquals(10, simpleComboBox.getSelectedInt());
        
        model.setSelectedItem(20);
        simpleComboBox.setModel(model);
        
        assertEquals(20, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(100L);
        simpleComboBox.setModel(model);
        
        assertEquals(100L, simpleComboBox.getSelectedLong());
        
        model.setSelectedItem(200L);
        simpleComboBox.setModel(model);
        
        assertEquals(200L, simpleComboBox.getSelectedLong());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithInvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Item 1");
        simpleComboBox.setModel(model);
        
        simpleComboBox.getSelectedInt();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithInvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Item 1");
        simpleComboBox.setModel(model);
        
        simpleComboBox.getSelectedLong();
    }
}