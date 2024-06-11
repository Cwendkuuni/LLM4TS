package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ComboLegTest {
  
  @Test
  public void testEmptyComboLeg() {
    ComboLeg comboLeg = new ComboLeg();
    assertEquals(0, comboLeg.m_conId);
    assertEquals(0, comboLeg.m_ratio);
    assertEquals(null, comboLeg.m_action);
    assertEquals(null, comboLeg.m_exchange);
    assertEquals(0, comboLeg.m_openClose);
    assertEquals(0, comboLeg.m_shortSaleSlot);
    assertEquals(null, comboLeg.m_designatedLocation);
  }
  
  @Test
  public void testComboLegWithParameters() {
    ComboLeg comboLeg = new ComboLeg(123, 2, "BUY", "NYSE", ComboLeg.OPEN);
    assertEquals(123, comboLeg.m_conId);
    assertEquals(2, comboLeg.m_ratio);
    assertEquals("BUY", comboLeg.m_action);
    assertEquals("NYSE", comboLeg.m_exchange);
    assertEquals(ComboLeg.OPEN, comboLeg.m_openClose);
    assertEquals(0, comboLeg.m_shortSaleSlot);
    assertEquals(null, comboLeg.m_designatedLocation);
  }
  
  @Test
  public void testComboLegWithAllParameters() {
    ComboLeg comboLeg = new ComboLeg(456, 1, "SELL", "NASDAQ", ComboLeg.CLOSE, 1, "LOC");
    assertEquals(456, comboLeg.m_conId);
    assertEquals(1, comboLeg.m_ratio);
    assertEquals("SELL", comboLeg.m_action);
    assertEquals("NASDAQ", comboLeg.m_exchange);
    assertEquals(ComboLeg.CLOSE, comboLeg.m_openClose);
    assertEquals(1, comboLeg.m_shortSaleSlot);
    assertEquals("LOC", comboLeg.m_designatedLocation);
  }
  
  @Test
  public void testEquals() {
    ComboLeg comboLeg1 = new ComboLeg(123, 2, "BUY", "NYSE", ComboLeg.OPEN);
    ComboLeg comboLeg2 = new ComboLeg(123, 2, "BUY", "NYSE", ComboLeg.OPEN);
    assertEquals(true, comboLeg1.equals(comboLeg2));
  }
  
  @Test
  public void testNotEquals() {
    ComboLeg comboLeg1 = new ComboLeg(123, 2, "BUY", "NYSE", ComboLeg.OPEN);
    ComboLeg comboLeg2 = new ComboLeg(456, 1, "SELL", "NASDAQ", ComboLeg.CLOSE, 1, "LOC");
    assertEquals(false, comboLeg1.equals(comboLeg2));
  }
}