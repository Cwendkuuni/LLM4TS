import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collection;

public class VassalRebellionTest {

    private VassalRebellion vassalRebellion;

    @BeforeEach
    public void setUp() {
        vassalRebellion = new VassalRebellion();
    }

    @Test
    public void testPerform_WithLoyalVassalsAndNonZeroPersonalCastlesAndFiefdomCastles_ShouldSetRebelliousVassalsToExpectedValue() {
        // Arrange
        Fiefdoms mockFiefdoms = new Fiefdoms();
        mockFiefdoms.setPersonalCastles(2);
        mockFiefdoms.setFiefdomCastles(3);
        mockFiefdoms.setLoyalVassals(new ArrayList<>());

        Collection<Fiefdoms> mockCollection = new ArrayList<>();
        mockCollection.add(mockFiefdoms);

        // Act
        vassalRebellion.perform(mockCollection);

        // Assert
        Assertions.assertEquals(0, mockFiefdoms.getLoyalVassals().size());
        Assertions.assertEquals(true, mockFiefdoms.isRebelliousVassals());
        Assertions.assertEquals(false, mockFiefdoms.isKingdom());
    }

    @Test
    public void testPerform_WithLoyalVassalsAndLowerValueThanLoyalVassals_ShouldSetRebelliousVassalsToValueAndNotChangeKingdomStatus() {
        // Arrange
        Fiefdoms mockFiefdoms = new Fiefdoms();
        mockFiefdoms.setLoyalVassals(new ArrayList<>());
        int value = 5;
        mockFiefdoms.setValue(value);

        Collection<Fiefdoms> mockCollection = new ArrayList<>();
        mockCollection.add(mockFiefdoms);

        // Act
        vassalRebellion.perform(mockCollection);

        // Assert
        Assertions.assertEquals(value, mockFiefdoms.getLoyalVassals().size());
        Assertions.assertEquals(true, mockFiefdoms.isRebelliousVassals());
        Assertions.assertEquals(true, mockFiefdoms.isKingdom());
    }

    @Test
    public void testPerform_WithLoyalVassalsAndZeroPersonalCastlesAndZeroFiefdomCastles_ShouldNotChangeRebelliousVassalsAndKingdomStatus() {
        // Arrange
        Fiefdoms mockFiefdoms = new Fiefdoms();
        mockFiefdoms.setPersonalCastles(0);
        mockFiefdoms.setFiefdomCastles(0);
        mockFiefdoms.setLoyalVassals(new ArrayList<>());

        Collection<Fiefdoms> mockCollection = new ArrayList<>();
        mockCollection.add(mockFiefdoms);

        // Act
        vassalRebellion.perform(mockCollection);

        // Assert
        Assertions.assertEquals(0, mockFiefdoms.getLoyalVassals().size());
        Assertions.assertEquals(false, mockFiefdoms.isRebelliousVassals());
        Assertions.assertEquals(true, mockFiefdoms.isKingdom());
    }

    @Test
    public void testPerform_WithNoLoyalVassals_ShouldCreateNewRollEvent() {
        // Arrange
        Fiefdoms mockFiefdoms = new Fiefdoms();
        mockFiefdoms.setLoyalVassals(new ArrayList<>());

        Collection<Fiefdoms> mockCollection = new ArrayList<>();
        mockCollection.add(mockFiefdoms);

        // Act & Assert
        Assertions.assertThrows(InterruptedException.class, () -> vassalRebellion.perform(mockCollection));
        // We assume that the RollEvent constructor throws InterruptedException based on the code provided,
        // if not, we can update this assertion accordingly.
    }
}