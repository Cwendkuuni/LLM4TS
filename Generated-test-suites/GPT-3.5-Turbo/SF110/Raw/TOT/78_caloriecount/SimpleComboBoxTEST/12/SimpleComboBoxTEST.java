import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    private SimpleComboBox comboBox;
    private SimpleComboBoxModel model;

    @Before
    public void setUp() {
        model = new SimpleComboBoxModel();
        comboBox = new SimpleComboBox(model);
    }

    @Test
    public void testSetModel() {
        // Typical use case
        ComboBoxModel newModel = new SimpleComboBoxModel();
        comboBox.setModel(newModel);
        assertEquals(newModel, comboBox.getModel());

        // Error scenario - null value should throw an exception
        try {
            comboBox.setModel(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetSelectedValue() {
        // Typical use case
        model.setSelectedValue("Value");
        assertEquals("Value", comboBox.getSelectedValue());

        // Edge case - no selected value
        model.setSelectedValue(null);
        assertNull(comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        // Typical use case
        comboBox.setSelectedValue("Value");
        assertEquals("Value", model.getSelectedValue());

        // Error scenario - null value should throw an exception
        try {
            comboBox.setSelectedValue(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetSelectedInt() {
        // Typical use case - Integer value
        model.setSelectedValue(5);
        assertEquals(5, comboBox.getSelectedInt());

        // Typical use case - Long value that can be cast to int
        model.setSelectedValue(10L);
        assertEquals(10, comboBox.getSelectedInt());

        // Typical use case - Short value
        model.setSelectedValue((short) 15);
        assertEquals(15, comboBox.getSelectedInt());

        // Typical use case - Byte value
        model.setSelectedValue((byte) 20);
        assertEquals(20, comboBox.getSelectedInt());

        // Error scenario - String value should throw an exception
        model.setSelectedValue("Value");
        try {
            comboBox.getSelectedInt();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        // Edge case - no selected value
        model.setSelectedValue(null);
        assertEquals(-1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        // Typical use case - Integer value
        model.setSelectedValue(5);
        assertEquals(5L, comboBox.getSelectedLong());

        // Typical use case - Long value
        model.setSelectedValue(10L);
        assertEquals(10L, comboBox.getSelectedLong());

        // Typical use case - Short value that can be cast to long
        model.setSelectedValue((short) 15);
        assertEquals(15L, comboBox.getSelectedLong());

        // Typical use case - Byte value that can be cast to long
        model.setSelectedValue((byte) 20);
        assertEquals(20L, comboBox.getSelectedLong());

        // Error scenario - String value should throw an exception
        model.setSelectedValue("Value");
        try {
            comboBox.getSelectedLong();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        // Edge case - no selected value
        model.setSelectedValue(null);
        assertEquals(-1L, comboBox.getSelectedLong());
    }
}