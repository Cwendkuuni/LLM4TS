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
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        assertEquals(model, simpleComboBox.getModel());
    }
    
    @Test
    public void testGetSelectedValue_NullModel() {
        assertNull(simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        Integer selectedValue = 10;
        model.setSelectedValue(selectedValue);
        simpleComboBox.setModel(model);
        assertEquals(selectedValue, simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        Integer selectedValue = 10;
        simpleComboBox.setModel(model);
        simpleComboBox.setSelectedValue(selectedValue);
        assertEquals(selectedValue, model.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt_NullModel() {
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_IntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        Integer selectedValue = 10;
        model.setSelectedValue(selectedValue);
        simpleComboBox.setModel(model);
        assertEquals(selectedValue, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_LongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        Long selectedValue = 1000L;
        model.setSelectedValue(selectedValue);
        simpleComboBox.setModel(model);
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_ShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        Short selectedValue = 100;
        model.setSelectedValue(selectedValue);
        simpleComboBox.setModel(model);
        assertEquals(selectedValue, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_ByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        Byte selectedValue = 10;
        model.setSelectedValue(selectedValue);
        simpleComboBox.setModel(model);
        assertEquals(selectedValue, simpleComboBox.getSelectedInt());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testGetSelectedInt_InvalidValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        String selectedValue = "invalid";
        model.setSelectedValue(selectedValue);
        simpleComboBox.setModel(model);
        simpleComboBox.getSelectedInt();
    }
    
    @Test
    public void testGetSelectedLong_NullModel() {
        assertEquals(-1L, simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_IntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        Integer selectedValue = 10;
        model.setSelectedValue(selectedValue);
        simpleComboBox.setModel(model);
        assertEquals(selectedValue.longValue(), simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_LongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        Long selectedValue = 1000L;
        model.setSelectedValue(selectedValue);
        simpleComboBox.setModel(model);
        assertEquals(selectedValue, simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_ShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        Short selectedValue = 100;
        model.setSelectedValue(selectedValue);
        simpleComboBox.setModel(model);
        assertEquals(selectedValue.longValue(), simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_ByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        Byte selectedValue = 10;
        model.setSelectedValue(selectedValue);
        simpleComboBox.setModel(model);
        assertEquals(selectedValue.longValue(), simpleComboBox.getSelectedLong());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testGetSelectedLong_InvalidValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        String selectedValue = "invalid";
        model.setSelectedValue(selectedValue);
        simpleComboBox.setModel(model);
        simpleComboBox.getSelectedLong();
    }
}