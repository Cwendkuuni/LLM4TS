import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.impl.Area;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.Weapon;

public class SimpleSectorTest {

    @Mock
    private Area area;
    @Mock
    private Weapon weapon;
    @Mock
    private CombatantSide combatant;

    private SimpleSector sector;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    }

    @Test
    public void testFree_ZeroOccupiers_OccupierChangedIsNotCalled() {
        sector.free();

        verify(area, never()).getOccupier();
        verify(area, never()).isOccupied();
    }

    @Test
    public void testFree_Occupiers_OccupierChangedIsCalled() {
        when(area.isOccupied()).thenReturn(true);
        when(area.getOccupier()).thenReturn(combatant);
        when(combatant.getWeapon()).thenReturn(weapon);
        when(weapon.getSectorBonus(SectorBonus.BonusType.DAMAGE)).thenReturn(5);

        sector._addArea(area);
        sector.free();

        verify(combatant, times(1)).getWeapon();
        verify(weapon, times(1)).getSectorBonus(SectorBonus.BonusType.DAMAGE);
        verify(area, times(1)).getOccupier();
        verify(area, times(1)).isOccupied();
    }

    @Test
    public void testGainControl_DifferentSide_OccupierChangedIsCalled() {
        CombatantSide side = CombatantSide.ATTACKER;

        sector.gainControl(side);

        verify(area, never()).getOccupier();
        verify(area, never()).isOccupied();
    }

    @Test
    public void testGainControl_SameSide_OccupierChangedIsNotCalled() {
        when(combatant.getWeapon()).thenReturn(weapon);
        when(weapon.getSectorBonus(SectorBonus.BonusType.ATTACK)).thenReturn(5);
        
        CombatantSide side = CombatantSide.DEFENDER;

        sector._addArea(area);
        sector.gainControl(side);

        verify(combatant, never()).getWeapon();
        verify(weapon, never()).getSectorBonus(SectorBonus.BonusType.ATTACK);
        verify(area, times(1)).getOccupier();
        verify(area, times(1)).isOccupied();
    }

    @Test
    public void testGetOccupier_NoOccupier_ReturnsNull() {
        CombatantSide expected = null;

        CombatantSide actual = sector.getOccupier();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetOccupier_WithOccupier_ReturnsOccupier() {
        when(area.isOccupied()).thenReturn(true);
        when(area.getOccupier()).thenReturn(combatant);

        sector._addArea(area);

        CombatantSide expected = combatant;
        CombatantSide actual = sector.getOccupier();

        assertEquals(expected, actual);
    }

    @Test
    public void testIsOccupied_NoOccupier_ReturnsFalse() {
        boolean expected = false;

        boolean actual = sector.isOccupied();

        assertEquals(expected, actual);
    }

    @Test
    public void testIsOccupied_WithOccupier_ReturnsTrue() {
        when(area.isOccupied()).thenReturn(true);

        sector._addArea(area);

        boolean expected = true;
        boolean actual = sector.isOccupied();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetHeight_ReturnsHeight() {
        int expected = 10;

        int actual = sector.getHeight();

        assertEquals(expected, actual);
    }

    @Test
    public void testIsDefenderSpawn_ReturnsDefenderSpawn() {
        boolean expected = true;

        boolean actual = sector.isDefenderSpawn();

        assertEquals(expected, actual);
    }

    @Test
    public void testIsAttackerSpawn_ReturnsAttackerSpawn() {
        boolean expected = false;

        boolean actual = sector.isAttackerSpawn();

        assertEquals(expected, actual);
    }

    @Test
    public void testIsFlag_ReturnsFlag() {
        boolean expected = true;

        boolean actual = sector.isFlag();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetBonus_ReturnsSectorBonus() {
        when(weapon.getSectorBonus(SectorBonus.BonusType.ATTACK)).thenReturn(5);
        when(weapon.getSectorBonus(SectorBonus.BonusType.DEFENSE)).thenReturn(3);
        when(weapon.getSectorBonus(SectorBonus.BonusType.DAMAGE)).thenReturn(4);

        when(area.isOccupied()).thenReturn(true);
        when(area.getOccupier()).thenReturn(combatant);
        when(combatant.getWeapon()).thenReturn(weapon);

        sector._addArea(area);

        SectorBonus expected = new SectorBonus(7, 5, 4);
        SectorBonus actual = sector.getBonus(CharacterClass.WARRIOR);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetAttackBonus_ReturnsAttackBonus() {
        when(weapon.getSectorBonus(SectorBonus.BonusType.ATTACK)).thenReturn(5);

        when(area.isOccupied()).thenReturn(true);
        when(area.getOccupier()).thenReturn(combatant);
        when(combatant.getWeapon()).thenReturn(weapon);

        sector._addArea(area);

        int expected = 12;
        int actual = sector.getAttackBonus(CharacterClass.WARRIOR);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetDefenseBonus_ReturnsDefenseBonus() {
        when(weapon.getSectorBonus(SectorBonus.BonusType.DEFENSE)).thenReturn(3);

        when(area.isOccupied()).thenReturn(true);
        when(area.getOccupier()).thenReturn(combatant);
        when(combatant.getWeapon()).thenReturn(weapon);

        sector._addArea(area);

        int expected = 8;
        int actual = sector.getDefenseBonus(CharacterClass.WARRIOR);

        assertEquals(expected, actual);
    }

    @Test
    public void testClassBonus_BonusClassMatch_ReturnsClassBonus() {
        int expected = 2;
        int actual = sector.classBonus(CharacterClass.WARRIOR);

        assertEquals(expected, actual);
    }

    @Test
    public void testClassBonus_BonusClassDoesNotMatch_ReturnsZeroBonus() {
        int expected = 0;
        int actual = sector.classBonus(CharacterClass.MAGE);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetArea_ReturnsAreas() {
        Set<Area> expected = new HashSet<>();
        expected.add(area);

        sector._addArea(area);
        Set<Area> actual = sector.getAreas();

        assertEquals(expected, actual);
    }

    @Test
    public void test_addArea() {
        Set<Area> expected = new HashSet<>();
        expected.add(area);

        sector._addArea(area);
        Set<Area> actual = sector.getAreas();

        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode_SameSectors_ReturnsSameHashCode() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);

        int expected = sector.hashCode();
        int actual = other.hashCode();

        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode_DifferentSectors_ReturnsDifferentHashCode() {
        SimpleSector other = new SimpleSector(15, false, true, 8, 6, false, 4, CharacterClass.MAGE);

        int expected = sector.hashCode();
        int actual = other.hashCode();

        assertFalse(expected == actual);
    }

    @Test
    public void testEquals_SameSector_ReturnsTrue() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);

        assertTrue(sector.equals(other));
    }

    @Test
    public void testEquals_DifferentSector_ReturnsFalse() {
        SimpleSector other = new SimpleSector(15, false, true, 8, 6, false, 4, CharacterClass.MAGE);

        assertFalse(sector.equals(other));
    }

    @Test
    public void testAddObserver_AddsObserver() {
        SectorObserver observer = mock(SectorObserver.class);
        int originalSize = sector.observers.size();

        sector.addObserver(observer);

        int newSize = sector.observers.size();

        assertTrue(newSize > originalSize);
        assertTrue(sector.observers.contains(observer));
    }

    @Test
    public void testRemoveObserver_RemovesObserver() {
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        int originalSize = sector.observers.size();

        sector.removeObserver(observer);

        int newSize = sector.observers.size();

        assertTrue(newSize < originalSize);
        assertFalse(sector.observers.contains(observer));
    }

}