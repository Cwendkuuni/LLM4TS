package de.outstare.fortbattleplayer.model.impl;

import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.SectorObserver;
import de.outstare.fortbattleplayer.model.CharacterClass;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class SimpleSectorTest {
    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.TANK);
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
    public void testGetOccupier() {
        assertNull(sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGetBonus() {
        SectorBonus bonus = sector.getBonus(CharacterClass.TANK);
        assertNotNull(bonus);
        assertEquals(5, bonus.getAttackBonus());
        assertEquals(3, bonus.getDefenseBonus());
    }

    @Test
    public void testGetAreas() {
        Set<Area> areas = new HashSet<>();
        assertEquals(areas, sector.getAreas());
    }

    @Test
    public void testAddArea() {
        Area area = new Area();
        sector._addArea(area);
        Set<Area> areas = new HashSet<>();
        areas.add(area);
        assertEquals(areas, sector.getAreas());
    }

    @Test
    public void testFree() {
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
    }

    @Test
    public void testGainControl() {
        CombatantSide side = CombatantSide.ATTACKERS;
        sector.gainControl(side);
        assertEquals(1, sector.getOccupierCount());
        assertEquals(side, sector.getOccupier());
    }

    @Test
    public void testHashCode() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.TANK);
        assertEquals(other.hashCode(), sector.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.TANK);
        assertTrue(sector.equals(other));
    }
}