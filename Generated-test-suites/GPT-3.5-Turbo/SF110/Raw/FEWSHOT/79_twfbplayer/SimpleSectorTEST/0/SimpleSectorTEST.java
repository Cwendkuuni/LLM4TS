package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Weapon;

public class SimpleSectorTest {

    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.ARCHER);
    }

    @Test
    public void testFree_noCombatants_leaveSector() {
        sector.free();
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testFree_oneCombatant_leaveSector() {
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGainControl_noCombatants_occupySector() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void testGainControl_occupiedByDifferentSide_changeOccupier() {
        sector.gainControl(CombatantSide.ATTACKER);
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void testGetOccupier_notOccupied_returnNull() {
        assertFalse(sector.isOccupied());
        assertEquals(null, sector.getOccupier());
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
        SectorBonus bonus = sector.getBonus(CharacterClass.ARCHER);
        assertEquals(7, bonus.getAttackBonus());
        assertEquals(5, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testGetAreas() {
        Set<Area> areas = new HashSet<>();
        assertEquals(areas, sector.getAreas());
    }

    @Test
    public void test_addArea() {
        Area area = new Area();
        sector._addArea(area);
        Set<Area> areas = new HashSet<>();
        areas.add(area);
        assertEquals(areas, sector.getAreas());
    }

    @Test
    public void testHashCode() {
        int expectedHash = 31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * 1 + sector.getAreas().hashCode()) + 5) + 1231) + 2) + 5) + 3) + 1231) + 2) + CharacterClass.ARCHER.hashCode();
        assertEquals(expectedHash, sector.hashCode());
    }

    @Test
    public void testEquals_sameObject_returnTrue() {
        assertEquals(sector, sector);
    }

    @Test
    public void testEquals_differentClass_returnFalse() {
        assertFalse(sector.equals(new Object()));
    }

    @Test
    public void testEquals_null_returnFalse() {
        assertFalse(sector.equals(null));
    }

    @Test
    public void testEquals_sameFields_returnTrue() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.ARCHER);
        assertEquals(sector, other);
    }

    @Test
    public void testEquals_differentHeight_returnFalse() {
        SimpleSector other = new SimpleSector(5, true, false, 5, 3, true, 2, CharacterClass.ARCHER);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEquals_differentDefenderSpawn_returnFalse() {
        SimpleSector other = new SimpleSector(10, false, false, 5, 3, true, 2, CharacterClass.ARCHER);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEquals_differentAttackerSpawn_returnFalse() {
        SimpleSector other = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEquals_differentAttackBonus_returnFalse() {
        SimpleSector other = new SimpleSector(10, true, false, 10, 3, true, 2, CharacterClass.ARCHER);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEquals_differentDefendBonus_returnFalse() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 5, true, 2, CharacterClass.ARCHER);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEquals_differentFlag_returnFalse() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.ARCHER);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEquals_differentClassBonus_returnFalse() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 5, CharacterClass.ARCHER);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEquals_differentClassType_returnFalse() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEquals_differentAreas_returnFalse() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.ARCHER);
        other._addArea(new Area());
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEquals_sameAreas_returnTrue() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.ARCHER);
        sector._addArea(new Area());
        other._addArea(new Area());
        assertEquals(sector, other);
    }

    @Test
    public void testEquals_sameOccupied_returnTrue() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.ATTACKER);
        other.gainControl(CombatantSide.ATTACKER);
        assertEquals(sector, other);
    }

    @Test
    public void testEquals_differentOccupied_returnFalse() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.ATTACKER);
        other.gainControl(CombatantSide.DEFENDER);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testAddObserver() {
        SectorObserver observer = new SectorObserver();
        sector.addObserver(observer);
        Set<SectorObserver> observers = new HashSet<>();
        observers.add(observer);
        assertEquals(observers, sector.getObservers());
    }

    @Test
    public void testRemoveObserver() {
        SectorObserver observer = new SectorObserver();
        sector.addObserver(observer);
        sector.removeObserver(observer);
        Set<SectorObserver> observers = new HashSet<>();
        assertEquals(observers, sector.getObservers());
    }

    private class SectorObserver implements Observer {

        @Override
        public void occupierChanged() {
            // do nothing
        }
    }

}