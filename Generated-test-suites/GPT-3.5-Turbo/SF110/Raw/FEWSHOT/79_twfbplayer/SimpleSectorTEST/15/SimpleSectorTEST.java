package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.SectorObserver;
import de.outstare.fortbattleplayer.model.Weapon;

public class SimpleSectorTest {
    private final int height = 10;
    private final boolean defenderSpawn = false;
    private final boolean attackerSpawn = false;
    private final int attackBonus = 5;
    private final int defendBonus = 5;
    private final boolean flag = false;
    private final int classBonus = 3;
    private final CharacterClass classType = CharacterClass.WARRIOR;
    private final SimpleSector sector = new SimpleSector(height, defenderSpawn, attackerSpawn, attackBonus, defendBonus, flag, classBonus, classType);

    @Test
    public void testFree() {
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertNull(sector.getOccupier());
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        assertTrue(sector.isOccupied());
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
        assertEquals(height, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        assertEquals(defenderSpawn, sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        assertEquals(attackerSpawn, sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        assertEquals(flag, sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        final SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
        assertEquals(8, bonus.getAttackBonus());
        assertEquals(8, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testGetAreas() {
        final Set<Area> areas = new HashSet<>();
        assertEquals(areas, sector.getAreas());
    }

    @Test
    public void test_addArea() {
        final Area area = new Area();
        sector._addArea(area);
        final Set<Area> areas = new HashSet<>();
        areas.add(area);
        assertEquals(areas, sector.getAreas());
    }

    @Test
    public void testHashCode() {
        final SimpleSector other = new SimpleSector(height, defenderSpawn, attackerSpawn, attackBonus, defendBonus, flag, classBonus, classType);
        assertEquals(other.hashCode(), sector.hashCode());
    }

    @Test
    public void testEquals() {
        final SimpleSector other = new SimpleSector(height, defenderSpawn, attackerSpawn, attackBonus, defendBonus, flag, classBonus, classType);
        assertTrue(sector.equals(other));
    }

    @Test
    public void testAddObserver() {
        final SectorObserver observer = new SectorObserver() {
            @Override
            public void occupierChanged() {
                // do nothing
            }
        };
        sector.addObserver(observer);
        // check if observer has been added
    }

    @Test
    public void testRemoveObserver() {
        final SectorObserver observer = new SectorObserver() {
            @Override
            public void occupierChanged() {
                // do nothing
            }
        };
        sector.addObserver(observer);
        sector.removeObserver(observer);
        // check that observer has been removed
    }
}