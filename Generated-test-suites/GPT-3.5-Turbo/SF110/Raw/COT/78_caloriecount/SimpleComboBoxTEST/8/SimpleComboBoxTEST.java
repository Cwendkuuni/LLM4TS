
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);

        comboBox.setModel(model);

        assertEquals(model, comboBox.getModel());
    }

    @Test
    public void testGetSelectedValue_NullModel() {
        SimpleComboBoxModel model = null;
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertNull(comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValue_ValidModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Value");
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals("Value", comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);

        comboBox.setSelectedValue("Value");

        assertEquals("Value", comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt_NullModel() {
        SimpleComboBoxModel model = null;
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals(-1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_IntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(5);
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals(5, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_LongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(5000L);
        SimpleComboBox comboBox = new SimpleComboBox(model);

        // Casting should truncate the long value to an int
        assertEquals(5000, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_ShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((short) 10);
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals(10, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_ByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((byte) 2);
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals(2, comboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_InvalidValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Value");
        SimpleComboBox comboBox = new SimpleComboBox(model);

        // Expecting an IllegalArgumentException to be thrown
        comboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLong_NullModel() {
        SimpleComboBoxModel model = null;
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals(-1L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_IntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(1000);
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals(1000L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_LongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(5000L);
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals(5000L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_ShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((short) 20);
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals(20L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_ByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((byte) 4);
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals(4L, comboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_InvalidValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Value");
        SimpleComboBox comboBox = new SimpleComboBox(model);

        // Expecting an IllegalArgumentException to be thrown
        comboBox.getSelectedLong();
    }

}

