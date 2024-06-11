import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class SimpleSectorTest {

    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(5, true, false, 10, 5, true, 3, CharacterClass.WARRIOR);
    }

    @Test
    public void testFree() {
        // Initial occupier count
        assertEquals(0, sector.occupierCount);
        assertNull(sector.occupier);

        // Free without combatant
        sector.free();
        assertEquals(0, sector.occupierCount);
        assertNull(sector.occupier);

        // Add a combatant
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(1, sector.occupierCount);
        assertEquals(CombatantSide.DEFENDER, sector.occupier);

        // Free with combatant
        sector.free();
        assertEquals(0, sector.occupierCount);
        assertNull(sector.occupier);
    }

    @Test
    public void testGainControl() {
        // Initial occupier count
        assertEquals(0, sector.occupierCount);
        assertNull(sector.occupier);

        // Gain control with attacker
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(1, sector.occupierCount);
        assertEquals(CombatantSide.ATTACKER, sector.occupier);

        // Gain control with defender
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(2, sector.occupierCount);
        assertEquals(CombatantSide.DEFENDER, sector.occupier);
    }

    @Test
    public void testGetOccupier() {
        // Initial occupier
        assertNull(sector.getOccupier());

        // Gain control with attacker
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());

        // Gain control with defender
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        // Sector is not occupied initially
        assertFalse(sector.isOccupied());

        // Gain control with attacker
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector.isOccupied());

        // Free the sector
        sector.free();
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGetHeight() {
        assertEquals(5, sector.getHeight());
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
        assertTrue(sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
        assertEquals(13, bonus.getAttackBonus());
        assertEquals(8, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());

        bonus = sector.getBonus(CharacterClass.MAGE);
        assertEquals(3, bonus.getAttackBonus());
        assertEquals(8, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testGetAreas() {
        Set<Area> areas = sector.getAreas();
        assertTrue(areas.isEmpty());

        Area area = new Area();
        sector._addArea(area);
        areas = sector.getAreas();
        assertFalse(areas.isEmpty());
        assertTrue(areas.contains(area));
    }

    @Test
    public void testHashCode() {
        int hashCode = sector.hashCode();
        assertEquals(759199831, hashCode);
    }

    @Test
    public void testEquals() {
        SimpleSector sameSector = new SimpleSector(5, true, false, 10, 5, true, 3, CharacterClass.WARRIOR);
        assertEquals(sector, sameSector);

        SimpleSector differentSector = new SimpleSector(10, false, true, 5, 10, false, 2, CharacterClass.ARCHER);
        assertNotEquals(sector, differentSector);
    }

    @Test
    public void testAddObserver() {
        SectorObserver observer = new SectorObserver();
        sector.addObserver(observer);
        assertTrue(sector.observers.contains(observer));
    }

    @Test
    public void testRemoveObserver() {
        SectorObserver observer = new SectorObserver();
        sector.addObserver(observer);
        assertTrue(sector.observers.contains(observer));

        sector.removeObserver(observer);
        assertFalse(sector.observers.contains(observer));
    }
}