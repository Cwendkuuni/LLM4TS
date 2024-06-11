package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;
import org.junit.*;

public class SimpleSectorTest {

    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    }

    @Test
    public void testFree() {
        sector.free();
        assertEquals(0, sector.getOccupierCount());
    }

    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(1, sector.getOccupierCount());
    }

    @Test
    public void testGetOccupier() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
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
        assertEquals(7, bonus.getAttackBonus());
        assertEquals(5, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testGetAreas() {
        assertNotNull(sector.getAreas());
    }

    @Test
    public void testHashCode() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertEquals(sector.hashCode(), other.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertTrue(sector.equals(other));
    }

    @Test
    public void testAddObserver() {
        sector.addObserver(new SectorObserver() {
        });
        assertEquals(1, sector.getObservers().size());
    }

    @Test
    public void testRemoveObserver() {
        SectorObserver observer = new SectorObserver() {
        };
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertEquals(0, sector.getObservers().size());
    }

}