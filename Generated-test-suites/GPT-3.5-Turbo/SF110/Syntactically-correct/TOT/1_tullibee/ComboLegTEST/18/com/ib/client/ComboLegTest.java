
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {
    
    private ComboLeg comboLeg;
    
    @Before
    public void setUp() {
        comboLeg = new ComboLeg();
    }
    
    @Test
    public void testDefaultConstructor() {
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
        ComboLeg comboLeg1 = new ComboLeg(123, 1, "BUY", "NYSE", 1);
        assertEquals(123, comboLeg1.m_conId);
        assertEquals(1, comboLeg1.m_ratio);
        assertEquals("BUY", comboLeg1.m_action);
        assertEquals("NYSE", comboLeg1.m_exchange);
        assertEquals(1, comboLeg1.m_openClose);
        assertEquals(0, comboLeg1.m_shortSaleSlot);
        assertNull(comboLeg1.m_designatedLocation);
    }
    
    @Test
    public void testParameterizedConstructor2() {
        ComboLeg comboLeg2 = new ComboLeg(456, 2, "SELL", "NASDAQ", 2, 1, "Location");
        assertEquals(456, comboLeg2.m_conId);
        assertEquals(2, comboLeg2.m_ratio);
        assertEquals("SELL", comboLeg2.m_action);
        assertEquals("NASDAQ", comboLeg2.m_exchange);
        assertEquals(2, comboLeg2.m_openClose);
        assertEquals(1, comboLeg2.m_shortSaleSlot);
        assertEquals("Location", comboLeg2.m_designatedLocation);
    }
    
    @Test
    public void testEqualsSameObject() {
        assertTrue(comboLeg.equals(comboLeg));
    }
    
    @Test
    public void testEqualsNullObject() {
        assertFalse(comboLeg.equals(null));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        assertFalse(comboLeg.equals("someString"));
    }
    
    @Test
    public void testEqualsEqualComboLeg() {
        ComboLeg comboLeg1 = new ComboLeg(123, 1, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(123, 1, "BUY", "NYSE", 1);
        assertTrue(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsDifferentComboLeg() {
        ComboLeg comboLeg1 = new ComboLeg(123, 1, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(456, 2, "SELL", "NASDAQ", 2, 1, "Location");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
}
