import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimpleSectorTest {
    
    private SimpleSector sector;
    
    @Before
    public void setup() {
        sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    }
    
    @Test
    public void testFree() {
        // Test when occupierCount is greater than 0
        sector.occupierCount = 1;
        sector.free();
        Assert.assertEquals(0, sector.occupierCount);
        
        // Test when occupierCount is 0
        sector.free();
        Assert.assertEquals(0, sector.occupierCount);
        Assert.assertNull(sector.occupier);
    }
    
    @Test
    public void testGainControl() {
        // Test when occupier is different from side
        sector.occupierCount = 0;
        sector.gainControl(CombatantSide.ATTACKER);
        Assert.assertEquals(1, sector.occupierCount);
        Assert.assertEquals(CombatantSide.ATTACKER, sector.occupier);
        
        // Test when occupier is the same as side
        sector.gainControl(CombatantSide.ATTACKER);
        Assert.assertEquals(2, sector.occupierCount);
    }
    
    @Test
    public void testGetOccupier() {
        // Test when occupier is null
        CombatantSide occupier = sector.getOccupier();
        Assert.assertNull(occupier);
        
        // Test when occupier is not null
        sector.occupier = CombatantSide.DEFENDER;
        occupier = sector.getOccupier();
        Assert.assertEquals(CombatantSide.DEFENDER, occupier);
    }
    
    @Test
    public void testIsOccupied() {
        // Test when occupier is null
        boolean occupied = sector.isOccupied();
        Assert.assertFalse(occupied);
        
        // Test when occupier is not null
        sector.occupier = CombatantSide.DEFENDER;
        occupied = sector.isOccupied();
        Assert.assertTrue(occupied);
    }
    
    @Test
    public void testGetHeight() {
        int height = sector.getHeight();
        Assert.assertEquals(10, height);
    }
    
    @Test
    public void testIsDefenderSpawn() {
        boolean defenderSpawn = sector.isDefenderSpawn();
        Assert.assertTrue(defenderSpawn);
    }
    
    @Test
    public void testIsAttackerSpawn() {
        boolean attackerSpawn = sector.isAttackerSpawn();
        Assert.assertFalse(attackerSpawn);
    }
    
    @Test
    public void testIsFlag() {
        boolean flag = sector.isFlag();
        Assert.assertTrue(flag);
    }
    
    @Test
    public void testGetBonus() {
        SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
        Assert.assertEquals(7, bonus.getAttackBonus());
        Assert.assertEquals(5, bonus.getDefenseBonus());
        Assert.assertEquals(0, bonus.getDamageBonus());
    }
    
    @Test
    public void testGetAreas() {
        Set<Area> areas = sector.getAreas();
        Assert.assertNotNull(areas);
        Assert.assertEquals(0, areas.size());
    }
    
    @Test
    public void testAddArea() {
        Area area = new Area();
        sector._addArea(area);
        Set<Area> areas = sector.getAreas();
        Assert.assertEquals(1, areas.size());
        Assert.assertTrue(areas.contains(area));
    }
    
    @Test
    public void testHashCode() {
        int hashCode = sector.hashCode();
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testEquals() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        boolean equals = sector.equals(other);
        Assert.assertTrue(equals);
    }
    
    @Test
    public void testAddObserver() {
        SectorObserver observer = new SectorObserver() {
            @Override
            public void occupierChanged() {
                
            }
        };
        sector.addObserver(observer);
        Set<SectorObserver> observers = sector.observers;
        Assert.assertEquals(1, observers.size());
        Assert.assertTrue(observers.contains(observer));
    }
    
    @Test
    public void testRemoveObserver() {
        SectorObserver observer = new SectorObserver() {
            @Override
            public void occupierChanged() {
                
            }
        };
        sector.removeObserver(observer);
        Set<SectorObserver> observers = sector.observers;
        Assert.assertEquals(0, observers.size());
        Assert.assertFalse(observers.contains(observer));
    }
}