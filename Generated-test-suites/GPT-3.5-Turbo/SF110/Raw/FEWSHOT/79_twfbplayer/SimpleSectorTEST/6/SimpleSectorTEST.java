package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.SectorObserver;
import de.outstare.fortbattleplayer.model.Weapon;

public class SimpleSectorTest {
    
    private SimpleSector sector;
    private Area area1;
    private Area area2;
    
    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.SOLDIER);
        area1 = new Area();
        area2 = new Area();
        sector._addArea(area1);
        sector._addArea(area2);
    }

    @Test
    public void testFreeWithPositiveOccupierCount() {
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.ATTACKER);
        sector.gainControl(CombatantSide.DEFENDER);
        
        sector.free();
        
        assertEquals(2, sector.getOccupierCount());
    }
    
    @Test
    public void testFreeWithZeroOccupierCount() {
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.ATTACKER);
        
        sector.free();
        
        assertEquals(1, sector.getOccupierCount());
    }
    
    @Test
    public void testFreeWithNegativeOccupierCount() {
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        sector.free();
        
        sector.free();
        
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
    }
    
    @Test
    public void testGainControlChangeOccupier() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(1, sector.getOccupierCount());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(2, sector.getOccupierCount());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }
    
    @Test
    public void testGainControlSameOccupier() {
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.DEFENDER);

        assertEquals(3, sector.getOccupierCount());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }
    
    @Test
    public void testOccupiedWithNoOccupier() {
        assertFalse(sector.isOccupied());
    }
    
    @Test
    public void testOccupiedWithOccupier() {
        sector.gainControl(CombatantSide.DEFENDER);
        
        assertTrue(sector.isOccupied());
    }
    
    @Test
    public void testGetBonus() {
        SectorBonus bonus = sector.getBonus(CharacterClass.SOLDIER);
        
        assertEquals(7, bonus.getAttackBonus());
        assertEquals(5, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }
    
    @Test
    public void testGetAreas() {
        Set<Area> areas = new HashSet<>();
        areas.add(area1);
        areas.add(area2);
        
        assertEquals(areas, sector.getAreas());
    }
    
    @Test
    public void testHashCode() {
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.SOLDIER);
        
        assertEquals(sector.hashCode(), sector2.hashCode());
    }
    
    @Test
    public void testEqualsWithSameObject() {
        SimpleSector sector2 = sector;
        
        assertTrue(sector.equals(sector2));
    }
    
    @Test
    public void testEqualsWithNull() {
        assertFalse(sector.equals(null));
    }
    
    @Test
    public void testEqualsWithDifferentClass() {
        assertFalse(sector.equals(area1));
    }
    
    @Test
    public void testEqualsWithDifferentHeight() {
        SimpleSector sector2 = new SimpleSector(5, true, false, 5, 3, true, 2, CharacterClass.SOLDIER);
        
        assertFalse(sector.equals(sector2));
    }
    
    @Test
    public void testEqualsWithDifferentDefenderSpawn() {
        SimpleSector sector2 = new SimpleSector(10, false, false, 5, 3, true, 2, CharacterClass.SOLDIER);
        
        assertFalse(sector.equals(sector2));
    }
    
    @Test
    public void testEqualsWithDifferentAttackerSpawn() {
        SimpleSector sector2 = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.SOLDIER);
        
        assertFalse(sector.equals(sector2));
    }
    
    @Test
    public void testEqualsWithDifferentAttackBonus() {
        SimpleSector sector2 = new SimpleSector(10, true, false, 10, 3, true, 2, CharacterClass.SOLDIER);
        
        assertFalse(sector.equals(sector2));
    }
    
    @Test
    public void testEqualsWithDifferentDefenseBonus() {
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 5, true, 2, CharacterClass.SOLDIER);
        
        assertFalse(sector.equals(sector2));
    }
    
    @Test
    public void testEqualsWithDifferentFlag() {
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.SOLDIER);
        
        assertFalse(sector.equals(sector2));
    }
    
    @Test
    public void testEqualsWithDifferentClassBonus() {
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 3, true, 5, CharacterClass.SOLDIER);
        
        assertFalse(sector.equals(sector2));
    }
    
    @Test
    public void testEqualsWithDifferentClassType() {
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.ASSASSIN);
        
        assertFalse(sector.equals(sector2));
    }
    
    @Test
    public void testEqualsWithDifferentAreas() {
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.SOLDIER);
        sector2._addArea(new Area());
        
        assertFalse(sector.equals(sector2));
    }

    @Test
    public void testAddObserver() {
        SectorObserver observer = new SectorObserver() {
            @Override
            public void occupierChanged() {
                // do nothing
            }
        };
        
        sector.addObserver(observer);
        
        assertEquals(1, sector.observers.size());
    }
    
    @Test
    public void testRemoveObserver() {
        SectorObserver observer = new SectorObserver() {
            @Override
            public void occupierChanged() {
                // do nothing
            }
        };
        sector.addObserver(observer);
        
        sector.removeObserver(observer);
        
        assertEquals(0, sector.observers.size());
    }
}