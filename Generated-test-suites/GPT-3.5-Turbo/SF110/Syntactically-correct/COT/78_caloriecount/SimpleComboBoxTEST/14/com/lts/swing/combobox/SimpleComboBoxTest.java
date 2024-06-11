
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    @Test
    public void testGetSelectedValue_NullModel_ReturnNull() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue_WithModel_SetValueSuccessfully(){
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue("Test");
        assertEquals("Test", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt_NullModel_ReturnNegativeOne() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_IntegerType_ReturnIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(5);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertEquals(5, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_LongType_ReturnIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(100000000000L);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertEquals(1410065408, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_ShortType_ReturnShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 10);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertEquals(10, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_ByteType_ReturnByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 2);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertEquals(2, simpleComboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_InvalidType_ThrowIllegalArgumentException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("invalid");
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLong_NullModel_ReturnNegativeOne() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        assertEquals(-1L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_IntegerType_ReturnLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(5);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertEquals(5L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_LongType_ReturnLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(100000000000L);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertEquals(100000000000L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_ShortType_ReturnLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 10);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertEquals(10L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_ByteType_ReturnLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 2);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertEquals(2L, simpleComboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_InvalidType_ThrowIllegalArgumentException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("invalid");
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.getSelectedLong();
    }
}

