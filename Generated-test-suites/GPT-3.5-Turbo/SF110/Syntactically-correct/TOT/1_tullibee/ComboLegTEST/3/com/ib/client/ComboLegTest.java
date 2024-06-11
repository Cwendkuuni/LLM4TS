import static org.junit.Assert.*;
import org.junit.Test;
import com.ib.client.ComboLeg;

public class ComboLegTest {
    
    @Test
    public void testComboLegConstructor1(){
        ComboLeg comboLeg = new ComboLeg();
        
        // Verify default values are set correctly
        assertEquals(0, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testComboLegConstructor2(){
        int conId = 1;
        int ratio = 2;
        String action = "BUY";
        String exchange = "NYSE";
        int openClose = ComboLeg.CLOSE;
        
        ComboLeg comboLeg = new ComboLeg(conId, ratio, action, exchange, openClose);
        
        // Verify values are set correctly
        assertEquals(conId, comboLeg.m_conId);
        assertEquals(ratio, comboLeg.m_ratio);
        assertEquals(action, comboLeg.m_action);
        assertEquals(exchange, comboLeg.m_exchange);
        assertEquals(openClose, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testComboLegConstructor3(){
        int conId = 1;
        int ratio = 2;
        String action = "SELL";
        String exchange = "NASDAQ";
        int openClose = ComboLeg.OPEN;
        int shortSaleSlot = 1;
        String designatedLocation = "Floor";
        
        ComboLeg comboLeg = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);
        
        // Verify values are set correctly
        assertEquals(conId, comboLeg.m_conId);
        assertEquals(ratio, comboLeg.m_ratio);
        assertEquals(action, comboLeg.m_action);
        assertEquals(exchange, comboLeg.m_exchange);
        assertEquals(openClose, comboLeg.m_openClose);
        assertEquals(shortSaleSlot, comboLeg.m_shortSaleSlot);
        assertEquals(designatedLocation, comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testEqualsTrue(){
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        
        assertTrue(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsFalseDifferentConId(){
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        ComboLeg leg2 = new ComboLeg(2, 2, "BUY", "NYSE", ComboLeg.OPEN);
        
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsFalseDifferentRatio(){
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        ComboLeg leg2 = new ComboLeg(1, 3, "BUY", "NYSE", ComboLeg.OPEN);
        
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsFalseDifferentAction(){
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        ComboLeg leg2 = new ComboLeg(1, 2, "SELL", "NYSE", ComboLeg.OPEN);
        
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsFalseDifferentExchange(){
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NASDAQ", ComboLeg.OPEN);
        
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsFalseDifferentOpenClose(){
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE);
        
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsFalseDifferentShortSaleSlot(){
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 1, "Floor");
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 2, "Floor");
        
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsFalseDifferentDesignatedLocation(){
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 1, "Floor");
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 1, "Desk");
        
        assertFalse(leg1.equals(leg2));
    }
    
}