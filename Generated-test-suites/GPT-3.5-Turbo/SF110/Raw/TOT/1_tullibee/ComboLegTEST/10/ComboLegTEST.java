import static org.junit.Assert.*;
import org.junit.Test;
import com.ib.client.ComboLeg;

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
    public void testParameterizedConstructor1() {
        int conId = 123;
        int ratio = 2;
        String action = "BUY";
        String exchange = "NYSE";
        int openClose = ComboLeg.OPEN;
        
        ComboLeg comboLeg = new ComboLeg(conId, ratio, action, exchange, openClose);
        
        assertEquals(conId, comboLeg.m_conId);
        assertEquals(ratio, comboLeg.m_ratio);
        assertEquals(action, comboLeg.m_action);
        assertEquals(exchange, comboLeg.m_exchange);
        assertEquals(openClose, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testParameterizedConstructor2() {
        int conId = 456;
        int ratio = 1;
        String action = "SELL";
        String exchange = "NASDAQ";
        int openClose = ComboLeg.CLOSE;
        int shortSaleSlot = 1;
        String designatedLocation = "LOC-123";
        
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
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(123, 2, "BUY", "NYSE", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(123, 2, "BUY", "NYSE", ComboLeg.OPEN);
        ComboLeg comboLeg3 = new ComboLeg(456, 1, "SELL", "NASDAQ", ComboLeg.CLOSE);
        
        assertTrue(comboLeg1.equals(comboLeg2));
        assertFalse(comboLeg1.equals(comboLeg3));
    }
}