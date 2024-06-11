import org.junit.Test;
import static org.junit.Assert.*;

import com.ib.client.ComboLeg;

public class ComboLegTest {

    @Test
    public void testComboLegDefaultConstructor() {
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
    public void testComboLegPartialConstructor() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE);
        
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(ComboLeg.CLOSE, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testComboLegFullConstructor() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "SELL", "NASDAQ", ComboLeg.OPEN, 5, "Location");
        
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("SELL", comboLeg.m_action);
        assertEquals("NASDAQ", comboLeg.m_exchange);
        assertEquals(ComboLeg.OPEN, comboLeg.m_openClose);
        assertEquals(5, comboLeg.m_shortSaleSlot);
        assertEquals("Location", comboLeg.m_designatedLocation);
    }

    @Test
    public void testEqualsWithSameObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE);
        
        assertTrue(comboLeg.equals(comboLeg));
    }

    @Test
    public void testEqualsWithNullObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE);
        
        assertFalse(comboLeg.equals(null));
    }

    @Test
    public void testEqualsWithDifferentObject() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE);
        ComboLeg comboLeg2 = new ComboLeg(2, 2, "BUY", "NYSE", ComboLeg.CLOSE);
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithEqualObjects() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE);
        
        assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentConId() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE);
        ComboLeg comboLeg2 = new ComboLeg(2, 2, "BUY", "NYSE", ComboLeg.CLOSE);
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentRatio() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE);
        ComboLeg comboLeg2 = new ComboLeg(1, 3, "BUY", "NYSE", ComboLeg.CLOSE);
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentAction() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "SELL", "NYSE", ComboLeg.CLOSE);
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentExchange() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NASDAQ", ComboLeg.CLOSE);
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentOpenClose() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentShortSaleSlot() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE, 5, "Location");
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentDesignatedLocation() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE, 0, "Location");
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithMatchingDesignatedLocationIgnoringCase() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE, 0, "Location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE, 0, "LOCATION");
        
        assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithNullDesignatedLocation() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE, 0, "Location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE, 0, null);
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }
}