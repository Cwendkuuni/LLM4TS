
package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class SimpleSectorTest {
    private SimpleSector sector;

    @Before
    public void setup() {
        sector = new SimpleSector(10, true, false, 5, 8, true, 3, CharacterClass.WARRIOR);
    }

    @Test
    public void testOccupierChanged() {
        // Check if observers are notified when occupier changes
        sector.addObserver(new DummyObserver());
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(DummyObserver.wasNotified);
        DummyObserver.reset();
    }

    @Test
    public void testFree_PositiveOccupierCount() {
        // Check if occupier count decrements when freeing sector
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertEquals(1, sector.occupierCount);
    }

    @Test
    public void testFree_ZeroOccupierCount() {
        // Check if occupier is set to null when occupier count reaches zero
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertNull(sector.getOccupier());
        assertEquals(0, sector.occupierCount);
    }

    @Test
    public void testGainControl_NewOccupier() {
        // Check if occupier changes when a new side gains control
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void testGainControl_SameOccupier() {
        // Check if occupier does not change when the same side gains control again
        sector.gainControl(CombatantSide.ATTACKER);
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testGetOccupier_Occupied() {
        // Check if isOccupied returns true when the sector is occupied
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testGetOccupier_NotOccupied() {
        // Check if isOccupied returns false when the sector is not occupied
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGetHeight() {
        // Check if getHeight returns the correct height of the sector
        assertEquals(10, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        // Check if isDefenderSpawn returns the correct value
        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        // Check if isAttackerSpawn returns the correct value
        assertFalse(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        // Check if isFlag returns the correct value
        assertTrue(sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        // Check if getBonus returns the correct sector bonus
        SectorBonus expectedBonus = new SectorBonus(8, 11, 0);
        assertEquals(expectedBonus, sector.getBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetAreas() {
        // Check if getAreas returns the set of areas
        Set<Area> expectedAreas = new HashSet<Area>();
        Area area1 = new Area();
        Area area2 = new Area();
        expectedAreas.add(area1);
        expectedAreas.add(area2);
        sector._addArea(area1);
        sector._addArea(area2);

        assertEquals(expectedAreas, sector.getAreas());
    }

    private static class DummyObserver implements SectorObserver {
        public static boolean wasNotified = false;

        @Override
        public void occupierChanged() {
            wasNotified = true;
        }

        public static void reset() {
            wasNotified = false;
        }
    }
}
