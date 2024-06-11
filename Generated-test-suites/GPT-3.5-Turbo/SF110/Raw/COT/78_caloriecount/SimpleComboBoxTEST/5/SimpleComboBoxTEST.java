
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    private SimpleComboBox simpleComboBox;

    @Before
    public void setUp() {
        simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
    }

    @Test
    public void testSetModel() {
        ComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        assertEquals(model, simpleComboBox.getModel());
    }

    @Test
    public void testGetSelectedValue_NullSimpleModel() {
        Object selectedValue = simpleComboBox.getSelectedValue();
        assertNull(selectedValue);
    }

    @Test
    public void testGetSelectedValue_ValidSimpleModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("value");
        simpleComboBox.setModel(model);
        Object selectedValue = simpleComboBox.getSelectedValue();
        assertEquals("value", selectedValue);
    }

    @Test
    public void testSetSelectedValue() {
        simpleComboBox.setModel(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue("value");
        Object selectedValue = simpleComboBox.getSelectedValue();
        assertEquals("value", selectedValue);
    }

    @Test
    public void testGetSelectedInt_NullSimpleModel() {
        int selectedInt = simpleComboBox.getSelectedInt();
        assertEquals(-1, selectedInt);
    }

    @Test
    public void testGetSelectedInt_IntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(123);
        simpleComboBox.setModel(model);
        int selectedInt = simpleComboBox.getSelectedInt();
        assertEquals(123, selectedInt);
    }

    @Test
    public void testGetSelectedInt_LongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(123L);
        simpleComboBox.setModel(model);
        int selectedInt = simpleComboBox.getSelectedInt();
        assertEquals(123, selectedInt);
    }

    @Test
    public void testGetSelectedInt_ShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((short) 123);
        simpleComboBox.setModel(model);
        int selectedInt = simpleComboBox.getSelectedInt();
        assertEquals(123, selectedInt);
    }

    @Test
    public void testGetSelectedInt_ByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((byte) 123);
        simpleComboBox.setModel(model);
        int selectedInt = simpleComboBox.getSelectedInt();
        assertEquals(123, selectedInt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_InvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("value");
        simpleComboBox.setModel(model);
        simpleComboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLong_NullSimpleModel() {
        long selectedLong = simpleComboBox.getSelectedLong();
        assertEquals(-1L, selectedLong);
    }

    @Test
    public void testGetSelectedLong_IntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(123);
        simpleComboBox.setModel(model);
        long selectedLong = simpleComboBox.getSelectedLong();
        assertEquals(123L, selectedLong);
    }

    @Test
    public void testGetSelectedLong_LongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(123L);
        simpleComboBox.setModel(model);
        long selectedLong = simpleComboBox.getSelectedLong();
        assertEquals(123L, selectedLong);
    }

    @Test
    public void testGetSelectedLong_ShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((short) 123);
        simpleComboBox.setModel(model);
        long selectedLong = simpleComboBox.getSelectedLong();
        assertEquals(123L, selectedLong);
    }

    @Test
    public void testGetSelectedLong_ByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((byte) 123);
        simpleComboBox.setModel(model);
        long selectedLong = simpleComboBox.getSelectedLong();
        assertEquals(123L, selectedLong);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_InvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("value");
        simpleComboBox.setModel(model);
        simpleComboBox.getSelectedLong();
    }

}
