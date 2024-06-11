import org.junit.*;

public class ComboLegTest {
    // Test cases for ComboLeg constructor with no parameters
    @Test
    public void testComboLegConstructorNoParams() {
        ComboLeg comboLeg = new ComboLeg();
        Assert.assertEquals(0, comboLeg.m_conId);
        Assert.assertEquals(0, comboLeg.m_ratio);
        Assert.assertNull(comboLeg.m_action);
        Assert.assertNull(comboLeg.m_exchange);
        Assert.assertEquals(0, comboLeg.m_openClose);
        Assert.assertEquals(0, comboLeg.m_shortSaleSlot);
        Assert.assertNull(comboLeg.m_designatedLocation);
    }
    
    // Test cases for ComboLeg constructor with p_conId, p_ratio, p_action, p_exchange, and p_openClose parameters
    @Test
    public void testComboLegConstructorWithParams() {
        ComboLeg comboLeg = new ComboLeg(123, 2, "BUY", "NYSE", 1);
        Assert.assertEquals(123, comboLeg.m_conId);
        Assert.assertEquals(2, comboLeg.m_ratio);
        Assert.assertEquals("BUY", comboLeg.m_action);
        Assert.assertEquals("NYSE", comboLeg.m_exchange);
        Assert.assertEquals(1, comboLeg.m_openClose);
        Assert.assertEquals(0, comboLeg.m_shortSaleSlot);
        Assert.assertNull(comboLeg.m_designatedLocation);
    }
    
    // Test cases for ComboLeg constructor with all parameters
    @Test
    public void testComboLegConstructorWithAllParams() {
        ComboLeg comboLeg = new ComboLeg(456, 3, "SELL", "NASDAQ", 2, 3, "Location");
        Assert.assertEquals(456, comboLeg.m_conId);
        Assert.assertEquals(3, comboLeg.m_ratio);
        Assert.assertEquals("SELL", comboLeg.m_action);
        Assert.assertEquals("NASDAQ", comboLeg.m_exchange);
        Assert.assertEquals(2, comboLeg.m_openClose);
        Assert.assertEquals(3, comboLeg.m_shortSaleSlot);
        Assert.assertEquals("Location", comboLeg.m_designatedLocation);
    }
    
    // Test case for equals method
    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(123, 2, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(123, 2, "BUY", "NYSE", 1);
        
        Assert.assertTrue(comboLeg1.equals(comboLeg2));
    }
}