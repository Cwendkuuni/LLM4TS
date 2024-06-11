import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    @Test
    public void testSetModel_ValidModel_ModelSetSuccessfully() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox scb = new SimpleComboBox(model);
        
        ComboBoxModel newModel = new DefaultComboBoxModel();
        scb.setModel(newModel);
        
        assertEquals(newModel, scb.getModel());
        assertEquals(newModel, scb.mySimpleModel);
    }
    
    @Test
    public void testSetModel_InvalidModel_ModelSetSuccessfully() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox scb = new SimpleComboBox(model);
        
        ComboBoxModel newModel = new DefaultComboBoxModel();
        scb.setModel(newModel);
        
        assertEquals(newModel, scb.getModel());
        assertNull(scb.mySimpleModel);
    }
    
    @Test
    public void testGetSelectedValue_WithNullSimpleModel_ReturnNull() {
        SimpleComboBoxModel model = null;
        SimpleComboBox scb = new SimpleComboBox(model);
        
        assertNull(scb.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedValue_WithValidSimpleModel_ReturnSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        String value = "Test";
        model.addElement(value);
        SimpleComboBox scb = new SimpleComboBox(model);
        
        assertEquals(value, scb.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue_WithNullSimpleModel_DoNothing() {
        SimpleComboBoxModel model = null;
        SimpleComboBox scb = new SimpleComboBox(model);

        scb.setSelectedValue("Test");
        
        assertNull(scb.mySimpleModel);
    }
    
    @Test
    public void testSetSelectedValue_WithValidSimpleModel_SetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox scb = new SimpleComboBox(model);

        String value = "Test";
        scb.setSelectedValue(value);
        
        assertEquals(value, scb.getSelectedValue());
        assertEquals(value, scb.mySimpleModel.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt_WithNullSelectedValue_ReturnMinusOne() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox scb = new SimpleComboBox(model);

        assertEquals(-1, scb.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_WithIntegerSelectedValue_ReturnIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        int value = 5;
        model.addElement(value);
        SimpleComboBox scb = new SimpleComboBox(model);

        assertEquals(value, scb.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_WithLongSelectedValue_ReturnLongValueAsInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        long value = 10L;
        model.addElement(value);
        SimpleComboBox scb = new SimpleComboBox(model);

        assertEquals((int)value, scb.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_WithShortSelectedValue_ReturnShortValueAsInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        short value = 3;
        model.addElement(value);
        SimpleComboBox scb = new SimpleComboBox(model);

        assertEquals((int)value, scb.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_WithByteSelectedValue_ReturnByteValueAsInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        byte value = 1;
        model.addElement(value);
        SimpleComboBox scb = new SimpleComboBox(model);

        assertEquals((int)value, scb.getSelectedInt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_WithInvalidSelectedValue_ThrowIllegalArgumentException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        String value = "Test";
        model.addElement(value);
        SimpleComboBox scb = new SimpleComboBox(model);

        scb.getSelectedInt();
    }
    
    @Test
    public void testGetSelectedLong_WithNullSelectedValue_ReturnMinusOne() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox scb = new SimpleComboBox(model);

        assertEquals(-1L, scb.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_WithIntegerSelectedValue_ReturnIntegerValueAsLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        int value = 5;
        model.addElement(value);
        SimpleComboBox scb = new SimpleComboBox(model);

        assertEquals((long)value, scb.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_WithLongSelectedValue_ReturnLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        long value = 10L;
        model.addElement(value);
        SimpleComboBox scb = new SimpleComboBox(model);

        assertEquals(value, scb.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_WithShortSelectedValue_ReturnShortValueAsLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        short value = 3;
        model.addElement(value);
        SimpleComboBox scb = new SimpleComboBox(model);

        assertEquals((long)value, scb.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_WithByteSelectedValue_ReturnByteValueAsLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        byte value = 1;
        model.addElement(value);
        SimpleComboBox scb = new SimpleComboBox(model);

        assertEquals((long)value, scb.getSelectedLong());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_WithInvalidSelectedValue_ThrowIllegalArgumentException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        String value = "Test";
        model.addElement(value);
        SimpleComboBox scb = new SimpleComboBox(model);

        scb.getSelectedLong();
    }
}