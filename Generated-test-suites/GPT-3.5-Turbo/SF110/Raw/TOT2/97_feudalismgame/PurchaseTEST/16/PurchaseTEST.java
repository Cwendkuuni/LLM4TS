import org.junit.Test;
import static org.junit.Assert.*;

public class PurchaseTest {

    @Test
    public void testPerform() {
        // Test basic functionality of perform method
    }

    @Test
    public void testVassal() {
        // Test typical use case where a vassal is bought successfully
        // Test edge case where buyer is a king
        // Test edge case where buyer already owns a fiefdom castle
        // Test error scenario where fiefdom does not belong to the kingdom
    }

    @Test
    public void testCastleFiefdom() {
        // Test typical use case where castle for fiefdom is bought successfully
        // Test edge case where buyer is a king and no vikings, magyars, or muslims in the fiefdom
        // Test error scenario where fiefdom does not belong to the kingdom
    }

    @Test
    public void testCastlePersonal() {
        // Test typical use case where personal castle is bought successfully
        // Test edge case where buyer is a king and fiefdom owner is a knight
        // Test error scenario where fiefdom does not belong to the kingdom
    }

    @Test
    public void testMercenary() {
        // Test typical use case where mercenaries are hired successfully
        // Test edge case where buyer is a king
        // Test error scenario where knight is non-existent
    }
}