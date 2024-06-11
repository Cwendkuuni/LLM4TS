import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ComboLegTest {
    
    private ComboLeg comboLeg;
    
    @Before
    public void setUp() {
        comboLeg = new ComboLeg();
    }
    
    @After
    public void tearDown() {
        comboLeg = null;
    }
    
    // Test case for default constructor
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
    
    // Test case for constructor with p_conId, p_ratio, p_action, p_exchange, and p_openClose parameters
    @Test
    public void testComboLegConstructorWithParams1() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NASDAQ", 1);
        
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NASDAQ", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }
    
    // Test case for constructor with all parameters
    @Test
    public void testComboLegConstructorWithParams2() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "SELL", "NYSE", 2, 1, "LOC1");
        
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("SELL", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(2, comboLeg.m_openClose);
        assertEquals(1, comboLeg.m_shortSaleSlot);
        assertEquals("LOC1", comboLeg.m_designatedLocation);
    }
    
    // Test case for equals method
    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1);
        
        assertTrue(comboLeg1.equals(comboLeg2));
    }
    
    // Test case for equals method with different object
    @Test
    public void testEqualsWithDifferentObject() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1);
        String str = "I am a string";
        
        assertFalse(comboLeg1.equals(str));
    }
    
    // Test case for equals method with null object
    @Test
    public void testEqualsWithNull() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1);
        
        assertFalse(comboLeg1.equals(null));
    }
    
    // Test case for equals method with different conId
    @Test
    public void testEqualsWithDifferentConId() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1);
        ComboLeg comboLeg2 = new ComboLeg(2, 2, "BUY", "NASDAQ", 1);
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    // Test case for equals method with different ratio
    @Test
    public void testEqualsWithDifferentRatio() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 3, "BUY", "NASDAQ", 1);
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    // Test case for equals method with different action
    @Test
    public void testEqualsWithDifferentAction() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "SELL", "NASDAQ", 1);
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    // Test case for equals method with different exchange
    @Test
    public void testEqualsWithDifferentExchange() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    // Test case for equals method with different openClose
    @Test
    public void testEqualsWithDifferentOpenClose() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NASDAQ", 2);
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    // Test case for equals method with different shortSaleSlot
    @Test
    public void testEqualsWithDifferentShortSaleSlot() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1, 1, "LOC1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1, 2, "LOC1");
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    // Test case for equals method with different designatedLocation
    @Test
    public void testEqualsWithDifferentDesignatedLocation() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1, 1, "LOC1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1, 1, "LOC2");
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }
}