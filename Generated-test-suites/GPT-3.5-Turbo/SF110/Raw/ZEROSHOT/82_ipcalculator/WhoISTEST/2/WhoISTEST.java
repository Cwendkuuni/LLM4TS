import org.junit.*;
import static org.junit.Assert.*;

public class WhoISTest {

  @Test
  public void testActionPerformed_queryButton() {
    WhoIS whois = new WhoIS();
    whois.queryTextField.setText("example");
    whois.actionPerformed(new ActionEvent(whois.queryButton, ActionEvent.ACTION_PERFORMED, ""));
    // Add assertion statements here
  }

  @Test
  public void testActionPerformed_resetButton() {
    WhoIS whois = new WhoIS();
    whois.reset();
    // Add assertion statements here
  }

  @Test
  public void testStartQuery_emptyQuery() {
    WhoIS whois = new WhoIS();
    whois.queryTextField.setText("");
    whois.startQuery();
    // Add assertion statements here
  }

  @Test
  public void testStartQuery_nonEmptyQuery() {
    WhoIS whois = new WhoIS();
    whois.queryTextField.setText("example");
    whois.startQuery();
    // Add assertion statements here
  }

  @Test
  public void testGetQuery_emptyQuery() {
    WhoIS whois = new WhoIS();
    whois.queryTextField.setText("");
    String query = whois.getQuery();
    assertEquals("", query);
  }

  @Test
  public void testGetQuery_nonEmptyQuery() {
    WhoIS whois = new WhoIS();
    whois.queryTextField.setText("example");
    String query = whois.getQuery();
    assertEquals("example", query);
  }

  @Test
  public void testReset() {
    WhoIS whois = new WhoIS();
    whois.reset();
    // Add assertion statements here
  }

}