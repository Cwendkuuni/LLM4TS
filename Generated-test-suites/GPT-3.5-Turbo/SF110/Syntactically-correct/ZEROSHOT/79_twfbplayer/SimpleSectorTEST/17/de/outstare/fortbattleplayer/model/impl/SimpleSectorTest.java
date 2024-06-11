import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleSectorTest {

    @Test
    public void testFree() {
        // Test case 1: occupierCount > 0
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.WARRIOR);
        sector.occupierCount = 1;
        sector.free();
        assertEquals(0, sector.occupierCount);
        assertNull(sector.getOccupier());

        // Test case 2: occupierCount = 0
        sector = new SimpleSector(5, false, true, 8, 3, false, 4, CharacterClass.MAGE);
        sector.free();
        assertEquals(0, sector.occupierCount);
        assertNull(sector.getOccupier());
    }

    @Test
    public void testGainControl() {
        SimpleSector sector = new SimpleSector(10, true, true, 7, 3, true, 1, CharacterClass.ARCHER);
        // Test case 1: occupier is different
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(1, sector.occupierCount);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());

        // Test case 2: occupier is same
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(2, sector.occupierCount);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testGetOccupier() {
        SimpleSector sector = new SimpleSector(15, false, false, 6, 2, false, 3, CharacterClass.WARRIOR);
        // Test case 1: occupier is not null
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());

        // Test case 2: occupier is null
        sector = new SimpleSector(20, true, true, 4, 1, true, 5, CharacterClass.MAGE);
        assertNull(sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        SimpleSector sector = new SimpleSector(25, false, true, 3, 1, false, 6, CharacterClass.WARRIOR);
        // Test case 1: occupier is not null
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector.isOccupied());

        // Test case 2: occupier is null
        sector.free();
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGetHeight() {
        SimpleSector sector = new SimpleSector(30, true, false, 2, 2, true, 7, CharacterClass.ARCHER);
        assertEquals(30, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        SimpleSector sector = new SimpleSector(35, true, true, 1, 1, true, 8, CharacterClass.MAGE);
        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        SimpleSector sector = new SimpleSector(40, false, true, 0, 0, false, 9, CharacterClass.WARRIOR);
        assertTrue(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        SimpleSector sector = new SimpleSector(45, true, true, 0, 0, true, 10, CharacterClass.MAGE);
        assertTrue(sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        SimpleSector sector = new SimpleSector(50, true, false, 1, 1, false, 11, CharacterClass.ARCHER);
        SectorBonus bonus = sector.getBonus(CharacterClass.MAGE);
        assertNotNull(bonus);
        assertEquals(1, bonus.getAttackBonus());
        assertEquals(1, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testGetAreas() {
        SimpleSector sector = new SimpleSector(55, false, true, 1, 1, true, 12, CharacterClass.WARRIOR);
        assertNotNull(sector.getAreas());
        assertEquals(0, sector.getAreas().size());
    }

    @Test
    public void test_addArea() {
        SimpleSector sector = new SimpleSector(60, true, false, 1, 1, true, 13, CharacterClass.MAGE);
        Area area = new Area();
        sector._addArea(area);
        assertTrue(sector.getAreas().contains(area));
    }

    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(1, true, true, 1, 1, true, 1, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(1, true, true, 1, 1, true, 1, CharacterClass.WARRIOR);
        SimpleSector sector3 = new SimpleSector(1, false, true, 1, 1, true, 1, CharacterClass.WARRIOR);

        assertEquals(sector1.hashCode(), sector2.hashCode());
        assertNotEquals(sector1.hashCode(), sector3.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(1, true, true, 1, 1, true, 1, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(1, true, true, 1, 1, true, 1, CharacterClass.WARRIOR);
        SimpleSector sector3 = new SimpleSector(1, false, true, 1, 1, true, 1, CharacterClass.WARRIOR);

        assertTrue(sector1.equals(sector2));
        assertFalse(sector1.equals(sector3));
    }

    @Test
    public void testAddObserver() {
        SimpleSector sector = new SimpleSector(1, true, true, 1, 1, true, 1, CharacterClass.WARRIOR);
        sector.addObserver(null); // observer can be null, no exception should be thrown
    }

    @Test
    public void testRemoveObserver() {
        SimpleSector sector = new SimpleSector(1, true, true, 1, 1, true, 1, CharacterClass.WARRIOR);
        sector.removeObserver(null); // observer can be null, no exception should be thrown
    }
}