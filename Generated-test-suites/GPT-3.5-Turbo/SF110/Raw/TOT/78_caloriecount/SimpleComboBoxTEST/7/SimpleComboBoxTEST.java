import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;

public class SimpleComboBoxTest {

    private SimpleComboBox simpleComboBox;
    private SimpleComboBoxModel simpleComboBoxModel;

    @Before
    public void setUp() {
        simpleComboBoxModel = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
    }

    @Test
    public void testSetModel_ValidModel_ModelSet() {
        assertFalse(simpleComboBox.isEditable());
        assertEquals(simpleComboBoxModel, simpleComboBox.getModel());
    }

    @Test
    public void testSetModel_InvalidModel_ExceptionThrown() {
        ComboBoxModel invalidModel = new DefaultComboBoxModel();

        try {
            simpleComboBox.setModel(invalidModel);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected Exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetSelectedValue_NullModel_ReturnsNull() {
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValue_HasModel_ReturnsSelectedValue() {
        String value = "Test value";
        simpleComboBoxModel.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue_NullValue_ValueSet() {
        assertNull(simpleComboBox.getSelectedValue());

        String value = "Test value";
        simpleComboBox.setSelectedValue(value);

        assertEquals(value, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt_NullModel_ReturnsNegativeOne() {
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_IntegerValue_ReturnsIntegerValue() {
        int value = 123;
        simpleComboBoxModel.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_LongValue_ReturnsConvertedLongValue() {
        long value = 10000000000L;
        simpleComboBoxModel.setSelectedValue(value);
        assertEquals((int)value, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_ShortValue_ReturnsShortValue() {
        short value = 99;
        simpleComboBoxModel.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_ByteValue_ReturnsByteValue() {
        byte value = 5;
        simpleComboBoxModel.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_InvalidType_ExceptionThrown() {
        Object value = "Test value";
        simpleComboBoxModel.setSelectedValue(value);

        try {
            simpleComboBox.getSelectedInt();
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected Exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetSelectedLong_NullModel_ReturnsNegativeOne() {
        assertEquals(-1L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_IntegerValue_ReturnsLongValue() {
        long value = 100000000000L;
        simpleComboBoxModel.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_LongValue_ReturnsLongValue() {
        long value = 1234567890L;
        simpleComboBoxModel.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_ShortValue_ReturnsConvertedLongValue() {
        short value = 99;
        simpleComboBoxModel.setSelectedValue(value);
        assertEquals((long)value, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_ByteValue_ReturnsConvertedLongValue() {
        byte value = 5;
        simpleComboBoxModel.setSelectedValue(value);
        assertEquals((long)value, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_InvalidType_ExceptionThrown() {
        Object value = "Test value";
        simpleComboBoxModel.setSelectedValue(value);

        try {
            simpleComboBox.getSelectedLong();
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected Exception: " + e.getMessage());
        }
    }
}