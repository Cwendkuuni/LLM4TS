import org.junit.Test;
import static org.junit.Assert.*;

public class WhoISTest {

    @Test
    public void testStartQueryWithNoQuery() {
        WhoIS whois = new WhoIS();
        // Testing when there is no query detected
        // Expected behavior: Display error message "No Query Detected"
        // An error message dialog should be shown
        // Assuming queryTextField is empty initially
        whois.startQuery();
    }

    @Test
    public void testStartQueryWithValidQuery() {
        WhoIS whois = new WhoIS();
        // Testing with a valid query
        // Expected behavior: Query method should be called
        // No error message should be displayed
        // Assuming valid input is provided in queryTextField
        whois.queryTextField.setText("examplequery");
        whois.startQuery();
    }

    @Test
    public void testDisplayError() {
        WhoIS whois = new WhoIS();
        // Testing the displayError method
        // Expected behavior: Display error message with the provided string
        // An error message dialog should be shown
        String errorMessage = "This is an error message";
        whois.displayError(errorMessage);
    }

    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        // Testing the reset method
        // Expected behavior: All fields should be reset to their initial state
        // queryTextField should be empty, resetRadioButton selected, lowercaseD unchecked, etc.
        // Assuming some fields are modified before calling reset
        whois.queryTextField.setText("examplequery");
        whois.lowercaseL.setSelected(true);
        whois.reset();
        assertEquals("", whois.queryTextField.getText());
        assertTrue(whois.resetRadioButton.isSelected());
        assertFalse(whois.lowercaseD.isSelected());
    }

    // Add more test cases for other methods as needed

}