import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.SectorObserver;
import de.outstare.fortbattleplayer.model.Weapon;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;

public class SimpleSectorTest {

    private SimpleSector sector;
    private Set<Area> areas;
    private Area area1;
    private Area area2;
    private SectorObserver observer;

    @Before
    public void setUp() {
        areas = new HashSet<>();
        area1 = new Area();
        area2 = new Area();
        areas.add(area1);
        areas.add(area2);
        observer = new MockSectorObserver();
        sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        sector._addArea(area1);
        sector._addArea(area2);
        sector.addObserver(observer);
    }

    @Test
    public void testFree() {
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
    }

    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(1, sector.getOccupierCount());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(2, sector.getOccupierCount());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        assertFalse(sector.isOccupied());
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector.isOccupied());
        sector.gainControl(CombatantSide.DEFENDER);
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
        assertEquals(9, bonus.getAttackBonus());
        assertEquals(5, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testGetBonus_InvalidClass() {
        SectorBonus bonus = sector.getBonus(CharacterClass.ARCHER);
        assertNotNull(bonus);
        assertEquals(0, bonus.getAttackBonus());
        assertEquals(0, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testGetAreas() {
        Set<Area> expectedAreas = new HashSet<>();
        expectedAreas.add(area1);
        expectedAreas.add(area2);
        
        assertEquals(expectedAreas, sector.getAreas());
    }

    @Test
    public void testHashCode() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        other._addArea(area1);
        other._addArea(area2);
        
        assertEquals(sector.hashCode(), other.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        other._addArea(area1);
        other._addArea(area2);
        
        assertEquals(sector, other);
    }

    @Test
    public void testAddObserver() {
        assertEquals(1, ((MockSectorObserver) observer).getObserverCount());
    }

    @Test
    public void testRemoveObserver() {
        sector.removeObserver(observer);
        assertEquals(0, ((MockSectorObserver) observer).getObserverCount());
    }
    
    private static class MockSectorObserver implements SectorObserver {
        
        private int observerCount;
        
        @Override
        public void occupierChanged() {
            // do nothing
        }

        public int getObserverCount() {
            return observerCount;
        }
        
    }
}
