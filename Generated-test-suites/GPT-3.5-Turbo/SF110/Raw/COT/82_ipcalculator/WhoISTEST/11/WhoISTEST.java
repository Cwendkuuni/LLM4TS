import org.junit.Test;

public class WhoISTest {

    @Test
    public void testConstructor() {
        WhoIS whois = new WhoIS();
        // Asserts go here
    }

    @Test
    public void testActionPerformedQueryButton() {
        WhoIS whois = new WhoIS();
        // Simulate a click on queryButton
        // Asserts go here
    }

    @Test
    public void testActionPerformedResetButton() {
        WhoIS whois = new WhoIS();
        // Simulate a click on resetButton
        // Asserts go here
    }

    @Test
    public void testStartQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example query");
        // Call startQuery method
        // Asserts go here
    }

    @Test
    public void testDisplayError() {
        WhoIS whois = new WhoIS();
        String error = "Example error";
        // Call displayError method with the error message
        // Asserts go here
    }

    @Test
    public void testQuery() {
        WhoIS whois = new WhoIS();
        String query = "example query";
        // Call query method with the query string
        // Asserts go here
    }

    @Test
    public void testGetQuery() {
        WhoIS whois = new WhoIS();
        // Set the required fields for a valid query
        String expectedQuery = "expected query string";
        String actualQuery = whois.getQuery();
        // Asserts go here
    }

    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        // Call reset method
        // Asserts go here
    }
}