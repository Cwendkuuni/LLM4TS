package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.SectorObserver;
import de.outstare.fortbattleplayer.model.Weapon;

public class SimpleSectorTest {
    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
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
        assertEquals(9, bonus.getAttack());
        assertEquals(5, bonus.getDefense());
        assertEquals(0, bonus.getDamage());
    }

    @Test
    public void testGetAreas() {
        Set<Area> areas = sector.getAreas();
        assertNotNull(areas);
        assertEquals(0, areas.size());
    }

    @Test
    public void test_addArea() {
        Area area = mock(Area.class);
        sector._addArea(area);
        Set<Area> areas = sector.getAreas();
        assertNotNull(areas);
        assertEquals(1, areas.size());
        assertTrue(areas.contains(area));
    }

    @Test
    public void test_sectorOccupier() {
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        assertTrue(sector.isOccupied());
        sector.free();
        assertNull(sector.getOccupier());
        assertFalse(sector.isOccupied());
        sector.removeObserver(observer);
    }

    @Test
    public void testHashCode() {
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertEquals(sector.hashCode(), sector2.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertTrue(sector.equals(sector));
        assertTrue(sector.equals(sector2));
        assertFalse(sector.equals(null));
        assertTrue(sector.equals(sector));
        assertFalse(sector.equals(""));
        assertFalse(sector.equals(new Object()));
        assertFalse(sector.equals(new SimpleSector(10, true, false, 6, 3, true, 2, CharacterClass.WARRIOR)));
        assertFalse(sector.equals(new SimpleSector(10, true, false, 5, 2, true, 2, CharacterClass.WARRIOR)));
        assertFalse(sector.equals(new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR)));
        assertFalse(sector.equals(new SimpleSector(10, true, false, 5, 3, true, 3, CharacterClass.WARRIOR)));
        assertFalse(sector.equals(new SimpleSector(5, true, false, 5, 3, true, 2, CharacterClass.WARRIOR)));
        assertFalse(sector.equals(new SimpleSector(10, false, false, 5, 3, true, 2, CharacterClass.WARRIOR)));
        assertFalse(sector.equals(new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR)));
        assertFalse(sector.equals(new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.SORCERER)));
    }

    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testFree() {
        sector.free();
        assertNull(sector.getOccupier());
        assertFalse(sector.isOccupied());
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        assertTrue(sector.isOccupied());
        sector.free();
        assertNull(sector.getOccupier());
        assertFalse(sector.isOccupied());
    }
}