import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {

    private SimpleComboBox comboBox;

    @Before
    public void setUp() {
        comboBox = new SimpleComboBox(new SimpleComboBoxModel());
    }

    @Test
    public void testSetModelWithSimpleComboBoxModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);
        Assert.assertEquals(model, comboBox.getModel());
        Assert.assertEquals(model, comboBox.mySimpleModel);
    }

    @Test
    public void testSetModelWithOtherComboBoxModel() {
        ComboBoxModel model = new DefaultComboBoxModel();
        comboBox.setModel(model);
        Assert.assertEquals(model, comboBox.getModel());
        Assert.assertNull(comboBox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValueWithNullModel() {
        comboBox.mySimpleModel = null;
        Assert.assertNull(comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWithNonNullModel() {
        Object selectedValue = new Object();
        comboBox.mySimpleModel = new SimpleComboBoxModel(selectedValue);
        Assert.assertEquals(selectedValue, comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueWithExistingValue() {
        Object value = new Object();
        comboBox.mySimpleModel = new SimpleComboBoxModel(value);
        comboBox.setSelectedValue(value);
        Assert.assertEquals(value, comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueWithNonExistingValue() {
        Object value = new Object();
        comboBox.mySimpleModel = new SimpleComboBoxModel();
        comboBox.setSelectedValue(value);
        Assert.assertEquals(value, comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedIntWithIntegerValue() {
        Integer value = 10;
        comboBox.mySimpleModel = new SimpleComboBoxModel(value);
        Assert.assertEquals(value.intValue(), comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithLongValue() {
        Long value = 10L;
        comboBox.mySimpleModel = new SimpleComboBoxModel(value);
        Assert.assertEquals(value.intValue(), comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithShortValue() {
        Short value = 10;
        comboBox.mySimpleModel = new SimpleComboBoxModel(value);
        Assert.assertEquals(value.intValue(), comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithByteValue() {
        Byte value = 10;
        comboBox.mySimpleModel = new SimpleComboBoxModel(value);
        Assert.assertEquals(value.intValue(), comboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithInvalidValue() {
        Object value = new Object();
        comboBox.mySimpleModel = new SimpleComboBoxModel(value);
        comboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLongWithIntegerValue() {
        Integer value = 10;
        comboBox.mySimpleModel = new SimpleComboBoxModel(value);
        Assert.assertEquals(value.longValue(), comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithLongValue() {
        Long value = 10L;
        comboBox.mySimpleModel = new SimpleComboBoxModel(value);
        Assert.assertEquals(value.longValue(), comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithShortValue() {
        Short value = 10;
        comboBox.mySimpleModel = new SimpleComboBoxModel(value);
        Assert.assertEquals(value.longValue(), comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithByteValue() {
        Byte value = 10;
        comboBox.mySimpleModel = new SimpleComboBoxModel(value);
        Assert.assertEquals(value.longValue(), comboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithInvalidValue() {
        Object value = new Object();
        comboBox.mySimpleModel = new SimpleComboBoxModel(value);
        comboBox.getSelectedLong();
    }
}