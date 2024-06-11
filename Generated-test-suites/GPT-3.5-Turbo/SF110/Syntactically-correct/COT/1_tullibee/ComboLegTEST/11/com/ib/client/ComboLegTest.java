import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {

    // Test constructor with no parameters
    @Test
    public void testConstructor1() {
        ComboLeg comboLeg = new ComboLeg();
        assertEquals(0, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    // Test constructor with 5 parameters
    @Test
    public void testConstructor2() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }
    
    // Test constructor with 7 parameters
    @Test
    public void testConstructor3() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "Location");
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertEquals("Location", comboLeg.m_designatedLocation);
    }

    // Test equals method for equal objects
    @Test
    public void testEqualsEqualObjects() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "Location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "Location");
        assertTrue(comboLeg1.equals(comboLeg2));
    }
    
    // Test equals method for different objects
    @Test
    public void testEqualsDifferentObjects() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "Location");
        ComboLeg comboLeg2 = new ComboLeg(2, 2, "BUY", "NYSE", 1, 0, "Location");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

}