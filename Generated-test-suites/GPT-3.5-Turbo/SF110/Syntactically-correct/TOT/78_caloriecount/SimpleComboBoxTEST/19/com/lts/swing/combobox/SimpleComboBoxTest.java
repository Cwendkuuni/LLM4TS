import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {

    private static SimpleComboBoxModel simpleComboBoxModel;
    private static SimpleComboBox simpleComboBox;

    @Before
    public void setup() {
        simpleComboBoxModel = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
    }

    // Test for setModel()
    @Test
    public void testSetModel() {
        ComboBoxModel model = new DefaultComboBoxModel();
        simpleComboBox.setModel(model);
        assertEquals(model, simpleComboBox.getModel());

        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        simpleComboBox.setModel(simpleModel);
        assertEquals(simpleModel, simpleComboBox.model);
    }

    // Test for getSelectedValue() with null mySimpleModel
    @Test
    public void testGetSelectedValueWithNullModel() {
        simpleComboBox.mySimpleModel = null;
        assertNull(simpleComboBox.getSelectedValue());
    }

    // Test for getSelectedValue() with non-null mySimpleModel
    @Test
    public void testGetSelectedValueWithNonNullModel() {
        Object selectedValue = new Object();
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(selectedValue);
        assertEquals(selectedValue, simpleComboBox.getSelectedValue());
    }

    // Test for setSelectedValue()
    @Test
    public void testSetSelectedValue() {
        Object selectedValue = new Object();
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.setSelectedValue(selectedValue);
        assertEquals(selectedValue, simpleComboBox.mySimpleModel.getSelectedValue());
    }

    // Test for getSelectedInt() with null selectedValue
    @Test
    public void testGetSelectedIntWithNullSelectedValue() {
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(null);
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }

    // Test for getSelectedInt() with selectedValue of type Integer
    @Test
    public void testGetSelectedIntWithIntegerSelectedValue() {
        int selectedValue = 10;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(selectedValue);
        assertEquals(selectedValue, simpleComboBox.getSelectedInt());
    }

    // Test for getSelectedInt() with selectedValue of type Long
    @Test
    public void testGetSelectedIntWithLongSelectedValue() {
        long selectedValue = 10L;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(selectedValue);
        assertEquals((int) selectedValue, simpleComboBox.getSelectedInt());
    }

    // Test for getSelectedInt() with selectedValue of type Short
    @Test
    public void testGetSelectedIntWithShortSelectedValue() {
        short selectedValue = 10;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(selectedValue);
        assertEquals(selectedValue, simpleComboBox.getSelectedInt());
    }

    // Test for getSelectedInt() with selectedValue of type Byte
    @Test
    public void testGetSelectedIntWithByteSelectedValue() {
        byte selectedValue = 10;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(selectedValue);
        assertEquals(selectedValue, simpleComboBox.getSelectedInt());
    }

    // Test for getSelectedInt() with selectedValue of invalid type
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithInvalidSelectedValue() {
        String selectedValue = "invalid";
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(selectedValue);
        simpleComboBox.getSelectedInt();
    }

    // Test for getSelectedLong() with null selectedValue
    @Test
    public void testGetSelectedLongWithNullSelectedValue() {
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(null);
        assertEquals(-1L, simpleComboBox.getSelectedLong());
    }

    // Test for getSelectedLong() with selectedValue of type Integer
    @Test
    public void testGetSelectedLongWithIntegerSelectedValue() {
        int selectedValue = 10;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(selectedValue);
        assertEquals((long) selectedValue, simpleComboBox.getSelectedLong());
    }

    // Test for getSelectedLong() with selectedValue of type Long
    @Test
    public void testGetSelectedLongWithLongSelectedValue() {
        long selectedValue = 10L;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(selectedValue);
        assertEquals(selectedValue, simpleComboBox.getSelectedLong());
    }

    // Test for getSelectedLong() with selectedValue of type Short
    @Test
    public void testGetSelectedLongWithShortSelectedValue() {
        short selectedValue = 10;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(selectedValue);
        assertEquals((long) selectedValue, simpleComboBox.getSelectedLong());
    }

    // Test for getSelectedLong() with selectedValue of type Byte
    @Test
    public void testGetSelectedLongWithByteSelectedValue() {
        byte selectedValue = 10;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(selectedValue);
        assertEquals((long) selectedValue, simpleComboBox.getSelectedLong());
    }

    // Test for getSelectedLong() with selectedValue of invalid type
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithInvalidSelectedValue() {
        String selectedValue = "invalid";
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(selectedValue);
        simpleComboBox.getSelectedLong();
    }
}