package de.outstare.fortbattleplayer.model.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleSectorTest {
    @Test
    public void testFree() {
        // Test case for free() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        
        // Before calling free(), the occupierCount should be 0
        assertEquals(0, sector.occupierCount);
        
        // After calling free(), the occupierCount should be decremented by 1
        sector.free();
        assertEquals(-1, sector.occupierCount);
        
        // Test case for assert statement
        // Since occupierCount is less than 0, the assert statement will throw an AssertionError
        // Uncomment the following line to check the assert statement
        // sector.free();
    }
    
    @Test
    public void testOccupierChanged() {
        // Test case for occupierChanged() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        
        // Since observers is initialized as an empty set, the loop will not be executed
        sector.occupierChanged();
    }
    
    @Test
    public void testGainControl() {
        // Test case for gainControl() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        
        // Before calling gainControl(), the occupierCount should be 0
        assertEquals(0, sector.occupierCount);
        
        // After calling gainControl(), the occupierCount should be incremented by 1
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(1, sector.occupierCount);
        
        // The occupier should be set to the side passed as parameter
        assertEquals(CombatantSide.ATTACKER, sector.occupier);
    }
    
    @Test
    public void testGetOccupier() {
        // Test case for getOccupier() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        
        // When the occupier is null, getOccupier() should return null
        assertNull(sector.getOccupier());
        
        // After setting the occupier, getOccupier() should return the assigned value
        sector.occupier = CombatantSide.DEFENDER;
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }
    
    @Test
    public void testIsOccupied() {
        // Test case for isOccupied() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        
        // When the occupier is null, isOccupied() should return false
        assertFalse(sector.isOccupied());
        
        // After setting the occupier, isOccupied() should return true
        sector.occupier = CombatantSide.ATTACKER;
        assertTrue(sector.isOccupied());
    }
    
    @Test
    public void testGetHeight() {
        // Test case for getHeight() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        
        // The returned height should be the same as the one passed as parameter
        assertEquals(10, sector.getHeight());
    }
    
    @Test
    public void testIsDefenderSpawn() {
        // Test case for isDefenderSpawn() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        
        // The returned value should be the same as the one passed as parameter
        assertTrue(sector.isDefenderSpawn());
    }
    
    @Test
    public void testIsAttackerSpawn() {
        // Test case for isAttackerSpawn() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        
        // The returned value should be the same as the one passed as parameter
        assertFalse(sector.isAttackerSpawn());
    }
    
    @Test
    public void testIsFlag() {
        // Test case for isFlag() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        
        // The returned value should be the same as the one passed as parameter
        assertFalse(sector.isFlag());
    }
    
    @Test
    public void testGetBonus() {
        // Test case for getBonus() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        
        // The returned SectorBonus should have the expected values
        assertEquals(new SectorBonus(7, 5, 0), sector.getBonus(CharacterClass.WARRIOR));
    }
    
    @Test
    public void testGetDamageBonus() {
        // Test case for getDamageBonus() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        sector.areas.add(new Area());
        sector.areas.add(new Area());
        
        // Since both areas are unoccupied, the return value should be 0
        assertEquals(0, sector.getDamageBonus());
        
        // Setting the weapon sector bonus for the first area to 3
        sector.areas.iterator().next().getOccupier().getWeapon().setSectorBonus(SectorBonus.BonusType.DAMAGE, 3);
        assertEquals(3, sector.getDamageBonus());
    }
    
    @Test
    public void testGetAttackBonus() {
        // Test case for getAttackBonus() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        sector.areas.add(new Area());
        sector.areas.add(new Area());
        
        // The return value should be the sum of attackBonus, classBonus, and the weapon sector bonus
        assertEquals(10, sector.getAttackBonus(CharacterClass.WARRIOR));
    }
    
    @Test
    public void testGetDefenseBonus() {
        // Test case for getDefenseBonus() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        sector.areas.add(new Area());
        sector.areas.add(new Area());
        
        // The return value should be the sum of defendBonus, classBonus, and the weapon sector bonus
        assertEquals(8, sector.getDefenseBonus(CharacterClass.WARRIOR));
    }
    
    @Test
    public void testClassBonus() {
        // Test case for classBonus() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        
        // The return value should be classBonus if the classType matches the parameter, otherwise 0
        assertEquals(0, sector.classBonus(CharacterClass.ARCHER));
        assertEquals(2, sector.classBonus(CharacterClass.WARRIOR));
    }
    
    @Test
    public void testGetAreas() {
        // Test case for getAreas() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        
        // The returned set should be the same as the one defined in the class
        assertEquals(sector.areas, sector.getAreas());
    }
    
    @Test
    public void test_addArea() {
        // Test case for _addArea() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        
        // After calling _addArea(), the area should be added to the areas set
        Area area = new Area();
        sector._addArea(area);
        assertTrue(sector.areas.contains(area));
    }
    
    @Test
    public void testHashCode() {
        // Test case for hashCode() method
        SimpleSector sector1 = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        
        // The hash code should be the same for two objects with the same property values
        assertEquals(sector1.hashCode(), sector2.hashCode());
    }
    
    @Test
    public void testEquals() {
        // Test case for equals() method
        SimpleSector sector1 = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        SimpleSector sector3 = new SimpleSector(5, false, true, 7, 6, true, 3, CharacterClass.ARCHER);
        
        // Two objects with the same property values should be considered equal
        assertTrue(sector1.equals(sector2));
        
        // Two objects with different property values should not be considered equal
        assertFalse(sector1.equals(sector3));
        
        // An object should be considered equal to itself
        assertTrue(sector1.equals(sector1));
        
        // An object should not be considered equal to null
        assertFalse(sector1.equals(null));
        
        // An object should not be considered equal to a different type of object
        assertFalse(sector1.equals("test"));
    }
    
    @Test
    public void testAddObserver() {
        // Test case for addObserver() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        
        // After calling addObserver(), the observer should be added to the observers set
        SectorObserver observer = new SectorObserver() {
            @Override
            public void occupierChanged() {
                // do nothing
            }
        };
        sector.addObserver(observer);
        assertTrue(sector.observers.contains(observer));
    }
    
    @Test
    public void testRemoveObserver() {
        // Test case for removeObserver() method
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        
        // After calling removeObserver(), the observer should be removed from the observers set
        SectorObserver observer = new SectorObserver() {
            @Override
            public void occupierChanged() {
                // do nothing
            }
        };
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertFalse(sector.observers.contains(observer));
    }
}