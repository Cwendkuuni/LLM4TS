import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SimpleSectorTest {

    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
    }

    @Test
    public void testFree() {
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGetHeight() {
        assertEquals(10, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        assertFalse(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        assertFalse(sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        assertNotNull(sector.getBonus(CharacterClass.WIZARD));
    }

    @Test
    public void testGetAreas() {
        assertNotNull(sector.getAreas());
    }

    @Test
    public void testEquals() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        assertTrue(sector.equals(other));
    }

    @Test
    public void testHashCode() {
        assertEquals(sector.hashCode(), sector.hashCode());
    }

}