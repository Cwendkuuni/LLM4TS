
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.SectorObserver;
import de.outstare.fortbattleplayer.model.Weapon;
import de.outstare.fortbattleplayer.model.CharacterClass;

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
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void testGetOccupier() {
        assertNull(sector.getOccupier());
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        assertFalse(sector.isOccupied());
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
        int expectedAttackBonus = 5 + 2 + sector.getBonusFromWeapons(SectorBonus.BonusType.ATTACK);
        int expectedDefenseBonus = 3 + 2 + sector.getBonusFromWeapons(SectorBonus.BonusType.DEFENSE);
        SectorBonus expectedBonus = new SectorBonus(expectedAttackBonus, expectedDefenseBonus, sector.getDamageBonus());
        assertEquals(expectedBonus, sector.getBonus(CharacterClass.WARRIOR));
    }

    @Test
    public void testGetAreas() {
        Set<Area> areas = new HashSet<>();
        Area area1 = mock(Area.class);
        areas.add(area1);
        Area area2 = mock(Area.class);
        areas.add(area2);

        sector._addArea(area1);
        sector._addArea(area2);

        assertEquals(areas, sector.getAreas());
    }

    @Test
    public void test_addArea() {
        Set<Area> areas = new HashSet<>();
        Area area = mock(Area.class);
        areas.add(area);

        sector._addArea(area);

        assertEquals(areas, sector.getAreas());
    }

    @Test
    public void testHashCode() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);

        assertEquals(other.hashCode(), sector.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);

        assertTrue(sector.equals(other));
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

        verify(observer, times(1)).occupierChanged();
    }
}
