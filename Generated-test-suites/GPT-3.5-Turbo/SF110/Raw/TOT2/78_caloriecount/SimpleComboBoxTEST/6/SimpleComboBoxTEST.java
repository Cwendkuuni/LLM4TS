import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {

    SimpleComboBox myComboBox;

    @Before
    public void setUp() {
        myComboBox = new SimpleComboBox(new SimpleComboBoxModel());
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel newModel = new SimpleComboBoxModel();
        myComboBox.setModel(newModel);
        assertEquals(newModel, myComboBox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValue() {
        assertNull(myComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        Object value = "Test";
        myComboBox.setSelectedValue(value);
        assertEquals(value, myComboBox.mySimpleModel.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        assertEquals(-1, myComboBox.getSelectedInt());
        Object intValue = 10;
        myComboBox.setSelectedValue(intValue);
        assertEquals(intValue, myComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        assertEquals(-1L, myComboBox.getSelectedLong());
        Object longValue = 100L;
        myComboBox.setSelectedValue(longValue);
        assertEquals(longValue, myComboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntException() {
        Object stringValue = "Test";
        myComboBox.setSelectedValue(stringValue);
        myComboBox.getSelectedInt();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongException() {
        Object stringValue = "Test";
        myComboBox.setSelectedValue(stringValue);
        myComboBox.getSelectedLong();
    }
}