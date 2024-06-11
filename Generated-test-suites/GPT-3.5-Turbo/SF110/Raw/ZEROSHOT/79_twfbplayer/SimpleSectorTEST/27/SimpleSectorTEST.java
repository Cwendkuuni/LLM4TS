import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class SimpleSectorTest {

    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGainControl() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testGetOccupier() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testGetHeight() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertEquals(10, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertTrue(sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
        assertNotNull(bonus);
        assertEquals(5, bonus.getAttackBonus());
        assertEquals(3, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testGetAttackBonus() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertEquals(7, sector.getAttackBonus(CharacterClass.WARRIOR));
    }

    @Test
    public void testGetDefenseBonus() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertEquals(5, sector.getDefenseBonus(CharacterClass.WARRIOR));
    }

    @Test
    public void testGetAreas() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        Set<Area> areas = sector.getAreas();
        assertNotNull(areas);
        assertTrue(areas.isEmpty());
    }

    @Test
    public void test_addArea() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        Area area = new Area();
        sector._addArea(area);
        Set<Area> areas = sector.getAreas();
        assertNotNull(areas);
        assertEquals(1, areas.size());
        assertTrue(areas.contains(area));
    }

    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertEquals(sector1.hashCode(), sector2.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertEquals(sector1, sector2);
    }

    @Test
    public void testAddObserver() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        SectorObserver observer = new SectorObserver() {
            @Override
            public void occupierChanged() {
                // do nothing
            }
        };
        sector.addObserver(observer);
        Set<SectorObserver> observers = sector.getObservers();
        assertNotNull(observers);
        assertEquals(1, observers.size());
        assertTrue(observers.contains(observer));
    }

    @Test
    public void testRemoveObserver() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        SectorObserver observer = new SectorObserver() {
            @Override
            public void occupierChanged() {
                // do nothing
            }
        };
        sector.addObserver(observer);
        sector.removeObserver(observer);
        Set<SectorObserver> observers = sector.getObservers();
        assertNotNull(observers);
        assertTrue(observers.isEmpty());
    }
}