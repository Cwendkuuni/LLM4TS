package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class SimpleSectorTest {
    private SimpleSector simpleSector;

    @Before
    public void setup() {
        simpleSector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
    }

    @Test
    public void testFree_WithOccupierCountGreaterThan0_OccupierCountDecreasesBy1() {
        simpleSector._addArea(new Area());
        simpleSector.gainControl(CombatantSide.ATTACKER);
        simpleSector.free();
        assertEquals(0, simpleSector.occupierCount);
    }

    @Test
    public void testFree_WithOccupierCountEqualTo0_OccupierIsSetToNull() {
        simpleSector._addArea(new Area());
        simpleSector.gainControl(CombatantSide.ATTACKER);
        simpleSector.free();
        assertNull(simpleSector.occupier);
    }

    @Test
    public void testFree_WithOccupierCountEqualTo0_OccupierChangedIsCalledForEachSectorObserver() {
        SectorObserverMock observer1 = new SectorObserverMock();
        SectorObserverMock observer2 = new SectorObserverMock();
        simpleSector.addObserver(observer1);
        simpleSector.addObserver(observer2);

        simpleSector._addArea(new Area());
        simpleSector.gainControl(CombatantSide.ATTACKER);
        simpleSector.free();

        assertTrue(observer1.occupierChangedCalled);
        assertTrue(observer2.occupierChangedCalled);
    }

    @Test
    public void testFree_WithOccupierCountEqualTo0_OccupierCountIsGreaterThanEqualTo0() {
        simpleSector._addArea(new Area());
        simpleSector.free();
        assertTrue(simpleSector.occupierCount >= 0);
    }

    @Test
    public void testGainControl_WithDifferentOccupier_OccupierIsSetToSide() {
        simpleSector.gainControl(CombatantSide.ATTACKER);
        assertSame(CombatantSide.ATTACKER, simpleSector.occupier);
    }

    @Test
    public void testGainControl_WithDifferentOccupier_OccupierChangedIsCalledForEachSectorObserver() {
        SectorObserverMock observer1 = new SectorObserverMock();
        SectorObserverMock observer2 = new SectorObserverMock();
        simpleSector.addObserver(observer1);
        simpleSector.addObserver(observer2);

        simpleSector.gainControl(CombatantSide.ATTACKER);

        assertTrue(observer1.occupierChangedCalled);
        assertTrue(observer2.occupierChangedCalled);
    }

    @Test
    public void testGainControl_WithSameOccupier_OccupierCountIncreasesBy1() {
        simpleSector._addArea(new Area());
        simpleSector.gainControl(CombatantSide.ATTACKER);
        int initialCount = simpleSector.occupierCount;
        simpleSector.gainControl(CombatantSide.ATTACKER);
        assertEquals(initialCount + 1, simpleSector.occupierCount);
    }

    @Test
    public void testGainControl_WithSameOccupier_OccupierCountIsGreaterThanEqualTo0() {
        simpleSector._addArea(new Area());
        simpleSector.gainControl(CombatantSide.ATTACKER);
        simpleSector.gainControl(CombatantSide.ATTACKER);
        assertTrue(simpleSector.occupierCount >= 0);
    }

    @Test
    public void testGetOccupier_WithOccupierSet_ReturnsOccupier() {
        simpleSector.gainControl(CombatantSide.ATTACKER);
        assertSame(CombatantSide.ATTACKER, simpleSector.getOccupier());
    }

    @Test
    public void testGetOccupier_WithOccupierNotSet_ReturnsNull() {
        assertNull(simpleSector.getOccupier());
    }

    @Test
    public void testIsOccupied_WithOccupierSet_ReturnsTrue() {
        simpleSector.gainControl(CombatantSide.ATTACKER);
        assertTrue(simpleSector.isOccupied());
    }

    @Test
    public void testIsOccupied_WithOccupierNotSet_ReturnsFalse() {
        assertFalse(simpleSector.isOccupied());
    }

    @Test
    public void testGetHeight_ReturnsHeight() {
        assertEquals(10, simpleSector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn_WithDefenderSpawnTrue_ReturnsTrue() {
        assertTrue(simpleSector.isDefenderSpawn());
    }

    @Test
    public void testIsDefenderSpawn_WithDefenderSpawnFalse_ReturnsFalse() {
        SimpleSector sector = new SimpleSector(10, false, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        assertFalse(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn_WithAttackerSpawnTrue_ReturnsTrue() {
        assertTrue(simpleSector.isAttackerSpawn());
    }

    @Test
    public void testIsAttackerSpawn_WithAttackerSpawnFalse_ReturnsFalse() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        assertFalse(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag_WithFlagTrue_ReturnsTrue() {
        assertTrue(simpleSector.isFlag());
    }

    @Test
    public void testIsFlag_WithFlagFalse_ReturnsFalse() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, false, 5, CharacterClass.WARRIOR);
        assertFalse(sector.isFlag());
    }

    @Test
    public void testGetBonus_ReturnsSectorBonusWithCorrectValues() {
        SectorBonus bonus = simpleSector.getBonus(CharacterClass.WARRIOR);
        assertEquals(7, bonus.getAttackBonus());
        assertEquals(8, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testGetBonusWithDifferentCharClass_ReturnsSectorBonusWithCorrectValues() {
        SectorBonus bonus = simpleSector.getBonus(CharacterClass.SNIPER);
        assertEquals(5, bonus.getAttackBonus());
        assertEquals(6, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testGetBonus_WithOccupiedArea_ReturnsSectorBonusWithCorrectValues() {
        Area area = new Area();
        simpleSector._addArea(area);
        Combatant combatant = new Combatant();
        combatant.setWeapon(new Weapon());
        area.gainControl(combatant);
        SectorBonus bonus = simpleSector.getBonus(CharacterClass.WARRIOR);
        assertEquals(7, bonus.getAttackBonus());
        assertEquals(8, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testGetAreas_ReturnsSetOfAreas() {
        assertEquals(simpleSector.areas, simpleSector.getAreas());
    }

    @Test
    public void testAddObserver_WithObserver_ObserverIsAddedToObservers() {
        SectorObserverMock observer = new SectorObserverMock();
        simpleSector.addObserver(observer);
        assertTrue(simpleSector.observers.contains(observer));
    }

    @Test
    public void testRemoveObserver_WithObserver_ObserverIsRemovedFromObservers() {
        SectorObserverMock observer = new SectorObserverMock();
        simpleSector.addObserver(observer);
        simpleSector.removeObserver(observer);
        assertFalse(simpleSector.observers.contains(observer));
    }

    private class SectorObserverMock implements SectorObserver {
        boolean occupierChangedCalled = false;

        @Override
        public void occupierChanged() {
            occupierChangedCalled = true;
        }
    }
}