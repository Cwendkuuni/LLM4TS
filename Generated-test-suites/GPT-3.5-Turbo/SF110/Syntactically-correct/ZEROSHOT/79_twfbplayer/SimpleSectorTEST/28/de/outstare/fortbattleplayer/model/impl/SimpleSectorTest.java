package de.outstare.fortbattleplayer.model.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class SimpleSectorTest {
  
    private SimpleSector simpleSector;
    private Set<Area> areas;
    private Set<SectorObserver> observers;

    @Before
    public void setUp() {
        areas = new HashSet<>();
        observers = new HashSet<>();
        simpleSector = new SimpleSector(10, true, false, 5, 10, true, 15, CharacterClass.WARRIOR);
    }
    
    @Test
    public void testFree() {
        simpleSector.free();
        Assert.assertEquals(0, simpleSector.getOccupierCount());
        Assert.assertNull(simpleSector.getOccupier());
    }
    
    @Test
    public void testGainControl() {
        simpleSector.gainControl(CombatantSide.ATTACKER);
        Assert.assertEquals(1, simpleSector.getOccupierCount());
        Assert.assertEquals(CombatantSide.ATTACKER, simpleSector.getOccupier());
    }
    
    @Test
    public void testGetOccupier() {
        simpleSector.gainControl(CombatantSide.DEFENDER);
        Assert.assertEquals(CombatantSide.DEFENDER, simpleSector.getOccupier());
    }
    
    @Test
    public void testIsOccupied() {
        simpleSector.gainControl(CombatantSide.DEFENDER);
        Assert.assertTrue(simpleSector.isOccupied());
    }
    
    @Test
    public void testGetHeight() {
        Assert.assertEquals(10, simpleSector.getHeight());
    }
    
    @Test
    public void testIsDefenderSpawn() {
        Assert.assertTrue(simpleSector.isDefenderSpawn());
    }
    
    @Test
    public void testIsAttackerSpawn() {
        Assert.assertFalse(simpleSector.isAttackerSpawn());
    }
    
    @Test
    public void testIsFlag() {
        Assert.assertTrue(simpleSector.isFlag());
    }
    
    @Test
    public void testGetBonus() {
        SectorBonus bonus = simpleSector.getBonus(CharacterClass.WARRIOR);
        Assert.assertEquals(20, bonus.getAttack());
        Assert.assertEquals(25, bonus.getDefense());
        Assert.assertEquals(0, bonus.getDamage());
    }
    
    @Test
    public void testGetAreas() {
        simpleSector._addArea(new Area());
        Assert.assertEquals(1, simpleSector.getAreas().size());
    }
    
    @Test
    public void testAddObserver() {
        simpleSector.addObserver(new SectorObserverImpl());
        Assert.assertEquals(1, simpleSector.getObservers().size());
    }
    
    @Test
    public void testRemoveObserver() {
        SectorObserverImpl observer = new SectorObserverImpl();
        simpleSector.addObserver(observer);
        simpleSector.removeObserver(observer);
        Assert.assertEquals(0, simpleSector.getObservers().size());
    }

    private class SectorObserverImpl implements SectorObserver {
        @Override
        public void occupierChanged() {}
    }
}