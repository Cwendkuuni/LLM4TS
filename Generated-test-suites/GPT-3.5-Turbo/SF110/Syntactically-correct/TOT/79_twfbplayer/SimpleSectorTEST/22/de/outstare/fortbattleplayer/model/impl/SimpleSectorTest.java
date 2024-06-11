import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Set;
import java.util.HashSet;

public class SimpleSectorTest {

    private SimpleSector sector;

    @Before
    public void setup() {
        sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    }

    @Test
    public void testFree() {
        // Typical Case: OccupierCount is greater than zero, so decrements occupierCount and sets occupier null
        sector.occupier = CombatantSide.DEFENDER;
        sector.occupierCount = 1;
        sector.free();
        assertEquals(0, sector.occupierCount);
        assertNull(sector.occupier);

        // Edge Case: OccupierCount is already zero, so no change
        sector.free();
        assertEquals(0, sector.occupierCount);
        assertNull(sector.occupier);

        // Error Scenario: Negative occupierCount, should not occur in normal flow
        sector.occupierCount = -1;
        sector.free();
        assertEquals(-1, sector.occupierCount);
        assertNull(sector.occupier);
    }

    @Test
    public void testGainControl() {
        // Typical Case: Increment occupierCount and change occupier
        sector.occupierCount = 0;
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(1, sector.occupierCount);
        assertEquals(CombatantSide.ATTACKER, sector.occupier);

        // Edge Case: Already occupied by the same side, no change
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(1, sector.occupierCount);
        assertEquals(CombatantSide.ATTACKER, sector.occupier);
    }

    @Test
    public void testGetOccupier() {
        // Typical Case: Occupier is set to CombatantSide.DEFENDER
        sector.occupier = CombatantSide.DEFENDER;
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());

        // Edge Case: Occupier is null, should return null
        sector.occupier = null;
        assertNull(sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        // Typical Case: Occupier is set to CombatantSide.ATTACKER, should return true
        sector.occupier = CombatantSide.ATTACKER;
        assertTrue(sector.isOccupied());

        // Edge Case: Occupier is null, should return false
        sector.occupier = null;
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGetHeight() {
        // Typical Case: Height is set to 10, should return 10
        assertEquals(10, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        // Typical Case: defenderSpawn is set to true, should return true
        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        // Typical Case: attackerSpawn is set to false, should return false
        assertFalse(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        // Typical Case: flag is set to true, should return true
        assertTrue(sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        // Typical Case: charClass is of type WARRIOR, should return a SectorBonus object with correct attack and defense values
        SectorBonus expectedBonus = new SectorBonus(5, 3, 0);
        assertEquals(expectedBonus, sector.getBonus(CharacterClass.WARRIOR));

        // Edge Case: charClass is of type ARCHER, should return a SectorBonus object with correct attack and defense values
        expectedBonus = new SectorBonus(5, 3, 0);
        assertEquals(expectedBonus, sector.getBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetAreas() {
        // Typical Case: areas set is not empty, should return the set of areas
        Set<Area> expectedAreas = new HashSet<Area>();
        assertEquals(expectedAreas, sector.getAreas());

        // Edge Case: areas set is empty, should return an empty set
        sector._addArea(new Area());
        sector._addArea(new Area());
        expectedAreas.add(new Area());
        expectedAreas.add(new Area());
        assertEquals(expectedAreas, sector.getAreas());
    }

    @Test
    public void test_addArea() {
        // Typical Case: Adds an area to the areas set, should contain the added area
        Area area = new Area();
        sector._addArea(area);
        assertTrue(sector.getAreas().contains(area));
    }

    @Test
    public void testHashCode() {
        // Typical Case: Same parameters, should have same hash code
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertEquals(other.hashCode(), sector.hashCode());

        // Edge Case: Different height, should have different hash code
        other = new SimpleSector(15, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertNotEquals(other.hashCode(), sector.hashCode());

        // Edge Case: Different defenderSpawn, should have different hash code
        other = new SimpleSector(10, false, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertNotEquals(other.hashCode(), sector.hashCode());

        // Edge Case: Different attackerSpawn, should have different hash code
        other = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertNotEquals(other.hashCode(), sector.hashCode());

        // Edge Case: Different attackBonus, should have different hash code
        other = new SimpleSector(10, true, false, 6, 3, true, 2, CharacterClass.WARRIOR);
        assertNotEquals(other.hashCode(), sector.hashCode());

        // Edge Case: Different defenseBonus, should have different hash code
        other = new SimpleSector(10, true, false, 5, 4, true, 2, CharacterClass.WARRIOR);
        assertNotEquals(other.hashCode(), sector.hashCode());

        // Edge Case: Different flag, should have different hash code
        other = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        assertNotEquals(other.hashCode(), sector.hashCode());

        // Edge Case: Different classBonus, should have different hash code
        other = new SimpleSector(10, true, false, 5, 3, true, 3, CharacterClass.WARRIOR);
        assertNotEquals(other.hashCode(), sector.hashCode());

        // Edge Case: Different classType, should have different hash code
        other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.ARCHER);
        assertNotEquals(other.hashCode(), sector.hashCode());
    }

    @Test
    public void testEquals() {
        // Typical Case: Same parameters, should be equal
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertTrue(sector.equals(other));

        // Edge Case: Different height, should not be equal
        other = new SimpleSector(15, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector.equals(other));

        // Edge Case: Different defenderSpawn, should not be equal
        other = new SimpleSector(10, false, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector.equals(other));

        // Edge Case: Different attackerSpawn, should not be equal
        other = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector.equals(other));

        // Edge Case: Different attackBonus, should not be equal
        other = new SimpleSector(10, true, false, 6, 3, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector.equals(other));

        // Edge Case: Different defenseBonus, should not be equal
        other = new SimpleSector(10, true, false, 5, 4, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector.equals(other));

        // Edge Case: Different flag, should not be equal
        other = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        assertFalse(sector.equals(other));

        // Edge Case: Different classBonus, should not be equal
        other = new SimpleSector(10, true, false, 5, 3, true, 3, CharacterClass.WARRIOR);
        assertFalse(sector.equals(other));

        // Edge Case: Different classType, should not be equal
        other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.ARCHER);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testAddObserver() {
        // Typical Case: Adds an observer to the observers set, should contain the added observer
        SectorObserver observer = new SectorObserver();
        sector.addObserver(observer);
        assertTrue(sector.observers.contains(observer));
    }

    @Test
    public void testRemoveObserver() {
        // Typical Case: Removes an observer from the observers set, should not contain the removed observer
        SectorObserver observer = new SectorObserver();
        sector.observers.add(observer);
        sector.removeObserver(observer);
        assertFalse(sector.observers.contains(observer));
    }

}