import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import com.lts.swing.combobox.*;

public class SimpleComboBoxTest {
    private SimpleComboBox simpleComboBox;
    
    @Before
    public void setUp() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(model);
    }
    
    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Item 1");
        model.addElement("Item 2");
        simpleComboBox.setModel(model);
        
        assertTrue(simpleComboBox.getModel() instanceof SimpleComboBoxModel);
        assertEquals(model, simpleComboBox.getModel());
        assertEquals("Item 1", simpleComboBox.getItemAt(0));
        assertEquals("Item 2", simpleComboBox.getItemAt(1));
    }
    
    @Test
    public void testGetSelectedValue_NullModel() {
        assertNull(simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedValue_WithModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Item 1");
        simpleComboBox.setModel(model);
        
        assertNull(simpleComboBox.getSelectedValue());
        
        simpleComboBox.setSelectedValue("Item 1");
        assertEquals("Item 1", simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Item 1");
        model.addElement("Item 2");
        simpleComboBox.setModel(model);
        
        simpleComboBox.setSelectedValue("Item 2");
        assertEquals("Item 2", simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt_NullValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(null);
        simpleComboBox.setModel(model);
        
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_IntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(10);
        simpleComboBox.setModel(model);
        
        assertEquals(10, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_LongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(100L);
        simpleComboBox.setModel(model);
        
        assertEquals(100, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_ShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 5);
        simpleComboBox.setModel(model);
        
        assertEquals(5, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_ByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 3);
        simpleComboBox.setModel(model);
        
        assertEquals(3, simpleComboBox.getSelectedInt());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testGetSelectedInt_IllegalArgumentException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Invalid");
        simpleComboBox.setModel(model);
        
        simpleComboBox.getSelectedInt();
    }
    
    @Test
    public void testGetSelectedLong_NullValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(null);
        simpleComboBox.setModel(model);
        
        assertEquals(-1L, simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_IntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(10);
        simpleComboBox.setModel(model);
        
        assertEquals(10L, simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_LongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(100L);
        simpleComboBox.setModel(model);
        
        assertEquals(100L, simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_ShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 5);
        simpleComboBox.setModel(model);
        
        assertEquals(5L, simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_ByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 3);
        simpleComboBox.setModel(model);
        
        assertEquals(3L, simpleComboBox.getSelectedLong());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testGetSelectedLong_IllegalArgumentException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Invalid");
        simpleComboBox.setModel(model);
        
        simpleComboBox.getSelectedLong();
    }
}