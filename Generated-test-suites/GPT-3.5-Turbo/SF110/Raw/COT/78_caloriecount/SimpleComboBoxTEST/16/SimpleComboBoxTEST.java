import static org.junit.Assert.*;

import javax.swing.DefaultComboBoxModel;
import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {

    private SimpleComboBox comboBox;
    private SimpleComboBoxModel model;

    @Before
    public void setup() {
        model = new SimpleComboBoxModel();
        comboBox = new SimpleComboBox(model);
    }

    @Test
    public void testSetModel() {
        JComboBox.DefaultComboBoxModel defaultModel = new JComboBox.DefaultComboBoxModel(new String[] { "A", "B", "C" });
        comboBox.setModel(defaultModel);

        assertEquals(defaultModel, comboBox.getModel());
    }

    @Test
    public void testGetSelectedValueWhenModelIsNull() {
        assertNull(comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValue() {
        model.addElement("A");
        model.addElement("B");
        model.setSelectedItem("B");

        assertEquals("B", comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        model.addElement("A");
        model.addElement("B");
        comboBox.setSelectedValue("B");

        assertEquals("B", comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedIntWithIntegerValue() {
        model.addElement(123);
        model.setSelectedItem(123);

        assertEquals(123, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithLongValue() {
        model.addElement(123L);
        model.setSelectedItem(123L);

        assertEquals(123, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithShortValue() {
        model.addElement((short) 123);
        model.setSelectedItem((short) 123);

        assertEquals(123, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithByteValue() {
        model.addElement((byte) 123);
        model.setSelectedItem((byte) 123);

        assertEquals(123, comboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithInvalidValue() {
        model.addElement("A");
        model.setSelectedItem("A");

        comboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLongWithIntegerValue() {
        model.addElement(123);
        model.setSelectedItem(123);

        assertEquals(123L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithLongValue() {
        model.addElement(123L);
        model.setSelectedItem(123L);

        assertEquals(123L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithShortValue() {
        model.addElement((short) 123);
        model.setSelectedItem((short) 123);

        assertEquals(123L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithByteValue() {
        model.addElement((byte) 123);
        model.setSelectedItem((byte) 123);

        assertEquals(123L, comboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithInvalidValue() {
        model.addElement("A");
        model.setSelectedItem("A");

        comboBox.getSelectedLong();
    }
}


