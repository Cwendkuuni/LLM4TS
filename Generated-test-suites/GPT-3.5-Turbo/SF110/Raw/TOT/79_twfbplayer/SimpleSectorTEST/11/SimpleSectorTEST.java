import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import de.outstare.fortbattleplayer.model.*;

public class SimpleSectorTest {
    
    private SimpleSector simpleSector;
    
    @Before
    public void setup() {
        simpleSector = new SimpleSector(5, true, false, 10, 5, true, 3, CharacterClass.WARRIOR);
    }
    
    @Test
    public void testFree_withOccupierCountGreaterThanZero() {
        // Arrange
        simpleSector.gainControl(CombatantSide.ATTACKER);
        simpleSector.gainControl(CombatantSide.DEFENDER);
        
        // Act
        simpleSector.free();
        
        // Assert
        assertFalse(simpleSector.isOccupied());
    }
    
    @Test
    public void testFree_withOccupierCountEqualToZero() {
        // Arrange
        simpleSector.gainControl(CombatantSide.ATTACKER);
        
        // Act
        simpleSector.free();
        
        // Assert
        assertTrue(simpleSector.isOccupied());
    }
    
    @Test
    public void testGainControl_withDifferentOccupier() {
        // Arrange
        
        // Act
        simpleSector.gainControl(CombatantSide.ATTACKER);
        CombatantSide occupier = simpleSector.getOccupier();
        
        // Assert
        assertEquals(CombatantSide.ATTACKER, occupier);
    }
    
    @Test
    public void testGainControl_withSameOccupier() {
        // Arrange
        simpleSector.gainControl(CombatantSide.ATTACKER);
        
        // Act
        simpleSector.gainControl(CombatantSide.ATTACKER);
        CombatantSide occupier = simpleSector.getOccupier();
        
        // Assert
        assertEquals(CombatantSide.ATTACKER, occupier);
    }
    
    @Test
    public void testGetOccupier_whenOccupied() {
        // Arrange
        simpleSector.gainControl(CombatantSide.DEFENDER);
        
        // Act
        CombatantSide occupier = simpleSector.getOccupier();
        
        // Assert
        assertEquals(CombatantSide.DEFENDER, occupier);
    }
    
    @Test
    public void testGetOccupier_whenNotOccupied() {
        // Arrange
        
        // Act
        CombatantSide occupier = simpleSector.getOccupier();
        
        // Assert
        assertNull(occupier);
    }
    
    @Test
    public void testIsOccupied_whenOccupied() {
        // Arrange
        simpleSector.gainControl(CombatantSide.DEFENDER);
        
        // Act
        boolean isOccupied = simpleSector.isOccupied();
        
        // Assert
        assertTrue(isOccupied);
    }
    
    @Test
    public void testIsOccupied_whenNotOccupied() {
        // Arrange
        
        // Act
        boolean isOccupied = simpleSector.isOccupied();
        
        // Assert
        assertFalse(isOccupied);
    }
    
    @Test
    public void testGetHeight() {
        // Arrange
        
        // Act
        int height = simpleSector.getHeight();
        
        // Assert
        assertEquals(5, height);
    }
    
    @Test
    public void testIsDefenderSpawn_whenTrue() {
        // Arrange
        
        // Act
        boolean isDefenderSpawn = simpleSector.isDefenderSpawn();
        
        // Assert
        assertTrue(isDefenderSpawn);
    }
    
    @Test
    public void testIsDefenderSpawn_whenFalse() {
        // Arrange
        SimpleSector sector = new SimpleSector(5, false, false, 10, 5, true, 3, CharacterClass.WARRIOR);
        
        // Act
        boolean isDefenderSpawn = sector.isDefenderSpawn();
        
        // Assert
        assertFalse(isDefenderSpawn);
    }
    
    @Test
    public void testIsAttackerSpawn_whenTrue() {
        // Arrange
        
        // Act
        boolean isAttackerSpawn = simpleSector.isAttackerSpawn();
        
        // Assert
        assertFalse(isAttackerSpawn);
    }
    
