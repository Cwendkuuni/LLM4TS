import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class SimpleSectorTest {

    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    }

    @Test
    public void testFree() {
        // Test when occupierCount > 0
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertFalse(sector.isOccupied());

        // Test when occupierCount == 0
        sector.free();
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGainControl() {
        map.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void testGetOccupier() {
        assertNull(sector.getOccupier());
        map.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        assertFalse(sector.isOccupied());
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector.isOccupied());
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
        Set<Area> areas = sector.getAreas();
        assertNotNull(areas);
        assertTrue(areas.isEmpty());

        Area area = new Area();
        sector._addArea(area);

        areas = sector.getAreas();
        assertNotNull(areas);
        assertFalse(areas.isEmpty());
        assertEquals(1, areas.size());
        assertTrue(areas.contains(area));
    }

    @Test
    public void testAddObserver() {
        SectorObserver observer = new SectorObserverImpl();
        sector.addObserver(observer);

        Set<SectorObserver> observers = getObservers(sector);
        assertNotNull(observers);
        assertFalse(observers.isEmpty());
        assertEquals(1, observers.size());
        assertTrue(observers.contains(observer));
    }

    @Test
    public void testRemoveObserver() {
        SectorObserver observer1 = new SectorObserverImpl();
        SectorObserver observer2 = new SectorObserverImpl();
        sector.addObserver(observer1);
        sector.addObserver(observer2);

        Set<SectorObserver> observersBeforeRemoval = getObservers(sector);
        assertNotNull(observersBeforeRemoval);
        assertFalse(observersBeforeRemoval.isEmpty());
        assertEquals(2, observersBeforeRemoval.size());
        assertTrue(observersBeforeRemoval.contains(observer1));
        assertTrue(observersBeforeRemoval.contains(observer2));

        sector.removeObserver(observer1);

        Set<SectorObserver> observersAfterRemoval = getObservers(sector);
        assertNotNull(observersAfterRemoval);
        assertFalse(observersAfterRemoval.isEmpty());
        assertEquals(1, observersAfterRemoval.size());
        assertFalse(observersAfterRemoval.contains(observer1));
        assertTrue(observersAfterRemoval.contains(observer2));
    }

    private Set<SectorObserver> getObservers(SimpleSector sector) {
        try {
            Field observersField = sector.getClass().getDeclaredField("observers");
            observersField.setAccessible(true);
            Set<SectorObserver> observers = (Set<SectorObserver>) observersField.get(sector);
            return observers;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class SectorObserverImpl implements SectorObserver {
        @Override
        public void occupierChanged() {
            // do something
        }
    }
}