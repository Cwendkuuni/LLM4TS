import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleSectorTest {

    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        sector.occupierCount = 1;
        sector.occupier = CombatantSide.DEFENDER;
        sector.free();

        assertEquals(0, sector.occupierCount);
        assertNull(sector.occupier);
    }

    @Test
    public void testGainControl() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        sector.occupierCount = 0;

        sector.gainControl(CombatantSide.ATTACKER);
        sector.gainControl(CombatantSide.DEFENDER);

        assertEquals(2, sector.occupierCount);
        assertEquals(CombatantSide.DEFENDER, sector.occupier);
    }

    @Test
    public void testGetOccupier() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        sector.occupier = CombatantSide.ATTACKER;

        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        sector.occupier = CombatantSide.DEFENDER;

        assertTrue(sector.isOccupied());
    }

    @Test
    public void testGetHeight() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);

        assertEquals(10, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);

        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);

        assertFalse(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);

        assertTrue(sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);

        SectorBonus expected = new SectorBonus(8, 5, 0);

        assertEquals(expected, bonus);
    }

    @Test
    public void testGetAttackBonus() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        int attackBonus = sector.getAttackBonus(CharacterClass.WARRIOR);

        assertEquals(8, attackBonus);
    }

    @Test
    public void testGetDefenseBonus() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        int defenseBonus = sector.getDefenseBonus(CharacterClass.WARRIOR);

        assertEquals(5, defenseBonus);
    }

    @Test
    public void testClassBonus() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        int classBonus = sector.classBonus(CharacterClass.WARRIOR);

        assertEquals(3, classBonus);
    }

    @Test
    public void testGetAreas() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        Set<Area> areas = sector.getAreas();

        assertEquals(0, areas.size());
    }

    @Test
    public void testAddArea() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        Area area = new Area();

        sector._addArea(area);
        Set<Area> areas = sector.getAreas();

        assertEquals(1, areas.size());
        assertTrue(areas.contains(area));
    }

    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);

        assertEquals(sector1.hashCode(), sector2.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        SimpleSector sector3 = new SimpleSector(20, false, true, 6, 2, false, 4, CharacterClass.WARRIOR);

        assertTrue(sector1.equals(sector2));
        assertFalse(sector1.equals(sector3));
    }

    @Test
    public void testAddObserver() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        SectorObserver observer = new SectorObserverImpl();

        sector.addObserver(observer);

        assertTrue(sector.observers.contains(observer));
    }

    @Test
    public void testRemoveObserver() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        SectorObserver observer = new SectorObserverImpl();
        sector.observers.add(observer);

        sector.removeObserver(observer);

        assertFalse(sector.observers.contains(observer));
    }

    private class SectorObserverImpl implements SectorObserver {
        @Override
        public void occupierChanged() {

        }
    }
}