    @Test
    public void testIsAttackerSpawn_whenFalse() {
        // Arrange
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.WARRIOR);
        
        // Act
        boolean isAttackerSpawn = sector.isAttackerSpawn();
        
        // Assert
        assertTrue(isAttackerSpawn);
    }
    
    @Test
    public void testIsFlag_whenTrue() {
        // Arrange
        
        // Act
        boolean isFlag = simpleSector.isFlag();
        
        // Assert
        assertTrue(isFlag);
    }
    
    @Test
    public void testIsFlag_whenFalse() {
        // Arrange
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 3, CharacterClass.WARRIOR);
        
        // Act
        boolean isFlag = sector.isFlag();
        
        // Assert
        assertFalse(isFlag);
    }
    
    @Test
    public void testGetBonus() {
        // Arrange
        
        // Act
        SectorBonus bonus = simpleSector.getBonus(CharacterClass.WARRIOR);
        
        // Assert
        assertNotNull(bonus);
    }
    
    @Test
    public void testGetAttackBonus() {
        // Arrange
        
        // Act
        int attackBonus = simpleSector.getAttackBonus(CharacterClass.WARRIOR);
        
        // Assert
        assertEquals(13, attackBonus);
    }
    
    @Test
    public void testGetDefenseBonus() {
        // Arrange
        
        // Act
        int defenseBonus = simpleSector.getDefenseBonus(CharacterClass.WARRIOR);
        
        // Assert
        assertEquals(8, defenseBonus);
    }
    
    @Test
    public void testGetAreas() {
        // Arrange
        Area area1 = new Area();
        Area area2 = new Area();
        simpleSector._addArea(area1);
        simpleSector._addArea(area2);
        
        // Act
        Set<Area> areas = simpleSector.getAreas();
        
        // Assert
        assertEquals(2, areas.size());
        assertTrue(areas.contains(area1));
        assertTrue(areas.contains(area2));
    }
    
    @Test
    public void test_addArea() {
        // Arrange
        Area area = new Area();
        
        // Act
        simpleSector._addArea(area);
        
        // Assert
        assertTrue(simpleSector.getAreas().contains(area));
    }
    
    @Test
    public void testHashCode_withSameSimpleSectorObjects() {
        // Arrange
        SimpleSector sector1 = new SimpleSector(5, true, false, 10, 5, true, 3, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(5, true, false, 10, 5, true, 3, CharacterClass.WARRIOR);
        
        // Act
        int hashCode1 = sector1.hashCode();
        int hashCode2 = sector2.hashCode();
        
        // Assert
        assertEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testEquals_withSameSimpleSectorObjects() {
        // Arrange
        SimpleSector sector1 = new SimpleSector(5, true, false, 10, 5, true, 3, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(5, true, false, 10, 5, true, 3, CharacterClass.WARRIOR);
        
        // Act
        boolean equals = sector1.equals(sector2);
        
        // Assert
        assertTrue(equals);
    }
    
    @Test
    public void testEquals_withDifferentSimpleSectorObjects() {
        // Arrange
        SimpleSector sector1 = new SimpleSector(5, true, false, 10, 5, true, 3, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(6, true, false, 10, 5, true, 3, CharacterClass.WARRIOR);
        
        // Act
        boolean equals = sector1.equals(sector2);
        
        // Assert
        assertFalse(equals);
    }
    
    @Test
    public void testAddObserver() {
        // Arrange
        TestSectorObserver observer = new TestSectorObserver();
        
        // Act
        simpleSector.addObserver(observer);
        
        // Assert
        assertTrue(simpleSector.observers.contains(observer));
    }
    
    @Test
    public void testRemoveObserver() {
        // Arrange
        TestSectorObserver observer = new TestSectorObserver();
        simpleSector.addObserver(observer);
        
        // Act
        simpleSector.removeObserver(observer);
        
        // Assert
        assertFalse(simpleSector.observers.contains(observer));
    }
    
    private static class TestSectorObserver implements SectorObserver {
        
        public void occupierChanged() {
            // Do nothing
        }
        
    }

}