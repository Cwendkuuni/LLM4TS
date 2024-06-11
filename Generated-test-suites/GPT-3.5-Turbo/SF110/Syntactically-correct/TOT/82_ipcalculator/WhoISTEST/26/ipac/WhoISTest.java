import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WhoISTest {

    private WhoIS whois;

    @Before
    public void setUp() {
        whois = new WhoIS();
    }

    // Test cases for queryPanel() method

    @Test
    public void testQueryPanel_validInput() {
        // Arrange
        String query = "example";

        // Act
        whois.queryTextField.setText(query);
        whois.queryButton.doClick();

        // Assert
        // Check if the query was processed successfully
        // Check the expected output using a mock response
        assertEquals("Expected output", WhoISOutput.getOutput());
    }

    @Test
    public void testQueryPanel_emptyInput() {
        // Arrange
        String query = "";

        // Act
        whois.queryTextField.setText(query);
        whois.queryButton.doClick();

        // Assert
        // Check if an error message is displayed
        assertTrue("Error message displayed", WhoISOutput.isErrorMessageDisplayed());
    }

    @Test
    public void testQueryPanel_invalidInput() {
        // Arrange
        String query = "!@#$%";

        // Act
        whois.queryTextField.setText(query);
        whois.queryButton.doClick();

        // Assert
        // Check if an error message is displayed
        assertTrue("Error message displayed", WhoISOutput.isErrorMessageDisplayed());
    }

    // Test cases for additionalQueriesPanel() method

    @Test
    public void testAdditionalQueriesPanel_IPAddressLookups() {
        // Arrange
        whois.lowercaseL.setSelected(true);
        String query = "example";

        // Act
        whois.queryTextField.setText(query);
        whois.queryButton.doClick();

        // Assert
        // Check if the query was processed successfully with lowercaseL option
        // Check the expected output using a mock response
        assertEquals("Expected output", WhoISOutput.getOutput());
    }

    @Test
    public void testAdditionalQueriesPanel_MiscellaneousQueries() {
        // Arrange
        whois.miscList.setSelectedIndex(1); // Select "as-block" object type
        String query = "example";

        // Act
        whois.queryTextField.setText(query);
        whois.queryButton.doClick();

        // Assert
        // Check if the query was processed successfully with miscList option
        // Check the expected output using a mock response
        assertEquals("Expected output", WhoISOutput.getOutput());
    }

    // Test cases for queryHintsPanel() method

    @Test
    public void testQueryHintsPanel_startQuery() {
        // Arrange
        String query = "example";

        // Act
        whois.queryTextField.setText(query);
        whois.queryButton.doClick();

        // Assert
        // Check if the query was processed successfully
        // Check the expected output with query hints
        assertEquals("Expected output", WhoISOutput.getOutputWithQueryHints());
    }

    // Test cases for reset() method

    @Test
    public void testReset() {
        // Arrange
        whois.queryTextField.setText("example");
        whois.lowercaseD.setSelected(true);
        whois.inverseQueries.setSelectedIndex(1); // Select "admin-c" option
        whois.miscList.setSelectedIndex(2); // Select "as-set" object type

        // Act
        whois.reset();

        // Assert
        // Check if the query text, options, and object types are reset
        assertEquals("", whois.queryTextField.getText());
        assertFalse("Radio Button not selected", whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }

}