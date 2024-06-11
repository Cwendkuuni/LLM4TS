package de.outstare.fortbattleplayer.model.impl;

import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.SectorObserver;
import de.outstare.fortbattleplayer.model.SectorBonus;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class SimpleSectorTest {
    private SimpleSector simpleSector;

    @Before
    public void setUp() {
        simpleSector = new SimpleSector(5, true, false, 10, 5, true, 7, CharacterClass.WARRIOR);
    }

    @Test
    public void testFree() {
        simpleSector.gainControl(CombatantSide.ATTACKER);
        simpleSector.free();
        assertNull(simpleSector.getOccupier());
        assertFalse(simpleSector.isOccupied());

        simpleSector.gainControl(CombatantSide.DEFENDER);
        simpleSector.gainControl(CombatantSide.DEFENDER);
        simpleSector.free();
        assertNotNull(simpleSector.getOccupier());
        assertTrue(simpleSector.isOccupied());
        simpleSector.free();
        assertNull(simpleSector.getOccupier());
        assertFalse(simpleSector.isOccupied());
    }

    @Test
    public void testGainControl() {
        assertNull(simpleSector.getOccupier());

        simpleSector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, simpleSector.getOccupier());
        assertTrue(simpleSector.isOccupied());

        simpleSector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, simpleSector.getOccupier());
        assertTrue(simpleSector.isOccupied());
    }

    @Test
    public void testGetOccupier() {
        assertNull(simpleSector.getOccupier());

        simpleSector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, simpleSector.getOccupier());

        simpleSector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, simpleSector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        assertFalse(simpleSector.isOccupied());

        simpleSector.gainControl(CombatantSide.ATTACKER);
        assertTrue(simpleSector.isOccupied());

        simpleSector.gainControl(CombatantSide.DEFENDER);
        assertTrue(simpleSector.isOccupied());
    }

    @Test
    public void testGetHeight() {
        assertEquals(5, simpleSector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        assertTrue(simpleSector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        assertFalse(simpleSector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        assertTrue(simpleSector.isFlag());
    }

    @Test
    public void testGetBonus() {
        SectorBonus bonus = simpleSector.getBonus(CharacterClass.WARRIOR);
        assertEquals(17, bonus.getAttackBonus());
        assertEquals(12, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());

        bonus = simpleSector.getBonus(CharacterClass.ARCHER);
        assertEquals(0, bonus.getAttackBonus());
        assertEquals(0, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testGetDamageBonus() {
        int damageBonus = simpleSector.getDamageBonus();
        assertEquals(0, damageBonus);
    }

    @Test
    public void testGetAttackBonus() {
        int attackBonus = simpleSector.getAttackBonus(CharacterClass.WARRIOR);
        assertEquals(17, attackBonus);

        attackBonus = simpleSector.getAttackBonus(CharacterClass.ARCHER);
        assertEquals(0, attackBonus);
    }

    @Test
    public void testGetDefenseBonus() {
        int defenseBonus = simpleSector.getDefenseBonus(CharacterClass.WARRIOR);
        assertEquals(12, defenseBonus);

        defenseBonus = simpleSector.getDefenseBonus(CharacterClass.ARCHER);
        assertEquals(0, defenseBonus);
    }

    @Test
    public void testClassBonus() {
        int classBonus = simpleSector.classBonus(CharacterClass.WARRIOR);
        assertEquals(7, classBonus);

        classBonus = simpleSector.classBonus(CharacterClass.ARCHER);
        assertEquals(0, classBonus);
    }

    @Test
    public void testGetAreas() {
        Set<Area> areas = simpleSector.getAreas();
        assertEquals(new HashSet<Area>(), areas);
    }

    @Test
    public void test_addArea() {
        Area area = new Area();
        simpleSector._addArea(area);

        Set<Area> areas = simpleSector.getAreas();
        Set<Area> expected = new HashSet<>();
        expected.add(area);

        assertEquals(expected, areas);
    }

    @Test
    public void testHashCode() {
        SimpleSector other = new SimpleSector(5, true, false, 10, 5, true, 7, CharacterClass.WARRIOR);
        assertEquals(simpleSector.hashCode(), other.hashCode());

        other = new SimpleSector(5, false, false, 10, 5, true, 7, CharacterClass.WARRIOR);
        assertNotEquals(simpleSector.hashCode(), other.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector other = new SimpleSector(5, true, false, 10, 5, true, 7, CharacterClass.WARRIOR);
        assertTrue(simpleSector.equals(other));

        other = new SimpleSector(5, false, false, 10, 5, true, 7, CharacterClass.WARRIOR);
        assertFalse(simpleSector.equals(other));
    }

    @Test
    public void testAddObserver() {
        SectorObserver observer = new SectorObserver() {
            @Override
            public void occupierChanged() {

            }
        };
        simpleSector.addObserver(observer);

        Set<SectorObserver> observers = simpleSector.observers;
        Set<SectorObserver> expected = new HashSet<>();
        expected.add(observer);

        assertEquals(expected, observers);
    }

    @Test
    public void testRemoveObserver() {
        SectorObserver observer = new SectorObserver() {
            @Override
            public void occupierChanged() {

            }
        };
        simpleSector.addObserver(observer);

        simpleSector.removeObserver(observer);

        Set<SectorObserver> observers = simpleSector.observers;
        assertEquals(new HashSet<>(), observers);
    }
}