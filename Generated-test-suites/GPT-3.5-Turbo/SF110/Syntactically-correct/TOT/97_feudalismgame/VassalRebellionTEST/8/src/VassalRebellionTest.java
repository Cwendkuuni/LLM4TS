import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class VassalRebellionTest {

    private static final String FIEFDOM_NAME = "Test Fiefdom";

    private VassalRebellion vassalRebellion;
    private Fiefdoms fiefdomObj;

    @Before
    public void setUp() {
        vassalRebellion = new VassalRebellion();
        fiefdomObj = new Fiefdoms(FIEFDOM_NAME);
    }

    @Test
    public void testPerformWithZeroLoyalVassals() {
        fiefdomObj.setLoyalVassals(new LinkedList<>());

        vassalRebellion.perform(fiefdomObj.getLoyalVassals());

        assertTrue(fiefdomObj.isKingdom());
        assertEquals(0, fiefdomObj.getRebelliousVassals());
    }

    @Test
    public void testPerformWithRebelliousVassalsEqualToValue() {
        List<String> loyalVassals = new LinkedList<>();
        loyalVassals.add("Vassal 1");
        loyalVassals.add("Vassal 2");
        loyalVassals.add("Vassal 3");
        fiefdomObj.setLoyalVassals(loyalVassals);
        fiefdomObj.setValue(3);

        vassalRebellion.perform(fiefdomObj.getLoyalVassals());

        assertFalse(fiefdomObj.isKingdom());
        assertEquals(3, fiefdomObj.getRebelliousVassals());
    }

    @Test
    public void testPerformWithRebelliousVassalsLessThanValue() {
        List<String> loyalVassals = new LinkedList<>();
        loyalVassals.add("Vassal 1");
        fiefdomObj.setLoyalVassals(loyalVassals);
        fiefdomObj.setValue(3);

        vassalRebellion.perform(fiefdomObj.getLoyalVassals());

        assertEquals(1, fiefdomObj.getRebelliousVassals());
    }

    @Test
    public void testPerformWithPositivePersonalAndFiefdomCastles() {
        List<String> loyalVassals = new LinkedList<>();
        loyalVassals.add("Vassal 1");
        loyalVassals.add("Vassal 2");
        loyalVassals.add("Vassal 3");
        fiefdomObj.setLoyalVassals(loyalVassals);
        fiefdomObj.setPersonalCastles(1);
        fiefdomObj.setFiefdomCastles(1);

        vassalRebellion.perform(fiefdomObj.getLoyalVassals());

        assertEquals(1, fiefdomObj.getRebelliousVassals());
    }

    @Test
    public void testPerformWithNegativeRebelliousVassalsAfterPersonalAndFiefdomCastles() {
        List<String> loyalVassals = new LinkedList<>();
        loyalVassals.add("Vassal 1");
        loyalVassals.add("Vassal 2");
        loyalVassals.add("Vassal 3");
        fiefdomObj.setLoyalVassals(loyalVassals);
        fiefdomObj.setPersonalCastles(1);
        fiefdomObj.setFiefdomCastles(3);

        vassalRebellion.perform(fiefdomObj.getLoyalVassals());

        assertEquals(0, fiefdomObj.getRebelliousVassals());
    }

}