import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {
    private SimpleComboBox simpleComboBox;
    private SimpleComboBoxModel simpleComboBoxModel;

    @Before
    public void setUp() {
        simpleComboBoxModel = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
    }

    // Test for setModel method with SimpleComboBoxModel
    @Test
    public void testSetModelWithSimpleComboBoxModel() {
        SimpleComboBoxModel newModel = new SimpleComboBoxModel();
        simpleComboBox.setModel(newModel);
        assertEquals(newModel, simpleComboBox.getModel());
    }

    // Test for setModel method with non-SimpleComboBoxModel
    @Test
    public void testSetModelWithNonSimpleComboBoxModel() {
        DefaultComboBoxModel newModel = new DefaultComboBoxModel();
        simpleComboBox.setModel(newModel);
        assertNotEquals(newModel, simpleComboBox.getModel());
    }

    // Test for getSelectedValue method with null SimpleComboBoxModel
    @Test
    public void testGetSelectedValueWithNullSimpleComboBoxModel() {
        simpleComboBox.setModel(null);
        assertNull(simpleComboBox.getSelectedValue());
    }

    // Test for getSelectedValue method with non-null SimpleComboBoxModel
    @Test
    public void testGetSelectedValueWithNonNullSimpleComboBoxModel() {
        Object value = new Object();
        simpleComboBoxModel.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedValue());
    }

    // Test for setSelectedValue method
    @Test
    public void testSetSelectedValue() {
        Object value = new Object();
        simpleComboBox.setSelectedValue(value);
        assertEquals(value, simpleComboBoxModel.getSelectedValue());
    }

    // Test for getSelectedInt method with null selected value
    @Test
    public void testGetSelectedIntWithNullValue() {
        simpleComboBoxModel.setSelectedValue(null);
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }

    // Test for getSelectedInt method with Integer selected value
    @Test
    public void testGetSelectedIntWithIntegerValue() {
        Integer value = Integer.valueOf(10);
        simpleComboBoxModel.setSelectedValue(value);
        assertEquals(10, simpleComboBox.getSelectedInt());
    }

    // Test for getSelectedInt method with Long selected value
    @Test
    public void testGetSelectedIntWithLongValue() {
        Long value = Long.valueOf(20);
        simpleComboBoxModel.setSelectedValue(value);
        assertEquals(20, simpleComboBox.getSelectedInt());
    }

    // Test for getSelectedInt method with Short selected value
    @Test
    public void testGetSelectedIntWithShortValue() {
        Short value = Short.valueOf("30");
        simpleComboBoxModel.setSelectedValue(value);
        assertEquals(30, simpleComboBox.getSelectedInt());
    }

    // Test for getSelectedInt method with Byte selected value
    @Test
    public void testGetSelectedIntWithByteValue() {
        Byte value = Byte.valueOf("40");
        simpleComboBoxModel.setSelectedValue(value);
        assertEquals(40, simpleComboBox.getSelectedInt());
    }

    // Test for getSelectedInt method with invalid selected value
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithInvalidValue() {
        simpleComboBoxModel.setSelectedValue("Invalid");
        simpleComboBox.getSelectedInt();
    }

    // Test for getSelectedLong method with null selected value
    @Test
    public void testGetSelectedLongWithNullValue() {
        simpleComboBoxModel.setSelectedValue(null);
        assertEquals(-1L, simpleComboBox.getSelectedLong());
    }

    // Test for getSelectedLong method with Integer selected value
    @Test
    public void testGetSelectedLongWithIntegerValue() {
        Integer value = Integer.valueOf(50);
        simpleComboBoxModel.setSelectedValue(value);
        assertEquals(50L, simpleComboBox.getSelectedLong());
    }

    // Test for getSelectedLong method with Long selected value
    @Test
    public void testGetSelectedLongWithLongValue() {
        Long value = Long.valueOf(60);
        simpleComboBoxModel.setSelectedValue(value);
        assertEquals(60L, simpleComboBox.getSelectedLong());
    }

    // Test for getSelectedLong method with Short selected value
    @Test
    public void testGetSelectedLongWithShortValue() {
        Short value = Short.valueOf("70");
        simpleComboBoxModel.setSelectedValue(value);
        assertEquals(70L, simpleComboBox.getSelectedLong());
    }

    // Test for getSelectedLong method with Byte selected value
    @Test
    public void testGetSelectedLongWithByteValue() {
        Byte value = Byte.valueOf("80");
        simpleComboBoxModel.setSelectedValue(value);
        assertEquals(80L, simpleComboBox.getSelectedLong());
    }

    // Test for getSelectedLong method with invalid selected value
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithInvalidValue() {
        simpleComboBoxModel.setSelectedValue("Invalid");
        simpleComboBox.getSelectedLong();
    }
}