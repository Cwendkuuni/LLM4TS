
import static org.junit.Assert.*;
import org.junit.Test;

public class ComboLegTest {
  
  @Test
  public void testDefaultConstructor() {
    ComboLeg leg = new ComboLeg();
    
    assertEquals(0, leg.m_conId);
    assertEquals(0, leg.m_ratio);
    assertNull(leg.m_action);
    assertNull(leg.m_exchange);
    assertEquals(0, leg.m_openClose);
    assertEquals(0, leg.m_shortSaleSlot);
    assertNull(leg.m_designatedLocation);
  }
  
  @Test
  public void testParameterizedConstructor1() {
    int conId = 100;
    int ratio = 2;
    String action = "BUY";
    String exchange = "NYSE";
    int openClose = ComboLeg.OPEN;
    
    ComboLeg leg = new ComboLeg(conId, ratio, action, exchange, openClose);
    
    assertEquals(conId, leg.m_conId);
    assertEquals(ratio, leg.m_ratio);
    assertEquals(action, leg.m_action);
    assertEquals(exchange, leg.m_exchange);
    assertEquals(openClose, leg.m_openClose);
    assertEquals(0, leg.m_shortSaleSlot);
    assertNull(leg.m_designatedLocation);
  }
  
  @Test
  public void testParameterizedConstructor2() {
    int conId = 200;
    int ratio = 3;
    String action = "SELL";
    String exchange = "NASDAQ";
    int openClose = ComboLeg.CLOSE;
    int shortSaleSlot = 1;
    String designatedLocation = "LOC1";
    
    ComboLeg leg = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);
    
    assertEquals(conId, leg.m_conId);
    assertEquals(ratio, leg.m_ratio);
    assertEquals(action, leg.m_action);
    assertEquals(exchange, leg.m_exchange);
    assertEquals(openClose, leg.m_openClose);
    assertEquals(shortSaleSlot, leg.m_shortSaleSlot);
    assertEquals(designatedLocation, leg.m_designatedLocation);
  }
  
  @Test
  public void testEqualsSameObject() {
    ComboLeg leg = new ComboLeg();
    
    assertTrue(leg.equals(leg));
  }
  
  @Test
  public void testEqualsNull() {
    ComboLeg leg = new ComboLeg();
    
    assertFalse(leg.equals(null));
  }
  
  @Test
  public void testEqualsDifferentObject() {
    ComboLeg leg1 = new ComboLeg();
    ComboLeg leg2 = new ComboLeg();
    
    assertFalse(leg1.equals(leg2));
  }
}
