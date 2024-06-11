import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    private SimpleComboBox simpleComboBox;

    @Before
    public void setUp() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(simpleModel);
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel newModel = new SimpleComboBoxModel();
        simpleComboBox.setModel(newModel);
        assertEquals(newModel, simpleComboBox.getModel());
    }

    @Test
    public void testGetSelectedValueWithNullModel() {
        simpleComboBox.setModel(null);
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        simpleModel.setSelectedValue("Test");
        simpleComboBox.setModel(simpleModel);
        assertEquals("Test", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        simpleComboBox.setModel(simpleModel);
        simpleComboBox.setSelectedValue("Test");
        assertEquals("Test", simpleModel.getSelectedValue());
    }

    @Test
    public void testGetSelectedIntWithNullValue() {
        simpleComboBox.setSelectedValue(null);
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithIntegerValue() {
        simpleComboBox.setSelectedValue(10);
        assertEquals(10, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithLongValue() {
        simpleComboBox.setSelectedValue(100L);
        assertEquals(100, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithShortValue() {
        short shortValue = 50;
        simpleComboBox.setSelectedValue(shortValue);
        assertEquals(shortValue, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithByteValue() {
        byte byteValue = 5;
        simpleComboBox.setSelectedValue(byteValue);
        assertEquals(byteValue, simpleComboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithInvalidValue() {
        simpleComboBox.setSelectedValue("Invalid");
        simpleComboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLongWithNullValue() {
        simpleComboBox.setSelectedValue(null);
        assertEquals(-1L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithIntegerValue() {
        simpleComboBox.setSelectedValue(10);
        assertEquals(10L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithLongValue() {
        simpleComboBox.setSelectedValue(100L);
        assertEquals(100L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithShortValue() {
        short shortValue = 50;
        simpleComboBox.setSelectedValue(shortValue);
        assertEquals((long) shortValue, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithByteValue() {
        byte byteValue = 5;
        simpleComboBox.setSelectedValue(byteValue);
        assertEquals((long) byteValue, simpleComboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithInvalidValue() {
        simpleComboBox.setSelectedValue("Invalid");
        simpleComboBox.getSelectedLong();
    }
}