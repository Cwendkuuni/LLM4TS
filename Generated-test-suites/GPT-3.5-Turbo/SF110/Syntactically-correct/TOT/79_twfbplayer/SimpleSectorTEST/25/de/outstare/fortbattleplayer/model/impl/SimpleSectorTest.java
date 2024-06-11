import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SimpleSectorTest {

    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    }

    @Test
    public void testFree() {
        // Typical use case
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertFalse(sector.isOccupied());

        // Error scenario when occupierCount is negative
        sector.free();
        assertFalse(sector.isOccupied());

        // Edge case when occupierCount is 0
        sector.occupierCount = 0;
        sector.free();
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGainControl() {
        // Typical use case
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());

        // Typical use case when already occupied by the same side
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());

        // Typical use case when already occupied by a different side
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testGetOccupier() {
        // Typical use case
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());

        // Typical use case when no occupier
        assertNull(sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        // Typical use case when occupied
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());

        // Typical use case when not occupied
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGetHeight() {
        // Typical use case
        assertEquals(10, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        // Typical use case
        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        // Typical use case
        assertFalse(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        // Typical use case
        assertTrue(sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        // Typical use case
        SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
        assertEquals(7, bonus.getAttackBonus());
        assertEquals(5, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testGetAreas() {
        // Typical use case
        assertEquals(0, sector.getAreas().size());
    }
}