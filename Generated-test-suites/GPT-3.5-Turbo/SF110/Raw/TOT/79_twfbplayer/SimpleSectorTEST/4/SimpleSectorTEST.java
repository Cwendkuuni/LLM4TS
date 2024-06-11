import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;

public class SimpleSectorTest {
  
    private SimpleSector simpleSector;
    private CombatantSide side;
    private Area area;
  
    @Before
    public void setUp() {
        simpleSector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.KNIGHT);
        side = new CombatantSide();
        area = new Area();
    }

    @Test
    public void testFree_WhenOccupierCountIsGreaterThanZero_DecreaseOccupierCountByOne() {
        simpleSector.gainControl(side);
        simpleSector.free();
        assertEquals(0, simpleSector.getOccupierCount());
    }
  
    @Test
    public void testFree_WhenOccupierCountIsZero_SetOccupierToNull() {
        simpleSector.gainControl(side);
        simpleSector.free();
        assertEquals(null, simpleSector.getOccupier());
    }
  
    @Test
    public void testFree_WhenOccupierCountIsZero_CallOccupierChangedMethodForEachSectorObserver() {
        SectorObserverMock sectorObserver1 = new SectorObserverMock();
        SectorObserverMock sectorObserver2 = new SectorObserverMock();
        simpleSector.addObserver(sectorObserver1);
        simpleSector.addObserver(sectorObserver2);
      
        simpleSector.gainControl(side);
        simpleSector.free();
      
        assertTrue(sectorObserver1.isOccupierChangedCalled());
        assertTrue(sectorObserver2.isOccupierChangedCalled());
    }
  
    @Test
    public void testGainControl_WhenOccupierCountIsZero_IncreaseOccupierCountByOne() {
        simpleSector.gainControl(side);
        assertEquals(1, simpleSector.getOccupierCount());
    }
  
    @Test
    public void testGainControl_WhenOccupierCountIsZero_SetOccupierToGivenSide() {
        simpleSector.gainControl(side);
        assertEquals(side, simpleSector.getOccupier());
    }
  
    @Test
    public void testGainControl_WhenOccupierIsNotEqualToGivenSide_CallOccupierChangedMethodForEachSectorObserver() {
        SectorObserverMock sectorObserver1 = new SectorObserverMock();
        SectorObserverMock sectorObserver2 = new SectorObserverMock();
        simpleSector.addObserver(sectorObserver1);
        simpleSector.addObserver(sectorObserver2);
      
        simpleSector.gainControl(side);
      
        assertTrue(sectorObserver1.isOccupierChangedCalled());
        assertTrue(sectorObserver2.isOccupierChangedCalled());
    }
  
    @Test
    public void testGetOccupier_WhenOccupierIsNull_ReturnNull() {
        assertEquals(null, simpleSector.getOccupier());
    }
  
    @Test
    public void testIsOccupied_WhenOccupierIsNull_ReturnFalse() {
        assertFalse(simpleSector.isOccupied());
    }
  
    @Test
    public void testIsOccupied_WhenOccupierIsNotNull_ReturnTrue() {
        simpleSector.gainControl(side);
        assertTrue(simpleSector.isOccupied());
    }
  
    @Test
    public void testGetHeight_WhenCalled_ReturnHeight() {
        assertEquals(10, simpleSector.getHeight());
    }
  
    @Test
    public void testIsDefenderSpawn_WhenDefenderSpawnIsTrue_ReturnTrue() {
        assertTrue(simpleSector.isDefenderSpawn());
    }
  
    @Test
    public void testIsDefenderSpawn_WhenDefenderSpawnIsFalse_ReturnFalse() {
        SimpleSector anotherSimpleSector = new SimpleSector(10, false, false, 5, 3, true, 2, CharacterClass.KNIGHT);
        assertFalse(anotherSimpleSector.isDefenderSpawn());
    }
  
    @Test
    public void testIsAttackerSpawn_WhenAttackerSpawnIsTrue_ReturnTrue() {
        assertTrue(simpleSector.isAttackerSpawn());
    }
  
    @Test
    public void testIsAttackerSpawn_WhenAttackerSpawnIsFalse_ReturnFalse() {
        SimpleSector anotherSimpleSector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.KNIGHT);
        assertFalse(anotherSimpleSector.isAttackerSpawn());
    }
  
    @Test
    public void testIsFlag_WhenFlagIsTrue_ReturnTrue() {
        assertTrue(simpleSector.isFlag());
    }
  
    @Test
    public void testIsFlag_WhenFlagIsFalse_ReturnFalse() {
        SimpleSector anotherSimpleSector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.KNIGHT);
        assertFalse(anotherSimpleSector.isFlag());
    }
    
    @Test
    public void testGetBonus_WhenCalledWithValidCharacterClass_ReturnValidSectorBonus() {
        SectorBonus expected = new SectorBonus(10, 3, 5);
        assertEquals(expected, simpleSector.getBonus(CharacterClass.KNIGHT));
    }
  
    @Test
    public void testGetArea_WhenNoAreaIsAdded_ReturnEmptySet() {
        Set<Area> areas = simpleSector.getAreas();
        assertTrue(areas.isEmpty());
    }
  
    @Test
    public void testAddArea_WhenAreaIsAdded_ReturnSetContainingAddedArea() {
        simpleSector._addArea(area);
        Set<Area> areas = simpleSector.getAreas();
        assertTrue(areas.contains(area));
    }
  
    @Test
    public void testHashCode_WhenTwoSectorsAreEqual_ReturnSameHashCode() {
        SimpleSector anotherSimpleSector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.KNIGHT);
        assertEquals(simpleSector.hashCode(), anotherSimpleSector.hashCode());
    }
  
    @Test
    public void testEquals_WhenTwoSectorsAreEqual_ReturnTrue() {
        SimpleSector anotherSimpleSector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.KNIGHT);
        assertTrue(simpleSector.equals(anotherSimpleSector));
    }
  
    @Test
    public void testEquals_WhenTwoSectorsAreNotEqual_ReturnFalse() {
        SimpleSector anotherSimpleSector = new SimpleSector(12, true, false, 5, 3, true, 2, CharacterClass.KNIGHT);
        assertFalse(simpleSector.equals(anotherSimpleSector));
    }
  
    @Test
    public void testAddObserver_WhenObserverIsAddedToSector_ReturnObserverInList() {
        SectorObserverMock sectorObserver = new SectorObserverMock();
        simpleSector.addObserver(sectorObserver);
      
        assertEquals(sectorObserver, simpleSector.getObservers().get(0));
    }
  
    @Test
    public void testRemoveObserver_WhenObserverIsRemovedFromSector_RemoveObserverFromList() {
        SectorObserverMock sectorObserver = new SectorObserverMock();
        simpleSector.addObserver(sectorObserver);
        simpleSector.removeObserver(sectorObserver);
      
        assertTrue(simpleSector.getObservers().isEmpty());
    }
  
    private static class SectorObserverMock implements SectorObserver {
      
        private boolean isOccupierChangedCalled;
      
        public SectorObserverMock() {
            isOccupierChanged = false;
        }
      
        public boolean isOccupierChangedCalled() {
            return isOccupierChanged;
        }
      
        @Override
        public void occupierChanged() {
            isOccupierChanged = true;
        }
    }
}