
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ComboLegTest {

    @Test
    public void testEquals_SameObject() {
        ComboLeg leg1 = new ComboLeg();
        assertEquals(true, leg1.equals(leg1));
    }

    @Test
    public void testEquals_NullObject() {
        ComboLeg leg1 = new ComboLeg();
        assertEquals(false, leg1.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        ComboLeg leg1 = new ComboLeg();
        String other = "test";
        assertEquals(false, leg1.equals(other));
    }

    @Test
    public void testEquals_EqualComboLeg() {
        ComboLeg leg1 = new ComboLeg(1, 1, "action", "exchange", 1, 1, "location");
        ComboLeg leg2 = new ComboLeg(1, 1, "action", "exchange", 1, 1, "location");
        assertEquals(true, leg1.equals(leg2));
    }

    @Test
    public void testEquals_DifferentConId() {
        ComboLeg leg1 = new ComboLeg(1, 1, "action", "exchange", 1, 1, "location");
        ComboLeg leg2 = new ComboLeg(2, 1, "action", "exchange", 1, 1, "location");
        assertEquals(false, leg1.equals(leg2));
    }

    @Test
    public void testEquals_DifferentRatio() {
        ComboLeg leg1 = new ComboLeg(1, 1, "action", "exchange", 1, 1, "location");
        ComboLeg leg2 = new ComboLeg(1, 2, "action", "exchange", 1, 1, "location");
        assertEquals(false, leg1.equals(leg2));
    }

    @Test
    public void testEquals_DifferentOpenClose() {
        ComboLeg leg1 = new ComboLeg(1, 1, "action", "exchange", 1, 1, "location");
        ComboLeg leg2 = new ComboLeg(1, 1, "action", "exchange", 2, 1, "location");
        assertEquals(false, leg1.equals(leg2));
    }

    @Test
    public void testEquals_DifferentShortSaleSlot() {
        ComboLeg leg1 = new ComboLeg(1, 1, "action", "exchange", 1, 1, "location");
        ComboLeg leg2 = new ComboLeg(1, 1, "action", "exchange", 1, 2, "location");
        assertEquals(false, leg1.equals(leg2));
    }

    @Test
    public void testEquals_DifferentAction() {
        ComboLeg leg1 = new ComboLeg(1, 1, "action1", "exchange", 1, 1, "location");
        ComboLeg leg2 = new ComboLeg(1, 1, "action2", "exchange", 1, 1, "location");
        assertEquals(false, leg1.equals(leg2));
    }

    @Test
    public void testEquals_DifferentExchange() {
        ComboLeg leg1 = new ComboLeg(1, 1, "action", "exchange1", 1, 1, "location");
        ComboLeg leg2 = new ComboLeg(1, 1, "action", "exchange2", 1, 1, "location");
        assertEquals(false, leg1.equals(leg2));
    }

    @Test
    public void testEquals_DifferentDesignatedLocation() {
        ComboLeg leg1 = new ComboLeg(1, 1, "action", "exchange", 1, 1, "location1");
        ComboLeg leg2 = new ComboLeg(1, 1, "action", "exchange", 1, 1, "location2");
        assertEquals(false, leg1.equals(leg2));
    }
}
