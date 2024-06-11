import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.Area;
import de.outstare.fortbattleplayer.model.impl.CharacterClass;
import de.outstare.fortbattleplayer.model.impl.SectorObserver;
import de.outstare.fortbattleplayer.model.impl.Weapon;

public class SimpleSectorTest {
    
    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, false, 3, 2, true, 1, CharacterClass.WARRIOR);
    }
    
    @Test
    public void testFreeOccupiedSector() {
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testFreeSectorWithZeroOccupierCount() {
        sector.free();
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testFreeSectorObserverCall() {
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        sector.free();
        verify(observer).occupierChanged();
    }

    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testGetOccupierWhenNotOccupied() {
        assertEquals(null, sector.getOccupier());
    }

    @Test
    public void testIsOccupiedWhenNotOccupied() {
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
        assertEquals(new SectorBonus(4, 3, 0), sector.getBonus(CharacterClass.WARRIOR));
    }

    @Test
    public void testGetBonusWithDifferentClass() {
        assertEquals(new SectorBonus(3, 2, 0), sector.getBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetAreas() {
        Set<Area> areas = new HashSet<>();
        Area area = new Area();
        areas.add(area);
        sector._addArea(area);

        assertEquals(areas, sector.getAreas());
    }

    @Test
    public void testHashCode() {
        SimpleSector other = new SimpleSector(10, true, false, 3, 2, true, 1, CharacterClass.WARRIOR);
        assertEquals(sector.hashCode(), other.hashCode());
    }

    @Test
    public void testEqualsWithEqualSectors() {
        SimpleSector other = new SimpleSector(10, true, false, 3, 2, true, 1, CharacterClass.WARRIOR);
        assertTrue(sector.equals(other));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        SimpleSector other = new SimpleSector(10, true, false, 3, 2, true, 1, CharacterClass.ARCHER);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEqualsWithDifferentHeight() {
        SimpleSector other = new SimpleSector(5, true, false, 3, 2, true, 1, CharacterClass.WARRIOR);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEqualsWithDifferentArea() {
        Set<Area> areas = new HashSet<>();
        Area area = new Area();
        areas.add(area);
        SimpleSector other = new SimpleSector(10, true, false, 3, 2, true, 1, CharacterClass.WARRIOR);
        other._addArea(area);

        assertFalse(sector.equals(other));
    }

    @Test
    public void testAddObserver() {
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        verify(observer).occupierChanged();
    }

    @Test
    public void testRemoveObserver() {
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        sector.removeObserver(observer);
        verify(observer).occupierChanged();
    }

}