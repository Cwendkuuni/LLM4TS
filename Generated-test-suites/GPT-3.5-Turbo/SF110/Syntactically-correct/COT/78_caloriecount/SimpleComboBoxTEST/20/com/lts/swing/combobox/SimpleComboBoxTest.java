import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;

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
        ComboBoxModel newModel = new DefaultComboBoxModel();
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        
        comboBox.setModel(newModel);
        assertEquals(newModel, comboBox.getModel());
        
        comboBox.setModel(simpleModel);
        assertEquals(simpleModel, comboBox.getModel());
    }

    @Test
    public void testGetSelectedValue() {
        assertNull(comboBox.getSelectedValue());

        String item1 = "Item 1";
        String item2 = "Item 2";
        model.addElement(item1);
        model.addElement(item2);

        comboBox.setSelectedItem(item2);
        assertEquals(item2, comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        String item1 = "Item 1";
        String item2 = "Item 2";
        model.addElement(item1);
        model.addElement(item2);

        comboBox.setSelectedValue(item2);
        assertEquals(item2, comboBox.getSelectedItem());
    }

    @Test
    public void testGetSelectedInt() {
        int intValue = 20;
        Integer integer = new Integer(intValue);
        model.addElement(integer);

        comboBox.setSelectedValue(integer);
        assertEquals(intValue, comboBox.getSelectedInt());

        long longValue = 30L;
        Long longObj = new Long(longValue);
        model.addElement(longObj);

        comboBox.setSelectedValue(longObj);
        assertEquals((int) longValue, comboBox.getSelectedInt());

        short shortValue = 10;
        Short shortObj = new Short(shortValue);
        model.addElement(shortObj);

        comboBox.setSelectedValue(shortObj);
        assertEquals((int) shortValue, comboBox.getSelectedInt());
        
        byte byteValue = 5;
        Byte byteObj = new Byte(byteValue);
        model.addElement(byteObj);

        comboBox.setSelectedValue(byteObj);
        assertEquals((int) byteValue, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        long longValue = 30L;
        Long longObj = new Long(longValue);
        model.addElement(longObj);

        comboBox.setSelectedValue(longObj);
        assertEquals(longValue, comboBox.getSelectedLong());

        int intValue = 20;
        Integer integer = new Integer(intValue);
        model.addElement(integer);

        comboBox.setSelectedValue(integer);
        assertEquals((long) intValue, comboBox.getSelectedLong());

        short shortValue = 10;
        Short shortObj = new Short(shortValue);
        model.addElement(shortObj);

        comboBox.setSelectedValue(shortObj);
        assertEquals((long) shortValue, comboBox.getSelectedLong());
        
        byte byteValue = 5;
        Byte byteObj = new Byte(byteValue);
        model.addElement(byteObj);

        comboBox.setSelectedValue(byteObj);
        assertEquals((long) byteValue, comboBox.getSelectedLong());
    }
}