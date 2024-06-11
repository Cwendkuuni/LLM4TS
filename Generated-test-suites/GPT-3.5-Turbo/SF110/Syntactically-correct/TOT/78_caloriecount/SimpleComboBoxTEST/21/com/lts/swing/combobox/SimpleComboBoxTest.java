import org.junit.After;
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

    @After
    public void tearDown() {
        simpleComboBox = null;
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        assertEquals(model, simpleComboBox.getModel());
    }

    @Test
    public void testGetSelectedValue_NullModel() {
        simpleComboBox.mySimpleModel = null;
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValue_ValidModel() {
        Object selectedValue = new Object();
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(selectedValue);
        assertEquals(selectedValue, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        Object selectedValue = new Object();
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.setSelectedValue(selectedValue);
        assertEquals(selectedValue, simpleComboBox.mySimpleModel.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt_IntegerValue() {
        int intValue = 123;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(intValue);
        assertEquals(intValue, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_LongValue() {
        long longValue = 123456789L;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(longValue);
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_ShortValue() {
        short shortValue = 99;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(shortValue);
        assertEquals(99, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_ByteValue() {
        byte byteValue = 100;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(byteValue);
        assertEquals(100, simpleComboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_InvalidValue() {
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue("invalid");
        simpleComboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLong_IntegerValue() {
        int intValue = 123;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(intValue);
        assertEquals(123L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_LongValue() {
        long longValue = 123456789L;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(longValue);
        assertEquals(123456789L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_ShortValue() {
        short shortValue = 99;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(shortValue);
        assertEquals(99L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_ByteValue() {
        byte byteValue = 100;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(byteValue);
        assertEquals(100L, simpleComboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_InvalidValue() {
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue("invalid");
        simpleComboBox.getSelectedLong();
    }
}