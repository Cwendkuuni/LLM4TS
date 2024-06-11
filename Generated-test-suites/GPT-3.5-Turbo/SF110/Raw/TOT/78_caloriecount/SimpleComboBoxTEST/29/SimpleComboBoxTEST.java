import org.junit.*;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    private SimpleComboBoxModel simpleModel;
    private SimpleComboBox comboBox;

    @Before
    public void setUp() {
        simpleModel = new SimpleComboBoxModel();
        comboBox = new SimpleComboBox(simpleModel);
    }

    // Test case for setModel() with SimpleComboBoxModel
    @Test
    public void testSetModelWithSimpleComboBoxModel() {
        ComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);
        assertEquals(model, comboBox.getModel());
    }

    // Test case for setModel() with non-SimpleComboBoxModel
    @Test
    public void testSetModelWithNonSimpleComboBoxModel() {
        ComboBoxModel model = new DefaultComboBoxModel();
        comboBox.setModel(model);
        assertEquals(model, comboBox.getModel());
    }

    // Test case for getSelectedValue() with non-null mySimpleModel
    @Test
    public void testGetSelectedValueWithNonNullMySimpleModel() {
        simpleModel.setSelectedValue("Test Value");
        assertEquals("Test Value", comboBox.getSelectedValue());
    }

    // Test case for getSelectedValue() with null mySimpleModel
    @Test
    public void testGetSelectedValueWithNullMySimpleModel() {
        assertNull(comboBox.getSelectedValue());
    }

    // Test case for setSelectedValue()
    @Test
    public void testSetSelectedValue() {
        comboBox.setSelectedValue("Test Value");
        assertEquals("Test Value", simpleModel.getSelectedValue());
    }

    // Test case for getSelectedInt() with Integer value
    @Test
    public void testGetSelectedIntWithIntegerValue() {
        simpleModel.setSelectedValue(10);
        assertEquals(10, comboBox.getSelectedInt());
    }

    // Test case for getSelectedInt() with Long value within Integer range
    @Test
    public void testGetSelectedIntWithLongValueWithinRange() {
        simpleModel.setSelectedValue(1000L);
        assertEquals(1000, comboBox.getSelectedInt());
    }

    // Test case for getSelectedInt() with Long value outside Integer range
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithLongValueOutsideRange() {
        simpleModel.setSelectedValue(Long.MAX_VALUE);
        comboBox.getSelectedInt();
    }

    // Test case for getSelectedInt() with Short value
    @Test
    public void testGetSelectedIntWithShortValue() {
        simpleModel.setSelectedValue((short) 5);
        assertEquals(5, comboBox.getSelectedInt());
    }

    // Test case for getSelectedInt() with Byte value
    @Test
    public void testGetSelectedIntWithByteValue() {
        simpleModel.setSelectedValue((byte) 2);
        assertEquals(2, comboBox.getSelectedInt());
    }

    // Test case for getSelectedInt() with null value
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithNullValue() {
        simpleModel.setSelectedValue(null);
        comboBox.getSelectedInt();
    }

    // Test case for getSelectedLong() with Integer value
    @Test
    public void testGetSelectedLongWithIntegerValue() {
        simpleModel.setSelectedValue(10);
        assertEquals(10L, comboBox.getSelectedLong());
    }

    // Test case for getSelectedLong() with Long value
    @Test
    public void testGetSelectedLongWithLongValue() {
        simpleModel.setSelectedValue(1000L);
        assertEquals(1000L, comboBox.getSelectedLong());
    }

    // Test case for getSelectedLong() with Short value within Long range
    @Test
    public void testGetSelectedLongWithShortValueWithinRange() {
        simpleModel.setSelectedValue((short) 5);
        assertEquals(5L, comboBox.getSelectedLong());
    }

    // Test case for getSelectedLong() with Short value outside Long range
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithShortValueOutsideRange() {
        simpleModel.setSelectedValue(Short.MAX_VALUE);
        comboBox.getSelectedLong();
    }

    // Test case for getSelectedLong() with Byte value
    @Test
    public void testGetSelectedLongWithByteValue() {
        simpleModel.setSelectedValue((byte) 2);
        assertEquals(2L, comboBox.getSelectedLong());
    }

    // Test case for getSelectedLong() with null value
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithNullValue() {
        simpleModel.setSelectedValue(null);
        comboBox.getSelectedLong();
    }

}