import org.junit.*;
import static org.junit.Assert.*;

public class ComboLegTest {

    ComboLeg comboLeg;

    @Before
    public void setUp() {
        comboLeg = new ComboLeg();
    }

    @Test
    public void testConstructor() {
        assertEquals(0, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testOverloadedConstructor() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testCompleteConstructor() {
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
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg comboLeg3 = new ComboLeg(2, 2, "BUY", "NYSE", 1);
        
        assertTrue(comboLeg1.equals(comboLeg2));
        assertFalse(comboLeg1.equals(comboLeg3));
    }

    @Test
    public void testEqualsSameInstance() {
        assertTrue(comboLeg.equals(comboLeg));
    }

    @Test
    public void testEqualsNull() {
        assertFalse(comboLeg.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        assertFalse(comboLeg.equals("ComboLeg"));
    }

    @After
    public void tearDown() {
        comboLeg = null;
    }
}