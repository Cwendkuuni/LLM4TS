import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ComboLegTest {
    private ComboLeg comboLeg;

    @Before
    public void setUp() {
        comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1);
    }

    @Test
    public void testComboLegConstructor1() {
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testComboLegConstructor2() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "Location");
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(2, comboLeg.m_shortSaleSlot);
        assertEquals("Location", comboLeg.m_designatedLocation);
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
        assertFalse(comboLeg.equals(""));

    }

    @Test
    public void testEqualsEqualComboLeg() {
        ComboLeg equalComboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        assertTrue(comboLeg.equals(equalComboLeg));
    }

    @Test
    public void testEqualsDifferentConId() {
        ComboLeg differentConId = new ComboLeg(2, 2, "BUY", "NYSE", 1);
        assertFalse(comboLeg.equals(differentConId));
    }

    @Test
    public void testEqualsDifferentRatio() {
        ComboLeg differentRatio = new ComboLeg(1, 3, "BUY", "NYSE", 1);
        assertFalse(comboLeg.equals(differentRatio));
    }

    @Test
    public void testEqualsDifferentAction() {
        ComboLeg differentAction = new ComboLeg(1, 2, "SELL", "NYSE", 1);
        assertFalse(comboLeg.equals(differentAction));
    }

    @Test
    public void testEqualsDifferentExchange() {
        ComboLeg differentExchange = new ComboLeg(1, 2, "BUY", "NASDAQ", 1);
        assertFalse(comboLeg.equals(differentExchange));
    }

    @Test
    public void testEqualsDifferentOpenClose() {
        ComboLeg differentOpenClose = new ComboLeg(1, 2, "BUY", "NYSE", 2);
        assertFalse(comboLeg.equals(differentOpenClose));
    }

    @Test
    public void testEqualsDifferentShortSaleSlot() {
        ComboLeg differentShortSaleSlot = new ComboLeg(1, 2, "BUY", "NYSE", 1, 1, null);
        assertFalse(comboLeg.equals(differentShortSaleSlot));
    }

    @Test
    public void testEqualsDifferentDesignatedLocation() {
        ComboLeg differentDesignatedLocation = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "Location");
        assertFalse(comboLeg.equals(differentDesignatedLocation));
    }
}