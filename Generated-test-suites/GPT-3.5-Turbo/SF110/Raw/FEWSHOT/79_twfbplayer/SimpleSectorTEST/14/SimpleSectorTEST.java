
package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.SectorObserver;
import de.outstare.fortbattleplayer.model.Weapon;

public class SimpleSectorTest {
    
    private SimpleSector sector;
    private static final int HEIGHT = 100;
    private static final boolean DEFENDER_SPAWN = true;
    private static final boolean ATTACKER_SPAWN = true;
    private static final int ATTACK_BONUS = 5;
    private static final int DEFENSE_BONUS = 5;
    private static final boolean FLAG = true;
    private static final int CLASS_BONUS = 5;
    private static final CharacterClass CLASS_TYPE = CharacterClass.MAGE;
    
    @Before
    public void setup() {
        sector = new SimpleSector(HEIGHT, DEFENDER_SPAWN, ATTACKER_SPAWN, ATTACK_BONUS, DEFENSE_BONUS, FLAG, CLASS_BONUS, CLASS_TYPE);
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
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(2, sector.getOccupierCount());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }
    
    @Test
    public void testIsOccupied() {
        assertFalse(sector.isOccupied());
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
    }
    
    @Test
    public void testGetHeight() {
        assertEquals(HEIGHT, sector.getHeight());
    }
    
    @Test
    public void testIsDefenderSpawn() {
        assertTrue(sector.isDefenderSpawn());
    }
    
    @Test
    public void testIsAttackerSpawn() {
        assertTrue(sector.isAttackerSpawn());
    }
    
    @Test
    public void testIsFlag() {
        assertTrue(sector.isFlag());
    }
    
    @Test
    public void testGetBonus() {
        SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
        assertEquals((ATTACK_BONUS + DEFENSE_BONUS + CLASS_BONUS), bonus.getAttackerBonus());
        assertEquals((DEFENSE_BONUS + DEFENSE_BONUS + CLASS_BONUS), bonus.getDefenderBonus());
        assertEquals(0, bonus.getDamageBonus());
    }
    
    @Test
    public void testGetDamageBonus() {
        int damageBonus = sector.getDamageBonus();
        assertEquals(0, damageBonus);
    }
    
    @Test
    public void testGetOccupier() {
        assertNull(sector.getOccupier());
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }
    
    @Test
    public void testGetAttackBonus() {
        int attackBonus = sector.getAttackBonus(CharacterClass.WARRIOR);
        assertEquals((ATTACK_BONUS + CLASS_BONUS), attackBonus);
    }
    
    @Test
    public void testGetDefenseBonus() {
        int defenseBonus = sector.getDefenseBonus(CharacterClass.WARRIOR);
        assertEquals((DEFENSE_BONUS + CLASS_BONUS), defenseBonus);
    }
    
    @Test
    public void testClassBonus() {
        int classBonus = sector.classBonus(CharacterClass.WARRIOR);
        assertEquals(0, classBonus);
        classBonus = sector.classBonus(CLASS_TYPE);
        assertEquals(CLASS_BONUS, classBonus);
    }
    
    @Test
    public void testGetAreas() {
        Area area1 = new SimpleArea();
        Area area2 = new SimpleArea();
        sector._addArea(area1);
        sector._addArea(area2);
        Set<Area> areas = sector.getAreas();
        assertEquals(2, areas.size());
        assertTrue(areas.contains(area1));
        assertTrue(areas.contains(area2));
    }
    
    @Test
    public void testHashCode() {
        // create another sector object with same parameters
        SimpleSector sector2 = new SimpleSector(HEIGHT, DEFENDER_SPAWN, ATTACKER_SPAWN, ATTACK_BONUS, DEFENSE_BONUS, FLAG, CLASS_BONUS, CLASS_TYPE);
        assertEquals(sector.hashCode(), sector2.hashCode());
    }
    
    @Test
    public void testEquals() {
        // create another sector object with same parameters
        SimpleSector sector2 = new SimpleSector(HEIGHT, DEFENDER_SPAWN, ATTACKER_SPAWN, ATTACK_BONUS, DEFENSE_BONUS, FLAG, CLASS_BONUS, CLASS_TYPE);
        assertTrue(sector.equals(sector2));
        // create another sector object with different parameters
        SimpleSector sector3 = new SimpleSector(80, false, false, 0, 0, false, 0, CharacterClass.NONE);
        assertFalse(sector.equals(sector3));
    }
    
    @Test
    public void testAddAndRemoveObserver() {
        SectorObserver observer1 = new SimpleSectorObserver();
        SectorObserver observer2 = new SimpleSectorObserver();
        
        sector.addObserver(observer1);
        sector.addObserver(observer2);
        
        sector.gainControl(CombatantSide.DEFENDER);
        
        assertTrue(observer1.occupierChangedCalled);
        assertTrue(observer2.occupierChangedCalled);
        
        sector.removeObserver(observer1);
        sector.removeObserver(observer2);
    }
}

class SimpleArea implements Area {

    @Override
    public boolean isOccupied() {
        return false;
    }

    @Override
    public Combatant getOccupier() {
        return null;
    }
    
}

class SimpleSectorObserver implements SectorObserver {
    
    public boolean occupierChangedCalled = false;

    @Override
    public void occupierChanged() {
        occupierChangedCalled = true;
    }
    
}
