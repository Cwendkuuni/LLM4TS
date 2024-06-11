package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.SimpleSector;

public class SimpleSectorTest {
    
    private SimpleSector sector;
    private Set<Area> areas;
    private SectorObserver observer;
    
    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, false, 5, 10, true, 2, CharacterClass.WARRIOR);
        areas = new HashSet<>();
        observer = new SectorObserver() {
            @Override
            public void occupierChanged() {}
        };
    }
    
    @Test
    public void testFree() {
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
    }
    
    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(1, sector.getOccupierCount());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }
    
    @Test
    public void testGainControlSameSide() {
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(2, sector.getOccupierCount());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
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
    public void testHeight() {
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
        assertEquals(7, bonus.getAttackBonus());
        assertEquals(12, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }
    
    @Test
    public void testGetBonusDifferentClass() {
        SectorBonus bonus = sector.getBonus(CharacterClass.WIZARD);
        assertNotNull(bonus);
        assertEquals(5, bonus.getAttackBonus());
        assertEquals(10, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }
    
    @Test
    public void testGetBonusWithWeapon() {
        // TODO
    }
    
    @Test
    public void testGetAttackBonus() {
        assertEquals(7, sector.getAttackBonus(CharacterClass.WARRIOR));
    }
    
    @Test
    public void testGetDefenseBonus() {
        assertEquals(12, sector.getDefenseBonus(CharacterClass.WARRIOR));
    }
    
    @Test
    public void testClassBonus() {
        assertEquals(2, sector.classBonus(CharacterClass.WARRIOR));
    }
    
    @Test
    public void testGetAreas() {
        assertEquals(0, sector.getAreas().size());
        
        Area area = new Area();
        sector._addArea(area);
        assertEquals(1, sector.getAreas().size());
        assertTrue(sector.getAreas().contains(area));
    }
    
    @Test
    public void testHashCode() {
        SimpleSector sameSector = new SimpleSector(10, true, false, 5, 10, true, 2, CharacterClass.WARRIOR);
        assertEquals(sector.hashCode(), sameSector.hashCode());
    }
    
    @Test
    public void testEquals() {
        SimpleSector sameSector = new SimpleSector(10, true, false, 5, 10, true, 2, CharacterClass.WARRIOR);
        assertTrue(sector.equals(sector));
        assertTrue(sector.equals(sameSector));
        assertFalse(sector.equals(null));
        
        SimpleSector differentSector = new SimpleSector(10, false, false, 10, 5, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector.equals(differentSector));
    }
    
    @Test
    public void testAddObserver() {
        sector.addObserver(observer);
        // TODO
    }
    
    @Test
    public void testRemoveObserver() {
        sector.removeObserver(observer);
        // TODO
    }
    
}