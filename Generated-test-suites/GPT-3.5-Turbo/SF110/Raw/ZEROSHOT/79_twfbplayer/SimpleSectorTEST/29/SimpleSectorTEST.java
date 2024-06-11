package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;
import de.outstare.fortbattleplayer.model.*;

public class SimpleSectorTest {
    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    }

    @Test
    public void testFree_OccupierCountGreaterThanZero_DecreasesOccupierCountByOne() {
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertEquals(0, sector.getOccupierCount());
    }

    @Test
    public void testFree_OccupierCountEqualsZero_OccupierIsNull() {
        sector.free();
        assertNull(sector.getOccupier());
    }

    @Test
    public void testFree_OccupierCountEqualsZero_OccupierChangedCalled() {
        MockSectorObserver observer = new MockSectorObserver();
        sector.addObserver(observer);
        sector.free();
        assertTrue(observer.occupierChangedCalled);
    }

    @Test
    public void testGainControl_OccupierCountIncremented() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(1, sector.getOccupierCount());
    }

    @Test
    public void testGainControl_OccupierDifferentThanSide_OccupierChangedCalled() {
        MockSectorObserver observer = new MockSectorObserver();
        sector.addObserver(observer);
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(observer.occupierChangedCalled);
    }

    @Test
    public void testGetOccupier_NoOccupier_OccupierIsNull() {
        assertNull(sector.getOccupier());
    }

    @Test
    public void testGetOccupier_HasOccupier_OccupierIsNotNull() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertNotNull(sector.getOccupier());
    }

    @Test
    public void testIsOccupied_NoOccupier_ReturnsFalse() {
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testIsOccupied_HasOccupier_ReturnsTrue() {
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testGetHeight_ReturnsHeight() {
        assertEquals(10, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn_DefenderSpawnIsTrue_ReturnsTrue() {
        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsDefenderSpawn_DefenderSpawnIsFalse_ReturnsFalse() {
        SimpleSector sector = new SimpleSector(10, false, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn_AttackerSpawnIsTrue_ReturnsTrue() {
        assertTrue(sector.isAttackerSpawn());
    }

    @Test
    public void testIsAttackerSpawn_AttackerSpawnIsFalse_ReturnsFalse() {
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag_FlagIsTrue_ReturnsTrue() {
        assertTrue(sector.isFlag());
    }

    @Test
    public void testIsFlag_FlagIsFalse_ReturnsFalse() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        assertFalse(sector.isFlag());
    }

    @Test
    public void testGetBonus_ReturnsExpectedSectorBonus() {
        CharacterClass charClass = CharacterClass.WARRIOR;
        SectorBonus expectedBonus = new SectorBonus(7, 5, 0);
        assertEquals(expectedBonus, sector.getBonus(charClass));
    }

    @Test
    public void testGetDamageBonus_ReturnsExpectedDamageBonus() {
        int expectedDamageBonus = 0;
        assertEquals(expectedDamageBonus, sector.getDamageBonus());
    }

    @Test
    public void testGetBonusFromWeapons_ReturnsExpectedBonus() {
        int expectedBonus = 0;
        SectorBonus.BonusType type = SectorBonus.BonusType.DAMAGE;
        assertEquals(expectedBonus, sector.getBonusFromWeapons(type));
    }

    @Test
    public void testGetAttackBonus_ReturnsExpectedAttackBonus() {
        CharacterClass charClass = CharacterClass.WARRIOR;
        int expectedAttackBonus = 7;
        assertEquals(expectedAttackBonus, sector.getAttackBonus(charClass));
    }

    @Test
    public void testGetDefenseBonus_ReturnsExpectedDefenseBonus() {
        CharacterClass charClass = CharacterClass.WARRIOR;
        int expectedDefenseBonus = 5;
        assertEquals(expectedDefenseBonus, sector.getDefenseBonus(charClass));
    }

    @Test
    public void testClassBonus_ClassTypeIsNotCharClass_ReturnsZero() {
        CharacterClass charClass = CharacterClass.ARCHER;
        int expectedBonus = 0;
        assertEquals(expectedBonus, sector.classBonus(charClass));
    }

    @Test
    public void testClassBonus_ClassTypeIsCharClass_ReturnsClassBonus() {
        CharacterClass charClass = CharacterClass.WARRIOR;
        int expectedBonus = 2;
        assertEquals(expectedBonus, sector.classBonus(charClass));
    }

    @Test
    public void testGetAreas_ReturnsAreas() {
        Set<Area> expectedAreas = new HashSet<>();
        assertEquals(expectedAreas, sector.getAreas());
    }

    @Test
    public void test_addArea_AreaAddedToAreas() {
        Area area = new Area();
        sector._addArea(area);
        assertTrue(sector.getAreas().contains(area));
    }

    @Test
    public void testHashCode_ReturnsExpectedHashCode() {
        int expectedHashCode = 212;
        assertEquals(expectedHashCode, sector.hashCode());
    }

    @Test
    public void testEquals_ObjIsSameInstance_ReturnsTrue() {
        assertTrue(sector.equals(sector));
    }

    @Test
    public void testEquals_ObjIsNull_ReturnsFalse() {
        assertFalse(sector.equals(null));
    }

    @Test
    public void testEquals_ObjIsDifferentClass_ReturnsFalse() {
        assertFalse(sector.equals(new Object()));
    }

    @Test
    public void testEquals_ObjHasDifferentAreas_ReturnsFalse() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEquals_ObjHasDifferentAttackBonus_ReturnsFalse() {
        SimpleSector other = new SimpleSector(10, true, false, 10, 3, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEquals_ObjHasDifferentAttackerSpawn_ReturnsFalse() {
        SimpleSector other = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEquals_ObjHasDifferentClassBonus_ReturnsFalse() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 5, CharacterClass.WARRIOR);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEquals_ObjHasDifferentClassType_ReturnsFalse() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.ARCHER);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEquals_ObjHasDifferentDefendBonus_ReturnsFalse() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 8, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEquals_ObjHasDifferentDefenderSpawn_ReturnsFalse() {
        SimpleSector other = new SimpleSector(10, false, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEquals_ObjHasDifferentFlag_ReturnsFalse() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEquals_ObjHasDifferentHeight_ReturnsFalse() {
        SimpleSector other = new SimpleSector(15, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEquals_ObjEquals_ReturnsTrue() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertTrue(sector.equals(other));
    }

    @Test
    public void testAddObserver_ObserverAddedToObservers() {
        SectorObserver observer = new MockSectorObserver();
        sector.addObserver(observer);
        assertTrue(sector.getObservers().contains(observer));
    }

    @Test
    public void testRemoveObserver_ObserverRemovedFromObservers() {
        SectorObserver observer = new MockSectorObserver();
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertFalse(sector.getObservers().contains(observer));
    }
    
    private static class MockSectorObserver implements SectorObserver {
        private boolean occupierChangedCalled = false;

        @Override
        public void occupierChanged() {
            occupierChangedCalled = true;
        }
    }
}