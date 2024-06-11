import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {
    private SimpleComboBox simpleComboBox;
    
    @Before
    public void setUp() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(model);
    }
    
    @Test
    public void testSetModel_validModel_setsModel() {
        SimpleComboBoxModel newModel = new SimpleComboBoxModel();
        simpleComboBox.setModel(newModel);
        
        assertEquals(newModel, simpleComboBox.getModel());
    }
    
    @Test
    public void testSetModel_nonSimpleComboBoxModel_doesNotSetModel() {
        DefaultComboBoxModel newModel = new DefaultComboBoxModel();
        simpleComboBox.setModel(newModel);
        
        assertNotEquals(newModel, simpleComboBox.getModel());
    }
    
    @Test
    public void testGetSelectedValue_nullSimpleModel_returnsNull() {
        assertNull(simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedValue_validSimpleModel_returnsSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        Integer value = 10;
        model.addElement(value);
        simpleComboBox.setModel(model);
        
        assertEquals(value, simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue_validValue_setsSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        Integer value = 5;
        
        simpleComboBox.setSelectedValue(value);
        
        assertEquals(value, simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt_nullValue_returnsNegativeOne() {
        int selectedInt = simpleComboBox.getSelectedInt();
        
        assertEquals(-1, selectedInt);
    }
    
    @Test
    public void testGetSelectedInt_validIntegerValue_returnsIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        Integer value = 20;
        model.addElement(value);
        simpleComboBox.setModel(model);
        
        int selectedInt = simpleComboBox.getSelectedInt();
        
        assertEquals(value, selectedInt);
    }
    
    @Test
    public void testGetSelectedInt_validLongValue_returnsCastedLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        Long value = 30L;
        model.addElement(value);
        simpleComboBox.setModel(model);
        
        int selectedInt = simpleComboBox.getSelectedInt();
        
        assertEquals(value.intValue(), selectedInt);
    }
    
    @Test
    public void testGetSelectedInt_validShortValue_returnsShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        Short value = 40;
        model.addElement(value);
        simpleComboBox.setModel(model);
        
        int selectedInt = simpleComboBox.getSelectedInt();
        
        assertEquals(value, selectedInt);
    }
    
    @Test
    public void testGetSelectedInt_validByteValue_returnsByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        Byte value = 50;
        model.addElement(value);
        simpleComboBox.setModel(model);
        
        int selectedInt = simpleComboBox.getSelectedInt();
        
        assertEquals(value, selectedInt);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_invalidType_throwsIllegalArgumentException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Invalid");
        simpleComboBox.setModel(model);
        
        simpleComboBox.getSelectedInt();
    }
    
    @Test
    public void testGetSelectedLong_nullValue_returnsNegativeOne() {
        long selectedLong = simpleComboBox.getSelectedLong();
        
        assertEquals(-1L, selectedLong);
    }
    
    @Test
    public void testGetSelectedLong_validIntegerValue_returnsIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        Integer value = 20;
        model.addElement(value);
        simpleComboBox.setModel(model);
        
        long selectedLong = simpleComboBox.getSelectedLong();
        
        assertEquals(value.longValue(), selectedLong);
    }
    
    @Test
    public void testGetSelectedLong_validLongValue_returnsLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        Long value = 30L;
        model.addElement(value);
        simpleComboBox.setModel(model);
        
        long selectedLong = simpleComboBox.getSelectedLong();
        
        assertEquals(value, selectedLong);
    }
    
    @Test
    public void testGetSelectedLong_validShortValue_returnsShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        Short value = 40;
        model.addElement(value);
        simpleComboBox.setModel(model);
        
        long selectedLong = simpleComboBox.getSelectedLong();
        
        assertEquals(value, selectedLong);
    }
    
    @Test
    public void testGetSelectedLong_validByteValue_returnsByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        Byte value = 50;
        model.addElement(value);
        simpleComboBox.setModel(model);
        
        long selectedLong = simpleComboBox.getSelectedLong();
        
        assertEquals(value, selectedLong);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_invalidType_throwsIllegalArgumentException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Invalid");
        simpleComboBox.setModel(model);
        
        simpleComboBox.getSelectedLong();
    }
}