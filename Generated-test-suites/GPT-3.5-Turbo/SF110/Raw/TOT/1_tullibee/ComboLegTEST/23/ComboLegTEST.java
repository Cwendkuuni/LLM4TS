import static org.junit.Assert.*;
import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testConstructor1(){
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
    public void testConstructor2(){
        ComboLeg comboLeg = new ComboLeg(123, 1, "BUY", "NYSE", 1);
        assertEquals(123, comboLeg.m_conId);
        assertEquals(1, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testConstructor3(){
        ComboLeg comboLeg = new ComboLeg(456, 2, "SELL", "NASDAQ", 2, 3, "Location");
        assertEquals(456, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("SELL", comboLeg.m_action);
        assertEquals("NASDAQ", comboLeg.m_exchange);
        assertEquals(2, comboLeg.m_openClose);
        assertEquals(3, comboLeg.m_shortSaleSlot);
        assertEquals("Location", comboLeg.m_designatedLocation);
    }

    @Test
    public void testEquals_SameObject(){
        ComboLeg comboLeg1 = new ComboLeg(123, 1, "BUY", "NYSE", 1);
        assertTrue(comboLeg1.equals(comboLeg1));
    }

    @Test
    public void testEquals_NullObject(){
        ComboLeg comboLeg1 = new ComboLeg(123, 1, "BUY", "NYSE", 1);
        assertFalse(comboLeg1.equals(null));
    }

    @Test
    public void testEquals_DifferentClass(){
        ComboLeg comboLeg1 = new ComboLeg(123, 1, "BUY", "NYSE", 1);
        assertFalse(comboLeg1.equals("BUY"));
    }

    @Test
    public void testEquals_EqualComboLegs(){
        ComboLeg comboLeg1 = new ComboLeg(123, 1, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(123, 1, "BUY", "NYSE", 1);
        assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEquals_DifferentConId(){
        ComboLeg comboLeg1 = new ComboLeg(123, 1, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(456, 1, "BUY", "NYSE", 1);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEquals_DifferentRatio(){
        ComboLeg comboLeg1 = new ComboLeg(123, 1, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(123, 2, "BUY", "NYSE", 1);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEquals_DifferentAction(){
        ComboLeg comboLeg1 = new ComboLeg(123, 1, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(123, 1, "SELL", "NYSE", 1);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEquals_DifferentExchange(){
        ComboLeg comboLeg1 = new ComboLeg(123, 1, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(123, 1, "BUY", "NASDAQ", 1);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEquals_DifferentOpenClose(){
        ComboLeg comboLeg1 = new ComboLeg(123, 1, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(123, 1, "BUY", "NYSE", 2);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEquals_DifferentShortSaleSlot(){
        ComboLeg comboLeg1 = new ComboLeg(123, 1, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(123, 1, "BUY", "NYSE", 1, 2, null);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEquals_DifferentDesignatedLocation(){
        ComboLeg comboLeg1 = new ComboLeg(123, 1, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(123, 1, "BUY", "NYSE", 1, 0, "Location");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

}