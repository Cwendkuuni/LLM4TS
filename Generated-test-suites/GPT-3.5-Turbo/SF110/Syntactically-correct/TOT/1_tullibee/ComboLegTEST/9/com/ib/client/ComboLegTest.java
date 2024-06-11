import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ComboLegTest {
    
    private ComboLeg comboLeg;
    
    @Before
    public void setUp() {
        comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 3, "LOC");
    }
    
    @Test
    public void testComboLegConstructorDefault() {
        ComboLeg defaultComboLeg = new ComboLeg();
        assertEquals(0, defaultComboLeg.m_conId);
        assertEquals(0, defaultComboLeg.m_ratio);
        assertEquals(null, defaultComboLeg.m_action);
        assertEquals(null, defaultComboLeg.m_exchange);
        assertEquals(0, defaultComboLeg.m_openClose);
        assertEquals(0, defaultComboLeg.m_shortSaleSlot);
        assertEquals(null, defaultComboLeg.m_designatedLocation);
    }
    
    @Test
    public void testComboLegConstructorBasic() {
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(ComboLeg.OPEN, comboLeg.m_openClose);
        assertEquals(3, comboLeg.m_shortSaleSlot);
        assertEquals("LOC", comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testComboLegConstructorAllParams() {
        ComboLeg comboLegAllParams = new ComboLeg(4, 5, "SELL", "NASDAQ", ComboLeg.CLOSE, 6, "LOC2");
        assertEquals(4, comboLegAllParams.m_conId);
        assertEquals(5, comboLegAllParams.m_ratio);
        assertEquals("SELL", comboLegAllParams.m_action);
        assertEquals("NASDAQ", comboLegAllParams.m_exchange);
        assertEquals(ComboLeg.CLOSE, comboLegAllParams.m_openClose);
        assertEquals(6, comboLegAllParams.m_shortSaleSlot);
        assertEquals("LOC2", comboLegAllParams.m_designatedLocation);
    }
    
    @Test
    public void testEqualsNullObject() {
        assertFalse(comboLeg.equals(null));
    }
    
    @Test
    public void testEqualsSameObject() {
        assertTrue(comboLeg.equals(comboLeg));
    }
    
    @Test
    public void testEqualsDifferentClassObject() {
        Object obj = new Object();
        assertFalse(comboLeg.equals(obj));
    }
    
    @Test
    public void testEqualsEqualComboLeg() {
        ComboLeg sameComboLeg = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 3, "LOC");
        assertTrue(comboLeg.equals(sameComboLeg));
    }
    
    @Test
    public void testEqualsDifferentComboLeg() {
        ComboLeg differentComboLeg = new ComboLeg(1, 2, "SELL", "NYSE", ComboLeg.OPEN, 3, "LOC");
        assertFalse(comboLeg.equals(differentComboLeg));
    }
    
    @Test
    public void testEqualsDifferentConId() {
        ComboLeg differentConId = new ComboLeg(2, 2, "BUY", "NYSE", ComboLeg.OPEN, 3, "LOC");
        assertFalse(comboLeg.equals(differentConId));
    }
    
    @Test
    public void testEqualsDifferentRatio() {
        ComboLeg differentRatio = new ComboLeg(1, 3, "BUY", "NYSE", ComboLeg.OPEN, 3, "LOC");
        assertFalse(comboLeg.equals(differentRatio));
    }
    
    @Test
    public void testEqualsDifferentAction() {
        ComboLeg differentAction = new ComboLeg(1, 2, "SELL", "NYSE", ComboLeg.OPEN, 3, "LOC");
        assertFalse(comboLeg.equals(differentAction));
    }
    
    @Test
    public void testEqualsDifferentExchange() {
        ComboLeg differentExchange = new ComboLeg(1, 2, "BUY", "NASDAQ", ComboLeg.OPEN, 3, "LOC");
        assertFalse(comboLeg.equals(differentExchange));
    }
    
    @Test
    public void testEqualsDifferentOpenClose() {
        ComboLeg differentOpenClose = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE, 3, "LOC");
        assertFalse(comboLeg.equals(differentOpenClose));
    }
    
    @Test
    public void testEqualsDifferentShortSaleSlot() {
        ComboLeg differentShortSaleSlot = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 4, "LOC");
        assertFalse(comboLeg.equals(differentShortSaleSlot));
    }
    
    @Test
    public void testEqualsDifferentDesignatedLocation() {
        ComboLeg differentDesignatedLocation = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 3, "LOC2");
        assertFalse(comboLeg.equals(differentDesignatedLocation));
    }

}