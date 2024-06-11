import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class VassalRebellionTest {

    private VassalRebellion vassalRebellion;
    private Map<String, Fiefdoms> map;

    @Before
    public void setup() {
        vassalRebellion = new VassalRebellion();
        map = new HashMap<>();
        map.put("Fiefdom1", new Fiefdoms("Fiefdom1"));
        map.put("Fiefdom2", new Fiefdoms("Fiefdom2"));
    }

    @Test
    public void perform_LoyalVassalsExistAndValueIsEnoughToSetRebelliousVassals() {
        // Arrange
        Collection<String> col = Arrays.asList("Fiefdom1");
        map.get("Fiefdom1").addLoyalVassal("Vassal1");
        map.get("Fiefdom1").addLoyalVassal("Vassal2");
        map.get("Fiefdom1").setValue(3);
        int expectedRebelliousVassals = 3;
        boolean expectedKingdom = false;

        // Act
        vassalRebellion.perform(col);

        // Assert
        assertEquals(expectedRebelliousVassals, map.get("Fiefdom1").getRebelliousVassals());
        assertEquals(expectedKingdom, map.get("Fiefdom1").isKingdom());
    }

    @Test
    public void perform_LoyalVassalsExistButValueIsNotEnoughToSetRebelliousVassals() {
        // Arrange
        Collection<String> col = Arrays.asList("Fiefdom1");
        map.get("Fiefdom1").addLoyalVassal("Vassal1");
        map.get("Fiefdom1").setValue(2);
        int expectedRebelliousVassals = 1;
        boolean expectedKingdom = true;

        // Act
        vassalRebellion.perform(col);

        // Assert
        assertEquals(expectedRebelliousVassals, map.get("Fiefdom1").getRebelliousVassals());
        assertEquals(expectedKingdom, map.get("Fiefdom1").isKingdom());
    }

    @Test
    public void perform_LoyalVassalsDoNotExist() {
        // Arrange
        Collection<String> col = Arrays.asList("Fiefdom1");
        int expectedRebelliousVassals = 0;
        boolean expectedKingdom = true;

        // Act
        vassalRebellion.perform(col);

        // Assert
        assertEquals(expectedRebelliousVassals, map.get("Fiefdom1").getRebelliousVassals());
        assertEquals(expectedKingdom, map.get("Fiefdom1").isKingdom());
    }

    @Test
    public void perform_PersonalCastlesAndFiefdomCastlesExistAndValueIsEnoughToSetRebelliousVassals() {
        // Arrange
        Collection<String> col = Arrays.asList("Fiefdom1");
        map.get("Fiefdom1").addLoyalVassal("Vassal1");
        map.get("Fiefdom1").addLoyalVassal("Vassal2");
        map.get("Fiefdom1").setPersonalCastles(1);
        map.get("Fiefdom1").setFiefdomCastles(1);
        map.get("Fiefdom1").setValue(3);
        int expectedRebelliousVassals = 1;
        boolean expectedKingdom = true;

        // Act
        vassalRebellion.perform(col);

        // Assert
        assertEquals(expectedRebelliousVassals, map.get("Fiefdom1").getRebelliousVassals());
        assertEquals(expectedKingdom, map.get("Fiefdom1").isKingdom());
    }

    @Test
    public void perform_PersonalCastlesAndFiefdomCastlesExistButValueIsNotEnoughToSetRebelliousVassals() {
        // Arrange
        Collection<String> col = Arrays.asList("Fiefdom1");
        map.get("Fiefdom1").addLoyalVassal("Vassal1");
        map.get("Fiefdom1").setPersonalCastles(1);
        map.get("Fiefdom1").setFiefdomCastles(1);
        map.get("Fiefdom1").setValue(2);
        int expectedRebelliousVassals = 0;
        boolean expectedKingdom = true;

        // Act
        vassalRebellion.perform(col);

        // Assert
        assertEquals(expectedRebelliousVassals, map.get("Fiefdom1").getRebelliousVassals());
        assertEquals(expectedKingdom, map.get("Fiefdom1").isKingdom());
    }

    @Test
    public void perform_NoLoyalVassalsExist_ErrorScenario() {
        // Arrange
        Collection<String> col = Arrays.asList("Fiefdom1");
        int expectedRebelliousVassals = 0;
        boolean expectedKingdom = true;

        // Act
        vassalRebellion.perform(col);

        // Assert
        assertEquals(expectedRebelliousVassals, map.get("Fiefdom1").getRebelliousVassals());
        assertEquals(expectedKingdom, map.get("Fiefdom1").isKingdom());
    }

    @After
    public void tearDown() {
        vassalRebellion = null;
        map.clear();
    }
}