import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class WhoISTest {
  
  private WhoIS whois;
  
  @Before
  public void setUp() {
    whois = new WhoIS();
  }
  
  @Test
  public void testQueryTextField() {
    whois.queryTextField.setText("Test Query");
    assertEquals("Test Query", whois.queryTextField.getText());
  }
  
  @Test
  public void testLowercaseL() {
    whois.lowercaseL.setSelected(true);
    assertTrue(whois.lowercaseL.isSelected());
  }
  
  @Test
  public void testUppercaseL() {
    whois.uppercaseL.setSelected(true);
    assertTrue(whois.uppercaseL.isSelected());
  }
  
  @Test
  public void testLowercaseM() {
    whois.lowercaseM.setSelected(true);
    assertTrue(whois.lowercaseM.isSelected());
  }
  
  @Test
  public void testUppercaseM() {
    whois.uppercaseM.setSelected(true);
    assertTrue(whois.uppercaseM.isSelected());
  }
  
  @Test
  public void testLowercaseX() {
    whois.lowercaseX.setSelected(true);
    assertTrue(whois.lowercaseX.isSelected());
  }
  
  @Test
  public void testLowercaseD() {
    whois.booleanD = true;
    assertTrue(whois.booleanD);
  }
  
  @Test
  public void testInverseQueries() {
    whois.inverseQueries.setSelectedIndex(1);
    assertEquals("admin-c", whois.inverseQueries.getSelectedItem().toString());
    whois.inverseQueries.setSelectedIndex(2);
    assertEquals("admin-c,tech-c,zone-c", whois.inverseQueries.getSelectedItem().toString());
    // Test more cases...
  }
  
  @Test
  public void testMiscList() {
    whois.miscList.setSelectedIndex(1);
    assertEquals("as-block", whois.miscList.getSelectedValue());
    // Test more cases...
  }
  
  @Test
  public void testReset() {
    whois.queryTextField.setText("Test Query");
    whois.reset();
    assertEquals("", whois.queryTextField.getText());
    assertFalse(whois.lowercaseD.isSelected());
    assertEquals(0, whois.inverseQueries.getSelectedIndex());
    assertEquals(0, whois.miscList.getSelectedIndex());
  }
}