package de.outstare.fortbattleplayer.model.impl;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.SimpleSector;
import de.outstare.fortbattleplayer.model.SectorObserver;
import de.outstare.fortbattleplayer.model.Weapon;

public class SimpleSectorTest {
    

    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(5,true,false,10,20,true,5,CharacterClass.WARRIOR);
        sector.occupierCount = 1;
        sector.free();
        assertEquals(sector.occupierCount, 0);
        assertNull(sector.occupier);

        SimpleSector sector2 = new SimpleSector(3,false,true,0,0,false,0,CharacterClass.BARD);
        sector2.free();
        assertEquals(sector2.occupierCount, 0);
        assertNull(sector2.occupier);
    }

    @Test
    public void testOccupierChanged() {
        SimpleSector sector = new SimpleSector(5,true,false,10,20,true,5,CharacterClass.WARRIOR);
        SectorObserver observer = new SectorObserver(){
            @Override
            public void occupierChanged() {
                System.out.println("Occupier Changed");
            }
        };
        sector.observers.add(observer);
        sector.occupierChanged();
        assertTrue(true);
    }

    @Test
    public void testGainControl() {
        SimpleSector sector = new SimpleSector(5,true,false,10,20,true,5,CharacterClass.WARRIOR);
        CombatantSide side = CombatantSide.ATTACKER;
        sector.occupierCount = 0;
        sector.gainControl(side);
        assertEquals(sector.occupierCount, 1);
        assertEquals(sector.occupier, side);
    }

    @Test
    public void testGetOccupier() {
        SimpleSector sector = new SimpleSector(5,true,false,10,20,true,5,CharacterClass.WARRIOR);
        Piece side = CombatantSide.ATTACKER;
        sector.occupier = side;
        assertEquals(sector.getOccupier(), side);
    }

    @Test
    public void testIsOccupied() {
        SimpleSector sector = new SimpleSector(5,true,false,10,20,true,5,CharacterClass.WARRIOR);
        sector.occupierCount = 0;
        assertFalse(sector.isOccupied());
        sector.occupier = null;
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGetHeight() {
        SimpleSector sector = new SimpleSector(5,true,false,10,20,true,5,CharacterClass.WARRIOR);
        assertEquals(sector.getHeight(), 5);
    }

    @Test
    public void testIsDefenderSpawn() {
        SimpleSector sector = new SimpleSector(5,true,false,10,20,true,5,CharacterClass.WARRIOR);
        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        SimpleSector sector = new SimpleSector(5,true,false,10,20,true,5,CharacterClass.WARRIOR);
        assertFalse(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        SimpleSector sector = new SimpleSector(5,true,false,10,20,true,5,CharacterClass.WARRIOR);
        assertTrue(sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        SimpleSector sector = new SimpleSector(5,true,false,10,20,true,5,CharacterClass.WARRIOR);
        CharacterClass charClass = CharacterClass.WARRIOR;
        assertNotNull(sector.getBonus(charClass));
    }

    @Test
    public void testGetDamageBonus() {
        SimpleSector sector = new SimpleSector(5,true,false,10,20,true,5,CharacterClass.WARRIOR);
        assertEquals(sector.getDamageBonus(), 0);
    }

    @Test
    public void testGetAttackBonus() {
        SimpleSector sector = new SimpleSector(5,true,false,10,20,true,5,CharacterClass.WARRIOR);
        CharacterClass charClass = CharacterClass.WARRIOR;
        assertEquals(sector.getAttackBonus(charClass), 15);
    }

    @Test
    public void testGetDefenseBonus() {
        SimpleSector sector = new SimpleSector(5,true,false,10,20,true,5,CharacterClass.WARRIOR);
        CharacterClass charClass = CharacterClass.WARRIOR;
        assertEquals(sector.getDefenseBonus(charClass), 25);
    }

    @Test
    public void testClassBonus() {
        SimpleSector sector = new SimpleSector(5,true,false,10,20,true,5,CharacterClass.WARRIOR);
        CharacterClass charClass = CharacterClass.WARRIOR;
        assertEquals(sector.classBonus(charClass), 5);
    }

    @Test
    public void testGetAreas() {
        SimpleSector sector = new SimpleSector(5,true,false,10,20,true,5,CharacterClass.WARRIOR);
        assertNotNull(sector.getAreas());
    }

    @Test
    public void test_addArea() {
        SimpleSector sector = new SimpleSector(5,true,false,10,20,true,5,CharacterClass.WARRIOR);
        Area area = new Area();
        sector._addArea(area);
        assertTrue(sector.getAreas().contains(area));
    }

    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(5,true,false,10,20,true,5,CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(5,true,false,5,5,true,5,CharacterClass.WARRIOR);
        assertNotEquals(sector1.hashCode(), sector2.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(5,true,false,10,20,true,5,CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(5,true,false,5,5,true,5,CharacterClass.WARRIOR);
        assertFalse(sector1.equals(sector2));
    }

    @Test
    public void testAddObserver() {
        SimpleSector sector = new SimpleSector(5,true,false,10,20,true,5,CharacterClass.WARRIOR);
        SectorObserver observer = new SectorObserver(){
            @Override
            public void occupierChanged() {
                System.out.println("Occupier Changed");
            }
        };
        sector.addObserver(observer);
        assertTrue(sector.observers.contains(observer));
    }

    @Test
    public void testRemoveObserver() {
        SimpleSector sector = new SimpleSector(5,true,false,10,20,true,5,CharacterClass.WARRIOR);
        SectorObserver observer = new SectorObserver(){
            @Override
            public void occupierChanged() {
                System.out.println("Occupier Changed");
            }
        };
        sector.observers.add(observer);
        sector.removeObserver(observer);
        assertFalse(sector.observers.contains(observer));
    }

}