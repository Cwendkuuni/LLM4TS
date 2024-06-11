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
    }

    @Test
    public void testEquals_SameObject_ReturnsTrue() {
        assertTrue(comboLeg.equals(comboLeg));
    }

    @Test
    public void testEquals_NullObject_ReturnsFalse() {
        assertFalse(comboLeg.equals(null));
    }

    @Test
    public void testEquals_DifferentClass_ReturnsFalse() {
        assertFalse(comboLeg.equals(new Object()));
    }

    @Test
    public void testEquals_EqualComboLeg_ReturnsTrue() {
        ComboLeg other = new ComboLeg(0, 0, null, null, 0, 0, null);
        assertTrue(comboLeg.equals(other));
    }

    @Test
    public void testEquals_DifferentConId_ReturnsFalse() {
        ComboLeg other = new ComboLeg(1, 0, null, null, 0, 0, null);
        assertFalse(comboLeg.equals(other));
    }

    @Test
    public void testEquals_DifferentRatio_ReturnsFalse() {
        ComboLeg other = new ComboLeg(0, 1, null, null, 0, 0, null);
        assertFalse(comboLeg.equals(other));
    }

    @Test
    public void testEquals_DifferentAction_ReturnsFalse() {
        ComboLeg other = new ComboLeg(0, 0, "BUY", null, 0, 0, null);
        assertFalse(comboLeg.equals(other));
    }

    @Test
    public void testEquals_DifferentExchange_ReturnsFalse() {
        ComboLeg other = new ComboLeg(0, 0, null, "NYSE", 0, 0, null);
        assertFalse(comboLeg.equals(other));
    }

    @Test
    public void testEquals_DifferentOpenClose_ReturnsFalse() {
        ComboLeg other = new ComboLeg(0, 0, null, null, 1, 0, null);
        assertFalse(comboLeg.equals(other));
    }

    @Test
    public void testEquals_DifferentShortSaleSlot_ReturnsFalse() {
        ComboLeg other = new ComboLeg(0, 0, null, null, 0, 1, null);
        assertFalse(comboLeg.equals(other));
    }

    @Test
    public void testEquals_DifferentDesignatedLocation_ReturnsFalse() {
        ComboLeg other = new ComboLeg(0, 0, null, null, 0, 0, "USA");
        assertFalse(comboLeg.equals(other));
    }

    @Test
    public void testEquals_ComboLegWithNullFields_ReturnsFalse() {
        ComboLeg other = new ComboLeg();
        assertFalse(comboLeg.equals(other));
    }

}