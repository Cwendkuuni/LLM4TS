import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleSectorTest {

    private SimpleSector sector;

    @Test
    public void testFree() {
        // Test freeing when empty
        sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        
        // Test freeing with occupants
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertEquals(1, sector.getOccupierCount());
    }
    
    @Test
    public void testGainControl() {
        // Test gaining control
        sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }
    
    @Test
    public void testIsOccupied() {
        // Test occupied and unoccupied
        sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector.isOccupied());
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
    }
    
    // More test cases for each method can be added here
    
}