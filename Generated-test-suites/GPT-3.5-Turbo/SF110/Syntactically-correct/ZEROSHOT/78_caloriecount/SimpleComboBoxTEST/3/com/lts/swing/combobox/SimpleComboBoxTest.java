import org.junit.*;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {
    
    private SimpleComboBox simpleComboBox;
    private SimpleComboBoxModel simpleModel;
    
    @Before
    public void setUp() {
        simpleModel = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(simpleModel);
    }
    
    @Test
    public void testSetModel() {
        ComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        assertEquals(model, simpleComboBox.getModel());
        assertTrue(simpleComboBox.getModel() instanceof SimpleComboBoxModel);
        assertEquals(simpleModel, simpleComboBox.getMySimpleModel());
    }
    
    @Test
    public void testGetSelectedValue_WhenModelIsNull() {
        simpleModel = null;
        simpleComboBox.setMySimpleModel(simpleModel);
        assertNull(simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedValue_WhenModelIsNotNull() {
        Object value = new Object();
        simpleModel.setSelectedValue(value);
        simpleComboBox.setMySimpleModel(simpleModel);
        assertEquals(value, simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        Object value = new Object();
        simpleComboBox.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt_WhenValueIsNull() {
        simpleModel.setSelectedValue(null);
        simpleComboBox.setMySimpleModel(simpleModel);
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_WhenValueIsInteger() {
        Integer value = 10;
        simpleModel.setSelectedValue(value);
        simpleComboBox.setMySimpleModel(simpleModel);
        assertEquals(value, (Integer)simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_WhenValueIsLong() {
        Long value = 10L;
        simpleModel.setSelectedValue(value);
        simpleComboBox.setMySimpleModel(simpleModel);
        assertEquals(value.intValue(), simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_WhenValueIsShort() {
        Short value = 10;
        simpleModel.setSelectedValue(value);
        simpleComboBox.setMySimpleModel(simpleModel);
        assertEquals(value.intValue(), simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_WhenValueIsByte() {
        Byte value = 10;
        simpleModel.setSelectedValue(value);
        simpleComboBox.setMySimpleModel(simpleModel);
        assertEquals(value.intValue(), simpleComboBox.getSelectedInt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_WhenValueIsInvalid() {
        simpleModel.setSelectedValue("invalid");
        simpleComboBox.setMySimpleModel(simpleModel);
        simpleComboBox.getSelectedInt();
    }
    
    @Test
    public void testGetSelectedLong_WhenValueIsNull() {
        simpleModel.setSelectedValue(null);
        simpleComboBox.setMySimpleModel(simpleModel);
        assertEquals(-1L, simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_WhenValueIsInteger() {
        Integer value = 10;
        simpleModel.setSelectedValue(value);
        simpleComboBox.setMySimpleModel(simpleModel);
        assertEquals(value.longValue(), simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_WhenValueIsLong() {
        Long value = 10L;
        simpleModel.setSelectedValue(value);
        simpleComboBox.setMySimpleModel(simpleModel);
        assertEquals(value, simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_WhenValueIsShort() {
        Short value = 10;
        simpleModel.setSelectedValue(value);
        simpleComboBox.setMySimpleModel(simpleModel);
        assertEquals(value.longValue(), simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_WhenValueIsByte() {
        Byte value = 10;
        simpleModel.setSelectedValue(value);
        simpleComboBox.setMySimpleModel(simpleModel);
        assertEquals(value.longValue(), simpleComboBox.getSelectedLong());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_WhenValueIsInvalid() {
        simpleModel.setSelectedValue("invalid");
        simpleComboBox.setMySimpleModel(simpleModel);
        simpleComboBox.getSelectedLong();
    }
    
    @After
    public void tearDown() {
        simpleComboBox = null;
        simpleModel = null;
    }
}