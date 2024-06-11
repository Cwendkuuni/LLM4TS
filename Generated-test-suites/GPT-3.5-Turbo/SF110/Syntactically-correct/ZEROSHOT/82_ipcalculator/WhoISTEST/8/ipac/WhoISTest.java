import org.junit.*;
import static org.junit.Assert.*;

public class WhoISTest {

    @Test
    public void testActionPerformed_queryButton(){
        WhoIS whoIs = new WhoIS();
        whoIs.queryTextField.setText("TestQuery");
        whoIs.actionPerformed(new ActionEvent(whoIs.queryButton, ActionEvent.ACTION_PERFORMED, ""));
        // Check if the query is set correctly
        assertEquals("TestQuery", whoIs.getQuery());
    }

    @Test
    public void testActionPerformed_resetButton(){
        WhoIS whoIs = new WhoIS();
        whoIs.queryTextField.setText("TestQuery");
        whoIs.resetButton.setEnabled(true);
        whoIs.actionPerformed(new ActionEvent(whoIs.resetButton, ActionEvent.ACTION_PERFORMED, ""));
        // Check if the query text field is cleared
        assertEquals("", whoIs.queryTextField.getText());
        // Check if the reset radio button is selected
        assertTrue(whoIs.resetRadioButton.isSelected());
        // Check if the lowercaseD checkbox is unselected
        assertFalse(whoIs.lowercaseD.isSelected());
        // Check if the inverseQueries combobox is set to index 0
        assertEquals(0, whoIs.inverseQueries.getSelectedIndex());
        // Check if the miscList is set to index 0
        assertEquals(0, whoIs.miscList.getSelectedIndex());
    }

    @Test
    public void testgetQuery() {
        WhoIS whoIs = new WhoIS();
        whoIs.queryTextField.setText("TestQuery");
        whoIs.lowercaseL.setSelected(true);
        whoIs.inverseQueries.setSelectedIndex(1);
        whoIs.miscList.setSelectedIndex(2);
        String expectedResult = "-l -i admin-c TestQuery";
        assertEquals(expectedResult, whoIs.getQuery());
    }

    @Test
    public void testReset() {
        WhoIS whoIs = new WhoIS();
        whoIs.queryTextField.setText("TestQuery");
        whoIs.reset();

        // Check if the query text field is cleared
        assertEquals("", whoIs.queryTextField.getText());
        // Check if the reset radio button is selected
        assertTrue(whoIs.resetRadioButton.isSelected());
        // Check if the lowercaseD checkbox is unselected
        assertFalse(whoIs.lowercaseD.isSelected());
        // Check if the inverseQueries combobox is set to index 0
        assertEquals(0, whoIs.inverseQueries.getSelectedIndex());
        // Check if the miscList is set to index 0
        assertEquals(0, whoIs.miscList.getSelectedIndex());
    }
}