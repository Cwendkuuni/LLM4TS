import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {
    
    @Test
    public void testDefaultConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        assertEquals(0, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testConstructorWithParameters() {
        int conId = 1;
        int ratio = 2;
        String action = "BUY";
        String exchange = "NYSE";
        int openClose = 1;
        int shortSaleSlot = 0;
        String designatedLocation = "LOC";
        
        ComboLeg comboLeg = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);
        assertEquals(conId, comboLeg.m_conId);
        assertEquals(ratio, comboLeg.m_ratio);
        assertEquals(action, comboLeg.m_action);
        assertEquals(exchange, comboLeg.m_exchange);
        assertEquals(openClose, comboLeg.m_openClose);
        assertEquals(shortSaleSlot, comboLeg.m_shortSaleSlot);
        assertEquals(designatedLocation, comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testEquals_sameObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "LOC");
        assertTrue(comboLeg.equals(comboLeg));
    }
    
    @Test
    public void testEquals_nullObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "LOC");
        assertFalse(comboLeg.equals(null));
    }
    
    @Test
    public void testEquals_differentClass() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "LOC");
        assertFalse(comboLeg.equals("not a ComboLeg object"));
    }
    
    @Test
    public void testEquals_equalComboLegs() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "LOC");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "LOC");
        assertTrue(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEquals_differentConId() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "LOC");
        ComboLeg comboLeg2 = new ComboLeg(2, 2, "BUY", "NYSE", 1, 0, "LOC");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEquals_differentRatio() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "LOC");
        ComboLeg comboLeg2 = new ComboLeg(1, 3, "BUY", "NYSE", 1, 0, "LOC");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEquals_differentAction() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "LOC");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "SELL", "NYSE", 1, 0, "LOC");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEquals_differentExchange() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "LOC");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1, 0, "LOC");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEquals_differentOpenClose() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "LOC");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 2, 0, "LOC");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEquals_differentShortSaleSlot() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "LOC");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 1, "LOC");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEquals_differentDesignatedLocation() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "LOC");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "DL");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
}