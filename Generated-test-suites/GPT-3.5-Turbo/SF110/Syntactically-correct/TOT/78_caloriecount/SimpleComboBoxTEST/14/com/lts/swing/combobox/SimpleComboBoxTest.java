import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {

    private SimpleComboBox comboBox;

    @Before
    public void setup() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox = new SimpleComboBox(model);
    }

    @Test
    public void testSetModel_WithSimpleComboBoxModel_ShouldSetModelCorrectly() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
  
        comboBox.setModel(model);

        Assert.assertEquals(model, comboBox.getModel());
    }

    @Test
    public void testSetModel_WithNonSimpleComboBoxModel_ShouldNotSetModel() {
        MyComboBoxModel model = new MyComboBoxModel();
  
        comboBox.setModel(model);

        Assert.assertNotEquals(model, comboBox.getModel());
    }

    @Test
    public void testGetSelectedValue_WithNullSimpleModel_ShouldReturnNull() {
        Assert.assertNull(comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValue_WithNonNullSimpleModel_ShouldReturnSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Test");
        comboBox.setModel(model);

        Assert.assertEquals("Test", comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue_ShouldSetSelectedValueInSimpleModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);

        comboBox.setSelectedValue("Test");

        Assert.assertEquals("Test", model.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_WithInvalidType_ShouldThrowIllegalArgumentException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Test");
        comboBox.setModel(model);

        comboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedInt_WithIntegerValue_ShouldReturnIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10);
        comboBox.setModel(model);

        Assert.assertEquals(10, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_WithLongValueWithinIntegerRange_ShouldReturnIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(100L);
        comboBox.setModel(model);

        Assert.assertEquals(100, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_WithLongValueOutsideIntegerRange_ShouldReturnIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(Long.MAX_VALUE);
        comboBox.setModel(model);

        Assert.assertEquals(Integer.MAX_VALUE, comboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_WithInvalidType_ShouldThrowIllegalArgumentException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Test");
        comboBox.setModel(model);

        comboBox.getSelectedLong();
    }

    @Test
    public void testGetSelectedLong_WithLongValue_ShouldReturnLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(100L);
        comboBox.setModel(model);

        Assert.assertEquals(100L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_WithIntegerValue_ShouldReturnLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10);
        comboBox.setModel(model);

        Assert.assertEquals(10L, comboBox.getSelectedLong());
    }
}