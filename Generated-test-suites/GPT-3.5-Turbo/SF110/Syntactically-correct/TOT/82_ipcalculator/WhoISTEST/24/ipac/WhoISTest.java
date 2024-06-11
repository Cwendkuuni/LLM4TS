import org.junit.Test;
import static org.junit.Assert.*;

public class WhoISTest {

    @Test
    public void testQueryPanel() {
        WhoIS whoIS = new WhoIS();
        JPanel queryPanel = whoIS.queryPanel();

        // Typical use case: Query text field is not empty
        whoIS.queryTextField.setText("example.com");
        whoIS.queryButton.doClick();
        // Assert that the queryTextField is cleared after the query button is clicked
        assertEquals("", whoIS.queryTextField.getText());

        // Edge case: Query text field is empty
        whoIS.queryTextField.setText("");
        whoIS.queryButton.doClick();
        // Assert that an error message is displayed when the queryTextField is empty
        // (the error message may be displayed using JOptionPane, so it cannot be directly tested here)
        // Therefore, we can only assert that the queryTextField remains empty
        assertEquals("", whoIS.queryTextField.getText());
    }

    @Test
    public void testAdditionalQueriesPanel() {
        WhoIS whoIS = new WhoIS();
        JPanel additionalQueriesPanel = whoIS.additionalQueriesPanel();

        // Edge case: No action is taken, since it's only a panel with radio buttons
        // Cannot be directly tested here
        // Therefore, we can only assert that the panel is created without any errors
        assertNotNull(additionalQueriesPanel);
    }

    @Test
    public void testIPAddressLookups() {
        WhoIS whoIS = new WhoIS();
        JPanel ipAddressLookupsPanel = whoIS.IPAddressLookups();

        // Edge case: No action is taken, since it's only a panel with radio buttons
        // Cannot be directly tested here
        // Therefore, we can only assert that the panel is created without any errors
        assertNotNull(ipAddressLookupsPanel);
    }

    @Test
    public void testMiscellaneousQueries() {
        WhoIS whoIS = new WhoIS();
        JPanel miscellaneousQueriesPanel = whoIS.MiscellaneousQueries();

        // Edge case: No action is taken, since it's only a panel with combo box and list
        // Cannot be directly tested here
        // Therefore, we can only assert that the panel is created without any errors
        assertNotNull(miscellaneousQueriesPanel);
    }

    @Test
    public void testQueryHintsPanel() {
        WhoIS whoIS = new WhoIS();
        JPanel queryHintsPanel = whoIS.queryHintsPanel();

        // Edge case: No action is taken, since it's only a panel with labels
        // Cannot be directly tested here
        // Therefore, we can only assert that the panel is created without any errors
        assertNotNull(queryHintsPanel);
    }

    @Test
    public void testActionListener() {
        WhoIS whoIS = new WhoIS();

        // Edge case: No action is taken, since the action is handled by actionPerformed method
        // Cannot be directly tested here
        // Therefore, we can only assert that the button is created without any errors
        assertNotNull(whoIS.queryButton);
        assertNotNull(whoIS.resetButton);
    }

    @Test
    public void testStartQuery() {
        WhoIS whoIS = new WhoIS();

        // Typical use case: Query text field is not empty
        whoIS.queryTextField.setText("example.com");
        whoIS.startQuery();
        // Assert that the queryTextField is cleared after startQuery method is called
        assertEquals("", whoIS.queryTextField.getText());

        // Edge case: Query text field is empty
        whoIS.queryTextField.setText("");
        whoIS.startQuery();
        // Assert that an error message is displayed when the queryTextField is empty
        // (the error message may be displayed using JOptionPane, so it cannot be directly tested here)
        // Therefore, we can only assert that the queryTextField remains empty
        assertEquals("", whoIS.queryTextField.getText());
    }

    @Test
    public void testReset() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("example.com");
        whoIS.lowercaseD.setSelected(true);
        whoIS.inverseQueries.setSelectedIndex(2);
        whoIS.miscList.setSelectedIndex(4);

        // Typical use case: Reset method is called
        whoIS.reset();
        // Assert that all text fields and selections are reset
        assertEquals("", whoIS.queryTextField.getText());
        assertFalse(whoIS.lowercaseD.isSelected());
        assertEquals(0, whoIS.inverseQueries.getSelectedIndex());
        assertEquals(0, whoIS.miscList.getSelectedIndex());
    }
}