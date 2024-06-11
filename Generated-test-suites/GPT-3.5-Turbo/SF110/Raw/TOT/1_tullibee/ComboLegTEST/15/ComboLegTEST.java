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
    public void testComboLegDefaultConstructor() {
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
        comboLeg = new ComboLeg(123, 2, "BUY", "NYSE", 1);
        assertEquals(123, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testComboLegFullConstructor() {
        comboLeg = new ComboLeg(456, 1, "SELL", "NASDAQ", 2, 9, "LOC");
        assertEquals(456, comboLeg.m_conId);
        assertEquals(1, comboLeg.m_ratio);
        assertEquals("SELL", comboLeg.m_action);
        assertEquals("NASDAQ", comboLeg.m_exchange);
        assertEquals(2, comboLeg.m_openClose);
        assertEquals(9, comboLeg.m_shortSaleSlot);
        assertEquals("LOC", comboLeg.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(123, 2, "BUY", "NYSE", 1, 0, null);
        ComboLeg comboLeg2 = new ComboLeg(123, 2, "BUY", "NYSE", 1, 0, null);
        assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentConId() {
        ComboLeg comboLeg1 = new ComboLeg(123, 2, "BUY", "NYSE", 1, 0, null);
        ComboLeg comboLeg2 = new ComboLeg(456, 2, "BUY", "NYSE", 1, 0, null);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentRatio() {
        ComboLeg comboLeg1 = new ComboLeg(123, 2, "BUY", "NYSE", 1, 0, null);
        ComboLeg comboLeg2 = new ComboLeg(123, 3, "BUY", "NYSE", 1, 0, null);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentAction() {
        ComboLeg comboLeg1 = new ComboLeg(123, 2, "BUY", "NYSE", 1, 0, null);
        ComboLeg comboLeg2 = new ComboLeg(123, 2, "SELL", "NYSE", 1, 0, null);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentExchange() {
        ComboLeg comboLeg1 = new ComboLeg(123, 2, "BUY", "NYSE", 1, 0, null);
        ComboLeg comboLeg2 = new ComboLeg(123, 2, "BUY", "NASDAQ", 1, 0, null);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentOpenClose() {
        ComboLeg comboLeg1 = new ComboLeg(123, 2, "BUY", "NYSE", 1, 0, null);
        ComboLeg comboLeg2 = new ComboLeg(123, 2, "BUY", "NYSE", 2, 0, null);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentShortSaleSlot() {
        ComboLeg comboLeg1 = new ComboLeg(123, 2, "BUY", "NYSE", 1, 0, null);
        ComboLeg comboLeg2 = new ComboLeg(123, 2, "BUY", "NYSE", 1, 1, null);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentDesignatedLocation() {
        ComboLeg comboLeg1 = new ComboLeg(123, 2, "BUY", "NYSE", 1, 0, null);
        ComboLeg comboLeg2 = new ComboLeg(123, 2, "BUY", "NYSE", 1, 0, "LOC");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

}