
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleSectorTest {

    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    }

    @Test
    public void testFree() {
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
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
        assertTrue(sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
        assertNotNull(bonus);
        assertEquals(7, bonus.getAttackBonus());
        assertEquals(5, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testGetAreas() {
        assertTrue(sector.getAreas().isEmpty());
    }

    @Test
    public void test_addArea() {
        Area area = new Area();
        sector._addArea(area);
        assertTrue(sector.getAreas().contains(area));
    }

    @Test
    public void testHashCode() {
        // Test hash code based on all attributes of the sector
        int expectedHashCode = 31 * 1 + 10; // height
        expectedHashCode = 31 * expectedHashCode + (true ? 1231 : 1237); // defenderSpawn
        expectedHashCode = 31 * expectedHashCode + (false ? 1231 : 1237); // attackerSpawn
        expectedHashCode = 31 * expectedHashCode + 5; // attackBonus
        expectedHashCode = 31 * expectedHashCode + 3; // defendBonus
        expectedHashCode = 31 * expectedHashCode + (true ? 1231 : 1237); // flag
        expectedHashCode = 31 * expectedHashCode + 2; // classBonus
        expectedHashCode = 31 * expectedHashCode + CharacterClass.WARRIOR.hashCode(); // classType
        assertEquals(expectedHashCode, sector.hashCode());
    }

    @Test
    public void testEquals() {
        // Test equals based on all attributes of the sector
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertEquals(sector, other);
    }

    @Test
    public void testAddAndRemoveObserver() {
        DummyObserver observer = new DummyObserver();
        sector.addObserver(observer);
        assertEquals(1, sector.getObservers().size());
        assertTrue(sector.getObservers().contains(observer));
        sector.removeObserver(observer);
        assertTrue(sector.getObservers().isEmpty());
    }

    private static class DummyObserver implements SectorObserver {

        @Override
        public void occupierChanged() {
            // Do nothing
        }
    }
}
