import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SimpleSectorTest {

    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, false, 5, 10, true, 20, new CharacterClass("Warrior"));
    }

    @Test
    public void testFree() {
        sector.gainControl(new CombatantSide("Attacker"));
        sector.free();
        assertEquals("Occupier should be null after freeing the sector", null, sector.getOccupier());
        assertEquals("Occupier count should be decreased after freeing the sector", 0, sector.getOccupierCount());
    }

    @Test
    public void testGainControl() {
        sector.gainControl(new CombatantSide("Defender"));
        assertEquals("Occupier should be updated after gaining control", "Defender", sector.getOccupier().getName());
        assertEquals("Occupier count should be increased after gaining control", 1, sector.getOccupierCount());
    }

    @Test
    public void testGetBonus() {
        SectorBonus bonus = sector.getBonus(new CharacterClass("Mage"));
        assertNotNull("Bonus should not be null", bonus);
    }

    // Add more test cases for other methods...

}