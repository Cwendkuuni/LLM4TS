import static org.junit.Assert.*;
import org.junit.Test;

public class WhoISTest {

  @Test
  public void queryTest() {
    WhoIS whois = new WhoIS();
    whois.query("example query");
    // Add assertions to verify the correctness of the query method
  }

  @Test
  public void startQueryTest() {
    WhoIS whois = new WhoIS();
    whois.startQuery();
    // Add assertions to verify the correctness of the startQuery method
  }

  @Test
  public void displayErrorTest() {
    WhoIS whois = new WhoIS();
    whois.displayError("example error");
    // Add assertions to verify the correctness of the displayError method
  }

  @Test
  public void getQueryTest() {
    WhoIS whois = new WhoIS();
    whois.queryTextField.setText("example query");
    String query = whois.getQuery();
    // Add assertions to verify the correctness of the getQuery method
  }

  @Test
  public void resetTest() {
    WhoIS whois = new WhoIS();
    whois.reset();
    // Add assertions to verify the correctness of the reset method
  }
}