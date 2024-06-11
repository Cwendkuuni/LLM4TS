package de.outstare.fortbattleplayer.model.impl;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class SimpleSectorTest {
    @Test
    public void testFree() {
        // Test case for free() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testOccupierChanged() {
        // Test case for occupierChanged() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        MockObserver observer = new MockObserver();
        sector.addObserver(observer);
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(observer.isOccupierChangedCalled());
    }

    @Test
    public void testGainControl() {
        // Test case for gainControl(CombatantSide side) method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void testGetOccupier() {
        // Test case for getOccupier() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        // Test case for isOccupied() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector.isOccupied());

        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testGetHeight() {
        // Test case for getHeight() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertEquals(10, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        // Test case for isDefenderSpawn() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        // Test case for isAttackerSpawn() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        // Test case for isFlag() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertTrue(sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        // Test case for getBonus(CharacterClass charClass) method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.ROGUE);
        SectorBonus bonus = sector.getBonus(CharacterClass.ROGUE);
        assertNotNull(bonus);
    }

    @Test
    public void testGetDamageBonus() {
        // Test case for getDamageBonus() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        int bonus = sector.getDamageBonus();
        assertEquals(0, bonus);
    }

    @Test
    public void testGetAttackBonus() {
        // Test case for getAttackBonus(CharacterClass charClass) method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        int bonus = sector.getAttackBonus(CharacterClass.WARRIOR);
        assertEquals(7, bonus);
    }

    @Test
    public void testGetDefenseBonus() {
        // Test case for getDefenseBonus(CharacterClass charClass) method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        int bonus = sector.getDefenseBonus(CharacterClass.WARRIOR);
        assertEquals(5, bonus);
    }

    @Test
    public void testClassBonus() {
        // Test case for classBonus(CharacterClass charClass) method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        int bonus = sector.classBonus(CharacterClass.WARRIOR);
        assertEquals(2, bonus);
    }

    @Test
    public void testGetAreas() {
        // Test case for getAreas() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        Set<Area> areas = sector.getAreas();
        assertNotNull(areas);
    }

    @Test
    public void test_addArea() {
        // Test case for _addArea(Area area) method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        Area area = new Area();
        sector._addArea(area);
        Set<Area> areas = sector.getAreas();
        assertTrue(areas.contains(area));
    }

    @Test
    public void testHashCode() {
        // Test case for hashCode() method
        SimpleSector sector1 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertEquals(sector1.hashCode(), sector2.hashCode());
    }

    @Test
    public void testEquals() {
        // Test case for equals(Object obj) method
        SimpleSector sector1 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertTrue(sector1.equals(sector2));
    }

    @Test
    public void testAddObserver() {
        // Test case for addObserver(SectorObserver observer) method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        MockObserver observer = new MockObserver();
        sector.addObserver(observer);
        assertTrue(sector.observers.contains(observer));
    }

    @Test
    public void testRemoveObserver() {
        // Test case for removeObserver(SectorObserver observer) method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        MockObserver observer = new MockObserver();
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertFalse(sector.observers.contains(observer));
    }
}

class MockObserver implements SectorObserver {
    private boolean occupierChangedCalled;

    @Override
    public void occupierChanged() {
        occupierChangedCalled = true;
    }

    public boolean isOccupierChangedCalled() {
        return occupierChangedCalled;
    }
}