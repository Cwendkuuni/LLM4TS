
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {
    
    private static final Integer INTEGER_VALUE = 123;
    private static final Long LONG_VALUE = 456L;
    private static final Short SHORT_VALUE = 789;

    private SimpleComboBox simpleComboBox;

    @Before
    public void setUp() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(model);
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);

        assertEquals(model, simpleComboBox.getModel());
    }

    @Test
    public void testGetSelectedValue_NullModel() {
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValue_ValidModel() {
        Object selectedValue = new Object();
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(selectedValue);
        simpleComboBox.setModel(model);

        assertEquals(selectedValue, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        Object selectedValue = new Object();
        simpleComboBox.setSelectedValue(selectedValue);

        assertEquals(selectedValue, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt_IntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(INTEGER_VALUE);
        simpleComboBox.setModel(model);

        assertEquals(INTEGER_VALUE, (Integer) simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_LongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(LONG_VALUE);
        simpleComboBox.setModel(model);

        assertEquals((int) LONG_VALUE, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_ShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(SHORT_VALUE);
        simpleComboBox.setModel(model);

        assertEquals(SHORT_VALUE, (Short) simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_ByteValue() {
        byte byteValue = 10;
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(byteValue);
        simpleComboBox.setModel(model);

        assertEquals(byteValue, (Byte) simpleComboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_InvalidValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Invalid");
        simpleComboBox.setModel(model);

        simpleComboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLong_IntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(INTEGER_VALUE);
        simpleComboBox.setModel(model);

        assertEquals(INTEGER_VALUE, (Integer) simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_LongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(LONG_VALUE);
        simpleComboBox.setModel(model);

        assertEquals(LONG_VALUE, (Long) simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_ShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(SHORT_VALUE);
        simpleComboBox.setModel(model);

        assertEquals((long) SHORT_VALUE, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_ByteValue() {
        byte byteValue = 10;
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(byteValue);
        simpleComboBox.setModel(model);

        assertEquals((long) byteValue, simpleComboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_InvalidValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Invalid");
        simpleComboBox.setModel(model);

        simpleComboBox.getSelectedLong();
    }

}

