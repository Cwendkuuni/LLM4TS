import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    private SimpleComboBox comboBox;

    @Before
    public void setUp() {
        comboBox = new SimpleComboBox(new SimpleComboBoxModel());
    }

    @Test
    public void testSetModel() {
        ComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);

        assertEquals(model, comboBox.getModel());
        assertEquals(model, comboBox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValue() {
        assertNull(comboBox.getSelectedValue());

        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Value 1");
        model.addElement("Value 2");
        comboBox.setModel(model);

        assertEquals("Value 1", comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Value 1");
        model.addElement("Value 2");
        comboBox.setModel(model);

        comboBox.setSelectedValue("Value 2");
        assertEquals("Value 2", comboBox.getSelectedValue());

        comboBox.setSelectedValue("Value 3");
        assertEquals("Value 3", comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        comboBox.setModel(model);

        assertEquals(1, comboBox.getSelectedInt());

        model.addElement(3L);
        comboBox.setModel(model);

        assertEquals(3, comboBox.getSelectedInt());

        model.addElement((short)4);
        comboBox.setModel(model);

        assertEquals(4, comboBox.getSelectedInt());

        model.addElement((byte)5);
        comboBox.setModel(model);

        assertEquals(5, comboBox.getSelectedInt());

        try {
            model.addElement("Invalid");
            comboBox.setModel(model);

            comboBox.getSelectedInt();
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        comboBox.setModel(model);

        assertEquals(1L, comboBox.getSelectedLong());

        model.addElement(3L);
        comboBox.setModel(model);

        assertEquals(3L, comboBox.getSelectedLong());

        model.addElement((short)4);
        comboBox.setModel(model);

        assertEquals(4L, comboBox.getSelectedLong());

        model.addElement((byte)5);
        comboBox.setModel(model);

        assertEquals(5L, comboBox.getSelectedLong());

        try {
            model.addElement("Invalid");
            comboBox.setModel(model);

            comboBox.getSelectedLong();
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}