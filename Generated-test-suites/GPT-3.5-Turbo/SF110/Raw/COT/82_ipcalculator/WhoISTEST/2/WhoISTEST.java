import org.junit.*;
import static org.junit.Assert.*;

public class WhoISTest {

  @Test
  public void testQueryTextField() {
    WhoIS whois = new WhoIS();
    whois.queryTextField.setText("example");
    assertEquals("example", whois.getQuery());
  }

  @Test
  public void testInverseQueries() {
    WhoIS whois = new WhoIS();
    whois.inverseQueries.setSelectedIndex(2);
    assertEquals("-i admin-c,tech-c,zone-c example", whois.getQuery());
  }

  @Test
  public void testLowercaseL() {
    WhoIS whois = new WhoIS();
    whois.lowercaseL.setSelected(true);
    assertEquals("-l example", whois.getQuery());
  }

  @Test
  public void testUppercaseL() {
    WhoIS whois = new WhoIS();
    whois.uppercaseL.setSelected(true);
    assertEquals("-L example", whois.getQuery());
  }

  @Test
  public void testLowercaseM() {
    WhoIS whois = new WhoIS();
    whois.lowercaseM.setSelected(true);
    assertEquals("-m example", whois.getQuery());
  }

  @Test
  public void testUppercaseM() {
    WhoIS whois = new WhoIS();
    whois.uppercaseM.setSelected(true);
    assertEquals("-M example", whois.getQuery());
  }

  @Test
  public void testLowercaseX() {
    WhoIS whois = new WhoIS();
    whois.lowercaseX.setSelected(true);
    assertEquals("-x example", whois.getQuery());
  }

  @Test
  public void testLowercaseD() {
    WhoIS whois = new WhoIS();
    whois.lowercaseD.setSelected(true);
    assertEquals("-d example", whois.getQuery());
  }

  @Test
  public void testMiscList() {
    WhoIS whois = new WhoIS();
    whois.miscList.setSelectedIndices(new int[]{2, 4});
    assertEquals("-T as-set,filter-set example", whois.getQuery());
  }

  @Test
  public void testReset() {
    WhoIS whois = new WhoIS();
    whois.queryTextField.setText("example");
    whois.inverseQueries.setSelectedIndex(2);
    whois.lowercaseL.setSelected(true);
    whois.reset();
    assertEquals("", whois.queryTextField.getText());
    assertFalse(whois.lowercaseD.isSelected());
    assertEquals(0, whois.inverseQueries.getSelectedIndex());
    assertEquals(0, whois.miscList.getSelectedIndex());
  }

}