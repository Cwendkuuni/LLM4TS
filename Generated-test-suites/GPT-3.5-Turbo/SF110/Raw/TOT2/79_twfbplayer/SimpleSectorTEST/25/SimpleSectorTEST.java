import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleSectorTest {

    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(10, true, false, 20, 15, true, 5, CharacterClass.WARRIOR);
        sector.gainControl(CombatantSide.DEFENDERS);
        sector.free();
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGainControl() {
        SimpleSector sector = new SimpleSector(10, true, false, 20, 15, true, 5, CharacterClass.WARRIOR);
        sector.gainControl(CombatantSide.ATTACKERS);
        assertEquals(1, sector.getOccupierCount());
    }
    
    @Test
    public void testIsOccupied() {
        SimpleSector sector = new SimpleSector(10, true, false, 20, 15, true, 5, CharacterClass.WARRIOR);
        assertFalse(sector.isOccupied());
    }

    // Additional test cases for other methods should be added here

}